package bankapp.services;

import bankapp.utils.FormValidator;

public class AccountStateSelector {

    public static String selectAccountState() {
        while (true) {
            System.out.println("Seleccione 1. Activa  2. Bloqueada  3. Inactiva");
            int option = FormValidator.validateInt("Opcion");
            switch (option) {
                case 1: return "ACTIVA";
                case 2: return "BLOQUEADA";
                case 3: return "INACTIVA";
                default:
                    System.out.println("Seleccione una opcion valida (1, 2 o 3)");
            }
        }
    }
}
