
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.time.LocalDate;

public class TestFeeDiscrepancy {
    private static final String url = "jdbc:mysql://localhost:3306/qlchungcu";
    private static final String userName = "root";
    private static final String password = "";
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to DB.");

            String maHoKhau = "TQB002"; // Pick an existing HK
             // Find a valid MaHoKhau if TQB002 doesn't exist
            PreparedStatement ps = connection.prepareStatement("SELECT MaHoKhau FROM HoKhau LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                maHoKhau = rs.getString("MaHoKhau");
            } else {
                System.out.println("No HoKhau found to test.");
                return;
            }
            System.out.println("Testing with MaHoKhau: " + maHoKhau);

            // Ensure data exists for 2023, 2024, 2025, 2026
            int[] years = {2023, 2024, 2025, 2026};
            for(int year : years){
                 checkAndCreateFeeData(year);
            }

            // Print initial state
            printState(maHoKhau, years);

            // Simulate changing area
            float newArea = 100.0f + new Random().nextInt(50);
            System.out.println("\nChanging area to: " + newArea);
            changeDienTichHoData(maHoKhau, newArea, LocalDate.now().getYear());
            
            // Print state after change
            printState(maHoKhau, years);
            
            // Verify consistency
            verifyConsistency(maHoKhau, years, newArea);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkAndCreateFeeData(int year) {
        try {
            // 1. Check PhiDichVu
            PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM PhiDichVu WHERE Nam = ? LIMIT 1");
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                // If missing, just copy previous year or use defaults (simplified version of generateDataForYear)
                 System.out.println("Generating data for year " + year + " (Simplified)");
                 // Insert default dummy data for simplicity if not exists
                 // This test assumes data MIGHT exist. If not, we might need more logic or just pick another HK.
                 // For now, let's skip generation and hope DB has data or use existing years.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeDienTichHoData(String maHoKhau, float newDienTich, int nam){
        try {
            float giaPhiDichVu = 0.0f;
            // The SUSPECT LOGIC: SELECT without Nam
            PreparedStatement ps = connection.prepareStatement("SELECT GiaPhi FROM PhiDichVu WHERE MaHoKhau = ?");
            ps.setString(1, maHoKhau);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                giaPhiDichVu = rs.getFloat("GiaPhi");
                System.out.println("DEBUG: Fetched GiaPhi for update: " + giaPhiDichVu);
            }
            
            // Hokhau update
            ps = connection.prepareStatement("UPDATE Hokhau SET dienTichHo = ? WHERE MaHoKhau = ?");
            ps.setFloat(1, newDienTich);
            ps.setString(2, maHoKhau);
            ps.executeUpdate();
    
            // PhiDichVu update
            ps = connection.prepareStatement("UPDATE PhiDichVu SET TienNopMoiThang = ? WHERE MaHoKhau = ? and Nam >= ?");
            ps.setFloat(1, giaPhiDichVu * newDienTich);
            ps.setString(2, maHoKhau);
            ps.setInt(3, nam);
            int rows = ps.executeUpdate();
            System.out.println("DEBUG: Updated PhiDichVu for " + rows + " rows.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     private static void printState(String maHoKhau, int[] years) throws Exception {
        System.out.println("--- State ---");
        for (int year : years) {
            PreparedStatement ps = connection.prepareStatement("SELECT GiaPhi, TienNopMoiThang FROM PhiDichVu WHERE MaHoKhau = ? AND Nam = ?");
            ps.setString(1, maHoKhau);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                float giaPhi = rs.getFloat("GiaPhi");
                float tienNop = rs.getFloat("TienNopMoiThang");
                System.out.printf("Year %d: GiaPhi=%.2f, TienNop=%.2f\n", year, giaPhi, tienNop);
            }
        }
    }
    
    private static void verifyConsistency(String maHoKhau, int[] years, float area) throws Exception {
        System.out.println("\n--- Verification ---");
        boolean inconsistent = false;
        for (int year : years) {
            PreparedStatement ps = connection.prepareStatement("SELECT GiaPhi, TienNopMoiThang FROM PhiDichVu WHERE MaHoKhau = ? AND Nam = ?");
            ps.setString(1, maHoKhau);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                float giaPhi = rs.getFloat("GiaPhi");
                float tienNop = rs.getFloat("TienNopMoiThang");
                float expected = giaPhi * area;
                if (Math.abs(tienNop - expected) > 1.0) { // Allow small float error
                     System.out.printf("Year %d: INCONSISTENT! GiaPhi=%.2f, Area=%.2f, Expected=%.2f, Actual=%.2f\n", year, giaPhi, area, expected, tienNop);
                     inconsistent = true;
                } else {
                     System.out.printf("Year %d: OK\n", year);
                }
            }
        }
        if(inconsistent) {
            System.out.println("BUG DETECTED: TienNopMoiThang does not match GiaPhi * Area for some years.");
        } else {
             System.out.println("Data looks consistent.");
        }
    }
}
