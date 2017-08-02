package com.walmart.feeds.api.core.utils;

import java.util.regex.Pattern;

public class SlugParserUtil {

    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[^\\w-]");

    public static String toSlug(String input) {
        String lowerCasedInput = input.toLowerCase();
        String inputWithoutWhiteSpace = WHITESPACE.matcher(lowerCasedInput).replaceAll("-");
        String inputWithoutSpecialChars = SPECIAL_CHAR.matcher(inputWithoutWhiteSpace).replaceAll("");
        return inputWithoutSpecialChars;
    }

}
