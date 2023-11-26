// 
// File:     Config Class
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
import java.util.Base64;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

//
// Begin Class: Config
// 

public class Config {

    //
    // Begin SubClass: ConfigItem
    //
    private static class ConfigItem {

        //
        // Attributes
        //

        private String name = "";
        private String value = "";

        //
        // Constructors
        //

        public ConfigItem () {};

        public ConfigItem (String nameString, String valueString) {

            this.name = nameString;
            this.value = valueString;

        }

        //
        // Methods
        //

        public String getName () {
            return this.name;
        }

        public String getValue () {
            return this.value;
        }

        public void setName (String name) {
            this.name = name;
        }

        public void setValue (String value) {
            this.value = value;
        }

    } //end class ConfigItem

    //
    // Config Class Attributes
    //

    private static ArrayList<ConfigItem> configItems = new ArrayList<ConfigItem>(1);

    //
    // Constructors
    //

    public Config () {

        // Call provate function to load the config file
        this.loadConfigFile ();

        // The database location is a special case.
        // Windows and Linux / MacOS have different file path separators
        // The Linux version also sets this up for the Container file location inside Docker.
        
        String dbFolder = System.getProperty("user.dir");
        String dbFilePath = "";

        if ((System.getProperty("os.name").startsWith("Windows"))) {
            dbFilePath = dbFolder + "\\prototype\\database-files\\database.csv";
        } else {  
            dbFilePath = dbFolder + "/database-files/database.csv";
        }
        // Add it to the Config database
        ConfigItem newItem = new ConfigItem("db_file_path",dbFilePath);
        configItems.add(newItem);

    }

    //
    // Methods
    //

    // Return value for a key
    public static String getValue (String key) {

        for (ConfigItem configItem : configItems) {
            if (key.equals(configItem.name)) {
                //Utils.debugPrintln("Config.getValue("+ key + ") returning " + configItem.value);
                return configItem.value;
            } 
        } 
        return "NULL";

    }

    // Set value if key is already known to database
    public static void setValue (String key, String value) {

        for (ConfigItem configItem : configItems) {

            if (key.equals(configItem.name)) {
                configItem.setValue(value);
                break;
            }
        } 

    }

    // Returns App start up banner
    public static String appOpenBanner () {

        //String bannerString64 = Config.getValue("_app_banner_b64"); // Intended usage, but this does not decode correctly. Some file encoding problem? We load as UTF-8.
        String bannerString64 = "ICAgX19fICAgICAgICAgICAgICAgICAgICAgICAgICAgIF8gICAgICAgICAgICAgICAgICAgICAgICANCiAgLyBfX1xfXyBfIF8gX18gXyBfXyAgIF9fIF8gXyBfX3wgfCBfXyAgICAgICAgICBSb3NzIEdyYW50ICAgICAgICAgICAgICAgIA0KIC8gLyAgLyBfYCB8ICdfX3wgJ18gXCAvIF9gIHwgJ19ffCB8LyAvICAgICAgICAgIFNhbSBMb2Z0dXMgICAgICAgICAgICAgICAgDQovIC9fX3wgKF98IHwgfCAgfCB8XykgfCAoX3wgfCB8ICB8ICAgPCAgICAgICAgICAgVG9tIFJvd2FuICAgICAgICAgICAgICAgIA0KXF9fX18vXF9fLF98X3wgIHwgLl9fLyBcX18sX3xffCAgfF98XF9cICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICB8X3wgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIF8gICBfX18gIA0KICAvXC9cICAgX18gXyBfIF9fICAgX18gXyAgX18gXyAgX19fIF8gX18gIF9fICAgXy8gfCAvIF8gXCANCiAvICAgIFwgLyBfYCB8ICdfIFwgLyBfYCB8LyBfYCB8LyBfIFwgJ19ffCBcIFwgLyAvIHx8IHwgfCB8DQovIC9cL1wgXCAoX3wgfCB8IHwgfCAoX3wgfCAoX3wgfCAgX18vIHwgICAgIFwgViAvfCB8fCB8X3wgfA0KXC8gICAgXC9cX18sX3xffCB8X3xcX18sX3xcX18sIHxcX19ffF98ICAgICAgXF8vIHxfKF8pX19fLyANCiAgICAgICAgICAgICAgICAgICAgICAgICAgfF9fXy8gICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg==";
        byte[] byteArray = Base64.getDecoder().decode(bannerString64);;
        String bannerString = new String (byteArray);
        return Ansicolours.fgWHITE + bannerString + Ansicolours.RESET;

    }

    // Returns App close down banner
    public static String appCloseBanner () {

        return Ansicolours.MENUHEADER + Config.getValue("menu_app_bye_"+Config.getValue("language")) + Ansicolours.RESET;

    }

    // Debug Mode Method - Show entire config database
    public static void dumpConfigDebug () {

        Utils.debugPrintln(Integer.toString(configItems.size()));
        Utils.debugPrintln("Key\t\tValue");
        for (ConfigItem configItem : configItems) {

            Utils.debugPrintln(configItem.name + "\t\t" + configItem.value);
            
        }

    }

    //
    // Private helper functions
    //

    // Load the config file s
    private void loadConfigFile ()  {
    
        try {
            // This is a Java resource, so we need to ask for a handle
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream("config.txt");
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferreader = new BufferedReader(streamReader);
            String line = "";

            while ((line = bufferreader.readLine()) != null) {

                // We can comment the config file with a hash, it is ignored
                if (!line.startsWith("#")){ 
                    if (line.contains(":")) {
            
                        String key = line.split(":")[0];
                        String value = line.split(":")[1];
                        ConfigItem newItem = new ConfigItem(key,value);
                        configItems.add(newItem);

                    }
                }

            }

        inputStream.close();
        } catch (Exception e) {
            System.out.println(Ansicolours.ERROR + "An error occurred reading the config resource." + Ansicolours.RESET);
            e.printStackTrace();
        } // end catch

    }
  
} // end class Config

//
// End of File: Config Class
//



