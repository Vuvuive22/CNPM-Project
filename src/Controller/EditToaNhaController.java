package Controller;

import Model.MysqlConnector;
import Model.ToaNhaModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditToaNhaController {

    @FXML
    private TextField maToaNhaTF;

    @FXML
    private TextArea moTaTA;

    @FXML
    private TextField tenToaNhaTF;

    private ToaNhaModel toaNha;

    public void setToaNha(ToaNhaModel toaNha) {
        this.toaNha = toaNha;
        maToaNhaTF.setText(toaNha.getMaToaNha());
        tenToaNhaTF.setText(toaNha.getTenToaNha());
        moTaTA.setText(toaNha.getMoTa());
    }

    @FXML
    void cancel(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void save(ActionEvent event) {
        String id = maToaNhaTF.getText();
        String name = tenToaNhaTF.getText();
        String desc = moTaTA.getText();

        if (ControllerUtil.isEmptyOrNull(name)) {
            ControllerUtil.showErrorMessage("Vui lòng điền tên tòa nhà!");
            return;
        }

        toaNha.setTenToaNha(name);
        toaNha.setMoTa(desc);

        boolean success = MysqlConnector.getInstance().updateToaNha(toaNha);

        if (success) {
            ControllerUtil.showSuccessAlert("Cập nhật thành công!");
            closeStage(event);
        } else {
            ControllerUtil.showErrorMessage("Cập nhật thất bại!");
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
