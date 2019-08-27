package net.devaction.kafka.transferswebsocketsservice.localstores;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface LocalStoresManager{
    
    public void start(String bootstrapServers, String schemaRegistryUrl);
    
    public AccountBalanceEntity getBalance(String accountId);
    public TransferEntity getTransfer(String transferId);
    
    public void stop();
}


