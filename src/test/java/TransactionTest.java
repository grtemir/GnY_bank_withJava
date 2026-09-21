import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionTest {
    @BeforeAll
    static void setUp() throws SQLException {
        DatabaseManager.setUrl("jdbc:sqlite:bank_test.db");
        DatabaseManager.InitialTables();
    }
    @Test
    void testLogsInsertedOnlyAction(){
        int beforeCount= DatabaseTransactions.listLogs();

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);
        int afterCount = DatabaseTransactions.listLogs();
        assertEquals(beforeCount+1,afterCount);
    }
    @Test
    void testLogsInsertedActionAndId(){
        int beforeCount= DatabaseTransactions.listLogs();

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS,0);
        int afterCount = DatabaseTransactions.listLogs();
        assertEquals(beforeCount+1,afterCount);
    }
    @Test
    void testLogsInsertedOnlyActionAndAmount(){
        int beforeCount= DatabaseTransactions.listLogs();

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS,0.0);
        int afterCount = DatabaseTransactions.listLogs();
        assertEquals(beforeCount+1,afterCount);
    }
    @Test
    void testLogsInsertedOnlyActionAndAmountAndId(){
        int beforeCount= DatabaseTransactions.listLogs();

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS,0,0.0);
        int afterCount = DatabaseTransactions.listLogs();
        assertEquals(beforeCount+1,afterCount);
    }

    @Test
    void testLogsInsertedAll(){
        int beforeCount= DatabaseTransactions.listLogs();

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS,0,0,0.0);
        int afterCount = DatabaseTransactions.listLogs();
        assertEquals(beforeCount+1,afterCount);
    }

    @AfterEach
    void tearDown() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("DELETE FROM logRecords;");

        } catch (SQLException e) {
            System.out.println("Error in cleaning: " + e.getMessage());
        }
    }

}
