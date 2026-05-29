package bankapp.view;

import bankapp.services.input.AccountService;
import bankapp.utils.FormValidator;

public class AccountView {

    private final AccountService accountService;

    public AccountView(AccountService accountService) {
        this.accountService = accountService;
    }

    public void crearCuenta(int clientId) {
        accountService.createAccount(clientId);
    }

    public void consultarSaldo(int clientId) {
        accountService.getAccountByClientId(clientId);
    }

    public void consignar(int clientId) {
        double amount = FormValidator.validateDouble("Ingrese el monto a consignar");
        accountService.deposit(clientId, amount);
    }

    public void retirar(int clientId) {
        double amount = FormValidator.validateDouble("Ingrese el monto a retirar");
        accountService.withdraw(clientId, amount);
    }

    public void consultarMovimientos(int clientId) {
        accountService.getMovements(clientId);
    }

    public void transferir(int clientId) {
        String toAccountNumber = FormValidator.validateString(
                "Ingrese el numero de cuenta destino");
        double amount = FormValidator.validateDouble("Ingrese el monto a transferir");
        accountService.transfer(clientId, toAccountNumber, amount);
    }

    public void comprarConCredito(int clientId) {
        double amount = FormValidator.validateDouble("Ingrese el monto de la compra");
        String description = FormValidator.validateString("Descripcion de la compra");
        accountService.creditPurchase(clientId, amount, description);
    }
}
