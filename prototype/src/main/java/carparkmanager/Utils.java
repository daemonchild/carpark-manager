package carparkmanager;

// Import Stuff
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; 

public class Utils {


    public static String createCSVLineString (ArrayList<String> tokens) {

        int listSize = tokens.size();
        int count = 1;
        String csvLine = "";

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

    public static String getDateNow () {

        LocalDateTime theDateTime = LocalDateTime.now();

        return theDateTime.format(DateTimeFormatter.ISO_DATE);

    }

    public static String getTimeNow () {

        LocalDateTime theDateTime = LocalDateTime.now();
        return theDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

    }

    public static void debugPrintln (String message) {

        debugPrint(message);
        System.out.println();

    }

    public static void debugPrint (String message) {

        if (Config.debugMode) {
            System.out.print(Ansicolours.fgMAGENTA);
            System.out.print(message);
            System.out.print(Ansicolours.RESET);
        }    
    }

    
}
