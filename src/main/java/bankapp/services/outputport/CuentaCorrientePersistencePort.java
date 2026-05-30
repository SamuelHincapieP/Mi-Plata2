package bankapp.services.outputport;

import bankapp.domain.CuentaCorriente;

import java.util.Optional;


public interface CuentaCorrientePersistencePort {

    void saveCuentaCorriente(int cuentaID, double porcentajeSobregiro, double limiteSobregiro);
    Optional<CuentaCorriente> findByCuentaID(int cuentaID);
}
