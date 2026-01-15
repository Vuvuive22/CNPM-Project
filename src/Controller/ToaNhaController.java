package Controller;

import Model.MysqlConnector;
import Model.ToaNhaModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class ToaNhaController {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<ToaNhaModel, String> maToaNhaCol;

    @FXML
    private TableColumn<ToaNhaModel, String> moTaCol;

    @FXML
    private TableColumn<ToaNhaModel, String> tenToaNhaCol;

    @FXML
    private TableColumn<ToaNhaModel, ToaNhaModel> thaoTacCol;

    @FXML
    private TableView<ToaNhaModel> toaNhaTable;

    private ObservableList<ToaNhaModel> toaNhaList;

    @FXML
    public void initialize() {
        // Setup columns
        maToaNhaCol.setCellValueFactory(new PropertyValueFactory<>("MaToaNha"));
        tenToaNhaCol.setCellValueFactory(new PropertyValueFactory<>("TenToaNha"));
        moTaCol.setCellValueFactory(new PropertyValueFactory<>("MoTa"));

        // Delete/Edit Buttons Column
        thaoTacCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        thaoTacCol.setCellFactory(param -> new TableCell<ToaNhaModel, ToaNhaModel>() {
            private final Button editButton = new Button("");
            private final Button deleteButton = new Button("");
            private final HBox pane = new HBox(10, editButton, deleteButton);

            {
                // Styling buttons (Ideally load images, but text/style for now as placeholders
                // or use existing icons if available)
                // Using emojis or text for simplicity as per requirement "icon" - verifying
                // existing icons first
                // Edit Icon
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/edit.png")));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                editButton.setGraphic(editIcon);
                editButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                editButton.setOnAction(event -> {
                    ToaNhaModel toaNha = getTableView().getItems().get(getIndex());
                    handleEdit(toaNha);
                });

                // Delete Icon
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/trash.png")));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                deleteButton.setOnAction(event -> {
                    ToaNhaModel toaNha = getTableView().getItems().get(getIndex());
                    handleDelete(toaNha);
                });

                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(ToaNhaModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        // Load data
        loadData();
    }

    private void loadData() {
        toaNhaList = MysqlConnector.getInstance().getAllToaNha();
        toaNhaTable.setItems(toaNhaList);
    }

    @FXML
    void add(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/View/AddToaNhaView.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Thêm tòa nhà mới");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

            // Refresh table
            loadData();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(ToaNhaModel toaNha) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/View/EditToaNhaView.fxml"));
            javafx.scene.Parent root = loader.load();

            EditToaNhaController controller = loader.getController();
            controller.setToaNha(toaNha);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Sửa thông tin tòa nhà");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

            // Refresh table
            loadData();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(ToaNhaModel toaNha) {
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa tòa nhà " + toaNha.getTenToaNha() + "?");
        if (confirmed) {
            boolean success = MysqlConnector.getInstance().deleteToaNha(toaNha.getMaToaNha());
            if (success) {
                ControllerUtil.showSuccessAlert("Xóa thành công!");
                loadData();
            } else {
                ControllerUtil.showErrorMessage("Xóa thất bại!");
            }
        }
    }
}
