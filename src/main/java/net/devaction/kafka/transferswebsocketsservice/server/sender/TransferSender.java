package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.Session;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface TransferSender {

    public void send(TransferEntity transfer, Session session);
}


