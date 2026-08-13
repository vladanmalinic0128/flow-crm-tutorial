package com.example.application.services;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class CyrillicToLatinConverter {

    private static final Map<Character, String> CYRILLIC_TO_LATIN_MAP = buildCyrillicToLatinMap();

    private static Map<Character, String> buildCyrillicToLatinMap() {
        Map<Character, String> map = new HashMap<>();
        map.put('А', "A");
        map.put('Б', "B");
        map.put('В', "V");
        map.put('Г', "G");
        map.put('Д', "D");
        map.put('Ђ', "Đ");
        map.put('Е', "E");
        map.put('Ж', "Ž");
        map.put('З', "Z");
        map.put('И', "I");
        map.put('Ј', "J");
        map.put('К', "K");
        map.put('Л', "L");
        map.put('Љ', "Lj");
        map.put('М', "M");
        map.put('Н', "N");
        map.put('Њ', "Nj");
        map.put('О', "O");
        map.put('П', "P");
        map.put('Р', "R");
        map.put('С', "S");
        map.put('Т', "T");
        map.put('Ћ', "Ć");
        map.put('У', "U");
        map.put('Ф', "F");
        map.put('Х', "H");
        map.put('Ц', "C");
        map.put('Ч', "Č");
        map.put('Џ', "Dž");
        map.put('Ш', "Š");
        map.put('а', "a");
        map.put('б', "b");
        map.put('в', "v");
        map.put('г', "g");
        map.put('д', "d");
        map.put('ђ', "đ");
        map.put('е', "e");
        map.put('ж', "ž");
        map.put('з', "z");
        map.put('и', "i");
        map.put('ј', "j");
        map.put('к', "k");
        map.put('л', "l");
        map.put('љ', "lj");
        map.put('м', "m");
        map.put('н', "n");
        map.put('њ', "nj");
        map.put('о', "o");
        map.put('п', "p");
        map.put('р', "r");
        map.put('с', "s");
        map.put('т', "t");
        map.put('ћ', "ć");
        map.put('у', "u");
        map.put('ф', "f");
        map.put('х', "h");
        map.put('ц', "c");
        map.put('ч', "č");
        map.put('џ', "dž");
        map.put('ш', "š");
        return Collections.unmodifiableMap(map);
    }

    public String convert(String cyrillic) {
        if (cyrillic == null) {
            return null;
        }

        StringBuilder latin = new StringBuilder();
        for (char ch : cyrillic.toCharArray()) {
            String latinChar = CYRILLIC_TO_LATIN_MAP.get(ch);
            latin.append(latinChar != null ? latinChar : ch);
        }

        return latin.toString();
    }

    public String convertToUppercase(String cyrillic) {
        String converted = convert(cyrillic);
        return converted == null ? null : converted.toUpperCase(Locale.ROOT);
    }
}
