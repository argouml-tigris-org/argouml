rem @echo off
if not "%ANT_HOME%"=="" goto checkJava

rem missing variable: ANT_HOME
echo.
echo ANT_HOME environment variable is not set.
echo   Please set ANT_HOME to the installation directory of Ant 
echo   (this should be the one containing \bin and \lib directory).
echo   Latest version can be found on the Apache Jakarta Project site:
echo   http://jakarta.apache.org/
echo.
goto END


:CHECKJAVA
if "%JAVACMD%" == "" set JAVACMD=java
set LOCALCP=%CLASSPATH%;%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\jaxp.jar;%ANT_HOME%\lib\parser.jar


rem add tools.jar
if "%JAVA_HOME%" == "" goto NOJAVAHOME
if exist %JAVA_HOME%\lib\tools.jar SET LOCALCP=%LOCALCP%;%JAVA_HOME%\lib\tools.jar
if exist %JAVA_HOME%\jre\lib\classes.zip SET LOCALCP=%LOCALCP%;%JAVA_HOME%\jre\lib\classes.zip
goto RUNANT


:NOJAVAHOME
echo.
echo Warning: JAVA_HOME environment variable is not set.
echo   If build fails because sun.* classes could not be found
echo   you will need to set the JAVA_HOME environment variable
echo   to the directory where the JDK is installed.
echo.


:RUNANT
%JAVACMD% -classpath "%LOCALCP%" -Dant.home="%ANT_HOME%" %ANT_OPTS% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9


:END
set LOCALCLASSPATH=