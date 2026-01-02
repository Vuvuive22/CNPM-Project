package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private TextField addDiaChiText;

    @FXML
    private TextField addLyDoChuyenText;

    @FXML
    private DatePicker addNgayChuyenDi;

    @FXML
    private DatePicker addNgayLap;

    @FXML
    private TextField addMaHoKhauText;

    @FXML
    private TableColumn<HoKhauModel, String> diaChiCol;

    @FXML
    private TableView<HoKhauModel> householdTableView;

    @FXML
    private TableColumn<HoKhauModel, String> lyDoChuyenCol;

    @FXML
    private TableColumn<HoKhauModel, String> maHKCol;

    @FXML
    private TableColumn<HoKhauModel, Date> ngayChuyenDiCol;

    @FXML
    private TableColumn<HoKhauModel, Date> ngayLapCol;

    @FXML
    private TextField searchbar;

    @FXML
    private TextField updateDiaChiText;

    @FXML
    private TextField updateLyDoChuyenText;

    @FXML
    private ComboBox<String> updateMaHoKhauCBox;

    @FXML
    private DatePicker updateNgayChuyenDi;

    @FXML
    private DatePicker updateNgayLap;
    
    private ObservableList<HoKhauModel> list;  //List hộ khẩu
    
    private List<String> maHoKhauList = new ArrayList<>();
    
    @FXML
    public void initialize(){
        loadData();
        initializeSearchbar();
        for (HoKhauModel hoKhau : list) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        ObservableList<String> observableMaHoKhauList = FXCollections.observableArrayList(maHoKhauList);
        updateMaHoKhauCBox.setItems(observableMaHoKhauList);
    }
    

    @FXML
    public void addHoKhauOnAction(ActionEvent event) {
        String maHoKhau = addMaHoKhauText.getText();
        String diaChi = addDiaChiText.getText();
        LocalDate ngayLap = addNgayLap.getValue();
        LocalDate ngayChuyenDi = addNgayChuyenDi.getValue();
        String lyDoChuyen = addLyDoChuyenText.getText();
        if(ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(diaChi) || ngayLap == null || ngayChuyenDi == null || ControllerUtil.isEmptyOrNull(lyDoChuyen)){
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường trong form thêm hộ khẩu!");
            return;
        }
        if(maHoKhauList.contains(maHoKhau)){
            ControllerUtil.showErrorMessage("Hộ khẩu có mã " + maHoKhau + " đã tồn tại. Vui lòng nhập lại!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thêm hộ khẩu mới", "Bạn có chắc chắn muốn thêm hộ khẩu có mã " + maHoKhau + " không ?");
        if(confirmed){
            HoKhauModel newHoKhau = new HoKhauModel(maHoKhau, diaChi, ngayLap, ngayChuyenDi, lyDoChuyen);
            MysqlConnector.getInstance().addHoKhauData(newHoKhau);
            ControllerUtil.showSuccessAlert("Thêm hộ khẩu mới thành công!");
            list.add(newHoKhau);
            householdTableView.refresh();
            addMaHoKhauText.clear();
            addDiaChiText.clear();
            addNgayLap.setValue(null);
            addNgayChuyenDi.setValue(null);
            addLyDoChuyenText.clear();
        }
    }
    
    @FXML
    public void updateHoKhauOnAction(ActionEvent event) {
        String maHoKhau = updateMaHoKhauCBox.getValue();
        String diaChi = updateDiaChiText.getText();
        LocalDate ngayLap = updateNgayLap.getValue();
        LocalDate ngayChuyenDi = updateNgayChuyenDi.getValue();
        String lyDoChuyen = updateLyDoChuyenText.getText();
        if(ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(diaChi) || ngayLap == null || ngayChuyenDi == null || ControllerUtil.isEmptyOrNull(lyDoChuyen)){
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường trong form cập nhật hộ khẩu!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận cập nhật hộ khẩu", "Bạn có chắc chắn muốn cập nhật hộ khẩu có mã " + maHoKhau + " không ?");
        if(confirmed){
            HoKhauModel updatedHoKhau = new HoKhauModel(maHoKhau, diaChi, ngayLap, ngayChuyenDi, lyDoChuyen);
            MysqlConnector.getInstance().updateHoKhauData(updatedHoKhau);
            ControllerUtil.showSuccessAlert("Cập nhật hộ khẩu với mã " + maHoKhau + " thành công!");
            list = MysqlConnector.getInstance().getHoKhauData();
            householdTableView.setItems(list);
            householdTableView.refresh();
            updateMaHoKhauCBox.getSelectionModel().clearSelection();
            updateDiaChiText.clear();
            updateNgayLap.setValue(null);
            updateNgayChuyenDi.setValue(null);
            updateLyDoChuyenText.clear();
        }
    }

    @FXML
    public void deleteHoKhauOnAction(ActionEvent event) { 
        HoKhauModel hoKhau = householdTableView.getSelectionModel().getSelectedItem();
        if(hoKhau == null){
            ControllerUtil.showErrorMessage("Vui lòng chọn hộ khẩu muốn xóa!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa hộ khẩu", "Xóa hộ khẩu này sẽ xóa toàn bộ nhân khẩu trong hộ. Bạn có chắc chắn muốn xóa hộ khẩu có mã " + hoKhau.getMaHoKhau() + " không ?");
        if(confirmed){
            MysqlConnector.getInstance().deleteHoKhauData(hoKhau.getMaHoKhau());
            list.remove(hoKhau);
            maHoKhauList.remove(hoKhau.getMaHoKhau());
            ObservableList<String> observableMaHoKhauList = FXCollections.observableArrayList(maHoKhauList);
            updateMaHoKhauCBox.setItems(observableMaHoKhauList);
            ControllerUtil.showSuccessAlert("Xóa hộ khẩu thành công!");
            householdTableView.refresh();
        }
        
    }
    
    private void loadData(){
        maHKCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        diaChiCol.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        ngayLapCol.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
        ngayChuyenDiCol.setCellValueFactory(new PropertyValueFactory<>("ngayChuyenDi"));
        lyDoChuyenCol.setCellValueFactory(new PropertyValueFactory<>("lyDoChuyen"));
        
        list = MysqlConnector.getInstance().getHoKhauData();
        householdTableView.setItems(list);
        
    }
    
    private void initializeSearchbar(){
        FilteredList<HoKhauModel> filteredData = new FilteredList<>(list, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(HoKhauModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                return HoKhauModel.getMaHoKhau().toLowerCase().contains(searchWord)
                || HoKhauModel.getDiaChi().toLowerCase().contains(searchWord)
                || dateFormat.format(HoKhauModel.getNgayLap()).contains(searchWord)
                || dateFormat.format(HoKhauModel.getNgayChuyenDi()).contains(searchWord)
                || HoKhauModel.getLyDoChuyen().toLowerCase().contains(searchWord);
            });
        });
        SortedList<HoKhauModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(householdTableView.comparatorProperty());
        householdTableView.setItems(sortedData);
    }
    

}
