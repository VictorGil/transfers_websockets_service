package net.devaction.kafka.transferswebsocketsservice.processor.dispatcher;

import javax.websocket.Session;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransferDataDispatcher {

    public void addSession(String accountId, Session session);

    public void dispatch(TransferEntity transfer);
}
