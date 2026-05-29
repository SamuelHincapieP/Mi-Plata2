package bankapp.services.input;

import bankapp.domain.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    public Admin createAdmin(Admin admin);
    public Optional<Admin> getAdminById(int id);
    public Optional<Admin> getAdminByEmail(String email);
    public List<Admin> getAllAdmins();
    public Admin updateAdmin(Admin admin);
    public void deleteAdmin(int id);
}
