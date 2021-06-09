import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;


public class FruitoreNotizie {
    public static ClientRequest req;
    static int PORT;
    static String IP;
    static Random rand;

    public static void main(String[] args) {
        rand = new Random();

        //try {
        if (args.length == 2) {
            if (isIPv4Valid(args[0])) {
                IP = args[0];
                try {
                    PORT = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Number format not valid");
                    System.exit(0);
                }
                if(PORT > 1023 && PORT < 65535) {
                    try {
                        System.out.println("Started connection");
                        // Crei il socket e gli passi l IP e la Porta
                        Socket s1 = new Socket(InetAddress.getByName(IP), PORT);
                        // Provi la connessione
                        System.out.println("Connected" + s1.getRemoteSocketAddress());

                        OutputStream outToServer = s1.getOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(outToServer);

                        List<Notizia.Tipo> types = new ArrayList<Notizia.Tipo>();
                        int numOfTypes = rand.nextInt(4) + 1;
                        for(int i = 0; i < numOfTypes; i++) {
                            Notizia.Tipo pickedType;
                            do {
                                pickedType = Notizia.getRandomType();
                            } while(types.contains(pickedType));
                            types.add(pickedType);
                        }

                        System.out.print("Fruitore ha scelto tipologie: ");
                        if(types.size() > 0) {
                            req = new ClientRequest(true, types.get(0), s1.getLocalSocketAddress());
                            out.writeObject(req);
                            System.out.print(types.get(0).toString() + " ");

                            outToServer = s1.getOutputStream();
                            out = new ObjectOutputStream(outToServer);

                            for(int i = 1; i < types.size(); i++) {
                                req = new ClientRequest(true, types.get(i), s1.getLocalSocketAddress());
                                out.writeObject(req);
                                out.reset();
                                System.out.print(types.get(i).toString() + " ");
                            }
                        }
                        System.out.println("\n");

                        InputStream inFromServer;
                        ObjectInputStream in;

                        while(true) {
                            inFromServer = s1.getInputStream();

                            try {
                                in = new ObjectInputStream(inFromServer);
                                ServerResponse response = null;
                                try {
                                    response = (ServerResponse) in.readObject();
                                } catch(ClassNotFoundException e) {}
                                if (response != null) {
                                    System.out.println(response.toString());
                                }
                            } catch(SocketException e) {
                                System.out.println("Connection closed by the server.");
                                System.exit(0);
                            }
                        }
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