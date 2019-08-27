package net.devaction.kafka.transferswebsocketsservice.processor;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
// We need this class because we do not control 
// the construction of the serverEndPoint class
// so we cannot easily inject the MessageWrapperProcessor
public class MessageWrapperProcessorSingletonProvider{
    private MessageWrapperProcessorSingletonProvider() {}
    
    private static MessageWrapperProcessor processor;             

    public static MessageWrapperProcessor getProcessor(){
        return processor;
    }

    public static void setProcessor(MessageWrapperProcessor processor){
        MessageWrapperProcessorSingletonProvider.processor = processor;
    }    
}
