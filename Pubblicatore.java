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

            // Per controllare se il buffer viene riempito correttamente
            //buffer.printBuffer();   
            buffer.resetBuffer();
            // Per controllare se il buffer viene svuotato correttamente
            //buffer.printBuffer();
            try { Thread.sleep(5000); } catch(InterruptedException e) {}
        }
    }

    class ProduttoreNotizie extends Thread {
        BufferNotizie buffer;

        public ProduttoreNotizie(BufferNotizie buffer) {
            this.buffer = buffer;
        }

        public void run() {
            while(true) {
                Notizia.Tipo type = Notizia.getRandomType();
                String content = Notizia.getRandomContent();
                Notizia news = new Notizia(type, content);
                buffer.setItem(news);

                try { Thread.sleep(1000); } catch(InterruptedException e) {}
            }
        }
    }

    
}
