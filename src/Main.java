import java.io.IOException;
import java.util.Scanner;

/**
 * The Main class that runs the language detector
 *
 * @author bzhang464
 */
public class Main {

    /**
     * Runs the language detector
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a test string.");
        String input = scan.nextLine();
        scan.close();
        LanguageModel[] models = new LanguageModel[4];
        try{
            models[0] = new LanguageModel("English", "english.txt");
            models[1] = new LanguageModel("Spanish", "spanish.txt");
            models[2] = new LanguageModel("French","french.txt");
            models[3] = new LanguageModel("Italian", "italian.txt");
        } catch(IOException e) {
            e.printStackTrace();
        }
        double probsum = 0;
        LanguageModel max = models[0];
        for(LanguageModel x: models){
            probsum += x.probability(input);
            if(x.probability(input)>max.probability(input)){
                max = x;
            }
        }
        System.out.println("Analyzing: " + input);
        for(LanguageModel x: models){
            System.out.print("Probability that test string is  ");
            System.out.printf("%7s: %.2f\n", x.getName(), x.probability(input)/probsum);
        }
        System.out.println("Test string is most likely " + max.getName());
    }
}
