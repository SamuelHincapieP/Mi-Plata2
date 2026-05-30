package bankapp.services;

import bankapp.utils.FormValidator;

public class ClientTypeSelector {

    public static String selectClientType() {
        while (true) {
            System.out.println("Seleccione 1. Natural  2. Juridico");
            int option = FormValidator.validateInt("Opcion");
            switch (option) {
                case 1: return "NATURAL";
                case 2: return "JURIDICO";
                default:
                    System.out.println("Seleccione una opcion valida (1 o 2)");
            }
        }
    }
}
