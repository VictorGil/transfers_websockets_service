package net.devaction.kafka.transferswebsocketsservice.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class SimpleTestServerMain {
    private static final Logger log = LoggerFactory.getLogger(SimpleTestServerMain.class);

    public static void main(String[] args) {
        new SimpleTestServerMain().runServer();
    }

    public void runServer() {
        Server server = new Server("localhost", 38201, "/endpoint", null,
                ServerTestEndPoint.class);

        try {
            log.info("Going to start the WebSockets server");
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception ex) {
            log.error("{}", ex, ex);
        } finally {
            log.info("Going to stop the WebSockets server");
            server.stop();
            log.info("WebSockets server stopped, exiting");
        }
    }
}
