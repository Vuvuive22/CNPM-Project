package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HoKhauController {

    @FXML
    private TextField addMaHoKhauText;
    @FXML
    private TextField addDienTichText;
    @FXML
    private DatePicker addNgayLap;

    @FXML
    private ComboBox<String> updateMaHoKhauCBox;
    @FXML
    private TextField updateDienTichText;
    @FXML
    private DatePicker updateNgayLap;

    @FXML
    private TableView<HoKhauModel> householdTableView;
    @FXML
    private TableColumn<HoKhauModel, String> maHKCol;
    @FXML
    private TableColumn<HoKhauModel, Float> dienTichCol;
    @FXML
    private TableColumn<HoKhauModel, String> chuHoCol;
    @FXML
    private TableColumn<HoKhauModel, LocalDate> ngayLapCol;

    @FXML
    private TextField searchbar;

    private ObservableList<HoKhauModel> list;
    private List<String> maHoKhauList = new ArrayList<>();

    @FXML
    public void initialize() {
        loadData();
        initializeSearchbar();

        // Populate update ComboBox
        for (HoKhauModel hoKhau : list) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        updateMaHoKhauCBox.setItems(FXCollections.observableArrayList(maHoKhauList));

        // Listener for update selection
        updateMaHoKhauCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (HoKhauModel hk : list) {
                    if (hk.getMaHoKhau().equals(newValue)) {
                        updateDienTichText.setText(String.valueOf(hk.getDienTichHo()));
                        updateNgayLap.setValue(hk.getNgayLap());
                        break;
                    }
                }
            }
        });
    }

    @FXML
    public void addHoKhauOnAction(ActionEvent event) {
        String maHoKhau = addMaHoKhauText.getText();
        String dienTichStr = addDienTichText.getText();
        LocalDate ngayLap = addNgayLap.getValue();

        if (ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(dienTichStr) || ngayLap == null) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin!");
            return;
        }

        if (maHoKhauList.contains(maHoKhau)) {
            ControllerUtil.showErrorMessage("Mã căn hộ đã tồn tại!");
            return;
        }

        try {
            float dienTich = Float.parseFloat(dienTichStr);
            if (dienTich <= 0) {
                ControllerUtil.showErrorMessage("Diện tích phải > 0");
                return;
            }

            if (ControllerUtil.showConfirmationDialog("Xác nhận", "Thêm hộ khẩu mới?")) {
                // Owner is initially null/empty until assigned via NhanKhau
                HoKhauModel newHoKhau = new HoKhauModel(maHoKhau, ngayLap, dienTich, "");
                MysqlConnector.getInstance().addHoKhauData(newHoKhau);

                ControllerUtil.showSuccessAlert("Thêm thành công!");
                refreshData();
                clearAddForm();
            }
        } catch (NumberFormatException e) {
            ControllerUtil.showErrorMessage("Diện tích không hợp lệ!");
        }
    }

    @FXML
    public void updateHoKhauOnAction(ActionEvent event) {
        String maHoKhau = updateMaHoKhauCBox.getValue();
        String dienTichStr = updateDienTichText.getText();
        LocalDate ngayLap = updateNgayLap.getValue();

        if (ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(dienTichStr) || ngayLap == null) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin!");
            return;
        }

        try {
            float dienTich = Float.parseFloat(dienTichStr);
            if (dienTich <= 0) {
                ControllerUtil.showErrorMessage("Diện tích phải > 0");
                return;
            }

            if (ControllerUtil.showConfirmationDialog("Xác nhận", "Cập nhật hộ khẩu?")) {
                // Preserve existing owner name for model, though updateHoKhauData ignores it
                String currentOwner = "";
                for (HoKhauModel hk : list)
                    if (hk.getMaHoKhau().equals(maHoKhau))
                        currentOwner = hk.getChuHo();

                HoKhauModel updated = new HoKhauModel(maHoKhau, ngayLap, dienTich, currentOwner);
                MysqlConnector.getInstance().updateHoKhauData(updated);

                ControllerUtil.showSuccessAlert("Cập nhật thành công!");
                refreshData();
                clearUpdateForm();
            }
        } catch (NumberFormatException e) {
            ControllerUtil.showErrorMessage("Diện tích không hợp lệ!");
        }
    }

    @FXML
    public void deleteHoKhauOnAction(ActionEvent event) {
        HoKhauModel hoKhau = householdTableView.getSelectionModel().getSelectedItem();
        if (hoKhau == null) {
            ControllerUtil.showErrorMessage("Vui lòng chọn hộ khẩu muốn xóa!");
            return;
        }
        if (ControllerUtil.showConfirmationDialog("Xác nhận", "Xóa hộ khẩu " + hoKhau.getMaHoKhau() + "?")) {
            MysqlConnector.getInstance().deleteHoKhauData(hoKhau.getMaHoKhau());
            refreshData();
            ControllerUtil.showSuccessAlert("Xóa thành công!");
        }
    }

    private void loadData() {
        maHKCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        dienTichCol.setCellValueFactory(new PropertyValueFactory<>("dienTichHo"));
        chuHoCol.setCellValueFactory(new PropertyValueFactory<>("chuHo"));
        ngayLapCol.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));

        list = MysqlConnector.getInstance().getHoKhauData();
        householdTableView.setItems(list);
    }

    private void refreshData() {
        list = MysqlConnector.getInstance().getHoKhauData();
        householdTableView.setItems(list);

        // Update combo box
        maHoKhauList.clear();
        for (HoKhauModel hoKhau : list) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        updateMaHoKhauCBox.setItems(FXCollections.observableArrayList(maHoKhauList));

        initializeSearchbar();
    }

    private void initializeSearchbar() {
        FilteredList<HoKhauModel> filteredData = new FilteredList<>(list, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(hoKhau -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();

                if (hoKhau.getMaHoKhau().toLowerCase().contains(lowerCaseFilter))
                    return true;
                if (hoKhau.getChuHo() != null && hoKhau.getChuHo().toLowerCase().contains(lowerCaseFilter))
                    return true;
                return false;
            });
        });
        SortedList<HoKhauModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(householdTableView.comparatorProperty());
        householdTableView.setItems(sortedData);
    }

    private void clearAddForm() {
        addMaHoKhauText.clear();
        addDienTichText.clear();
        addNgayLap.setValue(null);
    }

    private void clearUpdateForm() {
        updateMaHoKhauCBox.getSelectionModel().clearSelection();
        updateDienTichText.clear();
        updateNgayLap.setValue(null);
    }
}
