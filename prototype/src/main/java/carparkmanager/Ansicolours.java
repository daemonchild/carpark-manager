// 
// File:     ANSI Colours Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 

package carparkmanager;

// Defines ANSI Colour Codes (American National Standards Institute)
// Reference: https://gist.github.com/Prakasaka/219fe5695beeb4d6311583e79933a009

// Begin: Class 
public class Ansicolours {

    //
    // Attributes
    //

    // Foreground Colours
    public static String fgBLUE = "\u001B[34m";
    public static String fgCYAN = "\u001B[36m";
    public static String fgRED = "\u001B[31m";
    public static String fgGREEN = "\u001B[32m";
    public static String fgWHITE = "\u001B[37m";
    public static String fgYELLOW = "\u001B[33m";
    public static String fgBLACK = "\u001B[30m";
    public static String fgMAGENTA = "\u001B[35m";

    // Background Colours
    public static String bgBLUE = "\u001B[44m";
    public static String bgCYAN = "\u001B[46m";
    public static String bgRED = "\u001B[41m";
    public static String bgGREEN = "\u001B[42m";
    public static String bgWHITE = "\u001B[47m";
    public static String bgYELLOW = "\u001B[43m";
    public static String bgBLACK = "\u001B[40m";
    public static String bgMAGENTA = "\u001B[45m";

    // Reset Code
    public static String RESET  = "\u001B[0m";


    public final String CLS = "\u001b[2J";

    // Helpful Combinations
    public static String WARN               = bgYELLOW + fgBLACK;
    public static String ERROR              = bgRED + fgBLACK;
    public static String GOOD               = bgCYAN + fgBLACK;
    public static String MENUHEADER         = fgGREEN;
    public static String MENUOPTION         = fgYELLOW;
    public static String MENUERROR          = bgRED + fgWHITE;

    //
    // Methods
    //

    // Returns a string wrapped by fgYellow
    public static String getString (String message, String colourValue) {

        // Needs fixing to look up in a map.
        String theString = "";
        theString = fgYELLOW + message + RESET;
        return theString;

    }

}

//
// End of File: ANSI Colours Class
//
