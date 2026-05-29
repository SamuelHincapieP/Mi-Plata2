package bankapp.services;

import bankapp.domain.enums.AccountTypeEnum;
import bankapp.utils.FormValidator;

public class AccountTypeSelector {

    public static AccountTypeEnum selectTypeAccount() {
        while (true) {
            System.out.println("Seleccione el tipo de cuenta:");
            System.out.println("1. Cuenta Ahorros");
            System.out.println("2. Cuenta Corriente");
            System.out.println("3. Tarjeta de Credito");

            int option = FormValidator.validateInt("Opcion");
            switch (option) {
                case 1: return AccountTypeEnum.CUENTA_AHORRO;
                case 2: return AccountTypeEnum.CUENTA_CORRIENTE;
                case 3: return AccountTypeEnum.CUENTA_CREDITO;
                default: System.out.println("  [!] Seleccione 1, 2 o 3.");
            }
        }
    }
}
