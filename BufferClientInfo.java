import java.io.*;
import java.net.*;
import java.util.*;

class BufferClientInfo {
    private List<ClientInfo> clientsInfo;

    // Constructor
    public BufferClientInfo() {
        clientsInfo = new ArrayList<ClientInfo>();
    }

    public synchronized List<ClientInfo> getClientsInfo() {
        return clientsInfo;
    }

    // Setter
    public synchronized void addItem(ClientInfo info) {
        if(!clientsInfo.contains(info)) clientsInfo.add(info);
    }

    public synchronized void removeItem(ClientInfo info) {
        if(clientsInfo.contains(info)) clientsInfo.remove(info);
    }

    public synchronized void setItem(SocketAddress address, Notizia.Tipo newType) {
        for(ClientInfo info: clientsInfo) {
            if(address.equals(info.getSocketAddress())) {
                info.addType(newType);
                System.out.print("List of types after ADD: ");
                for(Notizia.Tipo type: info.getNewsTypes()) {
                    System.out.print(type.toString() + " ");
                }
                System.out.println("\n");
            }
        }
    }
}