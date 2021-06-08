import java.io.*;
import java.net.*;
import java.util.*;

public class ClientInfo implements Serializable {
    private List<Notizia.Tipo> newsTypes;
    private SocketAddress socketAddress;
    private Socket socket;

    public ClientInfo(List<Notizia.Tipo> types, SocketAddress socketAddress) {
        this.newsTypes = new ArrayList<Notizia.Tipo>();
        for(Notizia.Tipo type: types) newsTypes.add(type);
        this.socketAddress = socketAddress;
    }

    public List<Notizia.Tipo> getNewsTypes() {
        return this.newsTypes;
    }

    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void addType(Notizia.Tipo newType) {
        if(!newsTypes.contains(newType)) newsTypes.add(newType);
    };

    public void removeType(Notizia.Tipo type) {
        if(newsTypes.contains(type)) newsTypes.remove(type);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Indirizzo IP: " + socketAddress.toString() + "\n");
        result.append("Tipologie: [");
        for(int i = 0; i < newsTypes.size(); i++) {
            result.append(newsTypes.get(i));
            result.append(i == newsTypes.size()-1 ? "" : ",");
        }

        result.append("]\n\n");
        return result.toString();
    }
}