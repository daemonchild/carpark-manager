// 
// File:     Admin Menu Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 

package carparkmanager;

// Import Libraries
import java.util.ArrayList;

//
// Begin Class: Admin Menu
//
public class AdminMenu {

    //
    // Attributes
    //    

    // Constants used in case statements
    private static class Const {

        public static final int LISTALLCARS_IN_DB    = 1001;
        public static final int LISTALLCARS_IN_CP    = 1002;
        public static final int MENU_GET_VRN         = 9001;
        public static final int SH_VEH_IN_CP         = 2001;
        public static final int SH_VEH_RECORD        = 2002;
        public static final int SH_VEH_HIST          = 2003;
        public static final int EDIT_VEH_HIST        = 2004;
        public static final int SH_CARPARK_INFO      = 2005;

    }

    // Keep track of the focus vehicle.
    private static String focusDefault = Config.getValue("am_focus_default");
    private static String focusVRN = focusDefault;

    //
    // Methods
    //    

    // Display the Menu
    public void display () {

        // Create a menu Menu Object
        Menu adminMenu = new Menu();

        // Menu loop variable
        boolean exitChosen = false;

        String lang =  "_" + Config.getValue("language");

        // Setup the Menu
        adminMenu.setMenuTitle(Config.getValue("am_title"+lang));
        adminMenu.addMenuOption(Config.getValue("am_list_in_cp"+lang), Const.LISTALLCARS_IN_CP);
        adminMenu.addMenuOption(Config.getValue("am_list_in_db"+lang), Const.LISTALLCARS_IN_DB);
        adminMenu.addMenuOption(Config.getValue("am_set_focus"+lang), Const.MENU_GET_VRN);
        adminMenu.addMenuOption(Config.getValue("am_sh_veh_det"+lang), Const.SH_VEH_RECORD);
        adminMenu.addMenuOption(Config.getValue("am_sh_veh_hist"+lang), Const.SH_VEH_HIST);
        adminMenu.addMenuOption(Config.getValue("am_ed_veh_hist"+lang), Const.EDIT_VEH_HIST);
        adminMenu.addMenuOption(Config.getValue("am_sh_cp_info"+lang), Const.SH_CARPARK_INFO);

        // Until the user exits the menu...
        do {

            // This needs to be inside the loop as the user must reset the focus vehicle
            adminMenu.setoptionalMessage(Ansicolours.fgGREEN + Config.getValue("am_focus_vehicle"+lang)+ " " +  Ansicolours.RESET + focusVRN);
            int menuOption = adminMenu.display();

            // The menu option returned is the 'function' assigned when the menu is first setup
            // The reason we do it this way is that it's now possible to insert menu items
            // or reorder without worrying about the case statement values changing
            switch (menuOption) {
                case (Const.LISTALLCARS_IN_CP):
                    func_LISTALLCARS_IN_CP ();          // Uses a private helper function (below) to keep this tidy.
                    break;

                case (Const.LISTALLCARS_IN_DB):
                    func_LISTALLCARS_IN_DB ();
                    break;
            
                case (Const.MENU_GET_VRN):
                    func_MENU_GET_VRN ();
                    break;

                case (Const.SH_VEH_IN_CP):
                    func_SH_VEH_IN_CP ();
                    break;

                case (Const.SH_VEH_RECORD):
                    func_SH_VEH_RECORD();
                    break;

                case (Const.SH_VEH_HIST):
                    func_SH_VEH_HIST();
                    break;

                case (Const.EDIT_VEH_HIST):
                    func_SH_VEH_HIST();
                    EditMenu editMenu = new EditMenu(focusVRN);
                    editMenu.display();
                    break;

                case (Const.SH_CARPARK_INFO):
                    func_SH_CARPARK_INFO();
                    break;
                       
                case 99:
                    // Exit the menu
                    Database.save();        // Save the database in case any edits have been made
                    exitChosen = true;
                    break;
            } // end switch
        } while (!exitChosen);

    } // end display

    //
    // Private helper Functions for each of the menu items above
    //

