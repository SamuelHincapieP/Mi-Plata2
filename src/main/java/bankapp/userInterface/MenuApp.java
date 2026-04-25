package bankapp.userinterface;

import bankapp.domain.Client;
import bankapp.service.AuthService;
import bankapp.utils.ClientFormValidation;
import bankapp.view.AccountView;
import bankapp.view.AdminView;
import bankapp.view.ClientView;

public class MenuApp {

    private final ClientView clientView;
    private final AdminView adminView;
    private final AccountView accountView;
    private final AuthService authService;

    public MenuApp(ClientView clientView, AdminView adminView,
                   AccountView accountView, AuthService authService) {
        this.clientView = clientView;
        this.adminView = adminView;
        this.accountView = accountView;
        this.authService = authService;
    }

    // ── Menu principal ─────────────────────────────────────────────────────
    public void showMainMenu() {
        System.out.println("=============================");
        System.out.println("   Bienvenido a Mi Plata     ");
        System.out.println("=============================");

        int init = ClientFormValidation.validateInt("Presione 1 para iniciar, 0 para salir");

        while (init != 0) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Iniciar Sesion");
            System.out.println("3. Salir");

            int option = ClientFormValidation.validateInt("Seleccione una opcion");

            switch (option) {
                case 1:
                    clientView.createClient();
                    break;
                case 2:
                    mostrarLoginMenu();
                    break;
                case 3:
                    System.out.println("Hasta luego!");
                    init = 0;
                    break;
                default:
                    System.out.println("  [!] Opcion no valida. Elija 1, 2 o 3.");
            }
        }
    }

    // ── MP-2: Login ────────────────────────────────────────────────────────
    private void mostrarLoginMenu() {
        System.out.println("\n=== INICIAR SESION ===");
        System.out.println("1. Soy Cliente");
        System.out.println("2. Soy Administrador");

        int tipo = ClientFormValidation.validateInt("Seleccione una opcion");

        if (tipo == 1) {
            String email    = ClientFormValidation.readLine("Ingrese su correo");
            String password = ClientFormValidation.readLine("Ingrese su clave");
            Client client = authService.loginClient(email, password);
            if (client != null) {
                showMenuClient(client);
            }
        } else if (tipo == 2) {
            String email    = ClientFormValidation.readLine("Correo administrador");
            String password = ClientFormValidation.readLine("Clave administrador");
            boolean ok = authService.loginAdmin(email, password);
            if (ok) {
                showMenuAdmin();
            }
        } else {
            System.out.println("  [!] Opcion no valida. Elija 1 o 2.");
        }
    }

    // ── Menu cliente (sesion activa) ───────────────────────────────────────
    private void showMenuClient(Client client) {
        System.out.println("\nBienvenido, " + client.getName() + "!");

        while (true) {
            System.out.println("\n=== MENU CLIENTE ===");
            System.out.println("1. Abrir cuenta bancaria");
            System.out.println("2. Consultar saldo");
            System.out.println("3. Consignar dinero");
            System.out.println("4. Retirar dinero");
            System.out.println("5. Ver movimientos");
            System.out.println("6. Transferir a otra cuenta");
            System.out.println("7. Compra con tarjeta de credito");
            System.out.println("8. Ver mi perfil");
            System.out.println("9. Cerrar sesion");

            int option = ClientFormValidation.validateInt("Seleccione una opcion");

            switch (option) {
                case 1:
                    accountView.crearCuenta(client.getId());
                    break;
                case 2:
                    accountView.consultarSaldo(client.getId());
                    break;
                case 3:
                    accountView.consignar(client.getId());
                    break;
                case 4:
                    accountView.retirar(client.getId());
                    break;
                case 5:
                    accountView.consultarMovimientos(client.getId());
                    break;
                case 6:
                    accountView.transferir(client.getId());
                    break;
                case 7:
                    accountView.comprarConCredito(client.getId());
                    break;
                case 8:
                    clientView.getClientById(client.getId());
                    break;
                case 9:
                    System.out.println("Sesion cerrada. Hasta luego, " + client.getName() + "!");
                    return;
                default:
                    System.out.println("  [!] Opcion no valida. Elija entre 1 y 9.");
            }
        }
    }

    // ── Menu administrador ─────────────────────────────────────────────────
    public void showMenuAdmin() {
        while (true) {
            System.out.println("\n=== MENU ADMINISTRADOR ===");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Cerrar sesion");

            int option = ClientFormValidation.validateInt("Seleccione una opcion");

            switch (option) {
                case 1:
                    customerMenuAdmin();
                    break;
                case 2:
                    System.out.println("Sesion de administrador cerrada.");
                    return;
                default:
                    System.out.println("  [!] Opcion no valida. Elija 1 o 2.");
            }
        }
    }
