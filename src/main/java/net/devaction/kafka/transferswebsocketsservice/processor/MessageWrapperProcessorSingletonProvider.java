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
    
    private static final MessageWrapperProcessor PROCESSOR = 
            new MessageWrapperProcessorImpl(
                    new AccountBalanceRequestProcessorImpl(), 
                    new TransferInfoRequestProcessorImpl());

    public static MessageWrapperProcessor getProcessor(){
        return PROCESSOR;
    }
}
