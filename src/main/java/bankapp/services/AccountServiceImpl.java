package bankapp.services;

import bankapp.domain.Account;
import bankapp.domain.CuentaAhorros;
import bankapp.domain.CuentaCorriente;
import bankapp.domain.Movement;
import bankapp.domain.TarjetaCredito;
import bankapp.services.input.AccountService;
import bankapp.services.outputport.AccountPersistencePort;
import bankapp.services.outputport.ClientPersistencePort;
import bankapp.services.outputport.CuentaAhorrosPersistencePort;
import bankapp.services.outputport.CuentaCorrientePersistencePort;
import bankapp.services.outputport.MovementPersistencePort;
import bankapp.services.outputport.TarjetaCreditoPersistencePort;

import java.util.List;
import java.util.Optional;

/**
 * Servicio principal de operaciones financieras.
 *
 * Historias de usuario cubiertas:
 *   MP-4  — Consultar saldo
 *   MP-5  — Consignar dinero
 *   MP-6  — Retirar dinero
 *   MP-7  — Consultar movimientos
 *   MP-8  — Transferencias
 *   MP-9  — Compra con tarjeta de crédito
 *   MP-26 — Mensaje de transacción exitosa
 *   MP-27 — Filtrar movimientos por fecha
 *   MP-34 — Abrir cuenta bancaria
 *   MP-35 — Ver tarjeta de crédito
 *   MP-36 — Pagar tarjeta de crédito
 *   MP-37 — Aplicar interés mensual a cuenta de ahorros
 *   MP-38 — Aplicar cargo mensual por sobregiro
 */
public class AccountServiceImpl implements AccountService {

    private static final int TIPO_AHORROS   = 1;
    private static final int TIPO_CORRIENTE = 2;
    private static final int TIPO_CREDITO   = 3;

    private static final double TASA_AHORROS   = 0.02;
    private static final double TASA_SOBREGIRO = 0.05;

    private final AccountPersistencePort          accountRepository;
    private final ClientPersistencePort           clientRepository;
    private final MovementPersistencePort         movementRepository;
    private final TarjetaCreditoPersistencePort   tarjetaRepository;
    private final CuentaAhorrosPersistencePort    ahorrosRepository;
    private final CuentaCorrientePersistencePort  corrienteRepository;

    public AccountServiceImpl(AccountPersistencePort accountRepository,
                              ClientPersistencePort clientRepository,
                              MovementPersistencePort movementRepository,
                              TarjetaCreditoPersistencePort tarjetaRepository,
                              CuentaAhorrosPersistencePort ahorrosRepository,
                              CuentaCorrientePersistencePort corrienteRepository) {
        this.accountRepository   = accountRepository;
        this.clientRepository    = clientRepository;
        this.movementRepository  = movementRepository;
        this.tarjetaRepository   = tarjetaRepository;
        this.ahorrosRepository   = ahorrosRepository;
        this.corrienteRepository = corrienteRepository;
    }

    // MP-34 Abrir cuenta bancaria — crear cuenta para el cliente según tipo elegido
    @Override
    public Account createAccount(int usuarioID, int tipoCuentaID, double saldoInicial, double cupoTC) {

        if (clientRepository.findClientById(usuarioID) == null) {
            System.out.println("Cliente no encontrado");
            return null;
        }
        if (accountRepository.findAccountByUsuarioID(usuarioID).isPresent()) {
            System.out.println("Ya tiene una cuenta registrada");
            return accountRepository.findAccountByUsuarioID(usuarioID).get();
        }

        Account account = new Account();
        account.setUsuarioID(usuarioID);
        account.setNumeroCuenta("MP" + String.format("%06d", (int)(Math.random() * 900000 + 100000)));
        account.setEstado("ACTIVA");
        account.setTipoCuentaID(tipoCuentaID);
        account.setSaldo(tipoCuentaID == TIPO_CREDITO ? 0 : saldoInicial);

        Account saved = accountRepository.saveAccount(account);

        if (tipoCuentaID == TIPO_AHORROS) {
            ahorrosRepository.saveCuentaAhorros(saved.getCuentaID(), TASA_AHORROS);
        }
        if (tipoCuentaID == TIPO_CORRIENTE) {
            corrienteRepository.saveCuentaCorriente(saved.getCuentaID(), TASA_SOBREGIRO, 1_000_000.00);
        }
        // MP-34 Abrir cuenta bancaria / MP-35 Ver tarjeta de crédito — crear tarjeta si tipo TC
        if (tipoCuentaID == TIPO_CREDITO) {
            TarjetaCredito tc = new TarjetaCredito(saved.getCuentaID(), cupoTC, 0, 0);
            tarjetaRepository.saveTarjetaCredito(tc);
            System.out.println("Tarjeta de credito creada");
            System.out.println("  Cupo total   : $" + String.format("%.2f", cupoTC));
            System.out.println("  Tasa mensual : 2.5%");
        }

        // MP-26 Mensaje de transacción exitosa — registrar movimiento de depósito inicial
        if (saved.getSaldo() > 0) {
            registrarMovimiento(saved.getCuentaID(), saved.getSaldo(),
                    "Deposito inicial", saved.getSaldo(), getTipoMovimientoID("CONSIGNACION"));
        }

        System.out.println("Cuenta creada exitosamente!");
        System.out.println(saved);
        return saved;
    }

