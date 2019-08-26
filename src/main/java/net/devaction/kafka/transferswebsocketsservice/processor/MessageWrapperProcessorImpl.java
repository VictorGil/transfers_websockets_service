package net.devaction.kafka.transferswebsocketsservice.processor;

import java.io.IOException;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.kafka.transferswebsocketsservice.message.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.TransferInfoRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageWrapperProcessorImpl implements MessageWrapperProcessor{
    private static final Logger log = LoggerFactory.getLogger(MessageWrapperProcessorImpl.class);

    private final AccountBalanceRequestProcessor accountBalanceRequestProcessor;
    private final TransferInfoRequestProcessor transferInfoRequestProcessor;
    
    private final ObjectMapper mapper;
    
    public MessageWrapperProcessorImpl(
            AccountBalanceRequestProcessor accountBalanceRequestProcessor,
            TransferInfoRequestProcessor transferInfoRequestProcessor) {
        
        this.accountBalanceRequestProcessor = accountBalanceRequestProcessor;
        this.transferInfoRequestProcessor = transferInfoRequestProcessor;
        
        mapper = new ObjectMapper();
    }
    
    @Override
    public void process(MessageWrapper messageWrapper, Session session){
        AccountBalanceRequest accountBalanceRequest;
        if (messageWrapper.getType()
                .equalsIgnoreCase(MessageType.BALANCE_REQUEST.name())) {
            try{
                accountBalanceRequest = mapper.readValue(messageWrapper.getPayload(), 
                        AccountBalanceRequest.class);
            } catch (IOException ex){
                log.error("Unable to deserialize {} message payload: {}",
                        MessageType.BALANCE_REQUEST.name(), 
                        messageWrapper, ex);
                return;
            }
            
            accountBalanceRequestProcessor.process(accountBalanceRequest, session);
        }
        
        TransferInfoRequest transferInfoRequest;
        if (messageWrapper.getType()
                .equalsIgnoreCase(MessageType.TRANSFER_INFO_REQUEST.name())) {
            try{
                transferInfoRequest = mapper.readValue(messageWrapper.getPayload(), 
                        TransferInfoRequest.class);
            } catch (IOException ex){
                log.error("Unable to deserialize {} message payload: {}",
                        MessageType.TRANSFER_INFO_REQUEST.name(), 
                        messageWrapper);
                return;
            }
            
            transferInfoRequestProcessor.process(transferInfoRequest, session);
        }
    }    
}
