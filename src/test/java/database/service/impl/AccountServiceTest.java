package database.service.impl;

import database.dao.entity.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Unit test for AccountService class.
 * @author Tomoto
 * <p>
 * 2020/11/2 16:01
 */
public class AccountServiceTest {

    @Before
    public void setUp() {
        System.out.println("setup...");
    }

    @After
    public void tearDown() {
        System.out.println("tear down...");
    }

    @Test
    public void match() throws SQLException {
        System.out.println("match test...");

        try (AccountService instance = AccountService.getInstance()) {
            assertTrue(instance.match("IzzelAliz", "HaiLuo"));
            assertFalse(instance.match("Tomoto", "wrongPassword"));
            assertFalse(instance.match("T0m0t0", "whatever"));
        }

        System.out.println("match test over.");
    }

    @Test
    public void register() throws SQLException {
        System.out.println("register test...");
        try (AccountService instance = AccountService.getInstance()) {
            Account zzzz = new Account("zzzz_ustc", "TuQiu");
            instance.register(zzzz.getName(), zzzz.getPassword());
            assertTrue(instance.match(zzzz.getName(), zzzz.getPassword()));
        }
        System.out.println("register test over.");
    }

    @Test
    public void resetNickname() throws SQLException {
        System.out.println("resetNickname test...");
        try (AccountService instance = AccountService.getInstance()) {
            instance.resetNickname("IzzelAliz", "海螺");
            assertEquals("海螺", instance.getNickName("IzzelAliz"));
        }
        System.out.println("resetNickname test over.");
    }

    @Test
    public void resetPassword() throws SQLException {
        System.out.println("resetPassword test...");
        try (AccountService instance = AccountService.getInstance()) {
            instance.resetPassword("IzzelAliz", "HaiLuoLuo");
            assertTrue(instance.match("IzzelAliz", "HaiLuoLuo"));
        }
        System.out.println("resetPassword test over.");
    }

    @Test
    public void getNickName() throws SQLException {
        System.out.println("getNickname test...");
        try (AccountService instance = AccountService.getInstance()) {
            assertEquals("海螺", instance.getNickName("IzzelAliz"));
        }
        System.out.println("getNickname test over.");
    }

    @Test
    public void getPassword() throws SQLException {
        System.out.println("getPassword test...");
        try (AccountService instance = AccountService.getInstance()) {
            assertEquals(instance.getPassword("IzzelAliz"), "HaiLuoLuo");
        }
        System.out.println("getPassword test over.");
    }
}