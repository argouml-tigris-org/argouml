@rem Copyright (c) 1996-99 The Regents of the University of California. All
@rem Rights Reserved. Permission to use, copy, modify, and distribute this
@rem software and its documentation without fee, and without a written
@rem agreement is hereby granted, provided that the above copyright notice
@rem and this paragraph appear in all copies.  This software program and
@rem documentation are copyrighted by The Regents of the University of
@rem California. The software program and documentation are supplied "AS
@rem IS", without any accompanying services from The Regents. The Regents
@rem does not warrant that the operation of the program will be
@rem uninterrupted or error-free. The end-user understands that the program
@rem was developed for research purposes and is advised not to rely
@rem exclusively on the program for any reason.  IN NO EVENT SHALL THE
@rem UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
@rem SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
@rem ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
@rem THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
@rem SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
@rem WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
@rem MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
@rem PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
@rem CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
@rem UPDATES, ENHANCEMENTS, OR MODIFICATIONS.


@rem EDIT THE FOLLOWING LINES IN makeall.bat TO MATCH YOUR CONFIGURATION

set JAVA_LIB=c:\vcafe\java\lib\classes.zip
set SWING_LIB=c:\swing-1.1.1-beta2\swing.jar
set XML_LIB=d:\jr\xml4j.jar
set ARGO_HOME=d:\jr\argouml070
set CLASSPATH=%JAVA_LIB%;%XML_LIB%;%ARGO_HOME%;%SWING_LIB%

@rem if using JDK 1.2 with jikes
@rem set JAVA_LIB=c:\jdk1.2\jre\lib\rt.jar

@rem EDIT THE FOLLOWING LINES IN makeall.bat TO MATCH YOUR COMPILER

@rem set COMPILER=c:\vcafe\java\bin\javac -classpath %CLASSPATH%
@rem set COMPILER=c:\jikes\jikes
set COMPILER=c:\vcafe\bin\sj -classpath %CLASSPATH%

cd %ARGO_HOME%\uci\util
%COMPILER% *.java

cd %ARGO_HOME%\Acme
%COMPILER% *.java

cd %ARGO_HOME%\Acme\JPM\Encoders
%COMPILER% *.java

cd %ARGO_HOME%\uci\ui
%COMPILER% *.java

cd %ARGO_HOME%\uci\graph
%COMPILER% *.java

cd %ARGO_HOME%\uci\gef
%COMPILER% *.java

cd %ARGO_HOME%\uci\gef\event
%COMPILER% *.java

cd %ARGO_HOME%\uci\gef\demo
%COMPILER% *.java

cd %ARGO_HOME%\uci\argo\checklist
%COMPILER% *.java

cd %ARGO_HOME%\uci\argo\kernel
%COMPILER% *.java

cd %ARGO_HOME%\uci\beans\editors
%COMPILER% *.java


cd %ARGO_HOME%\uci\xml
%COMPILER% *.java

cd %ARGO_HOME%\uci\xml\pgml
%COMPILER% *.java




cd %ARGO_HOME%\uci\uml\foundation\core
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\foundation\data_types
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\foundation\extension_mechanisms
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\model_management
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\behavioral_elements\collaborations
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\behavioral_elements\common_behavior
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\behavioral_elements\state_machines
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\behavioral_elements\use_cases
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\checklist
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\critics
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\critics\java
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\critics\patterns
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\critics\presentation
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\generate
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ocl
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\test\omg
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\util
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ui
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ui\nav
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ui\props
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ui\style
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ui\table
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\ui\todo
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml\visual
%COMPILER% *.java

cd %ARGO_HOME%\uci\uml
%COMPILER% *.java

cd %ARGO_HOME%\uci\xml\argo
%COMPILER% *.java

cd %ARGO_HOME%\uci\xml\xmi
%COMPILER% *.java


cd %ARGO_HOME%






