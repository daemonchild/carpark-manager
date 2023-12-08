// 
// File:     Main App Class ( ** Application Entrypoint ** )
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 

package carparkmanager;

// Begin Class: App 
public class App 
{
    public static void main( String[] args ) 
    {


        System.out.println(Ansicolours.CLS);

        // Load the Configuration file
        Config cfg = new Config ();

        // Attach to the database and load into memory
        Database.open();

        // Display Opening Banner Text
        System.out.print(Config.appOpenBanner());

        // Create MainMenu instance
        MainMenu mainMenu = new MainMenu();
        mainMenu.display();

        // Detach from database, and save
        Database.close();

        // Display Closing Banner Text
        System.out.print(Config.appCloseBanner());

    } //end main

} // end class App


//
// End of File: Main App Class
//
