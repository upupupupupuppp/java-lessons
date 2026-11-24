import java.util.Scanner;
import java.io.IOException;
public class Main {
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CAESAR CIPHER ===");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        System.out.println("3. Brute force");
        System.out.println("4. Statistical analysis");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a number!");
            return;
        }
        int choice = scanner.nextInt();
        if (choice == 0) {
            System.out.println("Goodbye!");
            return;
        } else if (choice < 0 || choice > 4) {
            System.out.println("wrong number");
            return;
        }

        int key = 0;

        if (choice == 1 || choice == 2) {
            System.out.print("Enter key: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number!");
                return;
            }
            key = scanner.nextInt();
            key = Math.floorMod(key, 26);
        }

        if (choice == 1) {
            try {
                CaesarCipher.processFile("src/input.txt", "src/output.txt", key);
                System.out.println("Encryption completed!");
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }

        } else if (choice == 2) {
            try {
                CaesarCipher.processFile("src/output.txt", "src/decrypted.txt", -key);
                System.out.println("Decryption completed!");
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        } else if (choice == 3) {
            try {
                CaesarCipher.bruteForce("src/output.txt", "src/brute_force.txt");
                System.out.println("Brute force completed!");
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        } else if (choice == 4) {
            try {
                CaesarCipher.statisticalAnalysis("src/output.txt", "src/statistical.txt");
                System.out.println("Statistical analysis completed!");
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }


    }
}
