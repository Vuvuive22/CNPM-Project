package Controller;

import Model.MysqlConnector;
import Model.TamTruModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TamTruListController implements Initializable {

    @FXML
    private TableView<TamTruModel> tamTruTable;

    @FXML
    private TableColumn<TamTruModel, String> maTamTruCol;

    @FXML
    private TableColumn<TamTruModel, String> soCCCDCol;

    @FXML
    private TableColumn<TamTruModel, LocalDate> tuNgayCol;

    @FXML
    private TableColumn<TamTruModel, LocalDate> denNgayCol;

    @FXML
    private TableColumn<TamTruModel, String> lyDoCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maTamTruCol.setCellValueFactory(new PropertyValueFactory<>("maTamTru"));
        soCCCDCol.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        tuNgayCol.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        denNgayCol.setCellValueFactory(new PropertyValueFactory<>("denNgay"));
        lyDoCol.setCellValueFactory(new PropertyValueFactory<>("lyDo"));

        loadData();
    }

    private void loadData() {
        ObservableList<TamTruModel> list = MysqlConnector.getInstance().getTamTruData();
        tamTruTable.setItems(list);
    }
}
