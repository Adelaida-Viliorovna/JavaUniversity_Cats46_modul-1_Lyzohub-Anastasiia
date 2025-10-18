package com.ua.rush.modul1.service;

import com.ua.rush.modul1.io.FileManager;
import com.ua.rush.modul1.util.Alphabet;
import com.ua.rush.modul1.util.TextShifter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
        final double combinedScore;
        final String lang;

        Candidate(int key, String plain, double combinedScore, String lang) {
            this.key = key;
            this.plain = plain;
            this.combinedScore = combinedScore;
            this.lang = lang;
        }
    }

    private static final double[] EN_FREQ = {
            8.167, 1.492, 2.782, 4.253, 12.702, 2.228, 2.015, 6.094, 6.966, 0.153,
            0.772, 4.025, 2.406, 6.749, 7.507, 1.929, 0.095, 5.987, 6.327, 9.056,
            2.758, 0.978, 2.360, 0.150, 1.974, 0.074
    };

    private static final double[] UA_FREQ = {
            6.0, // А
            1.0, // Б
            4.5, // В
            1.2, // Г
            0.5, // Ґ (rare)
            1.6, // Д
            0.8, // Е
            1.1, // Є
            0.7, // Ж
            1.9, // З
            6.5, // И (approx combined І/И handling below)
            6.0, // І
            0.6, // Ї
            1.4, // Й
            2.8, // К
            6.2, // Л
            6.9, // М
            3.5, // Н
            1.8, // О
            4.5, // П
            3.0, // Р
            3.8, // С
            1.7, // Т
            0.5, // У
            1.8, // Ф
            1.0, // Х
            1.2, // Ц
            0.9, // Ч
            0.8, // Ш
            0.1, // Щ (rare)
            0.2, // Ь
            0.6, // Ю
            1.5  // Я
    };

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

            int scoreEnWords = shifter.scoreEnglish(plain);
            int scoreUaWords = shifter.scoreUkrainian(plain);

            double freqEn = frequencyScoreNormalizedEnglish(plain);
            double freqUa = frequencyScoreNormalizedUkrainian(plain);

            boolean likelyEn = scoreEnWords >= scoreUaWords;

            double weightWords = 1.0;
            double weightFreq = 50.0;

            double combined;
            String lang;
            if (likelyEn) {
                combined = weightWords * scoreEnWords + weightFreq * freqEn;
                lang = "EN";
            } else {
                combined = weightWords * scoreUaWords + weightFreq * freqUa;
                lang = "UA";
            }

            candidates.add(new Candidate(k, plain, combined, lang));
        }

        candidates.sort(Comparator.comparingDouble((Candidate c) -> c.combinedScore).reversed());

        if (candidates.isEmpty()) {
            System.out.println("No candidates found.");
            return;
        }

        int topN = Math.min(3, candidates.size());
        System.out.println("Top " + topN + " candidates:");
        for (int i = 0; i < topN; i++) {
            Candidate c = candidates.get(i);
            String preview = getPreview(c.plain);
            System.out.printf("%d) key=%d lang=%s score=%.3f preview=\"%s\"%n", i + 1, c.key, c.lang, c.combinedScore, preview);
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

    private String getPreview(String text) {
        if (text == null) return "";
        String singleLine = text.replace("\n", " ").replace("\r", " ");
        singleLine = singleLine.trim();
        if (singleLine.length() <= 120) return singleLine;
        return singleLine.substring(0, 120) + "...";
    }

    /* =========================
       Частотний аналіз (χ²)
       ========================= */

    private double chiSquareEnglish(String text) {
        int[] counts = new int[26];
        int total = 0;
        for (char ch : text.toCharArray()) {
            char uc = Character.toUpperCase(ch);
            if (uc >= 'A' && uc <= 'Z') {
                counts[uc - 'A']++;
                total++;
            }
        }
        if (total == 0) return Double.POSITIVE_INFINITY;

        double chi2 = 0.0;
        for (int i = 0; i < 26; i++) {
            double observed = counts[i];
            double expected = EN_FREQ[i] / 100.0 * total;
            double denom = expected > 0.0 ? expected : 0.0001;
            double diff = observed - expected;
            chi2 += (diff * diff) / denom;
        }
        return chi2;
    }

    private double chiSquareUkrainian(String text) {
        char[] ua = Alphabet.UPPER_UA;
        int m = ua.length;
        int[] counts = new int[m];
        int total = 0;
        for (char ch : text.toCharArray()) {
            char uc = Character.toUpperCase(ch);
            int idx = indexOf(ua, uc);
            if (idx >= 0) {
                counts[idx]++;
                total++;
            }
        }
        if (total == 0) return Double.POSITIVE_INFINITY;

        double chi2 = 0.0;
        int len = Math.min(UA_FREQ.length, counts.length);
        for (int i = 0; i < len; i++) {
            double observed = counts[i];
            double expected = UA_FREQ[i] / 100.0 * total;
            double denom = expected > 0.0 ? expected : 0.0001;
            double diff = observed - expected;
            chi2 += (diff * diff) / denom;
        }
        return chi2;
    }

    private double frequencyScoreNormalizedEnglish(String text) {
        double chi2 = chiSquareEnglish(text);
        if (Double.isInfinite(chi2)) return 0.0;
        return 1.0 / (1.0 + chi2);
    }

    private double frequencyScoreNormalizedUkrainian(String text) {
        double chi2 = chiSquareUkrainian(text);
        if (Double.isInfinite(chi2)) return 0.0;
        return 1.0 / (1.0 + chi2);
    }

    private int indexOf(char[] arr, char c) {
        if (arr == null) return -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c) return i;
        }
        return -1;
    }
}
