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
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceSubscriptionRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class RequestTestSender {
    private static final Logger log = LoggerFactory.getLogger(
            RequestTestSender.class);

    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        new RequestTestSender().run();
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

            // MessageWrapper messageWrapper = createBalanceRequestMessageWrapper();
            MessageWrapper messageWrapper = createBalanceSubscriptionRequestMessageWrapper();
            // MessageWrapper messageWrapper = createTransferRequestMessageWrapper();

            log.debug("Going to send a message: {}", messageWrapper);

            session.getBasicRemote().sendObject(messageWrapper);
        } catch (Exception ex) {
            log.error("{}", ex, ex);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please press a key to stop the test WebSockets client.");
        try {
            reader.readLine();
        } catch (IOException ex) {
            log.error("{}", ex, ex);
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ex) {
            log.error("{}", ex, ex);
        }
        log.info("Exiting");
    }

    private MessageWrapper createBalanceRequestMessageWrapper() throws IOException {
        AccountBalanceRequest request = createAccountBalanceRequest();

        String json;
        try {

            json = mapper.writeValueAsString(request);
            log.debug("{} JSON string:\n {}",
                    AccountBalanceRequest.class.getSimpleName(), json);

        } catch (JsonProcessingException ex) {
            log.error("{}", ex, ex);
            throw ex;
        }

        return new MessageWrapper(MessageType.BALANCE_DATA_REQUEST.name(), json);
    }

    private MessageWrapper createBalanceSubscriptionRequestMessageWrapper() throws IOException {
        AccountBalanceRequest request = createAccountBalanceRequest();

        String json;
        try {

            json = mapper.writeValueAsString(request);
            log.debug("{} JSON string:\n {}",
                    AccountBalanceSubscriptionRequest.class.getSimpleName(), json);

        } catch (JsonProcessingException ex) {
            log.error("{}", ex, ex);
            throw ex;
        }

        return new MessageWrapper(MessageType.BALANCE_DATA_SUBSCRIPTION.name(), json);
    }

    private MessageWrapper createTransferDataRequestMessageWrapper() throws JsonProcessingException {
        TransferDataRequest request = createTransferDataRequest();

        String json;

        try {
            json = mapper.writeValueAsString(request);
            log.debug("{} JSON string:\n {}",
                    TransferDataRequest.class.getSimpleName(), json);

        } catch (JsonProcessingException ex) {
            log.error("{}", ex, ex);
            throw ex;
        }
        return  new MessageWrapper(MessageType.TRANSFER_DATA_REQUEST.name(), json);
    }

    private AccountBalanceRequest createAccountBalanceRequest() {

        // AccountBalanceRequest request = new AccountBalanceRequest("28a090daa001");
        AccountBalanceRequest request = new AccountBalanceRequest("28a090daa002");
        // AccountBalanceRequest request = new AccountBalanceRequest("28a090daa003");
        return request;
    }

    private TransferDataRequest createTransferDataRequest() {
        return new TransferDataRequest("5eb0f2dfd9c3"); // transfer id
        //return new TransferDataRequest("28a090daa001"); // account id --> won't work, as expected
    }

    private AccountBalanceSubscriptionRequest createAccountBalanceSubscriptionRequest() {

        AccountBalanceSubscriptionRequest request = new AccountBalanceSubscriptionRequest("28a090daa002");
        return request;
    }
}
