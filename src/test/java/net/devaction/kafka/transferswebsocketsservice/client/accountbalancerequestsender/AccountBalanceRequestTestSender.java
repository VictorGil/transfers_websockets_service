package net.devaction.kafka.transferswebsocketsservice.client.accountbalancerequestsender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperDecoder;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperEncoder;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferInfoRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AccountBalanceRequestTestSender{
    private static final Logger log = LoggerFactory.getLogger(
            AccountBalanceRequestTestSender.class);
    
    private ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) {
        new AccountBalanceRequestTestSender().run();
    }
    
    private void run() {
        Session session = null;
        try {
            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .encoders(Arrays.<Class<? extends Encoder>>asList(MessageWrapperEncoder.class))
                    .decoders(Arrays.<Class<? extends Decoder>>asList(MessageWrapperDecoder.class))
                    .build();

            final ClientManager client = ClientManager.createClient();
            session = client.connectToServer(new ClientEndPoint(), cec, 
                    new URI("ws://localhost:38201/endpoint/001"));   
            
            //MessageWrapper messageWrapper = createBalanceRequestMessageWrapper();
            MessageWrapper messageWrapper = createTransferRequestMessageWrapper();
            
            log.debug("Going to send a message: {}", messageWrapper);
                        
            session.getBasicRemote().sendObject(messageWrapper);           
        } catch (Exception ex) {
            log.error("{}", ex, ex);
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please press a key to stop the test WebSockets client.");
        try{
            reader.readLine();
        } catch (IOException ex){
            log.error("{}", ex, ex);
        }
        try{
            if (reader != null)
                reader.close();
        } catch (IOException ex){
            log.error("{}", ex, ex);
        }
        log.info("Exiting");
    }
    
    private MessageWrapper createBalanceRequestMessageWrapper() throws IOException{
        AccountBalanceRequest request = createAccountBalanceRequest();        
        
        String json;           
        try{
            json = mapper.writeValueAsString(request);
        } catch (JsonProcessingException ex){
            log.error("{}", ex, ex);
            throw ex;
        }
     
        return new MessageWrapper(MessageType.BALANCE_REQUEST.name(), json);        
    }
   
    private MessageWrapper createTransferRequestMessageWrapper() throws JsonProcessingException {
        TransferInfoRequest request = createTransferInfoRequest();
        
        String json;
        
        try{
            json = mapper.writeValueAsString(request);
        } catch (JsonProcessingException ex){
            log.error("{}", ex, ex);
            throw ex;
        }
        return  new MessageWrapper(MessageType.TRANSFER_INFO_REQUEST.name(), json);
    }
    
    private AccountBalanceRequest createAccountBalanceRequest() {

        AccountBalanceRequest request = new AccountBalanceRequest("28a090daa001");
        return request;
    } 
    
    private TransferInfoRequest createTransferInfoRequest() {
        //return new TransferInfoRequest("b8ed64d9e1b7"); // transfer id
        return new TransferInfoRequest("28a090daa001"); // account id 
    }
}
