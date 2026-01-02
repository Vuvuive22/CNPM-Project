package Controller;

import Model.MysqlConnector;
import Model.PhiDongGopModel;
import Model.ThanhToanModel;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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

public class ThanhToanController {

    @FXML
    private ComboBox<String> maHoKhauCBox;

    @FXML
    private TableColumn<ThanhToanModel, String> maHoKhauCol;

    @FXML
    private TableColumn<ThanhToanModel, Date> ngayThanhToanCol;

    @FXML
    private TableView<ThanhToanModel> paymentTableView;

    @FXML
    private CheckBox phiDichVuCheckBox;

    @FXML
    private CheckBox phiDongGopCheckBox;

    @FXML
    private CheckBox phiGuiXeCheckBox;

    @FXML
    private CheckBox phiQuanLyCheckBox;

    @FXML
    private CheckBox phiSinhHoatCheckBox;

    @FXML
    private TextField searchbar;

    @FXML
    private TextField soThangDichVuText;

    @FXML
    private TextField soThangGuiXeText;

    @FXML
    private TextField soThangQuanLyText;

    @FXML
    private TextField soTienDongGopText;

    @FXML
    private TableColumn<ThanhToanModel, Float> soTienThanhToanCol;

    @FXML
    private ComboBox<String> tenPhiCBox;

    @FXML
    private Label totalFeeLabel;
    
    @FXML
    private Label yearLabel;
    
    private final LocalDate currentDate = LocalDate.now();
    private final int currentYear = currentDate.getYear();
    private float currentTotalFee = 0.0f;
    private int totalMonth = 0;
    private int count = 0;
    
    private ObservableList<ThanhToanModel> paymentList;
    
