package net.devaction.kafka.accountbalanceconsumer;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.avro.util.AccountBalanceConverter;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.BalanceUpdatesDispatcher;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class AccountBalanceUpdateProcessorImpl implements AccountBalanceUpdateProcessor {

    private final BalanceUpdatesDispatcher updatesDispatcher;

    public AccountBalanceUpdateProcessorImpl(BalanceUpdatesDispatcher updatesDispatcher) {
        this.updatesDispatcher = updatesDispatcher;
    }

    @Override
    public void process(AccountBalance accountBalance) {
        AccountBalanceEntity abEntity = AccountBalanceConverter.convertToPojo(accountBalance);
        updatesDispatcher.dispatch(abEntity);
    }
}
