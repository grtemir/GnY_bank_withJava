import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static String URL = "jdbc:sqlite:banka.db";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    public static void setUrl(String newUrl){
        URL=newUrl;
    }

    public static void InitialTables() throws SQLException {
        String sqlUser = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL);";

        String sqlAcc = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "balance REAL DEFAULT 0.0, " +
                "FOREIGN KEY(id) REFERENCES users(id) ON DELETE CASCADE);";

        String sqlLogs="CREATE TABLE IF NOT EXISTS logRecords(" +
                "log_id INTEGER PRIMARY KEY,"+
                "sender_id INTEGER,action TEXT NOT NULL, "+
                "amount REAL,target_id INTEGER,crtime TIMESTAMP default CURRENT_TIMESTAMP"+
                ",FOREIGN KEY(sender_id) REFERENCES users(id));";


        try(Connection conn = getConnection();

        Statement st = conn.createStatement();) {

            st.execute("PRAGMA foreign_keys=ON;");
            st.execute(sqlUser);
            st.execute(sqlAcc);
            st.execute(sqlLogs);

            System.out.println("Tables created succesfully");
        }catch(SQLException e){
            System.out.println("Error in creating table: " + e.getMessage());
        }


    }
}