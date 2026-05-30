package bankapp.services.input;

import bankapp.domain.Account;
import bankapp.domain.Movement;
import bankapp.domain.TarjetaCredito;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account createAccount(int usuarioID, int tipoCuentaID, double saldoInicial, double cupoTC);

    Optional<Account> getAccountByUsuarioID(int usuarioID);

    // Version silenciosa: no imprime nada, usada internamente (ej: cargar perfil)
    Optional<Account> getAccountByUsuarioIDSilent(int usuarioID);

    // MP-5: Consignar
    Account deposit(int usuarioID, double amount);

    // MP-6: Retirar
    Account withdraw(int usuarioID, double amount);

    // MP-7: Movimientos
    List<Movement> getMovements(int usuarioID);

    // MP-8: Transferencia
    boolean transfer(int fromUsuarioID, String toNumeroCuenta, double amount);

    // MP-9: Compra TC
    boolean creditPurchase(int usuarioID, double amount, String description, int cuotas);

    // Ver tarjeta
    Optional<TarjetaCredito> getTarjetaByUsuarioID(int usuarioID);

    // Interés mensual cuenta ahorros
    void aplicarInteresAhorros(int usuarioID);

    // Cobro mensual por sobregiro en cuenta corriente
    void aplicarCargoCorriente(int usuarioID);

    // Pago de cuota mensual tarjeta de crédito
    boolean pagarTarjetaCredito(int usuarioID, double monto);
}
