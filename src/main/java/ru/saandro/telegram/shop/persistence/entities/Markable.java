package ru.saandro.telegram.shop.persistence.entities;

import java.io.*;

public interface Markable {
    String getMarkableName();

    String getMarkableDescription() throws IOException;
}
