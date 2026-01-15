package Controller;

import Model.MysqlConnector;
import Model.TamVangModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TamVangFormController {
    @FXML
    private TextField soCCCDText;
    @FXML
    private TextField maTamVangText;
    @FXML
    private TextField noiTamTruText;
    @FXML
    private DatePicker tuNgayPicker;
    @FXML
    private DatePicker denNgayPicker;

    @FXML
    void confirmOnAction(ActionEvent event) {
        String soCCCD = soCCCDText.getText();
        String maTamVang = maTamVangText.getText();
        String noiTamTru = noiTamTruText.getText();
        LocalDate tuNgay = tuNgayPicker.getValue();
        LocalDate denNgay = denNgayPicker.getValue();

        if (ControllerUtil.isEmptyOrNull(soCCCD) || ControllerUtil.isEmptyOrNull(maTamVang)
                || ControllerUtil.isEmptyOrNull(noiTamTru) || tuNgay == null || denNgay == null) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin!");
            return;
        }

        if (ControllerUtil.showConfirmationDialog("Xác nhận", "Khai báo tạm vắng?")) {
            TamVangModel tamVang = new TamVangModel(maTamVang, soCCCD, noiTamTru, tuNgay, denNgay);
            MysqlConnector.getInstance().addTamVangData(tamVang);
            ControllerUtil.showSuccessAlert("Khai báo thành công!");
            close();
        }
    }

    private void close() {
        Stage stage = (Stage) soCCCDText.getScene().getWindow();
        stage.close();
    }
}
