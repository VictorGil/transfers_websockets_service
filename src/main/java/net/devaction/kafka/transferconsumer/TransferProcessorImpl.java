package net.devaction.kafka.transferconsumer;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.avro.util.TransferConverter;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.TransferDataDispatcher;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferProcessorImpl implements TransferProcessor {

    private final TransferDataDispatcher transferDispatcher;

    public TransferProcessorImpl(TransferDataDispatcher transferDispatcher) {
        this.transferDispatcher = transferDispatcher;
    }

    @Override
    public void process(Transfer transfer) {
        TransferEntity transferEntity = TransferConverter.convertToPojo(transfer);
        transferDispatcher.dispatch(transferEntity);
    }
}
