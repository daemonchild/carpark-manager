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
// import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.awt.Font;

import carparkmanager.AsciiArt.Settings;

public class RunMode {

    static String exitCode = "QQ99QQQ";

    private static void printMenuHeader () {

        System.out.println("");
        System.out.println(Ansicolours.MENUHEADER + Config.getValue("rm_title_"+Config.getValue("language")) + Ansicolours.RESET);
        System.out.println(Config.getValue("rm_how_exit_"+Config.getValue("language")));
    }
    
    public static void manualEntry () {

        // Run mode as expected in requirements
        // Ask for a VRN, check it for being in the DB etc
        // Then entry or exit
        // etc etc etc


        boolean exitChosen = false;

        printMenuHeader();

        // Do stuff

        int loopCounter = 0;
        int maxBeforeHeader = 2; // Regular reminders of menu header
        do {

            System.out.println("");
            String vrnString = VRN.getFromUser();

            if (vrnString.equals(exitCode)) {

                // Secret exitCode chosen
                exitChosen = true;
                break;

            } else {

                ///

                drawCameraImage(vrnString);

                if (Database.getInDatabaseByVRN(vrnString)) {

                    if (Database.getInCarparkByVRN (vrnString)) {

                        //Utils.debugPrintln("VRN seen previously, departing.");
                        
                        Vehicle amendedVehicle = new Vehicle();

                        amendedVehicle = Database.getLatestDataByVRN(vrnString);

                        amendedVehicle.setExitDate(Utils.getDateNow());
                        amendedVehicle.setExitTime(Utils.getTimeNow());
                        amendedVehicle.setInCarpark("False");

                        // Calc the balance from the difference in times
                        
                        if (!amendedVehicle.getEntryDate().equals(amendedVehicle.getExitDate())) {

                            // We need to know how many days now.
                            amendedVehicle.addToBalance(999.99f);

                        } else {

                            DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm:ss");
                            LocalTime entryTime = LocalTime.parse(amendedVehicle.getEntryTime(),timeParser);
                            LocalTime exitTime = LocalTime.parse(amendedVehicle.getExitTime(),timeParser);

                            Duration timeElapsed = Duration.between (entryTime, exitTime);
                            int periods = (int) timeElapsed.toMinutes()/15;
                            float balance = periods * Float.parseFloat(Config.getValue("parking_fee"));
                            amendedVehicle.addToBalance(balance);

                        }

                        // Just do something nice here
                        String balanceString = "";

                        if (amendedVehicle.getBalance() < Float.parseFloat(Config.getValue("parking_fee"))) {

                            balanceString = Ansicolours.fgCYAN + "You have nothing to pay for this visit." + Ansicolours.RESET;

                        } else {

                            balanceString = "Please pay " +Ansicolours.fgCYAN+ "£" + amendedVehicle.getBalance()+ Ansicolours.RESET + " before departure.";

                        }

                        System.out.println(Ansicolours.fgYELLOW + "Thank you for parking with us today. "+Ansicolours.RESET);
                        System.out.println(balanceString);
                        System.out.println("Drive safely! We hope to see you again soon.");

                    } else {

                       // Utils.debugPrintln("VRN seen previously, arriving.");
                    
                        // A blank record, that is now in the carpark.
                        Vehicle newVehicle = new Vehicle(vrnString, "True"); 
                        Database.addRecord(newVehicle);
                        System.out.println(Ansicolours.fgGREEN+ "Welcome back to "+ Config.getValue("name_welsh") + "!" +Ansicolours.RESET);
                        System.out.println("Your arrival time is: " +Ansicolours.fgCYAN + newVehicle.getEntryDate() +" " + newVehicle.getEntryTime() +Ansicolours.RESET);
                        System.out.println("The parking fee at here is £" + Config.getValue("parking_fee") +" per full 15 minute period.");
                        System.out.println("Please proceed directly to an empty parking space.");
                    }

                } else {

                    //Utils.debugPrintln("VRN NOT seen previously, arriving.");
                    
                    // A blank record, that is now in the carpark.
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

    }

    private static void drawCameraImage(String vrnString) {

        System.out.println(Ansicolours.fgGREEN + Config.getValue("rm_anpr_image_"+Config.getValue("language")) + Ansicolours.RESET);
        Font plateFont = new Font("Arial", Font.PLAIN, 14);
        AsciiArt plateArt = new AsciiArt();
        Settings setup = new Settings(plateFont, 100, 20);      
        System.out.print(Ansicolours.fgYELLOW); 
        plateArt.drawString(vrnString, "#", setup);
        System.out.println(Ansicolours.RESET);

    }

    public static void networkListener () {

        // To Do -- extension

    }


}
