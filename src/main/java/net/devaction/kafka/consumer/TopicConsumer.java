package net.devaction.kafka.consumer;

import org.apache.avro.specific.SpecificRecord;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TopicConsumer<T extends SpecificRecord> {

    public void start();

    public void stop();

}
