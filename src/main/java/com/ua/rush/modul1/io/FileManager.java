package com.ua.rush.modul1.io;

import java.io.*;
import java.util.Scanner;

public class FileManager {
    public final String FILE_READ_SUCCESS = "File read successfully.";

    public String enterPath() {
        System.out.println("Enter path to the file:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    public int enterKey() {
        System.out.println("Enter key (integer):");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    public String entryFileInPath(String filePath) {
        String[] parts = filePath.split("/");
        if (parts.length == 1) {
            parts = filePath.split("\\\\");
        }
        return parts[parts.length - 1];
    }
    public String newNameFile(String fileName, String operation, int key) {
        if (fileName == null || fileName.isEmpty()) {
            return "_[" + operation + "-" + key + "]";
        }
        int lastDot = fileName.lastIndexOf('.');
        String base;
        String ext;
        if (lastDot == -1) {
            base = fileName;
            ext = "";
        } else {
            base = fileName.substring(0, lastDot);
            ext = fileName.substring(lastDot);
        }
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
        int lastSlash = filePath.lastIndexOf('/');
        int lastBackslash = filePath.lastIndexOf('\\');
        int lastSep = Math.max(lastSlash, lastBackslash);
        if (lastSep == -1) {
            return newFileName;
        }
        String prefix = filePath.substring(0, lastSep + 1);
        return prefix + newFileName;
    }
    public String readFile(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        var data = is.readAllBytes();
        String text = new String(data);
        is.close();
        return text;
    }
    public void writeFile(String newFilePath, String newText) throws IOException {
        OutputStream os = new FileOutputStream(newFilePath);
        os.write(newText.getBytes());
        os.close();
    }
}
