package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface TransferInfoRequestProcessor {

    public void process(TransferDataRequest request, Session session);
}
