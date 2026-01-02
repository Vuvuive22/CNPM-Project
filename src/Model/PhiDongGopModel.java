
package Model;

import java.time.LocalDate;

public class PhiDongGopModel {
    private String maHoKhau;
    private String tenPhi;
    private float soTien;
    private LocalDate ngayDongGop;

    public PhiDongGopModel(String maHoKhau, String tenPhi, float soTien, LocalDate ngayDongGop) {
        this.maHoKhau = maHoKhau;
        this.tenPhi = tenPhi;
        this.soTien = soTien;
        this.ngayDongGop = ngayDongGop;
    }

    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public String getTenPhi() {
        return tenPhi;
    }

    public void setTenPhi(String tenPhi) {
        this.tenPhi = tenPhi;
    }

    public float getSoTien() {
        return soTien;
    }

    public void setSoTien(float soTien) {
        this.soTien = soTien;
    }

    public LocalDate getNgayDongGop() {
        return ngayDongGop;
    }

    public void setNgayDongGop(LocalDate ngayDongGop) {
        this.ngayDongGop = ngayDongGop;
    }
}
