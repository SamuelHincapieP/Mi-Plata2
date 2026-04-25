package bankapp.domain;

import bankapp.domain.enums.AccountState;
import bankapp.domain.enums.AccountTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private int id;
    private String accountNumber;
    private double balance;
    private AccountTypeEnum accountType;
    private AccountState accountState;
    private String createDate;
    private int clientId;
    private List<Movement> movements;

    // constructor completo
    public Account(int id, String accountNumber, double balance, AccountTypeEnum accountType,
                   AccountState accountState, String createDate, int clientId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.accountState = accountState;
        this.createDate = createDate;
        this.clientId = clientId;
        this.movements = new ArrayList<>();
    }

    public Account() {
        this.movements = new ArrayList<>();
    }

    // get y set
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public AccountTypeEnum getAccountType() { return accountType; }
    public void setAccountType(AccountTypeEnum accountType) { this.accountType = accountType; }

    public AccountState getAccountState() { return accountState; }
    public void setAccountState(AccountState accountState) { this.accountState = accountState; }

    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public List<Movement> getMovements() { return movements; }
    public void setMovements(List<Movement> movements) { this.movements = movements; }

    @Override
    public String toString() {
        return "==============================\n" +
                "Cuenta #" + accountNumber + "\n" +
                "Tipo    : " + accountType.getDescription() + "\n" +
                "Saldo   : $" + String.format("%.2f", balance) + "\n" +
                "Estado  : " + accountState.getDescription() + "\n" +
                "Creada  : " + createDate + "\n" +
                "==============================";
    }
}