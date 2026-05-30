package bankapp.domain.enums;

public enum AccountState {

    //eS EL ESTADO DE LA CUENTA
    DISPONIBLE("Disponible"),
    BLOQUEADA("Bloqueada"),
    SUSPENDIDA("Suspendida");

    private final String description;

    AccountState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
