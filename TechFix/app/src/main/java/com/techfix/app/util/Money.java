package com.techfix.app.util;

import java.util.Locale;

public final class Money {

    private Money() {
    }

    public static long parse(String text) {
        if (text == null) return 0;
        String whole = text;
        int dot = whole.indexOf('.');
        if (dot >= 0) whole = whole.substring(0, dot); 
        String digits = whole.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String format(long amount) {
        return String.format(Locale.US, "LKR %,d.00", amount);
    }
}
