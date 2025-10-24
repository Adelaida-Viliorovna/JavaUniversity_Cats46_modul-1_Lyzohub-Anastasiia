package com.ua.rush.modul1.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Scanner;

public class FileManager {
    public static final String FILE_READ_SUCCESS = "File read successfully.";
    private final Scanner scanner;

    public FileManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public String enterPath() {
        System.out.println("Enter path to the file:");
        return scanner.nextLine().trim();
    }

    public int enterKey() {
        System.out.println("Enter key (integer):");
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again:");
            }
        }
    }

    public String entryFileInPath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        Path p = Paths.get(filePath);
        Path name = p.getFileName();
        return name == null ? filePath : name.toString();
    }

    public String newNameFile(String fileName, String operation, int key) {
        if (fileName == null || fileName.isEmpty()) {
            return "_[" + operation + "-" + key + "]";
        }
        int lastDot = fileName.lastIndexOf('.');
        String base = lastDot == -1 ? fileName : fileName.substring(0, lastDot);
        String ext = lastDot == -1 ? "" : fileName.substring(lastDot);
        int lastOpStart = base.lastIndexOf("_[");
        if (lastOpStart != -1 && base.endsWith("]")) {
            base = base.substring(0, lastOpStart);
        }
        return base + "_[" + operation + "-" + key + "]" + ext;
    }

    public String newPathFile(String filePath, String newFileName) {
        if (filePath == null || filePath.isEmpty()) {
            return newFileName;
        }
        Path p = Paths.get(filePath);
        Path parent = p.getParent();
        return parent == null ? newFileName : parent.resolve(newFileName).toString();
    }

    public String readFile(String filePath) throws IOException {
        Path p = Paths.get(filePath);
        return Files.readString(p, StandardCharsets.UTF_8);
    }

    public void writeFile(String newFilePath, String newText) throws IOException {
        Path p = Paths.get(newFilePath);
        Path parent = p.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(p, newText == null ? "" : newText, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
