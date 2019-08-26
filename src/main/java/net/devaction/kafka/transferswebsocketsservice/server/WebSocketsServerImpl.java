package net.devaction.kafka.transferswebsocketsservice.server;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class WebSocketsServerImpl implements WebSocketsServer{
    private static final Logger log = LoggerFactory.getLogger(WebSocketsServerImpl.class);

    private Server server;
    
    @Override
    public void start(String host, int port, String contextPath) throws Exception{
        log.info("Going to start the Websockets server, host: {},"
                + "port: {}, context path: {}",
                host, port, contextPath);        
       
                server = new Server(host, port, contextPath, null, 
                ServerEndPoint.class);
                
        try{
            server.start();
        } catch (DeploymentException ex){
            log.error("Unable to start the Tyrus server: {}", ex, ex);
            throw ex;
        }
        log.debug("WebSockets server has been started");
    }

    @Override
    public void stop(){
        log.info("Going to stop the WebSockets server");
        if (server != null) {
            server.stop();
            log.debug("The WebSockets server has been stopped");
        }        
    }
}

