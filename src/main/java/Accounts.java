public class Accounts {
    private int id;
    private String password;
    private String name;
    private String role;

    public Accounts(int id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public Accounts(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public Accounts(String password, String name,String role) {
        this.password = password;
        this.name = name;
        this.role=role;
    }
    public Accounts(int id, String password, String name, String role) {
        this.password = password;
        this.name = name;
        this.role=role;
        this.id = id;
    }


    public int getId() {
        return id;
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
        if(name==null || name.length()>20){
            throw new IllegalArgumentException("The name cannot be longer than 20 char or empty!!");

        }
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if(role.equals("admin") ||role.equals("user")){
            this.role = role;
            return;}
        throw new IllegalArgumentException("Role must be admin or user!!");
    }

}
