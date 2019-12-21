package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.transferswebsocketsservice.facade.BalanceAndTransferFacade;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataRequest;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferDataResponseSender;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferDataRequestProcessorImpl implements TransferDataRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(TransferDataRequestProcessorImpl.class);

    private final BalanceAndTransferFacade storesManager;
    private final TransferDataResponseSender sender;

    public TransferDataRequestProcessorImpl(BalanceAndTransferFacade storesManager,
            TransferDataResponseSender sender) {

        this.storesManager = storesManager;
        this.sender = sender;
    }

    @Override
    public void process(TransferDataRequest request, Session session) {
        log.trace("Session id: {}. Going to process the following request: {}",
                session.getId(), request);

        TransferEntity transfer = storesManager.getTransfer(request.getTransferId());
        sender.send(transfer, session);
    }
}
