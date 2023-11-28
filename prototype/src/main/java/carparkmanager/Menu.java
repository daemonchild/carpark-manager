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

// Import Libraries
import java.util.ArrayList;
import java.util.Scanner;

//
// Begin Class: Menu
//

public class Menu {

    // The other menu classes use the Menu class to build and display menus


    //
    // Begin Subclass: MenuItem
    //
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

    //
    // Attributes
    //

    String menuTitle = "";
    String optionalMessage = "";

    // List of Menu Options
    ArrayList<MenuItem> menuOptions = new ArrayList<MenuItem>(0);

    //
    // Constructors
    //

    public Menu () {

    }


    //
    // Methods
    //

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

    public void addMenuOption (String optionText, int function) {

        // Create new 
        MenuItem newItem = new MenuItem();

        int optionNumber = menuOptions.size() + 1;
        newItem.setOption(optionNumber);
        newItem.setFunction(function);
        newItem.setOptionText(optionText);

        // Add to the list
        this.menuOptions.add(newItem);
    }

    // Remove all the options without destroying the entire menu
    public void flushMenuOptions () {
        this.menuOptions.clear();
    }

    // Display the menu and return a chosen option (returns the CONSTANT)
   public int display () {

        boolean exitChosen = false;

        System.out.print(Ansicolours.RESET);
        System.out.print(Ansicolours.MENUHEADER); // NB no newline
        System.out.print(this.menuTitle);
        System.out.print(Ansicolours.RESET);

        // Padding width for subtitle message
        int padding = 80 - (this.optionalMessage.length());
        for (int p = 0; p < padding ; p++ ) {
            System.out.print(" ");
        }
        System.out.println(this.optionalMessage+ Ansicolours.RESET);

        // Menu Loop
        do {

            String menuLine = "";

            // Display the menu
            System.out.print(Ansicolours.getString(menuLine, "fgYELLOW")); // NB no newline
            for (int index = 0; index < menuOptions.size(); index++) {

                menuLine = getMenuLineString(index);
                System.out.println(menuLine);

            }

            // All menus use 99 to quit.
            System.out.print(Ansicolours.fgYELLOW);
            System.out.println("99.\t"+ Config.getValue("menu_quitmenu_"+Config.getValue("language")) +Ansicolours.RESET);

            // Get an option from the user.
            System.out.print(Ansicolours.fgBLUE + Config.getValue("menu_opt_prmt_"+Config.getValue("language")) + "> " + Ansicolours.RESET); 
            int menuOption = getMenuOption();

            // Process the options
            if (menuOption == 99) {
                exitChosen = true;
                break;
            }
            else if (menuOption > 0 & menuOption <= menuOptions.size()) {
                return menuOptions.get(menuOption-1).function;
            } 
            else {
                System.out.println(Ansicolours.MENUERROR + "[INVALID OPTION - CHOOSE 1 to " + menuOptions.size() + "]" + Ansicolours.RESET);
            }

        } while (!exitChosen);

        return 99;

    } //End Run


    // Checks if a string is an number by trying to convert to an Integer
    private boolean isNumber(String inputString) {

        // If the string is empty, it's not a number
        if (inputString == null) {
            return false;
        }
        // If the string is not a number this will throw an exception
        try {
            int integer = Integer.parseInt(inputString);
        } catch (NumberFormatException nfe) {
            // Not a number then.
            return false;
        }
        // It was a number.
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

    // Produce a single string with the menu option
    private String getMenuLineString (int index) {

        String menuOptionLineString;
        menuOptionLineString = Integer.toString(this.menuOptions.get(index).option) + ".\t" + this.menuOptions.get(index).optionText;
        return menuOptionLineString;

    }

} //end Menu class

//
// End of File: Menu Class
//