package bankapp.domain;


public class TarjetaCredito {

    private int    cuentaID;
    private double cupo;
    private double deuda;       // deuda = capital + intereses (total a pagar)
    private int    numeroCuotas;
    // Tasa mensual (ej: 0.025 = 2.5% mensual)
    private double tasaMensual;

    public TarjetaCredito() {}

    public TarjetaCredito(int cuentaID, double cupo, double deuda, int numeroCuotas) {
        this.cuentaID     = cuentaID;
        this.cupo         = cupo;
        this.deuda        = deuda;
        this.numeroCuotas = numeroCuotas;
        this.tasaMensual  = 0.025; // 2.5% mensual por defecto
    }

    public TarjetaCredito(int cuentaID, double cupo, double deuda, int numeroCuotas, double tasaMensual) {
        this.cuentaID     = cuentaID;
        this.cupo         = cupo;
        this.deuda        = deuda;
        this.numeroCuotas = numeroCuotas;
        this.tasaMensual  = tasaMensual;
    }

    public int    getCuentaID()    { return cuentaID; }
    public void   setCuentaID(int cuentaID) { this.cuentaID = cuentaID; }

    public double getCupo()        { return cupo; }
    public void   setCupo(double cupo) { this.cupo = cupo; }

    public double getDeuda()       { return deuda; }
    public void   setDeuda(double deuda) { this.deuda = deuda; }

    public int    getNumeroCuotas() { return numeroCuotas; }
    public void   setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }

    public double getTasaMensual() { return tasaMensual; }
    public void   setTasaMensual(double tasaMensual) { this.tasaMensual = tasaMensual; }

    public double getCupoDisponible() {
        return cupo - deuda;
    }

    public double getCuotaMensual() {
        if (numeroCuotas <= 0 || deuda <= 0) return 0;
        if (numeroCuotas == 1 || tasaMensual <= 0) return deuda;
        double i = tasaMensual;
        int    n = numeroCuotas;
        return deuda * i / (1 - Math.pow(1 + i, -n));
    }

    @Override
    public String toString() {
        return "==============================\n" +
                "Tarjeta de Credito\n" +
                "Cupo total      : $" + String.format("%.2f", cupo) + "\n" +
                "Deuda actual    : $" + String.format("%.2f", deuda) + "\n" +
                "Disponible      : $" + String.format("%.2f", getCupoDisponible()) + "\n" +
                "Cuotas restantes: " + numeroCuotas + "\n" +
                "Tasa mensual    : " + String.format("%.1f", tasaMensual * 100) + "%\n" +
                (deuda > 0 && numeroCuotas > 0
                        ? "Cuota mensual   : $" + String.format("%.2f", getCuotaMensual()) + "\n"
                        : "") +
                "==============================";
    }
}
