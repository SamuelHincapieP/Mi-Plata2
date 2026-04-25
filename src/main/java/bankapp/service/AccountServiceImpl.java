package bankapp.service;

import bankapp.domain.Account;
import bankapp.domain.Client;
import bankapp.domain.Movement;
import bankapp.domain.enums.AccountState;
import bankapp.domain.enums.AccountTypeEnum;
import bankapp.domain.enums.MovementTypeEnum;
import bankapp.repository.AccountRepository;
import bankapp.repository.ClientRepository;
import bankapp.utils.ClientFormValidation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private int nextMovementId = 1;

    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    // ── Crear cuenta ───────────────────────────────────────────────────────
    @Override
    public Account createAccount(int clientId) {
        Client client = clientRepository.findClientById(clientId);
        if (client == null) {
            System.out.println("  [!] Cliente no encontrado.");
            return null;
        }
        if (accountRepository.findAccountByClientId(clientId) != null) {
            System.out.println("  [!] Ya tiene una cuenta registrada.");
            return accountRepository.findAccountByClientId(clientId);
        }

        Account account = new Account();
        account.setClientId(clientId);

        String accountNumber = "MP" + String.format("%06d", clientId)
                + (int)(Math.random() * 9000 + 1000);
        account.setAccountNumber(accountNumber);
        account.setAccountType(AccountTypeSelector.selectTypeAccount());

        double initialDeposit = ClientFormValidation.validateDouble(
                "Ingrese deposito inicial (0 si no desea)");
        account.setBalance(initialDeposit);
        account.setAccountState(AccountState.DISPONIBLE);
        account.setCreateDate(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Account saved = accountRepository.saveAccount(account);
        client.setAccount(saved);

        if (initialDeposit > 0) {
            registrarMovimiento(saved, MovementTypeEnum.CONSIGNACION,
                    initialDeposit, "Deposito inicial");
        }

        System.out.println("  [OK] Cuenta creada exitosamente!");
        System.out.println(saved);
        return saved;
    }

    // ── MP-4: Consultar saldo ──────────────────────────────────────────────
    @Override
    public Account getAccountByClientId(int clientId) {
        Account account = accountRepository.findAccountByClientId(clientId);
        if (account == null) {
            System.out.println("  [!] No tiene cuenta registrada.");
            return null;
        }
        if (account.getAccountState() == AccountState.BLOQUEADA) {
            System.out.println("  [!] Su cuenta esta BLOQUEADA. Contacte soporte.");
            return account;
        }
        System.out.println("\n--- Su cuenta ---");
        System.out.println(account);
        return account;
    }