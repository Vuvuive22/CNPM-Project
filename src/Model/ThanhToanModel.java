
package Model;

import java.time.LocalDate;


public class ThanhToanModel {
    private String maHoKhau;
    private float soTienThanhToan;
    private LocalDate ngayThanhToan;
    
    public ThanhToanModel(String maHoKhau, float soTienThanhToan, LocalDate ngayThanhToan) {
        this.maHoKhau = maHoKhau;
        this.soTienThanhToan = soTienThanhToan;
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public float getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(float soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }

    public LocalDate getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDate ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }
}
