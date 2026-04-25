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

    // ── MP-3: Actualizar cliente ───────────────────────────────────────────
    @Override
    public Client updateClient(int id) {
        Client client = clientRepository.findClientById(id);

        if (client == null) {
            System.out.println("  [!] Cliente con ID " + id + " no encontrado.");
            return null;
        }

        System.out.println("Que desea actualizar?");
        System.out.println("1. Nombre   2. Correo   3. Clave   4. Tipo cliente");

        int option = ClientFormValidation.validateInt("Opcion");

        switch (option) {
            case 1:
                client.setName(ClientFormValidation.validateString("Nuevo nombre"));
                break;
            case 2:
                while (true) {
                    String email = ClientFormValidation.validateString("Nuevo correo");
                    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                        System.out.println("  [!] Correo invalido.");
                        continue;
                    }
                    Client existing = clientRepository.findClientByEmail(email);
                    if (existing != null && existing.getId() != id) {
                        System.out.println("  [!] Ese correo ya esta en uso.");
                        continue;
                    }
                    client.setEmail(email);
                    break;
                }
                break;
            case 3:
                while (true) {
                    String password = ClientFormValidation.validateString("Nueva clave");
                    if (!ClientFormValidation.validatePassword(password)) {
                        System.out.println("  [!] Clave no segura. Ejemplo: Hola123!");
                        continue;
                    }
                    client.setPassword(password);
                    break;
                }
                break;
            case 4:
                client.setClientType(ClientTypeSelector.selectClientType());
                break;
            default:
                System.out.println("  [!] Opcion no valida.");
                return client;
        }

        clientRepository.updateClient(client.getId());
        System.out.println("  [OK] Cliente actualizado exitosamente!");
        return client;
    }

    // ── MP-3: Eliminar cliente ─────────────────────────────────────────────
    @Override
    public void deleteClient(int id) {
        Client client = clientRepository.findClientById(id);
        if (client == null) {
            System.out.println("  [!] Cliente con ID " + id + " no encontrado.");
            return;
        }
        clientRepository.deleteClient(id);
        System.out.println("  [OK] Cliente eliminado exitosamente.");
    }
}