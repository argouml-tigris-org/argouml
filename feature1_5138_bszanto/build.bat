@echo off

rem
rem A skeleton script to call the real build script at 
rem ./argouml-build/build.bat
rem

setlocal
cd argouml-build
build.bat %1 %2 %3 %4 %5 %6 %7 %8 %9
endlocal
