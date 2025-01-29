package Services;
import Entites.Account;
import Entites.AccountStatus;
import java.util.List;


public class UpdateAccountService {
    private List<Account> accounts; /*to do add requet of the table */

    public UpdateAccountService(List<Account> accounts) {
        this.accounts = accounts;
    }

    public boolean updateAccountStatus(String id, AccountStatus newStatus) {
        Account account = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        if (account != null) {
            account.setStatus(newStatus);
            System.out.println("Account status updated to: " + newStatus);
            return true;
        }
        System.out.println("Account not found.");
        return false;
    }

    public boolean resetAccountPassword(String id, String newPassword) {
        Account account = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        if (account != null) {
            return account.resetPassword(newPassword);
        }
        System.out.println("Account not found.");
        return false;
    }
}

