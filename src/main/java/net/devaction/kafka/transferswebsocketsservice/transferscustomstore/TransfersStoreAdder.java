package net.devaction.kafka.transferswebsocketsservice.transferscustomstore;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since January 2020
 */
public interface TransfersStoreAdder {
    public void add(TransferEntity transfer);
}
