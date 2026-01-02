
package View;

import java.io.IOException;
import javafx.application.Application;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class MainView extends Application {
    private static Stage mainStage; // Biến static để lưu trữ Stage chính

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage; // Lưu trữ Stage chính
        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new javafx.scene.image.Image("/image/logoApp.png"));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }
}
