package ru.saandro.telegram.shop.core;

import javax.sql.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public final class PgSource implements DataSource {

    /**
     * H2 driver.
     */
    private static final Driver DRIVER = new org.postgresql.Driver();

    /**
     * Unique name of the DB.
     */
    private final transient String connectionString;
    private final String user;
    private final String password;

    /**
     * Public ctor.
     * @param connectionString DB connectionString
     */
    public PgSource(final String connectionString, String user, String password) {
        this.connectionString = connectionString;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties info = new Properties();
        info.setProperty("user", user);
        info.setProperty("password", password);
        return PgSource.DRIVER.connect(connectionString, info);
    }

    @Override
    public Connection getConnection(final String username,
                                    final String password) {
        throw new UnsupportedOperationException("#getConnection()");
    }

    @Override
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("#getLogWriter()");
    }

    @Override
    public void setLogWriter(final PrintWriter writer) {
        throw new UnsupportedOperationException("#setLogWriter()");
    }

    @Override
    public void setLoginTimeout(final int seconds) {
        throw new UnsupportedOperationException("#setLoginTimeout()");
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException("#getLoginTimeout()");
    }

    @Override
    public Logger getParentLogger() {
        throw new UnsupportedOperationException("#getParentLogger()");
    }

    @Override
    public <T> T unwrap(final Class<T> iface) {
        throw new UnsupportedOperationException("#unwrap()");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        throw new UnsupportedOperationException("#isWrapperFor()");
    }

}
