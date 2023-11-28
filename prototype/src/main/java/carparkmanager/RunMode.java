// 
// File:     Run Mode Class
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
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.awt.Font;

// Need to import this specific sub class to create the banner
import carparkmanager.AsciiArt.Settings;

//
// Begin Class: RunMode 
//

public class RunMode {

    // This is how the user exits the ANPR simulation
    static String exitCode = "QQ99QQQ";
    
    public static void manualEntry () {

        // Run mode - Simulated ANPR System

        boolean exitChosen = false;

        // Print the menu header, we will repeat regularly
        printMenuHeader();
        int loopCounter = 0;
        int maxBeforeHeader = 2; 

        // Run Mode Loop
        do {

            System.out.println("");

            // get a valid VRP
            String vrnString = VRN.getFromUser();

            if (vrnString.equals(exitCode)) {

                // 'Secret' exitCode chosen
                exitChosen = true;
                break;

            } else {

                // Draw Fake ANPR Camera Image
                drawCameraImage(vrnString);

                // If in the database, we have seen this vehicle before
                if (Database.getInDatabaseByVRN(vrnString)) {


                    if (Database.getInCarparkByVRN (vrnString)) {

                        // Is it in the carpark? If so, it's leaving now.
                        // Update it.
                        Vehicle amendedVehicle = new Vehicle();

                        amendedVehicle = Database.getLatestDataByVRN(vrnString);

                        amendedVehicle.setExitDate(Utils.getDateNow());
                        amendedVehicle.setExitTime(Utils.getTimeNow());
                        amendedVehicle.setInCarpark("False");

                        // Calc the balance from the difference in times
                        
                        if (!amendedVehicle.getEntryDate().equals(amendedVehicle.getExitDate())) {

                            // To do - We need to know how many days now, so fine them 100 for now.
                            amendedVehicle.addToBalance(100);

                        } 
                        else {

                            DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm:ss");
                            LocalTime entryTime = LocalTime.parse(amendedVehicle.getEntryTime(),timeParser);
                            LocalTime exitTime = LocalTime.parse(amendedVehicle.getExitTime(),timeParser);

                            Duration timeElapsed = Duration.between (entryTime, exitTime);
                            int periods = (int) timeElapsed.toMinutes()/15;
                            float balance = periods * Float.parseFloat(Config.getValue("parking_fee"));
                            amendedVehicle.addToBalance(balance);

                        }

                        // User experience
                        String balanceString = "";

                        if (amendedVehicle.getBalance() < Float.parseFloat(Config.getValue("parking_fee"))) {

                            balanceString = Ansicolours.fgCYAN + "You have nothing to pay for this visit." + Ansicolours.RESET;

                        } else {

                            balanceString = "Please pay " +Ansicolours.fgCYAN+ "£" + amendedVehicle.getBalance()+ Ansicolours.RESET + " before departure.";

                        }

                        System.out.println(Ansicolours.fgYELLOW + "Thank you for parking with us today. " + Ansicolours.RESET);
                        System.out.println(balanceString);
                        System.out.println("Drive safely! We hope to see you again soon.");

                    } else {
                    
                        // We have seen this vehicle before, so park it.
                        Vehicle newVehicle = new Vehicle(vrnString, "True"); 
                        Database.addRecord(newVehicle);
                        System.out.println(Ansicolours.fgGREEN+ "Welcome back to "+ Config.getValue("name_welsh") + "!" +Ansicolours.RESET);
                        System.out.println("Your arrival time is: " +Ansicolours.fgCYAN + newVehicle.getEntryDate() +" " + newVehicle.getEntryTime() +Ansicolours.RESET);
                        System.out.println("The parking fee at here is £" + Config.getValue("parking_fee") +" per full 15 minute period.");
                        System.out.println("Please proceed directly to an empty parking space.");
                    }

                } else {
             
                    // Never seen before, add to the database and welcome them.
                    Vehicle newVehicle = new Vehicle(vrnString, "True"); 
                    Database.addRecord(newVehicle);
                    System.out.println(Ansicolours.fgGREEN + "A very warm welcome!"+Ansicolours.RESET  +" We notice you've never been to "+ Config.getValue("name_welsh")+" before.");
                    System.out.println("Your arrival time is: " +Ansicolours.fgCYAN + newVehicle.getEntryDate() +" " + newVehicle.getEntryTime() +Ansicolours.RESET);
                    System.out.println("The parking fee here is £" + Config.getValue("parking_fee") +" per full 15 minute period.");
                    System.out.println("Please proceed directly to an empty parking space.");

                }

            }
        
        // Print a reminder header every so often
        if (loopCounter == maxBeforeHeader) {
            printMenuHeader();
            loopCounter = 0;
        } else {
            loopCounter++;
        }


        } while (!exitChosen);
        
        Utils.debugPrintln("[Exit code entered.]");
        Database.save();        // Save the database because edits have been made
                                // A real database would commit instantly. 
                                // This seems a reasonable compromise for the prototype.

    }

    // Use downloaded ASCII Art library to print a reg plate banner
    private static void drawCameraImage(String vrnString) {

        System.out.println(Ansicolours.fgGREEN + Config.getValue("rm_anpr_image_"+Config.getValue("language")) + Ansicolours.RESET);

        // Configure ART Settings
        Font plateFont = new Font("Arial", Font.PLAIN, 14);
        AsciiArt plateArt = new AsciiArt();
        Settings setup = new Settings(plateFont, 100, 20);      

        // Generate and print the reg plate banner
        System.out.print(Ansicolours.fgYELLOW); 
        plateArt.drawString(vrnString, "#", setup);
        System.out.println(Ansicolours.RESET);

    }

    // Repeated multiple times, so functionalised
    private static void printMenuHeader () {

        System.out.println("");
        System.out.println(Ansicolours.MENUHEADER + Config.getValue("rm_title_"+Config.getValue("language")) + Ansicolours.RESET);
        System.out.println(Config.getValue("rm_how_exit_"+Config.getValue("language")));
    }

    // To Do - Extension to simulate remote ANPR devices
    public static void networkListener () {}

}

//
// End File: Run Mode Class
//
