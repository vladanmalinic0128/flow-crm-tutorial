package com.example.application.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LatinToCyrillicConverter {

    private static final Map<String, Character> latinToCyrillicMap = new HashMap<>();

    static {
        // Digraphs must be added first to avoid incorrect single character translations
        latinToCyrillicMap.put("Lj", 'Љ');
        latinToCyrillicMap.put("LJ", 'Љ');
        latinToCyrillicMap.put("Nj", 'Њ');
        latinToCyrillicMap.put("NJ", 'Њ');
        latinToCyrillicMap.put("Dž", 'Џ');
        latinToCyrillicMap.put("DŽ", 'Џ');
        latinToCyrillicMap.put("lj", 'љ');
        latinToCyrillicMap.put("nj", 'њ');
        latinToCyrillicMap.put("dž", 'џ');

        latinToCyrillicMap.put("A", 'А');
        latinToCyrillicMap.put("B", 'Б');
        latinToCyrillicMap.put("V", 'В');
        latinToCyrillicMap.put("G", 'Г');
        latinToCyrillicMap.put("D", 'Д');
        latinToCyrillicMap.put("Đ", 'Ђ');
        latinToCyrillicMap.put("E", 'Е');
        latinToCyrillicMap.put("Ž", 'Ж');
        latinToCyrillicMap.put("Z", 'З');
        latinToCyrillicMap.put("I", 'И');
        latinToCyrillicMap.put("J", 'Ј');
        latinToCyrillicMap.put("K", 'К');
        latinToCyrillicMap.put("L", 'Л');
        latinToCyrillicMap.put("M", 'М');
        latinToCyrillicMap.put("N", 'Н');
        latinToCyrillicMap.put("O", 'О');
        latinToCyrillicMap.put("P", 'П');
        latinToCyrillicMap.put("R", 'Р');
        latinToCyrillicMap.put("S", 'С');
        latinToCyrillicMap.put("T", 'Т');
        latinToCyrillicMap.put("Ć", 'Ћ');
        latinToCyrillicMap.put("U", 'У');
        latinToCyrillicMap.put("F", 'Ф');
        latinToCyrillicMap.put("H", 'Х');
        latinToCyrillicMap.put("C", 'Ц');
        latinToCyrillicMap.put("Č", 'Ч');
        latinToCyrillicMap.put("Š", 'Ш');
        latinToCyrillicMap.put("a", 'а');
        latinToCyrillicMap.put("b", 'б');
        latinToCyrillicMap.put("v", 'в');
        latinToCyrillicMap.put("g", 'г');
        latinToCyrillicMap.put("d", 'д');
        latinToCyrillicMap.put("đ", 'ђ');
        latinToCyrillicMap.put("e", 'е');
        latinToCyrillicMap.put("ž", 'ж');
        latinToCyrillicMap.put("z", 'з');
        latinToCyrillicMap.put("i", 'и');
        latinToCyrillicMap.put("j", 'ј');
        latinToCyrillicMap.put("k", 'к');
        latinToCyrillicMap.put("l", 'л');
        latinToCyrillicMap.put("m", 'м');
        latinToCyrillicMap.put("n", 'н');
        latinToCyrillicMap.put("o", 'о');
        latinToCyrillicMap.put("p", 'п');
        latinToCyrillicMap.put("r", 'р');
        latinToCyrillicMap.put("s", 'с');
        latinToCyrillicMap.put("t", 'т');
        latinToCyrillicMap.put("ć", 'ћ');
        latinToCyrillicMap.put("u", 'у');
        latinToCyrillicMap.put("f", 'ф');
        latinToCyrillicMap.put("h", 'х');
        latinToCyrillicMap.put("c", 'ц');
        latinToCyrillicMap.put("č", 'ч');
        latinToCyrillicMap.put("š", 'ш');
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
                String digraph = latin.substring(i, i + 2);
                if (latinToCyrillicMap.containsKey(digraph)) {
                    cyrillic.append(latinToCyrillicMap.get(digraph));
                    i += 2;
                    continue;
                }
            }
            // Otherwise, use single character mapping
            String character = latin.substring(i, i + 1);
            cyrillic.append(latinToCyrillicMap.getOrDefault(character, character.charAt(0)));
            i++;
        }

        return cyrillic.toString();
    }

    public String convertToUppercase(String latin) {
        return convert(latin).toUpperCase();
    }
}
