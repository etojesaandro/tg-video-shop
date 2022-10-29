package ru.saandro.telegram.shop.persistence.entities;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;

public class CachedPgGenres implements Genres {

    private final DataSource dataSource;

    public CachedPgGenres(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Iterable<Genre> getAllGenres() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT * FROM GENRE")
                    .select(new ListOutcome<>(
                            rset -> new CachedGenre(new PgGenre(dataSource, rset.getLong(1)), rset.getString(2)
                            )));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Genre add(String name) throws IOException {
        try {
            return new CachedGenre(new PgGenre(dataSource,
                    new JdbcSession(dataSource)
                            .sql("INSERT INTO GENRE(NAME) VALUES(?)")
                            .set(name)
                            .insert(new SingleOutcome<>(Long.class))), name);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public void deleteGenreById(long id) throws SQLException {
        new JdbcSession(dataSource)
                .sql("DELETE FROM GENRE WHERE ID = ?")
                .set(id).execute();
    }
}
