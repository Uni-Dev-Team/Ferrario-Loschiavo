import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server extends Thread {
    private ServerSocket serverSocket;

    public void run() {
        while(true) {
            try {
                System.out.println("Waiting for client on port: " + serverSocket.getLocalPort());
                Socket server = serverSocket.accept();

                System.out.println("Just connected: " + server.getRemoteSocketAddress());
                DataInput in = new DataInputStream(server.getInputStream());

                System.out.println(in.readUTF());

                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Client connected successfully to "+ server.getLocalSocketAddress() + "!");
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
