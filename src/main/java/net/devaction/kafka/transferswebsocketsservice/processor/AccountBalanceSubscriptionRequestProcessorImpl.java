package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceSubscriptionRequest;
import net.devaction.kafka.transferswebsocketsservice.processor.balanceupdatesproducer.UpdatesDispatcher;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class AccountBalanceSubscriptionRequestProcessorImpl 
        implements AccountBalanceSubscriptionRequestProcessor{

    private static final Logger log = LoggerFactory.getLogger(AccountBalanceSubscriptionRequestProcessorImpl.class);

    private final UpdatesDispatcher updatesDispatcher;
    
    public AccountBalanceSubscriptionRequestProcessorImpl(UpdatesDispatcher updatesDispatcher){
        this.updatesDispatcher = updatesDispatcher;
    }
    
    @Override
    public void process(AccountBalanceSubscriptionRequest request, Session session){
        updatesDispatcher.addSession(request.getAccountId(), session);        
    }   
}
