package bankapp.services.outputport;

import bankapp.domain.Movement;

import java.util.List;

public interface MovementPersistencePort {

    Movement saveMovement(Movement movement);
    List<Movement> findMovementsByCuentaID(int cuentaID);
}
