package bankapp.domain.enums;

public enum AccountState {

    //eS EL ESTADO DE LA CUENTA
    DISPONIBLE ("Cuenta disponible"),
    BLOQUEADA ("Cuenta bloqueada"),
    SUSPENDIDA ("Cuenta suspendida");

    private final String description;

    AccountState(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}