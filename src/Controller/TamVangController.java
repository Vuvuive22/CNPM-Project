package Controller;

import Model.MysqlConnector;
import Model.TamVangModel;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TamVangController {

    @FXML
    private TableColumn<TamVangModel, LocalDate> denNgayCol;

    @FXML
    private TableColumn<TamVangModel, String> maTamVangCol;

    @FXML
    private TableColumn<TamVangModel, String> noiTamTruCol;

    @FXML
    private TableColumn<TamVangModel, String> soCCCDCol;

    @FXML
    private TableView<TamVangModel> tamVangTableView;

    @FXML
    private TableColumn<TamVangModel, LocalDate> tuNgayCol;
    
    private ObservableList<TamVangModel> list;
    
    @FXML
    public void initialize(){
        loadData();
    }
    
    @FXML
    public void deleteOnAction(ActionEvent event) {
        TamVangModel tamVang = tamVangTableView.getSelectionModel().getSelectedItem();
        if(tamVang == null){
            ControllerUtil.showErrorMessage("Vui lòng chọn nhân khẩu muốn xóa khỏi danh sách tạm vắng!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa nhân khẩu khỏi danh sách", "Bạn có chắc chắn muốn xóa hộ khẩu này khỏi danh sách không ?");
        if(confirmed){
            MysqlConnector.getInstance().deleteTamVangData(tamVang.getMaTamVang());
            list.remove(tamVang);
            ControllerUtil.showSuccessAlert("Xóa nhân khẩu khỏi danh sách thành công!");
            tamVangTableView.refresh();
        }
    }
    
    private void loadData(){
        maTamVangCol.setCellValueFactory(new PropertyValueFactory<>("maTamVang"));
        soCCCDCol.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        noiTamTruCol.setCellValueFactory(new PropertyValueFactory<>("noiTamTru"));
        tuNgayCol.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        denNgayCol.setCellValueFactory(new PropertyValueFactory<>("denNgay"));
        
        
        list = MysqlConnector.getInstance().getTamVangData();
        tamVangTableView.setItems(list);
    }

}
