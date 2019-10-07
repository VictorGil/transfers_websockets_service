package net.devaction.kafka.accountbalanceconsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import net.devaction.kafka.avro.AccountBalance;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public class AccountBalanceConsumer {

    private static final Logger log = LoggerFactory.getLogger(AccountBalanceConsumer.class);

    private Consumer<String, AccountBalance> consumer;

    private final AccountBalanceUpdateProcessor processor;

    private final String bootstrapServers;
    private final String schemaRegistryUrl;

    private volatile boolean stop;
    private boolean seekFromBeginning;

    private static final String TOPIC = "account-balances";

    public AccountBalanceConsumer(String bootstrapServers, String schemaRegistryUrl,
            AccountBalanceUpdateProcessor processor) {

        this.bootstrapServers = bootstrapServers;
        this.schemaRegistryUrl = schemaRegistryUrl;
        this.processor = processor;
    }

    public void start() {
        final Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "accountBalances-consumer-group-01");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "accountBalances-consumer-client-01");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        props.setProperty("enable.auto.commit", "true");

        consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(TOPIC));

        List<PartitionInfo> partitionsInfo = consumer.partitionsFor(TOPIC);
        log.info("Partitions for \"{}\" topic: {}", TOPIC, partitionsInfo);

        seekFromBeginningIfRequired();

        stop = false;

        log.info("\"{}\" topic consumer started", TOPIC);
        while (!stop) {
            poll();
        }

        log.info("Going to close the \"{}\" topic Kafka consumer.", TOPIC);
        consumer.close();
    }

    void poll() {
        log.trace("Going to poll for messages.");

        ConsumerRecords<String, AccountBalance> records =
                consumer.poll(Duration.ofMillis(100));

        if (!records.isEmpty())
            log.debug("Number of \"{}\" records polled: {}",
                    AccountBalance.class.getSimpleName(), records.count());

        for (ConsumerRecord<String, AccountBalance> record: records) {
            processor.process(record.value());
        }
    }

    private void seekFromBeginningIfRequired() {
        if (seekFromBeginning)
            seekFromBeginning();
    }

    private void seekFromBeginning() {
        while (consumer.assignment().isEmpty()) {
            log.trace("Going to perform a dummy poll");
            consumer.poll(Duration.ofMillis(100));
        }

        consumer.seekToBeginning(consumer.assignment());
    }

    public void stop() {
        log.info("We have been told to stop.");
        stop = true;
    }

    public void setSeekFromBeginningOn() {
        seekFromBeginning = true;
    }
}
