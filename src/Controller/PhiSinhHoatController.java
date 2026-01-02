package Controller;

import Model.CapNhatPhiSinhHoat;
import Model.MysqlConnector;
import Model.PhiSinhHoatModel;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class PhiSinhHoatController {

    @FXML
    private TableView<PhiSinhHoatModel> feeTableView;

    @FXML
    private TableColumn<PhiSinhHoatModel, String> maHoKhauCol;

    @FXML
    private TableColumn<CapNhatPhiSinhHoat, String> maHoKhauCol1;

    @FXML
    private ComboBox<Integer> monthCBox;

    @FXML
    private TextField searchbar;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang10Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang11Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang12Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang1Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang2Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang3Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang4Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang5Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang6Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang7Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang8Col;

    @FXML
    private TableColumn<PhiSinhHoatModel, Float> thang9Col;

    @FXML
    private TableColumn<CapNhatPhiSinhHoat, Float> tienDienCol;

    @FXML
    private TextField tienDienText;

    @FXML
    private TableColumn<CapNhatPhiSinhHoat, String> tienInternetCol;

    @FXML
    private TextField tienInternetText;

    @FXML
    private TableColumn<CapNhatPhiSinhHoat, String> tienNuocCol;

    @FXML
    private TextField tienNuocText;
 
    @FXML
    private TableView<CapNhatPhiSinhHoat> updateFeeTableView;

    @FXML
    private ComboBox<String> updateMaHoKhauCBox;

    @FXML
    private ComboBox<Integer> yearCBox;
    
    private ObservableList<PhiSinhHoatModel> feeList;
    
    private ObservableList<CapNhatPhiSinhHoat> updateFeeList; 
    
    private final LocalDate currentDate = LocalDate.now();
    
    @FXML
    public void initialize(){
        initializeComboBox();
        loadFeeData(yearCBox.getValue());
        loadUpdateFeeData(monthCBox.getValue(), yearCBox.getValue());    
        initializeSearchbar();
    }

    @FXML
    public void selectYearOnAction(ActionEvent event) {
        Integer year = yearCBox.getValue();
        loadFeeData(year);
        feeTableView.refresh();
    }
    
    @FXML
    public void selectMonthOnAction(ActionEvent event) {
        Integer month = monthCBox.getValue();
        Integer year = yearCBox.getValue();
        loadUpdateFeeData(month, year);
        updateFeeTableView.refresh();
    }
    
    @FXML
    public void updateFeeOnAction(ActionEvent event) {
        String maHoKhau = updateMaHoKhauCBox.getValue();
        Float tienDien = Float.valueOf(tienDienText.getText());
        Float tienNuoc = Float.valueOf(tienNuocText.getText());
        Float tienInternet = Float.valueOf(tienInternetText.getText());
        if(ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(String.valueOf(tienDien)) || ControllerUtil.isEmptyOrNull(String.valueOf(tienNuoc)) || ControllerUtil.isEmptyOrNull(String.valueOf(tienInternet))){
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường trong form!");
            return;
        }
        if(tienDien <= 0){
            ControllerUtil.showErrorMessage("Tiền điện không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(tienNuoc <= 0){
            ControllerUtil.showErrorMessage("Tiền nước không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(tienInternet <= 0){
            ControllerUtil.showErrorMessage("Tiền internet không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận cập nhật phí sinh hoạt", "Bạn có chắc chắn muốn cập nhật phí sinh hoạt tháng " + currentDate.getMonthValue() + " của hộ khẩu này không?");
        if(confirmed){
            boolean check = MysqlConnector.getInstance().isaddCapNhatPhiSinhHoatValidated(maHoKhau);
            if(check){
                ControllerUtil.showErrorMessage("Tiền sinh hoạt của hộ khẩu có mã " + maHoKhau + " đã được cập nhật. Vui lòng chọn lại mã hộ khẩu khác!");
                return;
            }
            CapNhatPhiSinhHoat fee = new CapNhatPhiSinhHoat(maHoKhau, tienDien, tienNuoc, tienInternet);
            MysqlConnector.getInstance().addCapNhatPhiSinhHoatData(fee);
            ControllerUtil.showSuccessAlert("Cập nhật phí sinh hoạt thành công!");
            updateFeeList.add(fee);
            updateFeeTableView.refresh();
            updateMaHoKhauCBox.getSelectionModel().clearSelection();
            tienDienText.clear();
            tienNuocText.clear();
            tienInternetText.clear();
        }
    }
    
    private void loadFeeData(int year) {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
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

        feeList = MysqlConnector.getInstance().getPhiSinhHoatData(year); 
        feeTableView.setItems(feeList);
    }
    
    private void loadUpdateFeeData(int month, int year){
        maHoKhauCol1.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        tienDienCol.setCellValueFactory(new PropertyValueFactory<>("tienDien"));               
        tienNuocCol.setCellValueFactory(new PropertyValueFactory<>("tienNuoc"));
        tienInternetCol.setCellValueFactory(new PropertyValueFactory<>("tienInternet"));

        updateFeeList = MysqlConnector.getInstance().getCapNhatPhiSinhHoatData(month, year);
        updateFeeTableView.setItems(updateFeeList);
    }
    
    private void initializeComboBox(){
        yearCBox.getItems().addAll(2023, 2024);
        yearCBox.setValue(currentDate.getYear());
        monthCBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7 , 8, 9, 10, 11, 12);
        monthCBox.setValue(currentDate.getMonthValue());
        
        ObservableList<String> observableMaHoKhauList = MysqlConnector.getInstance().getMaHoKhauData();
        updateMaHoKhauCBox.setItems(observableMaHoKhauList);
    }
    
    private void initializeSearchbar(){
        FilteredList<PhiSinhHoatModel> filteredData = new FilteredList<>(feeList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PhiSinhHoatModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                
                return PhiSinhHoatModel.getMaHoKhau().toLowerCase().contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang1()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang2()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang3()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang4()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang5()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang6()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang7()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang8()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang9()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang10()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang11()).contains(searchWord)
                || String.valueOf(PhiSinhHoatModel.getThang12()).contains(searchWord);
            });
        });
        SortedList<PhiSinhHoatModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(feeTableView.comparatorProperty());
        feeTableView.setItems(sortedData);
        
        FilteredList<CapNhatPhiSinhHoat> filteredData1 = new FilteredList<>(updateFeeList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData1.setPredicate(CapNhatPhiSinhHoat -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                
                return CapNhatPhiSinhHoat.getMaHoKhau().toLowerCase().contains(searchWord)
                || String.valueOf(CapNhatPhiSinhHoat.getTienDien()).contains(searchWord)
                || String.valueOf(CapNhatPhiSinhHoat.getTienNuoc()).contains(searchWord)
                || String.valueOf(CapNhatPhiSinhHoat.getTienInternet()).contains(searchWord);
            });
        });
        SortedList<CapNhatPhiSinhHoat> sortedData1 = new SortedList<>(filteredData1);
        sortedData1.comparatorProperty().bind(updateFeeTableView.comparatorProperty());
        updateFeeTableView.setItems(sortedData1);
    }
    
}
