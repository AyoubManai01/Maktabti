package Services;
import Entites.Account;
import java.util.List;

public class CreateAccountService {
    private List<Account> accounts;

    public CreateAccountService(List<Account> accounts) {
        this.accounts = accounts;
    }

    public boolean createAccount(Account account) {
        if (accounts.stream().noneMatch(a -> a.getId().equals(account.getId()))) {
            accounts.add(account);
            System.out.println("Account created: " + account.getId());
            return true;
        }
        System.out.println("Account creation failed. ID already exists.");
        return false;
    }
}

