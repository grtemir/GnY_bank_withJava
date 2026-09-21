public class Accounts {
    private int id;
    private String password;
    private String name;


    public Accounts(int id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public Accounts(String password, String name) {
        this.password = password;
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



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
