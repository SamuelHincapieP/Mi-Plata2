package bankapp.services.outputport;

import bankapp.domain.CuentaAhorros;

import java.util.Optional;


public interface CuentaAhorrosPersistencePort {

    void saveCuentaAhorros(int cuentaID, double tasaInteres);
    Optional<CuentaAhorros> findByCuentaID(int cuentaID);
}
