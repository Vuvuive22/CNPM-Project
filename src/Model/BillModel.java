package Model;

import java.sql.Timestamp;

public class BillModel {
    private int id;
    private String aptId;
    private String period;
    private float serviceFee;
    private float vehicleFee;
    private float preDebt;
    private float total;
    private boolean paid;
    private Timestamp paidAt;
    private String detailsJson;

    public BillModel(int id, String aptId, String period, float serviceFee, float vehicleFee, float preDebt,
            float total, boolean paid, Timestamp paidAt, String detailsJson) {
        this.id = id;
        this.aptId = aptId;
        this.period = period;
        this.serviceFee = serviceFee;
        this.vehicleFee = vehicleFee;
        this.preDebt = preDebt;
        this.total = total;
        this.paid = paid;
        this.paidAt = paidAt;
        this.detailsJson = detailsJson;
    }

    // Getters Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAptId() {
        return aptId;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public float getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(float serviceFee) {
        this.serviceFee = serviceFee;
    }

    public float getVehicleFee() {
        return vehicleFee;
    }

    public void setVehicleFee(float vehicleFee) {
        this.vehicleFee = vehicleFee;
    }

    public float getPreDebt() {
        return preDebt;
    }

    public void setPreDebt(float preDebt) {
        this.preDebt = preDebt;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }

    public String getDetailsJson() {
        return detailsJson;
    }

    public void setDetailsJson(String detailsJson) {
        this.detailsJson = detailsJson;
    }
}
