package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import Model.ToaNhaModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddHoKhauController {

    @FXML
    private TextField maHoKhauTF;

    @FXML
    private ComboBox<String> maToaNhaCB;

    @FXML
    private TextField dienTichTF;

    @FXML
    private DatePicker ngayLapDP;

    @FXML
    public void initialize() {
        // Populate Building ComboBox
        ObservableList<String> maToaNhaList = FXCollections.observableArrayList();
        for (ToaNhaModel tm : MysqlConnector.getInstance().getAllToaNha()) {
            maToaNhaList.add(tm.getMaToaNha());
        }
        maToaNhaCB.setItems(maToaNhaList);
        ngayLapDP.setValue(LocalDate.now());
    }

    @FXML
    void save(ActionEvent event) {
        String maHoKhau = maHoKhauTF.getText();
        String maToaNha = maToaNhaCB.getValue();
        String dienTichStr = dienTichTF.getText();
        LocalDate ngayLap = ngayLapDP.getValue();

        if (ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(dienTichStr) || ngayLap == null
                || ControllerUtil.isEmptyOrNull(maToaNha)) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin!");
            return;
        }

        try {
            float dienTich = Float.parseFloat(dienTichStr);
            if (dienTich <= 0) {
                ControllerUtil.showErrorMessage("Diện tích phải > 0");
                return;
            }

            // Check if exists
            if (MysqlConnector.getInstance().getMaHoKhauData().contains(maHoKhau)) {
                ControllerUtil.showErrorMessage("Mã căn hộ đã tồn tại!");
                return;
            }

            // Owner initially empty
            HoKhauModel newHoKhau = new HoKhauModel(maHoKhau, ngayLap, dienTich, "", maToaNha);
            MysqlConnector.getInstance().addHoKhauData(newHoKhau);

            ControllerUtil.showSuccessAlert("Thêm thành công!");
            closeStage(event);

        } catch (NumberFormatException e) {
            ControllerUtil.showErrorMessage("Diện tích không hợp lệ!");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
