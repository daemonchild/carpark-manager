// 
// File:     Database Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 

package carparkmanager;

// Import LIbraries
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

//
// Begin Class: Database
//  

public class Database {   

    //
    // Attributes
    //  

    private static ArrayList<Vehicle> carparkData = new ArrayList<Vehicle>(1);

    //
    // Constructors
    //  

    public Database () {};

    //
    // Methods
    // 

    public static void open ()  {
        readDataFile (Config.getValue("db_file_path"));
    }

    public static void save ()  {
        saveDataFile (Config.getValue("db_file_path"));
    }

    public static void close ()  {
        // Just save, a real database would be closed.
        saveDataFile (Config.getValue("db_file_path"));    
    }

    //
    // Database Query Methods (SELECT, UPDATE, DELETE functions)
    //

    public static void addRecord (Vehicle newRecord) {

        carparkData.add(newRecord);

    }


    // Get All VRNs in Database
    public static ArrayList<String>  getAllVRNsinDB () {

        // SELECT DISTINCT vrn FROM carparkDatabase;

        ArrayList<String> vrnList = new ArrayList<String>(1);
        
        if (carparkData.size() != 0) {
            for (Vehicle vehicle : carparkData) {

                // Create a list of unique VRNS in the database
                String theVRN = vehicle.getVRN();

                // This is potentially nasty, and why we use proper databases :-(
                // Worst case is n * n 

                if (vrnList.size() !=0 ){
                    boolean found = false;
                    for (String aVRN : vrnList) {

                        if (theVRN.equals(aVRN)) {
                            found = true;
                            break;  // short cut the rest of the loop.
                        } else {
                            found = false;
                        }
                    }

                    // You cannot modify an arraylist inside a loop that refers to it
                    // So using flags to bring this outside the for loop
                    if (!found) {
                        vrnList.add(theVRN);
                    }   

                } 
                else { // the list is empty, add the first entry
                    vrnList.add(theVRN);
                }
                
            } // end for 

        } // end if

        return vrnList;
    }

    // Get all VRNS in the Carpark
    public static ArrayList<String> getAllVRNsinCP () {

        // SELECT DISTINCT vrn FROM carparkDatabase where inCarPark = true;
        ArrayList<String> vrnList = Database.getAllVRNsinDB();
        ArrayList<String> vrnsInCarpark = new ArrayList<>(); 

        for (String vrnString : vrnList) {
            if (Database.getInCarparkByVRN(vrnString)) {
                vrnsInCarpark.add(vrnString);
            }
        }

        return vrnsInCarpark;

    }

    // Get Vehicle data By VRN
    public static ArrayList<Vehicle> getDataByVRN (String vrn) {

        // SELECT * FROM carparkDatabase where vrn = vrn;
        ArrayList<Vehicle> selectedData = new ArrayList<Vehicle>(1);
        if (carparkData.size() != 0) {

            for (Vehicle vehicle : carparkData) {
                if (vehicle.getVRN().equals(vrn)) {
                    selectedData.add(vehicle);
                }
            }
        }

        return selectedData;
    }

    // Get Latest Vehicle data By VRN
    public static Vehicle getLatestDataByVRN (String vrnString) {

        // SELECT * FROM carparkDatabase where vrn = vrn ORDER BY id DESC LIMIT 1;
        ArrayList<Vehicle> selectedData = getDataByVRN(vrnString);
        return selectedData.get(selectedData.size()-1);

    }

    // Get total spend for a VRN
    public static float getBalanceTotalByVRN (String vrn) {

        // SELECT SUM (balance) FROM carparkDatabase where vrn = vrn;
        ArrayList<Vehicle> selectedData = getDataByVRN(vrn);
        float totalBalance = 0.00f;
        for (Vehicle vehicle : selectedData) {         
            totalBalance = totalBalance + vehicle.getBalance();
        }
        return totalBalance;
    }

    // Check: Has the Database ever seen this VRN before?
    public static boolean getInDatabaseByVRN (String vrn) {

        // SELECT vrn from carparkDatabase where 'vrn' = vrn;
        ArrayList<Vehicle> selectedData = getDataByVRN(vrn);

        return (selectedData.size() != 0); 

    }

    // Check: Has the Database ever seen this VRN before, if so how many times?
    public static int getCountInDatabaseByVRN (String vrn) {

        // SELECT COUNT (vrn) from carparkDatabase where 'vrn' = vrn;

        if (getInDatabaseByVRN(vrn)) {
            ArrayList<Vehicle> selectedData = getDataByVRN(vrn);
            return (selectedData.size()); 
        } else {
            // If we have not seen it,
            return 0;
        }

    }

