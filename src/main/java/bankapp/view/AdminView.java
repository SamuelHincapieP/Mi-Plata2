package bankapp.view;

import bankapp.domain.Admin;
import bankapp.domain.Client;
import bankapp.domain.validations.ValidationRules;
import bankapp.services.AdminServiceImpl;
import bankapp.services.input.ClientAdminService;
import bankapp.utils.FormRuleValidator;
import bankapp.utils.FormValidator;

import java.util.List;


public class AdminView {

    private final AdminServiceImpl   adminService;
    private final ClientAdminService clientAdminService;

    public AdminView(AdminServiceImpl adminService, ClientAdminService clientAdminService) {
        this.adminService       = adminService;
        this.clientAdminService = clientAdminService;
    }

    // MP-39 Crear administrador — recolectar datos, validar y guardar con rol admin
    public void createAdmin() {

        String name = FormRuleValidator.readString(
                "Ingrese el nombre del administrador",
                ValidationRules.VALID_NAME,
                "El nombre debe tener minimo 3 letras y no contener numeros");

        String email = FormRuleValidator.readString(
                "Ingrese el correo",
                ValidationRules.VALID_EMAIL,
                "Correo invalido. Formato: ejemplo@correo.com");

        String password = FormRuleValidator.readString(
                "Ingrese la clave (min 8 chars, 1 mayuscula, 1 numero, 1 especial @#$%^&+=!*)",
                ValidationRules.VALID_PASSWORD,
                "Clave no segura. Ejemplo valido: Admin123!");

        String cargo = FormValidator.validateString("Ingrese el cargo (ej: Gerente, Cajero, Supervisor)");

        Admin admin = adminService.createAdmin(name, email, password, cargo);

        if (admin != null) {
            System.out.println("Administrador creado exitosamente!");
            System.out.println(admin);
        }
    }

    // MP-40 Listar administradores — mostrar ID, nombre, correo y cargo de cada admin
    public void getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();

        if (admins.isEmpty()) {
            System.out.println("No hay administradores registrados");
            return;
        }

        System.out.println("\n--- Lista de Administradores ---");
        for (Admin admin : admins) {
            System.out.println("ID    : " + admin.getId());
            System.out.println("Nombre: " + admin.getName());
            System.out.println("Correo: " + admin.getEmail());
            System.out.println("Cargo : " + admin.getCargo());
            System.out.println("------------------------------");
        }
    }

    // MP-3 Administración de usuarios / MP-22 Listar usuarios registrados — listar todos los clientes
    public void getAllClients() {
        List<Client> clients = clientAdminService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("No hay clientes registrados");
            return;
        }

        System.out.println("\n--- Lista de Clientes ---");
        for (Client client : clients) {
            System.out.println("ID    : " + client.getId());
            System.out.println("Nombre: " + client.getName());
            System.out.println("Correo: " + client.getEmail());
            System.out.println("Tipo  : " + client.getClientType());
            System.out.println("------------------------------");
        }
    }
}
