package bankapp.config;

import bankapp.domain.Admin;
import bankapp.persintence.repository.AccountRepositoryAdapterMySql;
import bankapp.persintence.repository.ClientRepositoryAdapterMySql;
import bankapp.services.*;
import bankapp.services.input.AccountService;
import bankapp.services.input.ClientService;
import bankapp.userinterface.MenuApp;
import bankapp.view.AccountView;
import bankapp.view.AdminView;
import bankapp.view.ClientView;

public class Config {

    public static MenuApp createMenuApp() {

        // Patron Simple Factory: crea todos los objetos necesarios
        // y los conecta entre capas (repository -> service -> view -> menu)

        Admin admin = new Admin();
        ClientRepositoryAdapterMySql clientRepositoryAdapterMySql = new ClientRepositoryAdapterMySql();
        AccountRepositoryAdapterMySql accountRepositoryAdapterMySql = new AccountRepositoryAdapterMySql();

        ClientService clientService = new ClientServiceImpl(clientRepositoryAdapterMySql);
        AccountService accountService = new AccountServiceImpl(accountRepositoryAdapterMySql, clientRepositoryAdapterMySql);
        AuthService authService = new AuthService(clientRepositoryAdapterMySql);

        ClientView clientView = new ClientView(clientService);
        AccountView accountView = new AccountView(accountService);

        AdminServiceImpl adminService = new AdminServiceImpl(admin, clientRepositoryAdapterMySql);
        AdminView adminView = new AdminView(adminService, admin);

        return new MenuApp(clientView, adminView, accountView, authService);
    }
}
