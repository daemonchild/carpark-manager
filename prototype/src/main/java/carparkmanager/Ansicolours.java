package carparkmanager;

// Defines ANSI Colour Codes for use with println et al
public class Ansicolours {

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

    // Helpful Combos
    public static String WARN               = bgYELLOW + fgBLACK;
    public static String ERROR              = bgRED + fgBLACK;
    public static String GOOD               = bgCYAN + fgBLACK;
    public static String MENUHEADER         = fgGREEN;
    public static String MENUOPTION         = fgYELLOW;
    public static String MENUERROR          = bgRED + fgWHITE;

    public static String getString (String message, int colourValue) {

        // From a map of sorts I guess?
        String theString = "";

        // Look up in map - just yellow for now!
        String colour = fgYELLOW;

        theString = colour + message + RESET;

        return theString;

    }

}