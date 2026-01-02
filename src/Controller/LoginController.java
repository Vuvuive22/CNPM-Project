    
package Controller;

import Model.MysqlConnector;
import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;


public class LoginController {
    @FXML
    private TextField txtUserName;
    
    @FXML
    private PasswordField txtHidePassword;
    
    @FXML
    private TextField txtShowPassword;
    
    @FXML
    private ImageView Open_Eye_Icon;
    
    @FXML
    private HBox showPassword;
    
    @FXML
    private HBox hidePassword;
    
    @FXML
    private javafx.scene.layout.AnchorPane backGround;
    
    @FXML
    private Button loginButton; 
    
    String password;
    
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            txtShowPassword.setVisible(true);
            Open_Eye_Icon.setVisible(true);

            Scene scene = loginButton.getScene();
            scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    loginButton.fire();
                }
            });
        });
    }
    
    @FXML
    public void LostFocus(MouseEvent event){
        backGround.requestFocus();
    }
    
    @FXML
    public void HidePasswordOnAction(KeyEvent keyEvent){
        password = txtHidePassword.getText();
        txtShowPassword.setText(password);
    }
    
    @FXML
    public void ShowPasswordOnAction(KeyEvent keyEvent){
        password = txtShowPassword.getText();
        txtHidePassword.setText(password);
    }
    
    
    @FXML
    public void Close_Eye_ClickOnAction(MouseEvent mouseEvent){
        showPassword.setVisible(true);
        hidePassword.setVisible(false);
        
    }   
    @FXML
    public void Open_Eye_ClickOnAction(MouseEvent mouseEvent){
        showPassword.setVisible(false);
        hidePassword.setVisible(true);
    }
    
    
    @FXML
    public void CheckLogin(ActionEvent event){
        String userName = txtUserName.getText();
        if(userName.isEmpty() || password.isEmpty()){
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ thông tin để đăng nhập!");
            return;
        }
        
        //Chỉ cần try-catch hàm getConnection() 1 lần trong hàm CheckLogin(), những lần sau không cần try-catch
        try {
            Connection connection = MysqlConnector.getInstance().getConnection();
            if (connection == null) {
                ControllerUtil.showErrorMessage("Lỗi kết nối với cơ sở dữ liệu.");
            }

            String sql = "SELECT * FROM user WHERE UserName = ? AND Password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userName);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    ControllerUtil.ChangeScene("HomeView.fxml", "Home");
                } else {
                    ControllerUtil.showErrorMessage("Sai tên đăng nhập hoặc mật khẩu.");
                }
            }
        } catch (SQLException e) {
            ControllerUtil.showErrorMessage("Lỗi kết nối với cơ sở dữ liệu.");
        }
    }
    
    
}
