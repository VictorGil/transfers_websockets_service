package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.SendHandler;
import javax.websocket.SendResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AsyncSendHandler implements SendHandler {
    private static final Logger log = LoggerFactory.getLogger(AsyncSendHandler.class);

    @Override
    public void onResult(SendResult result) {
        if (result.isOK())
            log.trace("Message successfully asychronously sent: {}", result);
        else
            log.error("Error when trying to asychronously send the message: {}", result);
    }
}
