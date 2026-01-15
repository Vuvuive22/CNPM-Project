package Model;

import java.sql.Timestamp;

public class PopulationMovementModel {
    private int id;
    private String aptId;
    private String residentId;
    private String type; // move_in, move_out, absent, stay
    private String status; // pending, approved, rejected
    private Timestamp createdAt;
    private String approvedBy;

    public PopulationMovementModel(int id, String aptId, String residentId, String type, String status,
            Timestamp createdAt, String approvedBy) {
        this.id = id;
        this.aptId = aptId;
        this.residentId = residentId;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedBy = approvedBy;
    }

    // Getters and Setters
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

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
