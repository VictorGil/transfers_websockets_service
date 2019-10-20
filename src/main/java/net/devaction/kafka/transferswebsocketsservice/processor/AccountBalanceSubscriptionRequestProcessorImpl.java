package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceSubscriptionRequest;
import net.devaction.kafka.transferswebsocketsservice.processor.balanceupdatesproducer.BalanceUpdatesDispatcher;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class AccountBalanceSubscriptionRequestProcessorImpl
        implements AccountBalanceSubscriptionRequestProcessor {

    private final BalanceUpdatesDispatcher updatesDispatcher;

    public AccountBalanceSubscriptionRequestProcessorImpl(BalanceUpdatesDispatcher updatesDispatcher) {
        this.updatesDispatcher = updatesDispatcher;
    }

    @Override
    public void process(AccountBalanceSubscriptionRequest request, Session session) {
        updatesDispatcher.addSession(request.getAccountId(), session);
    }
}
