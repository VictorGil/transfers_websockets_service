package net.devaction.kafka.transferconsumer;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.avro.Transfer;
import net.devaction.kafka.avro.util.TransferConverter;
import net.devaction.kafka.transferswebsocketsservice.processor.dispatcher.TransferDataDispatcher;
import net.devaction.kafka.transferswebsocketsservice.transferscustomstore.TransfersStoreAdder;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferUpdateProcessorWithoutCadence implements TransferUpdateProcessor {

    private final TransferDataDispatcher transferDispatcher;
    private final TransfersStoreAdder transfersStore;

    public TransferUpdateProcessorWithoutCadence(TransferDataDispatcher transferDispatcher, TransfersStoreAdder transfersStore) {
        this.transferDispatcher = transferDispatcher;
        this.transfersStore = transfersStore;
    }

    @Override
    public void process(Transfer transfer) {
        TransferEntity transferEntity = TransferConverter.convertToPojo(transfer);

        transfersStore.add(transferEntity);
        transferDispatcher.dispatch(transferEntity);
    }
}
