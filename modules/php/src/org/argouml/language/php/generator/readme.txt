Please send comments and suggestions to: mh@rent-a-tutor.com

Install:

Either 

rename GeneratorPHP with GeneratorJava to replace the existing Java Generator

or 

put it into the same directory as GeneratorJava and change all references to GeneratorJava to replace all calls to the existing Java Generator

The original location for GeneratorJava is:

argo_root\src_new\org\argouml\language\java\generator\

There is also a Section class which is creating and maintaining preserved sections "section.java". Place this file in the same directory as the generator.

It should be integrated into the existing system so that the user will be able to choose between Java and PHP but currently I don't have enough time to dig very deep into the ArgoUML design to do it myself.


There are some TaggedValues involved in the Generator Process:

On the class:

Name: constructor
Value: true

will generate a default constructor for the class


=========
On Attribues:

Name: get
Value: true

will generate a get-Method for that attribute

Name: set
Value: true

will generate a set-Method for that attribute

=========

Attribute Variables:

Attribute variables that do not visibility "public" will be cloaked in the system by changing the attribute name to <className>_<attributeName>. This is just an attempt to prevent using private or protected attributes from outside the class.

For all attributes that should not be public, you have to generate get- and set-Operations and you have to use these Operations to access the attributes, even within the class. This is necessary because PHP does not (yet) support private and protected attributes.

=========

Variable Passing (Operator Parameters):

If a variable for an operation is marked as "out" or "inout" the variable will be passed by reference, otherwise by value.

========

Default Values:

Default values can be stored in the model on Operator Parameters. They will be generated with the code.

==========

Preserved Sections:

with each code generation, special comments will be generated like this:

  function Testclass() {
  // section -64--88-0-40-76f2e8:ec37965ae0:-7fff begin
  // section -64--88-0-40-76f2e8:ec37965ae0:-7fff end
  }

All code you put within the "begin" and "end" lines will be preserved when you generate the code again. 
Please do not change anything within these lines because the sections are recognized by this comment syntax.

This also works if you change Method Names after the generation. 

  function /* public */ /* void*/ newOperation(/* */ &$test = "fddsaffa")
  {
  // section 603522:ec4c7ff768:-7ffc begin
  // section 603522:ec4c7ff768:-7ffc end


If you delete an Operation in the model. The code lost code will be added as comment to the end of the file.

============

To do:

- It would be nice to have a dialog tab for the properties that are now stored in the TaggedValues.
- The generator should be integrated in the main ArgoUML system so that the user can choose the language.



