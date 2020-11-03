package view;

import java.sql.SQLException;

/**
 * @author Tomoto
 * <p>
 * 2020/11/3 18:21
 */
public interface View extends AutoCloseable {
    @Override
    void close() throws SQLException;
}
