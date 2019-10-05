package net.devaction.kafka.accountbalanceconsumer;

import net.devaction.kafka.avro.AccountBalance;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public interface AccountBalanceProcessor{

    public void process(AccountBalance accountBalance);
}
