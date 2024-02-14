//coded by Divyesh Lathiya
//This is basic E-challan System where you can make e-challan,print e-challan and more features in it.


import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class E_Challan {
    Connection con;
    PreparedStatement stmt;
    Statement stmtt;
    ResultSet rs;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        E_Challan challan = new E_Challan();
        challan.createConnection();
    }

    void createConnection() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/btech65", "root", "290904");
            stmtt = con.createStatement();
            while (true) {

                System.out.println("");
                System.out.println("\t**********E-Challan Portal**********\n");
                System.out.println("\t1. Make Challan");
                System.out.println("\t2. Track Old Record");
                System.out.println("\t3. Precaution");
                System.out.println("\t4. Check Validity");
                System.out.println("\t5. Generate Challan");
                System.out.println("\t6. Exit");
                System.out.println("\tEnter Choice :");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: {
                        clearScreen();
                        makeChallan();
                        break;
                    }
                    case 2: {
                        clearScreen();
                        trackOldRecord();
                        break;
                    }
                    case 3: {
                        clearScreen();
                        precaution();
                        break;
                    }
                    case 4: {
                        clearScreen();
                        checkValidity();
                        break;
                    }
                    case 5: {
                        clearScreen();
                        generateChallan();
                        break;
                    }
                    case 6: {
                        System.exit(0);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void makeChallan() {
        try {
            System.out.println("Enter Challan No :");
            int chllanNO = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter Vehicle Owner name :");
            String ownName = sc.nextLine();
            System.out.println("Enter Date in (yyyy-mm-dd):");
            String date = sc.nextLine();

            // Parse the input date
            LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

            // Calculate validity date (60 days later)
            LocalDate validityDate = inputDate.plusDays(60);

            // Convert to SQL Date
            Date validitySQLDate = Date.valueOf(validityDate);

            LocalTime currentTime = LocalTime.now(); // Get current time
            Time time = Time.valueOf(currentTime); // Convert to SQL Time

            System.out.println("Enter Vehical No in (Gj-05-SA-1234)");
            String vehicleNo = sc.nextLine();
            vehicleNo.toUpperCase();
            System.out.println("Enter City :");
            String city = sc.nextLine();
            System.out.println("Enter State :");
            String state = sc.nextLine();
            System.out.println("Enter Reason for Challan :");
            String reason = sc.nextLine();
            System.out.println("Enter Challan Amount :");
            int challanAmount = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter Payment Mode :");
            String paymentMode = sc.nextLine();

            String query = "insert into eChallan values(?,?,?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(query);

            stmt.setInt(1, chllanNO);
            stmt.setString(2, ownName);
            stmt.setString(3, date);
            stmt.setTime(4, time);
            stmt.setString(5, vehicleNo);
            stmt.setString(6, state);
            stmt.setString(7, city);
            stmt.setString(8, reason);
            stmt.setInt(9, challanAmount);
            stmt.setString(10, paymentMode);
            stmt.setDate(11, validitySQLDate);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Challan created successfully.");
            } else {
                System.out.println("Failed to create challan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void trackOldRecord() {

        try {
            sc.nextLine();
            System.out.println("Enter Vehicle No :");
            String vehicleN = sc.nextLine();
            vehicleN.toLowerCase();
            // String searchQuery = "select eChallanNo,vehicleOwnName,reason from echallan
            // where vehicleNo = '"+vehicleN+"'";
            String searchQuery = "select *from echallan where vehicleNo = '" + vehicleN + "'";

            rs = stmtt.executeQuery(searchQuery);
            System.out.println("Serching ........");
            boolean found = false;

            while (rs.next()) {
                int eid = rs.getInt("EchallanNo");
                String vName = rs.getString("VehicleOwnName");
                String resn = rs.getString("Reason");
                Date date = rs.getDate("eDate");
                System.out.println("");
                System.out.println("Chllan No - " + eid + " \nVehicle Owner Name - " + vName);
                System.out.println("Reason - " + resn);
                System.out.println("Date = " + date);
                found = true;
            }

            if (!found) {
                System.out.println("\nRecord not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void precaution() {

        System.out.println("\t * Follow Traffic Rules");
        System.out.println("\t * Ensure Proper Documentation");
        System.out.println("\t * Avoid Using Mobile Phones while Driving");
        System.out.println("\t * Wear Seat Belts");
        System.out.println("\t * Helmet Usage while drive two-wheeler");
        System.out.println("\t * Avoid Drunk Driving");

    }

    void checkValidity() {
        try {
            boolean found = false;
            sc.nextLine();
            System.out.println("Enter Vehicle No :");
            String vehicleN = sc.nextLine();
            vehicleN.toUpperCase();
            String searchQuery = "select *from echallan where vehicleNo = '" + vehicleN + "'";
            rs = stmtt.executeQuery(searchQuery);

            while (rs.next()) {
                int eid = rs.getInt("EchallanNo");
                String vName = rs.getString("VehicleOwnName");
                String vNo = rs.getString("vehicleNo");
                Date date = rs.getDate("validityDate");

                System.out.println("Echallan No - " + eid);
                System.out.println("Vehicle Owner Name - " + vName);
                System.out.println("Vehicle NO - " + vNo);
                System.out.println("Last Date of E-Challan will be to pay - " + date);
                found = true;

            }

            if (!found) {
                System.out.println("Record Not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void generateChallan() {
        try {
            sc.nextLine();
            System.out.println("Enter Vehicle No :");
            String vehicleN = sc.nextLine();
            vehicleN.toUpperCase();
            String searchQuery = "select *from echallan where vehicleNo = '" + vehicleN + "'";

            rs = stmtt.executeQuery(searchQuery);
            boolean found = false;

            while (rs.next()) {
                int eid = rs.getInt("EchallanNo");
                String vName = rs.getString("VehicleOwnName");
                Time time = rs.getTime("eTime");
                String vNo = rs.getString("vehicleNo");
                String state = rs.getString("State");
                String city = rs.getString("City");
                Date date = rs.getDate("eDate");
                String resn = rs.getString("Reason");
                int amount = rs.getInt("challanAmount");
                String paymentMode = rs.getString("paymentMode");

                System.out.println("");
                System.out.println("---------------------------------------------------------------------------------");
                System.out.println("\t\t\t\tGujarat Police\t\t\t\n");
                System.out.println("\t\t\t\tE-Challan Recipt");
                System.out.println("---------------------------------------------------------------------------------");
                System.out.println("\t\t\t\t\t\t\t\tDate - " + date);
                System.out.println("\t\t\t\t\t\t\t\tTime - " + time);

                System.out.println("Chllan No - " + eid + " \nVehicle Owner Name - " + vName);
                System.out.println("Vehicle No - " + vNo);
                System.out.println("Reason - " + resn);
                System.out.println("Location - " + city + ", " + state);
                System.out.println("Payable Amount - " + amount);
                System.out.println("Payment Mode - " + paymentMode);
                System.out.println("---------------------------------------------------------------------------------");

                found = true;
            }
            if (!found) {
                System.out.println("\nRecord not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
