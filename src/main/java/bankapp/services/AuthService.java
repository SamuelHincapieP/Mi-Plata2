package bankapp.services;

import bankapp.domain.Admin;
import bankapp.domain.Client;
import bankapp.services.outputport.AdminPersistencePort;
import bankapp.services.outputport.ClientPersistencePort;
import bankapp.utils.PasswordUtil;


public class AuthService {

    private static final int MAX_ATTEMPTS = 3;

    private final ClientPersistencePort clientRepository;
    private final AdminPersistencePort  adminRepository;

    public AuthService(ClientPersistencePort clientRepository,
                       AdminPersistencePort adminRepository) {
        this.clientRepository = clientRepository;
        this.adminRepository  = adminRepository;
    }

    // MP-2 Inicio de sesión / MP-28 Bloquear acceso por contraseña incorrecta — login cliente
    public Client loginClient(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            System.out.println("El correo no puede estar vacio");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("La clave no puede estar vacia");
            return null;
        }

        Client client = clientRepository.findClientByEmail(email.trim());
        if (client == null) {
            System.out.println("Correo no registrado");
            return null;
        }

        // MP-28 Bloquear acceso por contraseña incorrecta — verificar bloqueo previo
        if (client.isAccountBlocked()) {
            System.out.println("Cuenta BLOQUEADA por multiples intentos. Contacte al administrador.");
            return null;
        }

        if (!PasswordUtil.verify(password, client.getPassword())) {
            // MP-28 Bloquear acceso por contraseña incorrecta — incrementar intentos y bloquear al llegar a 3
            int attempts = client.getAttemptsFailed() + 1;
            client.setAttemptsFailed(attempts);

            if (attempts >= MAX_ATTEMPTS) {
                client.setAccountBlocked(true);
                System.out.println("Cuenta BLOQUEADA por " + MAX_ATTEMPTS + " intentos fallidos.");
            } else {
                System.out.println("Clave incorrecta. Intentos restantes: " + (MAX_ATTEMPTS - attempts));
            }

            clientRepository.updateClient(client);
            return null;
        }

        // MP-2 Inicio de sesión — login exitoso: resetear intentos y retornar cliente
        client.setAttemptsFailed(0);
        clientRepository.updateClient(client);
        return client;
    }

    // MP-2 Inicio de sesión — login administrador
    public boolean loginAdmin(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            System.out.println("El correo no puede estar vacio");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("La clave no puede estar vacia");
            return false;
        }

        Admin admin = adminRepository.findAdminByEmail(email.trim());
        if (admin == null) {
            System.out.println("Correo no registrado como administrador");
            return false;
        }

        if (!PasswordUtil.verify(password, admin.getPassword())) {
            System.out.println("Clave incorrecta");
            return false;
        }

        System.out.println("Bienvenido, " + admin.getName() + "! Cargo: " + admin.getCargo());
        return true;
    }
}
