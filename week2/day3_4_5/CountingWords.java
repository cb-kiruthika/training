
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class CountingWords {
    
    private static boolean checkIfWord(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
    private Map<String, Integer> wordMap = new TreeMap<>();

    CountingWords(String inputFile, String outputFile) throws FileNotFoundException, IOException {
        Scanner in = null;
        PrintWriter out = null;
        try {
            in = new Scanner(new BufferedReader(new FileReader(inputFile)));
            out = new PrintWriter(new FileWriter(outputFile));

            String s;
            while (in.hasNext()) {
                s = in.next().toLowerCase();
                if (CountingWords.checkIfWord(s)) {
                    if (this.wordMap.containsKey(s)) {
                        this.wordMap.put(s, this.wordMap.get(s) + 1);
                    } else {
                        this.wordMap.put(s, 1);
                    }
                }
            }
            for (String str : this.wordMap.keySet()) {
                out.println(str + ":  " + this.wordMap.get(str));
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String inputFile = "/Users/cb-kiruthika/training/week2/day3/textDoc.txt";
        String outputFile = "/Users/cb-kiruthika/training/week2/day3/wordCount.txt";
        CountingWords cw = new CountingWords(inputFile, outputFile);
    }

}
