// 
// File:     VRN Class
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


public class VRN {

    public static String getFromUser () {

        // Get From a user scanner
        // Check for validity
        Scanner getUserInput = new Scanner(System.in);

        String vrnString = "";
        boolean validVRN = false;

        do {
            System.out.print(Config.getValue("vrn_enter_reg_"+Config.getValue("language"))+ " > ");
            vrnString = getUserInput.nextLine().toUpperCase();
            validVRN = checkValid(vrnString);

            if (!validVRN) {
                System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "["+Config.getValue("vrn_invalid_reg_"+Config.getValue("language"))+"]" + Ansicolours.RESET);
            }
            
        } while (!validVRN);

        return vrnString;

    }

    public static boolean checkValid (String vrnString) {

        // Ensure Uppercase
        vrnString = vrnString.toUpperCase();

        // Matching Modern UK VRNs (Work in progress)
        final String regexString = "^[A-Z]{2}[0-9]{2}[A-Z]{3}";

               // Return boolean value based on regex match
               try {

                    return vrnString.matches(regexString);
       
               } // end try
       
               catch (Exception e) {
       
                   // If things go wrong, fail gracefully.
                   System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INTERNAL ERROR] VRN Legality Check failed." + Ansicolours.RESET);
                   return false;
       
               } // end catch

    }

 
}
