package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.transferswebsocketsservice.accountbalanceretriever.AccountBalanceRetriever;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSender;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AccountBalanceRequestProcessorImpl implements 
        AccountBalanceRequestProcessor{
    
    private static final Logger log = LoggerFactory.getLogger(AccountBalanceRequestProcessorImpl.class);
    
    private final AccountBalanceRetriever retriever;
    private final AccountBalanceSender sender;

    public AccountBalanceRequestProcessorImpl(AccountBalanceRetriever retriever, AccountBalanceSender sender){
        this.retriever = retriever;
        this.sender = sender;
    }

    @Override
    public void process(AccountBalanceRequest request, Session session){
        log.trace("Session id: {}. Going to process the following request: {}", 
                session.getId(), request);
        
        AccountBalanceEntity balance = retriever.retrieve(request.getAccountId());
        sender.send(balance, session);
    }
}
