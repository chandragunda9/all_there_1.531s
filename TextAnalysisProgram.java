import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TextAnalysisProgram {

    private static final Set<String> EXCLUDED_WORDS = new HashSet<>(Arrays.asList(
            // Prepositions
            "in", "on", "at", "for", "with", "by", "about", "as", "through", "over", "under",
            "between", "into", "during", "after", "before", "above", "below", "around", "along",
            "across", "near", "without", "towards", "upon", "within", "outside", "beneath", "beyond",
            "since", "despite", "among", "toward", "from", "of", "to",

            // Pronouns
            "he", "she", "it", "they", "them", "his", "her", "hers", "its", "their", "theirs", "this",
            "that", "these", "those", "who", "whom", "which", "whose", "i", "you", "we", "us", "me",
            "my", "myself", "your", "yourself", "yourselves", "our", "ours", "ourselves", "him",

            // Conjunctions
            "and", "but", "or", "nor", "so", "for", "yet", "although", "because", "since", "unless",
            "if", "while", "when", "where", "after", "before", "until", "though", "even", "as", "than",
            "either", "neither", "whether",

            // Articles
            "the", "a", "an",

            // Modal Verbs
            "can", "could", "will", "would", "shall", "should", "may", "might", "must", "ought", "need",
            "dare", "is", "was", "are", "were", "be", "been", "being"
    ));



    public static void main(String[] args) {
        // Path to the input text file
        String filePath = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
        try {
            //Creating a URL
            URL url = new URL(filePath);
            //Fetching file content using InputStream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            //Analyzing text
            analyzeText(bufferedReader);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void analyzeText(BufferedReader reader) {

        long startTime=System.currentTimeMillis();

        //Frequency map to store all the words and their counts
        Map<String, Integer> wordFreqCountMap = new HashMap<>();
        int totalWordCount = 0;
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] wordsInLine = line.toLowerCase().split("[^a-zA-Z]+");

                for (String word : wordsInLine) {
                    //1. word should not be empty
                    //2. must not be from excluded list
                    //3. when split chandra's - [chandra, s], so need to exclude 's' also in the list as the question says ignore plural postfix of ("'s")
                    if (!word.isEmpty() && !EXCLUDED_WORDS.contains(word) && !word.equals("s")) {
                        //increment frequency of the word
                        wordFreqCountMap.put(word, wordFreqCountMap.getOrDefault(word, 0) + 1);
                        //incrementing the total count of word
                        ++totalWordCount;
                    }
                }
            }
            reader.close();

            //Printing Total Word Count (excluding filtered words)
            System.out.println("Total word count (excluding filtered words): " + totalWordCount);

            //Get the top 5 most frequent words
            List<Map.Entry<String, Integer>> top5Words = wordFreqCountMap.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))  // Sorting by frequency (descending)
                    .limit(5)  // Get the top 5 words
                    .toList();

            System.out.println("\nTop 5 most frequent words:");
            for (int i = 0; i < top5Words.size(); i++) {
                System.out.println((i + 1) + ". " + top5Words.get(i).getKey() + ": " + top5Words.get(i).getValue());
            }

            System.out.println("\nTop 50 unique words (sorted alphabetically):");
            List<String> sortedUniqueWordList = wordFreqCountMap.keySet().stream().sorted().toList();
            for (int i = 0; i < Math.min(50, sortedUniqueWordList.size()); i++) {
                System.out.println((i + 1) + ". " + sortedUniqueWordList.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        finally {
            long endTime=System.currentTimeMillis();
            System.out.println("\nTime required to create the output is "+(endTime-startTime)/1000.0+" seconds");
        }

    }
}
