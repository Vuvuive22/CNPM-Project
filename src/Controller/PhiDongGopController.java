package Controller;

import Model.DSPhiDongGop;
import Model.MysqlConnector;
import Model.PhiDongGopModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class PhiDongGopController {

    @FXML
    private TableView<PhiDongGopModel> feeTableView;

    @FXML
    private TableView<DSPhiDongGop> listFeeTableView;

    @FXML
    private TableColumn<PhiDongGopModel, String> maHoKhauCol;

    @FXML
    private TableColumn<PhiDongGopModel, Date> ngayDongGopCol;

    @FXML
    private TextArea noteText;

    @FXML
    private TextField searchbar;

    @FXML
    private TableColumn<PhiDongGopModel, Float> soTienCol;

    @FXML
    private TableColumn<DSPhiDongGop, Float> soTienGoiYCol;

    @FXML
    private TextField soTienGoiYText;

    @FXML
    private TableColumn<PhiDongGopModel, String> tenPhiCol;
    
    @FXML
    private TableColumn<DSPhiDongGop, String> tenPhi1Col;

    @FXML
    private TextField tenPhiText;

    private ObservableList<PhiDongGopModel> feeList;   //List số tiền đóng góp mỗi hộ khảu
    
    private ObservableList<DSPhiDongGop> list;   //List danh sách các loại phí đóng góp
    
    @FXML
    public void initialize(){
        loadData();
        initializeSearchbar();
        loadNoteDataFromFile();
        
        noteText.textProperty().addListener((observable, oldValue, newValue) -> {
            saveNoteDataToFile(newValue);
        });
    }
    
    @FXML
    public void addFeeOnAction(ActionEvent event) {
        String tenPhi = tenPhiText.getText();
        Float soTienGoiY = Float.valueOf(soTienGoiYText.getText());
        if(ControllerUtil.isEmptyOrNull(tenPhi) || ControllerUtil.isEmptyOrNull(String.valueOf(soTienGoiY))){
            ControllerUtil.showErrorMessage("Vui lòng nhập đầy đủ các trường trong form thêm khoản phí đóng góp!");
            return;
        }
        if(soTienGoiY < 0){
            ControllerUtil.showErrorMessage("Số tiền gợi ý phải >= 0. Vui lòng nhập lại!");
            return;
        }
                
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận thêm khoản phí đóng góp mới", "Bạn có chắc chắn muốn thêm " + tenPhi  + " vào danh sách phí đóng góp không ?");
        if(confirmed){
            DSPhiDongGop fee = new DSPhiDongGop(tenPhi, soTienGoiY);
            MysqlConnector.getInstance().addDSPhiDongGopData(fee);
            ControllerUtil.showSuccessAlert("Thêm khoản thu phí đóng góp mới thành công!");
            list.add(fee);
            listFeeTableView.refresh();
            tenPhiText.clear();
            soTienGoiYText.clear();
        }
    }

    @FXML
    public void deleteFeeOnAction(ActionEvent event) {
        DSPhiDongGop fee = listFeeTableView.getSelectionModel().getSelectedItem();
        if(fee == null){
            ControllerUtil.showErrorMessage("Vui lòng chọn loại phí đóng góp muốn xóa!");
            return;
        }
        boolean confirmed = ControllerUtil.showConfirmationDialog("Xác nhận xóa 1 khoản phí đóng góp", "Bạn có chắc chắn muốn xóa phí đóng góp này không ?");
        if(confirmed){
            MysqlConnector.getInstance().deleteDSPhiDongGopData(fee.getTenPhi());
            list.remove(fee);
            ControllerUtil.showSuccessAlert("Xóa phí thành công!");
            listFeeTableView.refresh();
        }
    }
    
    private void loadData() {
        maHoKhauCol.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        tenPhiCol.setCellValueFactory(new PropertyValueFactory<>("tenPhi"));
        soTienCol.setCellValueFactory(new PropertyValueFactory<>("soTien"));
        ngayDongGopCol.setCellValueFactory(new PropertyValueFactory<>("ngayDongGop"));
        feeList = MysqlConnector.getInstance().getPhiDongGopData();
        feeTableView.setItems(feeList);
        
        tenPhi1Col.setCellValueFactory(new PropertyValueFactory<>("tenPhi"));
        soTienGoiYCol.setCellValueFactory(new PropertyValueFactory<>("soTienGoiY"));
        list = MysqlConnector.getInstance().getDSPhiDongGopData();
        listFeeTableView.setItems(list);
        
    }
    
    private void initializeSearchbar(){
        FilteredList<PhiDongGopModel> filteredData = new FilteredList<>(feeList, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(PhiDongGopModel -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                return PhiDongGopModel.getMaHoKhau().toLowerCase().contains(searchWord)
                || PhiDongGopModel.getTenPhi().toLowerCase().contains(searchWord)
                || dateFormat.format(PhiDongGopModel.getNgayDongGop()).contains(searchWord)
                || String.valueOf(PhiDongGopModel.getSoTien()).contains(searchWord);
            });
        });
        SortedList<PhiDongGopModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(feeTableView.comparatorProperty());
        feeTableView.setItems(sortedData);
        
        
        FilteredList<DSPhiDongGop> filteredData1 = new FilteredList<>(list, b -> true);
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData1.setPredicate(DSPhiDongGop -> {
                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }
                String searchWord = newValue.toLowerCase();
                
                return DSPhiDongGop.getTenPhi().toLowerCase().contains(searchWord)
                || String.valueOf(DSPhiDongGop.getSoTienGoiY()).contains(searchWord);
            });
        });
        SortedList<DSPhiDongGop> sortedData1 = new SortedList<>(filteredData1);
        sortedData1.comparatorProperty().bind(listFeeTableView.comparatorProperty());
        listFeeTableView.setItems(sortedData1);
    }
    
    private void saveNoteDataToFile(String data) {
        URL resourceUrl = getClass().getResource("/Database");
        
        if (resourceUrl != null) {
            try {
                Path databasePath = Paths.get(resourceUrl.toURI());
                Path noteFilePath = databasePath.resolve("note.txt");
                Files.write(noteFilePath, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: Unable to get path to Database folder.");
        }
    }

    private void loadNoteDataFromFile() {
        try {
            URL resourceUrl = getClass().getResource("/Database/note.txt");
            
            if (resourceUrl != null) {
                Path noteFilePath = Paths.get(resourceUrl.toURI());
                String data = new String(Files.readAllBytes(noteFilePath));
                noteText.setText(data);
            } else {
                System.err.println("Error: Unable to get path to note.txt file.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
