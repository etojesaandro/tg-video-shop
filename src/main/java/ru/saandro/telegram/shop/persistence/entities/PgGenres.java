package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import javax.sql.*;
import java.sql.*;

import com.jcabi.jdbc.*;

public class PgGenres implements Genres {

    private final PersistenceProvider provider;

    public PgGenres(PersistenceProvider provider) {
        this.provider = provider;
    }

    @Override
    public Iterable<Markable> getAllGenres() throws SQLException {
        DataSource source = provider.getSource();
        return new JdbcSession(source)
                .sql("SELECT * FROM GENRE")
                .select(new ListOutcome<>(
                        rset -> new PgGenre(provider, rset.getLong(1), rset.getString(2))
                ));
    }

    public void deleteGenreById(long id) throws SQLException {
        DataSource source = provider.getSource();
        new JdbcSession(source)
                .sql("DELETE FROM GENRE WHERE ID = ?")
                .set(id).execute();
    }

    public PgGenre createGenre(String text) throws ShopBotException {
        PgGenre genre = new PgGenre(provider, null, text);
        genre.store();
        return genre;
    }
}
