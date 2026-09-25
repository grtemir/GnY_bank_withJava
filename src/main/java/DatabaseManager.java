import java.sql.*;

public class DatabaseManager {
    public static final int ADMIN_ID = 1;
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "Admin123";

    private static String URL = "jdbc:sqlite:banka.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void setUrl(String newUrl) {
        URL = newUrl;
    }

    public static void InitialTables() throws SQLException {
        String sqlUser = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL);";

        String sqlAcc = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "balance REAL DEFAULT 0.0, " +
                "FOREIGN KEY(id) REFERENCES users(id) ON DELETE CASCADE);";

        String sqlLogs = "CREATE TABLE IF NOT EXISTS logRecords(" +
                "log_id INTEGER PRIMARY KEY," +
                "sender_id INTEGER,action TEXT NOT NULL, " +
                "amount REAL,target_id INTEGER,crtime TIMESTAMP default CURRENT_TIMESTAMP" +
                ",FOREIGN KEY(sender_id) REFERENCES users(id));";


        String createAdmin = "INSERT OR IGNORE INTO users (id,name,password,role) VALUES(1,'admin',?, 'admin')";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
        ) {

            //st.execute("DROP table users");
            //st.execute("DROP table accounts");
            //st.execute("DROP table logRecords");


            st.execute("PRAGMA foreign_keys=ON;");
            st.execute(sqlUser);
            st.execute(sqlAcc);
            st.execute(sqlLogs);

            //resetLogs();;
            //resetUsersAndAccounts();
            try (
                    PreparedStatement ps = conn.prepareStatement(createAdmin);
            ) {
                String hashedPassword = SecurityManagement.hashPassword("Admin123");
                ps.setString(1, hashedPassword);
                ps.executeUpdate();
            }

            System.out.println("Tables created succesfully");
        } catch (SQLException e) {
            System.out.println("Error in creating table: " + e.getMessage());
        }


    }

    public static void resetLogs() {
        String sqlReset = "DELETE FROM logRecords";
        try (Connection conn = getConnection();

             Statement st = conn.createStatement();) {

            st.execute(sqlReset);


        } catch (SQLException e) {
            System.out.println("Error in reset table: " + e.getMessage());
        }

    }

    public static void resetUsersAndAccounts() {

        String sqlReset1 = "DELETE FROM users";
        String sqlReset2 = "DELETE FROM accounts";
        try (Connection conn = getConnection();
             Statement st1 = conn.createStatement();
             Statement st2 = conn.createStatement();) {

            st1.execute(sqlReset1);
            st1.execute(sqlReset2);


        } catch (SQLException e) {
            System.out.println("Error in reset table: " + e.getMessage());
        }

    }
}