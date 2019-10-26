package net.devaction.kafka.accountbalanceconsumer.runnable;

import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.consumer.TopicConsumer;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TopicConsumerRunnable<T extends SpecificRecord> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(TopicConsumerRunnable.class);

    private final TopicConsumer<T> topicConsumer;

    public TopicConsumerRunnable(TopicConsumer<T> topicConsumer) {
        this.topicConsumer = topicConsumer;
    }

    @Override
    public void run() {
        log.info("Going to start the {}", TopicConsumer.class.getSimpleName());
        topicConsumer.start();
    }
}
