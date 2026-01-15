
import Model.MysqlConnector;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

public class FixFees {
    public static void main(String[] args) {
        try {
            Connection conn = MysqlConnector.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            int year = LocalDate.now().getYear();

            System.out.println("Fixing fees for year: " + year);

            // 1. Fix PhiDichVu: Set TienNopMoiThang = GiaPhi * Area
            // Note: MySQL supports multi-table update
            String sql1 = "UPDATE PhiDichVu P " +
                    "JOIN HoKhau H ON P.MaHoKhau = H.MaHoKhau " +
                    "SET P.TienNopMoiThang = P.GiaPhi * H.DienTichHo " +
                    "WHERE P.Nam = " + year + " AND P.TienNopMoiThang = 0";

            int count1 = stmt.executeUpdate(sql1);
            System.out.println("Updated " + count1 + " records in PhiDichVu");

            // 2. Fix PhiQuanLy
            String sql2 = "UPDATE PhiQuanLy P " +
                    "JOIN HoKhau H ON P.MaHoKhau = H.MaHoKhau " +
                    "SET P.TienNopMoiThang = P.GiaPhi * H.DienTichHo " +
                    "WHERE P.Nam = " + year + " AND P.TienNopMoiThang = 0";

            int count2 = stmt.executeUpdate(sql2);
            System.out.println("Updated " + count2 + " records in PhiQuanLy");

            // 3. Fix PhiGuiXe only if vehicles exist but fee is 0?
            // Gets complex because price depends on vehicle type counts.
            // For now, let's assume Parking might be 0 if no vehicles.
            // If user complains about Parking specifically, we can address.
            // But let's check if we can simply re-calc based on stored counts
            String sql3 = "UPDATE PhiGuiXe P " +
                    "JOIN HoKhau H ON P.MaHoKhau = H.MaHoKhau " +
                    "SET P.TienNopMoiThang = (H.SoXeMay * P.GiaXeMay + H.SoOTo * P.GiaOTo + H.SoXeDap * P.GiaXeDap) " +
                    "WHERE P.Nam = " + year;
            // Not filtering by fee=0 here, just ensure it's consistent
            int count3 = stmt.executeUpdate(sql3);
            System.out.println("Updated/Verified " + count3 + " records in PhiGuiXe");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
