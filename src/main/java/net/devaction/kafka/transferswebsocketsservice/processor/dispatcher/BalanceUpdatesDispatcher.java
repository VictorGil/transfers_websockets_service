package net.devaction.kafka.transferswebsocketsservice.processor.dispatcher;

import javax.websocket.Session;

import net.devaction.entity.AccountBalanceEntity;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public interface BalanceUpdatesDispatcher {

    public void addSession(String accountId, Session session);

    public void dispatch(AccountBalanceEntity balance);
}
