
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SearchWord {

    private static boolean checkIfWord(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    SearchWord(String inputFile, String outputFile, String searchTerm) throws FileNotFoundException, IOException {
        Scanner in = null;
        PrintWriter out = null;
        int lineNumber = 0;
        try {
            in = new Scanner(new BufferedReader(new FileReader(inputFile)));
            out = new PrintWriter(new FileWriter(outputFile));

            while (in.hasNextLine()) {
                lineNumber++;
                String s = in.nextLine();
                int index = 1;
                if (s.contains(searchTerm)) {
                    out.print(lineNumber + ": ");
                    while ((index > 0) & (index < s.length())) {
                        index = index + searchTerm.length();
                        index = s.indexOf(searchTerm, index);
                        if (index != -1) {
                            out.print(index + ", ");
                        }
                    }
                    out.println();
                }
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
        String searchTerm = "and";
        String outputFile = "/Users/cb-kiruthika/training/week2/day3/" + searchTerm + ".txt";
        SearchWord sw = new SearchWord(inputFile, outputFile, searchTerm);
    }

}
