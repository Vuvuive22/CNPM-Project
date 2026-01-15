package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UpdateVehicleController {

    @FXML
    private ComboBox<String> maHoKhauCBox;

    @FXML
    private TextField soXeMayText;

    @FXML
    private TextField soOToText;

    @FXML
    private TextField soXeDapText;

    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    @FXML
    public void initialize() {
        // Load list of MaHoKhau
        ObservableList<HoKhauModel> vehicleList = MysqlConnector.getInstance().getVehicleData();
        List<String> maHoKhauList = new ArrayList<>();
        for (HoKhauModel hoKhau : vehicleList) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        maHoKhauCBox.setItems(FXCollections.observableArrayList(maHoKhauList));

        // Auto-fill when selection changes
        maHoKhauCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                // Find selected model
                for (HoKhauModel hk : vehicleList) {
                    if (hk.getMaHoKhau().equals(newValue)) {
                        soXeMayText.setText(String.valueOf(hk.getSoXeMay()));
                        soOToText.setText(String.valueOf(hk.getSoOTo()));
                        soXeDapText.setText(String.valueOf(hk.getSoXeDap()));
                        break;
                    }
                }
            }
        });
    }

    @FXML
    public void updateOnAction(ActionEvent event) {
        String maHoKhau = maHoKhauCBox.getValue();
        if (ControllerUtil.isEmptyOrNull(maHoKhau)) {
            ControllerUtil.showErrorMessage("Vui lòng chọn hộ khẩu!");
            return;
        }

        try {
            int soXeMay = Integer.parseInt(soXeMayText.getText());
            int soOTo = Integer.parseInt(soOToText.getText());
            int soXeDap = Integer.parseInt(soXeDapText.getText());

            if (soXeMay < 0 || soOTo < 0 || soXeDap < 0) {
                ControllerUtil.showErrorMessage("Số lượng xe không được âm!");
                return;
            }

            if (ControllerUtil.showConfirmationDialog("Xác nhận", "Cập nhật phương tiện cho hộ " + maHoKhau + "?")) {
                MysqlConnector.getInstance().changeVehicleData(maHoKhau, soXeMay, soOTo, soXeDap,
                        year > 0 ? year : java.time.LocalDate.now().getYear());
                ControllerUtil.showSuccessAlert("Cập nhật thành công!");
                closeStage();
            }

        } catch (NumberFormatException e) {
            ControllerUtil.showErrorMessage("Vui lòng nhập số nguyên hợp lệ!");
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
