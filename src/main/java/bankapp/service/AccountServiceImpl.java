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

    // ── MP-5: Consignar ────────────────────────────────────────────────────
    @Override
    public Account deposit(int clientId, double amount) {
        Account account = validarCuentaActiva(clientId);
        if (account == null) return null;

        if (amount <= 0) {
            System.out.println("  [!] El monto debe ser mayor a 0.");
            return account;
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.updateAccount(account);
        registrarMovimiento(account, MovementTypeEnum.CONSIGNACION, amount, "Consignacion");

        System.out.println("  [OK] Consignacion exitosa!");
        System.out.println("  Nuevo saldo: $" + String.format("%.2f", account.getBalance()));
        return account;
    }

    // ── MP-6: Retirar ──────────────────────────────────────────────────────
    @Override
    public Account withdraw(int clientId, double amount) {
        Account account = validarCuentaActiva(clientId);
        if (account == null) return null;

        if (amount <= 0) {
            System.out.println("  [!] El monto debe ser mayor a 0.");
            return account;
        }
        if (account.getAccountType() != AccountTypeEnum.CUENTA_CREDITO
                && account.getBalance() < amount) {
            System.out.println("  [!] Saldo insuficiente. Saldo: $"
                    + String.format("%.2f", account.getBalance()));
            return account;
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.updateAccount(account);
        registrarMovimiento(account, MovementTypeEnum.RETIRO, amount, "Retiro");

        System.out.println("  [OK] Retiro exitoso!");
        System.out.println("  Nuevo saldo: $" + String.format("%.2f", account.getBalance()));
        return account;
    }

    // ── MP-7: Movimientos ──────────────────────────────────────────────────
    @Override
    public void getMovements(int clientId) {
        Account account = accountRepository.findAccountByClientId(clientId);
        if (account == null) {
            System.out.println("  [!] No tiene cuenta registrada.");
            return;
        }
        List<Movement> movements = account.getMovements();
        if (movements.isEmpty()) {
            System.out.println("  No hay movimientos registrados.");
            return;
        }
        System.out.println("\n--- Movimientos de " + account.getAccountNumber() + " ---");
        for (Movement m : movements) {
            System.out.println(m);
        }
        System.out.println("  Saldo actual: $" + String.format("%.2f", account.getBalance()));
    }

    // ── MP-8: Transferencia ────────────────────────────────────────────────
    @Override
    public boolean transfer(int fromClientId, String toAccountNumber, double amount) {
        Account origin = validarCuentaActiva(fromClientId);
        if (origin == null) return false;

        if (amount <= 0) {
            System.out.println("  [!] El monto debe ser mayor a 0.");
            return false;
        }
        if (origin.getAccountNumber().equals(toAccountNumber)) {
            System.out.println("  [!] No puede transferir a la misma cuenta.");
            return false;
        }

        Account destination = accountRepository.findAccountByNumber(toAccountNumber);
        if (destination == null) {
            System.out.println("  [!] Cuenta destino no encontrada: " + toAccountNumber);
            return false;
        }
        if (destination.getAccountState() == AccountState.BLOQUEADA) {
            System.out.println("  [!] La cuenta destino esta bloqueada.");
            return false;
        }
        if (origin.getBalance() < amount) {
            System.out.println("  [!] Saldo insuficiente. Saldo: $"
                    + String.format("%.2f", origin.getBalance()));
            return false;
        }

        origin.setBalance(origin.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        accountRepository.updateAccount(origin);
        accountRepository.updateAccount(destination);

        registrarMovimiento(origin, MovementTypeEnum.TRANSFERENCIA_ENVIADA,
                amount, "Transferencia a " + toAccountNumber);
        registrarMovimiento(destination, MovementTypeEnum.TRANSFERENCIA_RECIBIDA,
                amount, "Transferencia desde " + origin.getAccountNumber());

        System.out.println("  [OK] Transferencia exitosa!");
        System.out.println("  Nuevo saldo: $" + String.format("%.2f", origin.getBalance()));
        return true;
    }

    // ── MP-9: Compra credito ───────────────────────────────────────────────
    @Override
    public boolean creditPurchase(int clientId, double amount, String description) {
        Account account = validarCuentaActiva(clientId);
        if (account == null) return false;

        if (account.getAccountType() != AccountTypeEnum.CUENTA_CREDITO) {
            System.out.println("  [!] Solo aplica para cuentas de credito.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("  [!] El monto debe ser mayor a 0.");
            return false;
        }

        double nuevaDeuda = account.getBalance() - amount;
        if (nuevaDeuda < -10000000) {
            System.out.println("  [!] Cupo insuficiente. Disponible: $"
                    + String.format("%.2f", account.getBalance() + 10000000));
            return false;
        }

        account.setBalance(nuevaDeuda);
        accountRepository.updateAccount(account);
        registrarMovimiento(account, MovementTypeEnum.COMPRA_CREDITO,
                amount, "Compra: " + description);

        System.out.println("  [OK] Compra realizada!");
        System.out.println("  Cupo usado: $" + String.format("%.2f", Math.abs(account.getBalance())));
        System.out.println("  Cupo disponible: $"
                + String.format("%.2f", 10000000 + account.getBalance()));
        return true;
    }

    // ── Privados ───────────────────────────────────────────────────────────
    private Account validarCuentaActiva(int clientId) {
        Client client = clientRepository.findClientById(clientId);
        if (client == null) {
            System.out.println("  [!] Cliente no encontrado.");
            return null;
        }
        Account account = accountRepository.findAccountByClientId(clientId);
        if (account == null) {
            System.out.println("  [!] No tiene cuenta registrada.");
            return null;
        }
        if (account.getAccountState() == AccountState.BLOQUEADA) {
            System.out.println("  [!] Cuenta BLOQUEADA. Contacte soporte.");
            return null;
        }
        if (account.getAccountState() == AccountState.SUSPENDIDA) {
            System.out.println("  [!] Cuenta SUSPENDIDA.");
            return null;
        }
        return account;
    }

    private void registrarMovimiento(Account account, MovementTypeEnum type,
                                     double amount, String desc) {
        String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Movement movement = new Movement(nextMovementId++, type, amount,
                account.getBalance(), desc, account.getId());
        movement.setDate(fecha);
        account.getMovements().add(movement);
    }
}