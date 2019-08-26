package net.devaction.kafka.transferswebsocketsservice.client.accountbalancerequestsender;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.kafka.transferswebsocketsservice.message.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ClientEndPoint extends Endpoint{
    private static final Logger log = LoggerFactory.getLogger(ClientEndPoint.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onOpen(Session session, EndpointConfig config){
        log.debug("Session {} has been opened.", session.getId());
        
        session.addMessageHandler(MessageWrapper.class, new ClientMessageHandler(session));
        
        try{
            MessageWrapper messageWrapper = createMessageWrapper();
            log.debug("Going to send a message: {}", messageWrapper);
            
            while (!session.isOpen()) {
                log.debug("Going to wait for the web socket session to open.");
                try{
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex){
                    log.error("{}", ex, ex);
                }
            }
            //session.getBasicRemote().sendText("hello");
            session.getBasicRemote().sendObject(messageWrapper);
            //session.getAsyncRemote().sendObject(messageWrapper);
            //session.getAsyncRemote().sendObject(messageWrapper, new AsyncSendHandler());
        //} catch (IOException | EncodeException ex){
        } catch (Exception ex){  
            log.error("Session: {}. Unable to send message: {}", 
                    session.getId(), ex, ex);
        }
    }

    private MessageWrapper createMessageWrapper() throws IOException{
        AccountBalanceRequest request = createAccountBalance();
        String json;
        
        try{
            json = mapper.writeValueAsString(request);
        } catch (JsonProcessingException ex){
            log.error("{}", ex, ex);
            throw ex;
        }
        
        return new MessageWrapper(MessageType.BALANCE_REQUEST.name(), json);
        //return new MessageWrapper(MessageType.BALANCE_REQUEST.name(), "whatever");
    }
    
    private AccountBalanceRequest createAccountBalance() {
        
        AccountBalanceRequest request = new AccountBalanceRequest("test-account-id-01");
        
        return request;
    } 
    
    @Override
    public void onError(Session session, Throwable throwable) {
        log.error("Session {} threw an error: {}", session.getId(), throwable, throwable);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason){
        log.debug("Session {} has been closed.", session.getId());
    }
}