    // MP-4 Consultar saldo — mostrar cuenta, saldo e info extra según tipo
    @Override
    public Optional<Account> getAccountByUsuarioID(int usuarioID) {

        Optional<Account> opt = accountRepository.findAccountByUsuarioID(usuarioID);
        if (opt.isEmpty()) {
            System.out.println("No tiene cuenta registrada");
            return Optional.empty();
        }

        Account account = opt.get();
        System.out.println("\n--- Su cuenta ---");
        System.out.println(account);

        // MP-4 Consultar saldo — mostrar tasa e interés estimado para cuenta de ahorros
        if (account.getTipoCuentaID() == TIPO_AHORROS) {
            ahorrosRepository.findByCuentaID(account.getCuentaID()).ifPresent(ca ->
                    System.out.println("  Tasa de interes mensual: " +
                            String.format("%.1f", ca.getTasaInteres() * 100) + "%" +
                            "  |  Interes estimado este mes: $" +
                            String.format("%.2f", account.getSaldo() * ca.getTasaInteres()))
            );
        }

        // MP-4 Consultar saldo — mostrar límite de sobregiro para cuenta corriente
        if (account.getTipoCuentaID() == TIPO_CORRIENTE) {
            corrienteRepository.findByCuentaID(account.getCuentaID()).ifPresent(cc -> {
                System.out.println("  Limite sobregiro: $" + String.format("%.2f", cc.getLimiteSobregiro()));
                if (account.getSaldo() < 0) {
                    double cargo = Math.abs(account.getSaldo()) * cc.getPorcentajeSobregiro();
                    System.out.println("  ** Saldo negativo. Cargo mensual por sobregiro: $" +
                            String.format("%.2f", cargo) + " **");
                }
            });
        }

        // MP-35 Ver tarjeta de crédito — mostrar info de la TC al consultar saldo
        if (account.getTipoCuentaID() == TIPO_CREDITO) {
            tarjetaRepository.findTarjetaByCuentaID(account.getCuentaID())
                    .ifPresent(System.out::println);
        }

        return opt;
    }

    // MP-5 Consignar dinero — sumar monto al saldo, registrar movimiento y mostrar confirmación
    @Override
    public Account deposit(int usuarioID, double amount) {

        Account account = getValidAccount(usuarioID);
        if (account == null) return null;

        if (account.getTipoCuentaID() == TIPO_CREDITO) {
            System.out.println("Las tarjetas de credito no admiten consignaciones. Use 'Pagar tarjeta de credito'");
            return account;
        }

        account.setSaldo(account.getSaldo() + amount);
        accountRepository.updateAccount(account);
        // MP-26 Mensaje de transacción exitosa — registrar y mostrar confirmación
        registrarMovimiento(account.getCuentaID(), amount, "Consignacion",
                account.getSaldo(), getTipoMovimientoID("CONSIGNACION"));

        System.out.println("Consignacion exitosa!");
        System.out.println("Nuevo saldo: $" + String.format("%.2f", account.getSaldo()));
        return account;
    }

