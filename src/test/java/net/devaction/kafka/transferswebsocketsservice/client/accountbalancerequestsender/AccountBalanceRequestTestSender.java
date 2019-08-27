package net.devaction.kafka.transferswebsocketsservice.client.accountbalancerequestsender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Decoder;
import javax.websocket.Encoder;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperDecoder;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperEncoder;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class AccountBalanceRequestSender{
    private static final Logger log = LoggerFactory.getLogger(
            AccountBalanceRequestSender.class);
    
    public static void main(String[] args) {
        new AccountBalanceRequestSender().run();
    }
    
    private void run() {       
        try {
            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .encoders(Arrays.<Class<? extends Encoder>>asList(MessageWrapperEncoder.class))
                    .decoders(Arrays.<Class<? extends Decoder>>asList(MessageWrapperDecoder.class))
                    .build();

            final ClientManager client = ClientManager.createClient();
            client.connectToServer(new ClientEndPoint(), cec, 
                    new URI("ws://localhost:38201/endpoint/001"));   
            
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
}
