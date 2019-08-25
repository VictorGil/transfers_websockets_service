package net.devaction.kafka.transferswebsocketsservice.server;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
@ServerEndpoint(value = "/") 
public class SimpleTestEndPoint extends Endpoint{
    private static final Logger log = LoggerFactory.getLogger(SimpleTestEndPoint.class);

    @Override
    public void onOpen(Session session, EndpointConfig config){
        log.debug("Session {} has been opened.", session.getId());
        
        session.addMessageHandler(String.class, new MessageHandler(session));
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
