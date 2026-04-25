package bankapp.service;

import bankapp.domain.enums.ClientState;
import bankapp.utils.ClientFormValidation;

public class ClientStateSelector {

    public static boolean selectClientState() {
        while (true) {
            System.out.println("1. Activo   2. Inactivo");
            int option = ClientFormValidation.validateInt("Opcion");
            switch (option) {
                case 1: return ClientState.ACTIVE.getDescription();
                case 2: return ClientState.INACTIVE.getDescription();
                default: System.out.println("  [!] Seleccione 1 o 2.");
            }
        }
    }
}
