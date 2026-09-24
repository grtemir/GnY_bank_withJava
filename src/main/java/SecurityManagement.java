import com.password4j.Password;

public class SecurityManagement {

    public static String hashPassword(String password) {
        return Password.hash(password).withBcrypt().getResult();
    }

    public static boolean verifyPassword(String plainPassword,String hashedPassword){
        return Password.check(plainPassword,hashedPassword).withBcrypt();
    }
}

