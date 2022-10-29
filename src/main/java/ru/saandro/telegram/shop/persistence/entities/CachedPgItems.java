package ru.saandro.telegram.shop.persistence.entities;

import javax.sql.*;
import java.io.*;
import java.sql.*;

import com.jcabi.jdbc.*;

public class CachedPgItems implements Items {

    public static final String ITEM_BY_GENRE_SQL = "SELECT i.id, i.title, i.description, i.author, g.id, i.price, i.preview_path, i.content_path FROM item i " +
            "JOIN item_genre ig " +
            "ON ig.item_id = i.id " +
            "JOIN genre g " +
            "ON g.id = ig.genre_id " +
            "WHERE g.id = ?";

    private final DataSource dataSource;

    public CachedPgItems(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Item add(String title, String description, String author, Integer price, String previewPath, String contentPath) throws IOException {
        try {
            return new PgItem(dataSource, new JdbcSession(dataSource)
                    .sql("INSERT INTO ITEM(TITLE, DESCRIPTION, AUTHOR, PRICE, PREVIEW_PATH, CONTENT_PATH) VALUES(?,?,?,?,?,?)")
                    .set(title).set(description).set(author).set(price).set(previewPath).set(contentPath)
                    .insert(new SingleOutcome<>(Long.class)));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Iterable<Item> getPurchasedItemsByUser(long userId) throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT i.id, i.title, i.description, i.author, i.price, i.preview_path, i.content_path " +
                            "FROM item i " +
                            "JOIN BOT_USER_ITEM ui " +
                            "ON ui.item_id = i.id " +
                            "JOIN BOT_USER u " +
                            "ON u.id = ui.bot_user_id " +
                            "WHERE u.id = ? ")
                    .set(userId)
                    .select(
                            new ListOutcome<>(
                                    rset -> new CachedItem(new PgItem(dataSource, rset.getLong(1)),
                                            rset.getString(2),
                                            rset.getString(3),
                                            rset.getString(4),
                                            rset.getInt(5),
                                            rset.getString(6),
                                            rset.getString(7))
                            )
                    );
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Iterable<Item> browseItemsByGenre(long genreId) throws IOException {
        try {
            JdbcSession sql = new JdbcSession(dataSource)
                    .sql(ITEM_BY_GENRE_SQL);
            sql.set(genreId);
            return sql.select(
                    new ListOutcome<>(
                            rset -> new CachedItem(new PgItem(dataSource, rset.getLong(1)),
                                    rset.getString(2),
                                    rset.getString(3),
                                    rset.getString(4),
                                    rset.getInt(5),
                                    rset.getString(7),
                                    rset.getString(8))
                    )
            );
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
