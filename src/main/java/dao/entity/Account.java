package dao.entity;

import java.util.Objects;

/**
 * User account.
 *
 * @author Tomoto
 * @date 2020/11/1 21:14
 */
public class Account {
//    public static final String DEFAULT_UNNAMED = "UNNAMED";

    private String acc_name;
    private String acc_nickname;
    private String acc_password;

    public Account() {
    }

    /**
     * Constructs an account.
     *
     * @param acc_name     the account name
     * @param acc_nickname the account nickname
     * @param acc_nickname the account password
     */
    public Account(String acc_name, String acc_nickname, String acc_password) {
        this.acc_name = acc_name;
        this.acc_nickname = acc_nickname;
        this.acc_password = acc_password;
    }

    /**
     * Constructs an account with same name and nickname.
     *
     * @param name     the account name
     * @param password the account password
     */
    public Account(String name, String password) {
        this.acc_name = name;
        this.acc_nickname = name;
        this.acc_password = password;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public Account setAcc_name(String acc_name) {
        this.acc_name = acc_name;
        return this;
    }

    public String getAcc_nickname() {
        return acc_nickname;
    }

    public Account setAcc_nickname(String acc_nickname) {
        this.acc_nickname = acc_nickname;
        return this;
    }

    public String getAcc_password() {
        return acc_password;
    }

    public Account setAcc_password(String acc_password) {
        this.acc_password = acc_password;
        return this;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + acc_name + '\'' +
                ", nickName='" + acc_nickname + '\'' +
                ", password='" + acc_password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(acc_name, account.acc_name) &&
                Objects.equals(acc_nickname, account.acc_nickname) &&
                Objects.equals(acc_password, account.acc_password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acc_name, acc_nickname, acc_password);
    }
}
