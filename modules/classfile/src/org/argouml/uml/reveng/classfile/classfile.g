header {
package org.argouml.uml.reveng.classfile;

import antlr.*;
import antlr.collections.*;
import java.util.*;
}   
/*
 * Java Classfile parser.
 *
 * Contributing authors:
 *     Andreas Rueckert <a_rueckert@gmx.net>
 */

/********************************
 * A parser for a Java classfile.
 ********************************/
class ClassfileParser extends Parser;

options { 
	exportVocab=Classfile;
	defaultErrorHandler = false;       // Don't generate parser error handlers
        buildAST = true;     
}

tokens {
	ACCESS_MODIFIERS;
	ATTRIBUTE_CONSTANT;
	CLASS_DEF;
	CONSTANT_CLASSINFO;
	CONSTANT_DOUBLEINFO;
	CONSTANT_FIELDINFO;
	CONSTANT_FLOATINFO;
	CONSTANT_INTEGERINFO;
	CONSTANT_INTERFACE_METHODINFO;
	CONSTANT_LONGINFO;
	CONSTANT_METHODINFO;
	CONSTANT_NAME_TYPE_INFO;
	CONSTANT_STRINGINFO;
	CONSTANT_UTF8STRING;
	CTOR_DEF;
	EXTENDS_CLAUSE;
	IDENT;
	IMPLEMENTS_CLAUSE;
	INTERFACE_DEF;
	MAGIC;
	METHOD_DEF;
	PARAMETERS;
	PARAMETER_DEF; 
	SOURCEFILE;
	THROWS;
	TYPE;
	UNKNOWN_ATTRIBUTE;
	VARIABLE_DEF;
	VERSION; 
}

