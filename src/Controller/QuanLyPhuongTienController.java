package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class QuanLyPhuongTienController {

    @FXML
    private TextField searchbar;

    @FXML
    private TableView<HoKhauModel> vehicleTableView;
    @FXML
    private TableColumn<HoKhauModel, String> maHoKhauCol;
    @FXML
    private TableColumn<HoKhauModel, Integer> soXeMayCol;
    @FXML
    private TableColumn<HoKhauModel, Integer> soOToCol;
    @FXML
    private TableColumn<HoKhauModel, Integer> soXeDapCol;

    private ObservableList<HoKhauModel> vehicleList;

    @FXML
    public void initialize() {
        vehicleList = FXCollections.observableArrayList();
        initializeSearchbar();
        loadData();

        vehicleTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && vehicleTableView.getSelectionModel().getSelectedItem() != null) {
                HoKhauModel selectedHoKhau = vehicleTableView.getSelectionModel().getSelectedItem();
                openVehicleDetails(selectedHoKhau.getMaHoKhau());
            }
        });
    }

    private void openVehicleDetails(String maHoKhau) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/VehicleDetailView.fxml"));
            Parent root = loader.load();

            VehicleDetailController controller = loader.getController();
            controller.setHoKhau(maHoKhau);

            Stage stage = new Stage();
            stage.setTitle("Chi tiết phương tiện");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtil.showErrorMessage("Lỗi khi mở cửa sổ chi tiết: " + e.getMessage());
        }
    }

    @FXML
    public void deleteAllDataOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa TẤT CẢ dữ liệu phương tiện không?");
        alert.setContentText("Hành động này không thể hoàn tác!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MysqlConnector.getInstance().deleteAllVehicleData();
            loadData();
            ControllerUtil.showSuccessAlert("Đã xóa toàn bộ dữ liệu phương tiện!");
        }
    }

    @FXML
    public void openAddVehicleOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddVehicleView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm phương tiện");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtil.showErrorMessage("Lỗi khi mở cửa sổ thêm phương tiện: " + e.getMessage());
        }
    }

    private void loadData() {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        soXeMayCol.setCellValueFactory(new PropertyValueFactory<>("soXeMay"));
        soOToCol.setCellValueFactory(new PropertyValueFactory<>("soOTo"));
        soXeDapCol.setCellValueFactory(new PropertyValueFactory<>("soXeDap"));

        ObservableList<HoKhauModel> newData = MysqlConnector.getInstance().getVehicleData();
        vehicleList.setAll(newData);
    }

    private void initializeSearchbar() {
        FilteredList<HoKhauModel> filteredData = new FilteredList<>(vehicleList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(HoKhauModel -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchWord = newValue.toLowerCase();

                return HoKhauModel.getMaHoKhau().toLowerCase().contains(searchWord)
                        || String.valueOf(HoKhauModel.getDienTichHo()).contains(searchWord)
                        || String.valueOf(HoKhauModel.getSoXeMay()).contains(searchWord)
                        || String.valueOf(HoKhauModel.getSoOTo()).contains(searchWord)
                        || String.valueOf(HoKhauModel.getSoXeDap()).contains(searchWord);
            });
        });
        SortedList<HoKhauModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(vehicleTableView.comparatorProperty());
        vehicleTableView.setItems(sortedData);
    }

}
