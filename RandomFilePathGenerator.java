import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomFilePathGenerator {

    public static void main(String[] args) {
        int numberOfPaths = 50000; 
        String outputFilePath = "init.txt";

        try {
            generateRandomFilePaths(numberOfPaths, outputFilePath);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void generateRandomFilePaths(int count, String outputFilePath) throws IOException {
        Random random = new Random();
        String[] folders = {"documents", "downloads", "music", "pictures", "videos", "desktop", "temp"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (int i = 0; i < count; i++) {
                StringBuilder path = new StringBuilder();

                int depth = random.nextInt(3) + 2; // tree depth 2-4
                for (int j = 0; j < depth; j++) {
                    String folderName = folders[random.nextInt(folders.length)];
                    int randomNumber = random.nextInt(1000); // random number for folder name
                    path.append(folderName).append(randomNumber);
                    if (j < depth - 1) {
                        path.append("/"); // add spearator
                    }
                }

                int randomFileNumber = random.nextInt(1000);
                String fileName = "file" + randomFileNumber + ".txt";
                path.append("/").append(fileName);

                writer.write(path.toString());
                writer.newLine();
            }
        }
    }
}

