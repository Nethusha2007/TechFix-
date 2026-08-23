package com.techfix.app.util;

import java.util.Locale;

/** Tiny helper for parsing and formatting the app's "LKR x,xxx.00" money strings. */
public final class Money {

    private Money() {
    }

    /** Parses the whole-rupee amount from a label such as "LKR 12,500.00" -> 12500. */
    public static long parse(String text) {
        if (text == null) return 0;
        String whole = text;
        int dot = whole.indexOf('.');
        if (dot >= 0) whole = whole.substring(0, dot); // drop the cents
        String digits = whole.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Formats a whole-rupee amount as "LKR 12,500.00". */
    public static String format(long amount) {
        return String.format(Locale.US, "LKR %,d.00", amount);
    }
}
