package Model;

import java.time.LocalDate;

public class TamVangModel {
    private String maTamVang;
    private String soCCCD;
    private String noiTamTru;
    private LocalDate tuNgay;
    private LocalDate denNgay;

    
    
    public TamVangModel(String maTamVang, String soCCCD, String noiTamTru, LocalDate tuNgay, LocalDate denNgay) {
        this.maTamVang = maTamVang;
        this.soCCCD = soCCCD;
        this.noiTamTru = noiTamTru;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    
    public String getMaTamVang() {
        return maTamVang;
    }

    public void setMaTamVang(String maTamVang) {
        this.maTamVang = maTamVang;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getNoiTamTru() {
        return noiTamTru;
    }

    public void setNoiTamTru(String noiTamTru) {
        this.noiTamTru = noiTamTru;
    }

    public LocalDate getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(LocalDate tuNgay) {
        this.tuNgay = tuNgay;
    }

    public LocalDate getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(LocalDate denNgay) {
        this.denNgay = denNgay;
    }
   
}
