package net.devaction.kafka.transferswebsocketsservice.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ServerImplTesterMain {
    private static final Logger log = LoggerFactory.getLogger(ServerImplTesterMain.class);

    public static void main(String[] args) {
        new ServerImplTesterMain().run();
    }

    private void run() {
        WebSocketsServer server = new WebSocketsServerImpl();

        BufferedReader reader = null;
        try {
            server.start("localhost", 38201, "/endpoint");
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception ex) {
            log.error(" {}", ex, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    log.error(" {}", ex, ex);
                }
            }

            log.info("Going to stop the WebSockets server");
            server.stop();
            log.info("WebSockets server stopped, exiting");
        }
    }
}
