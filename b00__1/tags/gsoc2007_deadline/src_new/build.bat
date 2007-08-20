@echo off

rem
rem build.bat always calls the version of ant distributed with ArgoUML
rem

setlocal
set ANT_HOME=..\tools\apache-ant-1.7.0

:: Convert relative path to absolute
pushd %ANT_HOME%
set ANT_HOME=%CD%
popd

call %ANT_HOME%\bin\ant %1 %2 %3 %4 %5 %6 %7 %8 %9

endlocal