package bankapp.services;

import bankapp.utils.FormValidator;

public class ClientTypeSelector {

    public static String selectClientType() {
        while (true) {
            System.out.println("Seleccione el tipo de cliente:");
            System.out.println("1. Nuevo   2. Antiguo");
            int option = FormValidator.validateInt("Opcion");
            switch (option) {
                case 1: return "Nuevo";
                case 2: return "Antiguo";
                default: System.out.println("  [!] Seleccione 1 o 2.");
            }
        }
    }
}
