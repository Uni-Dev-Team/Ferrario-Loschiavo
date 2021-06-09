import java.io.Serializable;
import java.net.*;

public class ClientRequest implements Serializable {
    // editFlag: 0 => Remove selected type; 1 => Add selected type
    
    private Boolean editFlag;
    private Notizia.Tipo type;
    private SocketAddress socketAddress;

    // Constructor
    public ClientRequest(Boolean editFlag, Notizia.Tipo type, SocketAddress socketAddress) {
        this.editFlag = editFlag;
        this.type = type;
        this.socketAddress = socketAddress;
    }

    // Getters & Setters
    public Boolean getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(Boolean editFlag) {
        this.editFlag = editFlag;
    }
    
    public Notizia.Tipo getType() {
        return type;
    }

    public void setType(Notizia.Tipo type) {
        this.type = type;
    }

    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }
}
