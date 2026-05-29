package bankapp.services.input;

import bankapp.domain.Account;

public interface  AccountService {

    // MP-4: Consultar saldo
    public Account getAccountByClientId(int clientId);

    // MP-5: Consignar dinero
    public Account deposit(int clientId, double amount);

    // MP-6: Retirar dinero
    public Account withdraw(int clientId, double amount);

    // MP-7: Consultar movimientos
    public void getMovements(int clientId);

    // MP-8: Transferencia
    public boolean transfer(int fromClientId, String toAccountNumber, double amount);

    // MP-9: Compra con tarjeta de credito
    public boolean creditPurchase(int clientId, double amount, String description);

    // Crear cuenta para un cliente
    public Account createAccount(int clientId);
}
