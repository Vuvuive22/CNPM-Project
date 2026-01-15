package Controller;

import Model.MysqlConnector;
import Model.PhuongTienModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class VehicleDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<PhuongTienModel> vehicleTableView;
    @FXML
    private TableColumn<PhuongTienModel, String> loaiXeCol;
    @FXML
    private TableColumn<PhuongTienModel, String> bienSoCol;
    @FXML
    private TableColumn<PhuongTienModel, String> chuXeCol;

    public void setHoKhau(String maHoKhau) {
        titleLabel.setText("Chi tiết phương tiện - " + maHoKhau);
        loadData(maHoKhau);
    }

    private void loadData(String maHoKhau) {
        loaiXeCol.setCellValueFactory(new PropertyValueFactory<>("loaiXe"));
        bienSoCol.setCellValueFactory(new PropertyValueFactory<>("bienSo"));
        chuXeCol.setCellValueFactory(new PropertyValueFactory<>("chuXe"));

        ObservableList<PhuongTienModel> list = MysqlConnector.getInstance().getVehiclesByHoKhau(maHoKhau);
        vehicleTableView.setItems(list);
    }

    @FXML
    public void deleteVehicleOnAction(ActionEvent event) {
        PhuongTienModel selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            ControllerUtil.showErrorMessage("Vui lòng chọn phương tiện cần xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa phương tiện này không?");
        alert.setContentText("Biển số: " + selectedVehicle.getBienSo());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = MysqlConnector.getInstance().deleteVehicle(selectedVehicle);
            if (success) {
                ControllerUtil.showSuccessAlert("Xóa phương tiện thành công!");
                loadData(selectedVehicle.getMaHoKhau());
            } else {
                ControllerUtil.showErrorMessage("Xóa thất bại!");
            }
        }
    }

    @FXML
    public void closeOnAction(ActionEvent event) {
        Stage stage = (Stage) vehicleTableView.getScene().getWindow();
        stage.close();
    }
}
