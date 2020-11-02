package database.service.impl;

import database.dao.entity.Account;
import database.dao.impl.AccountDao;
import database.service.Service;

import java.sql.SQLException;

/**
 * Service class of Account entities.
 * <p>
 * Singleton.
 *
 * @author Tomoto
 * @date 2020/11/2 15:03
 */
public class AccountService implements Service {
    private final AccountDao dao;

    private AccountService() {
        dao = AccountDao.getInstance();
    }

    /**
     * Invoke this method to get the only instance.
     */
    public static AccountService getInstance() {
        return Instance.INSTANCE;
    }

    /**
     * Match the account name and password.
     *
     * @param accountName the account name
     * @param password    the account password
     * @return whether account exists and password correct
     */
    public Boolean match(String accountName, String password) {
        Account account = dao.read(accountName);
        return account != null && account.getPassword().equals(password); // if account exists, then compare password
    }

    /**
     * Register a new account.
     *
     * @param accountName the account name
     * @param password    the account password
     * @return whether the creation was successful
     */
    public Boolean register(String accountName, String password) {
        if (dao.read(accountName) == null) {
            dao.create(new Account(accountName, password));
            return true;
        } else { // if account is already exists
            return false;
        }
    }

    /**
     * Gets account nickname.
     * @param accountName an account name
     * @return the account nickname or null if not exists
     */
    public String getNickName(String accountName) {
        Account account = dao.read(accountName);
        if (account != null) {
            return account.getNickname();
        }
        return null;
    }

    /**
     * Resets account nickname.
     * @param accountName an account name
     * @param nickname    new nickname
     * @return whether reset successful
     */
    public Boolean resetNickname(String accountName, String nickname) {
        Account account = dao.read(accountName);
        if (account != null) {
            account.setNickname(nickname);
            dao.update(accountName, account);
            return true;
        } else { // if account not exists
            return false;
        }
    }

    /**
     * Get account password.
     * @param accountName an account name
     * @return the account password or null if not exists
     */
    public String getPassword(String accountName) {
        Account account = dao.read(accountName);
        if (account != null) {
            return account.getPassword();
        }
        return null;
    }

    /**
     * Reset account password.
     * @param accountName an account name
     * @param password    new password
     * @return whether reset successful
     */
    public Boolean resetPassword(String accountName, String password) {
        Account account = dao.read(accountName);
        if (account != null) {
            account.setPassword(password);
            dao.update(accountName, account);
            return true;
        } else { // if account not exists
            return false;
        }
    }

    @Override
    public void close() throws SQLException {
        dao.close();
    }

    /**
     * Singleton instance class.
     */
    private static class Instance {
        public static final AccountService INSTANCE = new AccountService();
    }
}
