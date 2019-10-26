package net.devaction.kafka.transferswebsocketsservice.client.sender;

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

import net.devaction.kafka.transferswebsocketsservice.client.common.ClientEndPoint;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperDecoder;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapperEncoder;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TestRequestSender {
    private static final Logger log = LoggerFactory.getLogger(
            TestRequestSender.class);

    public void run(MessageWrapper messageWrapper) {
        Session session = null;
        try {
            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .encoders(Arrays.<Class<? extends Encoder>>asList(MessageWrapperEncoder.class))
                    .decoders(Arrays.<Class<? extends Decoder>>asList(MessageWrapperDecoder.class))
                    .build();

            final ClientManager client = ClientManager.createClient();
            session = client.connectToServer(new ClientEndPoint(), cec,
                    new URI("ws://localhost:38201/endpoint/001"));

            log.debug("Going to send a message: {}", messageWrapper);

            session.getBasicRemote().sendObject(messageWrapper);
        } catch (Exception ex) {
            log.error("{}", ex, ex);
            return;
        }

        // We exit when the user presses a key
        waitForKeyStroke();
    }

    private void waitForKeyStroke() {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
}
