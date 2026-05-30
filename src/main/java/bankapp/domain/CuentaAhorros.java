package bankapp.domain;

public class CuentaAhorros extends Account {

    private double tasaInteres;

    public CuentaAhorros() {
        super();
    }

    public CuentaAhorros(int cuentaID, String numeroCuenta, double saldo, String estado,
                         String fechaCreacion, int usuarioID, int tipoCuentaID, double tasaInteres) {
        super(cuentaID, numeroCuenta, saldo, estado, fechaCreacion, usuarioID, tipoCuentaID);
        this.tasaInteres = tasaInteres;
    }

    public double getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(double tasaInteres) { this.tasaInteres = tasaInteres; }
}
