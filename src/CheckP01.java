
import Model.MysqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CheckP01 {
    public static void main(String[] args) {
        try {
            Connection conn = MysqlConnector.getInstance().getConnection();
            String maHoKhau = "P01"; // User said P01
            int year = LocalDate.now().getYear(); // 2026?

            System.out.println("Checking data for " + maHoKhau + " year " + year);

            // 1. Check HoKhau
            String sql = "SELECT * FROM HoKhau WHERE MaHoKhau = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maHoKhau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Found HoKhau:");
                System.out.println("  DienTichHo: " + rs.getFloat("DienTichHo"));
                System.out.println("  MaToaNha: " + rs.getString("MaToaNha"));
            } else {
                System.out.println("HoKhau " + maHoKhau + " NOT FOUND!");
                return;
            }

            // 2. Check PhiDichVu
            checkFee(conn, "PhiDichVu", maHoKhau, year);
            // 3. Check PhiQuanLy
            checkFee(conn, "PhiQuanLy", maHoKhau, year);
            // 4. Check PhiGuiXe
            checkFee(conn, "PhiGuiXe", maHoKhau, year);

            // 5. Check Unit Prices in DB
            checkPrice(conn, "PhiDichVu", year);
            checkPrice(conn, "PhiQuanLy", year);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkFee(Connection conn, String table, String maHK, int year) throws Exception {
        String sql = "SELECT * FROM " + table + " WHERE MaHoKhau = ? AND Nam = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maHK);
        ps.setInt(2, year);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Found " + table + ":");
            // Check columns. Some have GiaPhi, some specific cols
            try {
                if (table.equals("PhiGuiXe")) {
                    System.out.println("  TienNopMoiThang: " + rs.getFloat("TienNopMoiThang"));
                } else {
                    System.out.println("  GiaPhi: " + rs.getFloat("GiaPhi"));
                    System.out.println("  TienNopMoiThang: " + rs.getFloat("TienNopMoiThang"));
                }
            } catch (Exception e) {
                System.out.println("  (Error reading cols: " + e.getMessage() + ")");
            }
        } else {
            System.out.println(table + " record NOT FOUND for " + year);
        }
    }

    private static void checkPrice(Connection conn, String table, int year) throws Exception {
        // This is tricky because prices are stored per record usually?
        // Or is there a master price table?
        // Implementation seems to use `SELECT GiaPhi FROM table WHERE Nam=...` limit 1?
        // In ensureAllHouseholdsHaveFeeData, it gets price from existing records.
        String sql = "SELECT GiaPhi FROM " + table + " WHERE Nam = ? LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, year);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Sample Price for " + table + " in " + year + ": " + rs.getFloat("GiaPhi"));
        } else {
            System.out.println("No records found in " + table + " for " + year + " to sample price from.");
        }
    }
}
