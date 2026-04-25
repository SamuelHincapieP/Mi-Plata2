package bankapp.repository;

import bankapp.domain.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientRepository {

    List<Client> clients = new ArrayList<>(Arrays.asList(
            new Client(1, "Juan Castaño",  "JnCast@gmail.com", "Juan12345!", 0, false, "Nuevo"),
            new Client(2, "Pepa Sepulveda","PpSep@gmail.com",  "Pepa12345!", 1, false, "Antiguo")
    ));

    private int nextId = 3;

    public Client saveClient(Client client) {
        // Validar que el ID no este duplicado
        if (findClientById(client.getId()) != null) {
            System.out.println("Ya existe un cliente con ese ID.");
            return null;
        }
        // Validar que el email no este duplicado
        if (findClientByEmail(client.getEmail()) != null) {
            System.out.println("Ya existe un cliente con ese correo.");
            return null;
        }
        clients.add(client);
        return client;
    }

    public List<Client> findAllClients() {
        for (Client client : clients) {
            System.out.println(client);
        }
        return clients;
    }

    public Client findClientById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    public Client findClientByEmail(String email) {
        for (Client client : clients) {
            if (client.getEmail() != null && client.getEmail().equalsIgnoreCase(email)) {
                return client;
            }
        }
        return null;
    }

    public Client updateClient(int id) {
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            if (id == client.getId()) {
                clients.set(i, client);
                return client;
            }
        }
        System.out.println("Cliente no encontrado");
        return null;
    }

    public void deleteClient(int id) {
        clients.removeIf(client -> client.getId() == id);
    }

    public int getNextId() {
        return nextId++;
    }
}
