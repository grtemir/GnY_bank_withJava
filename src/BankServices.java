import java.util.ArrayList;
import java.util.List;


public class BankServices {
    private List<Accounts> accounts =new ArrayList<Accounts>();
    private int nextID=1000;
    public Accounts createAccount(String name,String password){
        Accounts newAccount = new Accounts(nextID,password,0.0,name);

        accounts.add(newAccount);
        nextID++;
        return newAccount;
    }

    public Accounts findAccount(int id){
        for(Accounts acc: accounts){
            if(acc.getId()==id){
                return acc;
            }

        }
        return null;

    }

    public void transferMoney(int sender,int receiver,double amount){
        Accounts senderAcc=findAccount(sender);
        Accounts receiverAcc=findAccount(receiver);

        if(senderAcc==null || receiverAcc==null){
            System.out.println("Error:no available sender or receiver account");
        }
        if(senderAcc==receiverAcc){
            System.out.println("Error:cannot send same account");
        }


        senderAcc.withdraw(amount);
        receiverAcc.deposit(amount);

    }

    public Accounts authenticate(int id,String password){
        Accounts acc;
        acc=findAccount(id);
        if(acc==null || !acc.getPassword().equals(password)){
            System.out.println("Wrong id or password");
            return null;
        }
        return acc;

    }
}

