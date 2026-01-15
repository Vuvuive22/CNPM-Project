package Test;

import Model.MysqlConnector;
import Model.PopulationMovementModel;
import Model.BillModel;
import Service.BillingService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ManualTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== STARTING SYSTEM TEST ===");
            MysqlConnector db = MysqlConnector.getInstance();
            Connection conn = db.getConnection();
            BillingService billing = new BillingService();

            // 1. Resisent Module Test
            System.out.println("\n--- 1. Testing Resident Module ---");
            // Link User
            boolean linked = db.linkUserToResident("admin", "0123456789");
            System.out.println("Linked 'admin' to '0123456789': " + linked);

            // Create Movement
            PopulationMovementModel move = new PopulationMovementModel(0, "HK001", "0123456789", "absent", "pending",
                    null, null);
            db.createPopulationMovement(move);
            System.out.println("Created 'absent' movement request for HK001-0123456789");

            // Approve (Mock ID logic needed, assume ID 1 or fetch)
            // For test, just insert data directly if needed, but let's try to query max id
            PreparedStatement psMax = conn.prepareStatement("SELECT MAX(id) FROM population_movements");
            java.sql.ResultSet rsMax = psMax.executeQuery();
            int moveId = 0;
            if (rsMax.next())
                moveId = rsMax.getInt(1);

            db.approveMovement(moveId, true, "SuperAdmin");
            System.out.println("Approved movement " + moveId);

            // 2. Billing Module Test
            System.out.println("\n--- 2. Testing Billing Module ---");

            // Setup Vehicle Data for HK001
            PreparedStatement psVeh = conn.prepareStatement(
                    "INSERT INTO vehicles (apt_id, plate_number, type, monthly_fee) VALUES ('HK001', '29A-12345', 'motorbike', 70000)");
            psVeh.executeUpdate();
            System.out.println("Added vehicle for HK001");

            // Publish Bill Config
            Map<String, Double> prices = new HashMap<>();
            prices.put("DichVu", 5000.0);
            prices.put("QuanLy", 2000.0);

            billing.publishBillConfiguration("2024-02", prices);
            System.out.println("Published Bill Config for 2024-02");

            // Check Bills
            List<BillModel> overdue = billing.getOverdueBills();
            System.out.println("Overdue Bills: " + overdue.size());
            int billId = 0;
            for (BillModel b : overdue) {
                System.out
                        .println(" - Bill ID: " + b.getId() + " | Apt: " + b.getAptId() + " | Total: " + b.getTotal());
                if (b.getAptId().equals("HK001"))
                    billId = b.getId();
            }

            // Pay Bill
            if (billId > 0) {
                billing.collectBill(billId, 500000); // Partial
                System.out.println("Paid 500k for Bill " + billId);

                billing.collectBill(billId, 10000000); // Full
                System.out.println("Paid remaining for Bill " + billId);

                overdue = billing.getOverdueBills();
                System.out.println("Overdue Bills after Payment: " + overdue.size());
            }

            System.out.println("\n=== TEST COMPLETED ===");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
