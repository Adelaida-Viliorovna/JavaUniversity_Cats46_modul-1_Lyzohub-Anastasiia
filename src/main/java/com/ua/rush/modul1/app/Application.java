package com.ua.rush.modul1.app;

import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.service.BruteForcer;
import com.ua.rush.modul1.service.Decryptor;
import com.ua.rush.modul1.service.Encryptor;

import java.util.Scanner;

public class Application {
    private final Encryptor encryptor = new Encryptor();
    private final Decryptor decryptor = new Decryptor();
    private final BruteForcer bruteForcer = new BruteForcer();
    private final FileManager fileManager = new FileManager();
    public final String EXIT_TEXT = "Exiting...";
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        if(input.equalsIgnoreCase("exit")) {
            System.out.println(EXIT_TEXT);
            return;
        }
        if (!input.isEmpty()) {
            commandOptions(input);
        } else {
            mainMenu();
        }
    }
    private void commandOptions(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 2 && parts.length != 3) {
            System.out.println("Invalid command format. Please try again.");
            run();
            return;
        }
        String action = "";
        String filePath = "";
        int key = 0;
        if (parts.length == 3) {
            action = parts[0].toUpperCase();
            filePath = parts[1];
            key = Integer.parseInt(parts[2]);
        }
        if (parts.length == 2) {
            action = parts[0].toUpperCase();
            filePath = parts[1];
        }
        switch (action) {
            case "ENCRYPT":
                encryptor.encryptMessage(filePath, key, this);
                break;
            case "DECRYPT":
                decryptor.decryptMessage(filePath, key, this);
                break;
            case "BRUTEFORCE":
            case "BRUTE_FORCE":
            case "BRUTE-FORCE":
                bruteForcer.bruteForceDecryption(filePath, this);
                break;
            default:
                System.out.println("Invalid action. Please try again.");
                run();
                break;
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
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        chooseOption(choice);
    }
    private void chooseOption(int choose) {
        String filePath;
        int key;
        switch (choose) {
            case 1:
                filePath = fileManager.enterPath();
                key = fileManager.enterKey();
                encryptor.encryptMessage(filePath, key, this);
                break;
            case 2:
                filePath = fileManager.enterPath();
                key = fileManager.enterKey();
                decryptor.decryptMessage(filePath, key, this);
                break;
            case 3:
                filePath = fileManager.enterPath();
                bruteForcer.bruteForceDecryption(filePath, this);
                break;
            case 0:
                System.out.println(EXIT_TEXT);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                mainMenu();
                break;
        }
    }
}
