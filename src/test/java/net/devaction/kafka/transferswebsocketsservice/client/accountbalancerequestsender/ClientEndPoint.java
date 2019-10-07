package net.devaction.kafka.transferswebsocketsservice.client.accountbalancerequestsender;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ClientEndPoint extends Endpoint{
    private static final Logger log = LoggerFactory.getLogger(ClientEndPoint.class);

    @Override
    public void onOpen(Session session, EndpointConfig config){
        log.debug("Session {} has been opened.", session.getId());

        session.addMessageHandler(MessageWrapper.class, new ClientMessageHandler(session));
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        log.error("Session {} threw an error: {}", session.getId(), throwable, throwable);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason){
        log.debug("Session {} has been closed.", session.getId());
    }
}
