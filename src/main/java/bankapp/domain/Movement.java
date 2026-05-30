package bankapp.domain;


public class Movement {

    private int    movimientoID;
    private double montoMovimiento;
    private String fechaMovimiento;
    private String descripcionMovimiento;
    private double saldoPosterior;
    private int    cuentaID;
    private int    tipoID;

    // Nombre legible del tipo — se resuelve al mapear desde BD
    private String tipoNombre;

    public Movement() {}

    public Movement(int movimientoID, double montoMovimiento, String fechaMovimiento,
                    String descripcionMovimiento, double saldoPosterior,
                    int cuentaID, int tipoID) {
        this.movimientoID           = movimientoID;
        this.montoMovimiento        = montoMovimiento;
        this.fechaMovimiento        = fechaMovimiento;
        this.descripcionMovimiento  = descripcionMovimiento;
        this.saldoPosterior         = saldoPosterior;
        this.cuentaID               = cuentaID;
        this.tipoID                 = tipoID;
    }

    public int    getMovimientoID()   { return movimientoID; }
    public void   setMovimientoID(int movimientoID)  { this.movimientoID = movimientoID; }

    public double getMontoMovimiento()  { return montoMovimiento; }
    public void   setMontoMovimiento(double montoMovimiento) { this.montoMovimiento = montoMovimiento; }

    public String getFechaMovimiento()  { return fechaMovimiento; }
    public void   setFechaMovimiento(String fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getDescripcionMovimiento() { return descripcionMovimiento; }
    public void   setDescripcionMovimiento(String d) { this.descripcionMovimiento = d; }

    public double getSaldoPosterior()  { return saldoPosterior; }
    public void   setSaldoPosterior(double saldoPosterior) { this.saldoPosterior = saldoPosterior; }

    public int    getCuentaID()  { return cuentaID; }
    public void   setCuentaID(int cuentaID) { this.cuentaID = cuentaID; }

    public int    getTipoID()    { return tipoID; }
    public void   setTipoID(int tipoID) { this.tipoID = tipoID; }

    public String getTipoNombre() { return tipoNombre; }
    public void   setTipoNombre(String tipoNombre) { this.tipoNombre = tipoNombre; }

    /**
     * MP-7 / MP-27 — Criterio: "Cada movimiento debe mostrar fecha, descripcion,
     * monto, saldo posterior y tipo."
     */
    @Override
    public String toString() {
        String tipo = (tipoNombre != null && !tipoNombre.isEmpty())
                ? tipoNombre
                : resolverTipo(tipoID);
        return "  Fecha       : " + fechaMovimiento + "\n" +
                "  Tipo        : " + tipo + "\n" +
                "  Descripcion : " + descripcionMovimiento + "\n" +
                "  Monto       : $" + String.format("%.2f", montoMovimiento) + "\n" +
                "  Saldo post. : $" + String.format("%.2f", saldoPosterior) + "\n" +
                "  " + "-".repeat(40);
    }

    /** Fallback si tipoNombre no viene de la BD */
    private String resolverTipo(int id) {
        switch (id) {
            case 1: return "Consignacion";
            case 2: return "Retiro";
            case 3: return "Transferencia enviada";
            case 4: return "Transferencia recibida";
            case 5: return "Compra con tarjeta de credito";
            case 6: return "Pago tarjeta de credito";
            default: return "Operacion";
        }
    }
}
