
package Controller;

import Model.MysqlConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UserInfoFormController {
    @FXML
    private Label diaChiLabel;

    @FXML
    private TextField diaChiText;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailText;

    @FXML
    private Label hoTenLabel;

    @FXML
    private TextField hoTenText;

    @FXML
    private Label soDTLabel;

    @FXML
    private TextField soDTText;

    @FXML
    private Label tuoiLabel;

    @FXML
    private TextField tuoiText;
    
    private String username = "admin";
    
    @FXML
    public void initialize(){
        setEditable(false);
        hoTenLabel.setText( MysqlConnector.getInstance().getHoTenData(username));
        diaChiLabel.setText( MysqlConnector.getInstance().getDiaChiData(username));
        emailLabel.setText( MysqlConnector.getInstance().getEmailData(username));
        tuoiLabel.setText(Integer.toString(MysqlConnector.getInstance().getTuoiData(username)));
        soDTLabel.setText( MysqlConnector.getInstance().getSoDTData(username));
    }
    
    @FXML
    public void saveOnAction(ActionEvent event) {
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thay đổi", "Bạn có chắc chắn muốn lưu các thay đổi ?");
        if(confirmed){
            setEditing(false);
            MysqlConnector.getInstance().changeDiaChiData(username, diaChiText.getText());
            MysqlConnector.getInstance().changeHoTenData(username, hoTenText.getText());
            MysqlConnector.getInstance().changeEmailData(username, emailText.getText());
            MysqlConnector.getInstance().changeSoDTData(username, soDTText.getText());
            MysqlConnector.getInstance().changeTuoiData(username, Integer.parseInt(tuoiText.getText()));
            ControllerUtil.showSuccessAlert("Thay đổi thông tin cá nhân thành công!");
        }
    }

    @FXML
    public void changeOnAction(ActionEvent event) {
        setEditing(true);
    }
    
    private void setEditable(boolean editable){
        diaChiText.setVisible(editable);
        diaChiLabel.setVisible(!editable);
        emailText.setVisible(editable);
        emailLabel.setVisible(!editable);
        hoTenText.setVisible(editable);
        hoTenLabel.setVisible(!editable);
        soDTText.setVisible(editable);
        soDTLabel.setVisible(!editable);
        tuoiText.setVisible(editable);
        tuoiLabel.setVisible(!editable);
    }

    private void setEditing(boolean editing) {
        setEditable(editing);
        if (editing) {
            diaChiText.setText(diaChiLabel.getText());
            emailText.setText(emailLabel.getText());
            hoTenText.setText(hoTenLabel.getText());
            soDTText.setText(soDTLabel.getText());
            tuoiText.setText(tuoiLabel.getText());
        } else {
            diaChiLabel.setText(diaChiText.getText());
            emailLabel.setText(emailText.getText());
            hoTenLabel.setText(hoTenText.getText());
            soDTLabel.setText(soDTText.getText());
            tuoiLabel.setText(tuoiText.getText());
        }
    }

}
