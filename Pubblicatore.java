import java.util.Random;
import java.util.*;
import java.net.*;
import java.io.*;

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
            //buffer.printBuffer();
            buffer.resetBuffer();
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

    public class BufferNotizie {
        // Attributes of the Buffer
        static int BUFFER_SIZE;
        private Notizia[] buffer;
        private int numItems = 0;
    
        // Constructor
        public BufferNotizie(int bufsize) {
            BUFFER_SIZE = bufsize;
            buffer = new Notizia[BUFFER_SIZE];
        }
    
        // Getters
        public synchronized Notizia getNotizia(Notizia.Tipo type) {
            Notizia tmp = null;
            while(numItems==0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            for (Notizia notizia: buffer) {
                if (notizia != null && notizia.getType().equals(type)) {
                    tmp = notizia;
                }
            }
            notify();
                return tmp;
                
        }
    
        public synchronized List<Notizia> getAllNotizie() {
            List<Notizia> newsResult = new ArrayList<Notizia>();
    
            for(Notizia news: buffer) {
                if(news != null && !news.getContent().equals("")) {
                    newsResult.add(news);
                }
            }
    
            return newsResult;
        }
    
        public int getCurrentSize(){
            return numItems;
        }
    
        // Setter
        public synchronized void setItem(Notizia news) {
            while(numItems==BUFFER_SIZE){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    
            if(isPresent(news.getType())) {
               for (int i = 0; i < buffer.length; i++) {
                   if (buffer[i] != null && news.getType().equals(buffer[i].getType())) {
                        String resl = buffer[i].getContent();
                        resl += news.getContent();
                        buffer[i].setContent(resl);
                        //System.out.println("Buffer info: \nUPDATE: \n" + buffer[i].toString());
                        break;
                   }
               }
            } else {
                for (int i = 0; i < buffer.length; i++) {
                    if (buffer[i] == null) {
                        buffer[i] = news;
                        //System.out.println(buffer[i].toString());
                        break;
                    }
                }
            }
    
            
        }
    
        // Utility
        private synchronized Boolean isPresent(Notizia.Tipo type) {
            Boolean resl = false;
            
            for (Notizia news: buffer) {
                if (news != null && news.getType().equals(type)) {
                    resl = true;
                } 
            }
            return resl;
        }
    
        public synchronized void resetBuffer() {
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = null;
            }
        }
        private synchronized Boolean isEmpty() {
            Boolean resl = true;
            for (Notizia notizia : buffer) {
                if(notizia != null) {
                    resl = false;
                }
            }
            return resl;
        }
        public synchronized void printBuffer() {
            System.out.println("Buffer printed: ");
            if(!this.isEmpty()) {
                for (Notizia notizia : buffer) {
                    System.out.println(notizia);
                }
            } else {
                System.out.println("Buffer is empty");
            }
            
        }
    }
}
