package com.ua.rush.modul1.app;

import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.service.BruteForcer;
import com.ua.rush.modul1.service.Decryptor;
import com.ua.rush.modul1.service.Encryptor;
import com.ua.rush.modul1.util.TextShifter;

import java.util.Scanner;

public class Application {
    private final Scanner scanner = new Scanner(System.in);
    private final FileManager fileManager = new FileManager(scanner);
    private final TextShifter textShifter = new TextShifter();
    private final Encryptor encryptor = new Encryptor(fileManager, textShifter);
    private final Decryptor decryptor = new Decryptor(fileManager, textShifter);
    private final BruteForcer bruteForcer = new BruteForcer(fileManager, textShifter);

    public final String EXIT_TEXT = "Exiting...";
    private boolean running = true;

    public void run() {
        while (running) {
            System.out.println("Enter command or press Enter for menu (type exit to quit):");
            String input = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println(EXIT_TEXT);
                running = false;
                break;
            }
            if (!input.isEmpty()) {
                commandOptions(input);
            }
            else mainMenu();
        }
        scanner.close();
    }

    private void commandOptions(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 2 && parts.length != 3) {
            System.out.println("Invalid command format.");
            return;
        }
        String action = parts[0].toUpperCase();
        String filePath = parts[1];
        Integer key = null;
        if (parts.length == 3) {
            try {
                key = Integer.parseInt(parts[2]);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid key.");
                return;
            }
        }

        switch (action) {
            case "ENCRYPT":
                if (key == null) {
                    System.out.println("Key required.");
                    return;
                }
                encryptor.encryptMessage(filePath, key);
                break;
            case "DECRYPT":
                if (key == null) {
                    System.out.println("Key required.");
                    return;
                }
                decryptor.decryptMessage(filePath, key);
                break;
            case "BRUTEFORCE":
            case "BRUTE_FORCE":
            case "BRUTE-FORCE":
                bruteForcer.bruteForceDecryption(filePath);
                break;
            default:
                System.out.println("Invalid action.");
        }
    }

    private void mainMenu() {
        System.out.println("""
                Choose an option:
                1. Encrypt a message
                2. Decrypt a message
                3. Brute force decryption
                0. Exit
                """);
        String line = scanner.nextLine().trim();
        int choice;
        try {
            choice = Integer.parseInt(line);
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        switch (choice) {
            case 1 -> {
                String filePath = fileManager.enterPath();
                int key = fileManager.enterKey();
                encryptor.encryptMessage(filePath, key);
            }
            case 2 -> {
                String filePath = fileManager.enterPath();
                int key = fileManager.enterKey();
                decryptor.decryptMessage(filePath, key);
            }
            case 3 -> {
                String filePath = fileManager.enterPath();
                bruteForcer.bruteForceDecryption(filePath);
            }
            case 0 -> {
                System.out.println(EXIT_TEXT);
                running = false;
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}
