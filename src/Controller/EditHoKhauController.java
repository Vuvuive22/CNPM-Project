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

public class EditHoKhauController {

    @FXML
    private TextField maHoKhauTF;

    @FXML
    private ComboBox<String> maToaNhaCB;

    @FXML
    private TextField dienTichTF;

    @FXML
    private DatePicker ngayLapDP;

    private HoKhauModel hoKhau;

    @FXML
    public void initialize() {
        // Populate Building ComboBox
        ObservableList<String> maToaNhaList = FXCollections.observableArrayList();
        for (ToaNhaModel tm : MysqlConnector.getInstance().getAllToaNha()) {
            maToaNhaList.add(tm.getMaToaNha());
        }
        maToaNhaCB.setItems(maToaNhaList);
    }

    public void setHoKhau(HoKhauModel hoKhau) {
        this.hoKhau = hoKhau;
        maHoKhauTF.setText(hoKhau.getMaHoKhau());
        dienTichTF.setText(String.valueOf(hoKhau.getDienTichHo()));
        ngayLapDP.setValue(hoKhau.getNgayLap());
        maToaNhaCB.setValue(hoKhau.getMaToaNha());
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

            // Prepare updated model
            // Maintain current owner
            String currentOwner = hoKhau.getChuHo();
            HoKhauModel updatedHoKhau = new HoKhauModel(maHoKhau, ngayLap, dienTich, currentOwner, maToaNha);

            MysqlConnector.getInstance().updateHoKhauData(updatedHoKhau);

            ControllerUtil.showSuccessAlert("Cập nhật thành công!");
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
