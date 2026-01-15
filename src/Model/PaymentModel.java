package Model;

import java.sql.Timestamp;

public class PaymentModel {
    private int id;
    private int billId;
    private float amount;
    private Timestamp paidAt;

    public PaymentModel(int id, int billId, float amount, Timestamp paidAt) {
        this.id = id;
        this.billId = billId;
        this.amount = amount;
        this.paidAt = paidAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }
}