    private static void func_LISTALLCARS_IN_CP () {

        // To do: This is not the prettiest display
        // NB: Welsh translation for main menus only in prototype
        ArrayList<String> vrnList = Database.getAllVRNsinCP ();
        System.out.println("These are the vehicles currently in the carpark:");

        int count = 1;

        for (String vrn : vrnList) {
            
            System.out.println(count + ". " + Ansicolours.fgGREEN + vrn + Ansicolours.RESET + " with " + Ansicolours.fgGREEN + Database.getCountByVRN(vrn) + Ansicolours.RESET + " visits recorded");
            count++;
        }     
        System.out.println("\nThere are "+ Ansicolours.fgGREEN + vrnList.size() +  Ansicolours.RESET +" vehicles.\n");
    }

    private static void func_LISTALLCARS_IN_DB () {

        // To do: This is not the prettiest display
        // NB: Welsh translation for main menus only

        // Be nice to sort this list alphabetically.
        ArrayList<String> vrnList = Database.getAllVRNsinDB ();
        ArrayList<String> regulars = new ArrayList<String>(0);

        int count = 1;

        System.out.println("These are ALL the vehicles known to the database:");
        for (String vrn : vrnList) {

            String totalBalanceString = String.format("%.2f", Database.getBalanceTotalByVRN(vrn));
            System.out.println(count + ". " + Ansicolours.fgGREEN + vrn + Ansicolours.RESET + " with " + Ansicolours.fgGREEN + Database.getCountByVRN(vrn) + Ansicolours.RESET + " visits recorded, with a "+ Ansicolours.fgMAGENTA + Config.getValue("currency_symbol")+ totalBalanceString + Ansicolours.RESET + " total spend.");
            
            // If they are regular visitors, add them to a list
            if (Database.getCountByVRN(vrn) >= 3) {
                regulars.add(vrn);
            }

            count++;
        }
        System.out.println("\nThere are "+ Ansicolours.fgGREEN + vrnList.size() + Ansicolours.RESET + " unique vehicles in the database.");
        System.out.println("The database contains "+ Ansicolours.fgGREEN + Database.getCountInDatabase() +  Ansicolours.RESET +" records.");
        System.out.println(Ansicolours.fgCYAN + "Regulars: " + Ansicolours.RESET + regulars.toString());
        System.out.println();
    }

    private static void func_MENU_GET_VRN () {  
        
        // Set the focus vehicle for further queries, but only if we actually have data on that vehicle
        String userVRN = VRN.getFromUser();
        if (Database.getInDatabaseByVRN(userVRN)) {
            focusVRN = userVRN;
        } else {
            System.out.println(Ansicolours.MENUERROR+"[Vehicle "+userVRN+" is not known to this system.]"+Ansicolours.RESET);
        }
    }

    private static void func_SH_VEH_IN_CP () {

        // If the user has set a vehicle to focus the query then ask the database if the vehicle is present.
        if (!focusVRN.equals(focusDefault)) {
            boolean result = Database.getInCarparkByVRN(focusVRN);
            System.out.print(focusVRN);
            if (result) {
                System.out.println(Ansicolours.fgGREEN + " is in the carpark." + Ansicolours.RESET);
            } else {
                System.out.println(Ansicolours.fgRED +" is NOT in the carpark."+ Ansicolours.RESET);
            }
        } else {
            System.out.println(Ansicolours.MENUERROR + "[PLEASE SET VEHICLE TO QUERY]" + Ansicolours.RESET);
        }
    }


