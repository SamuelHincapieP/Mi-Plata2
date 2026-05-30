package bankapp.domain;

import bankapp.domain.enums.AccountState;
import bankapp.domain.enums.AccountTypeEnum;

public class Account {

    private int cuentaID;
    private String numeroCuenta;
    private double saldo;
    private String estado;
    private String fechaCreacion;
    private int usuarioID;
    private int tipoCuentaID;

    public Account() {}

    public Account(int cuentaID, String numeroCuenta, double saldo, String estado,
                   String fechaCreacion, int usuarioID, int tipoCuentaID) {
        this.cuentaID = cuentaID;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioID = usuarioID;
        this.tipoCuentaID = tipoCuentaID;
    }

    public int getCuentaID() { return cuentaID; }
    public void setCuentaID(int cuentaID) { this.cuentaID = cuentaID; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public int getUsuarioID() { return usuarioID; }
    public void setUsuarioID(int usuarioID) { this.usuarioID = usuarioID; }

    public int getTipoCuentaID() { return tipoCuentaID; }
    public void setTipoCuentaID(int tipoCuentaID) { this.tipoCuentaID = tipoCuentaID; }

    @Override
    public String toString() {
        return "==============================\n" +
                "Cuenta #  : " + numeroCuenta + "\n" +
                "Saldo     : $" + String.format("%.2f", saldo) + "\n" +
                "Estado    : " + estado + "\n" +
                "Creada    : " + fechaCreacion + "\n" +
                "==============================";
    }
}
