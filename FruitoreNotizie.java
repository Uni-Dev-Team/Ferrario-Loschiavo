import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FruitoreNotizie {
    public static ClientRequest req;
    static int PORT;
    static String IP;
    public static void main(String[] args) {
        if (args.length == 2) {
            if (isIPv4Valid(args[0])) {
                IP = args[0];
                try {
                    PORT = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Number format not valid");
                }
                if(PORT > 1023) {
                    try {
                        System.out.println("Started connection");
                        // Crei il socket e gli passi l IP e la Porta
                        Socket s1 = new Socket(InetAddress.getByName(IP), PORT);
                        // Provi la connessione
                        System.out.println("Connected" + s1.getRemoteSocketAddress());
    
                        // Formatti la richiesta che fai al server
                        OutputStream outToServer = s1.getOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(outToServer);
    
                        //List<Notizia.Tipo> types = new ArrayList<Notizia.Tipo>();
                        //types.add(Notizia.Tipo.ATTUALITA);
                        //types.add(Notizia.Tipo.POLITICA);

                        //for(Notizia.Tipo type: types) {
                        //  info = new ClientInfo(type, s1.getLocalSocketAddress());
                        //  out.writeObject(info);
                        //}
 
                        req = new ClientRequest(true, Notizia.Tipo.POLITICA, s1.getLocalSocketAddress());
                        out.writeObject(req);
                        req = new ClientRequest(false, Notizia.Tipo.POLITICA, s1.getLocalSocketAddress());
                        out.writeObject(req);

                        InputStream inFromServer;
                        ObjectInputStream in;
    
                        while(true) {
                            inFromServer = s1.getInputStream();
                            in = new ObjectInputStream(inFromServer);
                            ServerResponse response = null;
                            try {
                                response = (ServerResponse) in.readObject();
                            } catch(ClassNotFoundException e) {}
                            if (response != null) {
                                System.out.println(response.toString());
                            }


                        }
    
                        // inFromServer.close();
                        // in.close();
    
                        // Chiudi la connessione
                        // s1.close();
                    } catch(ConnectException e) {
                        System.err.println("Error: Unable to connect to the server. Check if the server is running on: \n-IP: " +args[0] + "\n-PORT: " + PORT );
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    } 
                } else {
                    System.err.println("Error: Well Known Ports not supported. Use a port that is higher than 1023");
                }
            } else {
                System.err.println("Error: IPv4 given addres isn't valid");
            }
        } else {
                System.err.println("Error: Check documentation for more details");
            }
    }


    private static Boolean isIPv4Valid(String address) {
        final String IPV4_REGEX =
                    "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);
        Matcher matcher = IPv4_PATTERN.matcher(address);
        return matcher.matches();

    }
}