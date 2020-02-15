package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataSubscriptionRequest;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.TransferDataDispatcher;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferDataUpdateSender;
import net.devaction.kafka.transferswebsocketsservice.transferscustomstore.TransfersStoreGetter;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferDataSubscriptionRequestProcessorImpl implements
        TransferDataSubscriptionRequestProcessor {

    private final TransferDataDispatcher transferDispatcher;
    private final TransfersStoreGetter transfersStoreGetter;
    private final TransferDataUpdateSender sender;

    public TransferDataSubscriptionRequestProcessorImpl(TransferDataDispatcher transferDispatcher,
            TransfersStoreGetter transfersStore, TransferDataUpdateSender sender) {

        this.transferDispatcher = transferDispatcher;
        this.transfersStoreGetter = transfersStore;
        this.sender = sender;
    }

    @Override
    public void process(TransferDataSubscriptionRequest request, Session session) {

        final Iterable<TransferEntity> pastTransfers = transfersStoreGetter.getTransfers(request.getAccountId());

        for (TransferEntity transfer : pastTransfers) {
            sender.send(transfer, session);
        }

        transferDispatcher.addSession(request.getAccountId(), session);
    }
}
