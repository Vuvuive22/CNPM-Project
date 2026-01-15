package Model;

public class VehicleModel {
    private int id;
    private String aptId;
    private String plateNumber;
    private String type;
    private float monthlyFee;
    private String status;

    public VehicleModel(int id, String aptId, String plateNumber, String type, float monthlyFee, String status) {
        this.id = id;
        this.aptId = aptId;
        this.plateNumber = plateNumber;
        this.type = type;
        this.monthlyFee = monthlyFee;
        this.status = status;
    }

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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(float monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
