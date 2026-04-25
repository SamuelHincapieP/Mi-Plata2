package bankapp.domain;

public class Admin extends Users {

    String rol;
    String permissions;

    // constructores
    public Admin(int id, String name, String email, String password, int attemptsFailed, boolean accountBlocked, String rol, String permissions) {
        super(id, name, email, password, attemptsFailed, accountBlocked);
        this.rol = rol;
        this.permissions = permissions;
    }

    public Admin() {
        super();
    }

    public Admin(String email) {
        super(email);
    }

    // get y set
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "rol='" + rol + '\'' +
                ", permissions='" + permissions + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accountBlocked=" + accountBlocked +
                '}';
    }
}