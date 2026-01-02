
package Model;


public class DSPhiDongGop {
    private String tenPhi;
    private float soTienGoiY;
    
    public DSPhiDongGop(String tenPhi, float soTienGoiY) {
        this.tenPhi = tenPhi;
        this.soTienGoiY = soTienGoiY;
    }

    public String getTenPhi() {
        return tenPhi;
    }

    public void setTenPhi(String tenPhi) {
        this.tenPhi = tenPhi;
    }

    public float getSoTienGoiY() {
        return soTienGoiY;
    }

    public void setSoTienGoiY(float soTienGoiY) {
        this.soTienGoiY = soTienGoiY;
    }

}
