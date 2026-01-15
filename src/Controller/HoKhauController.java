package Controller;

import Model.HoKhauModel;
import Model.MysqlConnector;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class HoKhauController {

    @FXML
    private TableView<HoKhauModel> householdTableView;
    @FXML
    private TableColumn<HoKhauModel, String> maHKCol;
    @FXML
    private TableColumn<HoKhauModel, String> maToaNhaCol;
    @FXML
    private TableColumn<HoKhauModel, Float> dienTichCol;
    @FXML
    private TableColumn<HoKhauModel, String> chuHoCol;
    @FXML
    private TableColumn<HoKhauModel, LocalDate> ngayLapCol;
    @FXML
    private TableColumn<HoKhauModel, HoKhauModel> thaoTacCol;

    @FXML
    private TextField searchbar;

    private ObservableList<HoKhauModel> list;

    @FXML
    public void initialize() {
        // Setup columns
        maHKCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        dienTichCol.setCellValueFactory(new PropertyValueFactory<>("dienTichHo"));
        chuHoCol.setCellValueFactory(new PropertyValueFactory<>("chuHo"));
        ngayLapCol.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
        maToaNhaCol.setCellValueFactory(new PropertyValueFactory<>("maToaNha"));

        // Thao Tac Column with Icons
        thaoTacCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        thaoTacCol.setCellFactory(param -> new TableCell<HoKhauModel, HoKhauModel>() {
            private final Button editButton = new Button("");
            private final Button deleteButton = new Button("");
            private final HBox pane = new HBox(10, editButton, deleteButton);

            {
                // Edit Icon
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/edit.png")));
                editIcon.setFitHeight(20);
                editIcon.setFitWidth(20);
                editButton.setGraphic(editIcon);
                editButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                editButton.setOnAction(event -> {
                    HoKhauModel hoKhau = getTableView().getItems().get(getIndex());
                    handleEdit(hoKhau);
                });

                // Delete Icon
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/image/trash.png")));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                deleteButton.setOnAction(event -> {
                    HoKhauModel hoKhau = getTableView().getItems().get(getIndex());
                    handleDelete(hoKhau);
                });

                pane.setAlignment(javafx.geometry.Pos.CENTER);
            }

            @Override
            protected void updateItem(HoKhauModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        loadData();
        initializeSearchbar();
    }

    @FXML
    public void addHoKhauOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddHoKhauView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Thêm hộ khẩu mới");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEdit(HoKhauModel hoKhau) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditHoKhauView.fxml"));
            Parent root = loader.load();

            EditHoKhauController controller = loader.getController();
            controller.setHoKhau(hoKhau);

            Stage stage = new Stage();
            stage.setTitle("Cập nhật hộ khẩu");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDelete(HoKhauModel hoKhau) {
        if (ControllerUtil.showConfirmationDialog("Xác nhận", "Xóa hộ khẩu " + hoKhau.getMaHoKhau() + "?")) {
            MysqlConnector.getInstance().deleteHoKhauData(hoKhau.getMaHoKhau());
            refreshData();
            ControllerUtil.showSuccessAlert("Xóa thành công!");
        }
    }

    private void loadData() {
        list = MysqlConnector.getInstance().getHoKhauData();
        householdTableView.setItems(list);
    }

    private void refreshData() {
        list = MysqlConnector.getInstance().getHoKhauData();
        householdTableView.setItems(list);
        initializeSearchbar();
    }

    private void initializeSearchbar() {
        FilteredList<HoKhauModel> filteredData = new FilteredList<>(list, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(hoKhau -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();

                if (hoKhau.getMaHoKhau().toLowerCase().contains(lowerCaseFilter))
                    return true;
                if (hoKhau.getChuHo() != null && hoKhau.getChuHo().toLowerCase().contains(lowerCaseFilter))
                    return true;
                if (hoKhau.getMaToaNha() != null && hoKhau.getMaToaNha().toLowerCase().contains(lowerCaseFilter))
                    return true;
                return false;
            });
        });
        SortedList<HoKhauModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(householdTableView.comparatorProperty());
        householdTableView.setItems(sortedData);
    }
}
