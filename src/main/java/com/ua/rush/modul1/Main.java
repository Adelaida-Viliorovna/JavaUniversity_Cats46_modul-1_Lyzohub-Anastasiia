package com.ua.rush.modul1;

import java.io.*;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

public class Main {
    private final char[] UPPER_EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final char[] LOWER_EN = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final char[] UPPER_UA = "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ".toCharArray();
    private final char[] LOWER_UA = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя".toCharArray();
    private final char[] PUNCTUATION = ".,«»\"':!? ".toCharArray();
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
    private String newPathFile(String filePath, String newFileName) {
            if (filePath == null || filePath.isEmpty()) {
                return newFileName;
            }
            int lastSlash = filePath.lastIndexOf('/');
            int lastBackslash = filePath.lastIndexOf('\\');
            int lastSep = Math.max(lastSlash, lastBackslash);
            if (lastSep == -1) {
                return newFileName;
            }
            String prefix = filePath.substring(0, lastSep + 1);
            return prefix + newFileName;
    }
    private String readFile(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        var data = is.readAllBytes();
        String text = new String(data);
        is.close();
        return text;
    }
    private String shiftText(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            result.append(shiftChar(ch, key));
        }
        return result.toString();
    }
    private char shiftChar(char ch, int key) {
        char[] alphabet = null;
        if (Character.isUpperCase(ch)) {
            if (contains(UPPER_EN, ch)) {
                alphabet = UPPER_EN;
            }
            else if (contains(UPPER_UA, ch)) {
                alphabet = UPPER_UA;
            }
        } else if (Character.isLowerCase(ch)) {
            if (contains(LOWER_EN, ch)) {
                alphabet = LOWER_EN;
            }
            else if (contains(LOWER_UA, ch)) {
                alphabet = LOWER_UA;
            }
        } else if (contains(PUNCTUATION, ch)) {
            alphabet = PUNCTUATION;
        }
        if (alphabet == null) {
            return ch;
        }
        int index = indexOf(alphabet, ch);
        int newIndex = (index + key) % alphabet.length;
        if (newIndex < 0) {
            newIndex += alphabet.length;
        }
        return alphabet[newIndex];
    }
    private boolean contains(char[] array, char ch) {
        for (char c : array) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }
    private int indexOf(char[] array, char ch) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ch) {
                return i;
            }
        }
        return -1;
    }
    private void writeFile(String newFilePath, String newText) throws IOException {
        OutputStream os = new FileOutputStream(newFilePath);
        os.write(newText.getBytes());
        os.close();
    }
    private void encryptMessage(String filePath, int key) {
        String fileName = entryFileInPath(filePath);
        String newFileName = newNameFile(fileName, "[ENCRYPTED]");
        String newFilePath = newPathFile(filePath, newFileName);
        String text = "";
        try {
            text = readFile(filePath);
            System.out.println("File read successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String newText = shiftText(text, key);
        try {
            writeFile(newFilePath, newText);
            System.out.println("Encrypted successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------------------");
        System.out.println("Encrypting file " + fileName + " at " + filePath + " with key " + key);
        System.out.println("New file name: " + newFileName);
        System.out.println("New file path: " + newFilePath);
        System.out.println("File content: " + text);
        System.out.println("Encrypted content: " + newText);
        System.out.println("------------------------------");
        run();
    }
    private void decryptMessage(String filePath, int key) {
        String fileName = entryFileInPath(filePath);
        String newFileName = newNameFile(fileName, "[DECRYPTED]");
        String newFilePath = newPathFile(filePath, newFileName);
        String text = "";
        try {
            text = readFile(filePath);
            System.out.println("File read successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String newText = shiftText(text, -key);
        try {
            writeFile(newFilePath, newText);
            System.out.println("Decrypted successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------------------");
        System.out.println("Decrypting file " + fileName + " at " + filePath + " with key " + key);
        System.out.println("New file name: " + newFileName);
        System.out.println("New file path: " + newFilePath);
        System.out.println("File content: " + text);
        System.out.println("Decrypted content: " + newText);
        System.out.println("------------------------------");
        run();
    }
    private void bruteForceDecryption(String filePath) {
        String fileName = entryFileInPath(filePath);
        int key = 0; // In a real scenario, this would be determined by the brute force process
        String newFileName = newNameFile(fileName, "[BRUTEFORCED]" + key);
        String text = "";
        try {
            text = readFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------------------");
        System.out.println("Brute force decrypting file " + fileName + " at " + filePath);
        System.out.println("New file name: " + newFileName);
        System.out.println("New file path: " + newPathFile(filePath, newFileName));
        System.out.println("File content: " + text);

        System.out.println("------------------------------");

    }

}
