package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.Session;

import net.devaction.entity.AccountBalanceEntity;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface AccountBalanceSender{

    public void send(AccountBalanceEntity accountBalance, Session session);
}
