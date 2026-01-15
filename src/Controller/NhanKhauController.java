package Controller;

import Model.MysqlConnector;
import Model.NhanKhauModel;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NhanKhauController {

    @FXML
    private TableView<NhanKhauModel> residentTableView;
    @FXML
    private TableColumn<NhanKhauModel, String> maHKCol;
    @FXML
    private TableColumn<NhanKhauModel, String> hoTenCol;
    @FXML
    private TableColumn<NhanKhauModel, Integer> tuoiCol;
    @FXML
    private TableColumn<NhanKhauModel, String> gioiTinhCol;
    @FXML
    private TableColumn<NhanKhauModel, String> soCCCDCol;
    @FXML
    private TableColumn<NhanKhauModel, String> soDTCol;
    @FXML
    private TableColumn<NhanKhauModel, String> quanHeCol;
    @FXML
    private TableColumn<NhanKhauModel, String> tamVangCol;
    @FXML
    private TableColumn<NhanKhauModel, String> tamTruCol;
    @FXML
    private TextField searchbar;

    private ObservableList<NhanKhauModel> list;

    @FXML
    public void initialize() {
        loadData();
        initializeSearchbar();
    }

    private void loadData() {
        maHKCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        hoTenCol.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        tuoiCol.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
        gioiTinhCol.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        soCCCDCol.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
        soDTCol.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        quanHeCol.setCellValueFactory(new PropertyValueFactory<>("quanHe"));
        tamVangCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().isTamVang() ? "Có" : "Không"));
        tamTruCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().isTamTru() ? "Có" : "Không"));

        list = MysqlConnector.getInstance().getNhanKhauData();
        residentTableView.setItems(list);
    }

    @FXML
    public void popupAddNhanKhau(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddNhanKhau.fxml"));
            Parent root = loader.load();

            // Reload data if added
            AddNhanKhauController controller = loader.getController();
            controller.isAddedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    refreshData();
                }
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thêm Nhân Khẩu");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Just in case listener didn't fire or simplified flow
            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
            ControllerUtil.showErrorMessage("Lỗi tải form thêm nhân khẩu: " + e.getMessage());
        }
    }

    @FXML
    public void popupUpdateNhanKhau(ActionEvent event) {
        NhanKhauModel selected = residentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ControllerUtil.showErrorMessage("Vui lòng chọn nhân khẩu để cập nhật!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateNhanKhau.fxml"));
            Parent root = loader.load();

            UpdateNhanKhauController controller = loader.getController();
            controller.setNhanKhau(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Cập nhật Nhân Khẩu");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void popupKhaiBaoTamTru(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TamTruForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Khai báo Tạm Trú");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void popupKhaiBaoTamVang(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TamVangForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Khai báo Tạm Vắng");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showTamTruList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TamTruListView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Danh sách Tạm Trú");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showTamVangList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TamVangListView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Danh sách Tạm Vắng");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteNhanKhauOnAction(ActionEvent event) {
        NhanKhauModel nhanKhau = residentTableView.getSelectionModel().getSelectedItem();
        if (nhanKhau == null) {
            ControllerUtil.showErrorMessage("Vui lòng chọn nhân khẩu muốn xóa!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa nhân khẩu",
                "Bạn có chắc chắn muốn xóa nhân khẩu không ?");
        if (confirmed) {
            MysqlConnector.getInstance().deleteNhanKhauData(nhanKhau.getCCCD());
            refreshData();
            ControllerUtil.showSuccessAlert("Xóa nhân khẩu thành công!");
        }
    }

    private void refreshData() {
        list = MysqlConnector.getInstance().getNhanKhauData();
        residentTableView.setItems(list);
        initializeSearchbar(); // Re-apply filter
    }

    private void initializeSearchbar() {
        FilteredList<NhanKhauModel> filteredData = new FilteredList<>(list, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(NhanKhauModel -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchWord = newValue.toLowerCase();

                if (NhanKhauModel.getMaHoKhau().toLowerCase().contains(searchWord)) {
                    return true;
                } else if (NhanKhauModel.getHoTen().toLowerCase().contains(searchWord)) {
                    return true;
                } else if (String.valueOf(NhanKhauModel.getTuoi()).contains(searchWord)) {
                    return true;
                } else if (NhanKhauModel.getGioiTinh().toLowerCase().contains(searchWord)) {
                    return true;
                } else if (NhanKhauModel.getCCCD().toLowerCase().contains(searchWord)) {
                    return true;
                } else if (NhanKhauModel.getSoDT().toLowerCase().contains(searchWord)) {
                    return true;
                } else if (NhanKhauModel.getQuanHe().toLowerCase().contains(searchWord)) {
                    return true;
                } else if ((NhanKhauModel.isTamVang() ? "Có" : "Không").toLowerCase().contains(searchWord)) {
                    return true;
                } else if ((NhanKhauModel.isTamTru() ? "Có" : "Không").toLowerCase().contains(searchWord)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<NhanKhauModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(residentTableView.comparatorProperty());
        residentTableView.setItems(sortedData);
    }
}
