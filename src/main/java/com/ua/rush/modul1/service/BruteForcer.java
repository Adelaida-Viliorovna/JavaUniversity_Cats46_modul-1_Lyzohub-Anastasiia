package com.ua.rush.modul1.service;

import com.ua.rush.modul1.app.Application;
import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.util.Alphabet;
import com.ua.rush.modul1.util.TextShifter;

import java.io.IOException;
import java.util.List;

public class BruteForcer {
    private final FileManager fileManager = new FileManager();
//    private final Application app = new Application();
    private final TextShifter textShifter = new TextShifter();
    private final Alphabet alph = new Alphabet(textShifter);

    public void bruteForceDecryption(String filePath, Application app) {
        String fileName = fileManager.entryFileInPath(filePath);
        String text = "";
        try {
            text = fileManager.readFile(filePath);
            System.out.println(fileManager.FILE_READ_SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int maxAlpha = Math.max(alph.UPPER_EN.length, alph.UPPER_UA.length);
        class Candidate { int key; String plain; int score; String langGuess; }
        List<Candidate> candidates = new java.util.ArrayList<>();
        for (int k = 0; k < maxAlpha; k++) {
            String plain = textShifter.shiftText(text, -k);
            int scoreEn = alph.scoreEnglish(plain);
            int scoreUa = alph.scoreUkrainian(plain);
            int totalScore = Math.max(scoreEn, scoreUa);
            String langGuess = scoreEn >= scoreUa ? "EN" : "UA";
            Candidate c = new Candidate();
            c.key = k;
            c.plain = plain;
            c.score = totalScore;
            c.langGuess = langGuess;
            candidates.add(c);
        }
        candidates.sort((a, b) -> Integer.compare(b.score, a.score));
        Candidate best = candidates.get(0);
        String newFileName = fileManager.newNameFile(fileName, "BRUTEFORCED", best.key);
        String newFilePath = fileManager.newPathFile(filePath, newFileName);
        try {
            fileManager.writeFile(newFilePath, best.plain);
            System.out.println("Brute-force done. Best key: " + best.key + " (lang guess: " + best.langGuess + ")");
            System.out.println("Saved to: " + newFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Top candidates:");
        for (int i = 0; i < Math.min(3, candidates.size()); i++) {
            Candidate c = candidates.get(i);
            System.out.println("Key=" + c.key + " score=" + c.score + " lang=" + c.langGuess);
            System.out.println("--- snippet ---");
            System.out.println(textShifter.truncateForDisplay(c.plain, 400));
            System.out.println("----------------");
        }
        app.run();
    }
}
