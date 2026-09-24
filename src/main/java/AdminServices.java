import javax.xml.crypto.Data;

public class AdminServices {

    public static int listLogs() {
        int count =DatabaseTransactions.listLogs();
        if (count==0) {
            System.out.println("There're no logs yet...");
            return 0;
        }
        else if(count==-1){
            System.out.println("An error occured while fetching logs...");
            return 0;
        }
        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);
        return count;


    }

    public static int listLogs(int id) {

        int count =DatabaseTransactions.listLogsAnyUser(id);
        if (count==0) {
            System.out.println("There're no logs yet...");
            return 0;
        }
        else if(count==-1){
            System.out.println("An error occured while fetching logs...");
            return 0;
        }
        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);
        return count;

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

        String hashedPassword=SecurityManagement.hashPassword("0000");

        Accounts acc=new Accounts(hashedPassword,name,"user");


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


    public static int listAccounts(){
        System.out.println("|______________________________________________|");
        System.out.println("|-------------------Accounts-------------------|");
        System.out.println("|_____|______________________________|_________|");
        int count=DatabaseTransactions.listAccount();
        System.out.println("|----------------------------------------------|");
        System.out.println("|--------------------------Listed %3d accounts-|");
        System.out.println("|______________________________________________|");
        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_ACCOUNTS);
        return count;

    }
}
