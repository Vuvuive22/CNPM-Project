
package Controller;

import Model.MysqlConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class ChangePwFormController {

    @FXML
    private Button changePwBtn;

    @FXML
    private PasswordField confirmedPwText;

    @FXML
    private PasswordField newPwText;

    @FXML
    private PasswordField oldPwText;

    @FXML
    public void changePwOnAction(ActionEvent event) {
        String oldPw = oldPwText.getText();
        String newPw = newPwText.getText();
        String confirmedPw = confirmedPwText.getText();
        String pw = MysqlConnector.getInstance().getPwData("admin");
        
        if(!oldPw.equals(pw)){
            ControllerUtil.showErrorMessage("Mật khẩu cũ không chính xác. Vui lòng nhập lại!");
            return;
        }
        if(!newPw.equals(confirmedPw)){
            ControllerUtil.showErrorMessage("Mật khẩu mới và xác nhận mật khẩu không khớp. Vui lòng nhập lại!");
            return;
        }
        if(!oldPw.equals(newPw)){
            ControllerUtil.showErrorMessage("Mật khẩu cũ và mật khẩu mới trùng nhau. Vui lòng nhập lại!");
            return;
        }
        
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thay đổi mật khẩu", "Bạn có chắc chắn muốn thay đổi mật khẩu không ?");
        if(confirmed){
            MysqlConnector.getInstance().changePwData("admin", newPw);
            ControllerUtil.showSuccessAlert("Thay đổi mật khẩu thành công!");
        }
        oldPwText.clear();
        newPwText.clear();
        confirmedPwText.clear();
    }
    
}
