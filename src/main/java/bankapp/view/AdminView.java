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

    // MP-40 Listar administradores — mostrar ID, nombre, correo y cargo de cada admin

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
