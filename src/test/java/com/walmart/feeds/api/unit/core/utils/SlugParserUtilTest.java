package com.walmart.feeds.api.unit.core.utils;

import com.walmart.feeds.api.core.utils.SlugParserUtil;
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

    @Test
    public void convertaWithAccentedLetters() {
        String input = "[Renato #Ibanhez çaãeécôlà 123@";

        String sluggedImput = SlugParserUtil.toSlug(input);

        assertEquals("renato-ibanhez-caaeecola-123", sluggedImput);
    }

}