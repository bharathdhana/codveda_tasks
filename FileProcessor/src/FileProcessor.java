import java.io.*;

public class FileProcessor {
    static void main(String[] args) {
        String inputFile = args.length > 0 ? args[0] : "src/input.txt";
        String outputFile = args.length > 1 ? args[1] : "src/output.txt";

        int lineCount = 0, wordCount = 0, charCount = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                charCount += line.length();
                String trimmedLine = line.trim();

                if(!trimmedLine.isEmpty()) {
                    wordCount += trimmedLine.split("\\s+").length;
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("File " + inputFile + " not found.");
            return;
        } catch (IOException e) {
            System.out.println("Error reading file " + inputFile + ".");
            return;
        }


        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write("result after reading..");
            bw.newLine();
            bw.write("-------------------");
            bw.newLine();
            bw.write("Input file: " + inputFile);
            bw.newLine();
            bw.write("Lines: " + lineCount);
            bw.newLine();
            bw.write("Words: " + wordCount);
            bw.newLine();
            bw.write("Characters: " + charCount);
            bw.newLine();
            System.out.print("Processing Complete. results written to " + outputFile);
        } catch (IOException e) {
            System.out.println("Error writing to " + outputFile + ".");
        }
    }
}
