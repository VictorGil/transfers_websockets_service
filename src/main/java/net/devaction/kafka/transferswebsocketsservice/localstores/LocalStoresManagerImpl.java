package net.devaction.kafka.transferswebsocketsservice.localstores;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.KafkaStreams.State;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
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
import net.devaction.entity.AccountBalanceEntity;
import net.devaction.entity.TransferEntity;
import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.avro.util.AccountBalanceConverter;
import net.devaction.kafka.avro.util.TransferConverter;
import net.devaction.kafka.streams.ExceptionHandler;
import net.devaction.kafka.transferswebsocketsservice.transferretriever.DummyReducer;
import net.devaction.kafka.transferswebsocketsservice.transferretriever.TransferIdKeyValueMapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class LocalStoresManagerImpl implements LocalStoresManager {
    private static final Logger log = LoggerFactory.getLogger(LocalStoresManagerImpl.class);

    private ReadOnlyKeyValueStore<String, AccountBalance> balancesStore;
    private ReadOnlyKeyValueStore<String, Transfer> transfersStore;

    private KafkaStreams streams;

    private static final String ACCOUNT_BALANCES_TOPIC = "account-balances";
    private static final String TRANSFERS_TOPIC = "transfers";

    private static final String ACCOUNT_BALANCES_STORE = "account-balances-store";
    private static final String TRANSFERS_STORE = "transfers-store";

    @Override
    public void start(String bootstrapServers, String schemaRegistryUrl) {
        StreamsBuilder builder = new StreamsBuilder();

        KTable<String,AccountBalance> accountBalancesKTable = setUpBalancesStore(
                schemaRegistryUrl, builder);

        KTable<String, Transfer> transfersKTable = setUpTransfersStore(
                schemaRegistryUrl, builder);

        final Properties streamsConfigProperties = new Properties();
        streamsConfigProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "websockets-server");
        streamsConfigProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfigProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        streams = new KafkaStreams(builder.build(), streamsConfigProperties);

        streams.setUncaughtExceptionHandler(new ExceptionHandler());
        streams.start();

        while (streams.state() != State.RUNNING) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                log.error("Interrupted while waiting for the \"Streams\" to start.", ex);
                Thread.currentThread().interrupt();
            }
        }

        balancesStore = streams.store(accountBalancesKTable.queryableStoreName(),
                QueryableStoreTypes.<String,AccountBalance>keyValueStore());

        transfersStore = streams.store(transfersKTable.queryableStoreName(),
                QueryableStoreTypes.<String,Transfer>keyValueStore());
    }

    private KTable<String,AccountBalance> setUpBalancesStore(String schemaRegistryUrl,
            StreamsBuilder builder) {

        KeyValueBytesStoreSupplier accountBalanceStoreSupplier =
                Stores.inMemoryKeyValueStore(ACCOUNT_BALANCES_STORE);

        final Serde<String> stringSerde = Serdes.String();
        final Serde<AccountBalance> accountBalanceSerde = new SpecificAvroSerde<>();

        final boolean isKeySerde = false;
        accountBalanceSerde.configure(
                Collections.singletonMap(
                        AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                        schemaRegistryUrl),
                isKeySerde);

        return builder.table(
                ACCOUNT_BALANCES_TOPIC,
                Materialized.<String,AccountBalance>as(accountBalanceStoreSupplier)
                        .withKeySerde(stringSerde)
                        .withValueSerde(accountBalanceSerde)
                        .withCachingDisabled());
    }

    private KTable<String,Transfer> setUpTransfersStore(String schemaRegistryUrl,
            StreamsBuilder builder) {

        KeyValueBytesStoreSupplier transferStoreSupplier =
                Stores.inMemoryKeyValueStore(TRANSFERS_STORE);

        final Serde<String> stringSerde = Serdes.String();
        final Serde<Transfer> transferSerde = new SpecificAvroSerde<>();

        final boolean isKeySerde = false;
        transferSerde.configure(
                Collections.singletonMap(
                        AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                        schemaRegistryUrl),
                isKeySerde);

        KStream<String, Transfer> accountIdKeyTransfersStream = builder.stream(TRANSFERS_TOPIC,
                Consumed.with(stringSerde, transferSerde));

        // We are "re-keying" the data from the "transfers" topic
        KGroupedStream<String, Transfer> groupStream = accountIdKeyTransfersStream.groupBy(
                new TransferIdKeyValueMapper(), Grouped.with(stringSerde, transferSerde));

        return groupStream.reduce(
                new DummyReducer(),
                Materialized.<String,Transfer>as(transferStoreSupplier)
                        .withKeySerde(stringSerde)
                        .withValueSerde(transferSerde)
                        .withCachingDisabled());
    }

    @Override
    public AccountBalanceEntity getBalance(String accountId) {
        AccountBalance accountBalance = balancesStore.get(accountId);

        if (accountBalance == null) {
            log.error("The account with id {} has not been set up", accountId);
            return AccountBalanceEntity.createNAobject(accountId);
        }

        return AccountBalanceConverter.convertToPojo(accountBalance);
    }

    @Override
    public TransferEntity getTransfer(String transferId) {

        Transfer transfer = transfersStore.get(transferId);

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
