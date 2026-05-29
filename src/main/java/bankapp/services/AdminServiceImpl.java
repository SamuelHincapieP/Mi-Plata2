package bankapp.services;

import bankapp.domain.Admin;
import bankapp.domain.Client;
import bankapp.persintence.repository.ClientRepositoryAdapterMySql;
import bankapp.services.input.AdminService;
import bankapp.services.input.ClientAdminService;

import java.util.List;
import java.util.Optional;

public class AdminServiceImpl implements AdminService, ClientAdminService {

    private final ClientRepositoryAdapterMySql clientRepositoryAdapterMySql;

    public AdminServiceImpl(Admin admin, ClientRepositoryAdapterMySql clientRepositoryAdapterMySql) {
        this.clientRepositoryAdapterMySql = clientRepositoryAdapterMySql;
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
        return clientRepositoryAdapterMySql.findAllClients();
    }

    @Override
    public void deleteClient(int id) {
        clientRepositoryAdapterMySql.deleteClient(id);
    }
}
