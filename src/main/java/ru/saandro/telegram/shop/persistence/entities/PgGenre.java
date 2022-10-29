package ru.saandro.telegram.shop.persistence.entities;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;

public class PgGenre implements Markable, Genre {
    private final DataSource dataSource;
    private final Long id;

    public PgGenre(DataSource dataSource, Long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    @Override
    public String getMarkableName() {
        return id + "";
    }

    @Override
    public String getMarkableDescription() throws IOException {
        return name();
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public String name() throws IOException {
        try {
            return new JdbcSession(dataSource)
                    .sql("SELECT name FROM genre WHERE id = ?")
                    .set(id)
                    .select(new SingleOutcome<String>(String.class));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
