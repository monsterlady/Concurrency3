import java.util.Random;

public class Document {

    private final String data;

    /**
     * Produces a document with a random 8 character string (fake word)
     */
    Document(){
        Random random = new Random();
        char[] string = new char[8];
        for(int j = 0; j < string.length; j++)
        {
            string[j] = (char)('a' + random.nextInt(26));
        }
        this.data = new String(string);
    }

    /**
     * Returns our fake word
     * @return String
     */
    public String getData(){
        return data;
    }
}
