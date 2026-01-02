
package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import Model.NhanKhauModel;
import Model.TamTruModel;
import Model.TamVangModel;
import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NhanKhauController {
    @FXML
    private TextField addGioiTinhText;

    @FXML
    private TextField addHoTenText;

    @FXML
    private ComboBox<String> addMaHoKhauCBox;

    @FXML
    private TextField addQuanHeText;

    @FXML
    private TextField addSoCCCDText;

    @FXML
    private TextField addSoDTText;

    @FXML
    private TextField addTuoiText;

    @FXML
    private DatePicker denNgayTamTruText;

    @FXML
    private DatePicker denNgayTamVangText;

    @FXML
    private TableColumn<NhanKhauModel, String> gioiTinhCol;

    @FXML
    private TableColumn<NhanKhauModel, String> hoTenCol;

    @FXML
    private CheckBox isChuHoCBox;

    @FXML
    private CheckBox isChuHoCBox1;

    @FXML
    private TextField lyDoTamTruText;

    @FXML
    private TableColumn<NhanKhauModel, String> maHKCol;

    @FXML
    private TextField noiTamTruText;

    @FXML
    private TableColumn<NhanKhauModel, String> quanHeCol;

    @FXML
    private TableView<NhanKhauModel> residentTableView;

    @FXML
    private TextField searchbar;

    @FXML
    private TableColumn<NhanKhauModel, String> soCCCDCol;

    @FXML
    private TextField soCCCDTamTruText;

    @FXML
    private TextField soCCCDTamVangText;

    @FXML
    private TableColumn<NhanKhauModel, String> soDTCol;

    @FXML
    private TextField soGiayTamTruText;

    @FXML
    private TextField soGiayTamVangText;

    @FXML
    private TableColumn<NhanKhauModel, String> tamTruCol;

    @FXML
    private TableColumn<NhanKhauModel, String> tamVangCol;

    @FXML
    private DatePicker tuNgayTamTruText;

    @FXML
    private DatePicker tuNgayTamVangText;

    @FXML
    private TableColumn<NhanKhauModel, Integer> tuoiCol;

    @FXML
    private TextField updateGioiTinhText;

    @FXML
    private TextField updateHoTenText;

    @FXML
    private ComboBox<String> updateMaHoKhauCBox;

    @FXML
    private TextField updateQuanHeText;

    @FXML
    private TextField updateSoCCCDText;

    @FXML
    private TextField updateSoDTText;

    @FXML
    private TextField updateTuoiText;
    

    private ObservableList<NhanKhauModel> list;  //List nhân khẩu
    private ObservableList<HoKhauModel> listHK;  //List hộ khẩu
    
    
    @FXML
    public void initialize(){
        loadData();
        initializeSearchbar();
        initializeForm();
    }
    
    
    @FXML
    public void addNhanKhauOnAction(ActionEvent event){
        String maHoKhau = addMaHoKhauCBox.getValue();
        String quanHe = addQuanHeText.getText();
        String gioiTinh = addGioiTinhText.getText();
        String hoTen = addHoTenText.getText();
        String soCCCD = addSoCCCDText.getText();
        String soDT = addSoDTText.getText();
        String tuoi = addTuoiText.getText();
        if (ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(gioiTinh) || ControllerUtil.isEmptyOrNull(hoTen) || ControllerUtil.isEmptyOrNull(soCCCD) || ControllerUtil.isEmptyOrNull(soDT) || ControllerUtil.isEmptyOrNull(tuoi) || ControllerUtil.isEmptyOrNull(quanHe)) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin cho tất cả các trường trong form thêm nhân khẩu!");
            return;
        }
        if (isSoCCCDDuplicated(soCCCD)) {
            ControllerUtil.showErrorMessage("Số CCCD/CMND vừa nhập đã tồn tại! Vui lòng nhập lại!");
            return;
        }
        if(Integer.parseInt(tuoi) <= 0){
            ControllerUtil.showErrorMessage("Tuổi không hợp lệ! Vui lòng nhập lại!");
            return;
        }
        
        if(!isQuanHeValidated(quanHe, maHoKhau)){
            ControllerUtil.showErrorMessage("Quan hệ không hợp lệ! Mỗi hộ khẩu chỉ có 1 Chủ Hộ, vui lòng nhập lại!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thêm nhân khẩu mới", "Bạn có chắc chắn muốn thêm nhân khẩu không ?");
        if(confirmed){
            NhanKhauModel newNhanKhau = new NhanKhauModel(maHoKhau, hoTen, Integer.parseInt(tuoi), gioiTinh, soCCCD, soDT, quanHe, false, false);
            MysqlConnector.getInstance().addNhanKhauData(newNhanKhau);
            list.add(newNhanKhau);
            ControllerUtil.showSuccessAlert("Thêm nhân khẩu mới thành công!");
            residentTableView.refresh();
            addMaHoKhauCBox.getSelectionModel().clearSelection();
            addQuanHeText.clear();
            addGioiTinhText.clear();
            addHoTenText.clear();
            addSoCCCDText.clear();
            addSoDTText.clear();
            addTuoiText.clear();
            isChuHoCBox.setSelected(false);
        }
    }
    
    @FXML
    public void khaiBaoTamTruOnAction(ActionEvent event) {
        String soCCCD = soCCCDTamTruText.getText();
        String maTamTru = soGiayTamTruText.getText();
        String lyDo = lyDoTamTruText.getText();
        LocalDate tuNgay = tuNgayTamTruText.getValue();
        LocalDate denNgay = denNgayTamTruText.getValue();
        if (ControllerUtil.isEmptyOrNull(soCCCD) || ControllerUtil.isEmptyOrNull(maTamTru) || ControllerUtil.isEmptyOrNull(lyDo) || tuNgay == null || denNgay == null) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin cho tất cả các trường trong form khai báo tạm trú!");
            return;
        }
        if (isSoCCCDDuplicated(soCCCD)) {
            ControllerUtil.showErrorMessage("Số CCCD/CMND nằm trong danh sách nhân khẩu sống trong chung cư! Vui lòng nhập lại!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận khai báo tạm trú", "Bạn có chắc chắn muốn khai báo tạm trú cho nhân khẩu này không ?");
        if(confirmed){
            TamTruModel tamTru = new TamTruModel(maTamTru, soCCCD, lyDo, tuNgay, denNgay);
            MysqlConnector.getInstance().addTamTruData(tamTru);
            ControllerUtil.showSuccessAlert("Khai báo tạm trú thành công!");
            list = MysqlConnector.getInstance().getNhanKhauData();
            residentTableView.setItems(list);
            residentTableView.refresh();
            soCCCDTamTruText.clear();
            soGiayTamTruText.clear();
            lyDoTamTruText.clear();
            tuNgayTamTruText.setValue(null);
            denNgayTamTruText.setValue(null);
        }
    }

    @FXML
    public void khaiBaoTamVangOnAction(ActionEvent event) {
        String soCCCD = soCCCDTamVangText.getText();
        String maTamVang = soGiayTamVangText.getText();
        String noiTamTru = noiTamTruText.getText();
        LocalDate tuNgay = tuNgayTamVangText.getValue();
        LocalDate denNgay = denNgayTamVangText.getValue();

        if (ControllerUtil.isEmptyOrNull(soCCCD) || ControllerUtil.isEmptyOrNull(maTamVang) || ControllerUtil.isEmptyOrNull(noiTamTru) || tuNgay == null || denNgay == null) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin cho tất cả các trường trong form khai báo tạm vắng!");
            return;
        }

        if (!isSoCCCDDuplicated(soCCCD)) {
            ControllerUtil.showErrorMessage("Số CCCD/CMND vừa nhập không tồn tại! Vui lòng nhập lại!");
            return;
        }

        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận khai báo tạm vắng", "Bạn có chắc chắn muốn khai báo tạm vắng cho nhân khẩu này không ?");
        if (confirmed) {
            TamVangModel tamVang = new TamVangModel(maTamVang, soCCCD, noiTamTru, tuNgay, denNgay);
            MysqlConnector.getInstance().addTamVangData(tamVang);
            ControllerUtil.showSuccessAlert("Khai báo tạm vắng thành công!");
            list = MysqlConnector.getInstance().getNhanKhauData();
            residentTableView.setItems(list);
            residentTableView.refresh();
            soCCCDTamVangText.clear();
            soGiayTamVangText.clear();
            noiTamTruText.clear();
            tuNgayTamVangText.setValue(null);
            denNgayTamVangText.setValue(null);
        }
    }

    @FXML
    public void updateNhanKhauOnAction(ActionEvent event) {
        String maHoKhau = updateMaHoKhauCBox.getValue();
        String quanHe = updateQuanHeText.getText();
        String gioiTinh = updateGioiTinhText.getText();
        String hoTen = updateHoTenText.getText();
        String soCCCD = updateSoCCCDText.getText();
        String soDT = updateSoDTText.getText();
        String tuoi = updateTuoiText.getText();
        if (ControllerUtil.isEmptyOrNull(maHoKhau) || ControllerUtil.isEmptyOrNull(gioiTinh) || ControllerUtil.isEmptyOrNull(hoTen) || ControllerUtil.isEmptyOrNull(soCCCD) || ControllerUtil.isEmptyOrNull(soDT) || ControllerUtil.isEmptyOrNull(tuoi) || ControllerUtil.isEmptyOrNull(quanHe)) {
            ControllerUtil.showErrorMessage("Vui lòng nhập đủ thông tin cho tất cả các trường trong form cập nhật nhân khẩu!");
            return;
        }
        if (!isSoCCCDDuplicated(soCCCD)) {
            ControllerUtil.showErrorMessage("Số CCCD/CMND vừa nhập không tồn tại! Vui lòng nhập lại!");
            return;
        }
        if(Integer.parseInt(tuoi) <= 0){
            ControllerUtil.showErrorMessage("Tuổi không hợp lệ! Vui lòng nhập lại!");
            return;
        }
        
        for(NhanKhauModel nhanKhau : list){
            if(nhanKhau.getCCCD().equals(soCCCD)){
                NhanKhauModel oldNhanKhau = nhanKhau;
                if(oldNhanKhau.getQuanHe().equals(quanHe)){
                    break;
                }
                if(!isQuanHeValidated(quanHe, maHoKhau) && isChuHo(quanHe)){
                    ControllerUtil.showErrorMessage("Quan hệ không hợp lệ! Mỗi hộ khẩu chỉ có 1 Chủ Hộ, vui lòng nhập lại!");
                    return;
                }
                break;
            }
        }
        
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận cập nhật nhân khẩu", "Bạn có chắc chắn muốn cập nhật nhân khẩu này không ?");
        if(confirmed){
            NhanKhauModel updatedNhanKhau = new NhanKhauModel(maHoKhau, hoTen, Integer.parseInt(tuoi), gioiTinh, soCCCD, soDT, quanHe, false, false);
            MysqlConnector.getInstance().updateNhanKhauData(updatedNhanKhau);
            ControllerUtil.showSuccessAlert("Cập nhật nhân khẩu thành công!");
            list = MysqlConnector.getInstance().getNhanKhauData();
            residentTableView.setItems(list);
            residentTableView.refresh();
            updateMaHoKhauCBox.getSelectionModel().clearSelection();
            updateQuanHeText.clear();
            updateGioiTinhText.clear();
            updateHoTenText.clear();
            updateSoCCCDText.clear();
            updateSoDTText.clear();
            updateTuoiText.clear();
            isChuHoCBox1.setSelected(false);
        }
    }

    @FXML
    public void xemTamTruOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(NhanKhauController.class.getResource("/View/TamTruView.fxml"));
        Stage changePwStage = new Stage();
        changePwStage.setResizable(false);
        changePwStage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo chỉ có thể tương tác với cửa sổ này
        changePwStage.setTitle("Danh sách nhân khẩu khai báo tạm trú");
        changePwStage.setScene(new Scene(root));
        changePwStage.showAndWait();
        list = MysqlConnector.getInstance().getNhanKhauData();
        residentTableView.setItems(list);
        residentTableView.refresh();
    }

    @FXML
    public void xemTamVangOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(NhanKhauController.class.getResource("/View/TamVangView.fxml"));
        Stage changePwStage = new Stage();
        changePwStage.setResizable(false);
        changePwStage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo chỉ có thể tương tác với cửa sổ này
        changePwStage.setTitle("Danh sách nhân khẩu khai báo tạm vắng");
        changePwStage.setScene(new Scene(root));
        changePwStage.showAndWait();
        list = MysqlConnector.getInstance().getNhanKhauData();
        residentTableView.setItems(list);
        residentTableView.refresh();
    }

    
    @FXML
    public void deleteNhanKhauOnAction(ActionEvent event){
        NhanKhauModel nhanKhau = residentTableView.getSelectionModel().getSelectedItem();
        if(nhanKhau == null){
            ControllerUtil.showErrorMessage("Vui lòng chọn nhân khẩu muốn xóa!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa nhân khẩu", "Bạn có chắc chắn muốn xóa nhân khẩu không ?");
        if(confirmed){
            MysqlConnector.getInstance().deleteNhanKhauData(nhanKhau.getCCCD());
            list.remove(nhanKhau);
            ControllerUtil.showSuccessAlert("Xóa nhân khẩu thành công!");
            residentTableView.refresh();
        }
        
    }
    
    private void loadData(){
        maHKCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        hoTenCol.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        tuoiCol.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
        gioiTinhCol.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        soCCCDCol.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
        soDTCol.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        quanHeCol.setCellValueFactory(new PropertyValueFactory<>("quanHe"));
        tamVangCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isTamVang() ? "Có" : "Không"));
        tamTruCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isTamTru() ? "Có" : "Không"));
        
        list = MysqlConnector.getInstance().getNhanKhauData();
        residentTableView.setItems(list);
    }
    
    private void initializeSearchbar(){
        FilteredList<NhanKhauModel> filteredData = new FilteredList<>(list, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(NhanKhauModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                
                String searchWord = newValue.toLowerCase();
                
                if(NhanKhauModel.getMaHoKhau().toLowerCase().contains(searchWord)){
                    return true;
                }
                else if(NhanKhauModel.getHoTen().toLowerCase().contains(searchWord)){
                    return true;
                }
                else if(String.valueOf(NhanKhauModel.getTuoi()).contains(searchWord)){
                    return true;
                }
                else if(NhanKhauModel.getGioiTinh().toLowerCase().contains(searchWord)){
                    return true;
                }
                else if(NhanKhauModel.getCCCD().toLowerCase().contains(searchWord)){
                    return true;
                }
                else if(NhanKhauModel.getSoDT().toLowerCase().contains(searchWord)){
                    return true;
                }
                else if(NhanKhauModel.getQuanHe().toLowerCase().contains(searchWord)){
                    return true;
                }
                else if((NhanKhauModel.isTamVang() ? "Có" : "Không").toLowerCase().contains(searchWord)){
                    return true;
                }
                else if((NhanKhauModel.isTamTru() ? "Có" : "Không").toLowerCase().contains(searchWord)){
                    return true;
                }
                else{
                    return false;
                }
                
            });
        });
        SortedList<NhanKhauModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(residentTableView.comparatorProperty());
        residentTableView.setItems(sortedData);
    }
    
    private void initializeForm(){
        addQuanHeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isChuHoCBox.isSelected() && !newValue.equals("Chủ Hộ")) {
                isChuHoCBox.setSelected(false);
            }
        });
        isChuHoCBox.setOnAction(event -> {
            if (isChuHoCBox.isSelected()) {
                addQuanHeText.setText("Chủ Hộ");
            } else if ("Chủ Hộ".equals(addQuanHeText.getText())) {
                addQuanHeText.clear();
            }
        });
        
        updateQuanHeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isChuHoCBox1.isSelected() && !newValue.equals("Chủ Hộ")) {
                isChuHoCBox1.setSelected(false);
            }
        });
        isChuHoCBox1.setOnAction(event -> {
            if (isChuHoCBox1.isSelected()) {
                updateQuanHeText.setText("Chủ Hộ");
            } else if ("Chủ Hộ".equals(updateQuanHeText.getText())) {
                updateQuanHeText.clear();
            }
        });
        
        //Set item for addMaHoKhauCBox
        listHK = MysqlConnector.getInstance().getHoKhauData();
        List<String> maHoKhauList = new ArrayList<>();
        for (HoKhauModel hoKhau : listHK) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }
        ObservableList<String> observableMaHoKhauList = FXCollections.observableArrayList(maHoKhauList);
        addMaHoKhauCBox.setItems(observableMaHoKhauList);
        updateMaHoKhauCBox.setItems(observableMaHoKhauList);
    }
    
    
    //Check trùng CCCD
    private boolean isSoCCCDDuplicated(String soCCCD) {
        for (NhanKhauModel nhanKhau : list) {
            if (nhanKhau.getCCCD().equals(soCCCD)) {
                return true; // Số CCCD đã tồn tại trong danh sách
            }
        }
        return false; // Số CCCD không trùng
    }
    
    //Check quanHe không phải chủ hộ của maHoKhau-đã có chủ hộ
    private boolean isQuanHeValidated(String quanHe, String maHoKhau){
        if(!isChuHo(quanHe)){
            return true;
        }
        for(NhanKhauModel nhanKhau : list){
            if(nhanKhau.getMaHoKhau().equals(maHoKhau)){
                if ("Chủ Hộ".equalsIgnoreCase(nhanKhau.getQuanHe())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isChuHo(String quanHe){
        for (int i = 0; i < quanHe.length(); i++) {   //Phải dùng cái này vì dùng equals lỗi
            if(quanHe.charAt(i) != "Chủ Hộ".charAt(i)){
                return false;
            }
        }
        return true;
    }
}
