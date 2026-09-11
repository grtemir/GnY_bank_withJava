import java.util.Scanner;

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
        if (Transaction.getLogs().isEmpty()) {
            System.out.println("There're no log yet...");
            return;
        }
        for (String log : Transaction.getLogs()) {
            System.out.println(log);
        }
        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);

    }

    public static void listLogs(int id) {
        if (Transaction.getLogs().isEmpty()) {
            System.out.println("There're no log yet...");
            return;
        }
        boolean status = false;
        String logFormat = "ID: " + id;
        for (String log : Transaction.getLogs()) {
            if (log.contains(logFormat)) {
                System.out.println(log);
                status = true;
            }
        }
        if (!status) {
            System.out.println("There are no log yet for this account");
            return;
        }
        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_LOGS);

    }

    public static double totalBalance() {
       double totalBalance=0.0;

       for(Accounts acc: BankServices.getAccounts()){
           totalBalance+=acc.getBalance();
       }
        Transaction.logGenerator(Transaction.ActionType.ADMIN_CHECK_TOTAL_BALANCE,totalBalance);

        return totalBalance;
    }

    public static void createAccountByAdmin(int id,double balance,String name){
        if(name==null || name.trim().isEmpty()){
            System.out.println("Please dont enter empty values...");
            return;
        }
        else if(balance<0 || id<0) {
            System.out.println("Balance and id cannot be below zero...");
            return;
        }
        Accounts acc=new Accounts(id,"0000",balance,name);

        BankServices.getAccounts().add(acc);

        Transaction.logGenerator(Transaction.ActionType.ADMIN_CREATE_ACCOUNT,id);
    }

    public static void deleteAccountByAdmin(int id){
        if(id<0){
            System.out.println("Id cannot be below zero...");
            return;
        }
        for(Accounts acc : BankServices.getAccounts()){
            if(acc.getId()==id){
                BankServices.getAccounts().remove(acc);
                System.out.println("Deleted succesfully of account "+id);
                Transaction.logGenerator(Transaction.ActionType.ADMIN_DELETE_ACCOUNT,id);
                return;
            }
        }
        System.out.println("Cannot found this account...");
    }


    public static void listAccounts(){
        System.out.println("|______________________________________________|");
        System.out.println("|-------------------Accounts-------------------|");
        System.out.println("|_____|______________________________|_________|");
        for(Accounts acc : BankServices.getAccounts()) {
            System.out.println("|%5d|%30s|%9.2f|".formatted(acc.getId(), acc.getName(), acc.getBalance()));
        }
        System.out.println("|----------------------------------------------|");
        System.out.println("|--------------------------Listed %3d accounts-|".formatted(BankServices.getAccounts().size()));
        System.out.println("|______________________________________________|");

        Transaction.logGenerator(Transaction.ActionType.ADMIN_LIST_ACCOUNTS);
    }
}
