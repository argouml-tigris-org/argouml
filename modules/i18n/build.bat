@echo off

if not "%ANT_HOME%"=="" goto CHECKJAVA
rem missing variable: ANT_HOME
echo ******************************************************************
echo      Warning: ANT_HOME environment variable is not set.
echo   Please set ANT_HOME to the installation directory of the build
echo   utility Ant (this should be the one containing \bin and \lib
echo   directories). The latest version of ANT can be found on the
echo   Apache Jakarta Project site: http://jakarta.apache.org/
echo ******************************************************************
goto END

:CHECKJAVA
if "%JAVACMD%" == "" set JAVACMD=JAVA
set LOCALCP=%CLASSPATH%;%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\jaxp.jar;%ANT_HOME%\lib\parser.jar

rem add tools.jar
if "%JAVA_HOME%" == "" goto NOJAVAHOME
if exist %JAVA_HOME%\lib\tools.jar SET LOCALCP=%LOCALCP%;%JAVA_HOME%\lib\tools.jar
if exist %JAVA_HOME%\jre\lib\classes.zip SET LOCALCP=%LOCALCP%;%JAVA_HOME%\jre\lib\classes.zip
goto RUNANT

:NOJAVAHOME
echo ***********************************************************
echo    Warning: JAVA_HOME environment variable is not set.
echo   Please set the JAVA_HOME environment variable to the 
echo   directory where the JDK is installed.
echo ***********************************************************
goto END

:RUNANT
%JAVA_HOME%\BIN\%JAVACMD% -classpath %LOCALCP% -Dant.home=%ANT_HOME% -Dant.opts=%ANT_OPTS% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

:END
set LOCALCP=
