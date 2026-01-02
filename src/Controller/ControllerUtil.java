
package Controller;

import View.MainView;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


//Lớp Util hỗ trợ các lớp khác trong package Controller

public class ControllerUtil {
    public static void ChangeScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/View/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            Stage mainStage = MainView.getMainStage(); 
            mainStage.setScene(scene);
            mainStage.setTitle(title);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static boolean showConfirmationDialog(String title, String header) {
        // Tạo một cửa sổ xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    public static boolean isEmptyOrNull(String value) {
        return value == null || value.trim().isEmpty();
    }
}
