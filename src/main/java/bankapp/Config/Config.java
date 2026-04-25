package bankapp.config;

import bankapp.domain.Admin;
import bankapp.repository.AccountRepository;
import bankapp.repository.ClientRepository;
import bankapp.service.*;
import bankapp.userinterface.MenuApp;
import bankapp.view.AccountView;
import bankapp.view.AdminView;
import bankapp.view.ClientView;

public class Config {

    public static MenuApp createMenuApp() {

        // Patron Simple Factory: crea todos los objetos necesarios
        // y los conecta entre capas (repository -> service -> view -> menu)

        Admin admin = new Admin();
        ClientRepository clientRepository = new ClientRepository();
        AccountRepository accountRepository = new AccountRepository();

        ClientService clientService = new ClientServiceImpl(clientRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository, clientRepository);
        AuthService authService = new AuthService(clientRepository);

        ClientView clientView = new ClientView(clientService);
        AccountView accountView = new AccountView(accountService);

        AdminServiceImpl adminService = new AdminServiceImpl(admin, clientRepository);
        AdminView adminView = new AdminView(adminService, admin);

        return new MenuApp(clientView, adminView, accountView, authService);
    }
}
