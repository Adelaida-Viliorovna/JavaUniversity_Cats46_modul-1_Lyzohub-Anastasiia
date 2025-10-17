package com.ua.rush.modul1.util;

import com.ua.rush.modul1.util.TextShifter;

public class Alphabet {
    private final TextShifter textShifter;

    public static final char[] UPPER_EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final char[] LOWER_EN = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static final char[] UPPER_UA = "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ".toCharArray();
    public static final char[] LOWER_UA = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя".toCharArray();
    public static final char[] PUNCTUATION = ".,«»\"':!? ".toCharArray();
    public Alphabet(TextShifter textShifter) {
        this.textShifter = textShifter;
    }
    public int scoreEnglish(String text) {
        String[] commonEn = {" the ", " be ", " to ", " of ", " and ", " a ", " in ", " that ", " is ", " it ", " for ", " i ", " you ", " have "};
        return textShifter.countWordMatches(text.toLowerCase(), commonEn);
    }
    public int scoreUkrainian(String text) {
        String[] commonUa = {" і ", " в ", " не ", " на ", " що ", " він ", " я ", " це ", " до ", " з ", " по "};
        return textShifter.countWordMatches(text.toLowerCase(), commonUa);
    }
}
