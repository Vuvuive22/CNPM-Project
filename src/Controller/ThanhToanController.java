package Controller;

import Model.MysqlConnector;
import Model.PhiDongGopModel;
import Model.ThanhToanModel;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ThanhToanController {

    @FXML
    private ComboBox<String> maHoKhauCBox;
    @FXML
    private ComboBox<Integer> monthCBox;
    @FXML
    private ComboBox<Integer> yearCBox;

    @FXML
    private TableColumn<ThanhToanModel, String> maHoKhauCol;
    @FXML
    private TableColumn<ThanhToanModel, LocalDate> ngayThanhToanCol;
    @FXML
    private TableColumn<ThanhToanModel, Float> soTienThanhToanCol;
    @FXML
    private TableView<ThanhToanModel> paymentTableView;

    @FXML
    private ComboBox<String> tenPhiCBox;
    @FXML
    private TextField soTienDongGopText;

    @FXML
    private Label totalFeeLabel;

    // access the VBox from FXML
    @FXML
    private VBox feeListVBox;

    @FXML
    private TextField searchbar;

    private final LocalDate currentDate = LocalDate.now();
    private ObservableList<ThanhToanModel> paymentList;
    private float calculatedTotal = 0.0f;

    @FXML
    public void initialize() {
        // Load Payment History
        loadPaymentHistory();
        initializeSearchbar();

        // Populate Apartment List
        ObservableList<String> maHoKhauList = MysqlConnector.getInstance().getMaHoKhauData();
        maHoKhauCBox.getItems().setAll(maHoKhauList);

        // Populate Month & Year
        for (int i = 1; i <= 12; i++) {
            monthCBox.getItems().add(i);
        }
        int currentYearVal = Year.now().getValue();
        yearCBox.getItems().addAll(currentYearVal - 1, currentYearVal, currentYearVal + 1);

        // Default values
        monthCBox.setValue(currentDate.getMonthValue());
        yearCBox.setValue(currentYearVal);

        // Populate Contribution Fees
        ObservableList<String> feeNameList = MysqlConnector.getInstance().getFeeNameData();
        tenPhiCBox.getItems().setAll(feeNameList);

        // Listeners
        maHoKhauCBox.setOnAction(e -> calculateTotal());
        monthCBox.setOnAction(e -> calculateTotal());
        yearCBox.setOnAction(e -> calculateTotal());

        soTienDongGopText.textProperty().addListener((obs, oldVal, newVal) -> updateTotalFromSelection());
    }

    private void calculateTotal() {
        if (maHoKhauCBox.getValue() == null || monthCBox.getValue() == null || yearCBox.getValue() == null) {
            return;
        }

        String maHoKhau = maHoKhauCBox.getValue();
        int month = monthCBox.getValue();
        int year = yearCBox.getValue();

        // Ensure data exists for this year
        MysqlConnector.getInstance().checkAndCreateFeeData(year);

        feeListVBox.getChildren().clear();

        // 1. Fee: Service (PhiDichVu)
        float monthlyFeeDichVu = MysqlConnector.getInstance().getTienNopMoiThangData("PhiDichVu", maHoKhau, year);
        if (MysqlConnector.getInstance().isLegalPayment("PhiDichVu", maHoKhau, month, year)) {
            addFeeCheckBox("Phí dịch vụ", monthlyFeeDichVu, "PhiDichVu");
        } else {
            addPaidLabel("Phí dịch vụ: Đã thanh toán");
        }

        // 2. Fee: Management (PhiQuanLy)
        float monthlyFeeQuanLy = MysqlConnector.getInstance().getTienNopMoiThangData("PhiQuanLy", maHoKhau, year);
        if (MysqlConnector.getInstance().isLegalPayment("PhiQuanLy", maHoKhau, month, year)) {
            addFeeCheckBox("Phí quản lý", monthlyFeeQuanLy, "PhiQuanLy");
        } else {
            addPaidLabel("Phí quản lý: Đã thanh toán");
        }

        // 3. Fee: Parking (PhiGuiXe)
        float monthlyFeeGuiXe = MysqlConnector.getInstance().getTienNopMoiThangData("PhiGuiXe", maHoKhau, year);
        if (MysqlConnector.getInstance().isLegalPayment("PhiGuiXe", maHoKhau, month, year)) {
            addFeeCheckBox("Phí gửi xe", monthlyFeeGuiXe, "PhiGuiXe");
        } else {
            addPaidLabel("Phí gửi xe: Đã thanh toán");
        }

        // 4. Fee: Living (PhiSinhHoat - Electricity/Water/etc)
        if (MysqlConnector.getInstance().isHavingLivingFee(maHoKhau, month, year)) {
            if (MysqlConnector.getInstance().isLegalPayment("PhiSinhHoat", maHoKhau, month, year)) {
                float livingFee = MysqlConnector.getInstance().getLivingFeeThisMonth(maHoKhau, month, year);
                if (livingFee > 0) {
                    addFeeCheckBox("Phí sinh hoạt", livingFee, "PhiSinhHoat");
                } else {
                    addPaidLabel("Phí sinh hoạt: Chưa có dữ liệu (0 VND)");
                }
            } else {
                addPaidLabel("Phí sinh hoạt: Đã thanh toán");
            }
        } else {
            addPaidLabel("Phí sinh hoạt: Chưa cập nhật số liệu");
        }

        // Calculate initial total (all selected by default)
        updateTotalFromSelection();
    }

    private void addFeeCheckBox(String label, float amount, String type) {
        CheckBox cb = new CheckBox(String.format("%s: %,.0f VND", label, amount));
        // Store metadata in UserData: type|amount
        cb.setUserData(type + "|" + amount);
        cb.setSelected(true); // Default selected
        cb.setStyle("-fx-font-size: 16px; -fx-text-fill: #102a43;");
        cb.selectedProperty().addListener((obs, oldVal, newVal) -> updateTotalFromSelection());
        feeListVBox.getChildren().add(cb);
    }

    private void addPaidLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 16px; -fx-text-fill: green; -fx-font-weight: bold;");
        feeListVBox.getChildren().add(lbl);
    }

    private void updateTotalFromSelection() {
        float total = 0;

        // Sum from checkboxes
        for (javafx.scene.Node node : feeListVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox cb = (CheckBox) node;
                if (cb.isSelected()) {
                    String data = (String) cb.getUserData(); // "Type|Amount"
                    if (data != null) {
                        try {
                            float amount = Float.parseFloat(data.split("\\|")[1]);
                            total += amount;
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }

        // Add Contribution
        try {
            if (soTienDongGopText.getText() != null && !soTienDongGopText.getText().isEmpty()) {
                float contribution = Float.parseFloat(soTienDongGopText.getText());
                if (contribution > 0) {
                    total += contribution;
                }
            }
        } catch (NumberFormatException e) {
            // ignore
        }

        calculatedTotal = total;
        totalFeeLabel.setText(String.format("%,.0f", total));
    }

    @FXML
    public void confirmOnAction(ActionEvent event) {
        String maHoKhau = maHoKhauCBox.getValue();
        Integer month = monthCBox.getValue();
        Integer year = yearCBox.getValue();

        if (ControllerUtil.isEmptyOrNull(maHoKhau)) {
            ControllerUtil.showErrorMessage("Vui lòng chọn mã căn hộ!");
            return;
        }

        if (calculatedTotal <= 0) {
            ControllerUtil.showErrorMessage("Không có khoản phí nào được chọn hoặc tổng tiền bằng 0.");
            return;
        }

        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thanh toán",
                "Bạn có chắc chắn muốn thanh toán tổng số tiền " + String.format("%,.0f", calculatedTotal) + " đồng?");

        if (confirmed) {
            ThanhToanModel payment = new ThanhToanModel(maHoKhau, calculatedTotal, currentDate);
            MysqlConnector.getInstance().addThanhToanData(payment);

            // Process selected fees
            for (javafx.scene.Node node : feeListVBox.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        String data = (String) cb.getUserData();
                        if (data != null) {
                            String type = data.split("\\|")[0];
                            // Update DB based on type
                            if ("PhiSinhHoat".equals(type)) {
                                MysqlConnector.getInstance().updatePhiSinhHoatData(maHoKhau, month, year);
                            } else {
                                // For PhiDichVu, PhiQuanLy, PhiGuiXe
                                MysqlConnector.getInstance().updateFeeData(type, maHoKhau, month, year);
                            }
                        }
                    }
                }
            }

            // Contribution
            String feeName = tenPhiCBox.getValue();
            String dongGopStr = soTienDongGopText.getText();
            if (!ControllerUtil.isEmptyOrNull(feeName) && !ControllerUtil.isEmptyOrNull(dongGopStr)) {
                try {
                    float tienDongGop = Float.parseFloat(dongGopStr);
                    if (tienDongGop > 0) {
                        PhiDongGopModel model = new PhiDongGopModel(maHoKhau, feeName, tienDongGop, currentDate);
                        MysqlConnector.getInstance().addPhiDongGopData(model);
                    }
                } catch (NumberFormatException e) {
                }
            }

            ControllerUtil.showSuccessAlert("Thanh toán thành công!");

            // Refresh
            paymentList.add(payment);
            paymentTableView.refresh();

            // Re-calculate to refresh the list (paid items should become disabled/labels)
            calculateTotal();

            // Reset Contribution
            soTienDongGopText.clear();
            tenPhiCBox.getSelectionModel().clearSelection();
        }
    }

    private void loadPaymentHistory() {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        soTienThanhToanCol.setCellValueFactory(new PropertyValueFactory<>("soTienThanhToan"));
        ngayThanhToanCol.setCellValueFactory(new PropertyValueFactory<>("ngayThanhToan"));
        paymentList = MysqlConnector.getInstance().getThanhToanData();
        paymentTableView.setItems(paymentList);
    }

    private void initializeSearchbar() {
        FilteredList<ThanhToanModel> filteredData = new FilteredList<>(paymentList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ThanhToanModel -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                return ThanhToanModel.getMaHoKhau().toLowerCase().contains(searchWord)
                        || String.valueOf(ThanhToanModel.getSoTienThanhToan()).contains(searchWord)
                        || dateFormat.format(ThanhToanModel.getNgayThanhToan()).contains(searchWord);
            });
        });
        SortedList<ThanhToanModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(paymentTableView.comparatorProperty());
        paymentTableView.setItems(sortedData);
    }
}
