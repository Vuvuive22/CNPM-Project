package Controller;

import Model.MysqlConnector;
import Model.PhuongTienModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddVehicleController {
    @FXML
    private ComboBox<String> maHoKhauCBox;
    @FXML
    private ComboBox<String> chuXeCBox;
    @FXML
    private ComboBox<String> loaiXeCBox;
    @FXML
    private TextField bienSoText;

    @FXML
    public void initialize() {
        // Load Apartment Codes
        maHoKhauCBox.setItems(MysqlConnector.getInstance().getMaHoKhauData());

        // Load Vehicle Types
        loaiXeCBox.setItems(FXCollections.observableArrayList("Xe Máy", "Ô Tô", "Xe Đạp"));

        // Listener for Apartment Selection
        maHoKhauCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                chuXeCBox.setItems(MysqlConnector.getInstance().getResidentsByHoKhau(newValue));
            }
        });
    }

    @FXML
    public void addVehicleOnAction(ActionEvent event) {
        String maHoKhau = maHoKhauCBox.getValue();
        String chuXe = chuXeCBox.getValue();
        String loaiXe = loaiXeCBox.getValue();
        String bienSo = bienSoText.getText();

        if (ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(chuXe)
                || ControllerUtil.isEmptyOrNull(loaiXe)) {
            ControllerUtil.showErrorMessage("Vui lòng điền đầy đủ thông tin (căn hộ, chủ xe, loại xe)!");
            return;
        }

        PhuongTienModel phuongTien = new PhuongTienModel(maHoKhau, loaiXe, bienSo, chuXe);
        boolean success = MysqlConnector.getInstance().addPhuongTien(phuongTien);

        if (success) {
            ControllerUtil.showSuccessAlert("Thêm phương tiện thành công!");
            closeStage();
        } else {
            ControllerUtil.showErrorMessage("Thêm phương tiện thất bại!");
        }
    }

    @FXML
    public void cancelOnAction(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) maHoKhauCBox.getScene().getWindow();
        stage.close();
    }
}
