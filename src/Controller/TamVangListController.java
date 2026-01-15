package Controller;

import Model.MysqlConnector;
import Model.TamVangModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TamVangListController implements Initializable {

    @FXML
    private TableView<TamVangModel> tamVangTable;

    @FXML
    private TableColumn<TamVangModel, String> maTamVangCol;

    @FXML
    private TableColumn<TamVangModel, String> soCCCDCol;

    @FXML
    private TableColumn<TamVangModel, String> noiTamTruCol;

    @FXML
    private TableColumn<TamVangModel, LocalDate> tuNgayCol;

    @FXML
    private TableColumn<TamVangModel, LocalDate> denNgayCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maTamVangCol.setCellValueFactory(new PropertyValueFactory<>("maTamVang"));
        soCCCDCol.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        noiTamTruCol.setCellValueFactory(new PropertyValueFactory<>("noiTamTru"));
        tuNgayCol.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        denNgayCol.setCellValueFactory(new PropertyValueFactory<>("denNgay"));

        loadData();
    }

    private void loadData() {
        ObservableList<TamVangModel> list = MysqlConnector.getInstance().getTamVangData();
        tamVangTable.setItems(list);
    }
}
