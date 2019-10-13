package net.devaction.kafka.transferswebsocketsservice.message;

/**
 * @author Víctor Gil
 *
 * since August 2019
 *
 * It must match:
 * https://github.com/VictorGil/transfers_frontend/blob/master/src/app/entities/messageType.ts
 */
public enum MessageType {
    BALANCE_DATA_REQUEST,
    BALANCE_DATA_RESPONSE,

    BALANCE_DATA_SUBSCRIPTION,
    BALANCE_DATA_UPDATE,

    TRANSFER_DATA_REQUEST,
    TRANSFER_DATA_RESPONSE,

    TRANSFER_DATA_SUBSCRIPTION,
    TRANSFER_DATA_UPDATE
}