    private static void func_SH_VEH_RECORD () {

        // If the user has set a vehicle to focus the query then ask the database for the last known record
        if (Database.getInDatabaseByVRN(focusVRN)) {

            // Get the last data for vehicle
            Vehicle vehicle = new Vehicle();
            vehicle = Database.getLatestDataByVRN(focusVRN);
            
            // We track these to format the table nicely
            String exitDate = vehicle.getExitDate();
            String exitTime = "";

            // If the vehicle is still in the carpark, then the date will be NULL
            if (exitDate.equals("NULL")) {
                exitDate = "In Carpark";
                exitTime = "In Carpark";  
            } else {
                exitTime = vehicle.getExitTime();
            }

            // Colourful output of the vehicle record
            System.out.println("");
            System.out.println("The latest record for this vehicle is: ");
            System.out.println("Registration:\t" +Ansicolours.fgBLUE + vehicle.getVRN() +Ansicolours.RESET);
            System.out.print("Entry Date:\t" +Ansicolours.fgBLUE +  vehicle.getEntryDate()+Ansicolours.RESET);
            System.out.println("\tEntry Time:\t" +Ansicolours.fgBLUE + vehicle.getEntryTime()+Ansicolours.RESET);
            System.out.print("Exit Date:\t" + Ansicolours.fgBLUE + exitDate +Ansicolours.RESET);
            System.out.println("\tExit Time:\t" +Ansicolours.fgBLUE + exitTime +Ansicolours.RESET);
            System.out.print("Balance:\t"+Ansicolours.fgBLUE + Config.getValue("currency_symbol") + vehicle.getBalanceAsString());
            System.out.print(Ansicolours.RESET);
            
            System.out.print("\t\tVehicle is currently ");
            if (vehicle.getInCarpark().toUpperCase().equals("TRUE")) {
                System.out.print(Ansicolours.fgGREEN + "in the carpark" + Ansicolours.RESET);
            } else {
                System.out.print(Ansicolours.fgRED +"NOT in the carpark"+ Ansicolours.RESET);
            }
            System.out.println(" at this time.");


        } else {

            System.out.println(Ansicolours.MENUERROR + "[PLEASE SET VEHICLE TO QUERY]" + Ansicolours.RESET);

        }
        
    }

    private static void func_SH_VEH_HIST () { 

        // If the user has set a vehicle to focus the query then ask the database for all records
        if (Database.getInDatabaseByVRN(focusVRN)) {


            // Get all data for vehicle
            ArrayList<Vehicle> selectedData = new ArrayList<Vehicle>(1);
            selectedData = Database.getDataByVRN(focusVRN);

            int count = 1;

            // Print in a table format
            System.out.println("");
            System.out.println("The history for this vehicle is: ");
            System.out.println(Ansicolours.fgBLUE+"No.\tRegistration\tEntry\t\t\tExit\t\t\tBalance"+Ansicolours.RESET);

            for (Vehicle vehicle : selectedData) {

                String exitDate = vehicle.getExitDate();
                String exitTime = "";
                // If the vehicle is still in the carpark, then 
                if (exitDate.toUpperCase().equals("NULL")) {
                    exitDate = Ansicolours.fgGREEN + "In Carpark" + Ansicolours.RESET;
                    exitTime = "\t";  
                } else {
                    exitTime = vehicle.getExitTime();
                }

                System.out.println(count + "\t" + vehicle.getVRN() + "\t\t"+ vehicle.getEntryDate() + " " + vehicle.getEntryTime() + "\t"+ exitDate + " " + exitTime + "\t" + Config.getValue("currency_symbol") + vehicle.getBalanceAsString());
                count ++;
                
            }

            // Calculate the total spend for the vehicle
            String totalBalanceString = String.format("%.2f", Database.getBalanceTotalByVRN(focusVRN));
            System.out.print("\t\t\t\t\t\tTotal Spend:\t\t");
            System.out.println(Ansicolours.fgYELLOW + Config.getValue("currency_symbol") + totalBalanceString + Ansicolours.RESET);
        
        } 

    } //end function

    private static void func_SH_CARPARK_INFO () {

        // Print out the carpark data
        System.out.println("Carpark:\t" +Ansicolours.fgBLUE + Config.getValue("name_english") + " / " + Config.getValue("name_welsh")+Ansicolours.RESET);
        System.out.println("Postcode:\t" +Ansicolours.fgBLUE + Config.getValue("postcode")+Ansicolours.RESET);
        System.out.println("Location:\t" +Ansicolours.fgBLUE + Config.getValue("latitude") + " / " + Config.getValue("longitude")+Ansicolours.RESET);
        System.out.print("Charging Period (hours): " +Ansicolours.fgBLUE + Config.getValue("charge_period")+Ansicolours.RESET);
        System.out.println("\tParking Fee: " +Ansicolours.fgBLUE + Config.getValue("parking_fee")+Ansicolours.RESET);
        System.out.println("Capacity (not tracked):\t" +Ansicolours.fgBLUE +  Config.getValue("capacity")+Ansicolours.RESET);

    }

} // end class

//
// End of File: Admin Menu Class
//
