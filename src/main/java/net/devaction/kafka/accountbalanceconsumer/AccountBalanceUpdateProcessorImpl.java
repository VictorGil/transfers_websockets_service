package net.devaction.kafka.accountbalanceconsumer;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.avro.AccountBalance;
import net.devaction.kafka.avro.util.AccountBalanceConverter;
import net.devaction.kafka.transferswebsocketsservice.processor.balanceupdatesproducer.UpdatesDispatcher;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class AccountBalanceUpdateProcessorImpl implements AccountBalanceUpdateProcessor {

    private final UpdatesDispatcher updatesDispatcher;

    public AccountBalanceUpdateProcessorImpl(UpdatesDispatcher updatesDispatcher) {
        this.updatesDispatcher = updatesDispatcher;
    }

    @Override
    public void process(AccountBalance accountBalance) {
        AccountBalanceEntity abEntity = AccountBalanceConverter.convertToPojo(accountBalance);
        updatesDispatcher.dispatch(abEntity);
    }
}
