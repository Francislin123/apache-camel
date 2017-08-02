package com.walmart.feeds.api.core.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class SlugParserUtilTest {

    @Test
    public void convertSimpleStringToSlug() {
        String input = "Renato";

        String sluggedInput = SlugParserUtil.toSlug(input);

        assertEquals("renato", sluggedInput);
    }

    @Test
    public void convertComposedStringToSlug() {
        String input = "Renato Ibanhez";

        String sluggedImput = SlugParserUtil.toSlug(input);

        assertEquals("renato-ibanhez", sluggedImput);
    }

    @Test
    public void convertInputWithSpecialCharactersToSlug() {
        String input = "[Renato #Ibanhez 123@";

        String sluggedImput = SlugParserUtil.toSlug(input);

        assertEquals("renato-ibanhez-123", sluggedImput);
    }

}