package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceSubscriptionRequest;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface AccountBalanceSubscriptionRequestProcessor {

    void process(AccountBalanceSubscriptionRequest request, Session session);
}
