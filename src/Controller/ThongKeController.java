package Controller;

import Model.MysqlConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ThongKeController {
    @FXML
    private Label numOfHoKhauLabel;

    @FXML
    private Label numOfNhanKhauLabel;

    @FXML
    private Label numOfToaNhaLabel;

    @FXML
    public void initialize() {
        numOfHoKhauLabel.setText(String.valueOf(MysqlConnector.getInstance().getNumberOfHoKhau()));
        numOfNhanKhauLabel.setText(String.valueOf(MysqlConnector.getInstance().getNumberOfNhanKhau()));
        numOfToaNhaLabel.setText(String.valueOf(MysqlConnector.getInstance().getNumberOfToaNha()));
    }
}
