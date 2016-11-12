#!/bin/bash

javac MyBot.java
javac JTWebManV3.java
./halite -d "50 50" "java -jar build/libs/MyBot.jar" "java -jar oldbots/JTWebManV6.jar" "java -jar oldbots/JTWebManV5.jar" "java -jar oldbots/JTWebManV4.jar"
