package bankapp.userinterface;

import bankapp.domain.Account;
import bankapp.domain.Client;
import bankapp.services.input.AccountService;
import bankapp.persistence.database.DataBaseConnectionMySql;
import bankapp.services.AuthService;
import bankapp.utils.FormValidator;
import bankapp.utils.ScannerHolder;
import bankapp.view.AccountView;
import bankapp.view.AdminView;
import bankapp.view.ClientView;

import java.util.Optional;
import java.util.Scanner;


public class MenuApp {

    private final Scanner sc = ScannerHolder.get();

    private final ClientView     clientView;
    private final AdminView      adminView;
    private final AccountView    accountView;
    private final AuthService    authService;
    private final AccountService accountService;

    public MenuApp(ClientView clientView, AdminView adminView,
                   AccountView accountView, AuthService authService,
                   AccountService accountService) {
        this.clientView     = clientView;
        this.adminView      = adminView;
        this.accountView    = accountView;
        this.authService    = authService;
        this.accountService = accountService;
    }

    // MP-1 Registro de cliente / MP-2 Inicio de sesión — menú principal de entrada
    public void showMainMenu() {

        System.out.println("Bienvenido a Mi Plata");
        System.out.println("Presione 1 para iniciar la aplicacion");

        int init = FormValidator.validateInt("");

        while (init != 0) {

            DataBaseConnectionMySql.getInstance().getConnection();

            System.out.println("\n1. Registrar Cliente  2. Iniciar Sesion  3. Salir");
            int option = FormValidator.validateInt("Opcion");

            switch (option) {
                case 1:
                    // MP-1 Registro de cliente — ir al formulario de registro
                    clientView.createClient();
                    break;
                case 2:
                    // MP-2 Inicio de sesión — ir al login
                    mostrarLoginMenu();
                    break;
                case 3:
                    System.out.println("Saliendo de la aplicacion. Hasta luego!");
                    init = 0;
                    break;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    // MP-2 Inicio de sesión — elegir entre login cliente o administrador
    public void mostrarLoginMenu() {
        System.out.println("Seleccione 1. Cliente  2. Administrador");
        int tipo = FormValidator.validateInt("Opcion");

        if (tipo == 1) {
            loginCliente();
        } else if (tipo == 2) {
            loginAdministrador();
        } else {
            System.out.println("Opcion no valida, por favor seleccione una opcion valida");
        }
    }

    // MP-2 Inicio de sesión — autenticar cliente y abrir su menú
    private void loginCliente() {
        System.out.println("Ingrese su correo");
        String email = sc.nextLine().trim();
        System.out.println("Ingrese su clave");
        String password = sc.nextLine().trim();

        Client client = authService.loginClient(email, password);
        if (client != null) {
            showMenuClient(client);
        }
    }

    // MP-2 Inicio de sesión — autenticar administrador y abrir su menú
    private void loginAdministrador() {
        System.out.println("Correo administrador");
        String email = sc.nextLine().trim();
        System.out.println("Clave administrador");
        String password = sc.nextLine().trim();

        boolean ok = authService.loginAdmin(email, password);
        if (ok) {
            showMenuAdmin();
        }
    }

    // Menú cliente — opciones de cuenta y perfil
    public void showMenuClient(Client client) {

        System.out.println("Bienvenido, " + client.getName() + "!");

        // MP-20 Consultar datos personales — obtener número de cuenta al entrar
        final String[] numeroCuenta = { obtenerNumeroCuenta(client.getId()) };

        while (true) {
            System.out.println("\n=== MENU CLIENTE ===");
            System.out.println(" 1. Abrir cuenta");
            System.out.println(" 2. Consultar saldo");
            System.out.println(" 3. Consignar dinero");
            System.out.println(" 4. Retirar dinero");
            System.out.println(" 5. Ver movimientos");
            System.out.println(" 6. Transferir a otra cuenta");
            System.out.println(" 7. Compra con tarjeta de credito");
            System.out.println(" 8. Ver tarjeta de credito");
            System.out.println(" 9. Pagar tarjeta de credito");
            System.out.println("10. Aplicar interes mensual (ahorros)");
            System.out.println("11. Aplicar cargo mensual (corriente)");
            System.out.println("12. Mi perfil");
            System.out.println("13. Cerrar sesion");

            int option = FormValidator.validateInt("Opcion");

            switch (option) {
                case 1:
                    // MP-34 Abrir cuenta bancaria
                    accountView.crearCuenta(client.getId());
                    numeroCuenta[0] = obtenerNumeroCuenta(client.getId()); // MP-20 actualizar número
                    break;
                case 2:
                    // MP-4 Consultar saldo
                    accountView.consultarSaldo(client.getId());
                    break;
                case 3:
                    // MP-5 Consignar dinero
                    accountView.consignar(client.getId());
                    break;
                case 4:
                    // MP-6 Retirar dinero
                    accountView.retirar(client.getId());
                    break;
                case 5:
                    // MP-7 Consultar movimientos / MP-27 Filtrar movimientos por fecha
                    accountView.consultarMovimientos(client.getId());
                    break;
                case 6:
                    // MP-8 Transferencias
                    accountView.transferir(client.getId());
                    break;
                case 7:
                    // MP-9 Compra con tarjeta de crédito
                    accountView.comprarConCredito(client.getId());
                    break;
                case 8:
                    // MP-35 Ver tarjeta de crédito
                    accountView.verTarjeta(client.getId());
                    break;
                case 9:
                    // MP-36 Pagar tarjeta de crédito
                    accountView.pagarTarjeta(client.getId());
                    break;
                case 10:
                    // MP-37 Aplicar interés mensual a cuenta de ahorros
                    accountView.aplicarIntereses(client.getId());
                    break;
                case 11:
                    // MP-38 Aplicar cargo mensual por sobregiro
                    accountView.aplicarCargoCorriente(client.getId());
                    break;
                case 12:
                    // MP-20 Consultar datos personales — ver perfil propio
                    clientView.verMiPerfil(client.getId(), numeroCuenta[0]);
                    break;
                case 13:
                    // MP-19 Cerrar sesión — mensaje de despedida y volver al menú principal
                    System.out.println("Sesion cerrada. Hasta luego, " + client.getName() + "!");
                    return;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    // Menú administrador — gestión de clientes y admins
    public void showMenuAdmin() {

        while (true) {
            System.out.println("\n=== MENU ADMINISTRADOR ===");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Administradores");
            System.out.println("3. Cerrar sesion");

            int option = FormValidator.validateInt("Opcion");

            switch (option) {
                case 1:
                    // MP-3 Administración de usuarios
                    customerMenuAdmin();
                    break;
                case 2:
                    // MP-39 Crear administrador / MP-40 Listar administradores
                    adminMenuAdmin();
                    break;
                case 3:
                    // MP-19 Cerrar sesión — mensaje de sesión cerrada
                    System.out.println("Sesion cerrada.");
                    return;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    // MP-3 Administración de usuarios — submenú de gestión de clientes para el admin
    public void customerMenuAdmin() {

        System.out.println("Gestion de Clientes");
        while (true) {
            System.out.println("\n1. Crear Cliente");
            System.out.println("2. Buscar cliente por ID");   // MP-23 Buscar usuario por documento
            System.out.println("3. Modificar cliente");        // MP-10 Editar perfil / MP-25 Actualizar información
            System.out.println("4. Ver todos los clientes");   // MP-22 Listar usuarios registrados
            System.out.println("5. Eliminar cliente");         // MP-24 Eliminar usuario
            System.out.println("6. Volver");

            int option = FormValidator.validateInt("Opcion");

            switch (option) {
                case 1:
                    // MP-1 Registro de cliente — crear desde el admin
                    clientView.createClient();
                    break;
                case 2:
                    // MP-23 Buscar usuario por documento
                    clientView.getClientById();
                    break;
                case 3:
                    // MP-10 Editar perfil / MP-25 Actualizar información personal
                    clientView.updateClient();
                    break;
                case 4:
                    // MP-3 Administración de usuarios / MP-22 Listar usuarios registrados
                    adminView.getAllClients();
                    break;
                case 5:
                    // MP-24 Eliminar usuario
                    clientView.deleteClient();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    // MP-39 Crear administrador / MP-40 Listar administradores — submenú de gestión de admins
    public void adminMenuAdmin() {

        System.out.println("Gestion de Administradores");
        while (true) {
            System.out.println("\n1. Crear Administrador");  // MP-39 Crear administrador
            System.out.println("2. Ver todos");              // MP-40 Listar administradores
            System.out.println("3. Volver");

            int option = FormValidator.validateInt("Opcion");

            switch (option) {
                case 1:
                    // MP-39 Crear administrador
                    adminView.createAdmin();
                    break;
                case 2:
                    // MP-40 Listar administradores
                    adminView.getAllAdmins();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opcion no valida, por favor seleccione una opcion valida");
            }
        }
    }

    // MP-20 Consultar datos personales — obtener número de cuenta sin imprimir nada
    private String obtenerNumeroCuenta(int usuarioID) {
        Optional<Account> opt = accountService.getAccountByUsuarioIDSilent(usuarioID);
        return opt.map(Account::getNumeroCuenta).orElse(null);
    }
}
