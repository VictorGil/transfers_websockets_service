package net.devaction.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TopicConsumerImpl<T extends SpecificRecord> implements TopicConsumer<T> {
    private static final Logger log = LoggerFactory.getLogger(TopicConsumerImpl.class);

    private Consumer<String, T> consumer;

    private final AvroRecordProcessor<T> processor;

    private final String bootstrapServers;
    private final String schemaRegistryUrl;

    private final String topic;

    private final boolean seekFromBeginning;

    private volatile boolean stop;

    public TopicConsumerImpl(String topic, String bootstrapServers, String schemaRegistryUrl,
            AvroRecordProcessor<T> processor, boolean seekFromBeginning) {

        this.topic = topic;
        this.bootstrapServers = bootstrapServers;
        this.schemaRegistryUrl = schemaRegistryUrl;
        this.processor = processor;
        this.seekFromBeginning = seekFromBeginning;
    }

    @Override
    public void start() {
        final Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, topic + "-consumer-group-01");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, topic + "-consumer-client-01");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        props.setProperty("enable.auto.commit", "true");

        consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(topic));

        List<PartitionInfo> partitionsInfo = consumer.partitionsFor(topic);
        log.info("Partitions for \"{}\" topic: {}", topic, partitionsInfo);

        seekFromBeginningIfRequired();

        stop = false;

        log.info("\"{}\" topic consumer started", topic);
        while (!stop) {
            poll();
        }

        log.info("Going to close the \"{}\" topic Kafka consumer.", topic);
        consumer.close();
    }

    void poll() {
        log.trace("Going to poll for messages.");

        ConsumerRecords<String, T> records =
                consumer.poll(Duration.ofMillis(100));

        if (!records.isEmpty()) {
            log.debug("Number of records polled: {}", records.count());
        }

        for (ConsumerRecord<String, T> record: records) {
            processor.process(record.value());
        }
    }

    @Override
    public void stop() {
        log.info("We have been told to stop.");
        stop = true;
    }

    private void seekFromBeginningIfRequired() {
        if (seekFromBeginning) {
            seekFromBeginning();
        }
    }

    private void seekFromBeginning() {
        while (consumer.assignment().isEmpty()) {
            log.trace("Going to perform a dummy poll");
            consumer.poll(Duration.ofMillis(100));
        }

        consumer.seekToBeginning(consumer.assignment());
    }
}
