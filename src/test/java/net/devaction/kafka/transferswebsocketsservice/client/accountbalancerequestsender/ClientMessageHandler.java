package net.devaction.kafka.transferswebsocketsservice.client.accountbalancerequestsender;

import javax.websocket.MessageHandler.Whole;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ClientMessageHandler implements Whole<MessageWrapper> {
    private static final Logger log = LoggerFactory.getLogger(ClientMessageHandler.class);

    private final Session session;

    public ClientMessageHandler(Session session) {
        this.session = session;
    }

    @Override
    public void onMessage(MessageWrapper message) {
        log.debug("Session: {}. Message has been received: {}", session.getId(), message);
    }
}
