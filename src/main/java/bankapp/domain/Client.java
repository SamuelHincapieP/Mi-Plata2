package bankapp.domain;

public class Client extends Users {

    private String clientType;

    public Client(int id, String name, String email, String password,
                  int attemptsFailed, boolean accountBlocked, String clientType) {
        super(id, name, email, password, attemptsFailed, accountBlocked);
        this.clientType = clientType;
    }

    public Client() {
        super();
    }

    public String getClientType() { return clientType; }
    public void setClientType(String clientType) { this.clientType = clientType; }

    @Override
    public String toString() {
        return "==============================\n" +
                "ID       : " + id + "\n" +
                "Nombre   : " + name + "\n" +
                "Email    : " + email + "\n" +
                "Tipo     : " + clientType + "\n" +
                "Bloqueado: " + accountBlocked + "\n" +
                "==============================";
    }
}
