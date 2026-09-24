public class BankServices {

    public static void transferMoney(int sender, int receiver, double amount) {



        if (sender == receiver) {
            System.out.println("Error:cannot send same account");
            Transaction.logGenerator(Transaction.ActionType.FAILED_TRANSFER, sender, receiver, amount);
            return;
        }


        DatabaseTransactions.withdraw(sender,amount);
        DatabaseTransactions.deposit(receiver,amount);
        Transaction.logGenerator(Transaction.ActionType.TRANSFER, sender, receiver, amount);


    }

    public static Accounts authenticate(int id, String password) {
        Accounts acc=DatabaseTransactions.findUserInfos(id);
        if (acc == null || !SecurityManagement.verifyPassword(password,acc.getPassword())) {
            System.out.println("Wrong id or password");
            return null;
        }
        Transaction.logGenerator(Transaction.ActionType.SUCCESS_LOGIN, id);
        return acc;
    }


    public static void changePassword(String currentPass, String newpass, Accounts acc) {
        if (acc == null || newpass == null || newpass.trim().isEmpty()) {
            System.out.println("Invalid value...");
            if(acc!=null) {
                Transaction.logGenerator(Transaction.ActionType.FAILED_CHANGE_PASSWORD,acc.getId());
            }return;
        }
        if (SecurityManagement.verifyPassword(currentPass,acc.getPassword())) {
            DatabaseTransactions.changePassword(acc.getId(),newpass);
            Transaction.logGenerator(Transaction.ActionType.CHANGE_PASSWORD, acc.getId());
            return;
        }
        System.out.println("Wrong password, please try again...");
        Transaction.logGenerator(Transaction.ActionType.FAILED_CHANGE_PASSWORD, acc.getId());

    }

    public static void closeAccount(int id, String password) {
        Accounts acc;
        acc = authenticate(id, password);
        if (acc == null) {
            return;
        }

        if (DatabaseTransactions.checkBalance(id) != 0.0) {
            System.out.println("Your balance must be zero to delete account...");
            Transaction.logGenerator(Transaction.ActionType.FAILED_DELETE_ACCOUNT, id);
            return;
        }
        DatabaseTransactions.deleteAccount(id);
        Transaction.logGenerator(Transaction.ActionType.DELETE_ACCOUNT, id);

    }
    public static int createAccountByUSer(String name,String password){
        if(name==null || name.trim().isEmpty()){
            System.out.println("Please dont enter empty values...");
            return -1;
        }
        String hashedPassword=SecurityManagement.hashPassword(password);

        Accounts acc=new Accounts(hashedPassword,name);

        int id=DatabaseTransactions.addAccount(acc);
        Transaction.logGenerator(Transaction.ActionType.USER_CREATE_ACCOUNT,id);
        return id;
    }
}

