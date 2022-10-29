package ru.saandro.telegram.shop.persistence.entities;

import java.io.*;
import java.sql.*;
import java.util.Optional;

public interface Genres {
    Iterable<Genre> getAllGenres() throws SQLException, IOException;

    Genre add(String name) throws IOException;

    void deleteGenreById(long id) throws IOException;

    Optional<CachedGenre> getGenreById(long id) throws IOException;
}
