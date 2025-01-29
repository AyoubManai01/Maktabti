package Services;


import Entites.Account;

import java.util.List;

public class ReadAccountService {
    private List<Account> accounts;

    public ReadAccountService(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccountById(String id) {
        return accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }
}

