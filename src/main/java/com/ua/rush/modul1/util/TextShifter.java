package com.ua.rush.modul1.util;

public class TextShifter {
    private final Alphabet alph = new Alphabet(this);
    public String shiftText(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            result.append(shiftChar(ch, key));
        }
        return result.toString();
    }
    private char shiftChar(char ch, int key) {
        char[] alphabet = null;
        if (Character.isUpperCase(ch)) {
            if (contains(alph.UPPER_EN, ch)) {
                alphabet = alph.UPPER_EN;
            }
            else if (contains(alph.UPPER_UA, ch)) {
                alphabet = alph.UPPER_UA;
            }
        } else if (Character.isLowerCase(ch)) {
            if (contains(alph.LOWER_EN, ch)) {
                alphabet = alph.LOWER_EN;
            }
            else if (contains(alph.LOWER_UA, ch)) {
                alphabet = alph.LOWER_UA;
            }
        } else if (contains(alph.PUNCTUATION, ch)) {
            alphabet = alph.PUNCTUATION;
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
    protected int countWordMatches(String lowerText, String[] words) {
        int score = 0;
        for (String w : words) {
            int idx = 0;
            while ((idx = lowerText.indexOf(w, idx)) != -1) {
                score += 10;
                idx += w.length();
            }
        }
        int letters = 0;
        for (char c : lowerText.toCharArray()) if (Character.isLetter(c)) letters++;
        if (lowerText.length() > 0) {
            score += (letters * 1) / 100;
        }
        return score;
    }
    public String truncateForDisplay(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...";
    }
}
