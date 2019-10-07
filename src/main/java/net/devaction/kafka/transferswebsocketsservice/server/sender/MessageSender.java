package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface MessageSender {

    void send(MessageWrapper message, Session session);
}
