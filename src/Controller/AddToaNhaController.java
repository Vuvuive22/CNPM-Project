package Controller;

import Model.MysqlConnector;
import Model.ToaNhaModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddToaNhaController {

    @FXML
    private TextField maToaNhaTF;

    @FXML
    private TextArea moTaTA;

    @FXML
    private TextField tenToaNhaTF;

    @FXML
    void cancel(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void save(ActionEvent event) {
        String id = maToaNhaTF.getText();
        String name = tenToaNhaTF.getText();
        String desc = moTaTA.getText();

        if (ControllerUtil.isEmptyOrNull(id) || ControllerUtil.isEmptyOrNull(name)) {
            ControllerUtil.showErrorMessage("Vui lòng điền đầy đủ Mã và Tên tòa nhà!");
            return;
        }

        ToaNhaModel toaNha = new ToaNhaModel(id, name, desc);
        boolean success = MysqlConnector.getInstance().addToaNha(toaNha);

        if (success) {
            ControllerUtil.showSuccessAlert("Thêm tòa nhà thành công!");
            closeStage(event);
        } else {
            ControllerUtil.showErrorMessage("Thêm thất bại! Có thể mã tòa nhà đã tồn tại.");
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
