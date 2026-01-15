package Controller;

import Model.DSPhiDongGop;
import Model.MysqlConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPhiDongGopController {

    @FXML
    private TextField tenPhiText;

    @FXML
    private TextField soTienGoiYText;

    @FXML
    public void addFeeOnAction(ActionEvent event) {
        String tenPhi = tenPhiText.getText();
        String soTienStr = soTienGoiYText.getText();

        if (ControllerUtil.isEmptyOrNull(tenPhi) || ControllerUtil.isEmptyOrNull(soTienStr)) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin!");
            return;
        }

        try {
            float soTienGoiY = Float.parseFloat(soTienStr);
            if (soTienGoiY < 0) {
                ControllerUtil.showErrorMessage("Số tiền phải >= 0!");
                return;
            }

            if (ControllerUtil.showConfirmationDialog("Xác nhận", "Thêm khoản phí: " + tenPhi + "?")) {
                DSPhiDongGop fee = new DSPhiDongGop(tenPhi, soTienGoiY);
                MysqlConnector.getInstance().addDSPhiDongGopData(fee);
                ControllerUtil.showSuccessAlert("Thêm thành công!");
                closeStage();
            }
        } catch (NumberFormatException e) {
            ControllerUtil.showErrorMessage("Số tiền không hợp lệ!");
        }
    }

    @FXML
    public void cancelOnAction(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) tenPhiText.getScene().getWindow();
        stage.close();
    }
}
