/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;


public class NhanKhauModel {
    private String maHoKhau;
    private String hoTen;
    private int tuoi;
    private String gioiTinh;
    private String CCCD;
    private String SoDT;
    private String QuanHe;
    private boolean isTamTru;
    private boolean isTamVang;
    
    public NhanKhauModel(String maHoKhau, String hoTen, int tuoi, String gioiTinh, String CCCD, String SoDT, String QuanHe, boolean IsTamVang, boolean IsTamTru) {
        this.maHoKhau = maHoKhau;
        this.hoTen = hoTen;
        this.tuoi = tuoi;
        this.gioiTinh = gioiTinh;
        this.CCCD = CCCD;
        this.SoDT = SoDT;
        this.QuanHe = QuanHe;
        this.isTamVang = IsTamVang;
        this.isTamTru = IsTamTru;
    }
    
    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getTuoi() {
        return tuoi;
    }

    public void setTuoi(int tuoi) {
        this.tuoi = tuoi;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getSoDT() {
        return SoDT;
    }

    public void setSoDT(String SoDT) {
        this.SoDT = SoDT;
    }

    public String getQuanHe() {
        return QuanHe;
    }

    public void setQuanHe(String QuanHe) {
        this.QuanHe = QuanHe;
    }
    
    public boolean isTamTru() {
        return isTamTru;
    }

    
    public void setTamTru(boolean tamTru) {
        this.isTamTru = tamTru;
    }

    
    public boolean isTamVang() {
        return isTamVang;
    }

    
    public void setTamVang(boolean tamVang) {
        this.isTamVang = tamVang;
    }
    
}
