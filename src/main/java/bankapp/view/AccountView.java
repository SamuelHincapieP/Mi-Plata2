package bankapp.view;

import bankapp.domain.validations.ValidationRules;
import bankapp.services.AccountTypeSelector;
import bankapp.services.input.AccountService;
import bankapp.utils.FormRuleValidator;
import bankapp.utils.FormValidator;


public class AccountView {

    private final AccountService accountService;

    public AccountView(AccountService accountService) {
        this.accountService = accountService;
    }

    // MP-34 Abrir cuenta bancaria — elegir tipo, pedir depósito o cupo TC, crear cuenta
    public void crearCuenta(int usuarioID) {
        System.out.println("Tipo de cuenta");
        int tipoCuentaID = AccountTypeSelector.selectTypeAccount();

        double saldoInicial = 0;
        double cupoTC       = 0;

        if (tipoCuentaID == 3) {
            // MP-34 Abrir cuenta bancaria — solicitar cupo para tarjeta de crédito
            cupoTC = FormRuleValidator.readDouble(
                    "Ingrese el cupo de la tarjeta",
                    ValidationRules.POSITIVE_AMOUNT,
                    "El cupo debe ser mayor a 0");
        } else {
            // MP-34 Abrir cuenta bancaria — solicitar depósito inicial para ahorros o corriente
            saldoInicial = FormRuleValidator.readDouble(
                    "Ingrese deposito inicial (puede ser 0)",
                    value -> value >= 0,
                    "El deposito no puede ser negativo");
        }

        accountService.createAccount(usuarioID, tipoCuentaID, saldoInicial, cupoTC);
    }

    // MP-4 Consultar saldo — delegar al servicio que imprime saldo e info de la cuenta
    public void consultarSaldo(int usuarioID) {
        accountService.getAccountByUsuarioID(usuarioID);
    }

    // MP-5 Consignar dinero — pedir monto y delegar al servicio
    // MP-26 Mensaje de transacción exitosa — el servicio imprime la confirmación
    public void consignar(int usuarioID) {
        double amount = FormRuleValidator.readDouble(
                "Ingrese el monto a consignar",
                ValidationRules.POSITIVE_AMOUNT,
                "El monto debe ser mayor a 0");

        accountService.deposit(usuarioID, amount);
    }

    // MP-6 Retirar dinero — pedir monto y delegar al servicio
    // MP-26 Mensaje de transacción exitosa — el servicio imprime la confirmación
    public void retirar(int usuarioID) {
        double amount = FormRuleValidator.readDouble(
                "Ingrese el monto a retirar",
                ValidationRules.POSITIVE_AMOUNT,
                "El monto debe ser mayor a 0");

        accountService.withdraw(usuarioID, amount);
    }

    // MP-7 Consultar movimientos / MP-27 Filtrar movimientos por fecha — listar movimientos DESC
    public void consultarMovimientos(int usuarioID) {
        accountService.getMovements(usuarioID);
    }

    // MP-8 Transferencias — pedir cuenta destino y monto, delegar al servicio
    // MP-26 Mensaje de transacción exitosa — el servicio imprime la confirmación
    public void transferir(int usuarioID) {
        String numeroCuentaDestino = FormValidator.validateString("Ingrese el numero de cuenta destino");
        double amount = FormRuleValidator.readDouble(
                "Ingrese el monto a transferir",
                ValidationRules.POSITIVE_AMOUNT,
                "El monto debe ser mayor a 0");

        accountService.transfer(usuarioID, numeroCuentaDestino, amount);
    }

    // MP-9 Compra con tarjeta de crédito — pedir monto, descripción y cuotas
    // MP-26 Mensaje de transacción exitosa — el servicio imprime el detalle
    public void comprarConCredito(int usuarioID) {
        double amount = FormRuleValidator.readDouble(
                "Ingrese el monto de la compra",
                ValidationRules.POSITIVE_AMOUNT,
                "El monto debe ser mayor a 0");

        String description = FormValidator.validateString("Descripcion de la compra");

        int cuotas = FormRuleValidator.readInt(
                "Numero de cuotas (minimo 1)",
                ValidationRules.VALID_CUOTAS,
                "Las cuotas deben ser minimo 1");

        accountService.creditPurchase(usuarioID, amount, description, cuotas);
    }

    // MP-35 Ver tarjeta de crédito — mostrar cupo, deuda, cuotas y tasa mensual
    public void verTarjeta(int usuarioID) {
        accountService.getTarjetaByUsuarioID(usuarioID).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No tiene tarjeta de credito registrada")
        );
    }

    // MP-37 Aplicar interés mensual a cuenta de ahorros — delegar al servicio
    public void aplicarIntereses(int usuarioID) {
        accountService.aplicarInteresAhorros(usuarioID);
    }

    // MP-38 Aplicar cargo mensual por sobregiro — delegar al servicio
    public void aplicarCargoCorriente(int usuarioID) {
        accountService.aplicarCargoCorriente(usuarioID);
    }

    // MP-36 Pagar tarjeta de crédito — pedir monto y delegar al servicio
    // MP-26 Mensaje de transacción exitosa — el servicio imprime la confirmación
    public void pagarTarjeta(int usuarioID) {
        double monto = FormRuleValidator.readDouble(
                "Ingrese el monto a pagar",
                ValidationRules.POSITIVE_AMOUNT,
                "El monto debe ser mayor a 0");

        accountService.pagarTarjetaCredito(usuarioID, monto);
    }
}
