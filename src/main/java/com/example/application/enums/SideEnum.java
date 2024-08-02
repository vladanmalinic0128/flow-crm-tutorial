package com.example.application.enums;

public enum SideEnum {
    ONE_SIDED("Jednostrano"),
    TWO_SIDED("Dvostrano");

    private final String name;

    SideEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

