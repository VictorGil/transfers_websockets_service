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
public class TopicConsumerRunner<T extends SpecificRecord> {
    private static final Logger log = LoggerFactory.getLogger(TopicConsumerRunner.class);

    private final TopicConsumer<T> topicConsumer;

    public TopicConsumerRunner(TopicConsumer<T> topicConsumer) {
        this.topicConsumer = topicConsumer;
    }

    public void start() {
        TopicConsumerRunnable<T> runnable = new TopicConsumerRunnable<T>(topicConsumer);
        Thread thread = new Thread(runnable);
        thread.setName(topicConsumer.getClass().getSimpleName() + "-thread");
        thread.start();
    }

    /*
    public void stop() {
        log.info("We have been told to stop the {}", TopicConsumer.class.getSimpleName());
        topicConsumer.stop();
    }
    */
}
