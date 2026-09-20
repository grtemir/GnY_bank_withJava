import java.time.LocalDateTime;
import java.util.ArrayList;

public class Transaction {
    private final int senderId;
    private final int receiverId;
    private final double amount;


    public enum ActionType {
        DEPOSIT, WITHDRAW, FAILED_DEPOSIT, FAILED_WITHDRAW, TRANSFER,
        FAILED_TRANSFER, DELETE_ACCOUNT, FAILED_DELETE_ACCOUNT, CHANGE_PASSWORD,
        FAILED_CHANGE_PASSWORD, FAILED_LOGIN, SUCCESS_LOGIN , ADMIN_LIST_LOGS ,
        ADMIN_LIST_ACCOUNTS, ADMIN_CHECK_TOTAL_BALANCE , ADMIN_CREATE_ACCOUNT,
        ADMIN_DELETE_ACCOUNT,USER_CREATE_ACCOUNT
    }

    public Transaction(int senderId, int receiverId, double amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
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
        logGenerator(action,null, null, null);
    }

    public static void logGenerator(ActionType action, Double amount) {
        logGenerator(action, null, null, amount);
    }

    public static void logGenerator(ActionType action, Integer senderId) {
        logGenerator(action, senderId, null, null);
    }

    public static void logGenerator(ActionType action, Integer senderId, Double amount) {
        logGenerator(action, senderId, null, amount);
    }

    public static void logGenerator(ActionType action, Integer senderId, Integer targetId, Double amount) {

        DatabaseTransactions.logGenerator(senderId,targetId,amount,action.toString());

    }


}
