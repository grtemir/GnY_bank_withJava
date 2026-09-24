import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsTest {
    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseManager.setUrl("jdbc:sqlite:bank_test.db");
        DatabaseManager.InitialTables();
    }

    //setPassword() tests

    @Test
    void testNullSetPassword() {
        String oldPass = "1234";
        Accounts acc = new Accounts(5, oldPass, "alice");
        assertThrows(IllegalArgumentException.class, () -> acc.setPassword(null));

        assertEquals(oldPass, acc.getPassword());
    }

    @Test
    void testBigSetPassword() {
        String oldPass = "1234";
        String newPassBigger12Digits = "12345678901234";
        Accounts acc = new Accounts(5, oldPass, "alice");
        assertThrows(IllegalArgumentException.class, () -> acc.setPassword(newPassBigger12Digits));

        assertEquals(oldPass, acc.getPassword());
    }

    @Test
    void testSuccessfulSetPassword() {
        String oldPass = "1234";
        String newPass = "123456";
        Accounts acc = new Accounts(5, oldPass, "alice");
        acc.setPassword(newPass);
        assertEquals(newPass, acc.getPassword());
    }

    //setName() tests
    @Test
    void testNullSetName() {
        String oldName = "Alice";
        Accounts acc = new Accounts(5, "1234", oldName);
        assertThrows(IllegalArgumentException.class, () -> acc.setName(null));

        assertEquals(oldName, acc.getName());
    }

    @Test
    void testBigSetName() {
        String oldName = "Alice";
        Accounts acc = new Accounts(5, "1234", oldName);

        String newNameBigger20Digits = "Charlie Carol Hansi James";
        assertThrows(IllegalArgumentException.class, () -> acc.setName(newNameBigger20Digits));

        assertEquals(oldName, acc.getName());
    }

    @Test
    void testSuccessfulSetName() {
        String oldName = "Alice";
        Accounts acc = new Accounts(5, "1234", "alice");

        String newName = "Bob";

        acc.setName(newName);
        assertEquals(newName, acc.getName());
    }

    //setRole() tests

    @Test
    void testSuccessfulSetRole(){
        Accounts acc=new Accounts("1234","hans","admin");

        assertEquals("admin",acc.getRole());

    }

    @Test
    void testFailedSetRole(){
        Accounts acc=new Accounts("1234","hans","admin");

        assertThrows(IllegalArgumentException.class, () -> acc.setRole("male"));
    }




}
