package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.AccountBalanceRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface AccountBalanceRequestProcessor{

    void process(AccountBalanceRequest request, Session session);

}
