package net.devaction.kafka.transferswebsocketsservice.message;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageWrapper{
    
    private final MessageType type;
    private final String payload;

    public MessageWrapper(MessageType type, String payload){
        this.type = type;
        this.payload = payload;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((payload == null) ? 0 : payload.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        
        if (obj == null)
            return false;
        
        if (getClass() != obj.getClass())
            return false;
        
        MessageWrapper other = (MessageWrapper) obj;
        
        if (type != other.type)
            return false;
        
        if (payload == null){
            if (other.payload != null)
                return false;
        } else if (!payload.equals(other.payload))
            return false;        

        return true;
    }

    @Override
    public String toString(){
        return "MessageWrapper [type=" + type + ", payload=" + payload + "]";
    }
    
    public MessageType getType(){
        return type;
    }
    
    public String getPayload(){
        return payload;
    }
}
