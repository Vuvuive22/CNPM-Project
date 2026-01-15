package Model;

public class ToaNhaModel {
    private String MaToaNha;
    private String TenToaNha;
    private String MoTa;

    public ToaNhaModel(String maToaNha, String tenToaNha, String moTa) {
        MaToaNha = maToaNha;
        TenToaNha = tenToaNha;
        MoTa = moTa;
    }

    public String getMaToaNha() {
        return MaToaNha;
    }

    public void setMaToaNha(String maToaNha) {
        MaToaNha = maToaNha;
    }

    public String getTenToaNha() {
        return TenToaNha;
    }

    public void setTenToaNha(String tenToaNha) {
        TenToaNha = tenToaNha;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }
}
