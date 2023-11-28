// 
// File:     Utils Class
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; 

//
// Begin Class: Utils
//

public class Utils {

    // Creates a Comma separated line out of an list of strings
    public static String createCSVLineString (ArrayList<String> tokens) {

        int listSize = tokens.size();
        int count = 1;
        String csvLine = "";

        // Make sure not to end with a spare comma
        for (String token : tokens) {
            csvLine = csvLine + token;
            
            if (count != listSize) {
                csvLine = csvLine + ",";
                count ++;
            } else {
                csvLine = csvLine + "\n";
            }
        }
        return csvLine;
    }

    // Get a string of a date for 'right now'
    public static String getDateNow () {

        LocalDateTime theDateTime = LocalDateTime.now();
        return theDateTime.format(DateTimeFormatter.ISO_DATE);

    }

    // Get a string of a time for 'right now'
    public static String getTimeNow () {

        LocalDateTime theDateTime = LocalDateTime.now();
        return theDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

    }

    // Debug Function: Print only when debug:true in config file
    public static void debugPrintln (String message) {

        if (Config.getValue("debug_mode").equals("true")) {
            debugPrint(message + "\n");
        }
    }

    // Debug Function: Print only when debug:true in config file
    public static void debugPrint (String message) {

        if (Config.getValue("debug_mode").equals("true")) {
            System.out.print(Ansicolours.fgMAGENTA);
            System.out.print(message);
            System.out.print(Ansicolours.RESET);
        }    
    }

    
} // End utils

//
// End File: Run Mode Class
//
