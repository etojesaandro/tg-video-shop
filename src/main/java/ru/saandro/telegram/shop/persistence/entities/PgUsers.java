package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.logger.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.jcabi.jdbc.*;

public class PgUsers implements Users {

    private final ShopBot provider;
    private final SimpleTelegramLogger logger;

    public PgUsers(ShopBot provider, SimpleTelegramLogger logger) {
        this.provider = provider;
        this.logger = logger;
    }

    @Override
    public Optional<? extends BotUser> findUser(long userId) {
        try {
            DataSource source = provider.getSource();
            List<PgUser> selectionResult = new JdbcSession(source)
                    .sql("SELECT * FROM BOT_USER WHERE ID = ?")
                    .set(userId)
                    .select(new ListOutcome<>(
                            rset -> new PgUser(provider, rset.getLong(1),
                                    rset.getString(2), rset.getInt(3)
                            )
                    ));
            return selectionResult.stream().findFirst();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Unable to browse the videos", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends BotUser> findOrCreateUser(Long userId, String name) {
        Optional<? extends BotUser> user = findUser(userId);
        if (user.isEmpty()) {
            user = new PgUser(provider, userId, name, 0).create();
        }
        return user;
    }
}
