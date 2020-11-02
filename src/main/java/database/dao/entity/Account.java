package database.dao.entity;

import java.util.Objects;

/**
 * User account.
 *
 * @author Tomoto
 * @date 2020/11/1 21:14
 */
public class Account {
    public static final String DEFAULT_UNNAMED = "UNNAMED";

    private String name;
    private String nickname;
    private String password;

    public Account() {
    }

    /**
     * Constructs an account.
     * @param name     the account name
     * @param nickname the account nickname
     * @param password the account password
     */
    public Account(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }

    /**
     * Constructs an account with same name and nickname.
     *
     * @param name     the account name
     * @param password the account password
     */
    public Account(String name, String password) {
        this.name = name;
        this.nickname = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public Account setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", nickName='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) &&
                Objects.equals(nickname, account.nickname) &&
                Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nickname, password);
    }
}
