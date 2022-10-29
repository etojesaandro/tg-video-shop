package ru.saandro.telegram.shop.persistence.entities;

import java.io.*;
import java.sql.*;

public interface Genres {
    Iterable<Genre> getAllGenres() throws SQLException, IOException;

    Genre add(String name) throws IOException;
}
