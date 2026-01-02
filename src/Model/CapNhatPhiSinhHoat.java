
package Model;

import java.time.LocalDate;


public class CapNhatPhiSinhHoat {
    private String maHoKhau;
    private float tienDien;
    private float tienNuoc;
    private float tienInternet;
    private LocalDate ngayCapNhat;

    public CapNhatPhiSinhHoat(String maHoKhau, float tienDien, float tienNuoc, float tienInternet) {
        this.maHoKhau = maHoKhau;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tienInternet = tienInternet;
    }

    // Thêm getter và setter cho các trường

    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public float getTienDien() {
        return tienDien;
    }

    public void setTienDien(float tienDien) {
        this.tienDien = tienDien;
    }

    public float getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(float tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public float getTienInternet() {
        return tienInternet;
    }

    public void setTienInternet(float tienInternet) {
        this.tienInternet = tienInternet;
    }

    public LocalDate getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDate ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
}
