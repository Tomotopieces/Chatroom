package database.service;

import java.sql.SQLException;

/**
 * Database Service interface.
 * @author Tomoto
 * @date 2020/11/2 15:03
 */
public interface Service extends AutoCloseable {
    /**
     * Close the dao
     */
    @Override
    void close() throws SQLException;
}
