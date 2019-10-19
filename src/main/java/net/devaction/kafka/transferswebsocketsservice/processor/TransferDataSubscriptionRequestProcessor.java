package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataSubscriptionRequest;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransferDataSubscriptionRequestProcessor {

    void process(TransferDataSubscriptionRequest request, Session session);
}
