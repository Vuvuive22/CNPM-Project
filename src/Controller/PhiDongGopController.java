package Controller;

import Model.DSPhiDongGop;
import Model.MysqlConnector;
import Model.PhiDongGopModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

public class PhiDongGopController {

    @FXML
    private TableView<PhiDongGopModel> feeTableView;
    @FXML
    private TableColumn<PhiDongGopModel, String> maHoKhauCol;
    @FXML
    private TableColumn<PhiDongGopModel, String> tenPhiCol;
    @FXML
    private TableColumn<PhiDongGopModel, Float> soTienCol;
    @FXML
    private TableColumn<PhiDongGopModel, Date> ngayDongGopCol;

    @FXML
    private TableView<DSPhiDongGop> listFeeTableView;
    @FXML
    private TableColumn<DSPhiDongGop, String> tenPhi1Col;
    @FXML
    private TableColumn<DSPhiDongGop, Float> soTienGoiYCol;

    @FXML
    private TextField searchbar;

    private ObservableList<PhiDongGopModel> feeList;
    private ObservableList<DSPhiDongGop> list;

    @FXML
    public void initialize() {
        loadData();
        initializeSearchbar();
    }

    @FXML
    public void addFeeOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddPhiDongGopView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Thêm khoản phí đóng góp");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            // Refresh data after popup closes
            loadData();
            initializeSearchbar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteFeeOnAction(ActionEvent event) {
        DSPhiDongGop fee = listFeeTableView.getSelectionModel().getSelectedItem();
        if (fee == null) {
            ControllerUtil.showErrorMessage("Vui lòng chọn loại phí đóng góp muốn xóa!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa phí: " + fee.getTenPhi() + "?");
        if (confirmed) {
            MysqlConnector.getInstance().deleteDSPhiDongGopData(fee.getTenPhi());
            list.remove(fee);
            ControllerUtil.showSuccessAlert("Xóa phí thành công!");
            listFeeTableView.refresh();
        }
    }

    private void loadData() {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        tenPhiCol.setCellValueFactory(new PropertyValueFactory<>("tenPhi"));
        soTienCol.setCellValueFactory(new PropertyValueFactory<>("soTien"));
        ngayDongGopCol.setCellValueFactory(new PropertyValueFactory<>("ngayDongGop"));
        feeList = MysqlConnector.getInstance().getPhiDongGopData();
        feeTableView.setItems(feeList);

        tenPhi1Col.setCellValueFactory(new PropertyValueFactory<>("tenPhi"));
        soTienGoiYCol.setCellValueFactory(new PropertyValueFactory<>("soTienGoiY"));
        list = MysqlConnector.getInstance().getDSPhiDongGopData();
        listFeeTableView.setItems(list);
    }

    private void initializeSearchbar() {
        FilteredList<PhiDongGopModel> filteredData = new FilteredList<>(feeList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(phidongGop -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                String dateStr = "";
                if (phidongGop.getNgayDongGop() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateStr = dateFormat.format(phidongGop.getNgayDongGop());
                }

                return phidongGop.getMaHoKhau().toLowerCase().contains(searchWord)
                        || phidongGop.getTenPhi().toLowerCase().contains(searchWord)
                        || dateStr.contains(searchWord)
                        || String.valueOf(phidongGop.getSoTien()).contains(searchWord);
            });
        });
        SortedList<PhiDongGopModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(feeTableView.comparatorProperty());
        feeTableView.setItems(sortedData);
    }
}
