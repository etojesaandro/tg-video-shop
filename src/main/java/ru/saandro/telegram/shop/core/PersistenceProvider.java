package ru.saandro.telegram.shop.core;

import javax.sql.*;
import java.sql.*;

public interface PersistenceProvider {
    DataSource getSource() throws SQLException;
}
