package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataSubscriptionRequest;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferDataSubscriptionRequestProcessorImpl implements
        TransferDataSubscriptionRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(TransferDataSubscriptionRequestProcessorImpl.class);

    @Override
    public void process(TransferDataSubscriptionRequest request, Session session) {
        // TODO
    }
}
