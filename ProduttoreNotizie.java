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

            try { Thread.sleep(10000); } catch(InterruptedException e) {}
        }
    }
}