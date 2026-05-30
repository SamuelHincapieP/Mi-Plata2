package bankapp.domain.enums;

public enum  MovementTypeEnum {

    CONSIGNACION("Consignacion"),
    RETIRO("Retiro"),
    TRANSFERENCIA_ENVIADA("Transferencia enviada"),
    TRANSFERENCIA_RECIBIDA("Transferencia recibida"),
    COMPRA_CREDITO("Compra con tarjeta de credito");

    private final String description;

    MovementTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}