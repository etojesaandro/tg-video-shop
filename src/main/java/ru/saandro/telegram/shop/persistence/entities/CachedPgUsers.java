package ru.saandro.telegram.shop.persistence.entities;

import javax.sql.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import com.jcabi.jdbc.*;

public class CachedPgUsers implements Users {

    private final DataSource dataSource;

    public CachedPgUsers(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<? extends BotUser> findUser(long userId) throws IOException {
        try {
            List<BotUser> selectionResult = new JdbcSession(dataSource)
                    .sql("SELECT * FROM BOT_USER WHERE ID = ?")
                    .set(userId)
                    .select(new ListOutcome<>(
                            rset -> new CachedUser(new PgUser(dataSource, rset.getLong(1)),
                                    rset.getString(2),
                                    rset.getInt(3),
                                    rset.getBoolean(4))

                    ));
            return selectionResult.stream().findFirst();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Optional<? extends BotUser> findUser(String name) throws IOException {
        try {
            List<BotUser> selectionResult = new JdbcSession(dataSource)
                    .sql("SELECT * FROM BOT_USER WHERE NAME = ?")
                    .set(name)
                    .select(new ListOutcome<>(
                            rset -> new CachedUser(new PgUser(dataSource, rset.getLong(1)),
                                    rset.getString(2),
                                    rset.getInt(3),
                                    rset.getBoolean(4))

                    ));
            return selectionResult.stream().findFirst();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public BotUser findOrCreateUser(Long userId, String name) throws IOException {
        Optional<? extends BotUser> userOpt = findUser(userId);
        if (userOpt.isEmpty()) {
            userOpt = Optional.of(add(userId, name, 0, false));
        }
        return userOpt.get();
    }

    @Override
    public BotUser add(Long id, String name, Integer balance, boolean admin) throws IOException {
        try {
            return new CachedUser(new PgUser(dataSource,
                    new JdbcSession(dataSource)
                            .sql("INSERT INTO BOT_USER(ID, NAME ,BALANCE, ADMIN) VALUES(?,?,?,?)")
                            .set(id).set(name).set(balance).set(admin)
                            .insert(new SingleOutcome<>(Long.class))), name, balance, admin);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
