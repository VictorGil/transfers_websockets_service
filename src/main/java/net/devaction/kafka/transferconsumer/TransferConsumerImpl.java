package net.devaction.kafka.transferconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.consumer.AvroRecordProcessor;
import net.devaction.kafka.consumer.TopicConsumerImpl;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferConsumerImpl extends TopicConsumerImpl<Transfer> implements TransferConsumer {
    private static final Logger log = LoggerFactory.getLogger(TransferConsumerImpl.class);

    private static final String TOPIC = "transfers";

    public TransferConsumerImpl(String bootstrapServers, String schemaRegistryUrl,
            AvroRecordProcessor<Transfer> processor) {

        super(TOPIC, bootstrapServers, schemaRegistryUrl, processor, true);
    }
}
