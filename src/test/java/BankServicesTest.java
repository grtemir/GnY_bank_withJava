import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class BankServicesTest {
    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseManager.setUrl("jdbc:sqlite:bank_test.db");
        DatabaseManager.InitialTables();
    }

    //transfermoney() tests

    @Test
    void insufficientFundTest() {
        int sender = AdminServices.createAccountByAdmin("alice");
        int target = AdminServices.createAccountByAdmin("bob");

        DatabaseTransactions.deposit(sender, 1000.0);
        DatabaseTransactions.deposit(target, 500.0);


        assertThrows(IllegalArgumentException.class, () -> {
            BankServices.transferMoney(sender, target, 1500.0);
        });

        assertEquals(1000.0, DatabaseTransactions.checkBalance(sender));
        assertEquals(500.0, DatabaseTransactions.checkBalance(target));
    }

    @Test
    void successfulTransferTest() {
        int sender = AdminServices.createAccountByAdmin("alice");
        int target = AdminServices.createAccountByAdmin("bob");

        DatabaseTransactions.deposit(sender, 1000.0);
        DatabaseTransactions.deposit(target, 500.0);

        BankServices.transferMoney(sender, target, 500.0);

        assertEquals(500.0, DatabaseTransactions.checkBalance(sender));
        assertEquals(1000.0, DatabaseTransactions.checkBalance(target));
    }

    //authenticate() tests

    @Test
    void testSuccessfulLogin() {
        int id = BankServices.createAccountByUSer("alice", "1234");
        DatabaseTransactions.deposit(id, 500.0);

        Accounts acc = BankServices.authenticate(id, "1234");
        assertNotNull(acc);

        assertEquals(id, acc.getId());
        assertEquals(500.0, DatabaseTransactions.checkBalance(acc.getId()));
        assertEquals("alice", acc.getName());


    }

    @Test
    void testFailedLogin() {
        int id = BankServices.createAccountByUSer("alice", "1234");
        DatabaseTransactions.deposit(id, 500.0);

        String wrongPassword = "123";
        Accounts acc = BankServices.authenticate(id, wrongPassword);

        assertNull(acc);


    }

    //changePassword() tests

    @Test
    void testSuccessfulChangingPassword() {
        String oldPassword="1234";

        int id = BankServices.createAccountByUSer("alice", oldPassword);
        Accounts acc = BankServices.authenticate(id,oldPassword);

        String newPassword="11223344";
        BankServices.changePassword(oldPassword,newPassword,acc);

        Accounts accAfter= DatabaseTransactions.findUserInfos(id);

        assertEquals(newPassword,accAfter.getPassword());

    }

    @Test
    void testFailedChangingPassword() {
        String realOldPassword="1234";
        String wrongOldPassword="0246";
        int id = BankServices.createAccountByUSer("alice", realOldPassword);

        Accounts acc = BankServices.authenticate(id,realOldPassword);

        String newPassword="11223344";

        BankServices.changePassword(wrongOldPassword,newPassword,acc);

        Accounts accAfter= DatabaseTransactions.findUserInfos(id);

        assertEquals(realOldPassword,accAfter.getPassword());

    }

    //closeAccount() tests

    @Test
    void testSuccessCloseAccount(){
        int id = BankServices.createAccountByUSer("alice", "1234");

        BankServices.closeAccount(id,"1234");

        Accounts acc=DatabaseTransactions.findUserInfos(id);

        assertNull(acc);

    }

    @Test
    void testFailedWrongPasswordCloseAccount(){
        int id = BankServices.createAccountByUSer("alice", "1234");

        String wrongPassword="112233";
        BankServices.closeAccount(id,wrongPassword);

        Accounts acc=DatabaseTransactions.findUserInfos(id);

        assertNotNull(acc);
    }
    @Test
    void testFailedBalanceAmountCloseAccount(){
        int id = BankServices.createAccountByUSer("alice", "1234");
        DatabaseTransactions.deposit(id,500);

        BankServices.closeAccount(id,"1234");

        Accounts acc=DatabaseTransactions.findUserInfos(id);

        assertNotNull(acc);
    }

    //createAccountByUSer() tests

    @Test
    void testSuccessCreateAccountByUSer(){
        String name="Alice";
        String password="1234";

        int id= BankServices.createAccountByUSer(name,password);

        Accounts acc=DatabaseTransactions.findUserInfos(id);

        assertNotNull(acc);
    }

    @Test
    void testFailedCreateAccountByUSer(){
        String name=null;
        String password="1234";

        int id= BankServices.createAccountByUSer(name,password);

        Accounts acc=DatabaseTransactions.findUserInfos(id);
        assertNull(acc);
    }


}
