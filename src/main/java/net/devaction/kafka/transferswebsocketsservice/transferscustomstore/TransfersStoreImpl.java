package net.devaction.kafka.transferswebsocketsservice.transferscustomstore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransfersStoreImpl implements TransfersStore {
    private Map<String, HashSet<TransferEntity>> accountTransfersMap = new HashMap<String, HashSet<TransferEntity>>();

    @Override
    public void add(TransferEntity transfer) {
        if (accountTransfersMap.containsKey(transfer.getAccountId())) {
            accountTransfersMap.get(transfer.getAccountId()).add(transfer);
        } else {
            HashSet<TransferEntity> transfers = new HashSet<TransferEntity>();
            transfers.add(transfer);
            accountTransfersMap.put(transfer.getAccountId(), transfers);
        }
    }

    @Override
    public Set<TransferEntity> getTransfers(String accountId) {
        if (accountTransfersMap.containsKey(accountId)) {
            return accountTransfersMap.get(accountId);
        }

        return new HashSet<TransferEntity>(0);
    }
}
