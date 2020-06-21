# OS and Tools
Intellij Ultimate on Windows 10 64 bit

## Play Game
cd .\play_game\
java -jar <name>.jar

## Test Suite Contents   
1)Extract contents in Kalah.zip  
2)Open a new project in Intellij and copy over contents inside kalah_code

## Vertical Tests
cd .\kalah_code\
### Note:- Change gameView to V_VIEW in play method and gameMode to ALL_HUMAN
1)In Intellij->Project Structure->Libraries, select junit-3.8.2 and kalah-vertical.jar
2)In run configurations, make a new run configuration, select Junit and class: kalah.test.TestKalah

## Horizontal Tests 
cd .\kalah_code\
### Note:- Change gameView to H_VIEW in play method and gameMode to ALL_HUMAN
1)In Intellij->Project Structure->Libraries, select junit-3.8.2 and kalah.jar
2)In run configurations, make a new run configuration, select Junit and class: kalah.test.TestKalah

## Robot Tests
cd .\kalah_code\
### Note:- Change gameView to H_VIEW in play method and gameMode to P2_ROBOT
1)In Intellij->Project Structure->Libraries, select junit-3.8.2 and kalah-bmf.jar
2)In run configurations, make a new run configuration, select Junit and class: kalah.test.TestKalahBMFRobot
