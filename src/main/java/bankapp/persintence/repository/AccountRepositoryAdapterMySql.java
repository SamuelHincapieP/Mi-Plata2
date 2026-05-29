package bankapp.persintence.repository;

import bankapp.domain.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryAdapterMySql {

    private List<Account> accounts = new ArrayList<>();
    private int nextId = 1;

    public Account saveAccount(Account account) {
        account.setId(nextId++);
        accounts.add(account);
        return account;
    }

    public Account findAccountById(int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    public Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public Account findAccountByClientId(int clientId) {
        for (Account account : accounts) {
            if (account.getClientId() == clientId) {
                return account;
            }
        }
        return null;
    }

    public List<Account> findAllAccounts() {
        return accounts;
    }

    public Account updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == account.getId()) {
                accounts.set(i, account);
                return account;
            }
        }
        return null;
    }
}
