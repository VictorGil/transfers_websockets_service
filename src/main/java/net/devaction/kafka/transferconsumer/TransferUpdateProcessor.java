package net.devaction.kafka.transferconsumer;

import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.consumer.AvroRecordProcessor;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransferUpdateProcessor extends AvroRecordProcessor<Transfer> {
}
