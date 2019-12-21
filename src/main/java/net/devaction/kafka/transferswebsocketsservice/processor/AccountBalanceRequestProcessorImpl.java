package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.transferswebsocketsservice.localstores.LocalStoresManager;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSender;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AccountBalanceRequestProcessorImpl implements
        AccountBalanceRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(AccountBalanceRequestProcessorImpl.class);

    private final LocalStoresManager storesManager;
    private final AccountBalanceSender sender;

    public AccountBalanceRequestProcessorImpl(LocalStoresManager storesManager, AccountBalanceSender sender) {
        this.storesManager = storesManager;
        this.sender = sender;
    }

    @Override
    public void process(AccountBalanceRequest request, Session session) {
        log.trace("Session id: {}. Going to process the following request: {}",
                session.getId(), request);

        // This can be replace with a call to Cadence
        final AccountBalanceEntity balance = storesManager.getBalance(request.getAccountId());
        sender.send(balance, session, MessageType.BALANCE_DATA_RESPONSE);
    }
}
