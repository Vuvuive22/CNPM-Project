
package Controller;

import Model.HoKhauModel;
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
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;


public class PhiGuiXeController {

     @FXML
    private TableView<PhiCoDinhModel> feeTableView;

    @FXML
    private Label giaOToLabel;

    @FXML
    private TextField giaOToText;

    @FXML
    private Label giaXeDapLabel;

    @FXML
    private TextField giaXeDapText;

    @FXML
    private Label giaXeMayLabel;

    @FXML
    private TextField giaXeMayText;

    @FXML
    private TableColumn<PhiCoDinhModel, String> maHoKhauCol;

    @FXML
    private TableColumn<HoKhauModel, String> maHoKhauCol1;

    @FXML
    private TableColumn<PhiCoDinhModel, Float> phiMoiThangCol;

    @FXML
    private TextField searchbar;

    @FXML
    private TableColumn<HoKhauModel, Integer> soOToCol;

    @FXML
    private TextField soOToText;

    @FXML
    private TableColumn<HoKhauModel, Integer> soXeDapCol;

    @FXML
    private TextField soXeDapText;

    @FXML
    private TableColumn<HoKhauModel, Integer> soXeMayCol;

    @FXML
    private TextField soXeMayText;

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
    private ComboBox<String> updateMaHoKhauCBox;

    @FXML
    private TableView<HoKhauModel> vehicleTableView;

    @FXML
    private ComboBox<Integer> yearCBox;
    
    
    private ObservableList<PhiCoDinhModel> feeList; //Danh sách phí
    
    private ObservableList<HoKhauModel> vehicleList;  //Danh sách phương tiện mỗi hộ
    
    private final String tenPhi = "PhiGuiXe";
    
    @FXML
    public void initialize(){
        loadData(Year.now().getValue());
        initializeSearchbar();
        yearCBox.getItems().addAll(2023, 2024, 2025);
        yearCBox.setValue(Year.now().getValue());
        
        List<String> maHoKhauList = new ArrayList<>();
        for (HoKhauModel hoKhau : vehicleList) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        ObservableList<String> observableMaHoKhauList = FXCollections.observableArrayList(maHoKhauList);
        updateMaHoKhauCBox.setItems(observableMaHoKhauList);
    }
    
    @FXML 
    public void selectYearOnAction(ActionEvent event){
        Integer year = yearCBox.getValue();
        loadData(year);
        feeTableView.refresh();
    }
    
    @FXML
    public void changeVehicleOnAction(ActionEvent event) {
        String maHoKhau = updateMaHoKhauCBox.getValue();
        int soXeMay = Integer.parseInt(soXeMayText.getText());
        int soOTo = Integer.parseInt(soOToText.getText());
        int soXeDap = Integer.parseInt(soXeDapText.getText());
        if (ControllerUtil.isEmptyOrNull(Integer.toString(soXeMay)) || ControllerUtil.isEmptyOrNull(Integer.toString(soOTo)) || ControllerUtil.isEmptyOrNull(Integer.toString(soXeDap)) || ControllerUtil.isEmptyOrNull(maHoKhau)) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường!");
            return;
        }
        if(soXeMay < 0){
            ControllerUtil.showErrorMessage("Số xe máy không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(soOTo < 0){
            ControllerUtil.showErrorMessage("Số ô tô không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(soXeDap < 0){
            ControllerUtil.showErrorMessage("Số xe đạp không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận cập nhật số phương tiện của 1 hộ khẩu" , "Bạn có chắc chắn muốn thay đổi số phương tiện của hộ khẩu có mã " + maHoKhau + " không ?" );
        if(confirmed){
            MysqlConnector.getInstance().changeVehicleData(maHoKhau, soXeMay, soOTo, soXeDap, Year.now().getValue());
            ControllerUtil.showSuccessAlert("Cập nhật thành công!");
            feeList = MysqlConnector.getInstance().getFeeData(tenPhi, yearCBox.getValue());
            feeTableView.setItems(feeList);
            feeTableView.refresh();
            vehicleList = MysqlConnector.getInstance().getVehicleData();
            vehicleTableView.setItems(vehicleList);
            vehicleTableView.refresh();
            updateMaHoKhauCBox.getSelectionModel().clearSelection();
            updateMaHoKhauCBox.setPromptText("Mã hộ khẩu");
            soXeMayText.clear();
            soOToText.clear();
            soXeDapText.clear();
        }
    }

    @FXML
    public void changeFeeOnAction(ActionEvent event) {
        float giaXeMay = Float.parseFloat(giaXeMayText.getText());
        float giaOTo = Float.parseFloat(giaOToText.getText());
        float giaXeDap = Float.parseFloat(giaXeDapText.getText());
        
        if (ControllerUtil.isEmptyOrNull(Float.toString(giaOTo)) || ControllerUtil.isEmptyOrNull(Float.toString(giaXeMay)) || ControllerUtil.isEmptyOrNull(Float.toString(giaXeDap))) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường!");
            return;
        }
        
        if(giaXeMay <= 0){
            ControllerUtil.showErrorMessage("Phí gửi xe máy không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(giaOTo <= 0){
            ControllerUtil.showErrorMessage("Phí gửi ô tô không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(giaXeDap <= 0){
            ControllerUtil.showErrorMessage("Phí gửi xe đạp không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thay đổi tiền gửi xe đối với mỗi phương tiện" , "Bạn có chắc chắn muốn thay đổi tiền gửi xe đối với mỗi phương tiện không ?");
        if(confirmed){
            MysqlConnector.getInstance().changeFeePerVehicleData(giaXeMay, giaOTo, giaXeDap, Year.now().getValue());
            ControllerUtil.showSuccessAlert("Cập nhật thành công!");
            feeList = MysqlConnector.getInstance().getFeeData(tenPhi, yearCBox.getValue());
            feeTableView.setItems(feeList);
            feeTableView.refresh();
            giaXeMayLabel.setText(Float.toString(MysqlConnector.getInstance().getFeePerVehicleData("GiaXeMay", Year.now().getValue())) + " /đồng/xe/tháng");
            giaOToLabel.setText(Float.toString(MysqlConnector.getInstance().getFeePerVehicleData("GiaOTo", Year.now().getValue())) + " /đồng/xe/tháng");
            giaXeDapLabel.setText(Float.toString(MysqlConnector.getInstance().getFeePerVehicleData("GiaXeDap", Year.now().getValue())) + " /đồng/xe/tháng");
            giaXeMayText.clear();
            giaOToText.clear();
            giaXeDapText.clear();
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
        giaXeMayLabel.setText(fee + " /đồng/xe/tháng");
        fee = MysqlConnector.getInstance().getFeePerVehicleData("GiaOTo", year);
        giaOToLabel.setText(fee + " /đồng/xe/tháng");
        fee = MysqlConnector.getInstance().getFeePerVehicleData("GiaXeDap", year);
        giaXeDapLabel.setText(fee + " /đồng/xe/tháng");
    }
    
    private void initializeSearchbar(){
        FilteredList<PhiCoDinhModel> filteredData = new FilteredList<>(feeList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PhiCoDinhModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
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
        
        FilteredList<HoKhauModel> filteredData1 = new FilteredList<>(vehicleList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData1.setPredicate(HoKhauModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
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