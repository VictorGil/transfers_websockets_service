package net.devaction.kafka.transferswebsocketsservice.transferretriever;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import net.devaction.entity.TransferEntity;
import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.avro.util.TransferConverter;
import net.devaction.kafka.streams.ExceptionHandler;

import org.apache.kafka.streams.KafkaStreams.State;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferRetrieverImpl implements TransferRetriever{
    private static final Logger log = LoggerFactory.getLogger(TransferRetrieverImpl.class);

    private ReadOnlyKeyValueStore<String, Transfer> store;
    
    private KafkaStreams streams;

    private static final String TRANSFERS_TOPIC = "transfers"; 
    
    @Override
    public void start(String bootstrapServers, String schemaRegistryUrl) {
        
        final Properties streamsConfigProperties = new Properties();
        streamsConfigProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "transfer-retriever");
        streamsConfigProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfigProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        KeyValueBytesStoreSupplier transferSupplier = 
                Stores.inMemoryKeyValueStore("transfer-store");
        
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Transfer> transferSerde = new SpecificAvroSerde<>();
        
        final boolean isKeySerde = false;
        transferSerde.configure(
                Collections.singletonMap(
                        AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, 
                        schemaRegistryUrl),
                isKeySerde);

        StreamsBuilder builder = new StreamsBuilder(); 
        
        KStream<String, Transfer> accountIdKeyStream = builder.stream(TRANSFERS_TOPIC);
        accountIdKeyStream.selectKey(new TransferIdKeyValueMapper());
        
        KTable<String, Transfer> transfersKTable = accountIdKeyStream.groupByKey().reduce(
                new DummyReducer(), 
                Materialized.<String,Transfer>as(transferSupplier)
                        .withKeySerde(stringSerde)
                        .withValueSerde(transferSerde)
                        .withCachingDisabled());
        
        streams = new KafkaStreams(builder.build(), streamsConfigProperties);
        streams.setUncaughtExceptionHandler(new ExceptionHandler());
        streams.start();       

        while (streams.state() != State.RUNNING) {
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex){
                log.error("Interrupted while waiting for the \"Streams\" to start.", ex);
                Thread.currentThread().interrupt();
            }
        }
        
        store = streams.store(transfersKTable.queryableStoreName(), 
                QueryableStoreTypes.<String,Transfer>keyValueStore());                
    }
    
    @Override
    public TransferEntity retrieve(String transferId){

        Transfer transfer = store.get(transferId);
        
        if (transfer == null) {
            log.error("Transfer with id {} does not exist.", transferId);
            return null;
        }
        
        return TransferConverter.convertToPojo(transfer);
    }
    
    @Override
    public void stop() {
        log.info("We have been told to stop, closing the \"Streams\"");
        streams.close();
    }
}
