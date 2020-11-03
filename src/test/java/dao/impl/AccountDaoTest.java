package dao.impl;

import dao.entity.Account;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for AccountDao class.
 * @author Tomoto
 * <p>
 * 2020/11/2 9:38
 */
public class AccountDaoTest {

    @org.junit.Before
    public void setUp() {
        System.out.println("Setup...");
    }

    @org.junit.After
    public void tearDown() {
        System.out.println("TearDown...");
    }

    @org.junit.Test
    public void close() throws SQLException {
        AccountDao instance = AccountDao.getInstance();
        instance.close();
        assertTrue(instance.getConnection().isClosed());
    }

    @org.junit.Test
    public void create() throws SQLException {
        System.out.println("create test...");
        try (AccountDao instance = AccountDao.getInstance()) {
            Set<Account> list1 = instance.readAll(Account.class);
            Account zzzz = new Account("zzzz_ustc", "TuQiu");
            list1.add(zzzz);
            instance.create(zzzz);
            Set<Account> list2 = instance.readAll(Account.class);
            assertEquals(list1, list2);
        }
        System.out.println("create test over.");
    }

    @org.junit.Test
    public void createInBulk() throws SQLException {
        System.out.println("createInBulk test...");
        try (AccountDao instance = AccountDao.getInstance()) {
            Set<Account> list = instance.readAll(Account.class);

            Account zzzz = new Account("zzzz_ustc", "TuQiu");
            Account yuXuan = new Account("YuXuan", "YuXuan");
            list.add(yuXuan);
            list.add(zzzz);

            instance.createInBulk(zzzz, yuXuan);
            Set<Account> list2 = instance.readAll(Account.class);

            assertEquals(list, list2);
        }
        System.out.println("createInBulk test over.");
    }

    @org.junit.Test
    public void delete() throws SQLException {
        System.out.println("delete test...");
        try (AccountDao instance = AccountDao.getInstance()) {
            Set<Account> set = instance.readAll(Account.class);
            set.removeIf(account -> account.getAcc_name().equals("Tomoto"));
            instance.delete("Tomoto");
            Set<Account> list2 = instance.readAll(Account.class);
            assertEquals(set, list2);
        }
        System.out.println("delete test over.");
    }

    @org.junit.Test
    public void read() throws SQLException {
        System.out.println("read test...");
        try (AccountDao instance = AccountDao.getInstance()) {
            Account izzelAliz = instance.read(Account.class, "IzzelAliz");
            Account account = new Account("IzzelAliz", "UNNAMED", "HaiLuo");
            assertEquals(account, izzelAliz);
        }
        System.out.println("read tst over.");
    }

    @org.junit.Test
    public void readAll() throws SQLException {
        System.out.println("readAll test...");
        try (AccountDao instance = AccountDao.getInstance()) {
            Set<Account> accounts = instance.readAll(Account.class);
            Account tomoto = new Account("Tomoto", "UNNAMED", "FanQie");
            Account izzelAliz = new Account("IzzelAliz", "UNNAMED", "HaiLuo");
            Set<Account> set = new HashSet<>();
            set.add(izzelAliz);
            set.add(tomoto);
            assertEquals(set, accounts);
        }
        System.out.println("readAll test over.");
    }

    @org.junit.Test
    public void update() throws SQLException {
        System.out.println("update test...");
        try (AccountDao instance = AccountDao.getInstance()) {
            Account account = new Account("IzzelAliz", "HaiLuoPassword");
            instance.update("IzzelAliz", account);
            Account izzelAliz = instance.read(Account.class, "IzzelAliz");
            assertEquals(account, izzelAliz);
        }
        System.out.println("update test over.");
    }
}