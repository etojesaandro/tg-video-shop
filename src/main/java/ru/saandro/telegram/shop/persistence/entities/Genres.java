package ru.saandro.telegram.shop.persistence.entities;

import java.sql.*;

public interface Genres {
    Iterable<Markable> getAllGenres() throws SQLException;
}
