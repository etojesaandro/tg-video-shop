package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import javax.sql.*;
import java.io.*;
import java.sql.*;

import com.jcabi.jdbc.*;
import com.pengrad.telegrambot.request.*;

public class PgItem implements Item {

    private final DataSource dataSource;
    private final Long id;

    public PgItem(DataSource dataSource, Long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    public AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId) throws IOException {
        return new SendMessage(chatId, title());
    }

    public AbstractSendRequest<? extends AbstractSendRequest<?>> prepareContent(ShopBot bot, long chatId) throws IOException {
        return new SendMessage(chatId, title());
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String title() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT title FROM item WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String description() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT title FROM item WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String author() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT author FROM item WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Integer price() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT price FROM item WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(Integer.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String contentPath() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT content_path FROM item WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String previewPath() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT preview_path FROM item WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
