
package Model;

import java.time.LocalDate;

public class HoKhauModel {
    private String maHoKhau;
    private String diaChi;
    private LocalDate ngayLap;
    private LocalDate ngayChuyenDi;
    private String lyDoChuyen;
    private float dienTichHo;
    private int soXeMay;
    private int soOTo;
    private int soXeDap;
    
    
    public HoKhauModel(String maHoKhau, String diaChi, LocalDate ngayLap, LocalDate ngayChuyenDi, String lyDoChuyen) {
        this.maHoKhau = maHoKhau;
        this.diaChi = diaChi;
        this.ngayLap = ngayLap;
        this.ngayChuyenDi = ngayChuyenDi;
        this.lyDoChuyen = lyDoChuyen;
    }
    
    public HoKhauModel(String maHoKhau, float dienTichHo){
        this.maHoKhau = maHoKhau;
        this.dienTichHo = dienTichHo;
    }
    
    public HoKhauModel(String maHoKhau, int soXeMay, int soOTo, int soXeDap){
        this.maHoKhau = maHoKhau;
        this.soXeMay = soXeMay;
        this.soOTo = soOTo;
        this.soXeDap = soXeDap;
    }

    // Getters and Setters
    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public LocalDate getNgayChuyenDi() {
        return ngayChuyenDi;
    }

    public void setNgayChuyenDi(LocalDate ngayChuyenDi) {
        this.ngayChuyenDi = ngayChuyenDi;
    }

    public String getLyDoChuyen() {
        return lyDoChuyen;
    }

    public void setLyDoChuyen(String lydoChuyen) {
        this.lyDoChuyen = lydoChuyen;
    }
    
    public float getDienTichHo() {
        return dienTichHo;
    }

    public void setDienTichHo(float dienTichHo) {
        this.dienTichHo = dienTichHo;
    }
    
    public int getSoXeMay() {
        return soXeMay;
    }

    public void setSoXeMay(int soXeMay) {
        this.soXeMay = soXeMay;
    }

    public int getSoOTo() {
        return soOTo;
    }

    public void setSoOTo(int soOTo) {
        this.soOTo = soOTo;
    }

    public int getSoXeDap() {
        return soXeDap;
    }

    public void setSoXeDap(int soXeDap) {
        this.soXeDap = soXeDap;
    }
}
