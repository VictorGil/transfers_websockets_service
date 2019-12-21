package net.devaction.kafka.transferswebsocketsservice.localstores;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.entity.TransferEntity;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface BalanceAndTransferFacade {

    public void start();

    public AccountBalanceEntity getBalance(String accountId);

    public TransferEntity getTransfer(String transferId);

    public void stop();
}


