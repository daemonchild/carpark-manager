// 
// File:     Vehicle Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
// 


package carparkmanager;

import java.util.ArrayList;

public class Vehicle {

    // Schema from Initial requirements
    private String vrn = "";
    private String entryDate = "";
    private String entryTime = "";
    private String exitDate = "";
    private String exitTime = "";

    // Extentions to Schema
    private String inCarpark = "";
    private float balance = (float) 0.00;

    public String toString() {

        String vehicleString = this.vrn + ", " + this.entryDate + ", " + this.entryTime + ", " + this.exitDate + ", " + this.exitTime + ", " + this.balance + ", " + this.inCarpark;
        return vehicleString;

    }

    // Public Methods
    public String getVRN () {

        return this.vrn;

    }

    public void setVRN (String vrnString) {

        this.vrn = vrnString;

    }

    public String getEntryDate () {

        return this.entryDate;

    }

    public String getEntryTime () {

        return this.entryTime;

    }

    public String getExitDate () {

        return this.exitDate;

    }

    public String getExitTime () {

        return this.exitTime;

    }

    public void setEntryDate (String dateString) {

        this.entryDate = dateString;

    }

    public void setEntryTime (String timeString) {

        this.entryTime = timeString;

    }

    public void setExitDate (String dateString) {

        this.exitDate = dateString;

    }

    public void setExitTime (String timeString) {

        this.exitTime = timeString;

    }


    // Methods for extended schema
    public float getBalance () {

        return this.balance;

    }

    public String getBalanceAsString () {

        
        String value = Float.toString(this.balance);
        return value;

    }

    public void zeroBalance () {

        this.balance = 0;

    }

    public void addToBalance (float value) {

        this.balance = this.balance + value;

    }

    public void setInCarpark (String value) {

        this.inCarpark = value;

    }

    public String getInCarpark () {

        return this.inCarpark;

    }

    private ArrayList<String> getDataStringArray () {

        ArrayList<String> tokens = new ArrayList<String>(7);

        tokens.add(this.vrn);
        tokens.add(this.entryDate);
        tokens.add(this.entryTime);
        tokens.add(this.exitDate);
        tokens.add(this.exitTime);
        tokens.add(Float.toString(this.balance));
        tokens.add(this.inCarpark);

        return tokens;

    }

    public String getDataString () {

        return Utils.createCSVLineString(getDataStringArray());

    }

    // General constructor for when we just want a vehicle container
    public Vehicle (){

        this.balance = (float) 0.00;

    }

    // Simplfy creating a new record
    public Vehicle (String vrn, String inCarPark){

        this.balance = (float) 0.00;
        this.vrn = vrn;
        this.inCarpark = inCarPark;
        this.entryDate = Utils.getDateNow();
        this.entryTime = Utils.getTimeNow();
        this.exitTime = "NULL";
        this.exitDate = "NULL";

    }

    // For adding a completely known vehicle
    public Vehicle (String[] tokens){

        this.vrn = tokens[0];
        this.entryDate = tokens[1];
        this.entryTime = tokens[2];
        this.exitDate = tokens[3];
        this.exitTime = tokens[4];
        this.balance = Float.parseFloat(tokens[5]);
        this.inCarpark= tokens[6];

    }

    
}
