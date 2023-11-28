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

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// Import Libraries
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; 
import java.util.Date;

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


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Compare two dates and times
    public static int compareDateTime (String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int result = 0;
        Date dateTime1 = null;
        Date dateTime2 = null;

        try {
            dateTime1 = formatter.parse(date1);
            dateTime2 = formatter.parse(date2);
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        result = dateTime1.compareTo(dateTime2);
        return result;
    }

    // Return difference between datetimes, returns hours
    public static int getDiffDatetimeInHours (String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        Date dateTime1 = null;
        Date dateTime2 = null;

        try {
            dateTime1 = formatter.parse(date1);
            dateTime2 = formatter.parse(date2);
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        long difference = dateTime2.getTime() - dateTime1.getTime();
        long diffHours = difference / (3600 * 1000); 
        return Math.toIntExact(diffHours);
    }

    // Check for a valid date
    public static boolean checkDateValid (String dateString) {

        // Matching 202y-mm-dd
        final String regexString = "^202[0-9]-[0-1]{1}[0-9]-[0-3]{1}[0-9]";

        // Return boolean value based on regex match
         try {

            // First check, is it bascially a date?
            // This provides protection against weird strings.
            if (dateString.matches(regexString)) {

                // Second check
                String[] tokens = dateString.split("-");
                int year = Integer.parseInt(tokens[0]);
                int month = Integer.parseInt(tokens[1]);
                int day = Integer.parseInt(tokens[2]);

                if (year < 2020 || year >= 2030) {
                    return false;
                }

                if (month < 1 || month > 12) {
                    return false;
                }

                if (day < 1 || day > 31) {
                    return false;
                }

                // OK... You can still have the 31st Feb.. (and some others)
                if (month == 2 & day > 29) {
                    return false;
                }
                return true;

            } else {
                return false;
            }
       
        } // end try
        catch (Exception e) {
       
            // If things go wrong, fail gracefully.
            System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INTERNAL ERROR] VRN Legality Check failed." + Ansicolours.RESET);
            return false;
       
        } // end catch

    }

    // Check for a valid time
    public static boolean checkTimeValid (String timeString) {

        // Matching HH:mm:ss
        final String regexString = "[0-2][0-9]:[0-5][0-9]:[0-5][0-9]";

        // Return boolean value based on regex match
        try {

            if (timeString.matches(regexString)) {

                // Second check
                String[] tokens = timeString.split(":");
                int hour = Integer.parseInt(tokens[0]);
                int minute = Integer.parseInt(tokens[1]);
                int second = Integer.parseInt(tokens[2]);

                if (hour < 0 || hour > 23) {
                    return false;
                }

                if (minute < 0 || minute > 59) {
                    return false;
                }

                if (second < 0 || second > 59) {
                    return false;
                }

                return true;


            } else {
                return false;
            }
       
        } // end try
       catch (Exception e) {
       
            // If things go wrong, fail gracefully.
            System.out.println(Ansicolours.bgRED + Ansicolours.fgWHITE + "[INTERNAL ERROR] VRN Legality Check failed." + Ansicolours.RESET);
            return false;
       
        } // end catch
   
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
