package net.devaction.kafka.transferswebsocketsservice.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ClientTestEndPoint extends Endpoint{
    private static final Logger log = LoggerFactory.getLogger(ClientTestEndPoint.class);

    private final CountDownLatch messageLatch;
    
    public ClientTestEndPoint(CountDownLatch messageLatch){
        this.messageLatch = messageLatch;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config){
        log.debug("Session {} has been opened.", session.getId());
        
        session.addMessageHandler(String.class, 
                new ClientMessageHandler(session, messageLatch));
        
        log.debug("Going to send a message.");
        try{
            session.getBasicRemote().sendText("hello");
        } catch (IOException ex){
            log.error("Session: {}. Unable to send message: {}", 
                    session.getId(), ex, ex);
        }
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
