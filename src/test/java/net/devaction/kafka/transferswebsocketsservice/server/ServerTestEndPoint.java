package net.devaction.kafka.transferswebsocketsservice.server;

import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
@ServerEndpoint(value = "/001") 
public class ServerTestEndPoint{
    private static final Logger log = LoggerFactory.getLogger(ServerTestEndPoint.class);

    @OnOpen
    public void onOpen(final Session session, final EndpointConfig config){        
        log.debug("Session {} has been opened.", session.getId());      
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("Message received: {}", message);
        
        log.debug("Going to send a message to all the open sessions");
        Set<Session> sessions = session.getOpenSessions();
        int i = 0;
        for (Session sessionAux: sessions) {            
            if (sessionAux.isOpen()) {
                i++;                
                sessionAux.getAsyncRemote().sendText(
                        String.format("Message #%d to server session %s", 
                                i, session.getId()));
                log.trace("Message asynchronously being sent to {} session", sessionAux.getId());
            }            
        }
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Session {} threw an error: {}", session.getId(), throwable, throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        log.debug("Session {} has been closed.", session.getId());
    }
}
