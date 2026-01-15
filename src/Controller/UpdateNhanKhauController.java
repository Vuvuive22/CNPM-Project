package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import Model.NhanKhauModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UpdateNhanKhauController {

    @FXML
    private TextField hoTenText;
    @FXML
    private TextField tuoiText;
    @FXML
    private TextField gioiTinhText;
    @FXML
    private TextField soCCCDText;
    @FXML
    private TextField soDTText;
    @FXML
    private ComboBox<String> maHoKhauCBox;
    @FXML
    private TextField quanHeText;
    @FXML
    private CheckBox isChuHoCBox;

    private NhanKhauModel currentNhanKhau;

    public void setNhanKhau(NhanKhauModel nhanKhau) {
        this.currentNhanKhau = nhanKhau;
        fillData();
    }

    @FXML
    public void initialize() {
        // Load Ho Khau list
        ObservableList<HoKhauModel> listHK = MysqlConnector.getInstance().getHoKhauData();
        List<String> maHoKhauList = new ArrayList<>();
        for (HoKhauModel hoKhau : listHK) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        maHoKhauCBox.setItems(FXCollections.observableArrayList(maHoKhauList));

        // Logic for Chu Ho checkbox (Same as Add)
        quanHeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isChuHoCBox.isSelected() && !newValue.equals("Chủ Hộ")) {
                isChuHoCBox.setSelected(false);
            }
        });
        isChuHoCBox.setOnAction(event -> {
            if (isChuHoCBox.isSelected()) {
                quanHeText.setText("Chủ Hộ");
            } else if ("Chủ Hộ".equals(quanHeText.getText())) {
                quanHeText.clear();
            }
        });
    }

    private void fillData() {
        if (currentNhanKhau != null) {
            hoTenText.setText(currentNhanKhau.getHoTen());
            tuoiText.setText(String.valueOf(currentNhanKhau.getTuoi()));
            gioiTinhText.setText(currentNhanKhau.getGioiTinh());
            soCCCDText.setText(currentNhanKhau.getCCCD());
            soDTText.setText(currentNhanKhau.getSoDT());
            maHoKhauCBox.setValue(currentNhanKhau.getMaHoKhau());
            quanHeText.setText(currentNhanKhau.getQuanHe());
            isChuHoCBox.setSelected("Chủ Hộ".equals(currentNhanKhau.getQuanHe()));
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        String maHoKhau = maHoKhauCBox.getValue();
        String hoTen = hoTenText.getText();

        // ... Get other fields ...
        // Validate ...

        if (ControllerUtil.showConfirmationDialog("Xác nhận", "Cập nhật nhân khẩu này?")) {
            NhanKhauModel updated = new NhanKhauModel(
                    maHoKhau,
                    hoTen,
                    Integer.parseInt(tuoiText.getText()),
                    gioiTinhText.getText(),
                    soCCCDText.getText(),
                    soDTText.getText(),
                    quanHeText.getText(),
                    currentNhanKhau.isTamVang(),
                    currentNhanKhau.isTamTru());
            MysqlConnector.getInstance().updateNhanKhauData(updated);
            ControllerUtil.showSuccessAlert("Cập nhật thành công!");
            close();
        }
    }

    private void close() {
        Stage stage = (Stage) hoTenText.getScene().getWindow();
        stage.close();
    }
}