{
	// Constants as defined in the JVM classfile specs.
	public static final byte CONSTANT_Class 		=  7; 
 	public static final byte CONSTANT_Fieldref 		=  9; 
 	public static final byte CONSTANT_Methodref 		= 10;
 	public static final byte CONSTANT_InterfaceMethodref 	= 11;
 	public static final byte CONSTANT_String 		=  8;
 	public static final byte CONSTANT_Integer 		=  3; 
 	public static final byte CONSTANT_Float 		=  4; 
 	public static final byte CONSTANT_Long 	 		=  5; 
 	public static final byte CONSTANT_Double 		=  6;
 	public static final byte CONSTANT_NameAndType 		= 12;
 	public static final byte CONSTANT_Utf8 			=  1;

	// Access flags as defined in the JVM specs.
 	public static final short ACC_PUBLIC    = 0x0001;
        public static final short ACC_FINAL     = 0x0010;
        public static final short ACC_SUPER     = 0x0020;
        public static final short ACC_INTERFACE = 0x0200;
        public static final short ACC_ABSTRACT  = 0x0400;


	// The name of the current class (to be used for constructors)
	private String _className = null;

	// A array buffer for the constant pool.
	private AST [] _constant = null;

	/**
	 * Set the name of the currently parsed class.
	 *
	 * @param name The name of the class.
	 */
	private void setClassName(String name) {

	    // Remove the package info.
    	    int lastDot = name.lastIndexOf('.');
            if(lastDot == -1) {
		_className = name;
            }  else {
                _className = name.substring(lastDot+1);
            }    
	}

	/**
	 * Get the name of the currently parsed class.
	 *
	 * @return The name of the class.
	 */
	private String getClassName() {
	    return _className;
	}

	/**
	 * Init the buffer for a given number of AST nodes.
	 *
	 * @param size The number of AST nodes.
	 */
	private void initPoolBuffer(int size) {
	    // This is a casted short, so we have to remove the sign extension.
	    _constant = new AST[size & 0xffff];
	}	

	/**
	 * Add a AST holding a constant to the buffer.
	 *
	 * @param index The index of the buffer, where the node is copied to.
	 * @param node The AST node with the constant info.
	 */
	private void copyConstant(int index, AST node) {
	    _constant[index] = node;
	}

	/**
	 * Get a constant from the buffer.
	 *
	 * @param index The index of the node in the buffer.
	 *
	 * @return The AST at the given position.
	 */
	private AST getConstant(short index) {
	    return _constant[(int)index &0xffff];
	}

	/**
	 * Convert a classfile field descriptor.
	 *
	 * @param desc The descritor as a string.
	 *
	 * @return The descriptor as it would appear in a Java sourcefile.
	 */
	String convertDescriptor(String desc) {
	    int arrayDim = 0;
	    StringBuffer result = new StringBuffer();

	    while(desc.charAt(0) == '[') {
	        arrayDim++;
		desc = desc.substring(1);
	    }

	    switch(desc.charAt(0)) {
	    	case 'B': result.append("byte"); break;
		case 'C': result.append("char"); break;
		case 'D': result.append("double"); break;
		case 'F': result.append("float"); break;
		case 'I': result.append("int"); break;
		case 'J': result.append("long"); break;
		case 'S': result.append("short"); break;
		case 'Z': result.append("boolean"); break;
		case 'L': result.append(desc.substring( 1, desc.indexOf(';')));
	    }

	    for(int d = 0; d < arrayDim; d++) {
	        result.append("[]");
	    }

	    return result.toString();
	}

	/**
	 * Convert the descriptor of a method.
	 *
	 * @param The method descriptor as a String.
	 *
	 * @return The method descriptor as a array of Strings, that holds Java types.
	 */
	String [] convertMethodDescriptor(String desc) {
	    Vector resultBuffer = new Vector();  // A buffer for the result.
	    int arrayDim = 0;
	    String typeIdent = null;

	    if(desc.startsWith("(")) {  // parse parameters
		int paramLen = desc.indexOf(")") - 1;
	        String paramDesc = desc.substring( 1, 1 + paramLen);

		while(paramDesc.length() > 0) {

		    while(paramDesc.charAt(0) == '[') {
			arrayDim++;
			paramDesc = paramDesc.substring(1);
		    }
			
		    switch(paramDesc.charAt(0)) {
	    	        case 'B': typeIdent = "byte"; paramDesc = paramDesc.substring(1); break;
		        case 'C': typeIdent = "char"; paramDesc = paramDesc.substring(1); break;
		        case 'D': typeIdent = "double"; paramDesc = paramDesc.substring(1); break;
		        case 'F': typeIdent = "float"; paramDesc = paramDesc.substring(1); break;
		        case 'I': typeIdent = "int"; paramDesc = paramDesc.substring(1); break;
		        case 'J': typeIdent = "long"; paramDesc = paramDesc.substring(1); break;
		        case 'S': typeIdent = "short"; paramDesc = paramDesc.substring(1); break;
		        case 'Z': typeIdent = "boolean"; paramDesc = paramDesc.substring(1); break;
		        case 'L': int len = paramDesc.indexOf(';') - 1;
				  typeIdent = paramDesc.substring( 1, 1 + len).replace('/', '.');
				  paramDesc = paramDesc.substring(len + 2);
		    }		

		    for(int i=0; i < arrayDim; i++) {
			typeIdent += "[]";
		    }
		    arrayDim = 0;

		    resultBuffer.add(typeIdent);
		}
		desc = desc.substring(paramLen + 2);
	    }

	    // Now convert the return type descriptor.

	    while(desc.charAt(0) == '[') {
	        arrayDim++;
		desc = desc.substring(1);
	    }

	    switch(desc.charAt(0)) {
	    	case 'B': typeIdent = "byte"; break;
		case 'C': typeIdent = "char"; break;
		case 'D': typeIdent = "double"; break;
		case 'F': typeIdent = "float"; break;
		case 'I': typeIdent = "int"; break;
		case 'J': typeIdent = "long"; break;
		case 'S': typeIdent = "short"; break;
		case 'Z': typeIdent = "boolean"; break;
		case 'V': typeIdent = "void"; break;
		case 'L': typeIdent = desc.substring( 1, desc.indexOf(';')).replace('/','.');
	    }

	    for(int i=0; i < arrayDim; i++) {
	        typeIdent += "[]";
	    }

	    resultBuffer.insertElementAt( typeIdent, 0);

	    String [] result = new String [ resultBuffer.size() ];
	    resultBuffer.copyInto(result);
	    return result;
	}
}

