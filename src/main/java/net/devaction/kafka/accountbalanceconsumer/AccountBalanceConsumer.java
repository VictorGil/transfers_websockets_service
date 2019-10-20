package net.devaction.kafka.accountbalanceconsumer;

import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.consumer.TopicConsumer;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public interface AccountBalanceConsumer extends TopicConsumer<AccountBalance> {
}
