import java.util.Arrays;
import java.util.Optional;
import java.io.Serializable;

public class Notizia implements Serializable {

    public static enum Tipo {
        POLITICA(0),
        ATTUALITA(1),
        SCIENZA(2),
        SPORT(3);

        private final int value;

        Tipo(int value) {
            this.value = value;
        }

        public static Optional<Tipo> valueFromInt(int value) {
            return Arrays.stream(values())
            .filter(type -> type.value == value)
            .findFirst();
        }

        public static int getSize() {
            return values().length;
        }
    }

    // Attributes of Notizia
    private Tipo type;
    private String content;

    // Getters
    public Tipo getType() {return type;}
    
    public String getContent() {return content;}

    // Setters
    public void setType(Tipo type) {this.type = type;}

    public void setContent(String content) {this.content = content;}

    // Cotructors
    public Notizia(Tipo type, String content) {
        this.type = type;
        this.content = content;
    }

    public Notizia(){}

    public static Tipo getRandomType() {
        Tipo type = Tipo.valueFromInt(Pubblicatore.RAND.nextInt(Tipo.getSize())).get();
        return type;
    }

    public static String getRandomContent() {
        StringBuilder content = new StringBuilder();

        String[] articles = { "the ", "a ", "one ", "some ", "any " };
        String[] nouns = {"boy","dog","car","bicycle"};
        String[] verbs = {"ran","jumped","sansg","moves"};
        String[] prepositions = {"away","towards","around","near"};

        // Randomly create sentence
        for ( int j = 0; j < 20; j++ ) {
            int articles1 = Pubblicatore.RAND.nextInt(articles.length);
            int nouns1 = Pubblicatore.RAND.nextInt(nouns.length);
            int verbs1 = Pubblicatore.RAND.nextInt(verbs.length);
            int prepositions1 = Pubblicatore.RAND.nextInt(prepositions.length);
            int articles2 = Pubblicatore.RAND.nextInt(articles.length);
            int nouns2 = Pubblicatore.RAND.nextInt(nouns.length);

            StringBuilder buffer = new StringBuilder();
            // Concatenate words and add period
            buffer.append(articles[articles1])
            .append(nouns[nouns1])
            .append(verbs[verbs1])
            .append(prepositions[prepositions1])
            .append(articles[articles2])
            .append(nouns[nouns2])
            .append( ".\n");
            // Capitalize first letter
            buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
            content.append(buffer.toString());
        }

        return content.toString();
    }

    public String toString() {
        return "<***********************>\n Tipo: " + type.toString() + "\n Messaggio: " + content + "\n<***********************>\n";
    }
    
    
}