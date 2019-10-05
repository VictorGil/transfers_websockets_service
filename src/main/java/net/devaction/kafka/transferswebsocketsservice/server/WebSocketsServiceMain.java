package net.devaction.kafka.transferswebsocketsservice.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// We are aware that this class is not part of the Java API
// but we need it
import sun.misc.Signal;
import sun.misc.SignalHandler;

import net.devaction.kafka.transferswebsocketsservice.config.ConfigValues;
import net.devaction.kafka.transferswebsocketsservice.localstores.LocalStoresManager;
import net.devaction.kafka.transferswebsocketsservice.localstores.LocalStoresManagerImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.AccountBalanceRequestProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.AccountBalanceRequestProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.processor.MessageWrapperProcessorSingletonProvider;
import net.devaction.kafka.transferswebsocketsservice.processor.TransferInfoRequestProcessor;
import net.devaction.kafka.transferswebsocketsservice.processor.TransferInfoRequestProcessorImpl;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSender;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSenderImpl;
import net.devaction.kafka.transferswebsocketsservice.server.sender.MessageSender;
import net.devaction.kafka.transferswebsocketsservice.server.sender.MessageSenderImpl;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferSender;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferSenderImpl;
import net.devaction.kafka.transferswebsocketsservice.config.ConfigReader;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class WebSocketsServiceMain implements SignalHandler{
    private static final Logger log = LoggerFactory.getLogger(WebSocketsServiceMain.class);

    private static final String WINCH_SIGNAL = "WINCH";
    
    private LocalStoresManager storesManager;
    
    private WebSocketsServer server;
    
    // TODO
    // private AccountBalanceConsumer balanceConsumer;
    
    public static void main(String[] args){
        new WebSocketsServiceMain().run();
    }
    
    private void run() {
        registerThisAsOsSignalHandler();
        
        ConfigValues configValues;
        log.info("Going to read the configuration values");
        try{
            configValues = new ConfigReader().read();
        } catch (Exception ex){
            log.error("Unable to read the configuration values, exiting");
            return;
        }
        
        storesManager = new LocalStoresManagerImpl();
        storesManager.start(configValues.getKafkaBootstrapServers(), 
                configValues.getKafkaSchemaRegistryUrl());
        
        MessageSender messageSender = new MessageSenderImpl();        
        AccountBalanceSender abSender = new AccountBalanceSenderImpl(messageSender);
        
        AccountBalanceRequestProcessor abReqProcessor = 
                new AccountBalanceRequestProcessorImpl(storesManager, abSender);
        
        TransferSender transferSender = new TransferSenderImpl(messageSender); 
                
        TransferInfoRequestProcessor tiReqProcessor = 
                new TransferInfoRequestProcessorImpl(storesManager, transferSender);
        
        MessageWrapperProcessor messageProcessor = new MessageWrapperProcessorImpl(
                abReqProcessor, tiReqProcessor);
        
        MessageWrapperProcessorSingletonProvider.setProcessor(messageProcessor);
        
        server = new WebSocketsServerImpl();
        try{
            server.start(configValues.getServerHost(), 
                    configValues.getServerPort(), 
                    configValues.getContextPath());
        } catch (Exception ex){
            log.error("Unable to start the WebSockets server, "
                    + "configuration values: {}", configValues, ex);            
            stop();        
        }
    }
    
    @Override
    public void handle(Signal signal){
        log.info("We have received the signal to tell us to stop: {}", signal.getName());
        stop();
    }
    
    private void registerThisAsOsSignalHandler() {
        log.debug("Going to register this object to handle the {} signal", WINCH_SIGNAL);
        try{
            Signal.handle(new Signal(WINCH_SIGNAL), this);
        } catch(Exception ex){
            // Most likely this is a signal that's not supported on this
            // platform or with the JVM as it is currently configured
            log.error("FATAL: The signal is not supported: {}, exiting", WINCH_SIGNAL, ex);
            System.exit(1);
        }        
    }
    
    private void stop() {
        if (server != null)
            server.stop();
        
        storesManager.stop();
    }
}
