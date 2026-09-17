import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTransactions {
    public static int addAccount(Accounts acc) {

        String stUser = "INSERT INTO users VALUES(?,?)";
        String stAcc = "INSERT INTO accounts VALUES(?,?,?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstAcc = conn.prepareStatement(stAcc);
             PreparedStatement pstUser = conn.prepareStatement(stUser, Statement.RETURN_GENERATED_KEYS);) {

            pstUser.setString(1, acc.getName());
            pstUser.setString(2, acc.getPassword());
            pstUser.executeUpdate();

            int id = 0;
            var idRs = pstUser.getGeneratedKeys();
            if (idRs.next()) {
                id = idRs.getInt(1);
            }

            pstAcc.setInt(1, id);
            pstAcc.setString(2, acc.getName());
            pstAcc.setDouble(3, acc.getBalance());

            pstAcc.executeUpdate();


            return id;

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
            return -1;
        }

    }

    public static void deleteAccount(int id) {

        String stUser = "DELETE FROM users WHERE id=?";
        String stAcc = "DELETE FROM accounts WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstAcc = conn.prepareStatement(stAcc);
             PreparedStatement pstUser = conn.prepareStatement(stUser, Statement.RETURN_GENERATED_KEYS);) {


            pstAcc.setInt(1, id);
            pstAcc.executeUpdate();

            pstUser.setInt(1, id);
            pstUser.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
        }


    }

    public static double totalBalance() {

        String stm = "SELECT SUM(balance) FROM accounts";

        try (Connection conn = DatabaseManager.getConnection();
             Statement s = conn.createStatement();) {
            double total_balance;
            var rs = s.executeQuery(stm);
            if (rs.next()) {
                total_balance = rs.getDouble(1);
                return total_balance;
            }
        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
            return -1;
        }
    }
