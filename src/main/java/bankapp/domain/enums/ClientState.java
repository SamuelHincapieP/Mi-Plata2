package bankapp.domain.enums;

public enum ClientState {

    //Estado del cliente: Es para saber si el cliente es activo o inactivo
    ACTIVE(true),
    INACTIVE(false);

    private final boolean description;

    ClientState(boolean description){
        this.description = description;
    }

    public boolean getDescription(){
        return description;
    }
}
