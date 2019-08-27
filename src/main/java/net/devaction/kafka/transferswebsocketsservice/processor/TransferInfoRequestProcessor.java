package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferInfoRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface TransferInfoRequestProcessor{
    
    public void process(TransferInfoRequest request, Session session);
}
