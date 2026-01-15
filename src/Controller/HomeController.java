
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
import javafx.scene.control.Button;
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
    private Button statisticButton;

    @FXML
    private Button toaNhaButton;

    @FXML
    private Button householdButton;

    @FXML
    private Button residentButton;

    @FXML
    private Button vehicleButton;

    @FXML
    private MenuButton feeButton;

    @FXML
    private MenuItem managementFeeButton;

    @FXML
    private MenuItem parkingFeeButton;

    @FXML
    private MenuItem payButton;

    @FXML
    private MenuItem serviceFeeButton;

    @FXML
    private MenuItem livingFeeButton;

    @FXML
    private MenuItem voluntaryFeeButton;

    @FXML
    public void initialize() {
        // Set Trang chu (ThongKe) as default view
        setCenterContent("ThongKeView.fxml");
    }

    // Logout
    @FXML
    public void LogoutOnAction(ActionEvent event) {
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận đăng xuất",
                "Bạn có chắc chắn muốn đăng xuất không ?");
        if (confirmed) {
            ControllerUtil.ChangeScene("LoginView.fxml", "Login");
            ControllerUtil.showSuccessAlert("Đăng xuất thành công!");
        }
    }

    // Hiển thị thông tin cá nhân của admin
    @FXML
    public void UserInfoOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(HomeController.class.getResource("/View/UserInfoForm.fxml"));
        Stage changePwStage = new Stage();
        changePwStage.setResizable(false);
        changePwStage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo chỉ có thể tương tác với cửa sổ này
        changePwStage.setTitle("Thông tin cá nhân người dùng");
        changePwStage.setScene(new Scene(root));
        changePwStage.showAndWait();
    }

    // Hiển thị màn hình thay đổi mật khẩu
    @FXML
    public void ChangePwOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(HomeController.class.getResource("/View/ChangePwForm.fxml"));
        Stage changePwStage = new Stage();
        changePwStage.setResizable(false);
        changePwStage.initModality(Modality.APPLICATION_MODAL); // Đảm bảo chỉ có thể tương tác với cửa sổ này
        changePwStage.setTitle("Form thay đổi mật khẩu");
        changePwStage.setScene(new Scene(root));
        changePwStage.showAndWait();
    }

    @FXML
    public void QuanLyPhuongTienOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QuanLyPhuongTienView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Quản lý phương tiện");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void HandleClick(ActionEvent event) {
        if (event.getSource() == statisticButton) { // Trang chủ
            setCenterContent("ThongKeView.fxml");
        } else if (event.getSource() == toaNhaButton) { // Tòa nhà
            setCenterContent("ToaNhaView.fxml");
        } else if (event.getSource() == residentButton) { // Cư dân (NhanKhau)
            setCenterContent("NhanKhauView.fxml");
        } else if (event.getSource() == householdButton) { // Hộ gia đình (HoKhau)
            setCenterContent("HoKhauView.fxml");
        } else if (event.getSource() == serviceFeeButton) {
            setCenterContent("PhiDichVuView.fxml");
        } else if (event.getSource() == managementFeeButton) {
            setCenterContent("PhiQuanLyView.fxml");
        } else if (event.getSource() == parkingFeeButton) {
            setCenterContent("PhiGuiXeView.fxml");
        } else if (event.getSource() == livingFeeButton) {
            setCenterContent("PhiSinhHoatView.fxml");
        } else if (event.getSource() == voluntaryFeeButton) {
            setCenterContent("PhiDongGopView.fxml");
        } else if (event.getSource() == payButton) {
            setCenterContent("ThanhToanView.fxml");
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
