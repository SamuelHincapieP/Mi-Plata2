package bankapp.services;

import bankapp.utils.FormValidator;

public class AccountTypeSelector {

    public static int selectTypeAccount() {
        while (true) {
            System.out.println("Seleccione 1. Cuenta Ahorros  2. Cuenta Corriente  3. Tarjeta de Credito");
            int option = FormValidator.validateInt("Opcion");
            switch (option) {
                case 1: return 1;
                case 2: return 2;
                case 3: return 3;
                default:
                    System.out.println("Seleccione una opcion valida (1, 2 o 3)");
            }
        }
    }
}
