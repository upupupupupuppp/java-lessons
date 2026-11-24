import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;


public class CaesarCipher {
    private static final double[] ENGLISH_FREQUENCIES = {
            8.12, 1.49, 2.71, 4.32, 12.02, 2.30,
            2.03, 5.92, 7.31, 0.10, 0.69, 3.98,
            2.61, 6.95, 7.68, 1.82, 0.11, 6.02,
            6.28, 9.10, 2.88, 1.11, 2.09, 0.17,
            2.11, 0.07
    };
    public static String encrypt(String text, int key) {
        return process(text, key);
    }
    public static String decrypt(String text, int key) {
        return process(text, -key);
    }
    private static String process(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            if (letter >= 'A' && letter <= 'Z') {
                int index = letter - 'A';
                int newIndex = Math.floorMod((index + shift ), 26);

                char newLetter = (char) ('A' + newIndex);
                result.append(newLetter);
            } else if (letter >= 'a' && letter <= 'z') {
                int index = letter - 'a';
                int newIndex = Math.floorMod((index + shift ), 26);

                char newLetter = (char) ('a' + newIndex);
                result.append(newLetter);
            }
            else {
                result.append(letter);
            }
        }
        return result.toString();
    }

    public static String readFile(String fileName) throws IOException {
        return Files.readString(Path.of(fileName));
    }
    public static void writeFile(String fileName, String text) throws IOException {
        Files.writeString(Path.of(fileName), text);
    }

    public static void processFile(String inputFile, String outputFile, int shift) throws IOException {
        if (Files.size(Path.of(inputFile)) == 0) {
            throw new IOException("Input file is empty");
        }
        try (
                BufferedReader reader = Files.newBufferedReader(Path.of(inputFile));
                BufferedWriter writer = Files.newBufferedWriter(Path.of(outputFile))

        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                String encryptedLine = process(line, shift);
                writer.write(encryptedLine);
                writer.newLine();
            }
        }

    }
    public static void bruteForce(String inputFile, String outputFile) throws IOException {
        if (Files.size(Path.of(inputFile)) == 0) {
            throw new IOException("Input file is empty");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(outputFile))) {
            for (int key = 0; key < 26; key++) {
                writer.write("KEY = " + key);
                try (BufferedReader reader = Files.newBufferedReader(Path.of(inputFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String decryptedLine = process(line, -key);
                        writer.write(decryptedLine);
                        writer.newLine();
                    }
                }
                writer.newLine();
            }
        }
    }

    public static void statisticalAnalysis(String inputFile, String outputFile) throws IOException {
        if (Files.size(Path.of(inputFile)) == 0) {
            throw new IOException("Input file is empty");
        }
        double bestScore = Double.MAX_VALUE;
        int bestKey = 0;

        for (int key = 0; key < 26; key++) {
            int[] letterCounts = new int[26];
            int totalLetters = 0;



        try (BufferedReader reader = Files.newBufferedReader(Path.of(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String decryptedLine = process(line, -key);
                for (int i = 0; i < decryptedLine.length(); i++) {
                    char letter = decryptedLine.charAt(i);
                    if (letter >= 'A' && letter <= 'Z') {
                        int index = letter - 'A';
                        letterCounts[index]++;
                        totalLetters++;
                    }
                }
            }
        }
            if (totalLetters == 0) {
                throw new IOException("Input file contains no English letters");
            }
            double score = 0;
            for (int i = 0; i < 26; i++) {
                double expected = ENGLISH_FREQUENCIES[i] * totalLetters / 100.0;
                double difference = letterCounts[i] - expected;
                score += (difference * difference) / expected;
            }
            if (score < bestScore) {
                bestScore = score;
                bestKey = key;
            }

    }
        processFile(inputFile, outputFile, -bestKey);
        System.out.println("Best key: " + bestKey);
}





}


