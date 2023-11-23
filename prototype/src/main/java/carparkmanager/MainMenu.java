// 
// File:     MainMenu Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 


package carparkmanager;

public class MainMenu {

    private static class Const {

        // Constants
        public static final int MENU_RUNMODE_MANUAL                 = 1001;
        public static final int MENU_RUNMODE_NETWORK                = 1002;
        public static final int MENU_ADMINMODE                      = 1009;

    }

    public MainMenu () {}

    public void display () {

        boolean exitChosen = false;

        Menu mainMenu = new Menu();
        mainMenu.setMenuTitle(Ansicolours.fgGREEN + "Main Application Menu\t" + Ansicolours.RESET + Config.getValue("name_welsh"));
        mainMenu.addMenuOption("Enter Run Mode", Const.MENU_RUNMODE_MANUAL);
        mainMenu.addMenuOption("Enter Admin Menu", Const.MENU_ADMINMODE);
        mainMenu.addMenuOption("Enter Network Run Mode " + Ansicolours.fgRED + "(Experimental)" + Ansicolours.RESET, Const.MENU_RUNMODE_NETWORK);
        mainMenu.setoptionalMessage(Ansicolours.fgGREEN + "Current time: " + Ansicolours.RESET + Utils.getTimeNow()+ " " + Utils.getDateNow());

        do {

            System.out.println("");
            int menuOption = mainMenu.display();

            // The reason we do it this way is that it's now possible to insert menu items
            // or reorder without worrying about the case statement values changing
            switch (menuOption) {
                case (Const.MENU_RUNMODE_MANUAL):

                    RunMode.manualEntry();
                    break;

                case (Const.MENU_RUNMODE_NETWORK):

                    RunMode.networkListener();
                    break;

                case (Const.MENU_ADMINMODE):

                    AdminMenu.display();
                    break;
            
                case 99:
                    // Exit the menu
                    exitChosen = true;
                    break;
            } // end switch
        } while (!exitChosen);         
        
    }


}
