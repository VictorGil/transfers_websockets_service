package net.devaction.kafka.transferswebsocketsservice.processor.dispatcher;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferDataDispatcherImpl implements TransferDataDispatcher {
    private static final Logger log = LoggerFactory.getLogger(TransferDataDispatcherImpl.class);

    @Override
    public void addSession(String accountId, Session session){

    }

    @Override
    public void dispatch(TransferEntity transfer){


    }
}
