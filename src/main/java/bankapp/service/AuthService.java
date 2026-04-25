package bankapp.service;

import bankapp.domain.Client;
import bankapp.repository.ClientRepository;

public class AuthService {

    private static final int MAX_ATTEMPTS = 3;
    private final ClientRepository clientRepository;

    public AuthService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client loginClient(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("  [!] El correo no puede estar vacio.");
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("  [!] La clave no puede estar vacia.");
            return null;
        }

        Client client = clientRepository.findClientByEmail(email.trim());
        if (client == null) {
            System.out.println("  [!] Correo no registrado.");
            return null;
        }
        if (client.isAccountBlocked()) {
            System.out.println("  [!] Cuenta BLOQUEADA por multiples intentos. Contacte al administrador.");
            return null;
        }
        if (!client.getPassword().equals(password)) {
            int attempts = client.getAttemptsFailed() + 1;
            client.setAttemptsFailed(attempts);
            clientRepository.updateClient(client.getId());

            if (attempts >= MAX_ATTEMPTS) {
                client.setAccountBlocked(true);
                clientRepository.updateClient(client.getId());
                System.out.println("[!] Cuenta BLOQUEADA por " + MAX_ATTEMPTS + " intentos fallidos.");
            } else {
                System.out.println("[!] Clave incorrecta. Intentos restantes: "
                        + (MAX_ATTEMPTS - attempts));
            }
            return null;
        }

        client.setAttemptsFailed(0);
        clientRepository.updateClient(client.getId());
        System.out.println("  [OK] Bienvenido, " + client.getName() + "!");
        return client;
    }

    public boolean loginAdmin(String email, String password) {
        if ("admin@miplata.com".equals(email) && "Admin1234!".equals(password)) {
            System.out.println("  [OK] Bienvenido, Administrador!");
            return true;
        }
        System.out.println("[!] Credenciales incorrectas.");
        return false;
    }
}
