import java.io.*;
import java.util.*;

public class ServerResponse implements Serializable {
    //List<Notizia> news;
    String message;

    //public ServerResponse(List<Notizia> news) {
    //    this.news = new ArrayList<Notizia>();
    //    for(Notizia n: news) {
    //        this.news.add(n);
    //    }
    //}
//
    //public String toString() {
    //    /*StringBuilder res = new StringBuilder();
    //    for(Notizia n: news) {
    //        res.append(n.toString() + "\n");
    //    }
    //    return res.toString();*/
    //}

    public ServerResponse(String mes) {
        this.message = mes;
    }

    public String getMessage() {
        return this.message;
    }
}