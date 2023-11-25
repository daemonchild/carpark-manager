// 
// File:     Menu Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 

package carparkmanager;
import java.util.ArrayList;
import java.util.Scanner;

// Expirimental
import java.lang.reflect.*;
import java.io.*;



public class Menu {

    private class MenuItem {

        private int option;
        private String optionText;
        private int function;

        public void setOption(int option) {
            this.option = option;
        }
        
        public int getOption() {
            return this.option;
        }

        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }
        
        public String getOptionText() {
            return this.optionText;
        }
        
        public void setFunction(int function) {
            this.function = function;
        }

        public int getFunction() {
            return this.function;
        }

    }

    public String getMenuTitle() {
        return this.menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getoptionalMessage() {
        return this.optionalMessage;
    }

    public void setoptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }

    String menuTitle = "";

    String optionalMessage = "";

        // List of Menu Options
    ArrayList<MenuItem> menuOptions = new ArrayList<MenuItem>(0);


    public Menu () {

    }


    private String getMenuLineString (int index) {

        String menuOptionLineString;

        menuOptionLineString = Integer.toString(this.menuOptions.get(index).option) + ".\t" + this.menuOptions.get(index).optionText;
        return menuOptionLineString;

    }

    public void addMenuOption (String optionText, int function) {

        // Create new 
        MenuItem newItem = new MenuItem();

        int optionNumber = menuOptions.size() + 1;

        newItem.setOption(optionNumber);
        newItem.setFunction(function);
        newItem.setOptionText(optionText);

        this.menuOptions.add(newItem);
    }

    public void flushMenuOptions () {

        this.menuOptions.clear();
    }

    public void addMenuOptionHMM (String optionKey, int function) {

        // Create new 
        MenuItem newItem = new MenuItem();

        int optionNumber = menuOptions.size() + 1;

        newItem.setOption(optionNumber);
        newItem.setFunction(function);
        newItem.setOptionText(optionKey);

        this.menuOptions.add(newItem);
    }

    private boolean isNumber(String inputString) {

        // If it's empty, just give up
        if (inputString == null) {
            return false;
        }
        // This could throw an exception
        try {
            int integer = Integer.parseInt(inputString);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // Returns a number from user input.
    private int getMenuOption () {

        Scanner getMenuOption = new Scanner(System.in);
        String menuOptionString = "";
        int menuOption;
        // This is quite trusting of user input at the moment.
        menuOptionString = getMenuOption.nextLine();
        

        if (isNumber(menuOptionString)) {
            menuOption = Integer.parseInt(menuOptionString);
            return menuOption;
        }
        else {
            // Allows for a menu option zero if needed
            return -1;


        } //end if else      

    }

    public int display () {

        boolean exitChosen = false;

        System.out.print(Ansicolours.RESET);
        System.out.print(Ansicolours.MENUHEADER); // NB no newline
        System.out.print(this.menuTitle);
        System.out.print(Ansicolours.RESET);
        // Padding width
        int padding = 75 - this.optionalMessage.length();
        for (int p = 0; p < padding ; p++ ) {

            System.out.print(" ");

        }
        System.out.print(this.optionalMessage);
        System.out.println(Ansicolours.RESET);

        do {

            String menuLine = "";

            // Display the menu
            System.out.print(Ansicolours.getString(menuLine, 1)); // NB no newline
            for (int index = 0; index < menuOptions.size(); index++) {

                menuLine = getMenuLineString(index);
                System.out.println(menuLine);

            }
            System.out.print(Ansicolours.fgYELLOW);
            System.out.println("99.\t"+ Config.getValue("menu_quitmenu_"+Config.getValue("language")) +Ansicolours.RESET);

            System.out.print(Ansicolours.fgBLUE + Config.getValue("menu_opt_prmt_"+Config.getValue("language")) + "> " + Ansicolours.RESET); 
            int menuOption = getMenuOption();

            // Process the options
            if (menuOption == 99) {
                exitChosen = true;
                break;
            }
            else if (menuOption > 0 & menuOption <= menuOptions.size()) {
                return menuOptions.get(menuOption-1).function;
            } else
            {
                System.out.println(Ansicolours.MENUERROR + "[INVALID OPTION]" + Ansicolours.RESET);
            }

        } while (!exitChosen);

        return 99;



    } //End Run

} //end Menu class
