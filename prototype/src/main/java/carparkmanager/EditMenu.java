// 
// File:     Edit Menu Class
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
import java.util.Scanner;
import java.util.ArrayList;

//
// Begin Class: Database
//  

public class EditMenu {

    //
    // Attributes
    //    

    // Constants used in case statements
    private static class Const {

        public static final int MENU_EDIT         = 1001;
        public static final int MENU_DELETE       = 1002;

        public static final int MENU_ENTRYD       = 2001;
        public static final int MENU_ENTRYT       = 2002;
        public static final int MENU_EXITD        = 2003;
        public static final int MENU_EXITT        = 2004;
        public static final int MENU_BALANCE      = 2005;
        public static final int MENU_ICP          = 2006;
        public static final int SH_RECORD         = 2007;

    }

    // This Edit/Delete menu is all about this VRN
    private String vrnString = "";

    //
    // Constructors
    // 

    public EditMenu (String vrn) {

        this.vrnString = vrn;
        
    }

    //
    // Methods
    // 

    public void display () {
        Menu thisMenu = new Menu();

        boolean exitChosen = false;

        // Setup the Menu
        thisMenu.setMenuTitle("Edit History for " + this.vrnString);
        thisMenu.addMenuOption("Edit Line", Const.MENU_EDIT);
        thisMenu.addMenuOption("Delete Line", Const.MENU_DELETE);

        int numberRows = Database.getCountByVRN(vrnString);

        do {

            System.out.println("");
            int menuOption = thisMenu.display();
            int chosenRow = 0;
            ArrayList<Integer> rows = Database.getIndicesByVRN(this.vrnString);

            // The reason we do it this way is that it's now possible to insert menu items
            // or reorder without worrying about the case statement values changing
            switch (menuOption) {
                case (Const.MENU_DELETE):
                    chosenRow = getRowFromUser(numberRows, "Enter row number to " + Ansicolours.fgRED + "delete:" + Ansicolours.RESET);
                    Database.deleteRecordByIndex(rows.get(chosenRow-1)); 
                    //rows.remove(chosenRow-1)
                    break;

                case (Const.MENU_EDIT):
                    chosenRow = getRowFromUser(numberRows, "Enter row number to " + Ansicolours.fgGREEN + "edit:" + Ansicolours.RESET);

                    // New object to be edited, copy old into it
                    Vehicle amendedVehicle = new Vehicle();
                    amendedVehicle = Database.getRecordByIndex(rows.get(chosenRow-1));

                    // Get user to perform the edit via helper function below.
                    amendedVehicle = editVehicle (amendedVehicle);

                    // Update the record
                    Database.updateRecordByIndex(rows.get(chosenRow-1), amendedVehicle);
                    break;
            
                case 99:
                    // Exit the menu
                    exitChosen = true;
                    break;
            } // end switch
        } while (!exitChosen);         

    }

    //
    // Private helper Functions 
    //

    // Get the row number from a user
    private static int getRowFromUser (int maxRow, String messageString) {

        // Get From a user scanner
        // Check for validity
        Scanner getUserInput = new Scanner(System.in);

        int number = 0;
        boolean validVRN = false;

        do {

            // A bit of UX :-)
            if (maxRow == 1) {

                System.out.println(Ansicolours.fgCYAN + "You can only choose row 1." + Ansicolours.RESET);
                // So we choose one for them.
                return 1;

            } else if (maxRow == 2) {

                System.out.println("You may choose rows 1 or 2.");

            } else {
                System.out.println("You can choose from rows 1 to " + maxRow + ".");
            }

            System.out.print(messageString + " > ");
            number = getUserInput.nextInt();

            // Validate their choice
            if (!(number > 0 & number <= maxRow)) {
                System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INVALID ROW ENTERED]" + Ansicolours.RESET);
            }
            
        } while (!(number > 0 & number <= maxRow));

        // Note: Could allow a return of 0 to abort the edit.
        // Would have to pass this back up each function.

