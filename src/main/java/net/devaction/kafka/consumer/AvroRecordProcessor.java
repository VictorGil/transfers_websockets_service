package net.devaction.kafka.consumer;

import org.apache.avro.specific.SpecificRecord;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface AvroRecordProcessor<T extends SpecificRecord> {

    public void process(T record);
}
