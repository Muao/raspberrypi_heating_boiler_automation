It's my pet project for some automation of heating equipment in my home via raspberry pi 4B and java. 
I write here instructions for tuning raspberrian os (in manuals_for_tuning_of_raspberry_pi folder) and code. 
As it develops I going to add using equipment and configurations.

Now I use: 
-Java 11; 
-MariaDB for storage statistic data;
-Hibernate for DAO;
-TelegramAPI for control raspberry pi via telegram bot 
-log4j-slf4j bridge for getting all logs from all included jars (like hibernate) and put it to log file;
-pi4j for using GPIO.

I use ssh and wagon-maven-plugin for deploying app to raspberry pi from my ide (intellij idea) running on windows 10.
I make 'fat jar' with all needed (that comes via maven) dependencies for run on raspberry pi via maven-assembly-plugin.

DEVISES:
Raspberry Pi 4 Model B
SanDisk Extrime PRO microSDXC UHS-I card A2 64GB as a sistem storage
RPICT4T4 for connecting current and temperature sensors to a RaspberryPi (http://lechacalshop.com/gb/internetofthing/15-raspberrypi-4-current-sensor-adaptor-4-temperature-emoncms-0746092282428.html?search_query=RPICT4T4+Version+2.5&results=2)
Temperature sensors: DS18B20
Current sensors: SCT-013
