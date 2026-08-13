package com.example.application.services;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class LatinToCyrillicConverter {
    private static final int DIGRAPH_LENGTH = 2;

    private static final Map<String, Character> LATIN_TO_CYRILLIC_MAP = buildLatinToCyrillicMap();

    private static Map<String, Character> buildLatinToCyrillicMap() {
        Map<String, Character> map = new HashMap<>();
        // Digraphs must be added first to avoid incorrect single character translations
        map.put("Lj", 'Љ');
        map.put("LJ", 'Љ');
        map.put("Nj", 'Њ');
        map.put("NJ", 'Њ');
        map.put("Dž", 'Џ');
        map.put("DŽ", 'Џ');
        map.put("lj", 'љ');
        map.put("nj", 'њ');
        map.put("dž", 'џ');

        map.put("A", 'А');
        map.put("B", 'Б');
        map.put("V", 'В');
        map.put("G", 'Г');
        map.put("D", 'Д');
        map.put("Đ", 'Ђ');
        map.put("E", 'Е');
        map.put("Ž", 'Ж');
        map.put("Z", 'З');
        map.put("I", 'И');
        map.put("J", 'Ј');
        map.put("K", 'К');
        map.put("L", 'Л');
        map.put("M", 'М');
        map.put("N", 'Н');
        map.put("O", 'О');
        map.put("P", 'П');
        map.put("R", 'Р');
        map.put("S", 'С');
        map.put("T", 'Т');
        map.put("Ć", 'Ћ');
        map.put("U", 'У');
        map.put("F", 'Ф');
        map.put("H", 'Х');
        map.put("C", 'Ц');
        map.put("Č", 'Ч');
        map.put("Š", 'Ш');
        map.put("a", 'а');
        map.put("b", 'б');
        map.put("v", 'в');
        map.put("g", 'г');
        map.put("d", 'д');
        map.put("đ", 'ђ');
        map.put("e", 'е');
        map.put("ž", 'ж');
        map.put("z", 'з');
        map.put("i", 'и');
        map.put("j", 'ј');
        map.put("k", 'к');
        map.put("l", 'л');
        map.put("m", 'м');
        map.put("n", 'н');
        map.put("o", 'о');
        map.put("p", 'п');
        map.put("r", 'р');
        map.put("s", 'с');
        map.put("t", 'т');
        map.put("ć", 'ћ');
        map.put("u", 'у');
        map.put("f", 'ф');
        map.put("h", 'х');
        map.put("c", 'ц');
        map.put("č", 'ч');
        map.put("š", 'ш');
        return Collections.unmodifiableMap(map);
    }

    public String convert(String latin) {
        if (latin == null) {
            return null;
        }

        StringBuilder cyrillic = new StringBuilder();
        int i = 0;

        while (i < latin.length()) {
            // Check for two-letter digraphs first
            if (i < latin.length() - 1) {
                String digraph = latin.substring(i, i + DIGRAPH_LENGTH);
                Character mapped = LATIN_TO_CYRILLIC_MAP.get(digraph);
                if (mapped != null) {
                    cyrillic.append(mapped.charValue());
                    i += DIGRAPH_LENGTH;
                    continue;
                }
            }
            // Otherwise, use single character mapping
            String character = latin.substring(i, i + 1);
            cyrillic.append(LATIN_TO_CYRILLIC_MAP.getOrDefault(character, character.charAt(0)));
            i++;
        }

        return cyrillic.toString();
    }

    public String convertToUppercase(String latin) {
        String converted = convert(latin);
        return converted == null ? null : converted.toUpperCase(Locale.ROOT);
    }
}
