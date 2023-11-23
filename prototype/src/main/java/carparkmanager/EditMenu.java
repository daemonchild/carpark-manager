package carparkmanager;

import java.util.Scanner;
import java.util.ArrayList;

public class EditMenu {

    private static class Const {

        // Constants
        public static final int MENU_EDIT        = 1001;
        public static final int MENU_DELETE      = 1002;


        // Constants
        public static final int MENU_ENTRYD       = 2001;
        public static final int MENU_ENTRYT       = 2002;
        public static final int MENU_EXITD        = 2003;
        public static final int MENU_EXITT        = 2004;
        public static final int MENU_BALANCE      = 2005;
        public static final int MENU_ICP          = 2006;
        public static final int SH_RECORD         = 2007;

    }

    private String vrnString = "";

    public EditMenu (String vrn) {

        this.vrnString = vrn;
        
    }

    public void display () {
        Menu thisMenu = new Menu();

        boolean exitChosen = false;

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
                    Vehicle amendedVehicle = new Vehicle();
                    amendedVehicle = Database.getRecordByIndex(rows.get(chosenRow-1));
                    amendedVehicle = editVehicle (amendedVehicle);
                    Database.updateRecordByIndex(rows.get(chosenRow-1), amendedVehicle);
                    Utils.debugPrintln(amendedVehicle.toString());
                    break;
            
                case 99:
                    // Exit the menu
                    exitChosen = true;
                    break;
            } // end switch
        } while (!exitChosen);         

    }

    private static int getRowFromUser (int maxRow, String messageString) {

        // Get From a user scanner
        // Check for validity
        Scanner getUserInput = new Scanner(System.in);

        int number = 0;
        boolean validVRN = false;

        do {
            if (maxRow == 1) {

                System.out.println("You can only choose row 1.");

            } else if (maxRow == 2) {

                System.out.println("You may choose between rows 1 and 2.");

            } else {
                System.out.println("You can choose rows 1 to " + maxRow + ".");
            }

            System.out.print(messageString + " > ");
            number = getUserInput.nextInt();

            if (!(number > 0 & number <= maxRow)) {
                System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INVALID ROW ENTERED]" + Ansicolours.RESET);
            }
            
        } while (!(number > 0 & number <= maxRow));

        return number;

    }

    private static Vehicle editVehicle (Vehicle vehicle) {


        Menu editmenu = new Menu();
        boolean exitChosen = false;

        editmenu.setMenuTitle("Choose a column to edit:");
        editmenu.addMenuOption("Entry Date", Const.MENU_ENTRYD);
        editmenu.addMenuOption("Entry Time", Const.MENU_ENTRYT);
        editmenu.addMenuOption("Exit Date",Const.MENU_EXITD);
        editmenu.addMenuOption("Exit Time", Const.MENU_EXITT);
        editmenu.addMenuOption("Balance", Const.MENU_BALANCE);
        editmenu.addMenuOption("Flip 'in Carpark' flag", Const.MENU_ICP);
        editmenu.addMenuOption("View record", Const.SH_RECORD);
        editmenu.setoptionalMessage("Changes are SAVED on exit.");

        do {
            System.out.println("");
            System.out.println("Registration, EntryDate, EntryTime, ExitDate, ExitTime, Balance, InCarpark");
            System.out.println(vehicle.toString());
            int menuOption = editmenu.display();

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
                            if (vehicle.getInCarpark().toUpperCase().equals("FALSE")) {
                                vehicle.setInCarpark("True");
                                vehicle.setExitTime("NULL");
                                vehicle.setExitDate("NULL");
                            } else { 
                                vehicle.setInCarpark("False");
                                vehicle.setExitDate(Utils.getDateNow());
                                vehicle.setExitTime(Utils.getTimeNow());
                            }
                            break;
                        case (Const.SH_RECORD):
                            System.out.println(vehicle.toString());
                            break;
                        case 99:
                            // Exit the menu
                            exitChosen = true;
                            break;
                    } // end switch
        } while (!exitChosen);  

        return vehicle;

    }

    private static String getDateFromUser () {

        // Get From a user scanner
        // Check for validity
        Scanner getUserInput = new Scanner(System.in);

        boolean validDate = false;

        System.out.print("Enter a date (YYYY-MM-DD) > ");
        String userDate = getUserInput.nextLine();

        // To Do: Check for validity

        return userDate;


    }

    private static String getTimeFromUser () {

        // Get From a user scanner
        // Check for validity
        Scanner getUserInput = new Scanner(System.in);
        boolean validTime = false;

        System.out.print("Enter a time (HH:mm:ss) > ");
        String userTime = getUserInput.nextLine();

        // To Do: Check for validity

        return userTime;

    }

    private static float getBalanceFromUser () {

        // Get From a user scanner
        // Check for validity
        Scanner getUserInput = new Scanner(System.in);
        System.out.print("Enter a new balance ()" + Config.getValue("currency_symbol") + ") > ");

        float newBalance = getUserInput.nextFloat();

        // To Do: Check for validity

        return newBalance;

    }


}
