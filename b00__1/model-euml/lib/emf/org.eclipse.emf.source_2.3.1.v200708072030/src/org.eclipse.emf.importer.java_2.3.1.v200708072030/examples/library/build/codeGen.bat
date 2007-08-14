rem You need to set the ECLIPSE_DIR variable below.  It should point to your eclipse directory.
set ECLIPSE_DIR=z

rem The GenModel IPath (it has to be a workspace absolute path, in which the project is a valid Java Project)
set GENMODEL_IPATH=/myjavaproject/model/library.genmodel

rem The location of the model project
set MODEL_PROJECT=c:\lib

set GENMODEL_LOCATION=%MODEL_PROJECT%\model\library.genmodel

rem The workspace directory
set WORKSPACE=%MAIN_DIR%\..\codegenWorkspace

rem Java2GenModel application
java -classpath %ECLIPSE_DIR%\startup.jar org.eclipse.core.launcher.Main -clean -data %WORKSPACE% -application org.eclipse.emf.importer.ecore.Java2GenModel %GENMODEL_IPATH% -modelProject %MODEL_PROJECT% src -modelPluginID library.model -copyright "This is my code." -jdkLevel "5.0" 

rem Generator application
java -classpath %ECLIPSE_DIR%\startup.jar org.eclipse.core.launcher.Main -clean -data %WORKSPACE% -application org.eclipse.emf.codegen.ecore.Generator -model %GENMODEL_LOCATION%