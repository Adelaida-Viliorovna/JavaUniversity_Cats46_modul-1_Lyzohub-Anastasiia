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

    public static final String EXIT_TEXT = "Exiting...";
    private boolean running = true;

    private final String[] startupArgs;

    public Application() {
        this(new String[0]);
    }

    public Application(String[] args) {
        this.startupArgs = args == null ? new String[0] : args.clone();
    }

    public void run() {
        if (startupArgs != null && startupArgs.length >= 1) {
            boolean handled = handleStartupArgs(startupArgs);
            if (handled) {
                scanner.close();
                return;
            } else {
                System.out.println("Invalid startup arguments. Entering interactive mode.");
            }
        }

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
            } else {
                mainMenu();
            }
        }
        scanner.close();
    }

    private boolean handleStartupArgs(String[] args) {
        if (args.length < 2) {
            return false;
        }

        String action = args[0].toUpperCase();
        Integer key = null;
        int lastIndex = args.length - 1;

        try {
            key = Integer.parseInt(args[lastIndex]);
        } catch (NumberFormatException ignored) {
            key = null;
        }

        String filePath;
        if (key != null && args.length >= 3) {
            filePath = joinArgsRange(args, 1, lastIndex - 1);
        } else {
            filePath = joinArgsRange(args, 1, lastIndex);
        }

        return processAction(action, filePath, key);
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
            } catch (NumberFormatException e) {
                System.out.println("Invalid key.");
                return;
            }
        }

        if (!processAction(action, filePath, key)) {
            System.out.println("Action failed or invalid.");
        }
    }

    private boolean processAction(String action, String filePath, Integer key) {
        switch (action) {
            case "ENCRYPT" -> {
                if (key == null) {
                    System.out.println("Key required for ENCRYPT.");
                    return false;
                }
                encryptor.encryptMessage(filePath, key);
            }
            case "DECRYPT" -> {
                if (key == null) {
                    System.out.println("Key required for DECRYPT.");
                    return false;
                }
                decryptor.decryptMessage(filePath, key);
            }
            case "BRUTEFORCE", "BRUTE_FORCE", "BRUTE-FORCE" -> {
                bruteForcer.bruteForceDecryption(filePath);
            }
            default -> {
                System.out.println("Invalid action: " + action);
                return false;
            }
        }
        return true;
    }

    private String joinArgsRange(String[] args, int i, int j) {
        if (i > j || i < 0 || j >= args.length) return "";
        StringBuilder sb = new StringBuilder();
        for (int k = i; k <= j; k++) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(args[k]);
        }
        return sb.toString().trim();
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
        } catch (NumberFormatException e) {
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
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}
