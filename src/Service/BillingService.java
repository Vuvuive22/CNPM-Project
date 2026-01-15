package Service;

import Model.MysqlConnector;
import Model.BillModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillingService {

    public void publishBillConfiguration(String period, Map<String, Double> servicePrices) {
        Connection conn = null;
        try {
            conn = MysqlConnector.getInstance().getConnection();

            // 1. Save Config
            // For simplicity, just saving period. In real app, save servicePrices as JSON
            // in bill_configurations
            PreparedStatement psConfig = conn
                    .prepareStatement("INSERT IGNORE INTO bill_configurations (period, services_json) VALUES (?, ?)");
            psConfig.setString(1, period);
            psConfig.setString(2, servicePrices.toString());
            psConfig.executeUpdate();

            // 2. Get All Apartments
            PreparedStatement psApt = conn.prepareStatement("SELECT * FROM hokhau");
            ResultSet rsApt = psApt.executeQuery();

            while (rsApt.next()) {
                String aptId = rsApt.getString("MaHoKhau");
                float area = rsApt.getFloat("dienTichHo");

                // 3. Calculate Service Fee
                // Assuming servicePrices has keys like "DichVu", "QuanLy", "Dien", "Nuoc"
                // Simplified: Total Service = (DichVu + QuanLy) * Area + Fixed Fees
                double unitPriceService = servicePrices.getOrDefault("DichVu", 0.0);
                double unitPriceManagement = servicePrices.getOrDefault("QuanLy", 0.0);
                // usage defaults?
                float serviceFee = (float) ((unitPriceService + unitPriceManagement) * area);

                // 4. Calculate Vehicle Fee
                float vehicleFee = 0;
                PreparedStatement psVeh = conn.prepareStatement(
                        "SELECT SUM(monthly_fee) as total FROM vehicles WHERE apt_id = ? AND status = 'active'");
                psVeh.setString(1, aptId);
                ResultSet rsVeh = psVeh.executeQuery();
                if (rsVeh.next()) {
                    vehicleFee = rsVeh.getFloat("total");
                }

                // 5. Check Pre-debt (From previous month bills)
                // Determine previous period string (logic omitted for brevity, using simple
                // check)
                float preDebt = 0;
                // Query previous unbound bills? (Simple approach: Sum all unpaid bills before
                // this period)
                // PreparedStatement psDebt = conn.prepareStatement("SELECT SUM(total -
                // IFNULL((SELECT SUM(amount) FROM payments WHERE bill_id = bills.id),0)) FROM
                // bills WHERE apt_id = ? AND paid = FALSE AND period < ?");
                // For now, let's just set 0 or Mock.

                float total = serviceFee + vehicleFee + preDebt;

                // 6. Upsert Bill
                // Check if bill exists
                PreparedStatement psCheck = conn
                        .prepareStatement("SELECT id FROM bills WHERE apt_id = ? AND period = ?");
                psCheck.setString(1, aptId);
                psCheck.setString(2, period);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    // Update
                    int billId = rsCheck.getInt("id");
                    PreparedStatement psUp = conn.prepareStatement(
                            "UPDATE bills SET service_fee=?, vehicle_fee=?, pre_debt=?, total=? WHERE id=?");
                    psUp.setFloat(1, serviceFee);
                    psUp.setFloat(2, vehicleFee);
                    psUp.setFloat(3, preDebt);
                    psUp.setFloat(4, total);
                    psUp.setInt(5, billId);
                    psUp.executeUpdate();
                } else {
                    // Insert
                    PreparedStatement psIns = conn.prepareStatement(
                            "INSERT INTO bills (apt_id, period, service_fee, vehicle_fee, pre_debt, total) VALUES (?, ?, ?, ?, ?, ?)");
                    psIns.setString(1, aptId);
                    psIns.setString(2, period);
                    psIns.setFloat(3, serviceFee);
                    psIns.setFloat(4, vehicleFee);
                    psIns.setFloat(5, preDebt);
                    psIns.setFloat(6, total);
                    psIns.executeUpdate();
                }

                // 7. Notify
                System.out.println("Notification: Bill created for " + aptId + ", Total: " + total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean collectBill(int billId, float amount) {
        Connection conn = null;
        try {
            conn = MysqlConnector.getInstance().getConnection();
            // 1. Create Payment
            PreparedStatement psPay = conn.prepareStatement("INSERT INTO payments (bill_id, amount) VALUES (?, ?)");
            psPay.setInt(1, billId);
            psPay.setFloat(2, amount);
            psPay.executeUpdate();

            // 2. Check if fully paid
            PreparedStatement psBill = conn.prepareStatement("SELECT total FROM bills WHERE id = ?");
            psBill.setInt(1, billId);
            ResultSet rsBill = psBill.executeQuery();
            if (rsBill.next()) {
                float total = rsBill.getFloat("total");

                // Get total paid
                PreparedStatement psPaid = conn.prepareStatement("SELECT SUM(amount) FROM payments WHERE bill_id = ?");
                psPaid.setInt(1, billId);
                ResultSet rsPaid = psPaid.executeQuery();
                float totalPaid = 0;
                if (rsPaid.next())
                    totalPaid = rsPaid.getFloat(1);

                if (totalPaid >= total) {
                    PreparedStatement psUpdate = conn
                            .prepareStatement("UPDATE bills SET paid = TRUE, paid_at = CURRENT_TIMESTAMP WHERE id = ?");
                    psUpdate.setInt(1, billId);
                    psUpdate.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BillModel> getOverdueBills() {
        List<BillModel> list = new ArrayList<>();
        try {
            Connection conn = MysqlConnector.getInstance().getConnection();
            String query = "SELECT * FROM bills WHERE paid = FALSE ORDER BY total DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(new BillModel(
                        rs.getInt("id"),
                        rs.getString("apt_id"),
                        rs.getString("period"),
                        rs.getFloat("service_fee"),
                        rs.getFloat("vehicle_fee"),
                        rs.getFloat("pre_debt"),
                        rs.getFloat("total"),
                        rs.getBoolean("paid"),
                        rs.getTimestamp("paid_at"),
                        rs.getString("details_json")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
