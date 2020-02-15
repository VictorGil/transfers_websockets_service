package net.devaction.kafka.transferswebsocketsservice.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uber.cadence.client.WorkflowClient;

import net.devaction.cadence.accountbalanceworkflow.AccountBalanceWorkflow;
import net.devaction.entity.AccountBalanceEntity;
import net.devaction.entity.TransferEntity;
import net.devaction.kafka.transferswebsocketsservice.transferscustomstore.TransfersStoreGetter;

/**
 * @author Víctor Gil
 *
 * since December 2019
 */
public class CadenceFacade implements BalanceAndTransferFacade, TransfersStoreGetter {
    private static final Logger log = LoggerFactory.getLogger(CadenceFacade.class);

    private final String domain;
    private WorkflowClient workflowClient;

    public CadenceFacade(String domain) {
        this.domain = domain;
    }

    @Override
    public void start() {
        log.debug("Going to start the Cadence workflow client, domain: \"{}\"", domain);
        workflowClient = WorkflowClient.newInstance(domain);
    }

    @Override
    public AccountBalanceEntity getBalance(String accountId) {
        final AccountBalanceWorkflow accountBalanceWf = workflowClient.newWorkflowStub(AccountBalanceWorkflow.class,
                accountId);

        final AccountBalanceEntity balanceEntity = accountBalanceWf.getBalance();

        log.trace("{} for \"{}\" accountId: {}", AccountBalanceEntity.class.getSimpleName(), accountId, balanceEntity);
        return balanceEntity;
    }

    @Override
    public TransferEntity getTransfer(String transferId) {
        final String errorMessage = "The \"getTransfer(String transferId)\" method "
                + "is not implemented when using Cadence";
        log.error(errorMessage);
        throw new UnsupportedOperationException(errorMessage);
    }

    @Override
    public Iterable<TransferEntity> getTransfers(String accountId) {
        final AccountBalanceWorkflow accountBalanceWf = workflowClient.newWorkflowStub(AccountBalanceWorkflow.class,
                accountId);

        return accountBalanceWf.getTransfersList();
    }

    @Override
    public void stop() {
        log.info("Going to stop/close the workflow client");
        workflowClient.close();
        log.info("The workflow client has been closed");
    }
}
