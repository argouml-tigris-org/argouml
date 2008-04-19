@echo off

:: Build file configured for Eclipse project directory structure
:: build.bat always calls the version of ant distributed with ArgoUML

setlocal
set ANT_HOME=..\argouml-core-tools\apache-ant-1.7.0

:: Convert relative path to absolute
pushd %ANT_HOME%
set ANT_HOME=%CD%
popd

call "%ANT_HOME%\bin\ant" -Doverride.build.properties=eclipse-ant-build.properties %1 %2 %3 %4 %5 %6 %7 %8 %9

endlocal