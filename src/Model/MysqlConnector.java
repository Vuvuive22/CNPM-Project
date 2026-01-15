
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

//Using Singleton Pattern to optimize this class
public class MysqlConnector {
    private static MysqlConnector instance = null;
    private Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/qlchungcu";
    private final String userName = "root";
    private final String password = "";

    private MysqlConnector() {
        // Private constructor to prevent external instantiation.
    }

    public static MysqlConnector getInstance() {
        if (instance == null) {
            instance = new MysqlConnector();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, userName, password);
        }
        return connection;
    }

    public void closeDB() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    // User data
    public String getPwData(String username) {
        String pw = null;
        try {
            String query = "SELECT Password FROM user WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pw = rs.getString("Password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pw;
    }

    public void changePwData(String username, String newPw) {
        try {
            String query = "UPDATE user SET Password = ? WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newPw);
            ps.setString(2, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getHoTenData(String username) {
        String hoTen = null;
        try {
            String query = "SELECT HoTen FROM user WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hoTen = rs.getString("HoTen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hoTen;
    }

    public String getEmailData(String username) {
        String email = null;
        try {
            String query = "SELECT Email FROM user WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("Email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    public String getSoDTData(String username) {
        String soDT = null;
        try {
            String query = "SELECT SoDT FROM user WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                soDT = rs.getString("SoDT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soDT;
    }

    public String getDiaChiData(String username) {
        String diaChi = null;
        try {
            String query = "SELECT DiaChi FROM user WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                diaChi = rs.getString("DiaChi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diaChi;
    }

    public int getTuoiData(String username) {
        int tuoi = -1; // Giả sử tuổi không bao giờ là giá trị âm
        try {
            String query = "SELECT Tuoi FROM user WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tuoi = rs.getInt("Tuoi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tuoi;
    }

    public void changeHoTenData(String username, String newHoTen) {
        try {
            String query = "UPDATE user SET HoTen = ? WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newHoTen);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeEmailData(String username, String newEmail) {
        try {
            String query = "UPDATE user SET Email = ? WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newEmail);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeSoDTData(String username, String newSoDT) {
        try {
            String query = "UPDATE user SET SoDT = ? WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newSoDT);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeDiaChiData(String username, String newDiaChi) {
        try {
            String query = "UPDATE user SET DiaChi = ? WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newDiaChi);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeTuoiData(String username, int newTuoi) {
        try {
            String query = "UPDATE user SET Tuoi = ? WHERE Username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, newTuoi);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Nhân khẩu data

    public ObservableList<NhanKhauModel> getNhanKhauData() {
        ObservableList<NhanKhauModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from NhanKhau");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new NhanKhauModel(
                        rs.getString("MaHoKhau"),
                        rs.getString("HoTen"),
                        Integer.parseInt(rs.getString("Tuoi")),
                        rs.getString("GioiTinh"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("SoDT"),
                        rs.getString("QuanHe"),
                        Integer.parseInt(rs.getString("TamVang")) == 1,
                        Integer.parseInt(rs.getString("TamTru")) == 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getNumberOfNhanKhau() {
        int numberOfNhanKhau = 0;
        try {
            String query = "SELECT COUNT(*) AS count FROM NhanKhau";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                numberOfNhanKhau = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfNhanKhau;
    }

    public void addNhanKhauData(NhanKhauModel nhanKhau) {
        try {
            String query = "INSERT INTO NhanKhau (MaHoKhau, HoTen, Tuoi, GioiTinh, SoCMND_CCCD, SoDT, QuanHe, TamVang, TamTru) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nhanKhau.getMaHoKhau());
            ps.setString(2, nhanKhau.getHoTen());
            ps.setInt(3, nhanKhau.getTuoi());
            ps.setString(4, nhanKhau.getGioiTinh());
            ps.setString(5, nhanKhau.getCCCD());
            ps.setString(6, nhanKhau.getSoDT());
            ps.setString(7, nhanKhau.getQuanHe());
            ps.setInt(8, 0);
            ps.setInt(9, 0);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNhanKhauData(NhanKhauModel updatedNhanKhau) {
        try {
            String query = "UPDATE NhanKhau SET MaHoKhau = ?, HoTen = ?, Tuoi = ?, GioiTinh = ?, SoDT = ?, QuanHe = ?, TamVang = ?, TamTru = ? WHERE SoCMND_CCCD = ?";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, updatedNhanKhau.getMaHoKhau());
            ps.setString(2, updatedNhanKhau.getHoTen());
            ps.setInt(3, updatedNhanKhau.getTuoi());
            ps.setString(4, updatedNhanKhau.getGioiTinh());
            ps.setString(5, updatedNhanKhau.getSoDT());
            ps.setString(6, updatedNhanKhau.getQuanHe());
            ps.setInt(7, 0);
            ps.setInt(8, 0);
            ps.setString(9, updatedNhanKhau.getCCCD());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNhanKhauData(String soCCCD) {
        try {
            String query = "DELETE FROM NhanKhau WHERE SoCMND_CCCD = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, soCCCD);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<TamTruModel> getTamTruData() {
        ObservableList<TamTruModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM TamTru");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TamTruModel(
                        rs.getString("MaTamTru"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("LyDo"),
                        rs.getObject("TuNgay", LocalDate.class),
                        rs.getObject("DenNgay", LocalDate.class)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<TamVangModel> getTamVangData() {
        ObservableList<TamVangModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM TamVang");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TamVangModel(
                        rs.getString("MaTamVang"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("NoiTamTru"),
                        rs.getObject("TuNgay", LocalDate.class),
                        rs.getObject("DenNgay", LocalDate.class)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addTamTruData(TamTruModel tamTru) {
        try {
            String query = "INSERT INTO TamTru (MaTamTru, SoCMND_CCCD, LyDo, TuNgay, DenNgay) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, tamTru.getMaTamTru());
            ps.setString(2, tamTru.getSoCCCD());
            ps.setString(3, tamTru.getLyDo());
            ps.setObject(4, tamTru.getTuNgay());
            ps.setObject(5, tamTru.getDenNgay());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTamVangData(TamVangModel tamVang) {
        try {
            String query = "INSERT INTO TamVang (MaTamVang, SoCMND_CCCD, NoiTamTru, TuNgay, DenNgay) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, tamVang.getMaTamVang());
            ps.setString(2, tamVang.getSoCCCD());
            ps.setString(3, tamVang.getNoiTamTru());
            ps.setObject(4, tamVang.getTuNgay());
            ps.setObject(5, tamVang.getDenNgay());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTamTruData(String maTamTru) {
        try {
            String query = "DELETE FROM TamTru WHERE MaTamTru = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maTamTru);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTamVangData(String maTamVang) {
        try {
            String query = "DELETE FROM TamVang WHERE MaTamVang = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maTamVang);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hộ khẩu data
    public ObservableList<HoKhauModel> getHoKhauData() {
        ObservableList<HoKhauModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM HoKhau");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HoKhauModel(
                        rs.getString("MaHoKhau"),
                        rs.getString("DiaChi"),
                        rs.getObject("NgayLap", LocalDate.class),
                        rs.getObject("NgayChuyenDi", LocalDate.class),
                        rs.getString("LyDoChuyen")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<String> getMaHoKhauData() {
        ObservableList<HoKhauModel> hoKhauList = getHoKhauData();
        ObservableList<String> maHoKhauList = FXCollections.observableArrayList();

        for (HoKhauModel hoKhau : hoKhauList) {
            maHoKhauList.add(hoKhau.getMaHoKhau());
        }

        return maHoKhauList;
    }

    public int getNumberOfHoKhau() {
        int numberOfHoKhau = 0;
        try {
            String query = "SELECT COUNT(*) AS count FROM HoKhau";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                numberOfHoKhau = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfHoKhau;
    }

    public void addHoKhauData(HoKhauModel newHoKhau) { // Có trigger để thêm dữ liệu các bảng khác rồi
        try {
            String query = "INSERT INTO HoKhau (MaHoKhau, DiaChi, NgayLap, NgayChuyenDi, LyDoChuyen) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, newHoKhau.getMaHoKhau());
            ps.setString(2, newHoKhau.getDiaChi());
            ps.setObject(3, newHoKhau.getNgayLap());
            ps.setObject(4, newHoKhau.getNgayChuyenDi());
            ps.setString(5, newHoKhau.getLyDoChuyen());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHoKhauData(HoKhauModel updatedHoKhau) {
        try {
            String query = "UPDATE HoKhau SET DiaChi = ?, NgayLap = ?, NgayChuyenDi = ?, LyDoChuyen = ? WHERE MaHoKhau = ?";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, updatedHoKhau.getDiaChi());
            ps.setObject(2, updatedHoKhau.getNgayLap());
            ps.setObject(3, updatedHoKhau.getNgayChuyenDi());
            ps.setString(4, updatedHoKhau.getLyDoChuyen());
            ps.setString(5, updatedHoKhau.getMaHoKhau());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHoKhauData(String maHoKhau) { // Có trigger để xóa dữ liệu các bảng khác rồi
        try {
            String query = "DELETE FROM HoKhau WHERE MaHoKhau = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, maHoKhau);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // fee data

    // Check and create data for new year if not exists
    public void checkAndCreateFeeData(int year) {
        try {
            // 1. Check PhiDichVu if completely empty for that year
            PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM PhiDichVu WHERE Nam = ? LIMIT 1");
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                // Data missing entirely, generate for all tables
                generateDataForYear(year);
            } else {
                // Check if any households are missing
                ensureAllHouseholdsHaveFeeData(year);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cleanDuplicateData(int year) {
        String[] tables = { "PhiDichVu", "PhiQuanLy", "PhiGuiXe", "PhiSinhHoat" };
        for (String table : tables) {
            try {
                // Find duplicates
                String query = "SELECT MaHoKhau, COUNT(*) c FROM " + table
                        + " WHERE Nam = ? GROUP BY MaHoKhau HAVING c > 1";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, year);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String maHoKhau = rs.getString("MaHoKhau");
                    int count = rs.getInt("c");
                    // Delete duplicates, keep 1
                    // Since we can't distinguish, we delete (count - 1) records
                    String deleteQuery = "DELETE FROM " + table + " WHERE MaHoKhau = ? AND Nam = ? LIMIT ?";
                    PreparedStatement psDelete = connection.prepareStatement(deleteQuery);
                    psDelete.setString(1, maHoKhau);
                    psDelete.setInt(2, year);
                    psDelete.setInt(3, count - 1);
                    psDelete.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void ensureAllHouseholdsHaveFeeData(int year) {
        // Clean up duplicates first
        cleanDuplicateData(year);

        try {
            // Get prices from previous year or current defaults
            float giaPhiDichVu = getGiaPhiData("PhiDichVu", year);
            if (giaPhiDichVu == 0)
                giaPhiDichVu = getGiaPhiData("PhiDichVu", year - 1);
            if (giaPhiDichVu == 0)
                giaPhiDichVu = 16500;

            float giaPhiQuanLy = getGiaPhiData("PhiQuanLy", year);
            if (giaPhiQuanLy == 0)
                giaPhiQuanLy = getGiaPhiData("PhiQuanLy", year - 1);
            if (giaPhiQuanLy == 0)
                giaPhiQuanLy = 7000;

            float giaXeMay = getFeePerVehicleData("GiaXeMay", year);
            if (giaXeMay == 0)
                giaXeMay = getFeePerVehicleData("GiaXeMay", year - 1);
            if (giaXeMay == 0)
                giaXeMay = 70000;

            float giaOTo = getFeePerVehicleData("GiaOTo", year);
            if (giaOTo == 0)
                giaOTo = getFeePerVehicleData("GiaOTo", year - 1);
            if (giaOTo == 0)
                giaOTo = 1200000;

            float giaXeDap = getFeePerVehicleData("GiaXeDap", year);
            if (giaXeDap == 0)
                giaXeDap = getFeePerVehicleData("GiaXeDap", year - 1);
            if (giaXeDap == 0)
                giaXeDap = 50000;

            ObservableList<HoKhauModel> areaList = getDienTichHoData();
            ObservableList<HoKhauModel> vehicleList = getVehicleData();

            for (HoKhauModel hk : areaList) {
                String maHoKhau = hk.getMaHoKhau();
                float area = hk.getDienTichHo();

                // PhiDichVu
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO PhiDichVu (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) SELECT ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM PhiDichVu WHERE MaHoKhau = ? AND Nam = ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaPhiDichVu);
                ps.setFloat(3, giaPhiDichVu * area);
                ps.setInt(4, year);
                ps.setString(5, maHoKhau);
                ps.setInt(6, year);
                ps.executeUpdate();

                // PhiQuanLy
                ps = connection.prepareStatement(
                        "INSERT INTO PhiQuanLy (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) SELECT ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM PhiQuanLy WHERE MaHoKhau = ? AND Nam = ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaPhiQuanLy);
                ps.setFloat(3, giaPhiQuanLy * area);
                ps.setInt(4, year);
                ps.setString(5, maHoKhau);
                ps.setInt(6, year);
                ps.executeUpdate();

                // PhiSinhHoat
                ps = connection.prepareStatement(
                        "INSERT INTO PhiSinhHoat (MaHoKhau, Nam) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM PhiSinhHoat WHERE MaHoKhau = ? AND Nam = ?)");
                ps.setString(1, maHoKhau);
                ps.setInt(2, year);
                ps.setString(3, maHoKhau);
                ps.setInt(4, year);
                ps.executeUpdate();
            }

            // PhiGuiXe
            for (HoKhauModel hk : vehicleList) {
                String maHoKhau = hk.getMaHoKhau();
                float totalFee = hk.getSoXeMay() * giaXeMay + hk.getSoOTo() * giaOTo + hk.getSoXeDap() * giaXeDap;

                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO PhiGuiXe (MaHoKhau, GiaXeMay, GiaOTo, GiaXeDap, TienNopMoiThang, Nam) SELECT ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM PhiGuiXe WHERE MaHoKhau = ? AND Nam = ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaXeMay);
                ps.setFloat(3, giaOTo);
                ps.setFloat(4, giaXeDap);
                ps.setFloat(5, totalFee);
                ps.setInt(6, year);
                ps.setString(7, maHoKhau);
                ps.setInt(8, year);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateDataForYear(int year) {
        System.out.println("Generating data for year: " + year);
        try {
            // Get prices from previous year or default
            float giaPhiDichVu = getGiaPhiData("PhiDichVu", year - 1);
            if (giaPhiDichVu == 0)
                giaPhiDichVu = 16500; // Default fallback

            float giaPhiQuanLy = getGiaPhiData("PhiQuanLy", year - 1);
            if (giaPhiQuanLy == 0)
                giaPhiQuanLy = 7000; // Default fallback

            float giaXeMay = getFeePerVehicleData("GiaXeMay", year - 1);
            if (giaXeMay == 0)
                giaXeMay = 70000;
            float giaOTo = getFeePerVehicleData("GiaOTo", year - 1);
            if (giaOTo == 0)
                giaOTo = 1200000;
            float giaXeDap = getFeePerVehicleData("GiaXeDap", year - 1);
            if (giaXeDap == 0)
                giaXeDap = 50000;

            ObservableList<HoKhauModel> hoKhauList = getHoKhauData();
            ObservableList<HoKhauModel> vehicleList = getVehicleData(); // Contains updated vehicle counts

            for (HoKhauModel hk : hoKhauList) {
                String maHoKhau = hk.getMaHoKhau();

                // PhiDichVu
                // Check redundancy for safety although we checked existence above
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT IGNORE INTO PhiDichVu (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) VALUES (?, ?, ?, ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaPhiDichVu);
                // Need Area for calculation. hoKhauList from getHoKhauData() doesn't have Area
                // populated?
                // getHoKhauData() query: SELECT * FROM HoKhau. table has `dienTichHo`.
                // HoKhauModel constructor in getHoKhauData() does NOT take area?
                // Let's check HoKhauModel... Assuming we might need to fetch area separately or
                // use a join.
                // Actually getDienTichHoData() returns area. Let's use that.
            }

            // Re-fetching Area Map
            ObservableList<HoKhauModel> areaList = getDienTichHoData();

            for (HoKhauModel hk : areaList) {
                String maHoKhau = hk.getMaHoKhau();
                float area = hk.getDienTichHo();

                // PhiDichVu
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT IGNORE INTO PhiDichVu (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) VALUES (?, ?, ?, ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaPhiDichVu);
                ps.setFloat(3, giaPhiDichVu * area);
                ps.setInt(4, year);
                ps.executeUpdate();

                // PhiQuanLy
                ps = connection.prepareStatement(
                        "INSERT IGNORE INTO PhiQuanLy (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) VALUES (?, ?, ?, ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaPhiQuanLy);
                ps.setFloat(3, giaPhiQuanLy * area);
                ps.setInt(4, year);
                ps.executeUpdate();
            }

            // PhiGuiXe
            for (HoKhauModel hk : vehicleList) {
                String maHoKhau = hk.getMaHoKhau();
                float totalFee = hk.getSoXeMay() * giaXeMay + hk.getSoOTo() * giaOTo + hk.getSoXeDap() * giaXeDap;

                PreparedStatement ps = connection.prepareStatement(
                        "INSERT IGNORE INTO PhiGuiXe (MaHoKhau, GiaXeMay, GiaOTo, GiaXeDap, TienNopMoiThang, Nam) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, maHoKhau);
                ps.setFloat(2, giaXeMay);
                ps.setFloat(3, giaOTo);
                ps.setFloat(4, giaXeDap);
                ps.setFloat(5, totalFee);
                ps.setInt(6, year);
                ps.executeUpdate();
            }

            // PhiSinhHoat
            for (HoKhauModel hk : areaList) {
                String maHoKhau = hk.getMaHoKhau();
                PreparedStatement ps = connection
                        .prepareStatement("INSERT IGNORE INTO PhiSinhHoat (MaHoKhau, Nam) VALUES (?, ?)");
                ps.setString(1, maHoKhau);
                ps.setInt(2, year);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<PhiCoDinhModel> getFeeData(String tenPhi, int nam) {
        checkAndCreateFeeData(nam);
        ObservableList<PhiCoDinhModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tenPhi + " WHERE Nam = ?");
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PhiCoDinhModel(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("TienNopMoiThang"),
                        rs.getFloat("Thang1"),
                        rs.getFloat("Thang2"),
                        rs.getFloat("Thang3"),
                        rs.getFloat("Thang4"),
                        rs.getFloat("Thang5"),
                        rs.getFloat("Thang6"),
                        rs.getFloat("Thang7"),
                        rs.getFloat("Thang8"),
                        rs.getFloat("Thang9"),
                        rs.getFloat("Thang10"),
                        rs.getFloat("Thang11"),
                        rs.getFloat("Thang12")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public float getGiaPhiData(String tenPhi, int nam) {
        float giaPhi = 0.0f;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT GiaPhi FROM " + tenPhi + " WHERE Nam = ?");
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                giaPhi = rs.getFloat("GiaPhi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return giaPhi;
    }

    public void changeFeeData(String tenPhi, float newFee, int nam) {
        try {
            ObservableList<HoKhauModel> model = getDienTichHoData();
            for (HoKhauModel hoKhau : model) {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE " + tenPhi + " SET GiaPhi = ?, TienNopMoiThang = ? WHERE MaHoKhau = ? and Nam >= ?");
                ps.setFloat(1, newFee);
                ps.setFloat(2, newFee * hoKhau.getDienTichHo());
                ps.setString(3, hoKhau.getMaHoKhau());
                ps.setInt(4, nam);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<HoKhauModel> getDienTichHoData() {
        ObservableList<HoKhauModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MaHoKhau, dienTichHo FROM Hokhau");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HoKhauModel(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("dienTichHo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void changeDienTichHoData(String maHoKhau, float newDienTich, int nam) {
        try {
            // Update Area in HoKhau table
            PreparedStatement ps = connection.prepareStatement("UPDATE Hokhau SET dienTichHo = ? WHERE MaHoKhau = ?");
            ps.setFloat(1, newDienTich);
            ps.setString(2, maHoKhau);
            ps.executeUpdate();

            // Update PhiDichVu: Recalculate monthly fee based on its own unit price
            // (GiaPhi)
            ps = connection.prepareStatement(
                    "UPDATE PhiDichVu SET TienNopMoiThang = GiaPhi * ? WHERE MaHoKhau = ? and Nam >= ?");
            ps.setFloat(1, newDienTich);
            ps.setString(2, maHoKhau);
            ps.setInt(3, nam);
            ps.executeUpdate();

            // Update PhiQuanLy: Recalculate monthly fee based on its own unit price
            // (GiaPhi)
            ps = connection.prepareStatement(
                    "UPDATE PhiQuanLy SET TienNopMoiThang = GiaPhi * ? WHERE MaHoKhau = ? and Nam >= ?");
            ps.setFloat(1, newDienTich);
            ps.setString(2, maHoKhau);
            ps.setInt(3, nam);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<HoKhauModel> getVehicleData() {
        ObservableList<HoKhauModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT MaHoKhau, SoXeMay, SoOTo, SoXeDap FROM Hokhau");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HoKhauModel(
                        rs.getString("MaHoKhau"),
                        rs.getInt("SoXeMay"),
                        rs.getInt("SoOTo"),
                        rs.getInt("SoXeDap")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public float getFeePerVehicleData(String feeName, int nam) {
        float fee = 0.0f;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT " + feeName + " FROM PhiGuiXe WHERE Nam = ?");
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fee = rs.getFloat(feeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fee;
    }

    public void changeVehicleData(String maHoKhau, int soXeMay, int soOTo, int soXeDap, int nam) {
        float fee1 = getFeePerVehicleData("GiaXeMay", nam);
        float fee2 = getFeePerVehicleData("GiaOTo", nam);
        float fee3 = getFeePerVehicleData("GiaXeDap", nam);
        try {
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE HoKhau SET SoXeMay = ?, SoOTo = ?, SoXeDap = ? WHERE MaHoKhau = ?");
            ps.setInt(1, soXeMay);
            ps.setInt(2, soOTo);
            ps.setInt(3, soXeDap);
            ps.setString(4, maHoKhau);
            ps.executeUpdate();

            ps = connection.prepareStatement("UPDATE PhiGuiXe SET TienNopMoiThang = ? WHERE MaHoKhau = ? and Nam >= ?");
            ps.setFloat(1, fee1 * soXeMay + fee2 * soOTo + fee3 * soXeDap);
            ps.setString(2, maHoKhau);
            ps.setInt(3, nam);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeFeePerVehicleData(float giaXeMay, float giaOTo, float giaXeDap, int nam) {
        ObservableList<HoKhauModel> list = getVehicleData();
        for (HoKhauModel hoKhau : list) {
            String maHoKhau = hoKhau.getMaHoKhau();
            int soXeMay = hoKhau.getSoXeMay();
            int soXeDap = hoKhau.getSoXeDap();
            int soOTo = hoKhau.getSoOTo();
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE PhiGuiXe SET GiaXeMay = ?, GiaOTo = ?, GiaXeDap = ?, TienNopMoiThang = ? WHERE MaHoKhau = ? and nam >= ?");
                ps.setFloat(1, giaXeMay);
                ps.setFloat(2, giaOTo);
                ps.setFloat(3, giaXeDap);
                ps.setFloat(4, giaXeMay * soXeMay + giaXeDap * soXeDap + giaOTo * soOTo);
                ps.setString(5, maHoKhau);
                ps.setInt(6, nam);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<PhiDongGopModel> getPhiDongGopData() {
        ObservableList<PhiDongGopModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT MaHoKhau, TenPhi, SoTien, NgayDongGop FROM PhiDongGop");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PhiDongGopModel(
                        rs.getString("MaHoKhau"),
                        rs.getString("TenPhi"),
                        rs.getFloat("SoTien"),
                        rs.getDate("NgayDongGop").toLocalDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<DSPhiDongGop> getDSPhiDongGopData() {
        ObservableList<DSPhiDongGop> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT TenPhi, SoTienGoiY FROM DanhSachPhiDongGop");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DSPhiDongGop(
                        rs.getString("TenPhi"),
                        rs.getFloat("SoTienGoiY")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addPhiDongGopData(PhiDongGopModel phiDongGopModel) {
        try {
            String query = "INSERT INTO PhiDongGop (MaHoKhau, TenPhi, SoTien, NgayDongGop) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, phiDongGopModel.getMaHoKhau());
            ps.setString(2, phiDongGopModel.getTenPhi());
            ps.setFloat(3, phiDongGopModel.getSoTien());
            ps.setObject(4, phiDongGopModel.getNgayDongGop());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDSPhiDongGopData(DSPhiDongGop dsPhiDongGop) {
        try {
            String query = "INSERT INTO DanhSachPhiDongGop (TenPhi, SoTienGoiY) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, dsPhiDongGop.getTenPhi());
            ps.setFloat(2, dsPhiDongGop.getSoTienGoiY());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDSPhiDongGopData(String tenPhi) {
        try {
            String query = "DELETE FROM DanhSachPhiDongGop WHERE TenPhi = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, tenPhi);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<PhiSinhHoatModel> getPhiSinhHoatData(int nam) {
        checkAndCreateFeeData(nam);
        ObservableList<PhiSinhHoatModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM PhiSinhHoat WHERE Nam = ?");
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PhiSinhHoatModel(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("Thang1"),
                        rs.getFloat("Thang2"),
                        rs.getFloat("Thang3"),
                        rs.getFloat("Thang4"),
                        rs.getFloat("Thang5"),
                        rs.getFloat("Thang6"),
                        rs.getFloat("Thang7"),
                        rs.getFloat("Thang8"),
                        rs.getFloat("Thang9"),
                        rs.getFloat("Thang10"),
                        rs.getFloat("Thang11"),
                        rs.getFloat("Thang12")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<CapNhatPhiSinhHoat> getCapNhatPhiSinhHoatData(int month, int year) {
        ObservableList<CapNhatPhiSinhHoat> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT * FROM CapNhatPhiSinhHoat WHERE Thang = ? and Nam = ?");
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CapNhatPhiSinhHoat(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("TienDien"),
                        rs.getFloat("TienNuoc"),
                        rs.getFloat("TienInternet")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addCapNhatPhiSinhHoatData(CapNhatPhiSinhHoat fee, int month, int year) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO CapNhatPhiSinhHoat (MaHoKhau, TienDien, TienNuoc, TienInternet, Thang, Nam) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setString(1, fee.getMaHoKhau());
            ps.setFloat(2, fee.getTienDien());
            ps.setFloat(3, fee.getTienNuoc());
            ps.setFloat(4, fee.getTienInternet());
            ps.setInt(5, month);
            ps.setInt(6, year);
            ps.executeUpdate();

            // Update the total in PhiSinhHoat table
            updatePhiSinhHoatData(fee.getMaHoKhau(), month, year);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isaddCapNhatPhiSinhHoatValidated(String maHoKhau, int month, int year) { // Kiểm tra trường hợp 1 mã
                                                                                            // hộ khẩu không được phép
                                                                                            // có 2 dòng dữ liệu trong 1
                                                                                            // tháng
        try {
            String query = "SELECT COUNT(*) FROM CapNhatPhiSinhHoat WHERE MaHoKhau = ? AND Thang = ? AND Nam = ?";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, maHoKhau);
            ps.setInt(2, month);
            ps.setInt(3, year);

            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // payment data
    public ObservableList<ThanhToanModel> getThanhToanData() {
        ObservableList<ThanhToanModel> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ThanhToan");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ThanhToanModel(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("SoTienThanhToan"),
                        rs.getDate("NgayThanhToan").toLocalDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<String> getFeeNameData() { // Lấy tên các loại phí đóng góp
        ObservableList<DSPhiDongGop> feeList = getDSPhiDongGopData();
        ObservableList<String> feeNameList = FXCollections.observableArrayList();

        for (DSPhiDongGop fee : feeList) {
            feeNameList.add(fee.getTenPhi());
        }

        return feeNameList;
    }

    public void addThanhToanData(ThanhToanModel newThanhToan) {
        try {
            String query = "INSERT INTO ThanhToan (MaHoKhau, SoTienThanhToan, NgayThanhToan) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, newThanhToan.getMaHoKhau());
            ps.setFloat(2, newThanhToan.getSoTienThanhToan());
            ps.setObject(3, newThanhToan.getNgayThanhToan());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra xem 1 hộ khẩu đã nộp tenPhi chưa nếu chưa thì return true
    public boolean isLegalPayment(String tenPhi, String maHoKhau, int thang, int nam) {
        String columnName = "Thang" + thang;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT " + columnName + " FROM " + tenPhi + " WHERE MaHoKhau = ? AND Nam = ?");
            ps.setString(1, maHoKhau);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                float value = rs.getFloat(columnName);
                return (value == 0.0f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // trả về số tiền nộp mỗi tháng của Phí quản lý, phí dịch vụ, phí gửi xe 1 hộ
    // khẩu
    public float getTienNopMoiThangData(String tenPhi, String maHoKhau, int nam) {
        float tienNopMoiThang = 0.0f;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT TienNopMoiThang FROM " + tenPhi + " WHERE MaHoKhau = ? and Nam = ?");
            ps.setString(1, maHoKhau);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tienNopMoiThang = rs.getFloat("TienNopMoiThang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tienNopMoiThang;
    }

    // Kiểm tra xem 1 hộ khẩu đã được cập nhật phí sinh hoạt chưa, nếu chưa return
    // false
    public boolean isHavingLivingFee(String maHoKhau, int thang, int nam) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(*) AS Count FROM CapNhatPhiSinhHoat WHERE MaHoKhau = ? AND Thang = ? AND Nam = ?");
            ps.setString(1, maHoKhau);
            ps.setInt(2, thang);
            ps.setInt(3, nam);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("Count");
                return count > 0; // Nếu có ít nhất một bản ghi, có nghĩa là đã cập nhật phí sinh hoạt
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Trả về tiền phí sinh hoạt tháng này của 1 hộ khẩu
    public float getLivingFeeThisMonth(String maHoKhau, int thang, int nam) {
        float totalLivingFee = 0.0f;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT TienDien, TienNuoc, TienInternet FROM CapNhatPhiSinhHoat WHERE MaHoKhau = ? AND Thang = ? AND Nam = ?");
            ps.setString(1, maHoKhau);
            ps.setInt(2, thang);
            ps.setInt(3, nam);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                float tienDien = rs.getFloat("TienDien");
                float tienNuoc = rs.getFloat("TienNuoc");
                float tienInternet = rs.getFloat("TienInternet");

                // Tổng hợp các giá trị của TienDien, TienNuoc, TienInternet
                totalLivingFee = tienDien + tienNuoc + tienInternet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalLivingFee;
    }

    public void updateFeeData(String tenPhi, String maHoKhau, int thang, int nam) {
        try {
            String columnName = "Thang" + thang;
            String updateQuery = "UPDATE " + tenPhi + " SET " + columnName
                    + " = TienNopMoiThang WHERE MaHoKhau = ? AND Nam = ?";

            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ps.setString(1, maHoKhau);
            ps.setInt(2, nam);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePhiSinhHoatData(String maHoKhau, int thang, int nam) {
        try {
            String columnName = "Thang" + thang;
            String updateQuery = "UPDATE PhiSinhHoat SET " + columnName + " = ? WHERE MaHoKhau = ? AND Nam = ?";

            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ps.setFloat(1, getLivingFeeThisMonth(maHoKhau, thang, nam));
            ps.setString(2, maHoKhau);
            ps.setInt(3, nam);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}