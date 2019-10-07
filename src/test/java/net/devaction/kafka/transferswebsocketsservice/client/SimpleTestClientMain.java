package net.devaction.kafka.transferswebsocketsservice.client;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class SimpleTestClientMain {
    private static final Logger log = LoggerFactory.getLogger(SimpleTestClientMain.class);

    public static void main(String args[]) {
        new SimpleTestClientMain().run();
    }

    private void run() {
        final CountDownLatch messageLatch = new CountDownLatch(1);

        try {
            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

            final ClientManager client = ClientManager.createClient();
            client.connectToServer(new ClientTestEndPoint(messageLatch), cec,
                    new URI("ws://localhost:38201/endpoint/001"));

            log.debug("Going to sleep.");
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("{}", ex, ex);
        }
    }
}
