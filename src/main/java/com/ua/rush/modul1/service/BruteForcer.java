package com.ua.rush.modul1.service;

import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.util.Alphabet;
import com.ua.rush.modul1.util.TextShifter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BruteForcer {
    private final FileManager fileManager;
    private final TextShifter shifter;

    public BruteForcer(FileManager fileManager, TextShifter shifter) {
        this.fileManager = fileManager;
        this.shifter = shifter;
    }

    private static final class Candidate {
        final int key;
        final String plain;
        final int score;
        final String lang;

        Candidate(int key, String plain, int score, String lang) {
            this.key = key; this.plain = plain; this.score = score; this.lang = lang;
        }
    }

    public void bruteForceDecryption(String filePath) {
        String fileName = fileManager.entryFileInPath(filePath);
        String text;
        try {
            text = fileManager.readFile(filePath);
            System.out.println(fileManager.FILE_READ_SUCCESS);
        } catch (IOException e) {
            System.out.println("Unable to read file: " + e.getMessage());
            return;
        }

        int maxAlpha = Math.max(Alphabet.UPPER_EN.length, Alphabet.UPPER_UA.length);
        List<Candidate> candidates = new ArrayList<>();
        for (int k = 0; k < maxAlpha; k++) {
            String plain = shifter.shiftText(text, -k);
            int scoreEn = shifter.scoreEnglish(plain);
            int scoreUa = shifter.scoreUkrainian(plain);
            int total = Math.max(scoreEn, scoreUa);
            String lang = scoreEn >= scoreUa ? "EN" : "UA";
            candidates.add(new Candidate(k, plain, total, lang));
        }

        candidates.sort((a, b) -> Integer.compare(b.score, a.score));
        if (candidates.isEmpty()) {
            System.out.println("No candidates found.");
            return;
        }
        Candidate best = candidates.get(0);
        String newFileName = fileManager.newNameFile(fileName, "BRUTEFORCED", best.key);
        String newFilePath = fileManager.newPathFile(filePath, newFileName);
        try {
            fileManager.writeFile(newFilePath, best.plain);
            System.out.println("Brute-force done. Best key: " + best.key + " (lang: " + best.lang + ")");
            System.out.println("Saved to: " + newFilePath);
        } catch (IOException e) {
            System.out.println("Unable to write file: " + e.getMessage());
        }

    }
}
