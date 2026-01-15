package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/qlchungcu";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        setup();
    }

    public static void setup() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = conn.createStatement()) {

            System.out.println("Updating Database Schema...");

            // 1. Update Residents (NhanKhau) - Add Mapping Config
            try {
                stmt.executeUpdate("ALTER TABLE nhankhau ADD COLUMN UserName varchar(30) UNIQUE DEFAULT NULL");
                System.out.println("Added UserName to nhankhau.");
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate column")) {
                    e.printStackTrace();
                } else {
                    System.out.println("Column UserName already exists in nhankhau.");
                }
            }

            try {
                stmt.executeUpdate(
                        "ALTER TABLE nhankhau ADD CONSTRAINT FK_NhanKhau_User FOREIGN KEY (UserName) REFERENCES user(UserName) ON DELETE SET NULL");
                System.out.println("Added Foreign Key FK_NhanKhau_User.");
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate")) {
                    // Start ignore
                }
            }

            // 2. Population Movements
            String sqlMovements = "CREATE TABLE IF NOT EXISTS population_movements (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "apt_id varchar(10), " + // Maps to MaHoKhau
                    "resident_id varchar(15), " + // Maps to SoCMND_CCCD
                    "type ENUM('move_in', 'move_out', 'absent', 'stay'), " +
                    "status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "approved_by varchar(30), " +
                    "FOREIGN KEY (apt_id) REFERENCES hokhau(MaHoKhau), " +
                    "FOREIGN KEY (resident_id) REFERENCES nhankhau(SoCMND_CCCD) " +
                    ")";
            stmt.executeUpdate(sqlMovements);
            System.out.println("Table population_movements checked/created.");

            // 3. Vehicles
            String sqlVehicles = "CREATE TABLE IF NOT EXISTS vehicles (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "apt_id varchar(10), " +
                    "plate_number varchar(20), " +
                    "type varchar(20), " + // motorbike, car, bicycle
                    "monthly_fee FLOAT, " +
                    "status varchar(20) DEFAULT 'active', " +
                    "FOREIGN KEY (apt_id) REFERENCES hokhau(MaHoKhau)" +
                    ")";
            stmt.executeUpdate(sqlVehicles);
            System.out.println("Table vehicles checked/created.");

            // 4. Bill Configurations
            String sqlBillConfigs = "CREATE TABLE IF NOT EXISTS bill_configurations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "period varchar(10), " + // YYYY-MM
                    "services_json TEXT, " + // JSON string of service prices
                    "unique (period)" +
                    ")";
            stmt.executeUpdate(sqlBillConfigs);
            System.out.println("Table bill_configurations checked/created.");

            // 5. Bills
            String sqlBills = "CREATE TABLE IF NOT EXISTS bills (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "apt_id varchar(10), " +
                    "period varchar(10), " +
                    "service_fee FLOAT DEFAULT 0, " +
                    "vehicle_fee FLOAT DEFAULT 0, " +
                    "pre_debt FLOAT DEFAULT 0, " +
                    "total FLOAT DEFAULT 0, " +
                    "paid BOOLEAN DEFAULT FALSE, " +
                    "paid_at TIMESTAMP NULL, " +
                    "details_json TEXT, " +
                    "FOREIGN KEY (apt_id) REFERENCES hokhau(MaHoKhau)" +
                    ")";
            stmt.executeUpdate(sqlBills);
            System.out.println("Table bills checked/created.");

            // 6. Payments
            String sqlPayments = "CREATE TABLE IF NOT EXISTS payments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "bill_id INT, " +
                    "amount FLOAT, " +
                    "paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (bill_id) REFERENCES bills(id)" +
                    ")";
            stmt.executeUpdate(sqlPayments);
            System.out.println("Table payments checked/created.");

            System.out.println("Database Setup Completed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