    // MP-6 Retirar dinero — validar fondos/sobregiro, actualizar saldo, registrar movimiento
    @Override
    public Account withdraw(int usuarioID, double amount) {

        Account account = getValidAccount(usuarioID);
        if (account == null) return null;

        if (account.getTipoCuentaID() == TIPO_CREDITO) {
            System.out.println("No puede retirar dinero de una tarjeta de credito");
            return account;
        }

        // MP-6 Retirar dinero — para corriente: permitir sobregiro hasta el límite
        if (account.getTipoCuentaID() == TIPO_CORRIENTE) {
            CuentaCorriente cc = corrienteRepository.findByCuentaID(account.getCuentaID()).orElse(null);
            double limite     = cc != null ? cc.getLimiteSobregiro() : 0.0;
            double disponible = account.getSaldo() + limite;

            if (disponible < amount) {
                System.out.println("Fondos insuficientes.");
                System.out.println("  Saldo actual       : $" + String.format("%.2f", account.getSaldo()));
                System.out.println("  Limite de sobregiro: $" + String.format("%.2f", limite));
                System.out.println("  Maximo a retirar   : $" + String.format("%.2f", disponible));
                return account;
            }

            account.setSaldo(account.getSaldo() - amount);
            accountRepository.updateAccount(account);
            // MP-26 Mensaje de transacción exitosa — registrar y mostrar confirmación
            registrarMovimiento(account.getCuentaID(), amount, "Retiro",
                    account.getSaldo(), getTipoMovimientoID("RETIRO"));

            System.out.println("Retiro exitoso!");
            System.out.println("Nuevo saldo: $" + String.format("%.2f", account.getSaldo()));
            if (account.getSaldo() < 0) {
                System.out.println("  ** AVISO: Su cuenta quedo en sobregiro: $" +
                        String.format("%.2f", account.getSaldo()) + " **");
                System.out.println("  Se aplicara un cargo del " +
                        (cc != null ? String.format("%.1f", cc.getPorcentajeSobregiro() * 100) : "5.0") +
                        "% mensual sobre el saldo negativo.");
            }

        } else {
            // MP-6 Retirar dinero — para ahorros: no permitir saldo negativo
            if (account.getSaldo() < amount) {
                System.out.println("Saldo insuficiente.");
                System.out.println("  Saldo disponible: $" + String.format("%.2f", account.getSaldo()));
                System.out.println("  Monto solicitado: $" + String.format("%.2f", amount));
                return account;
            }

            account.setSaldo(account.getSaldo() - amount);
            accountRepository.updateAccount(account);
            registrarMovimiento(account.getCuentaID(), amount, "Retiro",
                    account.getSaldo(), getTipoMovimientoID("RETIRO"));

            System.out.println("Retiro exitoso!");
            System.out.println("Nuevo saldo: $" + String.format("%.2f", account.getSaldo()));
        }

        return account;
    }

    // MP-7 Consultar movimientos / MP-27 Filtrar movimientos por fecha — listar ordenados DESC
    @Override
    public List<Movement> getMovements(int usuarioID) {

        Optional<Account> opt = accountRepository.findAccountByUsuarioID(usuarioID);
        if (opt.isEmpty()) {
            System.out.println("No tiene cuenta registrada");
            return List.of();
        }

        List<Movement> movements = movementRepository.findMovementsByCuentaID(opt.get().getCuentaID());
        if (movements.isEmpty()) {
            System.out.println("No hay movimientos registrados");
            return movements;
        }

        System.out.println("\n--- Movimientos de " + opt.get().getNumeroCuenta() + " ---");
        for (Movement m : movements) {
            System.out.println(m);
        }
        System.out.println("Saldo actual: $" + String.format("%.2f", opt.get().getSaldo()));
        return movements;
    }

