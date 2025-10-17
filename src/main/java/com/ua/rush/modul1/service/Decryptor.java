package com.ua.rush.modul1.service;

import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.util.TextShifter;

import java.io.IOException;

public class Decryptor {
    private final FileManager fileManager;
    private final TextShifter shifter;

    public Decryptor(FileManager fileManager, TextShifter shifter) {
        this.fileManager = fileManager;
        this.shifter = shifter;
    }

    public void decryptMessage(String filePath, int key) {
        String fileName = fileManager.entryFileInPath(filePath);
        String newFileName = fileManager.newNameFile(fileName, "DECRYPTED", key);
        String newFilePath = fileManager.newPathFile(filePath, newFileName);
        String text;
        try {
            text = fileManager.readFile(filePath);
            System.out.println(fileManager.FILE_READ_SUCCESS);
        } catch (IOException e) {
            System.out.println("Unable to read file: " + e.getMessage());
            return;
        }
        String newText = shifter.shiftText(text, -key);
        try {
            fileManager.writeFile(newFilePath, newText);
            System.out.println("Decrypted successfully. Saved to: " + newFilePath);
        } catch (IOException e) {
            System.out.println("Unable to write file: " + e.getMessage());
        }
    }
}
