# Energy Consumption Application
It s a Spring Boot Application. 
After compiling project u can start project with EnergyApplication class. 
Right click and Run as Java Application.

# Tools
- Eclipse with maven
- Spring Boot
- MySql

# API Documantation 
- http://localhost:8080/swagger-ui.html#
- http://localhost:8080/swagger-ui.html#/file-input-controller
- http://localhost:8080/swagger-ui.html#/fraction-controller
- http://localhost:8080/swagger-ui.html#/meter-reading-controller

# Implementation:

 1. I created 3 controller.  First controller for Fractions second
    controller for MeterReadings, third controller for file
    readings(bonus assesment)
 2.  Befor saving Fraction list and MeterReading list validation method is running.  
	 Fraction validation :
     - Validates if any fraction for a profile missed or not.
	 - Validates if any summary of fractions for every profile is 1 or not.
	 - Validates if there is already a fraction in db with same profile and month.
	 MeterReading Validation:
	 - Validates if any meterReading amount smaller than previous.
	 - Validates if there is  a fraction in db with same profile and month.
	 - Validates if there is already a MeterReading in db with same profile and month.
	 - Validates if consumption is in estimated range.
If any of them is not valid they are marked as REJECTED and saved in DB as REJECTED.
Befor saving I also calculated consumtion so it will be easier to retrieve calculated consumption.

# Test
I created for all api an integration test.
Created UnitTest class for MeterReadingService 

# Bonus Assesment
I created an FileInput Api, provides 2 method one is for Fractions file the other is 
for MeterReading file. After getting data from files it calls existing 
FractionService and MeterReadingService.


# Exception
I created global exception handler. If i cant catch error it will return as Bad Request(400).
I created custom exceptions MyForbiddenException and MyResourceNotFoundException. 
If any of them is not valid in fraction validation then throws MyForbiddenException()-> 403
While retrieving consumtion if there is no such a record it will return 
MyResourceNotFoundException->404.


# Tables
- meter_reading_info
colomns: id, meter_id, meter_reading, profile, month,consumption, meter_reading_info_status
- fraction_info
colomns: id,fraction,month,profile
