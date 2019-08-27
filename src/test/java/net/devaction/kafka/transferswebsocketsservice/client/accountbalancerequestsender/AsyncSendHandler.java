package net.devaction.kafka.transferswebsocketsservice.client.accountbalancerequestsender;

import javax.websocket.SendHandler;
import javax.websocket.SendResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AsyncSendHandler implements SendHandler{
    private static final Logger log = LoggerFactory.getLogger(AsyncSendHandler.class);

    @Override
    public void onResult(SendResult result){
        log.info("Result of sending the message: {}", result);
    }
}
