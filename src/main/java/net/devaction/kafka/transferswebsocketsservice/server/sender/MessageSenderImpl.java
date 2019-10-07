package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.SendHandler;
import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageSenderImpl implements MessageSender {

    private final SendHandler handler = new AsyncSendHandler();

    @Override
    public void send(MessageWrapper message, Session session) {
        session.getAsyncRemote().sendObject(message, handler);
    }
}
