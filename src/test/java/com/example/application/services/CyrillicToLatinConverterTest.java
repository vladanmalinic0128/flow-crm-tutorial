package com.example.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CyrillicToLatinConverterTest {

    private final CyrillicToLatinConverter converter = new CyrillicToLatinConverter();

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
            "Љ, Lj",
            "Њ, Nj",
            "Џ, Dž",
            "љ, lj",
            "њ, nj",
            "џ, dž",
            "Ђ, Đ",
            "ђ, đ",
            "Ч, Č",
            "ч, č",
    })
    void convertsIndividualDigraphsAndDiacritics(String cyrillic, String expectedLatin) {
        assertEquals(expectedLatin, converter.convert(cyrillic));
    }

    @Test
    void convertsWholeSentenceLeavingUnmappedCharactersUntouched() {
        String cyrillic = "Љубазна њежност, џез и 25 сати!";
        String expectedLatin = "Ljubazna nježnost, džez i 25 sati!";

        assertEquals(expectedLatin, converter.convert(cyrillic));
    }

    @Test
    void convertToUppercaseReturnsNullForNullInput() {
        // Regression test: the old implementation called convert(...).toUpperCase(), which threw
        // a NullPointerException whenever convert(...) returned null.
        assertNull(converter.convertToUppercase(null));
    }

    @Test
    void convertToUppercaseUppercasesTheConvertedLatinText() {
        assertEquals("DŽEZ NJIVA LJUT", converter.convertToUppercase("џез њива љут"));
    }

    @Test
    void convertToUppercaseIsIndependentOfDefaultLocale() {
        // Regression test: under the Turkish locale, String.toUpperCase() (no Locale argument)
        // turns "i" into the dotted capital "İ" instead of "I". Since convert() can produce plain
        // Latin "i" characters (e.g. from Cyrillic 'и'), the uppercasing must be locale-independent.
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertEquals("IVO", converter.convertToUppercase("иво"));
        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
