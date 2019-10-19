package net.devaction.kafka.transferswebsocketsservice.message.incoming;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferDataRequest {
    private String transferId;

    // Jackson needs this constructor
    public TransferDataRequest() {
    }

    public TransferDataRequest(String transferId) {
        this.transferId = transferId;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    @Override
    public String toString() {
        return "TransferInfoRequest [transferId=" + transferId + "]";
    }

    @Override
    public int hashCode() {
        return 31 + ((transferId == null) ? 0 : transferId.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        TransferDataRequest other = (TransferDataRequest) obj;

        if (transferId == null) {
            if (other.transferId != null) {
                return false;
            }
        } else if (!transferId.equals(other.transferId)) {
            return false;
        }

        return true;
    }
}
