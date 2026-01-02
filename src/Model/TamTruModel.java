package Model;

import java.time.LocalDate;

public class TamTruModel {
    private String maTamTru;
    private String soCCCD;
    private String lyDo;
    private LocalDate tuNgay;
    private LocalDate denNgay;

    
    public TamTruModel(String maTamTru, String soCCCD, String lyDo, LocalDate tuNgay, LocalDate denNgay) {
        this.maTamTru = maTamTru;
        this.soCCCD = soCCCD;
        this.lyDo = lyDo;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    public String getMaTamTru() {
        return maTamTru;
    }

    public void setMaTamTru(String maTamTru) {
        this.maTamTru = maTamTru;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
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
