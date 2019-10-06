package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AccountBalanceSenderImpl implements AccountBalanceSender{
    private static final Logger log = LoggerFactory.getLogger(AccountBalanceSenderImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private final MessageSender messageSender;
    
    public AccountBalanceSenderImpl(MessageSender messageSender){
        this.messageSender = messageSender;
    }

    @Override
    public void send(AccountBalanceEntity accountBalance, Session session, 
            MessageType messageType){
        
        if (messageType != MessageType.BALANCE_DATA_RESPONSE &&
                messageType != MessageType.BALANCE_DATA_UPDATE) {
            
            log.error("Incorrect message type: {}", messageType);
        }
        
        String json;
        try{
            json = mapper.writeValueAsString(accountBalance);
        } catch (JsonProcessingException ex){
            log.error("Unable to serialize {} to JSON: {}", 
                    AccountBalanceEntity.class.getSimpleName(),
                    accountBalance, ex);
            return;
        }
        
        MessageWrapper message = new MessageWrapper(messageType.name(),
                json);
        
        messageSender.send(message, session);
    }
}
