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

// Begin Class: Admin Menu
public class AdminMenu {

    private static class Const {

        // Constants uded in case statements
        public static final int LISTALLCARS_IN_DB    = 1001;
        public static final int LISTALLCARS_IN_CP    = 1002;
        public static final int MENU_GET_VRN         = 9001;
        public static final int SH_VEH_IN_CP         = 2001;
        public static final int SH_VEH_RECORD        = 2002;
        public static final int SH_VEH_HIST          = 2003;
        public static final int EDIT_VEH_HIST        = 2004;
        public static final int SH_CARPARK_INFO      = 2005;

    }

    private static String focusDefault = "Not set";
    private static String focusVRN = focusDefault;

    public static void display () {
        Menu adminMenu = new Menu();

        boolean exitChosen = false;


        adminMenu.setMenuTitle("Admin Menu");
        adminMenu.addMenuOption("List all vehicles in carpark", Const.LISTALLCARS_IN_CP);
        adminMenu.addMenuOption("List all vehicles in database", Const.LISTALLCARS_IN_DB);
        adminMenu.addMenuOption("Set Focus Vehicle", Const.MENU_GET_VRN);
        adminMenu.addMenuOption("Show vehicle details (current status)", Const.SH_VEH_RECORD);
        adminMenu.addMenuOption("Show vehicle details (all history)", Const.SH_VEH_HIST);
        adminMenu.addMenuOption("Edit vehicle details (all history)", Const.EDIT_VEH_HIST);
        adminMenu.addMenuOption("Show Carpark Information", Const.SH_CARPARK_INFO);


        do {

            System.out.println("");
            adminMenu.setoptionalMessage(Ansicolours.fgGREEN + "Focus Vehicle: " + Ansicolours.RESET + focusVRN);
            int menuOption = adminMenu.display();


            // The reason we do it this way is that it's now possible to insert menu items
            // or reorder without worrying about the case statement values changing

            Utils.debugPrintln(Integer.toString(menuOption));

            switch (menuOption) {
                case (Const.LISTALLCARS_IN_CP):
                    func_LISTALLCARS_IN_CP ();
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
                    Database.save();
                    exitChosen = true;
                    break;
            } // end switch
        } while (!exitChosen);

    } //end display

    // Functions for each of the menu items to keep things neat

    private static void func_LISTALLCARS_IN_CP () {
        ArrayList<String> vrnList = Database.getAllVRNsinCP ();
        System.out.println("These are the vehicles in the carpark:");
        System.out.println(Utils.createCSVLineString(vrnList));      
        System.out.println("\nThere are "+ Ansicolours.fgGREEN + vrnList.size() +  Ansicolours.RESET +" vehicles.\n");
    }

    private static void func_LISTALLCARS_IN_DB () {
        ArrayList<String> vrnList = Database.getAllVRNsinDB ();
        System.out.println("These are ALL the vehicles known to the database:");
        System.out.println(Utils.createCSVLineString(vrnList));
        System.out.println("\nThere are "+ Ansicolours.fgGREEN + vrnList.size() +  Ansicolours.RESET +" unique vehicles in the database.\n");
                
    }

    private static void func_MENU_GET_VRN () {        
        String userVRN = VRN.getFromUser();
        if (Database.getInDatabaseByVRN(userVRN)) {
            focusVRN = userVRN;
        } else {
            System.out.println(Ansicolours.MENUERROR+"[Vehicle "+userVRN+" is not known to this system.]"+Ansicolours.RESET);
        }
    }

    private static void func_SH_VEH_IN_CP () {
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

        if (Database.getInDatabaseByVRN(focusVRN)) {

            // We are safe to proceed
            System.out.println("");
            System.out.println("The latest record for this vehicle is: ");
            Vehicle vehicle = new Vehicle();
            vehicle = Database.getLatestDataByVRN(focusVRN);
            
            String exitDate = vehicle.getExitDate();
            String exitTime = "";
            // If the vehicle is still in the carpark, then 
            if (exitDate.equals("NULL")) {
                exitDate = "In Carpark";
                exitTime = "In Carpark";  
            } else {
                exitTime = vehicle.getExitTime();
            }

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

        if (Database.getInDatabaseByVRN(focusVRN)) {

            // We are safe to proceed
            System.out.println("");
            System.out.println("The history for this vehicle is: ");
            ArrayList<Vehicle> selectedData = new ArrayList<Vehicle>(1);
            selectedData = Database.getDataByVRN(focusVRN);

            int count = 1;
            float totalBalance = 0.00f;

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
                float balance = vehicle.getBalance();
                totalBalance = totalBalance + balance;
                
            }

            System.out.print("\t\t\t\t\t\tTotal Spend:\t\t");
            System.out.println(Ansicolours.fgYELLOW + Config.getValue("currency_symbol") + totalBalance + Ansicolours.RESET);
        
        } 

    } //end function

    private static void func_SH_CARPARK_INFO () {

        System.out.println("Carpark:\t" +Ansicolours.fgBLUE + Config.getValue("name") + " / " + Config.getValue("name_welsh")+Ansicolours.RESET);
        System.out.println("Postcode:\t" +Ansicolours.fgBLUE + Config.getValue("postcode")+Ansicolours.RESET);
        System.out.println("Location:\t" +Ansicolours.fgBLUE + Config.getValue("latitude") + " / " + Config.getValue("longitude")+Ansicolours.RESET);
        System.out.print("Charging Period (hours): " +Ansicolours.fgBLUE + Config.getValue("charge_period")+Ansicolours.RESET);
        System.out.println("\tParking Fee: " +Ansicolours.fgBLUE + Config.getValue("parking_fee")+Ansicolours.RESET);
        System.out.println("Capacity (not tracked):\t" +Ansicolours.fgBLUE +  Config.getValue("capacity")+Ansicolours.RESET);



    }

    private static void func_APP_METADATA () {


        
    }

} // end class
