import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private static List<ClientInfo> clientList = new ArrayList<ClientInfo>();
    private static Pubblicatore publisher;
    private final InetAddress ipAddress = InetAddress.getLocalHost();
    public void run() {
        while(true) {
            try {
                System.out.println("Server info: Waiting for client on port: " + serverSocket.getLocalPort());
                Socket server = serverSocket.accept();
                
                System.out.println("Server info: Just connected: " + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                ClientInfo newClient = null;
                try {
                    newClient = (ClientInfo) in.readObject();
                    newClient.setSocket(server);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                
                if (!isClientPresent(newClient)) {
                    clientList.add(newClient);
                }
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
                ServerResponse res = new ServerResponse("news");
                out.writeObject(res);
            } catch (IOException e) {
                System.err.println("Server error:");
                e.printStackTrace();
            }
        }
    }

    private Boolean isClientPresent(ClientInfo newClient) {
        Boolean resl = false;
        for (ClientInfo client : clientList) {
            if (client.equals(newClient)) {
                resl = true;
                break;
            }
        }
        return resl;
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
