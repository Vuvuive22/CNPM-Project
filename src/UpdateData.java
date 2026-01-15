
import Model.MysqlConnector;
import java.sql.Connection;
import java.sql.Statement;

public class UpdateData {
    public static void main(String[] args) {
        try {
            Connection conn = MysqlConnector.getInstance().getConnection();
            Statement stmt = conn.createStatement();

            // User requested HK001, 002, 004 to be in building '01'
            // Assuming IDs are HK001, HK002, HK004 based on the pattern
            String[] ids = { "HK001", "HK002", "HK004" };

            int totalUpdates = 0;
            for (String id : ids) {
                // Try updating both variants (with and without HK prefix if needed, but usually
                // consistent)
                // Actually user wrote "HK001 002 004", likely implying the prefix
                // Let's safe-guard by checking exact ID or constructing it
                String sql = "UPDATE HoKhau SET MaToaNha = '01' WHERE MaHoKhau = '" + id + "'";
                int count = stmt.executeUpdate(sql);
                totalUpdates += count;
                System.out.println("Updated " + id + ": " + (count > 0 ? "Success" : "Not Found"));

                if (count == 0 && id.startsWith("HK")) {
                    // Try without HK just in case user meant literally 002
                    String shortId = id.substring(2);
                    sql = "UPDATE HoKhau SET MaToaNha = '01' WHERE MaHoKhau = '" + shortId + "'";
                    int count2 = stmt.executeUpdate(sql);
                    if (count2 > 0)
                        System.out.println("Updated " + shortId + ": Success");
                    totalUpdates += count2;
                }
            }

            System.out.println("Total records updated: " + totalUpdates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
