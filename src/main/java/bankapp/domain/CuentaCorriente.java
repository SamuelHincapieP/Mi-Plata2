package bankapp.domain;

public class CuentaCorriente extends Account {

    private double porcentajeSobregiro;
    private double limiteSobregiro;

    public CuentaCorriente() {
        super();
    }

    public CuentaCorriente(int cuentaID, String numeroCuenta, double saldo, String estado,
                           String fechaCreacion, int usuarioID, int tipoCuentaID,
                           double porcentajeSobregiro, double limiteSobregiro) {
        super(cuentaID, numeroCuenta, saldo, estado, fechaCreacion, usuarioID, tipoCuentaID);
        this.porcentajeSobregiro = porcentajeSobregiro;
        this.limiteSobregiro = limiteSobregiro;
    }

    public double getPorcentajeSobregiro() { return porcentajeSobregiro; }
    public void setPorcentajeSobregiro(double porcentajeSobregiro) { this.porcentajeSobregiro = porcentajeSobregiro; }

    public double getLimiteSobregiro() { return limiteSobregiro; }
    public void setLimiteSobregiro(double limiteSobregiro) { this.limiteSobregiro = limiteSobregiro; }
}