    // MP-8 Transferencias — descontar origen, acreditar destino, registrar ambos movimientos
    @Override
    public boolean transfer(int fromUsuarioID, String toNumeroCuenta, double amount) {

        Account origin = getValidAccount(fromUsuarioID);
        if (origin == null) return false;

        if (origin.getTipoCuentaID() == TIPO_CREDITO) {
            System.out.println("No puede transferir desde una tarjeta de credito");
            return false;
        }
        if (origin.getNumeroCuenta().equals(toNumeroCuenta)) {
            System.out.println("No puede transferir a la misma cuenta");
            return false;
        }
        if (origin.getSaldo() < amount) {
            System.out.println("Saldo insuficiente.");
            System.out.println("  Saldo disponible: $" + String.format("%.2f", origin.getSaldo()));
            return false;
        }

        Optional<Account> optDest = accountRepository.findAccountByNumeroCuenta(toNumeroCuenta);
        if (optDest.isEmpty()) {
            System.out.println("Cuenta destino no encontrada: " + toNumeroCuenta);
            return false;
        }

        Account destination = optDest.get();
        if ("BLOQUEADA".equals(destination.getEstado())) {
            System.out.println("La cuenta destino esta bloqueada");
            return false;
        }

        origin.setSaldo(origin.getSaldo() - amount);
        destination.setSaldo(destination.getSaldo() + amount);
        accountRepository.updateAccount(origin);
        accountRepository.updateAccount(destination);

        // MP-26 Mensaje de transacción exitosa — registrar movimiento salida y entrada
        registrarMovimiento(origin.getCuentaID(), amount,
                "Transferencia enviada a " + toNumeroCuenta,
                origin.getSaldo(), getTipoMovimientoID("TRANSFERENCIA_OUT"));
        registrarMovimiento(destination.getCuentaID(), amount,
                "Transferencia recibida de " + origin.getNumeroCuenta(),
                destination.getSaldo(), getTipoMovimientoID("TRANSFERENCIA_IN"));

        System.out.println("Transferencia exitosa!");
        System.out.println("  Enviado a   : " + toNumeroCuenta);
        System.out.println("  Monto       : $" + String.format("%.2f", amount));
        System.out.println("  Saldo actual: $" + String.format("%.2f", origin.getSaldo()));
        return true;
    }

    // MP-9 Compra con tarjeta de crédito — validar cupo, calcular intereses, registrar movimiento
    @Override
    public boolean creditPurchase(int usuarioID, double amount, String description, int cuotas) {

        Account account = getValidAccount(usuarioID);
        if (account == null) return false;

        if (account.getTipoCuentaID() != TIPO_CREDITO) {
            System.out.println("Esta operacion solo aplica para tarjetas de credito");
            return false;
        }

        Optional<TarjetaCredito> optTc = tarjetaRepository.findTarjetaByCuentaID(account.getCuentaID());
        if (optTc.isEmpty()) {
            System.out.println("No se encontro la tarjeta de credito");
            return false;
        }
        if (cuotas < 1) {
            System.out.println("El numero de cuotas debe ser minimo 1");
            return false;
        }
        if (amount <= 0) {
            System.out.println("El monto de la compra debe ser mayor a 0");
            return false;
        }

        TarjetaCredito tc = optTc.get();

        double totalAPagar  = calcularTotalConIntereses(amount, tc.getTasaMensual(), cuotas);
        double cuotaMensual = totalAPagar / cuotas;

        if (tc.getCupoDisponible() < amount) {
            System.out.println("Cupo insuficiente para financiar esta compra.");
            System.out.println("  Valor compra          : $" + String.format("%.2f", amount));
            System.out.println("  Total con intereses   : $" + String.format("%.2f", totalAPagar));
            System.out.println("  Cupo disponible       : $" + String.format("%.2f", tc.getCupoDisponible()));
            System.out.println("  Falta                 : $" + String.format("%.2f", amount - tc.getCupoDisponible()));
            return false;
        }

        tc.setDeuda(tc.getDeuda() + amount);
        tc.setNumeroCuotas(tc.getNumeroCuotas() + cuotas);
        tarjetaRepository.updateTarjetaCredito(tc);

        // MP-26 Mensaje de transacción exitosa — registrar y mostrar detalle de la compra
        registrarMovimiento(account.getCuentaID(), amount,
                "Compra TC (" + cuotas + " cuotas): " + description,
                account.getSaldo(), getTipoMovimientoID("COMPRA_TC"));

        System.out.println("Compra realizada exitosamente!");
        System.out.println("  Descripcion           : " + description);
        System.out.println("  Valor de la compra    : $" + String.format("%.2f", amount));
        System.out.println("  Cuotas                : " + cuotas);
        System.out.println("  Tasa mensual          : " + String.format("%.1f", tc.getTasaMensual() * 100) + "%");
        System.out.println("  Cuota mensual         : $" + String.format("%.2f", cuotaMensual));
        System.out.println("  Total a pagar         : $" + String.format("%.2f", totalAPagar));
        System.out.println("  Intereses totales     : $" + String.format("%.2f", totalAPagar - amount));
        System.out.println("  Cupo restante         : $" + String.format("%.2f", tc.getCupoDisponible()));
        return true;
    }

    private double calcularTotalConIntereses(double capital, double tasa, int cuotas) {
        if (cuotas == 1 || tasa <= 0) return capital;
        double cuotaMensual = capital * tasa / (1 - Math.pow(1 + tasa, -cuotas));
        return cuotaMensual * cuotas;
    }

