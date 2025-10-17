package com.ua.rush.modul1.service;

import com.ua.rush.modul1.app.Application;
import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.util.TextShifter;

import java.io.IOException;

public class Decryptor {
    private final FileManager fileManager = new FileManager();
//    private final Application app = new Application();
    private final TextShifter textShifter = new TextShifter();

    public void decryptMessage(String filePath, int key, Application app) {
        String fileName = fileManager.entryFileInPath(filePath);
        String newFileName = fileManager.newNameFile(fileName, "DECRYPTED", key);
        String newFilePath = fileManager.newPathFile(filePath, newFileName);
        String text = "";
        try {
            text = fileManager.readFile(filePath);
            System.out.println(fileManager.FILE_READ_SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String newText = textShifter.shiftText(text, -key);
        try {
            fileManager.writeFile(newFilePath, newText);
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
        app.run();
    }
}
