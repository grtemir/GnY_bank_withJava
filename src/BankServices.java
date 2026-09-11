import java.util.ArrayList;
import java.util.List;


public class BankServices {
    private static List<Accounts> accounts = new ArrayList<Accounts>();
    private int nextID = 1000;

    public Accounts createAccount(String name, String password) {
        Accounts newAccount = new Accounts(nextID, password, 0.0, name);

        accounts.add(newAccount);
        nextID++;
        return newAccount;
    }

    public static List<Accounts> getAccounts() {
        return accounts;
    }

    public Accounts findAccount(int id) {
        for (Accounts acc : accounts) {
            if (acc.getId() == id) {
                return acc;
            }

        }
        return null;

    }

    public void transferMoney(int sender, int receiver, double amount) {
        Accounts senderAcc = findAccount(sender);
        Accounts receiverAcc = findAccount(receiver);

        if (senderAcc == null || receiverAcc == null) {
            System.out.println("Error:no available sender or receiver account");
            Transaction.logGenerator(Transaction.ActionType.FAILED_TRANSFER, sender, receiver, amount);
            return;
        }
        if (senderAcc == receiverAcc) {
            System.out.println("Error:cannot send same account");
            Transaction.logGenerator(Transaction.ActionType.FAILED_TRANSFER, sender, receiver, amount);
            return;
        }


        senderAcc.withdraw(amount);
        receiverAcc.deposit(amount);
        Transaction.logGenerator(Transaction.ActionType.TRANSFER, sender, receiver, amount);


    }

    public Accounts authenticate(int id, String password) {
        Accounts acc;
        acc = findAccount(id);
        if (acc == null || !acc.getPassword().equals(password)) {
            System.out.println("Wrong id or password");
            return null;
        }
        Transaction.logGenerator(Transaction.ActionType.SUCCESS_LOGIN, id);
        return acc;
    }

    public void changePassword(String oldpass, String newpass, Accounts acc) {
        if (acc == null || newpass == null || newpass.trim().isEmpty()) {
            System.out.println("Invalid value...");
            Transaction.logGenerator(Transaction.ActionType.FAILED_CHANGE_PASSWORD, acc.getId());
            return;
        }
        if (acc.getPassword().equals(oldpass)) {
            acc.setPassword(newpass);
            Transaction.logGenerator(Transaction.ActionType.CHANGE_PASSWORD, acc.getId());
            return;
        }
        System.out.println("Wrong password, please try again...");
        Transaction.logGenerator(Transaction.ActionType.FAILED_CHANGE_PASSWORD, acc.getId());

    }

    public void closeAccount(int id, String password) {
        Accounts acc;
        acc = authenticate(id, password);
        if (acc == null) {
            return;
        }

        if (acc.getBalance() != 0.0) {
            System.out.println("Your balance must be zero to delete account...");
            Transaction.logGenerator(Transaction.ActionType.FAILED_DELETE_ACCOUNT, id);
            return;
        }
        accounts.remove(acc);
        Transaction.logGenerator(Transaction.ActionType.DELETE_ACCOUNT, id);

    }
}

