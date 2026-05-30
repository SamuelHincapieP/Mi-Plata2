package bankapp.services.outputport;

import bankapp.domain.TarjetaCredito;

import java.util.Optional;

public interface TarjetaCreditoPersistencePort {

    TarjetaCredito saveTarjetaCredito(TarjetaCredito tc);
    Optional<TarjetaCredito> findTarjetaByCuentaID(int cuentaID);
    TarjetaCredito updateTarjetaCredito(TarjetaCredito tc);
}
