package bankapp.view;

import bankapp.domain.Admin;
import bankapp.service.AdminServiceImpl;

public class AdminView {

    private final AdminServiceImpl adminService;
    private final Admin admin;

    public AdminView(AdminServiceImpl adminService, Admin admin) {
        this.adminService = adminService;
        this.admin = admin;
    }

    public void createAdmin() {
        adminService.createAdmin(admin);
    }

    public void getAllClients() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        adminService.getAllClients().forEach(System.out::println);
    }
}
