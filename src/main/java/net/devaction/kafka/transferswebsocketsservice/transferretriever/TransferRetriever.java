package net.devaction.kafka.transferswebsocketsservice.transferretriever;

import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface TransferRetriever{
    
    public void start(String bootstrapServers, String schemaRegistryUrl);
    
    public TransferEntity retrieve(String accountId);
    
    public void stop();
}
