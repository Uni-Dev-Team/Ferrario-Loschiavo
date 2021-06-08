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
		
		System.out.print(tmp+" read\n");
		notify();
		    return tmp;
            
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
                    System.out.println("UPDATE: \n" + buffer[i].toString());
                    break;
               }
           }
        } else {
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] == null) {
                    buffer[i] = news;
                    System.out.println(buffer[i].toString());
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
}