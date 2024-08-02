package com.example.application.enums;

public enum ScriptEnum {
    CYRILLIC("Ćirilica"),
    LATIN("Latinica");

    private final String name;

    ScriptEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
