package database.dao.impl;

import database.dao.entity.Account;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tomoto
 * @date 2020/11/2 9:38
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
        AccountDao instance = AccountDao.getInstance();
        List<Account> list1 = instance.readAll();
        Account zzzz = new Account("zzzz_ustc", "TuQiu");
        list1.add(zzzz);
        instance.create(zzzz);
        List<Account> list2 = instance.readAll();
        assertEquals(list1, list2);
        instance.close();
        System.out.println("create test over.");
    }

    @org.junit.Test
    public void createInBulk() throws SQLException {
        System.out.println("createInBulk test...");
        AccountDao instance = AccountDao.getInstance();
        List<Account> list = instance.readAll();

        Account zzzz = new Account("zzzz_ustc", "TuQiu");
        Account yuXuan = new Account("YuXuan", "YuXuan");
        list.add(yuXuan);
        list.add(zzzz);

        instance.createInBulk(zzzz, yuXuan);
        List<Account> list2 = instance.readAll();

        assertEquals(list, list2);

        instance.close();
        System.out.println("createInBulk test over.");
    }

    @org.junit.Test
    public void delete() throws SQLException {
        System.out.println("delete test...");
        AccountDao instance = AccountDao.getInstance();
        List<Account> list1 = instance.readAll();
        list1.remove(1);
        instance.delete("Tomoto");
        List<Account> list2 = instance.readAll();
        assertEquals(list1, list2);
        instance.close();
        System.out.println("delete test over.");
    }

    @org.junit.Test
    public void read() throws SQLException {
        System.out.println("read test...");
        AccountDao instance = AccountDao.getInstance();
        Account izzelAliz = instance.read("IzzelAliz");
        Account account = new Account("IzzelAliz", Account.DEFAULT_UNNAMED, "HaiLuo");
        assertEquals(account, izzelAliz);
        instance.close();
        System.out.println("read tst over.");
    }

    @org.junit.Test
    public void readAll() throws SQLException {
        System.out.println("readAll test...");
        AccountDao instance = AccountDao.getInstance();
        List<Account> accounts = instance.readAll();
        Account tomoto = new Account("Tomoto", Account.DEFAULT_UNNAMED, "FanQie");
        Account izzelAliz = new Account("IzzelAliz", Account.DEFAULT_UNNAMED, "HaiLuo");
        List<Account> list = new ArrayList<>();
        list.add(izzelAliz);
        list.add(tomoto);
        assertEquals(list, accounts);
        instance.close();
        System.out.println("readAll test over.");
    }

    @org.junit.Test
    public void update() throws SQLException {
        System.out.println("update test...");
        AccountDao instance = AccountDao.getInstance();
        Account account = new Account("IzzelAliz", "HaiLuoPassword");
        instance.update("IzzelAliz", account);
        Account izzelAliz = instance.read("IzzelAliz");
        assertEquals(account, izzelAliz);
        instance.close();
        System.out.println("update test over.");
    }
}