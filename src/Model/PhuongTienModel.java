package Model;

public class PhuongTienModel {
    private int maPhuongTien;
    private String maHoKhau;
    private String loaiXe;
    private String bienSo;
    private String chuXe;

    public PhuongTienModel(int maPhuongTien, String maHoKhau, String loaiXe, String bienSo, String chuXe) {
        this.maPhuongTien = maPhuongTien;
        this.maHoKhau = maHoKhau;
        this.loaiXe = loaiXe;
        this.bienSo = bienSo;
        this.chuXe = chuXe;
    }

    public PhuongTienModel(String maHoKhau, String loaiXe, String bienSo, String chuXe) {
        this.maHoKhau = maHoKhau;
        this.loaiXe = loaiXe;
        this.bienSo = bienSo;
        this.chuXe = chuXe;
    }

    public int getMaPhuongTien() {
        return maPhuongTien;
    }

    public void setMaPhuongTien(int maPhuongTien) {
        this.maPhuongTien = maPhuongTien;
    }

    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getChuXe() {
        return chuXe;
    }

    public void setChuXe(String chuXe) {
        this.chuXe = chuXe;
    }
}
