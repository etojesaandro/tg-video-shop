package ru.saandro.telegram.shop.dao;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.google.common.collect.ImmutableList;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;

import ru.saandro.telegram.shop.controller.EnumWithDescription;
import ru.saandro.telegram.shop.controller.VideoGenres;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

public class ConstPgItems implements Items {

    private final DataSource source;
    private final SimpleTelegramLogger logger;

    public ConstPgItems(DataSource dataSource, SimpleTelegramLogger logger)
    {
        this.source = dataSource;
        this.logger = logger;
    }

    @Override
    public Iterable<Item> getPurchasedItemsByUser(long userId) {
        try {
            return new JdbcSession(source)
                    .sql("SELECT i.id, i.title, i.description, i.author, g.name, i.price, i.preview_path, i.content_path FROM item i " +
                            "JOIN item_by_user iu" +
                            "ON iu.item_id = i.id " +
                            "JOIN user u" +
                            "ON u.uid = iu.id" +
                            "WHERE uid = ?")
                    .set(userId)
                    .select(
                            new ListOutcome<>(
                                    rset -> new PgItem(source, rset.getLong(0),
                                            rset.getString(1),
                                            rset.getString(2),
                                            rset.getString(3),
                                            EnumWithDescription.parse(rset.getString(4), VideoGenres.class).orElse(VideoGenres.ALL),
                                            rset.getInt(5),
                                            Paths.get(rset.getString(6)),
                                            Paths.get(rset.getString(7)))
                            )
                    );
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Unable to browse the videos", e);
            return ImmutableList.of();
        }
    }

    @Override
    public Iterable<Item> browseItemsByUserAndGenre(long userId, VideoGenres genre) {
        try {
            String suffix = "";
            if (genre != VideoGenres.ALL) {
                suffix = "WHERE g.name = ?";
            }
            JdbcSession sql = new JdbcSession(source)
                    .sql("SELECT i.id, i.title, i.description, g.name, i.price, i.preview_path, i.content_path FROM item i " +
                            "JOIN item_by_genre ig" +
                            "ON ig.item_id = i.id " +
                            "JOIN genre g" +
                            "ON g.id = ig.id" +
                            suffix);
            if (genre != VideoGenres.ALL) {
                sql.set(genre.getName());
            }
            return sql.select(
                    new ListOutcome<>(
                            rset -> new PgItem(source, rset.getLong(0),
                                    rset.getString(1),
                                    rset.getString(2),
                                    rset.getString(3),
                                    EnumWithDescription.parse(rset.getString(4), VideoGenres.class).orElse(VideoGenres.ALL),
                                    rset.getInt(5),
                                    Paths.get(rset.getString(6)),
                                            Paths.get(rset.getString(7)))
                    )
            );
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to browse the videos", e);
            return ImmutableList.of();
        }
    }
}
