import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    public static ClientInfo info;

    public static void main(String[] args) throws Exception {
        // Potrebbe dare errori TRY
        try {
            System.out.println("Started connection");

            // Crei il socket e gli passi l IP e la Porta
            Socket s1 = new Socket(InetAddress.getByName("192.168.1.62"), 1234);
            // Provi la connessione
            System.out.println("Connected" + s1.getRemoteSocketAddress());

            // Formatti la richiesta che fai al server
            OutputStream outToServer = s1.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);
            // DataOutputStream out = new DataOutputStream(outToServer);
            // Invii il messaggio al server
            // out.writeUTF("Ciao da: "+ s1.getLocalSocketAddress()+ "\n Che giorno è? ");

            List<Notizia.Tipo> types = new ArrayList<Notizia.Tipo>();
            types.add(Notizia.Tipo.ATTUALITA);
            types.add(Notizia.Tipo.POLITICA);
            info = new ClientInfo(types, s1.getLocalSocketAddress());
            out.writeObject(info);
            out.close();

            while(true) {
                ObjectInputStream inFromServer = new ObjectInputStream(s1.getInputStream());
                ServerResponse response = null;
                try {
                    response = (ServerResponse) inFromServer.readObject();
                } catch(ClassNotFoundException e) {}
                if (response != null) {
                    System.out.println(response.toString());
                }
                inFromServer.close();
            }

            // Chiudi la connessione
            // s1.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}