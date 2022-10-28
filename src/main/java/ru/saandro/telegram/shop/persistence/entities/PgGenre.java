package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import java.sql.*;

import com.jcabi.jdbc.*;

public class PgGenre implements Markable {
    private final PersistenceProvider provider;
    private final Long id;
    private final String name;

    public PgGenre(PersistenceProvider provider, Long id, String name) {
        this.provider = provider;
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return id + "";
    }

    @Override
    public String getDescription() {
        return name;
    }

    public void store() throws ShopBotException {
        try {
            new JdbcSession(provider.getSource())
                    .sql("INSERT INTO GENRE(NAME) VALUES(?)")
                    .set(name).execute();
        } catch (SQLException e) {
            throw new ShopBotException("Unable to create new Genre", e);
        }
    }
}
