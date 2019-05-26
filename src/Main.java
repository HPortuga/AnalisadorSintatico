import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String input = lerArquivo("code.xpp");

        Parser parser = new Parser(input);
        parser.program();
    }

    private static String lerArquivo(String fileName) {
        String input = "";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                input += (line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

         return input;
    }
}
