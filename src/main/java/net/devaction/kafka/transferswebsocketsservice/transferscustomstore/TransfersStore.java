package net.devaction.kafka.transferswebsocketsservice.transferscustomstore;

import java.util.Set;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransfersStore {

    public void add(TransferEntity transfer);

    public Set<TransferEntity> getTransfers(String accountId);
}
