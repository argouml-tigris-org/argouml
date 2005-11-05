C++ generator and reverse engineering module
Reverse engineering is still a work in progress.

Install:

 run ./build.sh install to generate the argo_cpp.jar file and copy it
 in the argouml/build/ext directory.

It should be integrated into the existing system so that the user will be able 
to choose between Java, C++ and others.

Operations are generated as "const" if they are marked as "query".
Operations which are NOT marked as "leaf", "root", "static", "abstract" are 
generated as virtual function.

There are some TaggedValues involved in the Generator Process:

On the class:

Name: constructor
Value: true
will generate a default constructor for the class

Name: header_incl
Value: filename
will include given file into header (.h)
TODO: what if the user wants more than one?! This problem is applicable 
to many of the bellow values.

Name: source_incl
Value: filename
will include given file into source (.cpp)

Name: typedef_public
Value: <source type> <type_name>
will create typedef line in the public area of the class with: 
typedef <source type> <type name>;

Name: typedef_protected
Value: <source type> <type_name>
will create typedef line in the protected area of the class with: 
typedef <source type> <type name>;

Name: typedef_private
Value: <source type> <type_name>
will create typedef line in the private area of the class with: 
typedef <source type> <type name>;

Name: typedef_global_header
Value: <source type> <type_name>
will create typedef line in the global area of the header with: 
typedef <source type> <type name>;

Name: typedef_global_source
Value: <source type> <type_name>
will create typedef line in the global area of the source file with: 
typedef <source type> <type name>;

Name: TemplatePath
Value: Directory
will search in the specified directory for the template files "header_template" 
and "cpp_template" which are placed in top of the corresponding file
( following tags in template file are replaced by model values:|FILENAME|, 
|DATE|, |YEAR|, |AUTHOR|, |EMAIL| )
( if no such tag is specified, the templates are searched in the subdirectory of 
the root directory for the code generation )

Name: email
Value: name@domain.country
will replace the tag |EMAIL| of the template file.

Name: author
Value: name
will replace the tag |AUTHOR| of the template file.

=========
On Attribues:

Name: pointer
Value: (true)
will generate the attribute as pointer

Name: reference
Value: (true)
will generate the attribute as reference

Name: usage
Value: source
will lead for class types to a predeclaration in the header, and the include of
the remote class header in the header of the generated class

Name: MultiplicityType
Value: list|slist|vector|map|stack|stringmap
will define a multiplicity as the corresponding STL container, if the range is 
variable ( fixed size ranges are transfered to: type name[size]).

Name: set
Value: private|protected|public
will create a simple function to set the attribute by a function
(call by reference is used for class-types, else call by value);
place the function in the given visibility area

Name: get
Value: private|protected|public
will create a simple const read/query function to get the value of the
attribute (return const reference to class-types); place the function in the 
given visibility area

=========
On Parameters:

Name: pointer
Value: (true)
will generate the attribute as pointer

Name: reference
Value: (true)
will generate the attribute as reference

=========

Variable Passing (Operator Parameters):

If a variable for an operation is marked as "out" or "inout" the variable will 
be passed by reference (default)
or pointer (needs tag <pointer> - see above), otherwise by value.


========

Default Values:

Default values can be stored in the model on Operator Parameters. They will be 
generated with the code.

==========

Preserved Sections:

with each code generation, special comments will be generated like this:

  Testclass::Testclass()
  // section -64--88-0-40-76f2e8:ec37965ae0:-7fff begin
	{
	}
  // section -64--88-0-40-76f2e8:ec37965ae0:-7fff end

All code you put within the "begin" and "end" lines will be preserved when you 
generate the code again. 
Please do not change anything within these lines because the sections are 
recognized by this comment syntax.
As the curley braces are placed within the preserved area, attribute 
initialisers are preserved on constructors.

This also works if you change Method Names after the generation. 

  function /* public */ /* void*/ newOperation(/* */ &$test = "fddsaffa")
  // section 603522:ec4c7ff768:-7ffc begin
	{
	}
  // section 603522:ec4c7ff768:-7ffc end


If you delete an Operation in the model. The code lost code will be added as 
comment to the end of the file.

============

- TODO: It would be nice to have a dialog tab for the properties that are now 
  stored in the TaggedValues.
- TODO: It would be nice to raise a dialog if no template files are found
- TODO: It woul be important to generate defined types (which could be realised 
  as enum)
