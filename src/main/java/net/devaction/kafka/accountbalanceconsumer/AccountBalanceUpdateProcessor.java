package net.devaction.kafka.accountbalanceconsumer;

import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.consumer.AvroRecordProcessor;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public interface AccountBalanceUpdateProcessor extends AvroRecordProcessor<AccountBalance> {
}