        return number;

    }

    // Second menu, linked only from first Edit menu.
    private static Vehicle editVehicle (Vehicle vehicle) {

        Menu editmenu = new Menu();
        boolean exitChosen = false;

        // Setup the menu
        // No welsh here yet :-(
        editmenu.setMenuTitle("Choose a column to edit:");
        editmenu.addMenuOption("Entry Date", Const.MENU_ENTRYD);
        editmenu.addMenuOption("Entry Time", Const.MENU_ENTRYT);
        editmenu.addMenuOption("Exit Date",Const.MENU_EXITD);
        editmenu.addMenuOption("Exit Time", Const.MENU_EXITT);
        editmenu.addMenuOption("Balance", Const.MENU_BALANCE);
        editmenu.addMenuOption("Flip 'in Carpark' flag", Const.MENU_ICP);
        editmenu.addMenuOption("View record", Const.SH_RECORD);
        editmenu.setoptionalMessage("Changes are SAVED on exit.");

        String status = "unedited, saved";

        do {
            System.out.println("");
            System.out.println(Ansicolours.fgYELLOW + "Edit Record (" + status + ")" + Ansicolours.RESET);
            System.out.println(Ansicolours.fgGREEN +"\t\tRegistration, Entry Date, Entry Time, Exit Date, Exit Time, Balance, In Carpark"+ Ansicolours.RESET);
            System.out.println("\t\t"+vehicle.toString());
            int menuOption = editmenu.display();

            // We know they will edit something or leave
            status = "edited, unsaved";

            // The reason we do it this way is that it's now possible to insert menu items
            // or reorder without worrying about the case statement values changing
                    switch (menuOption) {
                        case (Const.MENU_ENTRYD):
                            vehicle.setEntryDate(getDateFromUser());
                            break;
                        case (Const.MENU_ENTRYT):
                            vehicle.setEntryTime(getTimeFromUser());
                            break;
                        case (Const.MENU_EXITD):
                            vehicle.setExitDate(getDateFromUser());
                            break;
                        case (Const.MENU_EXITT):
                            vehicle.setEntryTime(getTimeFromUser());
                            break;
                        case (Const.MENU_BALANCE):
                            vehicle.zeroBalance();
                            vehicle.addToBalance(getBalanceFromUser());
                            break;
                        case (Const.MENU_ICP):

                            // Flip the InCarpark flag for the vehicle record
                            if (vehicle.getInCarpark().toUpperCase().equals("FALSE")) {

                                // If it's not left, when did it leave? We don't know yet.
                                vehicle.setInCarpark("True");
                                vehicle.setExitTime("NULL");
                                vehicle.setExitDate("NULL");
                            } else { 

                                // If it's left, when did it leave? Now. 
                                vehicle.setInCarpark("False");
                                vehicle.setExitDate(Utils.getDateNow());
                                vehicle.setExitTime(Utils.getTimeNow());
                            }
                            break;
                        case 99:
                            // Exit the menu
                            exitChosen = true;
                            break;
                    } // end switch
        } while (!exitChosen);  

        return vehicle;

    }

    // Get a date from a user
    private static String getDateFromUser () {

        Scanner getUserInput = new Scanner(System.in);
        boolean validDate = false;

        do {
                System.out.print("Enter a date (YYYY-MM-DD) > ");
                String userDate = getUserInput.nextLine();

                validDate = Utils.checkDateValid(userDate);

                if (validDate) {
                    return userDate;
                } else {
                    System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INVALID DATE ENTERED - 2020-01-01 to 2029-12-31 PLEASE]" + Ansicolours.RESET);
                }
            
        } while (!validDate);

        return "This will satisfy the compiler, but never ever be used.";

    }



    // Get a time from a user
    private static String getTimeFromUser () {

        // Get From a user scanner

        Scanner getUserInput = new Scanner(System.in);
        boolean validTime = false;

        do {
                
                System.out.print("Enter a time (HH:mm:ss) > ");
                String userTime = getUserInput.nextLine();

                validTime = Utils.checkTimeValid(userTime);

                if (validTime) {
                    return userTime;
                } else {
                    System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INVALID TIME ENTERED - 24 HOURS PLEASE]" + Ansicolours.RESET);
                }
            
        } while (!validTime);

        return "This will satisfy the compiler, but never ever be used.";

    }

 


    // Get a new balance from the user
    private static float getBalanceFromUser () {

        // Get From a user scanner
        Scanner getUserInput = new Scanner(System.in);
        System.out.print("Enter a new balance (" + Config.getValue("currency_symbol") + ") > ");

        float newBalance = getUserInput.nextFloat();

        // It doesn't matter how much you set it to, but it has to be positive or zero.
        if (newBalance < 0) {
            newBalance = 0.00f;
        }

        return newBalance;

    }


}

//
// End of File: Edit Menu Class
//