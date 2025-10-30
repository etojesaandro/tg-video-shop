package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;

public class TestGenre implements Genre {
    @Override
    public long id() {
        return 0;
    }

    @Override
    public String name() throws IOException {
        return "TestGenre";
    }

    @Override
    public String getMarkableName() {
        return "TestMarkableGenreName";
    }

    @Override
    public String getMarkableDescription() throws IOException {
        return "TestMarkableGenreDescription";
    }
}
