@echo on
echo
echo "Argo Build System (borrowed from FOP)"
echo "-------------------------------------"
echo

SET JAVA_HOME=c:\jdk1.2.2
SET PATH=%JAVA_HOME%/bin
SET ANT_HOME=c:\ant1.1\jakarta-ant\lib
SET NSUML_HOME=c:\argo08
SET XML_HOME=%NSUML_HOME%
SET OCL_HOME=%NSUML_HOME%
SET GEF_HOME=c:\gef\gef\src
SET LPATH=%ANT_HOME%\ant.jar;%ANT_HOME%\jaxp.jar;%ANT_HOME%\parser.jar;%JAVA_HOME%\lib\tools.jar;%GEF_HOME%\gef.jar;%NSUML_HOME%\nsuml.jar;%NSUML_HOME%\xml4j.jar;%OCL_HOME%\ocl-argo.jar
echo Building with classpath $CLASSPATH:$LOCALCLASSPATH
echo
echo Starting Ant...
echo on
java -Dant.home=%ANT_HOME% -classpath %LPATH% org.apache.tools.ant.Main %1 %2
