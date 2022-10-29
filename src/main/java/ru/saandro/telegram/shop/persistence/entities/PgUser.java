package ru.saandro.telegram.shop.persistence.entities;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;


public class PgUser implements BotUser {

    private final DataSource dataSource;
    private final long id;

    public PgUser(DataSource dataSource, Long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String name() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT name FROM bot_user WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Integer balance() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT balance FROM bot_user WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(Integer.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean admin() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT admin FROM bot_user WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<>(Boolean.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void purchaseItem(Item itemToPurchase) throws IOException {
        try {
            new JdbcSession(dataSource)
                    .sql("INSERT INTO BOT_USER_ITEM(BOT_USER_ID, ITEM_ID) VALUES(?,?)")
                    .set(id()).set(itemToPurchase.id()).execute();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public BotUser updateBalance(Integer balance) throws IOException {
        try {
            new JdbcSession(dataSource)
                    .sql("UPDATE BOT_USER " +
                            "SET BALANCE = ? " +
                            "WHERE ID = ?")
                    .set(balance).set(id)
                    .execute();
        } catch (SQLException e) {
            throw new IOException(e);
        }
        return this;
    }

    @Override
    public BotUser promote() throws IOException {
        try {
            new JdbcSession(dataSource)
                    .sql("UPDATE BOT_USER " +
                            "SET ADMIN = ? " +
                            "WHERE ID = ?")
                    .set(true).set(id)
                    .execute();
        } catch (SQLException e) {
            throw new IOException(e);
        }
        return this;
    }

    @Override
    public boolean hasItem(Item item) throws IOException {

        try {
            Long select = new JdbcSession(dataSource)
                    .sql("SELECT ID FROM BOT_USER_ITEM " +
                            "WHERE BOT_USER_ID = ? " +
                            "AND ITEM_ID = ?").set(id).set(item.id())
                    .select(new SingleOutcome<>(Long.class));
            return select != null;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
