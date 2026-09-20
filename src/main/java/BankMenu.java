import java.util.Scanner;

public class BankMenu {
    final private static Scanner scan = new Scanner(System.in);

    public static void runBankApp() {
        int chs;
        do {
            System.out.println(".---------------------------------------------.");
            System.out.println("|           WELCOME TO GNY BANK SYSTEM        |");
            System.out.println("|---------------------------------------------|");
            System.out.println("| 1:User Sign up...                           |");
            System.out.println("| 2:User Log in...                            |");
            System.out.println("| 3:Admin Log in...                           |");
            System.out.println("| 0:Exit...                                   |");
            System.out.println("| Please choose these one:                    |");
            System.out.println(".---------------------------------------------.");


            chs = scan.nextInt();

            clearCli();
            switch (chs) {
                case 1:
                    userRecord();
                    break;
                case 2:
                    userLogIn();
                    break;
                case 3:
                    adminLogIn();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("It is not a choos...");
            }
            pressEnterToContinue();
        }
        while (true);

    }


    private static void userRecord() {
        clearCli();
        System.out.println("Please enter your name:");
        String name = scan.next();
        System.out.println("Please enter your password:");
        String password = scan.next();

        BankServices.createAccountByUSer(name, password);
    }

    private static void userLogIn() {
        System.out.println("Please enter your id:");
        int id = scan.nextInt();
        System.out.println("Please enter your password:");
        String password = scan.next();

        Accounts user = BankServices.authenticate(id, password);
        if (user == null) {
            System.out.println("Wrong id or password, try again");
            return;
        }
        int chs;
        do {
            System.out.println(".---------------------------------------------.");
            System.out.println("|           WELCOME %17s                        |".formatted(user.getName()));
            System.out.println("|---------------------------------------------|");
            System.out.println("| 1:Check balance...                          |");
            System.out.println("| 2:Send money any account...                 |");
            System.out.println("| 3:Deposit money...                          |");
            System.out.println("| 4:Withdraw money from bank...               |");
            System.out.println("| 5:Change your password.    ..               |");
            System.out.println("| 0:Log out...                                |");
            System.out.println("| Please what you want:                       |");
            System.out.println(".---------------------------------------------.");

            chs = scan.nextInt();
            clearCli();
            switch (chs) {
                case 1:
                    System.out.println("Your balance is " + DatabaseTransactions.checkBalance(user.getId()));
                    break;
                case 2: {
                    System.out.println("Please enter id that you want to send money: ");
                    int receiver = scan.nextInt();
                    System.out.println("Please enter amount: ");
                    double amount = scan.nextDouble();
                    BankServices.transferMoney(user.getId(), receiver, amount);
                    break;
                }
                case 3: {
                    System.out.println("Please enter deposit amount: ");
                    double amount = scan.nextDouble();
                    DatabaseTransactions.deposit(user.getId(),amount);
                    break;
                }
                case 4: {
                    System.out.println("Please enter withdraw amount: ");
                    double amount = scan.nextDouble();
                    DatabaseTransactions.withdraw(user.getId(),amount);
                    break;
                }
                case 5: {
                    System.out.println("Please enter your current password: ");
                    String currPsw = scan.next();
                    System.out.println("Please enter your new password: ");
                    String newPsw = scan.next();
                    BankServices.changePassword(currPsw, newPsw, user);
                    break;
                }
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("It is not a choose...");
            }
            pressEnterToContinue();

        } while (true);

    }
// calculate fibonacci number

    private static void adminLogIn() {
        System.out.println("Please enter admin username : ");
        String id = scan.next();
        System.out.println("Please enter admin password: ");
        String password = scan.next();
        if (!AdminServices.adminAuth(id, password)) {
            System.out.println("You entered wrong admin info!!!");
            clearCli();
            return;        }
        clearCli();
        int chs;
        do {
            System.out.println("|---------------------------------------------|");
            System.out.println("|           WELCOME Admin                     |");
            System.out.println("|------------------=--------------------------|");
            System.out.println("| 1:List logs...                              |");
            System.out.println("| 2:List logs for any account...              |");
            System.out.println("| 3:Show total balance all of bank...         |");
            System.out.println("| 4:Delete any account...                     |");
            System.out.println("| 5:List accounts...                          |");
            System.out.println("| 0:Log out...                                |");
            System.out.println("| Please what you want:                       |");
            System.out.println(".---------------------------------------------.");

            chs = scan.nextInt();

            clearCli();
            switch (chs) {
                case 1:
                    AdminServices.listLogs();
                    break;
                case 2: {
                    System.out.println("Plase enter any id to see its log records: ");
                    int idLog = scan.nextInt();
                    AdminServices.listLogs(idLog);
                    break;
                }
                case 3: {
                    double balance;
                    balance = AdminServices.totalBalance();
                    System.out.println("Total balance is " + balance);
                    break;
                }
                case 4: {
                    int idDelete = scan.nextInt();
                    AdminServices.deleteAccountByAdmin(idDelete);
                    break;
                }
                case 5:
                    AdminServices.listAccounts();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("It is not a choose...");


            }
            pressEnterToContinue();
        }
        while (true);
    }

    private static void pressEnterToContinue() {
        System.out.println("Please enter to continue...");
        scan.nextLine();
        scan.nextLine();
    }

    private static void clearCli() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


}
