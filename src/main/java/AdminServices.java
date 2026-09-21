import javax.xml.crypto.Data;

public class AdminServices {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin123";

    public static boolean adminAuth(String username, String pass) {
        if (username == null || pass == null) {
            System.out.println("You entered empty value, please try again...");
            return false;
        } else if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(pass)) {
            System.out.println("Welcome to admin panel...");
            return true;
        }
        return false;
    }

    public static void listLogs() {
        int count =DatabaseTransactions.listLogs();
        if (count==0) {
            System.out.println("There're no logs yet...");
            return;
        }
        else if(count==1){
            System.out.println("An error occured while fetching logs...");
            return;
        }

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);

    }

    public static void listLogs(int id) {

        int count =DatabaseTransactions.listLogs();
        if (count==0) {
            System.out.println("There're no logs yet...");
            return;
        }
        else if(count==1){
            System.out.println("An error occured while fetching logs...");
            return;
        }
        DatabaseTransactions.listLogsAnyUser(id);
        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);

    }

    public static double totalBalance() {
       double totalBalance=0.0;

        totalBalance=DatabaseTransactions.totalBalance();

        Transaction.logGenerator(Transaction.ActionType.ADMIN_CHECK_TOTAL_BALANCE,totalBalance);

        return totalBalance;
    }

    public static int createAccountByAdmin(String name){
        if(name==null || name.trim().isEmpty()){
            System.out.println("You entered empty values...");
            return -1;
        }

        Accounts acc=new Accounts("0000",name);


        int id=DatabaseTransactions.addAccount(acc);
        Transaction.logGenerator(Transaction.ActionType.ADMIN_CREATE_ACCOUNT,id);
        return id;
    }

    public static void deleteAccountByAdmin(int id){
        if(id<0){
            System.out.println("Id cannot be below zero...");
            return;
        }
        if (DatabaseTransactions.checkBalance(id) != 0.0) {
            System.out.println("Your balance must be zero to delete account...");
            Transaction.logGenerator(Transaction.ActionType.FAILED_DELETE_ACCOUNT, id);
            return;
        }
        if(DatabaseTransactions.deleteAccount(id)) {
            Transaction.logGenerator(Transaction.ActionType.ADMIN_DELETE_ACCOUNT, id);
            return;
        }
        System.out.println("Cannot found this account...");
    }


    public static void listAccounts(){
        System.out.println("|______________________________________________|");
        System.out.println("|-------------------Accounts-------------------|");
        System.out.println("|_____|______________________________|_________|");
        DatabaseTransactions.listAccount();
        System.out.println("|----------------------------------------------|");
        System.out.println("|--------------------------Listed %3d accounts-|");
        System.out.println("|______________________________________________|");

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_ACCOUNTS);
    }
}
