import java.util.*;

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