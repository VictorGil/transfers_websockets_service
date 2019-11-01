package net.devaction.kafka.transferconsumer;

import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.consumer.AvroRecordProcessor;
import net.devaction.kafka.consumer.TopicConsumerImpl;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferConsumerImpl extends TopicConsumerImpl<Transfer> implements TransferConsumer {
    private static final String TOPIC = "transfers";

    public TransferConsumerImpl(String bootstrapServers, String schemaRegistryUrl,
            AvroRecordProcessor<Transfer> processor) {

        super(TOPIC, bootstrapServers, schemaRegistryUrl, processor, true);
    }
}
