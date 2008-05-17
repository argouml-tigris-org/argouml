@echo off

rem
rem build.bat always calls the version of ant distributed with ArgoUML
rem

set SAVE_ANT_HOME=%ANT_HOME%
set ANT_HOME=..\..\tools\ant-1.4.1
call %ANT_HOME%\bin\ant %1 %2 %3 %4 %5 %6 %7 %8 %9
set ANT_HOME=%SAVE_ANT_HOME%
set SAVE_ANT_HOME=
