package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public interface MessageWrapperProcessor {

    public void process(MessageWrapper messageWrapper, Session session);
}
