package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import Model.PhiCoDinhModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;

public class PhiGuiXeController {

    @FXML
    private TableView<PhiCoDinhModel> feeTableView;
    @FXML
    private TableColumn<PhiCoDinhModel, String> maHoKhauCol;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> phiMoiThangCol;

    // Month Columns
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang1Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang2Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang3Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang4Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang5Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang6Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang7Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang8Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang9Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang10Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang11Col;
    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang12Col;

    @FXML
    private ComboBox<Integer> yearCBox;
    @FXML
    private TextField searchbar;

    @FXML
    private TableView<HoKhauModel> vehicleTableView;
    @FXML
    private TableColumn<HoKhauModel, String> maHoKhauCol1;
    @FXML
    private TableColumn<HoKhauModel, Integer> soXeMayCol;
    @FXML
    private TableColumn<HoKhauModel, Integer> soOToCol;
    @FXML
    private TableColumn<HoKhauModel, Integer> soXeDapCol;

    // Current Price Labels
    @FXML
    private Label giaXeMayLabel;
    @FXML
    private Label giaOToLabel;
    @FXML
    private Label giaXeDapLabel;

    private ObservableList<PhiCoDinhModel> feeList;
    private ObservableList<HoKhauModel> vehicleList;
    private final String tenPhi = "PhiGuiXe";

    @FXML
    public void initialize() {
        yearCBox.getItems().addAll(2023, 2024, 2025, 2026);
        yearCBox.setValue(Year.now().getValue());

        loadData(yearCBox.getValue());
        initializeSearchbar();
    }

    @FXML
    public void selectYearOnAction(ActionEvent event) {
        loadData(yearCBox.getValue());
    }

    @FXML
    public void openUpdateVehicleOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateVehicleView.fxml"));
            Parent root = loader.load();

            UpdateVehicleController controller = loader.getController();
            controller.setYear(yearCBox.getValue());

            Stage stage = new Stage();
            stage.setTitle("Cập nhật xe");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData(yearCBox.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openUpdateFeeOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UpdateParkingFeeView.fxml"));
            Parent root = loader.load();

            UpdateParkingFeeController controller = loader.getController();
            controller.setYear(yearCBox.getValue());

            Stage stage = new Stage();
            stage.setTitle("Cập nhật giá gửi xe");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData(yearCBox.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData(int year) {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        phiMoiThangCol.setCellValueFactory(new PropertyValueFactory<>("tienNopMoiThang"));
        thang1Col.setCellValueFactory(new PropertyValueFactory<>("thang1"));
        thang2Col.setCellValueFactory(new PropertyValueFactory<>("thang2"));
        thang3Col.setCellValueFactory(new PropertyValueFactory<>("thang3"));
        thang4Col.setCellValueFactory(new PropertyValueFactory<>("thang4"));
        thang5Col.setCellValueFactory(new PropertyValueFactory<>("thang5"));
        thang6Col.setCellValueFactory(new PropertyValueFactory<>("thang6"));
        thang7Col.setCellValueFactory(new PropertyValueFactory<>("thang7"));
        thang8Col.setCellValueFactory(new PropertyValueFactory<>("thang8"));
        thang9Col.setCellValueFactory(new PropertyValueFactory<>("thang9"));
        thang10Col.setCellValueFactory(new PropertyValueFactory<>("thang10"));
        thang11Col.setCellValueFactory(new PropertyValueFactory<>("thang11"));
        thang12Col.setCellValueFactory(new PropertyValueFactory<>("thang12"));

        feeList = MysqlConnector.getInstance().getFeeData(tenPhi, year);
        feeTableView.setItems(feeList);

        maHoKhauCol1.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        soXeMayCol.setCellValueFactory(new PropertyValueFactory<>("soXeMay"));
        soOToCol.setCellValueFactory(new PropertyValueFactory<>("soOTo"));
        soXeDapCol.setCellValueFactory(new PropertyValueFactory<>("soXeDap"));
        vehicleList = MysqlConnector.getInstance().getVehicleData();
        vehicleTableView.setItems(vehicleList);

        float fee = MysqlConnector.getInstance().getFeePerVehicleData("GiaXeMay", year);
        giaXeMayLabel.setText(String.format("%,.0f", fee));
        fee = MysqlConnector.getInstance().getFeePerVehicleData("GiaOTo", year);
        giaOToLabel.setText(String.format("%,.0f", fee));
        fee = MysqlConnector.getInstance().getFeePerVehicleData("GiaXeDap", year);
        giaXeDapLabel.setText(String.format("%,.0f", fee));
    }

    private void initializeSearchbar() {
        FilteredList<PhiCoDinhModel> filteredData = new FilteredList<>(feeList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PhiCoDinhModel -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchWord = newValue.toLowerCase();

                return PhiCoDinhModel.getMaHoKhau().toLowerCase().contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getTienNopMoiThang()).contains(searchWord);
            });
        });
        SortedList<PhiCoDinhModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(feeTableView.comparatorProperty());
        feeTableView.setItems(sortedData);

        FilteredList<HoKhauModel> filteredData1 = new FilteredList<>(vehicleList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData1.setPredicate(HoKhauModel -> {
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
        SortedList<HoKhauModel> sortedData1 = new SortedList<>(filteredData1);
        sortedData1.comparatorProperty().bind(vehicleTableView.comparatorProperty());
        vehicleTableView.setItems(sortedData1);
    }

}