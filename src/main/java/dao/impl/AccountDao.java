package dao.impl;

import dao.Dao;
import dao.entity.Account;
import dao.util.DaoUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Dao class of Account entities.
 * <p>
 * Singleton.
 *
 * @author Totomo
 * <p>
 * 2020/11/1 20:50:21
 * @see Account
 */
public class AccountDao implements Dao<Account, String> {
    private static final String TABLE_NAME = "account_table";
    private final Connection connection;

    private AccountDao() throws IOException, SQLException {
        connection = DaoUtil.getConnection();
    }

    /**
     * Invoke this method to get the only instance.
     */
    public static AccountDao getInstance() {
        return Instance.INSTANCE;
    }

    @Override
    public AccountDao create(Account account) {
        // > A table name can't be used as a parameter. It must be hard coded.
        // https://stackoverflow.com/a/1208477/12348320
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `" + TABLE_NAME + "` (`acc_name`, `acc_nickname`, `acc_password`) VALUES (?, ?, ?);")) {
            statement.setString(1, account.getAcc_name());
            statement.setString(2, account.getAcc_nickname());
            statement.setString(3, account.getAcc_password());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Dao<Account, String> createInBulk(Account... entities) {
        String statementHead = "INSERT INTO `" + TABLE_NAME + "` (`acc_name`, `acc_nickname`, `acc_password`) VALUES ";
        StringBuilder statement = new StringBuilder();
        statement.append(statementHead); // append the head

        for (int i = 0; i < entities.length; i++) {
            statement.append("('").append(entities[i].getAcc_name()) // append current entity
                    .append("', '").append(entities[i].getAcc_nickname())
                    .append("', '").append(entities[i].getAcc_password()).append("')");
            if ((i + 1) % 1000 == 0 || i == entities.length - 1) {
                statement.append(";"); // add ';' to end
                try (Statement connectionStatement = connection.createStatement()) {
                    connectionStatement.executeUpdate(statement.toString()); // export entities to the database
                    statement = new StringBuilder(); // clear the statement
                    statement.append(statementHead); // append the head again
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                statement.append(", "); // add ',' for add next entity
            }
        }
        return null;
    }

    @Override
    public AccountDao delete(String accountName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM `" + TABLE_NAME + "` WHERE `acc_name` = ?;")) {
            statement.setString(1, accountName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Account read(Class<Account> tClass, String accountName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `" + TABLE_NAME + "` WHERE `acc_name` = ?;")) {
            statement.setString(1, accountName);
            try (ResultSet resultSet = statement.executeQuery()) { // auto close when exit the try block
                if (resultSet.next()) {
                    return new Account()
                            .setAcc_name(resultSet.getString("acc_name"))
                            .setAcc_nickname(resultSet.getString("acc_nickname"))
                            .setAcc_password(resultSet.getString("acc_password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Account> readAll(Class<Account> tClass) {
        Set<Account> result = new HashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + TABLE_NAME + "`;")) {
            while (resultSet.next()) {
                result.add(new Account()
                        .setAcc_name(resultSet.getString("acc_name"))
                        .setAcc_nickname(resultSet.getString("acc_nickname"))
                        .setAcc_password(resultSet.getString("acc_password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AccountDao update(String accountName, Account account) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE `" + TABLE_NAME + "` SET `acc_nickname` = ?, `acc_password` = ? WHERE `acc_name` = ?;")) {
            statement.setString(1, account.getAcc_nickname());
            statement.setString(2, account.getAcc_password());
            statement.setString(3, accountName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * Singleton instance class.
     */
    private static class Instance {
        public static AccountDao INSTANCE;

        static {
            try {
                INSTANCE = new AccountDao();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
