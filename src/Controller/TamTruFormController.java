package Controller;

import Model.MysqlConnector;
import Model.TamTruModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TamTruFormController {
    @FXML
    private TextField soCCCDText;
    @FXML
    private TextField maTamTruText;
    @FXML
    private TextField lyDoText;
    @FXML
    private DatePicker tuNgayPicker;
    @FXML
    private DatePicker denNgayPicker;

    @FXML
    void confirmOnAction(ActionEvent event) {
        String soCCCD = soCCCDText.getText();
        String maTamTru = maTamTruText.getText();
        String lyDo = lyDoText.getText();
        LocalDate tuNgay = tuNgayPicker.getValue();
        LocalDate denNgay = denNgayPicker.getValue();

        if (ControllerUtil.isEmptyOrNull(soCCCD) || ControllerUtil.isEmptyOrNull(maTamTru)
                || ControllerUtil.isEmptyOrNull(lyDo) || tuNgay == null || denNgay == null) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin!");
            return;
        }

        // Ideally check if CCCD exists. MysqlConnector will throw or handle?
        // Let's assume user enters valid CCCD or we check it.
        // Simplified flow:
        if (ControllerUtil.showConfirmationDialog("Xác nhận", "Khai báo tạm trú?")) {
            TamTruModel tamTru = new TamTruModel(maTamTru, soCCCD, lyDo, tuNgay, denNgay);
            MysqlConnector.getInstance().addTamTruData(tamTru);
            ControllerUtil.showSuccessAlert("Khai báo thành công!");
            close();
        }
    }

    private void close() {
        Stage stage = (Stage) soCCCDText.getScene().getWindow();
        stage.close();
    }
}
