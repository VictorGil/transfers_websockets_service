package net.devaction.kafka.transferswebsocketsservice.server;

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

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperDecoder;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperEncoder;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessorSingletonProvider;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
@ServerEndpoint(value = "/001",
decoders = MessageWrapperDecoder.class,
encoders = MessageWrapperEncoder.class) 
public class ServerEndPoint{
    private static final Logger log = LoggerFactory.getLogger(ServerEndPoint.class);

    @OnOpen
    public void onOpen(final Session session, final EndpointConfig config){        
        log.debug("Session {} has been opened.", session.getId());      
    }
    
    @OnMessage
    public void onMessage(MessageWrapper messageWrapper, Session session) {
        log.debug("Session {}. Message received: {}", session.getId(), messageWrapper);
        
        MessageWrapperProcessorSingletonProvider.
                getProcessor().process(messageWrapper, session);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Session {} threw an error: {}", session.getId(), throwable, throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        log.debug("Session {} has been closed. Reason: {}", session.getId(), closeReason);
    }
}
