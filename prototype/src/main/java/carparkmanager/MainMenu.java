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
        public static final int MENU_TRANSLATE                      = 1003;
        public static final int MENU_ADMINMODE                      = 1009;

    }

    public MainMenu () {}

    public void display () {

        boolean exitChosen = false;

        Menu mainMenu = new Menu();

        do {

            System.out.println("");

            String lang = "_" +Config.getValue("language");
            mainMenu.setMenuTitle(Ansicolours.fgGREEN + Config.getValue("mm_title"+lang) + "\t\t" + Ansicolours.RESET + Config.getValue("name"+lang));
            mainMenu.flushMenuOptions();
            mainMenu.addMenuOption(Config.getValue("mm_runmode"+lang), Const.MENU_RUNMODE_MANUAL);
            mainMenu.addMenuOption(Config.getValue("mm_adminmode"+lang), Const.MENU_ADMINMODE);
            mainMenu.addMenuOption(Config.getValue("mm_adminnetworkmode"+lang), Const.MENU_RUNMODE_NETWORK);
            mainMenu.addMenuOption(Config.getValue("mm_translate"+lang), Const.MENU_TRANSLATE);
            mainMenu.setoptionalMessage(Ansicolours.fgGREEN + Config.getValue("mm_currenttime"+lang) + " " + Ansicolours.RESET + Utils.getTimeNow()+ " " + Utils.getDateNow());

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

                case (Const.MENU_TRANSLATE):

                    if (Config.getValue("language").equals("english")) {

                        Config.setValue("language", "welsh");

                    } else {

                        Config.setValue("language", "english");

                    }
                    Utils.debugPrintln(Config.getValue("language"));
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
