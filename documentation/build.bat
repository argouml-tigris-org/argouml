@echo off

rem Author: Kunle Odutola
rem         May 2001
rem
rem $Id$
rem

rem +-------------------------------------------------------------------------+
rem ! Verify and Set Required Environment Variables                           !
rem +-------------------------------------------------------------------------+

@if "%OS%" == "Windows_NT"  setlocal

rem 	+---------------------------------------------------------------------+
rem 	! Check for existence of ANT_HOME environment variable                !
rem 	+---------------------------------------------------------------------+

if not "%ANT_HOME%" == "" goto check_JAVA_HOME

rem 		+-------------------------------------------------------------+
rem 		! ANT_HOME environment variable not found                     !
rem 		!                                                             !
rem 		! We will point ANT_HOME to our private copy of Ant           !
rem 		+-------------------------------------------------------------+

set ANT_HOME=..\lib

:check_JAVA_HOME
rem 		+-------------------------------------------------------------+
rem 		! Check for existence of JAVA_HOME environment variable       !
rem 		+-------------------------------------------------------------+

if "%JAVA_HOME%" == "" goto ERROR_JAVA_HOME

rem 		+-------------------------------------------------------------+
rem 		! Add Java's tools.jar to the classpath for running Ant       !
rem 		+-------------------------------------------------------------+

set LOCAL_CLASSPATH=
if exist %JAVA_HOME%\lib\tools.jar        SET LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;%JAVA_HOME%\lib\tools.jar

rem For JDK1.1 only
rem if exist %JAVA_HOME%\jre\lib\classes.zip  SET LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;%JAVA_HOME%\jre\lib\classes.zip

rem 		+-------------------------------------------------------------+
rem 		! Add Java's tools.jar to the classpath for running Ant       !
rem 		+-------------------------------------------------------------+

set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\ant.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\optional.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\NetComponents.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\parser.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\jaxp.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\xalan.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\xerces.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\bsf.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\js.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\fop.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib\w3c.jar
set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;..\lib

set LOCAL_CLASSPATH=%LOCAL_CLASSPATH%;%CLASSPATH%


if "%JAVACMD%" == "" set JAVACMD=JAVA

echo.
echo Building with classpath: %LOCAL_CLASSPATH%
echo.
echo ANT_HOME is: %ANT_HOME%
echo.
echo Starting Ant...
echo.


%JAVA_HOME%\bin\%JAVACMD% -Xmx512M -classpath %LOCAL_CLASSPATH% -Dant.home=%ANT_HOME% -Dant.opts=%ANT_OPTS% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9


goto END



rem +-------------------------------------------------------------------------+
rem ! Error exit points                                                       !
rem +-------------------------------------------------------------------------+

:ERROR_EXITS


:ERROR_JAVA_HOME
rem	+--------------------------------------------------------------------+
rem 	! JAVA_HOME environment variable not found                           !
rem 	+--------------------------------------------------------------------+

echo ******************************************************************
echo   ERROR: JAVA_HOME environment variable not found.
echo. 
echo   Please set JAVA_HOME to the installation directory for the Java JDK. 
echo ******************************************************************

goto END



rem +-------------------------------------------------------------------------+
rem ! Batch file exit: Cleanup and end                                        !
rem +-------------------------------------------------------------------------+

:END
set LOCAL_CLASSPATH=