// The entire classfile
classfile
	: magic_number
	  version_number
	  constant_pool
	  type_definition
	  field_block
	  method_block
	  attribute_block
	  EOF
        ;

// The magic number 0xCAFEBABE, every classfile starts with
magic_number!
{ int magic=0; } 
	: magic=u4 {magic==0xcafebabe}? { #magic_number = #[MAGIC,Integer.toHexString(magic)]; }
	;

// The version number.
version_number!
{ short minor=0,major=0; String verStr=null; }
	: minor=u2 major=u2 { verStr = ""+major+"."+minor; #version_number = #[VERSION,verStr]; }
	;

// The constant pool.
constant_pool!
{ short poolSize=0; int index=1; }
	: poolSize=u2 { initPoolBuffer(poolSize); }  // Parse the size of the constant pool + 1
	  ( 
	    {index < ((int)poolSize & 0xffff)}? 
            cp:cp_info 
             { 
               copyConstant(index++, #cp); 

	       // 8 byte constants consume 2 constant pool entries (according to the JVM specs).
	       if( (#cp.getType() == CONSTANT_LONGINFO) || (#cp.getType() == CONSTANT_DOUBLEINFO)) {
		  index++;
	       }
             } 
          )* 
	  {index==((int)poolSize & 0xffff)}?  	// Parse <poolSize-1> cp_info structures.
	;

// Info on a entry in the constant pool
cp_info!
{ byte tag=0; }
	: tag=u1  // This tag does actually belong to the *info structures according to the 
                  // classfile specs. Put putting it into the *info rules might cause quite
	          // a bit of guessing and backtracking, which might cause performance issues.
                  // So I check it, before the actual *info structures are parsed.
	  (      
	    {tag == CONSTANT_Class}?                cl:constant_class_info               {#cp_info=#cl;}
	    | {tag == CONSTANT_Fieldref}?           cf:constant_fieldref_info            {#cp_info=#cf;}
	    | {tag == CONSTANT_Methodref}?          cm:constant_methodref_info           {#cp_info=#cm;}
 	    | {tag == CONSTANT_InterfaceMethodref}? ci:constant_interface_methodref_info {#cp_info=#ci;}
 	    | {tag == CONSTANT_String}?             cs:constant_string_info              {#cp_info=#cs;}          
 	    | {tag == CONSTANT_Integer}?            ct:constant_integer_info             {#cp_info=#ct;}
 	    | {tag == CONSTANT_Float}?              ca:constant_float_info               {#cp_info=#ca;}
 	    | {tag == CONSTANT_Long}?               co:constant_long_info                {#cp_info=#co;}
 	    | {tag == CONSTANT_Double}?             cd:constant_double_info              {#cp_info=#cd;}
 	    | {tag == CONSTANT_NameAndType}?        cn:constant_name_and_type_info       {#cp_info=#cn;}
 	    | {tag == CONSTANT_Utf8}?               cu:constant_utf8_info                {#cp_info=#cu;}
	  )
	;

// Info on a class in the constant pool.
constant_class_info!
{ short name_index=0; }
	: name_index=u2  // Missing tag (according to the classfile specs)! See the cp_info rule!
	  { #constant_class_info = new ShortAST( CONSTANT_CLASSINFO, name_index); }
	;

// Info on a field in the constant pool.
constant_fieldref_info!
{
  short class_index=0;
  short name_and_type_index=0;
}
	: class_index=u2  // Missing tag (according to the classfile specs)! See the cp_info rule!
	  name_and_type_index=u2  
	   { 
	     #constant_fieldref_info = new ShortAST( CONSTANT_FIELDINFO, class_index);
	     #constant_fieldref_info.addChild( new ShortAST( CONSTANT_NAME_TYPE_INFO, name_and_type_index));
           }
	;

// Info on a class method in the constant pool.
constant_methodref_info!
{
  short class_index=0;
  short name_and_type_index=0;
}
	: class_index=u2  // Missing tag (according to the classfile specs)! See the cp_info rule!
	  name_and_type_index=u2 
	   { 
	     #constant_methodref_info = new ShortAST(CONSTANT_METHODINFO, class_index);
	     #constant_methodref_info.addChild( new ShortAST( CONSTANT_NAME_TYPE_INFO, name_and_type_index));
	   }
	;

// Info on a interface method in the constant pool.
constant_interface_methodref_info!
{
  short class_index=0;
  short name_and_type_index=0;
}
	: class_index=u2  // Missing tag (according to the classfile specs)! See the cp_info rule!
	  name_and_type_index=u2 
	   { 
	     #constant_interface_methodref_info = new ShortAST(CONSTANT_INTERFACE_METHODINFO,class_index);
	     #constant_interface_methodref_info.addChild( new ShortAST( CONSTANT_NAME_TYPE_INFO,name_and_type_index));
	   }
	;

// Info on a string in the constant pool.
constant_string_info!
{ short string_index=0; }
	: string_index=u2  // Missing tag (according to the classfile specs)! See the cp_info rule!
	   { #constant_string_info = new ShortAST( CONSTANT_STRINGINFO, string_index); }
	;

// Info on a string in the constant pool.
constant_integer_info!
{ int val=0; }
	: val=u4  // Missing tag (according to the classfile specs)! See the cp_info rule!
	   { #constant_integer_info = new ObjectAST(CONSTANT_INTEGERINFO, new Integer(val)); }
	;

// Info on a float in the constant pool.
constant_float_info!
{ int bytes=0; }
	: bytes=u4  // Missing tag (according to the classfile specs)! See the cp_info rule!
	   { #constant_float_info = new ObjectAST(CONSTANT_FLOATINFO, new Double(Float.intBitsToFloat(bytes))); }
	;

// Info on a long in the constant pool.
constant_long_info!
{ int high_bytes=0, low_bytes=0; long val = 0L; }
	: high_bytes=u4 low_bytes=u4  // Missing tag (according to the classfile specs)! See the cp_info rule!
	   { #constant_long_info = new ObjectAST(CONSTANT_LONGINFO, new Long((long)high_bytes | ((long)low_bytes & 0xFFFFL))); }
	;

// Info on a double in the constant pool.
constant_double_info!
{ int high_bytes=0, low_bytes=0; }
	: high_bytes=u4 low_bytes=u4  // Missing tag (according to the classfile specs)! See the cp_info rule!
	   { #constant_double_info = new ObjectAST(CONSTANT_DOUBLEINFO, new Double(Double.longBitsToDouble( (long)high_bytes | ((long)low_bytes & 0xFFFFL))));
}
	;

// Info on name and type.
constant_name_and_type_info!
{ short name_index=0, descriptor_index=0; }
	: name_index=u2
	  descriptor_index=u2  // Missing tag (according to the classfile specs)! See the cp_info rule!
	   {
	     #constant_name_and_type_info = new ShortAST(CONSTANT_NAME_TYPE_INFO,name_index);
	     #constant_name_and_type_info.addChild(new ShortAST(CONSTANT_STRINGINFO,descriptor_index));
	   }
	;

// A UTF8 encoded string in the constant pool.
constant_utf8_info!
{
  short length=0;
  byte [] bytes;
  byte bytebuf=0;
  int bytepos=0;
}
	: length=u2 { bytes = new byte[length]; }  // Missing tag (according to the classfile specs)! See the cp_info rule!
	  ( {length > 0}? bytebuf=u1 { bytes[bytepos++] = bytebuf; length--; } )* {length==0}? 
	  { 
            String name = new String(bytes);
	    name= name.replace('/','.'); 
	    if(name.startsWith("[") && name.endsWith("]")) {
		name = name.substring(1,name.length()-1) + "[]";
	    }
	    #constant_utf8_info = #[CONSTANT_UTF8STRING,name];
          }
	;

// The head of a class of interface definition.
type_definition!
	: m:access_modifiers
	  c:class_info
	  s:superclass_info
	  i:interface_block
	   { 
	     if( (((ShortAST)#m).getShortValue() & ACC_INTERFACE) > 0) {
	         #type_definition = #( [INTERFACE_DEF], m, c, ([EXTENDS_CLAUSE], i));
	     } else {
		 #type_definition = #( [CLASS_DEF], m, c, ([EXTENDS_CLAUSE], s), ([IMPLEMENTS_CLAUSE], i));
	     }
	   }
	;

// Access modifiers for the class
access_modifiers!
{ short modifiers=0; }
	: modifiers=u2 { #access_modifiers = new ShortAST( ACCESS_MODIFIERS, modifiers); }
	;

// Info on the main class
class_info!
{ short class_info_index = 0; }  	// A index of a entry in the constant pool.
	: class_info_index=u2  
	  { 
            String class_name = getConstant(((ShortAST)getConstant(class_info_index)).getShortValue()).getText();
	    setClassName(class_name);
	    #class_info = #[IDENT, class_name];
	  }
	;

// Info on the super class
superclass_info!
{ short class_info_index = 0; }   // A index of a entry in the constant pool.
	: class_info_index=u2
	  { 
            String class_name = getConstant(((ShortAST)getConstant(class_info_index)).getShortValue()).getText();
	    #superclass_info = #[IDENT, class_name];
	  }
	;

// Info on the implemented interfaces
interface_block
{ short interfaces_count=0; }
	: interfaces_count=u2    // Get the number of implemented interfaces.
 	  ( {interfaces_count > 0}? interface_info {interfaces_count--;} )* {interfaces_count==0}?  // Parse <interfaces_count> interface_info structures.
	;

// Info on a interface.
interface_info!
{ short interface_index=0; }
	: interface_index=u2  	// A valid index into the constant_pool table, that references a CONSTANT_Class_info structure.
	   { 
             String interface_name = getConstant(((ShortAST)getConstant(interface_index)).getShortValue()).getText();
	     #interface_info = #[IDENT, interface_name];
	   }
	;

// Info on all the fields of this class.
field_block
{ short fields_count=0; }
	: fields_count=u2  // Get the number of fields.
	  ( {fields_count > 0}? field_info {fields_count--;})* {fields_count==0}?  // Parse <fields_count> field_info structures.
	;

// Info on a field.
field_info
{
  short access_flags=0;
  short name_index=0;
  short descriptor_index=0;
  short attributes_count;
}
	: access_flags=u2
	  name_index=u2
	  descriptor_index=u2
          attributes_count=u2
	  ( {attributes_count > 0}? attribute_info {attributes_count--;})* {attributes_count==0}?
	   {
	     AST access = new ShortAST(ACCESS_MODIFIERS,access_flags);
	     String typeIdent = convertDescriptor(getConstant(descriptor_index).getText());
	     String name = getConstant(name_index).getText();
	     #field_info = #( #field_info, [VARIABLE_DEF], access, [TYPE,typeIdent], [IDENT,name]);
	   }
	;

// Info on all the methods of this class.
method_block
{ int methods_count=0; }
	: methods_count=u2  // Get the number of methods.
	  ( {methods_count > 0}? method_info {methods_count--;})* {methods_count==0}?  // Parse <methods_count> method_info structures.
	;

// Info on a method.
method_info!
{
  short access_flags=0;
  short name_index=0;
  short descriptor_index=0;
  short attributes_count=0;
  AST exceptions = #[THROWS];  // Create a empty exception clause.
}
	: access_flags=u2
          name_index=u2  
	  descriptor_index=u2
	  attributes_count=u2	
	  ( 
            {attributes_count > 0}? 
            attr:attribute_info 
	    (
	     // If this is a exception table, store it for the method AST.
	      {#attr != null && THROWS == #attr.getType()}? {exceptions = #attr;}

	      |  // Could also be a code attribute.
	    )
	     { attributes_count--; }
          )* 
	  {attributes_count==0}?
	    {
	      String [] method_descriptor = convertMethodDescriptor(getConstant(descriptor_index).getText());
	      AST parameters = new CommonAST();
	      parameters.setType(PARAMETERS);
	      for(int i=1; i < method_descriptor.length; i++) {
		 ShortAST access = new ShortAST(ACCESS_MODIFIERS, (short)0);
		 String paramType = method_descriptor[i];
		 String paramIdent = "param" + i;
		 AST param = #([PARAMETER_DEF], access, [TYPE,paramType], [IDENT, paramIdent]);
		 parameters.addChild(param);
	      }

	      AST access = new ShortAST(ACCESS_MODIFIERS,access_flags);
	      String ident = getConstant(name_index).getText();
	      if( "<init>".equals(ident)) {  // is this a constructor?
		  ident = getClassName();  // Use the class name as the constructor's method name.
		  #method_info = #( [CTOR_DEF], access,  [IDENT,ident], parameters, exceptions);
	      } else {
	          String retType = method_descriptor[0];
	          #method_info = #( [METHOD_DEF], access, [TYPE,retType], [IDENT,ident], parameters, exceptions);
	      }
	    }
	;

// Info on all the attributes of a class.
attribute_block
{ int attributes_count=0; }
	: attributes_count=u2  // Get the number of attributes.
	  ( {attributes_count > 0}? attribute_info {attributes_count--;})* {attributes_count==0}?  // Parse <attributes_count> attribute_info structures.
	;

// Info on a attribute
attribute_info!
{
  short attribute_name_index=0;
  int attribute_length=0;
  String attribute_name=null;
  byte [] info;
  int bytepos=0;
  byte bytebuf=0;
}
	: attribute_name_index=u2 {getConstant(attribute_name_index).getType()==CONSTANT_UTF8STRING}? {attribute_name = getConstant(attribute_name_index).getText(); }
	  attribute_length=u4
	  (
	    {"Code".equals(attribute_name)}? cattr:code_attribute { #attribute_info = #cattr; }
	    |
 	    {"ConstantValue".equals(attribute_name)}? cvattr:constantValue_attribute { #attribute_info = #cvattr; }
	    |
	    {"Exceptions".equals(attribute_name)}? exattr:exceptions_attribute { #attribute_info = #exattr; }
	    |
	    {"LineNumberTable".equals(attribute_name)}? lnattr:lineNumberTable_attribute { #attribute_info = #lnattr; }
	    |
 	    {"LocalVariableTable".equals(attribute_name)}? lattr:localVariableTable_attribute { #attribute_info = #lattr; }
	    |
	    // The classfile specs define a attribute, that gives info on the filename of the sourcecode.
	    {attribute_length==2 && "SourceFile".equals(attribute_name)}? sattr:sourcefile_attribute { #attribute_info = #sattr; }
	    |
	    // A compiler specific attribute, that is not known in detail..
	    { info = new byte[attribute_length]; }
	    ( {bytepos < attribute_length}? bytebuf=u1 {info[bytepos++] = bytebuf;} )* {bytepos==attribute_length}?
	    { #attribute_info = #[UNKNOWN_ATTRIBUTE, attribute_name]; }
	  ) exception catch [SemanticException se] {}
	;

// A predefined attribute, that holds the filename of the sourcecode.
sourcefile_attribute!
{ short sourcefile_index = 0; }
	: sourcefile_index=u2 
	   { 
	     String sourcefile_name = getConstant(sourcefile_index).getText();
	     #sourcefile_attribute = #[SOURCEFILE, sourcefile_name];
	   }
	;

// A attribute holding a constant value
constantValue_attribute!
{ short constantvalue_index = 0; }
	: constantvalue_index=u2 { #constantValue_attribute = new ShortAST(ATTRIBUTE_CONSTANT, constantvalue_index); }
	;

// A predefined attribute, that holds the code of a method.
// The name index and length are missing here, cause they are already
// parsed in the attribute_info rule to decide how to proceed further.
code_attribute
{
  short max_stack = 0;
  short max_locals = 0;
  int code_length = 0;
  int codepos = 0;  // This should be long, but Java seems cause problems with array sizes > max_int.
  byte [] code = null;
  byte bytebuf = 0;
  short exception_table_length = 0;
  int exceptionpos=0;
  short attribute_count=0;
  int attributepos=0;
}
	: max_stack=u2
	  max_locals=u2
	  code_length=u4 { code = new byte[code_length]; }
	  ( {codepos < code_length}? bytebuf=u1 {code[codepos++] = bytebuf;} )* {codepos==code_length}?
	  exception_table_length=u2
	  ( 
	    {exceptionpos < ((int)exception_table_length & 0xffff)}? 
            exception_table_entry { exceptionpos++; } 
          )* 
          {exceptionpos==((int)exception_table_length & 0xffff)}?
	  attribute_count=u2
	  ( {attributepos < ((int)attribute_count & 0xffff)}? attribute_info { attributepos++; } )* {attributepos==((int)attribute_count & 0xffff)}?
	;

// A entry in the exception table.
exception_table_entry
{
  short start_pc = 0;
  short end_pc = 0;
  short handler_pc = 0;
  short catch_type = 0;
}
	: start_pc=u2
          end_pc=u2
          handler_pc=u2
          catch_type=u2
	;

// A attribute, holding the table of thrown exceptions.
exceptions_attribute
{
  short number_of_exceptions = 0;
  int indexpos=0;
}
	: number_of_exceptions=u2
	  ( {indexpos < ((int)number_of_exceptions & 0xffff)}? exception_index_entry { indexpos++; } )* 
	  {indexpos==((int)number_of_exceptions & 0xffff)}?
	  { #exceptions_attribute = #( [THROWS], #exceptions_attribute); }
	;

// A entry in the table of thrown exceptions
exception_index_entry!
{ short index=0;}
	: index=u2 {index != 0}?  // For some reason, the specs define only exceptions, if index != 0? (why store a exception with no name?) 
	   { 
	     // The index references a Class_info structure in the constant pool,
	     // that we can use to get the name of the exception (class).
             String exception_name = getConstant(((ShortAST)getConstant(index)).getShortValue()).getText();
	     #exception_index_entry = #[IDENT, exception_name];
	   }
	;

// The linenumber table.
lineNumberTable_attribute
{ 
  short line_number_table_length = 0; 
  int entrypos = 0;
}
	: line_number_table_length=u2
          ( 
	    {entrypos < ((int)line_number_table_length & 0xffff)}? 
            lineNumberTableEntry { entrypos++; } 
          )* 
          {entrypos==((int)line_number_table_length & 0xffff)}?
	;

// A entry in the linenumber table.
lineNumberTableEntry
{ short start_pc=0, line_number=0; }
	: start_pc=u2
	  line_number=u2
	;

// The table with the local variables.
localVariableTable_attribute
{ 
  short local_variable_table_length = 0; 
  int entrypos=0;
}
	: local_variable_table_length=u2
	  ( 
	    {entrypos < ((int)local_variable_table_length & 0xffff)}? 
            localVariableTableEntry {entrypos++;}
          )* 
          {entrypos==((int)local_variable_table_length & 0xffff)}?
	;

// A entry in the local variable table.
localVariableTableEntry
{ short start_pc = 0, length = 0, name_index = 0, descriptor_index = 0, index = 0; }
	: start_pc=u2
          length=u2
          name_index=u2
          descriptor_index=u2
          index=u2
	;


//////////////////////
// Some utility rules.
//////////////////////

// A 1 byte int
u1! returns [byte res=0]
	: val:BYTE { res = ((ByteToken)val).getValue(); }
	;

// A 2 byte int
u2! returns [short res=0]
	: ( high:BYTE low:BYTE ) 
	    { res = (short)(((ByteToken)high).getShortValue() << 8 | ((ByteToken)low).getShortValue()); }
	;

// A 4 byte int
u4! returns [int res=0]
	: ( high1:BYTE high2:BYTE low1:BYTE low2:BYTE )  // Bytes are in highbyte 1st order!
	  { 
	    res = ((ByteToken)high1).getIntValue() << 24
	          | ((ByteToken)high2).getIntValue() << 16 
	          | ((ByteToken)low1).getIntValue() << 8
	          | ((ByteToken)low2).getIntValue();
	  }
	;


