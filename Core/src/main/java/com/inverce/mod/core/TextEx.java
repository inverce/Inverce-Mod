package com.inverce.mod.core;

import java.text.Normalizer;

class TextEx {
    public static String deAccent(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("ł", "l")
                .replaceAll("Ł", "L")
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
