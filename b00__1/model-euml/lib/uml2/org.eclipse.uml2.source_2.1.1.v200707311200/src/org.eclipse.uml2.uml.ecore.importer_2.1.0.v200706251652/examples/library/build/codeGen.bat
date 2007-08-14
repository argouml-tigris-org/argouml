rem You need to set the ECLIPSE_DIR variable below.  It should point to your eclipse directory.
set ECLIPSE_DIR=z

rem Model project
set MAIN_DIR=..

rem The directory indicated by the -modelProject argument should be neither a parent nor a child directory of the workspace.
set WORKSPACE=%MAIN_DIR%\..\codegenWorkspace

rem UML2GenModel application
java -classpath %ECLIPSE_DIR%\startup.jar org.eclipse.core.launcher.Main -clean -data %WORKSPACE% -application org.eclipse.uml2.uml.ecore.importer.UML2GenModel %MAIN_DIR%\model\library.uml %MAIN_DIR%\emf\library.genmodel -modelProject %MAIN_DIR% src -modelPluginID library.model -jdkLevel "5.0" -noQualify -package http:///library.ecore org.examples Library

rem Generator application
java -classpath %ECLIPSE_DIR%\startup.jar org.eclipse.core.launcher.Main -clean -data %WORKSPACE% -application org.eclipse.emf.codegen.ecore.Generator -model %MAIN_DIR%\emf\library.genmodel
