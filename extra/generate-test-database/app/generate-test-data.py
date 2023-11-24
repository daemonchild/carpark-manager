# 
# File:     Generate Test Data
#
# Course:   DAT4001 
# Date:     Autumn 2023
# Group:    Richard Greenwood
#           Ross Grant
#           Sam Loftus
#           Tom Rowan
# 

# Import Libraries
from datetime import datetime 
import time
import random
import string
import math

# Functions

# Create Random DateTime
def GetRandomDate(start_datetime, end_datetime):
    
    format_string = '%d-%m-%Y %H:%M:%S'
    
    # Create dates from start and end parameters
    start_time = time.mktime(time.strptime(start_datetime, format_string))
    end_time = time.mktime(time.strptime(end_datetime, format_string))
    
    # Random time, between the two time parameters
    rnd_datetime = start_time + random.random() * (end_time - start_time)
    return datetime.fromtimestamp(time.mktime(time.localtime(rnd_datetime)))


# Generate Random uppercase 'word'
def GetRandomWordUpper(length):
   letters = string.ascii_uppercase
   return ''.join(random.choice(letters) for i in range(length))
 

# Areacode Functions

# Custom data structure for DVLA areas
# The Areacode is the first two letters of a modern UK reg plate
class Areacode:
    code = ""
    location = ""
    type = ""

# Load the aread codes from a file
def LoadAreaCodeFile ():
    
    # Data obtained from : "https://www.car.co.uk/media/guides/number-plates/uk-number-plate-area-codes"
    # Accessed: 02/11/2023
    
    # Empty the Global database
    area_codes_list = []
    try:
        file_handle_new = open("areacodes-new-type.dat", "r")     
        new_type_data = file_handle_new.readlines()
        file_handle_new.close()
    except:
        print ("[FAIL] Opening area code data file failed.")
        
    # Data is rows like this: Cardiff,CA,CB,CC,CD,CE,CF,CG,CH,CJ,CK,CL,CM,CN,CO
    
    # Process each line and insert into the database of areacodes
    for line in new_type_data:
        
        tokens = line.rstrip().split(",")
        
        # Extract the first word
        location = tokens[0]
        tokens.remove(location)
        
        # Set up the object and add to the database
        for token in tokens:
            ac = Areacode()
            ac.location = location
            ac.code = token
            ac.type = "new"
            area_codes_list.append(ac)
    
    return area_codes_list


# Look up a DVLA area location from a number plate
# Not really needed, but simple to code
def GetLocationFromVRN (number_plate):
    
    areacode = number_plate[:2]
    for code in area_codes_list:
        if code.code == areacode:
            return (code.location)
            break
    return "Illegal"


# Get a random area code from the database
def GetRandomAreaCode ():
    random_number = random.randint(0, len(area_codes_list)-1)
    return area_codes_list[random_number].code

# The modern type reg plate is as follows:
# AREACODE YEARCODE [A-Z]{3}, eg: AB123DEF
# The area code gives the local DVLA office
# The year code is the year, but if the car was registered at the 6 month mark, you add 50.
# These registration plates started in 2001
# The last three are random letters.

def GetRandomUKVRN ():

    start_year = 1   #2001
    this_year =  int(datetime.now().strftime("%y"))   #23 currently
    
    # Pick a valid year of registration
    random_year = random.randint(start_year, this_year)
    
    # flip a coin - 0 or 1
    flipCoin = random_number = random.randint(0, 1)
    if flipCoin == 0:
        # Reg is from 6 month change
        random_year = random_year + 50
    
    # We need a string
    year_segment = str(random_year)
    
    # Padding with zeroes
    if len (year_segment) == 1:
        year_segment = "0" + year_segment
        
    return (GetRandomAreaCode() + year_segment + GetRandomWordUpper(3))
        