    // MP-37 Aplicar interés mensual a cuenta de ahorros — calcular tasa 2%, actualizar saldo, registrar
    @Override
    public void aplicarInteresAhorros(int usuarioID) {

        Account account = getValidAccount(usuarioID);
        if (account == null) return;

        if (account.getTipoCuentaID() != TIPO_AHORROS) {
            System.out.println("Esta operacion solo aplica para cuentas de ahorros");
            return;
        }
        if (account.getSaldo() <= 0) {
            System.out.println("No hay saldo para aplicar intereses");
            return;
        }

        CuentaAhorros ca = ahorrosRepository.findByCuentaID(account.getCuentaID()).orElse(null);
        if (ca == null) {
            System.out.println("No se encontro informacion de la cuenta de ahorros");
            return;
        }

        double interes    = account.getSaldo() * ca.getTasaInteres();
        double saldoAntes = account.getSaldo();

        account.setSaldo(account.getSaldo() + interes);
        accountRepository.updateAccount(account);
        registrarMovimiento(account.getCuentaID(), interes,
                "Interes mensual " + String.format("%.1f", ca.getTasaInteres() * 100) + "%",
                account.getSaldo(), getTipoMovimientoID("CONSIGNACION"));

        System.out.println("Interes mensual aplicado!");
        System.out.println("  Tasa mensual  : " + String.format("%.1f", ca.getTasaInteres() * 100) + "%");
        System.out.println("  Saldo antes   : $" + String.format("%.2f", saldoAntes));
        System.out.println("  Interes ganado: $" + String.format("%.2f", interes));
        System.out.println("  Saldo nuevo   : $" + String.format("%.2f", account.getSaldo()));
    }

    // MP-38 Aplicar cargo mensual por sobregiro — calcular 5% sobre saldo negativo, actualizar, registrar
    @Override
    public void aplicarCargoCorriente(int usuarioID) {

        Account account = getValidAccount(usuarioID);
        if (account == null) return;

        if (account.getTipoCuentaID() != TIPO_CORRIENTE) {
            System.out.println("Esta operacion solo aplica para cuentas corrientes");
            return;
        }

        CuentaCorriente cc = corrienteRepository.findByCuentaID(account.getCuentaID()).orElse(null);
        if (cc == null) {
            System.out.println("No se encontro informacion de la cuenta corriente");
            return;
        }

        if (account.getSaldo() >= 0) {
            System.out.println("Su cuenta corriente no tiene sobregiro. No se aplica cargo.");
            System.out.println("  Saldo actual: $" + String.format("%.2f", account.getSaldo()));
            return;
        }

        double saldoNegativo = Math.abs(account.getSaldo());
        double cargo         = saldoNegativo * cc.getPorcentajeSobregiro();
        double saldoAntes    = account.getSaldo();

        account.setSaldo(account.getSaldo() - cargo);
        accountRepository.updateAccount(account);
        registrarMovimiento(account.getCuentaID(), cargo,
                "Cargo mensual sobregiro " + String.format("%.1f", cc.getPorcentajeSobregiro() * 100) + "%",
                account.getSaldo(), getTipoMovimientoID("RETIRO"));

        System.out.println("Cargo mensual por sobregiro aplicado");
        System.out.println("  Tasa mensual  : " + String.format("%.1f", cc.getPorcentajeSobregiro() * 100) + "%");
        System.out.println("  Saldo anterior: $" + String.format("%.2f", saldoAntes));
        System.out.println("  Cargo aplicado: $" + String.format("%.2f", cargo));
        System.out.println("  Saldo actual  : $" + String.format("%.2f", account.getSaldo()));
    }

