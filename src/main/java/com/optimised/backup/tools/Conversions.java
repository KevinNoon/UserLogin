package com.optimised.backup.tools;

import java.util.regex.Pattern;

public class Conversions {
    private static final Pattern isInteger = Pattern.compile("[+-]?\\d+");

    public static int tryParseInt(String value) {
        if (value == null || !isInteger.matcher(value).matches()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }
}
