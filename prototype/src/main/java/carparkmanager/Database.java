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

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.ArrayList;


/*
 Class:         Database
 Public Methods:
                connect
                addRecord
                deleteRecord
                amendRecord
                getInCarparkByVRN
                getCountByVRN
*/

public class Database {   

    private static ArrayList<Vehicle> carparkData = new ArrayList<Vehicle>(1);

    public Database () {

    }

    public static void connect ()  {

        readDataFile (Config.DBFILEPATH);
        Utils.debugPrintln("Database loaded, " + getCountInDatabase() + " rows");

    }

    public static void close ()  {

        saveDataFile (Config.DBFILEPATH);
        Utils.debugPrintln("Database saved, " + getCountInDatabase() + " rows");

    }

    public static void save ()  {

        saveDataFile (Config.DBFILEPATH);
        Utils.debugPrintln("Database saved, " + getCountInDatabase() + " rows");

    }



    public static void addRecord (Vehicle newRecord) {

        carparkData.add(newRecord);
        //Utils.debugPrintln("Added new record, " + newRecord.getVRN());
    }

    private static void saveDataFile (String filePath) {

            // Write header row
            String fileHeader = "VRN,EntryDate,EntryTime,ExitDate,ExitTime,Balance,InCarpark\n";

            try {
                FileWriter outputFile = new FileWriter(Config.DBFILEPATH);
                outputFile.write(fileHeader);

                for (Vehicle vehicle : carparkData) {
                    
                    outputFile.write(vehicle.getDataString());

                }

                outputFile.close();
                
            } 
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

    }

    private static void readDataFile (String filePath) {



        try {

            Scanner scannerCSV = new Scanner(new File (filePath));  
            scannerCSV.useDelimiter(",");  

            while (scannerCSV.hasNextLine()) {
      
                // Read the line and create a new vehicle object
                
                String[] tokens = scannerCSV.nextLine().split(",");
                boolean calculateBalance = false;

                // Detect header row!
                // Reference: VRN,EntryDate,EntryTime,ExitDate,ExitTime,ParkingPeriods|Balance,InCarpark
                if (tokens[5].equals("ParkingPeriods")) {

                    // We need to calculate the balance
                    // It's an initial state file from the Python test data generator
                    calculateBalance = true;
                    Utils.debugPrintln("Loading a Python generated database.");

                } 
                if (!tokens[0].equals("VRN")) {

                    // Calculate the account value in £, and inject into tokens array
                    if (calculateBalance) {

                        float account = (Float.parseFloat(tokens[5])) * Config.FEE;
                        tokens[5] = Float.toString(account);
                        Utils.debugPrintln("We set " + tokens[5]);

                    }

                    Vehicle newVehicle = new Vehicle(tokens);
                    addRecord(newVehicle);

                } //end if

            } //end while

            scannerCSV.close();


        } //end try
        catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                
        } // end catch

    } //end readDataFile

    /* SELECT FUNCTIONS */


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
                // Have we seen theVRN already?

                if (vrnList.size() !=0 ){

                    boolean foundIT = false;

                    for (String aVRN : vrnList) {

                        if (theVRN.equals(aVRN)) {

                            foundIT = true;
                            break;  // short cut the rest of the loop.

                        } else {

                            foundIT = false;

                        }

                    }
                    // You cannot modify an arraylist inside a loop that refers to it
                    // So using flags to bring this outside the for loop
                    if (!foundIT) {

                        vrnList.add(theVRN);

                    }   

                } else { // the list is empty, just add it

                    vrnList.add(theVRN);
                }
                
            }

        } 

        return vrnList;

    }

    // Get all VRNS in the Carpark

    public static ArrayList<String> getAllVRNsinCP () {

        ArrayList<String> vrnList = Database.getAllVRNsinDB();
        ArrayList<String> vrnsInCarpark = new ArrayList<>(); 

        for (String vrnString : vrnList) {

            if (Database.getInCarparkByVRN(vrnString)) {

                vrnsInCarpark.add(vrnString);
            }
        }

        return vrnsInCarpark;

    }

    // Get Data By VRN
    public static ArrayList<Vehicle> getDataByVRN (String vrn) {

        ArrayList<Vehicle> selectedData = new ArrayList<Vehicle>(1);
        if (carparkData.size() != 0) {

            for (Vehicle vehicle : carparkData) {

                if (vehicle.getVRN().equals(vrn)) {

                    selectedData.add(vehicle);

                }

            }

        }
        Utils.debugPrintln("getDataByVRN returned " + selectedData.size() + " rows.");
        return selectedData;

    }

    // Get Data By VRN - latest only
    public static Vehicle getLatestDataByVRN (String vrnString) {

        int searchIndex = 0;
        int count = 0;

        Vehicle returnVehicle = new Vehicle();

        if (carparkData.size() != 0) {
            for (Vehicle vehicle : carparkData) {
                if (vehicle.getVRN().equals(vrnString)) {
                    searchIndex = count;
                }
                count++;
            }
            returnVehicle = carparkData.get(searchIndex);
            //Utils.debugPrintln("getLatestDataByVRN returned row " + searchIndex + ", " + vrnString + ", inCP: " + returnVehicle.getInCarpark());
        } 

        return returnVehicle;

    }

    public static void updateRecordByVRN (Vehicle amendedVehicle) {

        int searchIndex = 0;
        int count = 0;

        if (carparkData.size() != 0) {

            for (Vehicle vehicle : carparkData) {

                if (vehicle.getVRN().equals(amendedVehicle.getVRN())) {

                    searchIndex = count;

                }

                count++;

            }

            carparkData.set(searchIndex, amendedVehicle);
            Utils.debugPrintln("updateRecordByVRN updated row " + searchIndex + ", " + amendedVehicle.getVRN());

        } 
        
      
    }



    public static float getBalanceTotalByVRN (String vrn) {

        ArrayList<Vehicle> selectedData = getDataByVRN(vrn);

        float totalBalance = 0.00f;

        for (Vehicle vehicle : selectedData) {
            
            totalBalance = totalBalance + vehicle.getBalance();

        }
        return totalBalance;


    }

    // Have we ever seen this vrn?
    public static boolean getInDatabaseByVRN (String vrn) {

        // Select vrn from carparkDatabase where 'vrn' = vrn
        ArrayList<Vehicle> selectedData = getDataByVRN(vrn);

        return (selectedData.size() != 0); 

    }

    // Have we ever seen this vrn, if so how many times
    public static int getCountInDatabaseByVRN (String vrn) {

        // Select vrn from carparkDatabase where 'vrn' = vrn
        if (getInDatabaseByVRN(vrn)) {

            ArrayList<Vehicle> selectedData = getDataByVRN(vrn);
            return (selectedData.size()); 

        } else {

            return 0;

        }


    }

    // Because why not?
    public static int getCountInDatabase () {

        return (carparkData.size()); 

    }
    
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

    public static int getCountByVRN (String vrn) {

        // COUNT (Select vrn from carparkDatabase where 'vrn' = vrn)
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

    public static void deleteRecordByIndex(int index) {

        // Utils.debugPrintln("Deleting row " +index);
        carparkData.remove(index);

    }

    public static void updateRecordByIndex(int index, Vehicle vehicle) {

        Utils.debugPrintln("Updating row " + index);
        carparkData.set(index, vehicle);

        Utils.debugPrintln(carparkData.get(index).getExitDate());

    }


} //end class
