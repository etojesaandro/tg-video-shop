package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;

public class CachedGenre implements Genre {

    private final Genre origin;
    private final String name;

    public CachedGenre(Genre origin, String name) {
        this.origin = origin;
        this.name = name;
    }

    @Override
    public long id() {
        return origin.id();
    }

    @Override
    public String name() throws IOException {
        return name;
    }

    @Override
    public String getMarkableName() {
        return id() + "";
    }

    @Override
    public String getMarkableDescription() throws IOException {
        return name;
    }
}
