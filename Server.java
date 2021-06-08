import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private List<ClientInfo> clientList = new ArrayList<ClientInfo>();

    public void run() {
        while(true) {
            try {
                System.out.println("Waiting for client on port: " + serverSocket.getLocalPort());
                Socket server = serverSocket.accept();
                
                System.out.println("Just connected: " + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                ClientInfo newClient = null;
                try {
                    newClient = (ClientInfo) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                
                if (!isClientPresent(newClient)) {
                    clientList.add(newClient);
                }
                System.out.println("<< Client Connected List: >>");
                for (ClientInfo clientInfo : clientList) {
                    System.out.println(clientInfo);
                }

                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Client added to newsletter to Server("+ server.getLocalSocketAddress() + ")");
                server.close();

            } catch (SocketTimeoutException e) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } 
        }
    }

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on IP: " + serverSocket.getLocalSocketAddress() + " On Port: " + port);
    }

    // Utility

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
        // new Pubblicatore();
        int port = 1234;
      try {
         Thread t = new Server(port);
         t.start();
      } catch (IOException e) {
         e.printStackTrace();
      }
    }
}
