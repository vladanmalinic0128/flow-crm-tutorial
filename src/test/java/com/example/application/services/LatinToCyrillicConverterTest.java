package com.example.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LatinToCyrillicConverterTest {

    private final LatinToCyrillicConverter converter = new LatinToCyrillicConverter();

    @Test
    void convertReturnsNullForNullInput() {
        assertNull(converter.convert(null));
    }

    @Test
    void convertReturnsEmptyStringForEmptyInput() {
        assertEquals("", converter.convert(""));
    }

    @ParameterizedTest
    @CsvSource({
            "Lj, Љ",
            "LJ, Љ",
            "lj, љ",
            "Nj, Њ",
            "NJ, Њ",
            "nj, њ",
            "Dž, Џ",
            "DŽ, Џ",
            "dž, џ",
    })
    void convertsDigraphsAsSingleCyrillicLetters(String latin, String expectedCyrillic) {
        assertEquals(expectedCyrillic, converter.convert(latin));
    }

    @Test
    void convertsSingleTrailingLetterThatCouldStartADigraph() {
        // "n" has no following character to pair with, so it must fall back to the single-letter mapping.
        assertEquals("н", converter.convert("n"));
    }

    @Test
    void convertsWholeSentenceLeavingUnmappedCharactersUntouched() {
        String latin = "Ljubazna nježnost, džez i 25 sati!";
        String expectedCyrillic = "Љубазна њежност, џез и 25 сати!";

        assertEquals(expectedCyrillic, converter.convert(latin));
    }

    @Test
    void recognizesDigraphInsideAWordRatherThanTwoSeparateLetters() {
        // "konj" (horse) must convert to "коњ", not "конј" (k-o-n-j as four separate letters).
        assertEquals("коњ", converter.convert("konj"));
    }

    @Test
    void convertToUppercaseReturnsNullForNullInput() {
        // Regression test: the old implementation called convert(...).toUpperCase(), which threw
        // a NullPointerException whenever convert(...) returned null.
        assertNull(converter.convertToUppercase(null));
    }

    @Test
    void convertToUppercaseUppercasesTheConvertedCyrillicText() {
        assertEquals("КОЊ", converter.convertToUppercase("konj"));
    }
}
