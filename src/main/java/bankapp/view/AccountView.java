package bankapp.view;

import bankapp.service.AccountService;
import bankapp.utils.ClientFormValidation;

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
        double amount = ClientFormValidation.validateDouble("Ingrese el monto a consignar");
        accountService.deposit(clientId, amount);
    }

    public void retirar(int clientId) {
        double amount = ClientFormValidation.validateDouble("Ingrese el monto a retirar");
        accountService.withdraw(clientId, amount);
    }

    public void consultarMovimientos(int clientId) {
        accountService.getMovements(clientId);
    }

    public void transferir(int clientId) {
        String toAccountNumber = ClientFormValidation.validateString(
                "Ingrese el numero de cuenta destino");
        double amount = ClientFormValidation.validateDouble("Ingrese el monto a transferir");
        accountService.transfer(clientId, toAccountNumber, amount);
    }

    public void comprarConCredito(int clientId) {
        double amount = ClientFormValidation.validateDouble("Ingrese el monto de la compra");
        String description = ClientFormValidation.validateString("Descripcion de la compra");
        accountService.creditPurchase(clientId, amount, description);
    }
}
