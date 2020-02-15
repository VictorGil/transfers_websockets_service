package net.devaction.kafka.transferswebsocketsservice.transferscustomstore;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransfersStoreGetter {

    public Iterable<TransferEntity> getTransfers(String accountId);
}
