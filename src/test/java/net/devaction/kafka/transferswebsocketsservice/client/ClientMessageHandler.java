package net.devaction.kafka.transferswebsocketsservice.client;

import javax.websocket.MessageHandler.Whole;

import java.util.concurrent.CountDownLatch;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ClientMessageHandler implements Whole<String>{
    private static final Logger log = LoggerFactory.getLogger(ClientMessageHandler.class);

    private final CountDownLatch messageLatch;

    private final Session session;

    public ClientMessageHandler(Session session, CountDownLatch messageLatch){
        this.session = session;
        this.messageLatch = messageLatch;
    }

    @Override
    public void onMessage(String message){
        log.debug("Session: {}. Message has been received: {}", session.getId(), message);
        messageLatch.countDown();
    }
}
