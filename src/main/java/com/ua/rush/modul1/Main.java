package com.ua.rush.modul1;

import java.io.*;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }
    private void run() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        if (input.length() != 0) {
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
                encryptMessage(filePath, key);
                break;
            case "DECRYPT":
                decryptMessage(filePath, key);
                break;
            case "BRUTEFORCE":
            case "BRUTE_FORCE":
            case "BRUTE-FORCE":
                bruteForceDecryption(filePath);
                break;
            default:
                System.out.println("Invalid action. Please try again.");
                run();
                break;
        }

    }
    private int mainMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Encrypt a message");
        System.out.println("2. Decrypt a message");
        System.out.println("3. Brute force decryption");
        System.out.println("0. Exit");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        chooseOption(choice);
        return choice;
    }
    private void chooseOption(int choose) {
        String filePath;
        int key;
        switch (choose) {
            case 1:
                filePath = enterPath();
                key = enterKey();
                encryptMessage(filePath, key);
                break;
            case 2:
                filePath = enterPath();
                key = enterKey();
                decryptMessage(filePath, key);
                break;
            case 3:
                filePath = enterPath();
                bruteForceDecryption(filePath);
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                mainMenu();
                break;
        }
    }
    private String enterPath() {
        System.out.println("Enter path to the file:");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        return filePath;
    }
    private int enterKey() {
        System.out.println("Enter key (integer):");
        Scanner scanner = new Scanner(System.in);
        int key = scanner.nextInt();
        return key;
    }
    private String entryFileInPath(String filePath) {
        String[] parts = filePath.split("/");
        if (parts.length == 1) {
            parts = filePath.split("\\\\");
        }
        return parts[parts.length - 1];
    }
    private String newNameFile(String fileName, String operation) {
        String newFileName = "";
        String[] parts = fileName.split("\\.");
        String name = parts[0];
        String extension = parts[parts.length - 1];
        newFileName = name + "_" + operation + "." + extension;
        return newFileName;
    }
    private void readFile(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        var data = is.readAllBytes();
        String text = new String(data);
        System.out.println("File content: " + text);
        is.close();
    }
    private void encryptMessage(String filePath, int key) {
        String fileName = entryFileInPath(filePath);
        System.out.println("Encrypting file " + fileName + " at " + filePath + " with key " + key);
        String newFileName = newNameFile(fileName, "[ENCRYPTED]");
        System.out.println("New file name: " + newFileName);
        try {
            readFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void decryptMessage(String filePath, int key) {
        String fileName = entryFileInPath(filePath);
        System.out.println("Decrypting file " + fileName + " at " + filePath + " with key " + key);
        String newFileName = newNameFile(fileName, "[DECRYPTED]");
        System.out.println("New file name: " + newFileName);
    }
    private void bruteForceDecryption(String filePath) {
        String fileName = entryFileInPath(filePath);
        System.out.println("Brute force decrypting file " + fileName + " at " + filePath);
        int key = 0; // In a real scenario, this would be determined by the brute force process
        String newFileName = newNameFile(fileName, "[BRUTEFORCED]" + key);
        System.out.println("New file name: " + newFileName);
    }

}
