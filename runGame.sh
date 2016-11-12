#!/bin/bash

javac MyBot.java
javac JTWebManV3.java
./halite -d "30 30" "java -jar build/libs/MyBot.jar" "java -jar oldbots/JTWebManV5.jar" "java -jar oldbots/JTWebManV4.jar"
