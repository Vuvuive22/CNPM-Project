package Controller;

import Model.MysqlConnector;
import Model.TamTruModel;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TamTruController {

    @FXML
    private TableColumn<TamTruModel, LocalDate> denNgayCol;

    @FXML
    private TableColumn<TamTruModel, String> maTamTruCol;

    @FXML
    private TableColumn<TamTruModel, String> lyDoCol;

    @FXML
    private TableColumn<TamTruModel, String> soCCCDCol;

    @FXML
    private TableView<TamTruModel> tamTruTableView;

    @FXML
    private TableColumn<TamTruModel, LocalDate> tuNgayCol;
    
    private ObservableList<TamTruModel> list;
    
    @FXML
    public void initialize() {
        loadData();
    }

    @FXML
    public void deleteOnAction(ActionEvent event) {
        TamTruModel tamTru = tamTruTableView.getSelectionModel().getSelectedItem();
        if (tamTru == null) {
            ControllerUtil.showErrorMessage("Vui lòng chọn nhân khẩu muốn xóa khỏi danh sách tạm trú!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa nhân khẩu khỏi danh sách", "Bạn có chắc chắn muốn xóa hộ khẩu này khỏi danh sách không ?");
        if (confirmed) {
            MysqlConnector.getInstance().deleteTamTruData(tamTru.getMaTamTru());
            list.remove(tamTru);
            ControllerUtil.showSuccessAlert("Xóa nhân khẩu khỏi danh sách thành công!");
            tamTruTableView.refresh();
        }
    }

    private void loadData() {
        maTamTruCol.setCellValueFactory(new PropertyValueFactory<>("maTamTru"));
        soCCCDCol.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        lyDoCol.setCellValueFactory(new PropertyValueFactory<>("lyDo"));
        tuNgayCol.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        denNgayCol.setCellValueFactory(new PropertyValueFactory<>("denNgay"));

        list = MysqlConnector.getInstance().getTamTruData();
        tamTruTableView.setItems(list);
    }
}
