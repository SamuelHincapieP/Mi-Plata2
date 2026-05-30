package bankapp.services.input;

import bankapp.domain.Admin;

import java.util.List;

public interface AdminService {

    Admin createAdmin(String name, String email, String password, String cargo);
    List<Admin> getAllAdmins();
}
