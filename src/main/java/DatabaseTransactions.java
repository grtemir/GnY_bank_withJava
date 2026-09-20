import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTransactions {
    public static int addAccount(Accounts acc) {

        String stUser = "INSERT INTO users (name,password) VALUES(?,?)";
        String stAcc = "INSERT INTO accounts(id,name,balance) VALUES(?,?,?)";

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
            pstAcc.setDouble(3, 0.0);

            pstAcc.executeUpdate();


            return id;

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
            return -1;
        }

    }

    public static boolean deleteAccount(int id) {

        String stUser = "DELETE FROM users WHERE id=?";
        String stAcc = "DELETE FROM accounts WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstAcc = conn.prepareStatement(stAcc);
             PreparedStatement pstUser = conn.prepareStatement(stUser, Statement.RETURN_GENERATED_KEYS);) {


            pstAcc.setInt(1, id);
            pstAcc.executeUpdate();

            pstUser.setInt(1, id);
            pstUser.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
            return false;
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
        return -1;
    }

    public static void deposit(int id, double amount) {
        if (amount <= 0) {
            Transaction.logGenerator(Transaction.ActionType.FAILED_DEPOSIT, id, amount);
            throw new IllegalArgumentException("Please enter positive value...");
        }

        String stm = "Update accounts SET balance=balance+? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(stm);) {
            pst.setDouble(1, amount);
            pst.setInt(2, id);
            pst.executeUpdate();
            Transaction.logGenerator(Transaction.ActionType.DEPOSIT, id, amount);

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
        }


    }


    public static boolean withdraw(int id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("withdraw amount cannot be negative");
        }
        if (amount > DatabaseTransactions.checkBalance(id)) {
            Transaction.logGenerator(Transaction.ActionType.FAILED_WITHDRAW, id, amount);
            throw new IllegalArgumentException("Insufficient funds");
        }
        String stm = "Update accounts SET balance=balance-? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(stm);) {
            pst.setDouble(1, amount);
            pst.setInt(2, id);
            pst.executeUpdate();
            Transaction.logGenerator(Transaction.ActionType.DEPOSIT, id, amount);

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
        }

        Transaction.logGenerator(Transaction.ActionType.WITHDRAW, id, amount);
        return true;
    }

    public static double checkBalance(int id) {
        double balance;
        String stm = "SELECT balance FROM accounts WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(stm)) {
            pst.setInt(1, id);
            var rs = pst.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble(1);
                return balance;
            }
        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
            return -1;
        }
        return -1;

    }

    public static void listAccount() {
        String stm = "SELECT id,name,balance FROM accounts";

        List<Accounts> accounts = new ArrayList<Accounts>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement s = conn.createStatement();) {
            ResultSet rs = s.executeQuery(stm);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double balance = rs.getDouble(3);

                System.out.println("|%5d|%30s|%8.2f|".formatted(id, name, balance));

            }

        } catch (SQLException e) {
            System.out.println("Database cannot open" + e.getMessage());
        }

    }

    public static void changePassword(int id, String newPassword) {

        String stm = "UPDATE users SET password=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(stm);) {
            pst.setString(1, newPassword);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
        }

    }

    public static Accounts findUserInfos(int id) {
        String stm = "SELECT name,password FROM users WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(stm);) {
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String password = rs.getString(2);
                String name = rs.getString(1);
                Accounts acc = new Accounts(id, password, name);

                return acc;
            }
        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
        }
        return null;
    }

    public static void logGenerator(Integer id, Integer target_id, Double amount, String action) {

        String sqlLog = "INSERT INTO logRecords(id,action,amount,target_id) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(sqlLog);) {
            pst.setObject(1, id);
            pst.setString(2, action);
            pst.setObject(3, amount);
            pst.setObject(4, target_id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
        }
    }

    public static int listLogs() {
        String sqlListLogs = "SELECT id,action,amount,target_id, crtime FROM logRecords ";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stLogs = conn.createStatement();
             ) {

            ResultSet rsLogs = stLogs.executeQuery(sqlListLogs);
            int logCount=0;
            while (rsLogs.next()) {
                Integer id = rsLogs.getObject(1,Integer.class);
                String action = rsLogs.getString(2);
                Double amount = rsLogs.getObject(3,Double.class);
                Integer target_id = rsLogs.getObject(4,Integer.class);
                String localTime = rsLogs.getString(5);
                System.out.printf("[%s] user : %s -> %s | Amount: %s | Target: %s%n",
                        localTime, id, action,
                        amount, target_id);
                logCount++;
            }
            return  logCount;

        } catch (SQLException e) {
            System.out.println("Database cannot open " + e.getMessage());
            return -1;

        }

    }

    public static int listLogsAnyUser(int id) {
    String sqlUserLogs="SELECT * FROM logRecords WHERE id=?";
        try(Connection conn=DatabaseManager.getConnection();
            PreparedStatement pstLogs=conn.prepareStatement(sqlUserLogs);){
            pstLogs.setInt(1,id);
            ResultSet rs=pstLogs.executeQuery();
            int logCount=0;
            while(rs.next()){
                int idL = rs.getInt(1);
                String action = rs.getString(2);
                double amount = rs.getDouble(3);
                int target_id = rs.getInt(4);
                String localTime = rs.getString(5);
                System.out.printf("[%s] user : %d -> %s | Amount: %f | Target: %d%n",
                        localTime, id, action, amount, target_id);
                logCount++;
            }
            return logCount;
        }catch(SQLException e){
            System.out.println("Database cannot open "+e.getMessage());
            return -1;
        }

    }

}
