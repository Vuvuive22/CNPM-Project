package Model;

import java.time.LocalDate;

public class HoKhauModel {
    private String maHoKhau;
    private LocalDate ngayLap;
    private float dienTichHo;
    private String chuHo; // Tên chủ hộ
    private int soXeMay;
    private int soOTo;
    private int soXeDap;

    public HoKhauModel(String maHoKhau, LocalDate ngayLap, float dienTichHo, String chuHo) {
        this.maHoKhau = maHoKhau;
        this.ngayLap = ngayLap;
        this.dienTichHo = dienTichHo;
        this.chuHo = chuHo;
    }

    public HoKhauModel(String maHoKhau, float dienTichHo) {
        this.maHoKhau = maHoKhau;
        this.dienTichHo = dienTichHo;
    }

    // Constructor used for fees
    public HoKhauModel(String maHoKhau, int soXeMay, int soOTo, int soXeDap) {
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

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public float getDienTichHo() {
        return dienTichHo;
    }

    public void setDienTichHo(float dienTichHo) {
        this.dienTichHo = dienTichHo;
    }

    public String getChuHo() {
        return chuHo;
    }

    public void setChuHo(String chuHo) {
        this.chuHo = chuHo;
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
