package bankapp.service;

import bankapp.domain.Admin;
import bankapp.domain.Client;
import bankapp.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

public class AdminServiceImpl implements AdminService, ClientAdminService {

    private final ClientRepository clientRepository;

    public AdminServiceImpl(Admin admin, ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return null;
    }

    @Override
    public Optional<Admin> getAdminById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<Admin> getAllAdmins() {
        return List.of();
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return null;
    }

    @Override
    public void deleteAdmin(int id) {
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAllClients();
    }

    @Override
    public void deleteClient(int id) {
        clientRepository.deleteClient(id);
    }
}
