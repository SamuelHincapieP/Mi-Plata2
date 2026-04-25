package bankapp.domain.enums;

public enum AccountTypeEnum {

    //ES PARA SABER SI ES CORRIENTE, CREDITO O AHORRO
    CUENTA_CORRIENTE("Cuenta Corriente"),
    CUENTA_CREDITO("Cuenta de crédito"),
    CUENTA_AHORRO("Cuenta de ahorros");

    private final String description;

    AccountTypeEnum(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
