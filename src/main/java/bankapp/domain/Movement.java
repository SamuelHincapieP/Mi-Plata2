package bankapp.domain;

import bankapp.domain.enums.MovementTypeEnum;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movement {

    private int id;
    private MovementTypeEnum movementType;
    private double amount;
    private double balanceAfter;
    private String description;
    private String date;
    private int accountId;

    public Movement(int id, MovementTypeEnum movementType, double amount,
                    double balanceAfter, String description, int accountId) {
        this.id = id;
        this.movementType = movementType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.accountId = accountId;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Movement() {}

    // get y set
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public MovementTypeEnum getMovementType() { return movementType; }
    public void setMovementType(MovementTypeEnum movementType) { this.movementType = movementType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    @Override
    public String toString() {
        return "  [" + date + "] " + movementType.getDescription() +
                " | $" + String.format("%.2f", amount) +
                " | Saldo: $" + String.format("%.2f", balanceAfter) +
                " | " + description;
    }
}
