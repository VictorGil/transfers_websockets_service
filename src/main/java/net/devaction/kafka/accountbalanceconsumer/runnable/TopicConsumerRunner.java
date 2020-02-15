package net.devaction.kafka.accountbalanceconsumer.runnable;

import org.apache.avro.specific.SpecificRecord;

import net.devaction.kafka.consumer.TopicConsumer;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TopicConsumerRunner<T extends SpecificRecord> {
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
}
