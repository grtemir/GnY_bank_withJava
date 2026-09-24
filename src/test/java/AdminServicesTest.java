import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServicesTest {
    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseManager.setUrl("jdbc:sqlite:bank_test.db");
        DatabaseManager.InitialTables();
    }
    //adminAuth() tests

    @Test
    void testSuccessLogInAdmin() {
        assertNotNull(BankServices.authenticate(DatabaseManager.ADMIN_ID,DatabaseManager.ADMIN_PASSWORD));
    }

    @Test
    void testFailedLogInAdmin() {
        String wrongAdminPass="Admin1234";
        assertNull(BankServices.authenticate(DatabaseManager.ADMIN_ID,wrongAdminPass));

    }

    //listLogs() tests

    @Test
    void testSuccessListLogs() {
        DatabaseManager.resetLogs();
        AdminServices.createAccountByAdmin("hans");
        int count = AdminServices.listLogs();

        assertEquals(1, count);
    }

    @Test
    void testEmptyLogFile() {
        DatabaseManager.resetLogs();
        int count = AdminServices.listLogs();

        assertEquals(0, count);
    }


    //listLogs() with id tests

    @Test
    void testSuccessIdLogFile() {
        DatabaseManager.resetLogs();
        int id = AdminServices.createAccountByAdmin("hans");

        int count = AdminServices.listLogs(id);

        assertEquals(1, count);

    }

    @Test
    void testEmptyIdLogFile() {
        int id = AdminServices.createAccountByAdmin("hans");
        DatabaseManager.resetLogs();
        int count = AdminServices.listLogs(id);

        assertEquals(0, count);
    }

    //totalBalance() tests

    @Test
    void testSuccessTotalBalance() {
        DatabaseManager.resetLogs();
        DatabaseManager.resetUsersAndAccounts();

        double bal1 = 500;
        double bal2 = 1000;
        int id1 = AdminServices.createAccountByAdmin("hans");
        DatabaseTransactions.deposit(id1, bal1);
        int id2 = AdminServices.createAccountByAdmin("hans");
        DatabaseTransactions.deposit(id2, bal2);

        double expectedTotal = bal1 + bal2;
        double actualTotal = AdminServices.totalBalance();

        assertEquals(expectedTotal, actualTotal);
    }

    //createAccountByAdmin() tests

    @Test
    void testSuccessfulCreateAccountByAdmin() {
        int id = AdminServices.createAccountByAdmin("alice");
        assertTrue(id > 0);
    }

    @Test
    void testFailedCreateAccountByAdmin() {
        int id = AdminServices.createAccountByAdmin("");
        assertEquals(-1, id);
    }

    //deleteAccountByAdmin() tests

    @Test
    void testSuccessfulDeleteAccountByAdmin() {
        int id = AdminServices.createAccountByAdmin("alice");
        AdminServices.deleteAccountByAdmin(id);
        Accounts acc = DatabaseTransactions.findUserInfos(id);
        assertNull(acc);
    }

    @Test
    void testFailedDeleteAccountByAdmin() {
        int id = AdminServices.createAccountByAdmin("alice");
        DatabaseTransactions.deposit(id, 300);
        AdminServices.deleteAccountByAdmin(id);
        Accounts acc = DatabaseTransactions.findUserInfos(id);
        assertNotNull(acc);
    }

    //listAccounts() with id tests

    @Test
    void testSuccessListAccounts() {
        DatabaseManager.resetUsersAndAccounts();
        AdminServices.createAccountByAdmin("alice");
        AdminServices.createAccountByAdmin("bob");


        int count = AdminServices.listAccounts();


        assertEquals(2, count);

    }



}
