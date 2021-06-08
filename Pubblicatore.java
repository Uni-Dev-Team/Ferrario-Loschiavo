import java.util.Random;

public class Pubblicatore extends Thread {
    static int BUFFSIZE = 4;
    public static BufferNotizie buffer;
    public static Random RAND;
    private ProduttoreNotizie[] threadProduttore;

    public Pubblicatore() {
        buffer = new BufferNotizie(BUFFSIZE);
        RAND = new Random();
        threadProduttore = new ProduttoreNotizie[5];

        for(int i = 0; i < 5; i++) {
            threadProduttore[i] = new ProduttoreNotizie(buffer);
            threadProduttore[i].start();
        }
    }

    public void run() {
        while(true) {
            Server.sendNews();
            try { Thread.sleep(5000); } catch(InterruptedException e) {}
        }
    }
}
