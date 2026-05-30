package bankapp.view;

import bankapp.domain.Client;
import bankapp.domain.validations.ValidationRules;
import bankapp.services.ClientTypeSelector;
import bankapp.services.input.ClientService;
import bankapp.utils.FormRuleValidator;
import bankapp.utils.FormValidator;


public class ClientView {

    private final ClientService clientService;

    public ClientView(ClientService clientService) {
        this.clientService = clientService;
    }

    // MP-1 Registro de cliente — recolectar nombre, correo, clave y tipo, luego guardar
    public void createClient() {

        String name = FormRuleValidator.readString(
                "Ingrese el nombre completo",
                ValidationRules.VALID_NAME,
                "El nombre debe tener minimo 3 letras y no contener numeros");

        String email = FormRuleValidator.readString(
                "Ingrese el correo electronico",
                ValidationRules.VALID_EMAIL,
                "Correo invalido. Formato: ejemplo@correo.com");

        String password = FormRuleValidator.readString(
                "Ingrese la clave (min 8 chars, 1 mayuscula, 1 numero, 1 especial @#$%^&+=!*)",
                ValidationRules.VALID_PASSWORD,
                "Clave no segura. Ejemplo valido: Hola123!");

        System.out.println("Tipo de Cliente");
        String clientType = ClientTypeSelector.selectClientType();

        Client client = clientService.createClient(name, email, password, clientType);

        if (client != null) {
            System.out.println("Cliente registrado exitosamente!");
            System.out.println(client);
        }
    }

    // MP-20 Consultar datos personales — mostrar perfil propio con número de cuenta
    public void verMiPerfil(int id, String numeroCuenta) {
        Client client = clientService.getClientById(id);
        if (client != null) {
            System.out.println("\n=== Mi Perfil ===");
            System.out.println("ID              : " + client.getId());
            System.out.println("Nombre          : " + client.getName());
            System.out.println("Correo          : " + client.getEmail());
            System.out.println("Tipo de usuario : " + client.getClientType());
            if (numeroCuenta != null && !numeroCuenta.isEmpty()) {
                System.out.println("Numero de cuenta: " + numeroCuenta);
            } else {
                System.out.println("Numero de cuenta: (sin cuenta abierta)");
            }
            System.out.println("=================");
        } else {
            System.out.println("No se pudo cargar el perfil");
        }
    }

    // MP-23 Buscar usuario por documento — pedir ID y mostrar datos del cliente
    public void getClientById() {
        int id = FormRuleValidator.readInt(
                "Ingrese el ID del cliente a buscar",
                ValidationRules.POSITIVE_NUMBER,
                "El ID debe ser numerico y mayor a 0");

        Client client = clientService.getClientById(id);
        if (client != null) {
            System.out.println("\n--- Cliente encontrado ---");
            System.out.println("ID    : " + client.getId());
            System.out.println("Nombre: " + client.getName());
            System.out.println("Correo: " + client.getEmail());
            System.out.println("Tipo  : " + client.getClientType());
        } else {
            System.out.println("Cliente no encontrado con ID: " + id);
        }
    }

    // MP-10 Editar perfil / MP-25 Actualizar información personal — elegir campo y actualizar
    public void updateClient() {
        int id = FormRuleValidator.readInt(
                "Ingrese el ID del cliente a actualizar",
                ValidationRules.POSITIVE_NUMBER,
                "El ID debe ser mayor a 0");

        Client client = clientService.getClientById(id);

        if (client == null) {
            System.out.println("Cliente no encontrado con ID: " + id);
            return;
        }

        System.out.println("Cliente encontrado: " + client.getName());
        System.out.println("Actualizar  1. Nombre  2. Correo  3. Clave  4. Tipo cliente");
        int option = FormValidator.validateInt("Opcion");

        String name       = client.getName();
        String email      = client.getEmail();
        String password   = client.getPassword();
        String clientType = client.getClientType();

        switch (option) {
            case 1:
                // MP-10 Editar perfil — actualizar nombre con validación
                name = FormRuleValidator.readString(
                        "Nuevo nombre",
                        ValidationRules.VALID_NAME,
                        "El nombre debe tener minimo 3 letras y no contener numeros");
                break;
            case 2:
                // MP-25 Actualizar información personal — actualizar correo con validación
                email = FormRuleValidator.readString(
                        "Nuevo correo",
                        ValidationRules.VALID_EMAIL,
                        "Correo invalido. Formato: ejemplo@correo.com");
                break;
            case 3:
                // MP-11 Cambio de contraseña / MP-21 Recuperar contraseña — actualizar clave segura
                password = FormRuleValidator.readString(
                        "Nueva clave (min 8 chars, 1 mayuscula, 1 numero, 1 especial @#$%^&+=!*)",
                        ValidationRules.VALID_PASSWORD,
                        "Clave no segura. Ejemplo valido: Hola123!");
                break;
            case 4:
                // MP-10 Editar perfil — actualizar tipo de cliente
                System.out.println("Tipo de Cliente");
                clientType = ClientTypeSelector.selectClientType();
                break;
            default:
                System.out.println("Opcion no valida");
                return;
        }

        Client updated = clientService.updateClient(id, name, email, password, clientType);
        if (updated != null) {
            System.out.println("Cliente actualizado exitosamente!");
        }
    }

    // MP-24 Eliminar usuario — pedir ID, confirmar existencia y eliminar
    public void deleteClient() {
        int id = FormRuleValidator.readInt(
                "Ingrese el ID del cliente a eliminar",
                ValidationRules.POSITIVE_NUMBER,
                "El ID debe ser numerico y mayor a 0");

        Client client = clientService.getClientById(id);
        if (client == null) {
            System.out.println("Cliente no encontrado con ID: " + id);
            return;
        }

        clientService.deleteClient(id);
        System.out.println("Cliente eliminado exitosamente.");
    }
}
