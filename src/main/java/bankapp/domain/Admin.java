package bankapp.domain;

public class Admin extends Users {

    private String rol;
    private String permissions;
    private String cargo;

    public Admin(int id, String name, String email, String password,
                 int attemptsFailed, boolean accountBlocked,
                 String rol, String permissions, String cargo) {
        super(id, name, email, password, attemptsFailed, accountBlocked);
        this.rol = rol;
        this.permissions = permissions;
        this.cargo = cargo;
    }

    public Admin() {
        super();
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    @Override
    public String toString() {
        return "==============================\n" +
                "Admin ID : " + id + "\n" +
                "Nombre   : " + name + "\n" +
                "Email    : " + email + "\n" +
                "Rol      : " + rol + "\n" +
                "Cargo    : " + cargo + "\n" +
                "==============================";
    }
}
