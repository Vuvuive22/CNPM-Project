
package Model;

public class PhiCoDinhModel {  //Model chung cho Phí dịch vụ, phí quản lý, phí gửi xe
    private String maHoKhau;
    private float giaPhi;
    private float tienNopMoiThang;
    private float thang1;
    private float thang2;
    private float thang3;
    private float thang4;
    private float thang5;
    private float thang6;
    private float thang7;
    private float thang8;
    private float thang9;
    private float thang10;
    private float thang11;
    private float thang12;
    private int nam;

    public PhiCoDinhModel(String maHoKhau, float tienNopMoiThang, float thang1, float thang2, float thang3, float thang4, float thang5, float thang6, float thang7, float thang8, float thang9,float thang10, float thang11,float thang12) {
        this.maHoKhau = maHoKhau;
        this.tienNopMoiThang = tienNopMoiThang;
        this.thang1 = thang1;
        this.thang2 = thang2;
        this.thang3 = thang3;
        this.thang4 = thang4;
        this.thang5 = thang5;
        this.thang6 = thang6;
        this.thang7 = thang7;
        this.thang8 = thang8;
        this.thang9 = thang9;
        this.thang10 = thang10;
        this.thang11 = thang11;
        this.thang12 = thang12;
    }
    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public float getGiaPhi() {
        return giaPhi;
    }

    public void setGiaPhi(float giaPhi) {
        this.giaPhi = giaPhi;
    }

    public float getTienNopMoiThang() {
        return tienNopMoiThang;
    }

    public void setTienNopMoiThang(float tienNopMoiThang) {
        this.tienNopMoiThang = tienNopMoiThang;
    }
    
    public float getThang1() {
        return thang1;
    }

    public void setThang1(float thang1) {
        this.thang1 = thang1;
    }

    public float getThang2() {
        return thang2;
    }

    public void setThang2(float thang2) {
        this.thang2 = thang2;
    }

    public float getThang3() {
        return thang3;
    }

    public void setThang3(float thang3) {
        this.thang3 = thang3;
    }

    public float getThang4() {
        return thang4;
    }

    public void setThang4(float thang4) {
        this.thang4 = thang4;
    }

    public float getThang5() {
        return thang5;
    }

    public void setThang5(float thang5) {
        this.thang5 = thang5;
    }

    public float getThang6() {
        return thang6;
    }

    public void setThang6(float thang6) {
        this.thang6 = thang6;
    }

    public float getThang7() {
        return thang7;
    }

    public void setThang7(float thang7) {
        this.thang7 = thang7;
    }

    public float getThang8() {
        return thang8;
    }

    public void setThang8(float thang8) {
        this.thang8 = thang8;
    }

    public float getThang9() {
        return thang9;
    }

    public void setThang9(float thang9) {
        this.thang9 = thang9;
    }

    public float getThang10() {
        return thang10;
    }

    public void setThang10(float thang10) {
        this.thang10 = thang10;
    }

    public float getThang11() {
        return thang11;
    }

    public void setThang11(float thang11) {
        this.thang11 = thang11;
    }

    public float getThang12() {
        return thang12;
    }

    public void setThang12(float thang12) {
        this.thang12 = thang12;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }
}
