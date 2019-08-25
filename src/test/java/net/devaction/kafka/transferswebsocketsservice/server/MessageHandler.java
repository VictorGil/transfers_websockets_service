package net.devaction.kafka.transferswebsocketsservice.server;

import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageHandler implements Whole<String>{
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    final Session session;
    
    public MessageHandler(Session session){
        this.session = session;
    }

    @Override
    public void onMessage(String message){
        log.debug("Session: {}. Message has been received: {}", session.getId(), message);        
    }
}
