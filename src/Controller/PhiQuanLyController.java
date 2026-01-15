
package Controller;

import Model.MysqlConnector;
import Model.PhiCoDinhModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.Year;

import javafx.event.ActionEvent;

public class PhiQuanLyController {

    @FXML
    private TextField changeFeeText;

    @FXML
    private Label feeLabel;

    @FXML
    private TableColumn<PhiCoDinhModel, String> maHoKhauCol;

    @FXML
    private TableColumn<PhiCoDinhModel, Float> phiMoiThangCol;

    @FXML
    private TableView<PhiCoDinhModel> feeTableView;

    @FXML
    private TextField searchbar;

    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang10Col;

    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang11Col;

    @FXML
    private TableColumn<PhiCoDinhModel, Float> thang12Col;

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
    private ComboBox<Integer> yearCBox;

    private ObservableList<PhiCoDinhModel> feeList; // Danh sách phí

    private final String tenPhi = "PhiQuanLy";

    @FXML
    public void initialize() {
        loadData(Year.now().getValue());
        initializeSearchbar();
        yearCBox.getItems().addAll(2023, 2024, 2025, 2026);
        yearCBox.setValue(Year.now().getValue());
        feeLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 22;");
    }

    @FXML
    public void selectYearOnAction(ActionEvent event) {
        Integer year = yearCBox.getValue();
        loadData(year);
        feeTableView.refresh();
    }

    @FXML
    public void changeFeeOnAction(ActionEvent event) {
        float newFee = Float.parseFloat(changeFeeText.getText());
        if (ControllerUtil.isEmptyOrNull(Float.toString(newFee))) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường!");
            return;
        }
        if (newFee <= 0) {
            ControllerUtil.showErrorMessage("Phí quản lý mới không hợp lệ, vui lòng nhập lại!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thay đổi phí quản lý chung cư",
                "Bạn có chắc chắn muốn thay đổi phí quản lý chung cư không ?");
        if (confirmed) {
            MysqlConnector.getInstance().changeFeeData(tenPhi, newFee, Year.now().getValue());
            ControllerUtil.showSuccessAlert("Thay đổi phí quản lý chung cư thành công!");
            feeList = MysqlConnector.getInstance().getFeeData(tenPhi, yearCBox.getValue());
            feeTableView.setItems(feeList);
            feeTableView.refresh();
            feeLabel.setText("Phí quản lý chung cư hiện tại là: " + newFee + " /đồng/m2/tháng");
            changeFeeText.clear();
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

        float fee = MysqlConnector.getInstance().getGiaPhiData(tenPhi, year);
        feeLabel.setText("Phí quản lý chung cư hiện tại là: " + fee + " /đồng/m2/tháng");
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
                        || String.valueOf(PhiCoDinhModel.getTienNopMoiThang()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang1()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang2()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang3()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang4()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang5()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang6()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang7()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang8()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang9()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang10()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang11()).contains(searchWord)
                        || String.valueOf(PhiCoDinhModel.getThang12()).contains(searchWord);
            });
        });
        SortedList<PhiCoDinhModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(feeTableView.comparatorProperty());
        feeTableView.setItems(sortedData);
    }
}