package net.devaction.kafka.transferconsumer;

import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.consumer.TopicConsumer;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransferConsumer extends TopicConsumer<Transfer> {
}
