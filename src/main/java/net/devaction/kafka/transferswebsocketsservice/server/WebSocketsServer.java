package net.devaction.kafka.transferswebsocketsservice.server;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface WebSocketsServer {

    public void start(String host, int port, String contextPath)
            throws Exception;

    public void stop();
}
