import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private static List<ClientInfo> clientList = new ArrayList<ClientInfo>();
    private static List<ClientInfo> unreachableClientList = new ArrayList<ClientInfo>();
    private static List<ServeOneFruitore> serversThreads = new ArrayList<ServeOneFruitore>();
    private static Pubblicatore publisher;
    private final InetAddress ipAddress = InetAddress.getLocalHost();

    public void run() {
        while(true) {
            try {
                System.out.println("Server info: Waiting for client on port: " + serverSocket.getLocalPort());
                Socket server = serverSocket.accept();
                
                System.out.println("Server info: Just connected: " + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                ClientRequest newRequest = null;

                try {
                    newRequest = (ClientRequest) in.readObject();
                    System.out.println("RICHIESTA RICEVUTA DAL CLIENT: " + newRequest.getSocketAddress().toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                List<Notizia.Tipo> newsTypes = new ArrayList<Notizia.Tipo>();
                newsTypes.add(newRequest.getType());
                ClientInfo newClient = new ClientInfo(newsTypes, server, newRequest.getSocketAddress());
                clientList.add(newClient);

                ServeOneFruitore serverThread = new ServeOneFruitore(newClient);
                serverThread.setName("THREAD: "+newClient.getSocketAddress().toString());
                // serversThreads.add(serverThread);

                System.out.println("Server info: \n<< Client Connected List: >>");

                for (ClientInfo clientInfo : clientList) {
                    System.out.println(clientInfo);
                }

            } catch (SocketTimeoutException e) {
                System.err.println("Server error: Socket timed out!");
                break;
            } catch (IOException e) {
                System.err.println("Server error: ");
                e.printStackTrace();
                break;
            } 
        }
    }

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server info: Server started on IP: " + ipAddress.getHostAddress() + " On Port: " + port);
    }

    // Utility

    public static void sendNews() {
        List<Notizia> allNews = Pubblicatore.buffer.getAllNotizie();

        HashMap<SocketAddress, List<Notizia>> data = new HashMap<SocketAddress, List<Notizia>>();
        if(!clientList.isEmpty()) {
            for(ClientInfo info: clientList) {
                List<Notizia> clientNews = new ArrayList<Notizia>();
                for(Notizia news: allNews) {
                    for(Notizia.Tipo type: info.getNewsTypes()) {
                        if(news.getType().equals(type)) {
                            // Aggiungi news alla lista del client
                            clientNews.add(news);
                        }
                    }   
                }
                data.put(info.getSocketAddress(), clientNews);
            }
        } else {
            System.out.println("Server info: No client connected to the server");
        }
        
        for(ClientInfo info: clientList) {
            List<Notizia> news = data.get(info.getSocketAddress());
            OutputStream os;
            ObjectOutputStream out;
            try {
                os = info.getSocket().getOutputStream();
                out = new ObjectOutputStream(os);
                ServerResponse res = new ServerResponse(news);

                System.out.println("Publisher THREAD -> SENDING DATA TO: " + info.getSocketAddress().toString());

                out.writeObject(res);
            } catch (SocketException e) {
                System.err.println("CLIENT LIST INFO: "+ info.getSocketAddress() + "is unreachable it will be removed from the client list");
                unreachableClientList.add(info);
            }
             catch (IOException e) {
                System.err.println("Server error:");
                e.printStackTrace();
            }
        }

        if(clientList.removeAll(unreachableClientList)) {
            System.out.println("CLIENT LIST INFO: Unreachable client removed from the clients list");
        }
        
       
    }


    // Main
    public static void main(String[] args) {
        publisher = new Pubblicatore();
        int port = 1234;
      try {
         Thread t = new Server(port);
         t.start();
      } catch (IOException e) {
         e.printStackTrace();
      }

      // Thread pubblicatore parte con la sua attività
      publisher.start();
    }
}

class ServeOneFruitore extends Thread {
    Socket socket;
    ClientInfo clientInfo;
    private ObjectInputStream in;

    public ServeOneFruitore(ClientInfo cInfo) {
        this.clientInfo = cInfo;
        this.socket = clientInfo.getSocket();
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.start();
    }

    public void run() {
        try {
            while(true) {
                ClientRequest req = (ClientRequest) in.readObject();

                if(req.getEditFlag()) {
                    // Aggiungi tipo specificato
                    System.out.println(this.getName() + " RICHIESTA: " + req.getSocketAddress().toString() + " vuole AGGIUNGERE il tipo " + req.getType().toString());
                    
                } else {
                    // Rimuovi tipo specificato
                    System.out.println(this.getName() +" RICHIESTA: " + req.getSocketAddress().toString() + " vuole RIMUOVERE il tipo " + req.getType().toString());
                }
            }
        }catch(SocketException e) {
            System.out.println(this.getName()+" CLIENT DISCONNECTED: THREAD KILLED");
            this.interrupt();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class ClientInfo {
    private List<Notizia.Tipo> newsTypes;
    private Socket socket;
    private SocketAddress socketAddress;

    public ClientInfo(List<Notizia.Tipo> types, Socket socket, SocketAddress socketAddress) {
        this.newsTypes = new ArrayList<Notizia.Tipo>();
        for(Notizia.Tipo type: types) newsTypes.add(type);
        this.socket = socket;
        this.socketAddress = socketAddress;
    }

    public List<Notizia.Tipo> getNewsTypes() {
        return this.newsTypes;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public void addType(Notizia.Tipo newType) {
        if(!newsTypes.contains(newType)) newsTypes.add(newType);
    };

    public void removeType(Notizia.Tipo type) {
        if(newsTypes.contains(type)) newsTypes.remove(type);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Tipologie: [");
        for(int i = 0; i < newsTypes.size(); i++) {
            result.append(newsTypes.get(i));
            result.append(i == newsTypes.size()-1 ? "" : ",");
        }

        result.append("]\n\n");
        return result.toString();
    }
}
