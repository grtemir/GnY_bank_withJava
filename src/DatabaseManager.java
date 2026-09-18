import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:banka.db";
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver basariyla yuklendi!");
        } catch (ClassNotFoundException e) {
            System.err.println("SURUCU YUKLENEMEDI: " + e.getMessage());
        }
        return DriverManager.getConnection(URL);
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


        try(Connection conn = getConnection();

        Statement st = conn.createStatement();) {

            st.execute("PRAGMA foreign_keys=ON;");
            st.execute(sqlUser);
            st.execute(sqlAcc);

            System.out.println("Tables created succesfully");
        }catch(SQLException e){
            System.out.println("Error in creating table: " + e.getMessage());
        }


    }
}