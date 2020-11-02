package database.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Tomoto
 * <p>
 * 2020/11/2 16:01
 */
public class AccountServiceTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("setup...");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tear down...");
    }

    @Test
    public void match() {
        System.out.println("match test...");

        AccountService instance = AccountService.getInstance();

        assertTrue(instance.match("IzzelAliz", "HaiLuo"));
        assertFalse(instance.match("Tomoto", "wrongPassword"));
        assertFalse(instance.match("T0m0t0", "whatever"));

        System.out.println("match test over.");
    }

    @Test
    public void register() {

    }

    @Test
    public void resetNickname() {
    }

    @Test
    public void resetPassword() {
    }
}