package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.SendHandler;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageSenderImpl implements MessageSender {

    private final SendHandler handler = new AsyncSendHandler();

    private static final Logger log = LoggerFactory.getLogger(MessageSenderImpl.class);

    @Override
    public void send(MessageWrapper message, Session session) {

        try {
            session.getAsyncRemote().sendObject(message, handler);
        } catch (IllegalStateException | NullPointerException ex) {
            log.error("Could not send message:\n{}\n"
                    + "WebSockets session id: {}", message,
                    session == null ? null : session.getId(), ex);
        }
    }
}
