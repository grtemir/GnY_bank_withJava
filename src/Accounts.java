public class Accounts {
    private int id;
    private String password;
    private double balance;
    private String name;


    public Accounts(int id, String password, double balance, String name) {
        this.id = id;
        this.password = password;
        this.balance = balance;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Account id cannot be negative!!");
        }
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() > 12) {
            throw new IllegalArgumentException("The password cannot be longer than 12 char or empty!!");
        }
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Ballance cannot be negative!!");
        }
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void deposit(double amount) {
        if (amount <= 0) {
            Transaction.logGenerator(Transaction.ActionType.FAILED_DEPOSIT, this.id, amount);
            throw new IllegalArgumentException("Please enter positive value...");
        }
        this.balance += amount;
        Transaction.logGenerator(Transaction.ActionType.DEPOSIT, this.id, amount);
    }


    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("withdraw amount cannot be negative");
        }
        if (amount > this.balance) {
            Transaction.logGenerator(Transaction.ActionType.FAILED_WITHDRAW, this.id, amount);
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance -= amount;
        Transaction.logGenerator(Transaction.ActionType.WITHDRAW, this.id, amount);
        return true;
    }

    public void checkBalance() {
        System.out.println("Your balance is " + this.balance);
    }
}
