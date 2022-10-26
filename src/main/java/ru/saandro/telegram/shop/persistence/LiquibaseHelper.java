package ru.saandro.telegram.shop.persistence;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.change.CheckSum;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

public class LiquibaseHelper implements AutoCloseable {

    private static final String CHANGE_LOG_LOCK_LIST =
        "SELECT ID, LOCKGRANTED, LOCKEDBY FROM DATABASECHANGELOGLOCK WHERE LOCKED=TRUE";

    private static final String CHANGE_LOG_UNLOCK =
        "UPDATE DATABASECHANGELOGLOCK SET LOCKED=FALSE, LOCKGRANTED=null, LOCKEDBY=null";

    @Nonnull
    private final String changeLogFile;

    @Nonnull
    private final JdbcConnection jdbcConnection;

    @Nonnull
    private final Contexts contexts;

    private Database database;
    private Liquibase liquibase;

    public LiquibaseHelper(@Nonnull String changeLogFile, @Nonnull Connection connection,
                           @Nonnull String contexts) {
        this.changeLogFile = requireNonNull(changeLogFile);
        jdbcConnection = new JdbcConnection(requireNonNull(connection));
        this.contexts = new Contexts(requireNonNull(contexts));
    }

    @Nonnull
    public List<ChangeSet> getUnrunChangeSets() throws LiquibaseException {
        init();
        return listUnrunChangeSets();
    }

    public boolean hasNewChangeSets() throws LiquibaseException {
        return !getUnrunChangeSets().isEmpty();
    }

    public boolean migrate() throws LiquibaseException {
        init();
        List<ChangeSet> changeSets = listUnrunChangeSets();
        if (changeSets.isEmpty()) {
            return false;
        }
        validate();
        update();
        return true;
    }


    @Override
    public void close() throws DatabaseException {
        if (null != database) {
            database.close();
        }
    }

    private void init() throws LiquibaseException {
        if (null != liquibase) {
            return;
        }

        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
        database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
        liquibase = new Liquibase(changeLogFile, resourceAccessor, database);
    }

    @Nonnull
    private List<ChangeSet> listUnrunChangeSets() throws LiquibaseException {
        return liquibase.listUnrunChangeSets(contexts, new LabelExpression());
    }

    private void validate() throws LiquibaseException {
        liquibase.validate();
    }

    private void update() throws LiquibaseException {
        liquibase.update(contexts);
    }
}
