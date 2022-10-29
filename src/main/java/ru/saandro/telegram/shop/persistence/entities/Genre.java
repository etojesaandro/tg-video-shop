package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;

public interface Genre extends Markable {

    long id();

    String name() throws IOException;
}