    @FXML
    public void initialize(){
        ObservableList<String> maHoKhauList = MysqlConnector.getInstance().getMaHoKhauData();
        maHoKhauCBox.getItems().setAll(maHoKhauList);
        yearLabel.setText("Năm " + currentYear);
        ObservableList<String> feeNameList = MysqlConnector.getInstance().getFeeNameData();
        tenPhiCBox.getItems().setAll(feeNameList);
        
        maHoKhauCBox.setOnAction(event -> {
            initializeCheckBoxAndTextPair(phiDichVuCheckBox, soThangDichVuText, maHoKhauCBox.getValue(), "PhiDichVu");
            initializeCheckBoxAndTextPair(phiQuanLyCheckBox, soThangQuanLyText, maHoKhauCBox.getValue(), "PhiQuanLy");
            initializeCheckBoxAndTextPair(phiGuiXeCheckBox, soThangGuiXeText, maHoKhauCBox.getValue(), "PhiGuiXe");
        });       
        
        phiDongGopCheckBox.setOnAction(event -> {
            if(phiDongGopCheckBox.isSelected()){
                soTienDongGopText.setText("0");
            }
            else{
                soTienDongGopText.clear();
            }
        }); 
        soTienDongGopText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                phiDongGopCheckBox.setSelected(false);
            }
            else{
                phiDongGopCheckBox.setSelected(true);
            }
        });
        soTienDongGopText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                String tienDongGop = soTienDongGopText.getText();
                if(tienDongGop.matches("\\d+")){
                    currentTotalFee += Float.parseFloat(tienDongGop);                
                }
            }
        });
        
        
        loadData();
        initializeSearchbar();
    }
    
    
    @FXML
    public void confirmOnAction(ActionEvent event) {
        String maHoKhau = maHoKhauCBox.getValue();
        String soThangDichVu = soThangDichVuText.getText();
        String soThangQuanLy = soThangQuanLyText.getText();
        String soThangGuiXe = soThangGuiXeText.getText();
        String feeName = tenPhiCBox.getValue();
        float tienDongGop = 0.0f;
        if(ControllerUtil.isEmptyOrNull(maHoKhau)){
            ControllerUtil.showErrorMessage("Vui lòng chọn mã hộ khẩu!");
            return;
        }
        else if(phiDichVuCheckBox.isSelected() && !isValidInput(soThangDichVu)){
            ControllerUtil.showErrorMessage("Trường thanh toán các tháng ở dòng phí dịch vụ không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(phiQuanLyCheckBox.isSelected() && !isValidInput(soThangQuanLy)){
            ControllerUtil.showErrorMessage("Trường thanh toán các tháng ở dòng phí quản lý không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(phiGuiXeCheckBox.isSelected() && !isValidInput(soThangGuiXe)){
            ControllerUtil.showErrorMessage("Trường thanh toán các tháng ở dòng phí gửi xe không hợp lệ. Vui lòng nhập lại!");
            return;
        }
        else if(phiDongGopCheckBox.isSelected()){
            if(ControllerUtil.isEmptyOrNull(feeName)){
                ControllerUtil.showErrorMessage("Vui lòng chọn loại phí đóng góp");
                return;
            }
            else if(ControllerUtil.isEmptyOrNull(soTienDongGopText.getText())){
                ControllerUtil.showErrorMessage("Vui lòng nhập số tiền đóng góp cho " + feeName);
                return;
            }
            tienDongGop = Float.parseFloat(soTienDongGopText.getText());
            if(tienDongGop <= 0){
                ControllerUtil.showErrorMessage("Số tiền đóng góp không hợp lệ. Vui lòng nhập lại!");
                return;
            }
        }
        if(soThangDichVu.matches("\\d+")){
            int month = Integer.parseInt(soThangDichVu);
            if(!MysqlConnector.getInstance().isLegalPayment("PhiDichVu", maHoKhau, month , currentYear)){
                ControllerUtil.showErrorMessage("Tháng " + month + " hộ khẩu này đã thanh toán phí dịch vụ rồi!");
                return;
            }
        }
        else if (soThangDichVu.matches("\\d+-\\d+")) {
            String[] months = soThangDichVu.split("-");
            try {
                int startMonth = Integer.parseInt(months[0]);
                int endMonth = Integer.parseInt(months[1]);
                if(endMonth <= startMonth){
                    ControllerUtil.showErrorMessage("Trường số tháng thanh toán của phí dịch vụ không hợp lệ. Vui lòng nhập lại");
                    return;
                }
                for(int i = startMonth; i<= endMonth; i++){
                    if(!MysqlConnector.getInstance().isLegalPayment("PhiDichVu", maHoKhau, i , currentYear)){
                        ControllerUtil.showErrorMessage("Tháng " + i + " hộ khẩu này đã thanh toán phí dịch vụ rồi!");
                        return;
                    }
                }

            } catch (NumberFormatException e) {
                
            }
        }
        else if(soThangQuanLy.matches("\\d+")){
            int month = Integer.parseInt(soThangQuanLy);
            if(!MysqlConnector.getInstance().isLegalPayment("PhiQuanLy", maHoKhau, month , currentYear)){
                ControllerUtil.showErrorMessage("Tháng " + month + " hộ khẩu này đã thanh toán phí quản lý rồi!");
                return;
            }
        }
        else if (soThangQuanLy.matches("\\d+-\\d+")) {
            String[] months = soThangQuanLy.split("-");
            try {
                int startMonth = Integer.parseInt(months[0]);
                int endMonth = Integer.parseInt(months[1]);
                if(endMonth <= startMonth){
                    ControllerUtil.showErrorMessage("Trường số tháng thanh toán của phí quản lý không hợp lệ. Vui lòng nhập lại");
                    return;
                }
                for(int i = startMonth; i<= endMonth; i++){
                    if(!MysqlConnector.getInstance().isLegalPayment("PhiQuanLy", maHoKhau, i , currentYear)){
                        ControllerUtil.showErrorMessage("Tháng " + i + " hộ khẩu này đã thanh toán phí quản lý rồi!");
                        return;
                    }
                }

            } catch (NumberFormatException e) {
                
            }
        }
        else if(soThangGuiXe.matches("\\d+")){
            int month = Integer.parseInt(soThangGuiXe);
            if(!MysqlConnector.getInstance().isLegalPayment("PhiGuiXe", maHoKhau, month , currentYear)){
                ControllerUtil.showErrorMessage("Tháng " + month + " hộ khẩu này đã thanh toán phí gửi xe rồi!");
                return;
            }
        }
        else if (soThangGuiXe.matches("\\d+-\\d+")) {
            String[] months = soThangGuiXe.split("-");
            try {
                int startMonth = Integer.parseInt(months[0]);
                int endMonth = Integer.parseInt(months[1]);
                if(endMonth <= startMonth){
                    ControllerUtil.showErrorMessage("Trường số tháng thanh toán của phí gửi xe không hợp lệ. Vui lòng nhập lại");
                    return;
                }
                for(int i = startMonth; i<= endMonth; i++){
                    if(!MysqlConnector.getInstance().isLegalPayment("PhiGuiXe", maHoKhau, i , currentYear)){
                        ControllerUtil.showErrorMessage("Tháng " + i + " hộ khẩu này đã thanh toán phí gửi xe rồi!");
                        return;
                    }
                }

            } catch (NumberFormatException e) {
                
            }
        }
        else if(phiSinhHoatCheckBox.isSelected() && !MysqlConnector.getInstance().isLegalPayment("PhiSinhHoat", maHoKhau, currentDate.getMonthValue() , currentYear)){
            ControllerUtil.showErrorMessage("Tháng " + currentDate.getMonthValue() + " hộ khẩu này đã thanh toán phí sinh hoạt rồi!");
            return;
        }
        else if(MysqlConnector.getInstance().isHavingLivingFee(maHoKhau, currentDate.getMonthValue(), currentYear)){
            ControllerUtil.showErrorMessage("Phí sinh hoạt tháng " + currentDate.getMonthValue() + " của hộ khẩu này chưa được cập nhật. Vui lòng cập nhật trước khi thanh toán!");
            return;
        }
        
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thanh toán", "Bạn có chắc chắn muốn thanh toán " + currentTotalFee  + " đồng không ?");
        if(confirmed){
            ThanhToanModel payment = new ThanhToanModel(maHoKhau, currentTotalFee, currentDate);
            MysqlConnector.getInstance().addThanhToanData(payment);
            ControllerUtil.showSuccessAlert("Thanh toán thành công!");
            paymentList.add(payment);
            paymentTableView.refresh();
            if(phiDichVuCheckBox.isSelected()){
                if(soThangDichVu.matches("\\d+")){
                    int month = Integer.parseInt(soThangDichVu);
                    MysqlConnector.getInstance().updateFeeData("PhiDichVu", maHoKhau, month, currentYear);
                }
                else if (soThangDichVu.matches("\\d+-\\d+")) {
                    String[] months = soThangDichVu.split("-");
                    try {
                        int startMonth = Integer.parseInt(months[0]);
                        int endMonth = Integer.parseInt(months[1]);
                        for(int i = startMonth; i<= endMonth; i++){
                            MysqlConnector.getInstance().updateFeeData("PhiDichVu", maHoKhau, i , currentYear);
                        }
                    } catch (NumberFormatException e) {

                    }
                }
            }
            if(phiQuanLyCheckBox.isSelected()){
                if(soThangQuanLy.matches("\\d+")){
                    int month = Integer.parseInt(soThangQuanLy);
                    MysqlConnector.getInstance().updateFeeData("PhiQuanLy", maHoKhau, month, currentYear);
                }
                else if (soThangQuanLy.matches("\\d+-\\d+")) {
                    String[] months = soThangQuanLy.split("-");
                    try {
                        int startMonth = Integer.parseInt(months[0]);
                        int endMonth = Integer.parseInt(months[1]);
                        for(int i = startMonth; i<= endMonth; i++){
                            MysqlConnector.getInstance().updateFeeData("PhiQuanLy", maHoKhau, i , currentYear);
                        }
                    } catch (NumberFormatException e) {

                    }
                }
            }
            if(phiGuiXeCheckBox.isSelected()){
                if(soThangGuiXe.matches("\\d+")){
                    int month = Integer.parseInt(soThangGuiXe);
                    MysqlConnector.getInstance().updateFeeData("PhiGuiXe", maHoKhau, month, currentYear);
                }
                else if (soThangGuiXe.matches("\\d+-\\d+")) {
                    String[] months = soThangGuiXe.split("-");
                    try {
                        int startMonth = Integer.parseInt(months[0]);
                        int endMonth = Integer.parseInt(months[1]);
                        for(int i = startMonth; i<= endMonth; i++){
                            MysqlConnector.getInstance().updateFeeData("PhiGuiXe", maHoKhau, i , currentYear);
                        }
                    } catch (NumberFormatException e) {

                    }
                }
            }
            if(phiDongGopCheckBox.isSelected()){
                PhiDongGopModel model = new PhiDongGopModel(maHoKhau, feeName, tienDongGop, currentDate);
                MysqlConnector.getInstance().addPhiDongGopData(model);
            }
            if(phiSinhHoatCheckBox.isSelected()){
                MysqlConnector.getInstance().updatePhiSinhHoatData(maHoKhau, currentDate.getMonthValue(), currentYear);
            }
            
            maHoKhauCBox.getSelectionModel().clearSelection();
            soThangDichVuText.clear();
            soThangQuanLyText.clear();
            soThangGuiXeText.clear();
            phiSinhHoatCheckBox.setSelected(false);
            tenPhiCBox.getSelectionModel().clearSelection();
            soTienDongGopText.clear();
            totalFeeLabel.setText("0");
        }
             
    }
    
    @FXML
    public void getPhiSinhHoatOnAction(ActionEvent event) {
        String maHoKhau = maHoKhauCBox.getValue();
        if(maHoKhau.isEmpty()){
            ControllerUtil.showErrorMessage("Vui lòng chọn mã hộ khẩu trước");
            return;
        }
        if(phiSinhHoatCheckBox.isSelected()){
            float fee = MysqlConnector.getInstance().getLivingFeeThisMonth(maHoKhau, currentDate.getMonthValue(), currentYear);
            currentTotalFee += fee;
            totalFeeLabel.setText(String.valueOf(currentTotalFee));
        }
        else{
            float fee = MysqlConnector.getInstance().getLivingFeeThisMonth(maHoKhau, currentDate.getMonthValue(), currentYear);
            currentTotalFee -= fee;
            totalFeeLabel.setText(String.valueOf(currentTotalFee));
        }
    }


    private void loadData() {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        soTienThanhToanCol.setCellValueFactory(new PropertyValueFactory<>("soTienThanhToan"));
        ngayThanhToanCol.setCellValueFactory(new PropertyValueFactory<>("ngayThanhToan"));
        paymentList = MysqlConnector.getInstance().getThanhToanData();
        paymentTableView.setItems(paymentList);
    }
    
    private void initializeSearchbar(){
        FilteredList<ThanhToanModel> filteredData = new FilteredList<>(paymentList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ThanhToanModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                return ThanhToanModel.getMaHoKhau().toLowerCase().contains(searchWord)
                || String.valueOf(ThanhToanModel.getSoTienThanhToan()).contains(searchWord)
                || dateFormat.format(ThanhToanModel.getNgayThanhToan()).contains(searchWord);
            });
        });
        SortedList<ThanhToanModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(paymentTableView.comparatorProperty());
        paymentTableView.setItems(sortedData);
    }
    
    private boolean isValidInput(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return false;  // Nếu là null hoặc rỗng
        }

        // Kiểm tra định dạng
        if (!inputText.matches("(\\d+|\\d+-\\d+)")) {
            return false;  // Nếu không khớp định dạng
        }

        // Kiểm tra số tháng nếu có định dạng int-int
        if (inputText.contains("-")) {
            String[] months = inputText.split("-");
            try {
                int startMonth = Integer.parseInt(months[0]);
                int endMonth = Integer.parseInt(months[1]);

                if (startMonth < 1 || endMonth < startMonth || endMonth > 12) {
                    return false;  // Nếu tháng không hợp lệ
                }
            } catch (NumberFormatException e) {
                return false;  
            }
        } else {
            // Nếu chỉ có một số, kiểm tra nó là số nguyên từ 1 đến 12
            try {
                int singleMonth = Integer.parseInt(inputText);
                if (singleMonth < 1 || singleMonth > 12) {
                    return false;  // Nếu số tháng không hợp lệ
                }
            } catch (NumberFormatException e) {
                return false; 
            }
        }

        return true;  // Nếu không có vấn đề, trả về true
    }
    
    private void initializeCheckBoxAndTextPair(CheckBox checkBox, TextField textField, String maHoKhau, String tenPhi) {
        checkBox.setOnAction(event -> handleCheckBox(checkBox, textField));
        textField.textProperty().addListener((observable, oldValue, newValue) -> handleText(checkBox, maHoKhau, tenPhi ,newValue));
    }
    
    private void handleCheckBox(CheckBox checkBox, TextField textField) {
        if (checkBox.isSelected()) {
            String soThang = String.valueOf(currentDate.getMonthValue()); // Giá trị mặc định nếu chỉ thanh toán 1 tháng
            textField.setText(soThang);
        } else {
            textField.clear();
        }
    }
    
    private void handleText(CheckBox checkBox, String maHoKhau, String tenPhi, String newValue) {
        if(newValue.isEmpty()){
            checkBox.setSelected(false);
            updateTotalFeeLabel(tenPhi, maHoKhau, "");
        }
        else{
            checkBox.setSelected(true);
            updateTotalFeeLabel(tenPhi, maHoKhau, newValue);
        }
    }
     
    private void updateTotalFeeLabel(String tenPhi, String maHoKhau, String soThang) {
        float fee = MysqlConnector.getInstance().getTienNopMoiThangData(tenPhi, maHoKhau, currentYear);
        if (soThang.matches("\\d+")) {
            count++;
            totalMonth = 1;
            currentTotalFee += fee * totalMonth;
            totalFeeLabel.setText(String.valueOf(currentTotalFee));
        } else if (soThang.matches("\\d+-\\d+")) {
            String[] months = soThang.split("-");
            try {
                int startMonth = Integer.parseInt(months[0]);
                int endMonth = Integer.parseInt(months[1]);
                if(endMonth <= startMonth){
                    return;
                }
                count++;
                totalMonth = endMonth - startMonth + 1;
                currentTotalFee += fee * totalMonth;
                totalFeeLabel.setText(String.valueOf(currentTotalFee));

            } catch (NumberFormatException e) {
                
            }
        }
        else if(count > 0){
            count--;
            currentTotalFee -= fee * totalMonth;
            totalFeeLabel.setText(String.valueOf(currentTotalFee));
        }
           
    }

}
