package Controller;

import Model.MysqlConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateParkingFeeController {

    @FXML
    private TextField giaXeMayText;

    @FXML
    private TextField giaOToText;

    @FXML
    private TextField giaXeDapText;

    private int year;

    public void setYear(int year) {
        this.year = year;
        loadCurrentPrices();
    }

    private void loadCurrentPrices() {
        float giaXM = MysqlConnector.getInstance().getFeePerVehicleData("GiaXeMay", year);
        float giaOTo = MysqlConnector.getInstance().getFeePerVehicleData("GiaOTo", year);
        float giaXD = MysqlConnector.getInstance().getFeePerVehicleData("GiaXeDap", year);

        giaXeMayText.setText(String.valueOf(giaXM));
        giaOToText.setText(String.valueOf(giaOTo));
        giaXeDapText.setText(String.valueOf(giaXD));
    }

    @FXML
    public void updateOnAction(ActionEvent event) {
        try {
            float giaXeMay = Float.parseFloat(giaXeMayText.getText());
            float giaOTo = Float.parseFloat(giaOToText.getText());
            float giaXeDap = Float.parseFloat(giaXeDapText.getText());

            if (giaXeMay <= 0 || giaOTo <= 0 || giaXeDap <= 0) {
                ControllerUtil.showErrorMessage("Giá phí phải lớn hơn 0!");
                return;
            }

            if (ControllerUtil.showConfirmationDialog("Xác nhận", "Thay đổi giá gửi xe?")) {
                MysqlConnector.getInstance().changeFeePerVehicleData(giaXeMay, giaOTo, giaXeDap, year);
                ControllerUtil.showSuccessAlert("Cập nhật thành công!");
                closeStage();
            }

        } catch (NumberFormatException e) {
            ControllerUtil.showErrorMessage("Vui lòng nhập số hợp lệ!");
        }
    }

    @FXML
    public void cancelOnAction(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) giaXeMayText.getScene().getWindow();
        stage.close();
    }
}