    // Get total size of database
    public static int getCountInDatabase () {
        return (carparkData.size()); 
    }
    
    // Check: is the vehicle in the carpark?
    public static boolean getInCarparkByVRN (String vrn) {

        // Select inCarpark from carparkDatabase where 'vrn' = vrn
        Vehicle selectedData = new Vehicle();

        // Get the LAST entry in the database 
        selectedData = getLatestDataByVRN(vrn);
        if (selectedData == null) {
            return false;
        } else {
            if (selectedData.getInCarpark().toUpperCase().equals("TRUE")) {
                return true;
            } else {
                return false;
            }
        }

    }

    // How many records for a given vehicle?
    public static int getCountByVRN (String vrn) {

        // SELECT COUNT (vrn) from carparkDatabase where 'vrn' = vrn;
        ArrayList<Vehicle> selectedData = getDataByVRN(vrn);
        return selectedData.size();

    } //end getCount


    public static ArrayList<Integer> getIndicesByVRN (String vrnString) {

        // SELECT id FROM carpark_data WHERE vrn = vrn; 
        ArrayList<Integer> selectedIndices = new ArrayList<Integer>();
        int count = 0;

        if (carparkData.size() != 0) {
            for (Vehicle vehicle : carparkData) {
                if (vehicle.getVRN().equals(vrnString)) {
                    selectedIndices.add(count);
                }
                count++;
            }

            // Utils.debugPrintln("getLatestDataByVRN returned " + selectedIndices);
        } 

        return selectedIndices;

    }

    public static Vehicle getRecordByIndex(int index) {

        Utils.debugPrintln("Fetching row " +index);
        return carparkData.get(index);

    }

    //
    // Update / Delete Methods
    //

    // Delete a VRN from the Database
    public static void deleteVRNFromDB (Vehicle amendedVehicle) {

        int searchIndex = 0;

        if (carparkData.size() != 0) {

            // Search the database for the index number of selected record
            for (Vehicle vehicle : carparkData) {

                if (vehicle.getVRN().equals(amendedVehicle.getVRN())) {
                    // Delete the record
                    carparkData.remove(searchIndex);
                }
            }

        } 
        
    }

    // Update Record directly using 'id'
    public static void updateRecordByIndex(int index, Vehicle vehicle) {
        carparkData.set(index, vehicle);
    }

        // Delete a record directly usind 'id'
    public static void deleteRecordByIndex(int index) {
        carparkData.remove(index);
    }

    //
    // Private helper  functions
    //

    // Save the Database
    private static void saveDataFile (String filePath) {

        // Header row
        String fileHeader = "VRN,EntryDate,EntryTime,ExitDate,ExitTime,Balance,InCarpark\n";

        try {
            FileWriter outputFile = new FileWriter(filePath);
            outputFile.write(fileHeader);

            // Write out the entire database
            for (Vehicle vehicle : carparkData) {
                outputFile.write(vehicle.toString()+"\n");
            }

            outputFile.close();
                
        } 
        catch (IOException e) {
            System.out.println("An error occurred saving the database.");
            e.printStackTrace();
        }

    }

    // Load the Database
    private static void readDataFile (String filePath) {

        try {

            Scanner scannerCSV = new Scanner(new File (filePath));  

            while (scannerCSV.hasNextLine()) {
      
                // Read the line and create a new vehicle object
                String[] tokens = scannerCSV.nextLine().split(",");
                boolean calculateBalance = false;

                if (tokens.length == 0) {
                    // There is something wrong with the file.
                    break;
                }

                // Detect header row
                // Reference: VRN,EntryDate,EntryTime,ExitDate,ExitTime,Balance,InCarpark
                if (!tokens[0].equals("VRN")) {

                    // Calculate the account value in £, and inject into tokens array
                    if (calculateBalance) {
                        float account = (Float.parseFloat(tokens[5])) * Integer.parseInt(Config.getValue("parking_fee"));
                        tokens[5] = Float.toString(account);
                    }

                    Vehicle newVehicle = new Vehicle(tokens);
                    addRecord(newVehicle);
                } 

            } //end while

            scannerCSV.close();


        } //end try
        catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
        } 

    } //end readDataFile


} //end class

//
// End of File: Database Class
//