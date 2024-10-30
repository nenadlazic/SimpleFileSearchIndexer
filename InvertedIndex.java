import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InvertedIndex {
    private final Map<String, Set<String>> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }

    // Add file path in inverted index
    public void addFilePath(String filePath) {
        // Tokenization phase 1 - split by '/' 
        String[] parts = filePath.split("/"); 

        // Add token to index and generated prefix indexes 3+ character 
        for (String part : parts) {
            if (part.length() >= 3) { // Add prefix tokens 3 or more character
                addPrefixes(part, filePath);
            }
            index.computeIfAbsent(part, k -> new HashSet<>()).add(filePath);
        }

        // File name tokenization
        if (parts.length > 0) {
            String fileName = parts[parts.length - 1];
            addPrefixes(fileName, filePath); // Add prefix tokens 3 or more character
        }
    }

    // Generate prefix indexes 3 or more characters
    private void addPrefixes(String word, String filePath) {
        for (int i = 0; i < word.length(); i++) {
            for (int j = 3; j <= word.length() - i; j++) { // Prelazi kroz sve dužine prefiksa od 3 do dužine reči
                String prefix = word.substring(i, i + j);
                index.computeIfAbsent(prefix, k -> new HashSet<>()).add(filePath);
            }
        }
    }

    // Search by index
    public Set<String> search(String query) {
        return index.getOrDefault(query, Collections.emptySet());
    }

    // Print index
    public void printIndex() {
        System.out.println("Current index:");
        for (Map.Entry<String, Set<String>> entry : index.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }

    // Index initialization
    public void populateFromFile(String fileName) {
        long startTime = System.currentTimeMillis();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                addFilePath(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Index initialization error: " + e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        
        System.out.println("Initial indexing duration: " + duration + " ms");
    }

    public static void main(String[] args) {
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.populateFromFile("init.txt"); // Index init 

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("####################################################################################\n");
            System.out.println("Choose option: ");
            System.out.println("1. ADD_FILE_PATH");
            System.out.println("2. PRINT_INDEX");
            System.out.println("3. SEARCH");
            System.out.println("4. EXIT");
            System.out.print("####################################################################################\n");
            System.out.print("Option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine();
                    invertedIndex.addFilePath(filePath);
                    System.out.println("Path '" + filePath + "' is added.");
                    break;

                case "2":
                    invertedIndex.printIndex();
                    break;

                case "3":
                    System.out.print("Enter word for search (3 or more characters): ");
                    String searchTerm = scanner.nextLine();
                    Set<String> results = invertedIndex.search(searchTerm);
                    System.out.println("Results for '" + searchTerm + "':");
                    if (results.isEmpty()) {
                        System.out.println("Not found.");
                    } else {
                        for (String result : results) {
                            System.out.println(result);
                        }
                    }
                    break;

                case "4":
                    System.out.println("Exit.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Unknown option. Try again.");
                    break;
            }
        }
    }
}
