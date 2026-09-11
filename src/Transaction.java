import java.time.LocalDateTime;
import java.util.ArrayList;

public class Transaction {
    private final int senderId;
    private final int receiverId;
    private final double amount;

    private static final ArrayList<String> logs = new ArrayList<>();

    public enum ActionType {
        DEPOSIT, WITHDRAW, FAILED_DEPOSIT, FAILED_WITHDRAW, TRANSFER,
        FAILED_TRANSFER, DELETE_ACCOUNT, FAILED_DELETE_ACCOUNT, CHANGE_PASSWORD,
        FAILED_CHANGE_PASSWORD, FAILED_LOGIN, SUCCESS_LOGIN , ADMIN_LIST_LOGS ,
        ADMIN_LIST_ACCOUNTS, ADMIN_CHECK_TOTAL_BALANCE , ADMIN_CREATE_ACCOUNT,
        ADMIN_DELETE_ACCOUNT
    }

    public Transaction(int senderId, int receiverId, double amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public static ArrayList<String> getLogs() {
        return new ArrayList<>(logs);
    }


    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public static void logGenerator(ActionType action) {
        logGenerator(action,-1, -1, 0.0);
    }

    public static void logGenerator(ActionType action, double amount) {
        logGenerator(action, -1, -1, amount);
    }

    public static void logGenerator(ActionType action, int senderId) {
        logGenerator(action, senderId, -1, 0.0);
    }

    public static void logGenerator(ActionType action, int senderId, double amount) {
        logGenerator(action, senderId, -1, amount);
    }

    public static void logGenerator(ActionType action, int senderId, int receiverId, double amount) {
        logs.add("[%s] ID: %d -- RECEIVER_ID: %d -- Action: %s -- Amount: %.2f \n".formatted(LocalDateTime.now(), senderId, receiverId, action, amount));

    }


}
