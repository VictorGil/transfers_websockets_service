package net.devaction.kafka.transferswebsocketsservice.message;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public enum MessageType{
    BALANCE_DATA_REQUEST,
    BALANCE_DATA_RESPONSE,
    
    BALANCE_DATA_SUBSCRIPTION,
    BALANCE_DATA_UPDATE, 
    
    TRANSFER_DATA_REQUEST,
    TRANSFER_DATA_RESPONSE   
}
