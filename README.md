# Drone and medication dispatch rest sevice

The app  manages fleets of drone that are capable of carrying devices ,specifically, medication as load  for my use case.
The target task description is as listed below with coresponding endpoint, the current design approach is an item to  a drone with a single manager.
The embedded database is already preloaded with few items for both medication and drone .
The routine battery level check as functional requirment will log report to console.
For test purpose i assumed host to be localhost.

**Sample address** 
 - http://[host address]:8181/[endpoint]
**Task check with endpoints****
- registering a drone allows POST method /register
- loading a drone with medication items GET /selectdronemedication
- checking loaded medication items for a given drone; /device
- checking available drones for loading /selectdronemedication
- check drone battery level for a given drone /selectdronemedication
- viewing medication item picture /selectdronemedication

Prerequisites
 - Java 11 or newer
 - Gradle

**Resources**
 - Db,images and log files are in src/main/resources

**Install gradle on unix**

$ sudo add-apt-repository ppa:cwchien/gradle
$ sudo apt-get update
$ sudo apt-get install gradle

**Build**
 - $ cd thack4
 - $ ./gradlew jar



**Run it  from build/libs/**
 - $java -jar app-version-all.jar


Usage

The critical part towards getting  succeful response to every reuqest is by ensuring the following
-that all input for post method request is JSON formatted
-the post method require data to be sent with keyname  "json" as form-data parameter
-Adhere strictly to constraint imposed  on every field member of an entity


**Entity details:**
_For drone_
- serial_number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery_capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).
- lastmodified (timestamp)
  _For medication_
- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).
- status

**Usage**
 - To download a picture
http://localhost:8181/images?item=ASTYMIN_21EAKDIKKM.jpg

For instance to register a drone [http://localhost:8181/register]
 - method = POST
 - data = {"serial_number":"40734567","model":"Lightweight","weight":34,"battery_capacity":90,"state":"IDLE"}
 - curl --form "json={"serial_number":"40734567","model":"Lightweight","weight":34,"battery_capacity":90,"state":"IDLE"}" -X POST http://localhost:8181/register

To load medication to drone [http://localhost:8181/loaditems]
 - method = POST
 - data = {"drone":[serial_numner],"medication":[code]}
 - `curl --form "json={"drone":"23DSD5GH67","medication":"29EOPLIKKM"}" -X POST http://localhost:8181/loaditems`

***Response***
 - The response is also JSON formatted which corresponds to every request action in the below format
{"status":[],"message"}

---Please note that the value of status key  is custom defined.
