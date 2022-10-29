package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.ShopBot;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;

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
    public Genre genre() throws IOException {
        try {
            return new PgGenre(dataSource, new JdbcSession(dataSource)
                    .sql("SELECT id FROM genre g " +
                            "JOIN ITEM_GENRE ig " +
                            "ON g.id = ig.genre_id  " +
                            "JOIN ITEM i " +
                            "ON i.id = ig.item_id " +
                            "WHERE u.id = ? ")
                    .select(new SingleOutcome<>(Long.class)));
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
