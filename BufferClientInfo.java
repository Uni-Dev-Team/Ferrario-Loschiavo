
class BufferClientInfo {
    private List<ClientInfo> clientsInfo;

    // Constructor
    public BufferClientInfo() {
        clientsInfo = new ArrayList<ClientInfo>();
    }

    // Setter
    public synchronized void addItem(ClientInfo info) {
        if(!clientsInfo.contains(info)) clientsInfo.add(info);
    }

    public synchronized void removeItem(ClientInfo info) {
        if(clientsInfo.contains(info)) clientInfo.remove(info);
    }

    public synchronized void setItem(SocketAddress address, Notizia.Tipo newType) {
        for(ClientInfo info: clientsInfo) {
            if(address.getSocketAddress() == info.getSocketAddress()) {
                System.out.println("SOCKETADDRESS EQUALS");
                info.addType(newType);
            }
        }
    }
}