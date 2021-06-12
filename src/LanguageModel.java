import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;

/**
 * Represents a model of a language
 *
 * @author bzhang464
 */

public class LanguageModel{

    private final String name;
    private final double[][] probs;

    /**
     * Creates a language model
     *
     * @param name the name of the model
     * @param file the name of the language file
     * @throws IOException
     */
    public LanguageModel(String name, String file) throws IOException{
        int[][] counts = new int[26][26];
        this.name = name;
        System.out.print("Building "+this.name+" model ... ");
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String temp;
        while(null != (temp = reader.readLine())) {
            temp =
                    Normalizer
                            .normalize(temp, Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
            for(int i=0;i<temp.length()-1;i++) {
                int slow = temp.charAt(i);
                int fast = temp.charAt(i+1);
                counts[slow-'a'][fast-'a']++;
            }
        }
        reader.close();
        probs = new double[26][26];
        for(int i=0;i<26;i++){
            double rowtotal = 0;
            for(int j=0;j<26;j++){
                rowtotal = rowtotal + counts[i][j];
            }
            if(rowtotal==0){
                rowtotal = 1;
            }
            for(int k=0;k<26;k++){
                probs[i][k] = counts[i][k]/rowtotal;
                if(probs[i][k]==0){
                    probs[i][k]=0.01;
                }
            }
        }
        System.out.println("complete");
    }

    /**
     * Gives the name of the model
     *
     * @return name of the model
     */
    public String getName(){
        return name;
    }

    /**
     * Gives the model's table of probabilities
     *
     * @return table of probabilities
     */
    public String toString(){
        String result = " ";
        for(int i=0;i<26;i++){
            result = result + "    " + String.valueOf((char) ('a'+i));
        }
        result = result + "\n";
        for(int i=0;i<26;i++){
            result = result + String.valueOf((char) ('a'+i));
            for(int j=0;j<26;j++){
                result = result + " " + String.format("%.2f", probs[i][j]);
            }
            result = result + "\n";
        }
        return result;
    }

    /**
     * Gives the probability of the string being
     * from the model's language
     *
     * @param test the test string
     * @return the probability of the string being
     * from the model's language
     */
    public double probability(String test){
        double result = 1.0;
        char slow;
        char fast;
        String[] words = test.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        for(String word: words) {
            for(int i=0;i<word.length()-1;i++) {
                slow = word.charAt(i);
                fast = word.charAt(i+1);
                result = result*probs[slow-'a'][fast-'a'];
            }
        }
        return result;
    }
}
