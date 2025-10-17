package com.ua.rush.modul1.util;

import java.util.Locale;

public class TextShifter {

    public String shiftText(String text, int key) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) {
            sb.append(shiftChar(ch, key));
        }
        return sb.toString();
    }

    private char shiftChar(char ch, int key) {
        char[] alphabet = null;
        if (Character.isUpperCase(ch)) {
            if (contains(Alphabet.UPPER_EN, ch)) {
                alphabet = Alphabet.UPPER_EN;
            }
            else if (contains(Alphabet.UPPER_UA, ch)) {
                alphabet = Alphabet.UPPER_UA;
            }
        } else if (Character.isLowerCase(ch)) {
            if (contains(Alphabet.LOWER_EN, ch)) {
                alphabet = Alphabet.LOWER_EN;
            }
            else if (contains(Alphabet.LOWER_UA, ch)) {
                alphabet = Alphabet.LOWER_UA;
            }
        } else if (contains(Alphabet.PUNCTUATION, ch)) {
            alphabet = Alphabet.PUNCTUATION;
        }
        if (alphabet == null) {
            return ch;
        }
        int idx = indexOf(alphabet, ch);
        if (idx < 0) {
            return ch;
        }
        int newIdx = Math.floorMod(idx + key, alphabet.length);
        return alphabet[newIdx];
    }

    private boolean contains(char[] arr, char c) {
        for (char ch : arr) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }

    private int indexOf(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public int scoreEnglish(String text) {
        String[] commonEn = {" the ", " be ", " to ", " of ", " and ", " a ", " in ", " that ", " is ", " it ", " for "};
        return countWordMatches(text == null ? "" : text.toLowerCase(Locale.ROOT), commonEn);
    }

    public int scoreUkrainian(String text) {
        String[] commonUa = {" і ", " в ", " не ", " на ", " що ", " він ", " я ", " це ", " до "};
        return countWordMatches(text == null ? "" : text.toLowerCase(Locale.ROOT), commonUa);
    }

    public int countWordMatches(String lowerText, String[] words) {
        int score = 0;
        for (String w : words) {
            int idx = 0;
            while ((idx = lowerText.indexOf(w, idx)) != -1) {
                score += 10;
                idx += w.length();
            }
        }
        int letters = 0;
        for (char c : lowerText.toCharArray()) {
            if (Character.isLetter(c)) {
                letters++;
            }
        }
        if (!lowerText.isEmpty()) {
            score += letters / 100;
        }
        return score;
    }

}
