package bankapp.service;

import bankapp.domain.enums.AccountState;
import bankapp.utils.ClientFormValidation;

public class StateCount {

    public boolean countState() {
        while (true) {
            System.out.println("Seleccione el estado de la cuenta:");
            System.out.println("1. Disponible   2. Bloqueada   3. Suspendida");
            int option = ClientFormValidation.validateInt("Opcion");
            switch (option) {
                case 1:
                    System.out.println("Estado: " + AccountState.DISPONIBLE.getDescription());
                    return true;
                case 2:
                    System.out.println("Estado: " + AccountState.BLOQUEADA.getDescription());
                    return false;
                case 3:
                    System.out.println("Estado: " + AccountState.SUSPENDIDA.getDescription());
                    return false;
                default:
                    System.out.println("  [!] Seleccione 1, 2 o 3.");
            }
        }
    }
}
