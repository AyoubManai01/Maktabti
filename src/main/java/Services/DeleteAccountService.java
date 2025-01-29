package Services;

import Entites.Account;


import java.util.List;

public class DeleteAccountService  {
    private List<Account> accounts;

    public DeleteAccountService(List<Account> accounts) {
        this.accounts = accounts;
    }

    public boolean deleteAccount(String id) {
        Account account = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        if (account != null) {
            accounts.remove(account);
            System.out.println("Account deleted: " + id);
            return true;
        }
        System.out.println("Account not found.");
        return false;
    }
}

