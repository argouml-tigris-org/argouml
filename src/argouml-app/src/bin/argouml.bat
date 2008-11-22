@echo off
java -Xms64m -Xmx512m -cp argouml.jar org.argouml.application.Main %1 %2 %3 %4 %5 %6 %7 %8

if errorlevel 1 pause
