package dao.impl;

import dao.entity.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Tomoto
 * <p>
 * 2020/11/2 19:28
 */
public class DaoImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create() throws IOException, SQLException {
        System.out.println("create test...");
        try (DaoImpl<Account, String> dao = new DaoImpl<>()) {
            dao.create(new Account("zzzz_ustc", "TuQiu"));
        }
        System.out.println("create test over.");
    }
}