
package Controller;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;



public class HomeController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuButton feeButton;

    @FXML
    private MenuButton homeOwnerButton;

    @FXML
    private MenuItem householdButton;

    @FXML
    private MenuItem livingFeeButton;

    @FXML
    private MenuItem managementFeeButton;

    @FXML
    private MenuItem parkingFeeButton;

    @FXML
    private MenuItem payButton;

    @FXML
    private MenuItem residentButton;

    @FXML
    private MenuItem serviceFeeButton;

    @FXML
    private MenuItem statisticButton;

    @FXML
    private MenuItem voluntaryFeeButton;
    
    
    @FXML
    public void initialize(){
// Animations removed for cleaner UI/UX with VBox layout
        
        setCenterContent("ThongKeView.fxml");
        
    }
    
    
    //Logout
    @FXML
    public void LogoutOnAction(ActionEvent event){ 
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận đăng xuất", "Bạn có chắc chắn muốn đăng xuất không ?");
        if(confirmed){
            ControllerUtil.ChangeScene("LoginView.fxml", "Login");
            ControllerUtil.showSuccessAlert("Đăng xuất thành công!");
        }
    }
    
    
    //Hiển thị thông tin cá nhân của admin
    @FXML
    public void UserInfoOnAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(HomeController.class.getResource("/View/UserInfoForm.fxml"));
        Stage changePwStage = new Stage();
        changePwStage.setResizable(false);
        changePwStage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo chỉ có thể tương tác với cửa sổ này
        changePwStage.setTitle("Thông tin cá nhân người dùng");
        changePwStage.setScene(new Scene(root));
        changePwStage.showAndWait();
    }
    
    
    //Hiển thị màn hình thay đổi mật khẩu
    @FXML
    public void ChangePwOnAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(HomeController.class.getResource("/View/ChangePwForm.fxml"));
        Stage changePwStage = new Stage();
        changePwStage.setResizable(false);
        changePwStage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo chỉ có thể tương tác với cửa sổ này
        changePwStage.setTitle("Form thay đổi mật khẩu");
        changePwStage.setScene(new Scene(root));
        changePwStage.showAndWait();
    }
    
    
    @FXML
    public void HandleClick(ActionEvent event){
        if(event.getSource() == residentButton){  //Hiển thị bảng nhân khẩu
            setCenterContent("NhanKhauView.fxml");
        }
        else if(event.getSource() == householdButton){   //Hiển thị bảng hộ khẩu
            setCenterContent("HoKhauView.fxml");
        }
        else if(event.getSource() == serviceFeeButton){  //Hiển thị bảng phí dịch vụ
            setCenterContent("PhiDichVuView.fxml");
        }
        else if(event.getSource() == managementFeeButton){   //Hiển thị bảng phí quản lý
            setCenterContent("PhiQuanLyView.fxml");
        }
        else if(event.getSource() == parkingFeeButton){   //Hiển thị bảng phí gửi xe
            setCenterContent("PhiGuiXeView.fxml");
        }
        else if(event.getSource() == livingFeeButton){   //Hiện thị bảng phí sinh hoạt
            setCenterContent("PhiSinhHoatView.fxml");
        }
        else if(event.getSource() == voluntaryFeeButton){  //Hiển thị bảng phí đóng góp
            setCenterContent("PhiDongGopView.fxml");
        }
        else if(event.getSource() == payButton){   //Hiển thị thanh toán
            setCenterContent("ThanhToanView.fxml");
        }
        else if(event.getSource() == statisticButton){  //Hiển thị các thống kê
            setCenterContent("ThongKeView.fxml");
        }
    }
    
    private void setCenterContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/View/" + fxmlFile));
            Node centerContent = loader.load();
            borderPane.setCenter(centerContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
