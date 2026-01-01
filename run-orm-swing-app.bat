it works with--- 
mvn -pl swing-app package -DskipTests
@echo off
REM Build and run the ORM-enabled Swing App using the shaded JAR
cd /d "%~dp0"
echo Building shaded JAR...
mvn -pl swing-app package -DskipTests
echo Launching Concordia ORM Swing App...
java -jar swing-app\target\concordia-swing.jar
