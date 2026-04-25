package bankapp.service;

import bankapp.domain.Client;
import bankapp.repository.ClientRepository;
import bankapp.utils.ClientFormValidation;

import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // ── MP-1: Registro de cliente ──────────────────────────────────────────
    @Override
    public Client createClient() {
        Client client = new Client();

        client.setId(clientRepository.getNextId());
        System.out.println("ID asignado automaticamente: " + client.getId());

        client.setName(ClientFormValidation.validateString("Ingrese el nombre completo"));

        // Email: formato valido y unico
        while (true) {
            String email = ClientFormValidation.validateString("Ingrese el correo electronico");
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("  [!] Correo invalido. Formato: ejemplo@correo.com");
                continue;
            }
            if (clientRepository.findClientByEmail(email) != null) {
                System.out.println("  [!] Ese correo ya esta registrado. Use otro.");
                continue;
            }
            client.setEmail(email);
            break;
        }

        // Password segura
        while (true) {
            String password = ClientFormValidation.validateString(
                    "Ingrese la clave (min 8 caracteres, 1 mayuscula, 1 numero, 1 especial @#$%^&+=!*)");
            if (!ClientFormValidation.validatePassword(password)) {
                System.out.println("  [!] Clave no segura. Ejemplo valido: Hola123!");
                continue;
            }
            client.setPassword(password);
            break;
        }

        client.setAttemptsFailed(0);
        client.setAccountBlocked(false);
        client.setClientType(ClientTypeSelector.selectClientType());

        Client saved = clientRepository.saveClient(client);
        if (saved != null) {
            System.out.println("  [OK] Cliente registrado exitosamente!");
            System.out.println(saved);
        }
        return saved;
    }

    @Override
    public Client getClientById(int id) {
        Client client = clientRepository.findClientById(id);
        if (client == null) {
            System.out.println("  [!] No existe cliente con ID " + id);
        }
        return client;
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return Optional.ofNullable(clientRepository.findClientByEmail(email));
    }