# Create the data log for several days
def GenerateData (number_to_create_each_day, day_count):
    
    # Empty the arrays for building the data
    the_log = []
    seen_vrns = []

    # Create data for each day in turn
    for day in range(1,day_count+1):
        
        print ("+", end="")

        # Repeat to create a number of entries per day
        for count in range(1,number_to_create_each_day):

            # If it is after the first day, we can reuse previous reg numbers
            # Locals tend to reuse the carpark.
            
            if day > 1:
                
                print (".", end="")

                diceRoll = random.randint(0, 5)     
                if diceRoll <= 1:                   # roll 1 or 2 on a 6 sided die
                    # Take one from the list of previously seen number plates
                    index = random.randint(0, len(seen_vrns)-1)
                    number_plate=seen_vrns[index]
                else:
                    # We will generate a new one                 
                    number_plate = (GetRandomUKVRN())
            
            else:
                # On day one, we will always generate a new one                 
                    number_plate = (GetRandomUKVRN())
                    
            # Add back into the seen_vrns list every time
            # Simulates Regular visitors
            seen_vrns.append(number_plate)

            # Create two random datetime objects
            thedate = str(day) + "-" + MONTHYEAR
            
            # Pad the string
            if day < 10:
                thedate = "0" + thedate 
                
            rnd_datetime_a = GetRandomDate(thedate + " " + CARPARK_OPEN, thedate + " " + CARPARK_CLOSE)
            rnd_datetime_b = GetRandomDate(thedate + " " + CARPARK_OPEN, thedate + " " + CARPARK_CLOSE)
                
            # Which is earliest?
            if rnd_datetime_a < rnd_datetime_b:
                entry_datetime = rnd_datetime_a
                exit_datetime = rnd_datetime_b
            else:
                entry_datetime = rnd_datetime_b
                exit_datetime = rnd_datetime_a
            
            entry_date = entry_datetime.strftime("%Y-%m-%d")
            entry_time = entry_datetime.strftime("%H:%M:%S")
            
            now = datetime.now()
            
            # We do different things when the random exit time is in the future
            if exit_datetime > now:
                in_car_park = "True"
                exit_date = "NULL"
                exit_time = "NULL"
                # parking_periods = round((((now-entry_datetime).seconds)/3600)*4,2)
                parking_periods = math.floor((((now-entry_datetime).seconds)/3600)*4)
            else:
                in_car_park = "False"
                exit_date = exit_datetime.strftime("%Y-%m-%d")
                exit_time = exit_datetime.strftime("%H:%M:%S")
                parking_periods = math.floor((((exit_datetime-entry_datetime).seconds)/3600)*4)

            # You always pay for a small stay, caters for edge case random times
            if parking_periods == 0:
                parking_periods = 1

            balance = parking_periods * PERIOD_FEE
           
            # Create a CSV string and add to the log database
            log_string = number_plate + "," + \
                entry_date + "," + \
                entry_time + "," + \
                exit_date + "," + \
                exit_time + "," + \
                str(balance) + "," + \
                in_car_park
            
            the_log.append(log_string)
        
    # Return both arrays
    print ("")
    return the_log, seen_vrns


# Count how many times a VRN appears
def GetCountByVRN (vrn):
        return seen_vrns.count(vrn)


# Counts all the VRNs in the datase
def GetCountAllVRN (list_of_vrns):
    
    for vrn in list_of_vrns:
        print ("VRN "+vrn+" seen "+ str(CountVRN(vrn)) + " times.")


### Main Program Code ###

# Globals Constants

global PERIOD_FEE
global CARPARK_OPEN
global CARPARK_CLOSE
global MONTHYEAR
global DAYCOUNT
global NUMBER_PER_DAY
global area_codes_list

PERIOD_FEE = 0.30               # 0.30 per 1/4 hour, bargain
CARPARK_OPEN = "05:30:00"
CARPARK_CLOSE = "22:30:00"
MONTHYEAR = "11-2023"
DAYCOUNT = 23
NUMBER_PER_DAY = 50

print ("[HELLO] Generating Test Data File.")

# Load area codes data
area_codes_list = LoadAreaCodeFile()

# Generate some daily log data
the_log, seen_vrns = GenerateData (NUMBER_PER_DAY, DAYCOUNT)

# Save to a file
try:
    file_name = "output/database.csv"
    file_handle = open(file_name, "w")
    file_handle.write ("VRN,EntryDate,EntryTime,ExitDate,ExitTime,Balance,InCarpark\n")
    for line in the_log:
        file_handle.writelines(line+"\n")
    file_handle.close()
    print ("[SUCCESS] Writing output file was fine.")
except:
    print ("[FAIL] Writing output file failed.")
    
print ("[GOODBYE] Right, we're done.")



        