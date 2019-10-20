package net.devaction.kafka.accountbalanceconsumer;

import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.consumer.AvroRecordProcessor;
import net.devaction.kafka.consumer.TopicConsumerImpl;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public class AccountBalanceConsumerImpl extends TopicConsumerImpl<AccountBalance>
        implements AccountBalanceConsumer {

    private static final String TOPIC = "account-balances";

    public AccountBalanceConsumerImpl(String bootstrapServers, String schemaRegistryUrl,
            AvroRecordProcessor<AccountBalance> processor) {

        super(TOPIC, bootstrapServers, schemaRegistryUrl, processor, false);
    }
}