    // MP-36 Pagar tarjeta de crédito — abonar a la deuda, liberar cupo, registrar movimiento
    @Override
    public boolean pagarTarjetaCredito(int usuarioID, double monto) {

        Account account = getValidAccount(usuarioID);
        if (account == null) return false;

        if (account.getTipoCuentaID() != TIPO_CREDITO) {
            System.out.println("Esta operacion solo aplica para tarjetas de credito");
            return false;
        }

        Optional<TarjetaCredito> optTc = tarjetaRepository.findTarjetaByCuentaID(account.getCuentaID());
        if (optTc.isEmpty()) {
            System.out.println("No se encontro la tarjeta de credito");
            return false;
        }

        TarjetaCredito tc = optTc.get();

        if (tc.getDeuda() <= 0) {
            System.out.println("Su tarjeta no tiene deuda pendiente");
            return false;
        }
        if (monto <= 0) {
            System.out.println("El monto de pago debe ser mayor a 0");
            return false;
        }

        double pagoReal = Math.min(monto, tc.getDeuda());
        if (monto > tc.getDeuda()) {
            System.out.println("El pago supera la deuda. Se ajustara al total de la deuda: $" +
                    String.format("%.2f", tc.getDeuda()));
        }

        double deudaAntes = tc.getDeuda();
        tc.setDeuda(tc.getDeuda() - pagoReal);

        if (tc.getDeuda() <= 0.01) {
            tc.setDeuda(0);
            tc.setNumeroCuotas(0);
        } else if (tc.getNumeroCuotas() > 0) {
            double cuotaActual = tc.getCuotaMensual();
            if (cuotaActual > 0) {
                int cuotasEstimadas = (int) Math.ceil(tc.getDeuda() / cuotaActual);
                tc.setNumeroCuotas(Math.max(1, cuotasEstimadas));
            }
        }

        tarjetaRepository.updateTarjetaCredito(tc);
        // MP-26 Mensaje de transacción exitosa — registrar y mostrar confirmación de pago
        registrarMovimiento(account.getCuentaID(), pagoReal,
                "Pago tarjeta de credito",
                account.getSaldo(), getTipoMovimientoID("PAGO_TC"));

        System.out.println("Pago realizado exitosamente!");
        System.out.println("  Pago realizado  : $" + String.format("%.2f", pagoReal));
        System.out.println("  Deuda anterior  : $" + String.format("%.2f", deudaAntes));
        System.out.println("  Deuda restante  : $" + String.format("%.2f", tc.getDeuda()));
        System.out.println("  Cuotas restantes: " + tc.getNumeroCuotas());
        System.out.println("  Cupo liberado   : $" + String.format("%.2f", pagoReal));
        if (tc.getDeuda() == 0) {
            System.out.println("  ** Tarjeta pagada al dia. Cupo total disponible! **");
        }
        return true;
    }

    @Override
    public Optional<Account> getAccountByUsuarioIDSilent(int usuarioID) {
        return accountRepository.findAccountByUsuarioID(usuarioID);
    }

    // MP-35 Ver tarjeta de crédito — obtener TC por usuarioID
    @Override
    public Optional<TarjetaCredito> getTarjetaByUsuarioID(int usuarioID) {
        Optional<Account> optAcc = accountRepository.findAccountByUsuarioID(usuarioID);
        if (optAcc.isEmpty()) return Optional.empty();
        return tarjetaRepository.findTarjetaByCuentaID(optAcc.get().getCuentaID());
    }

    // ── Privados ───────────────────────────────────────────────────────────

    private Account getValidAccount(int usuarioID) {
        if (clientRepository.findClientById(usuarioID) == null) {
            System.out.println("Cliente no encontrado");
            return null;
        }
        Optional<Account> opt = accountRepository.findAccountByUsuarioID(usuarioID);
        if (opt.isEmpty()) {
            System.out.println("No tiene cuenta registrada");
            return null;
        }
        Account account = opt.get();
        if ("BLOQUEADA".equals(account.getEstado())) {
            System.out.println("Cuenta BLOQUEADA. Contacte soporte.");
            return null;
        }
        if ("INACTIVA".equals(account.getEstado())) {
            System.out.println("Cuenta INACTIVA");
            return null;
        }
        return account;
    }

    private void registrarMovimiento(int cuentaID, double monto, String descripcion,
                                     double saldoPosterior, int tipoID) {
        Movement m = new Movement();
        m.setCuentaID(cuentaID);
        m.setMontoMovimiento(monto);
        m.setDescripcionMovimiento(descripcion);
        m.setSaldoPosterior(saldoPosterior);
        m.setTipoID(tipoID);
        movementRepository.saveMovement(m);
    }

    private int getTipoMovimientoID(String tipo) {
        switch (tipo) {
            case "CONSIGNACION":       return 1;
            case "RETIRO":             return 2;
            case "TRANSFERENCIA_OUT":  return 3;
            case "TRANSFERENCIA_IN":   return 4;
            case "COMPRA_TC":          return 5;
            case "PAGO_TC":            return 6;
            default:                   return 1;
        }
    }
}
