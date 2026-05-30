package bankapp.services.outputport;

import bankapp.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountPersistencePort {

    Account saveAccount(Account account);
    Optional<Account> findAccountById(int id);
    Optional<Account> findAccountByUsuarioID(int usuarioID);
    Optional<Account> findAccountByNumeroCuenta(String numeroCuenta);
    List<Account> findAllAccounts();
    Account updateAccount(Account account);
    void deleteAccount(int id);
}
