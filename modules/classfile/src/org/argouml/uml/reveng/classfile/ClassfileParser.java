// $ANTLR 2.7.1: "classfile.g" -> "ClassfileParser.java"$

package org.argouml.uml.reveng.classfile;

import antlr.*;
import antlr.collections.*;
import java.util.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/********************************
 * A parser for a Java classfile.
 ********************************/
public class ClassfileParser extends antlr.LLkParser
       implements ClassfileTokenTypes
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

protected ClassfileParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public ClassfileParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected ClassfileParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public ClassfileParser(TokenStream lexer) {
  this(lexer,1);
}

public ClassfileParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final void classfile() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST classfile_AST = null;
		
		magic_number();
		astFactory.addASTChild(currentAST, returnAST);
		version_number();
		astFactory.addASTChild(currentAST, returnAST);
		constant_pool();
		astFactory.addASTChild(currentAST, returnAST);
		type_definition();
		astFactory.addASTChild(currentAST, returnAST);
		field_block();
		astFactory.addASTChild(currentAST, returnAST);
		method_block();
		astFactory.addASTChild(currentAST, returnAST);
		attribute_block();
		astFactory.addASTChild(currentAST, returnAST);
		AST tmp1_AST = null;
		tmp1_AST = (AST)astFactory.create(LT(1));
		astFactory.addASTChild(currentAST, tmp1_AST);
		match(Token.EOF_TYPE);
		classfile_AST = (AST)currentAST.root;
		returnAST = classfile_AST;
	}
	
	public final void magic_number() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST magic_number_AST = null;
		int magic=0;
		
		magic=u4();
		if (!(magic==0xcafebabe))
		  throw new SemanticException("magic==0xcafebabe");
		magic_number_AST = (AST)currentAST.root;
		magic_number_AST = (AST)astFactory.create(MAGIC,Integer.toHexString(magic));
		currentAST.root = magic_number_AST;
		currentAST.child = magic_number_AST!=null &&magic_number_AST.getFirstChild()!=null ?
			magic_number_AST.getFirstChild() : magic_number_AST;
		currentAST.advanceChildToEnd();
		returnAST = magic_number_AST;
	}
	
	public final void version_number() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_number_AST = null;
		short minor=0,major=0; String verStr=null;
		
		minor=u2();
		major=u2();
		version_number_AST = (AST)currentAST.root;
		verStr = ""+major+"."+minor; version_number_AST = (AST)astFactory.create(VERSION,verStr);
		currentAST.root = version_number_AST;
		currentAST.child = version_number_AST!=null &&version_number_AST.getFirstChild()!=null ?
			version_number_AST.getFirstChild() : version_number_AST;
		currentAST.advanceChildToEnd();
		returnAST = version_number_AST;
	}
	
	public final void constant_pool() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_pool_AST = null;
		AST cp_AST = null;
		short poolSize=0; int index=1;
		
		poolSize=u2();
		initPoolBuffer(poolSize);
		{
		_loop6:
		do {
			if (((LA(1)==BYTE))&&(index < ((int)poolSize & 0xffff))) {
				cp_info();
				cp_AST = (AST)returnAST;
				
				copyConstant(index++, cp_AST); 
				
					       // 8 byte constants consume 2 constant pool entries (according to the JVM specs).
					       if( (cp_AST.getType() == CONSTANT_LONGINFO) || (cp_AST.getType() == CONSTANT_DOUBLEINFO)) {
						  index++;
					       }
				
			}
			else {
				break _loop6;
			}
			
		} while (true);
		}
		if (!(index==((int)poolSize & 0xffff)))
		  throw new SemanticException("index==((int)poolSize & 0xffff)");
		returnAST = constant_pool_AST;
	}
	
	public final void type_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_definition_AST = null;
		AST m_AST = null;
		AST c_AST = null;
		AST s_AST = null;
		AST i_AST = null;
		
		access_modifiers();
		m_AST = (AST)returnAST;
		class_info();
		c_AST = (AST)returnAST;
		superclass_info();
		s_AST = (AST)returnAST;
		interface_block();
		i_AST = (AST)returnAST;
		type_definition_AST = (AST)currentAST.root;
		
			     if( (((ShortAST)m_AST).getShortValue() & ACC_INTERFACE) > 0) {
			         type_definition_AST = (AST)astFactory.make( (new ASTArray(4)).add((AST)astFactory.create(INTERFACE_DEF)).add(m_AST).add(c_AST).add((AST)astFactory.make( (new ASTArray(2)).add((AST)astFactory.create(EXTENDS_CLAUSE)).add(i_AST))));
			     } else {
				 type_definition_AST = (AST)astFactory.make( (new ASTArray(5)).add((AST)astFactory.create(CLASS_DEF)).add(m_AST).add(c_AST).add((AST)astFactory.make( (new ASTArray(2)).add((AST)astFactory.create(EXTENDS_CLAUSE)).add(s_AST))).add((AST)astFactory.make( (new ASTArray(2)).add((AST)astFactory.create(IMPLEMENTS_CLAUSE)).add(i_AST))));
			     }
			
		currentAST.root = type_definition_AST;
		currentAST.child = type_definition_AST!=null &&type_definition_AST.getFirstChild()!=null ?
			type_definition_AST.getFirstChild() : type_definition_AST;
		currentAST.advanceChildToEnd();
		returnAST = type_definition_AST;
	}
	
	public final void field_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST field_block_AST = null;
		short fields_count=0;
		
		fields_count=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop32:
		do {
			if (((LA(1)==BYTE))&&(fields_count > 0)) {
				field_info();
				astFactory.addASTChild(currentAST, returnAST);
				fields_count--;
			}
			else {
				break _loop32;
			}
			
		} while (true);
		}
		if (!(fields_count==0))
		  throw new SemanticException("fields_count==0");
		field_block_AST = (AST)currentAST.root;
		returnAST = field_block_AST;
	}
	
	public final void method_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST method_block_AST = null;
		int methods_count=0;
		
		methods_count=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop38:
		do {
			if (((LA(1)==BYTE))&&(methods_count > 0)) {
				method_info();
				astFactory.addASTChild(currentAST, returnAST);
				methods_count--;
			}
			else {
				break _loop38;
			}
			
		} while (true);
		}
		if (!(methods_count==0))
		  throw new SemanticException("methods_count==0");
		method_block_AST = (AST)currentAST.root;
		returnAST = method_block_AST;
	}
	
	public final void attribute_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST attribute_block_AST = null;
		int attributes_count=0;
		
		attributes_count=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop45:
		do {
			if (((LA(1)==BYTE))&&(attributes_count > 0)) {
				attribute_info();
				astFactory.addASTChild(currentAST, returnAST);
				attributes_count--;
			}
			else {
				break _loop45;
			}
			
		} while (true);
		}
		if (!(attributes_count==0))
		  throw new SemanticException("attributes_count==0");
		attribute_block_AST = (AST)currentAST.root;
		returnAST = attribute_block_AST;
	}
	
	public final int  u4() throws RecognitionException, TokenStreamException {
		int res=0;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST u4_AST = null;
		Token  high1 = null;
		AST high1_AST = null;
		Token  high2 = null;
		AST high2_AST = null;
		Token  low1 = null;
		AST low1_AST = null;
		Token  low2 = null;
		AST low2_AST = null;
		
		{
		high1 = LT(1);
		high1_AST = (AST)astFactory.create(high1);
		match(BYTE);
		high2 = LT(1);
		high2_AST = (AST)astFactory.create(high2);
		match(BYTE);
		low1 = LT(1);
		low1_AST = (AST)astFactory.create(low1);
		match(BYTE);
		low2 = LT(1);
		low2_AST = (AST)astFactory.create(low2);
		match(BYTE);
		}
		
			    res = ((ByteToken)high1).getIntValue() << 24
			          | ((ByteToken)high2).getIntValue() << 16 
			          | ((ByteToken)low1).getIntValue() << 8
			          | ((ByteToken)low2).getIntValue();
			
		returnAST = u4_AST;
		return res;
	}
	
	public final short  u2() throws RecognitionException, TokenStreamException {
		short res=0;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST u2_AST = null;
		Token  high = null;
		AST high_AST = null;
		Token  low = null;
		AST low_AST = null;
		
		{
		high = LT(1);
		high_AST = (AST)astFactory.create(high);
		match(BYTE);
		low = LT(1);
		low_AST = (AST)astFactory.create(low);
		match(BYTE);
		}
		res = (short)(((ByteToken)high).getShortValue() << 8 | ((ByteToken)low).getShortValue());
		returnAST = u2_AST;
		return res;
	}
	
	public final void cp_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST cp_info_AST = null;
		AST cl_AST = null;
		AST cf_AST = null;
		AST cm_AST = null;
		AST ci_AST = null;
		AST cs_AST = null;
		AST ct_AST = null;
		AST ca_AST = null;
		AST co_AST = null;
		AST cd_AST = null;
		AST cn_AST = null;
		AST cu_AST = null;
		byte tag=0;
		
		tag=u1();
		{
		if (((LA(1)==BYTE))&&(tag == CONSTANT_Class)) {
			constant_class_info();
			cl_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cl_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Fieldref)) {
			constant_fieldref_info();
			cf_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cf_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Methodref)) {
			constant_methodref_info();
			cm_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cm_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_InterfaceMethodref)) {
			constant_interface_methodref_info();
			ci_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=ci_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_String)) {
			constant_string_info();
			cs_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cs_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Integer)) {
			constant_integer_info();
			ct_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=ct_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Float)) {
			constant_float_info();
			ca_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=ca_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Long)) {
			constant_long_info();
			co_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=co_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Double)) {
			constant_double_info();
			cd_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cd_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_NameAndType)) {
			constant_name_and_type_info();
			cn_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cn_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else if (((LA(1)==BYTE))&&(tag == CONSTANT_Utf8)) {
			constant_utf8_info();
			cu_AST = (AST)returnAST;
			cp_info_AST = (AST)currentAST.root;
			cp_info_AST=cu_AST;
			currentAST.root = cp_info_AST;
			currentAST.child = cp_info_AST!=null &&cp_info_AST.getFirstChild()!=null ?
				cp_info_AST.getFirstChild() : cp_info_AST;
			currentAST.advanceChildToEnd();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
		returnAST = cp_info_AST;
	}
	
	public final byte  u1() throws RecognitionException, TokenStreamException {
		byte res=0;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST u1_AST = null;
		Token  val = null;
		AST val_AST = null;
		
		val = LT(1);
		val_AST = (AST)astFactory.create(val);
		match(BYTE);
		res = ((ByteToken)val).getValue();
		returnAST = u1_AST;
		return res;
	}
	
	public final void constant_class_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_class_info_AST = null;
		short name_index=0;
		
		name_index=u2();
		constant_class_info_AST = (AST)currentAST.root;
		constant_class_info_AST = new ShortAST( CONSTANT_CLASSINFO, name_index);
		currentAST.root = constant_class_info_AST;
		currentAST.child = constant_class_info_AST!=null &&constant_class_info_AST.getFirstChild()!=null ?
			constant_class_info_AST.getFirstChild() : constant_class_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_class_info_AST;
	}
	
	public final void constant_fieldref_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_fieldref_info_AST = null;
		
		short class_index=0;
		short name_and_type_index=0;
		
		
		class_index=u2();
		name_and_type_index=u2();
		constant_fieldref_info_AST = (AST)currentAST.root;
		
			     constant_fieldref_info_AST = new ShortAST( CONSTANT_FIELDINFO, class_index);
			     constant_fieldref_info_AST.addChild( new ShortAST( CONSTANT_NAME_TYPE_INFO, name_and_type_index));
		
		currentAST.root = constant_fieldref_info_AST;
		currentAST.child = constant_fieldref_info_AST!=null &&constant_fieldref_info_AST.getFirstChild()!=null ?
			constant_fieldref_info_AST.getFirstChild() : constant_fieldref_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_fieldref_info_AST;
	}
	
	public final void constant_methodref_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_methodref_info_AST = null;
		
		short class_index=0;
		short name_and_type_index=0;
		
		
		class_index=u2();
		name_and_type_index=u2();
		constant_methodref_info_AST = (AST)currentAST.root;
		
			     constant_methodref_info_AST = new ShortAST(CONSTANT_METHODINFO, class_index);
			     constant_methodref_info_AST.addChild( new ShortAST( CONSTANT_NAME_TYPE_INFO, name_and_type_index));
			
		currentAST.root = constant_methodref_info_AST;
		currentAST.child = constant_methodref_info_AST!=null &&constant_methodref_info_AST.getFirstChild()!=null ?
			constant_methodref_info_AST.getFirstChild() : constant_methodref_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_methodref_info_AST;
	}
	
	public final void constant_interface_methodref_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_interface_methodref_info_AST = null;
		
		short class_index=0;
		short name_and_type_index=0;
		
		
		class_index=u2();
		name_and_type_index=u2();
		constant_interface_methodref_info_AST = (AST)currentAST.root;
		
			     constant_interface_methodref_info_AST = new ShortAST(CONSTANT_INTERFACE_METHODINFO,class_index);
			     constant_interface_methodref_info_AST.addChild( new ShortAST( CONSTANT_NAME_TYPE_INFO,name_and_type_index));
			
		currentAST.root = constant_interface_methodref_info_AST;
		currentAST.child = constant_interface_methodref_info_AST!=null &&constant_interface_methodref_info_AST.getFirstChild()!=null ?
			constant_interface_methodref_info_AST.getFirstChild() : constant_interface_methodref_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_interface_methodref_info_AST;
	}
	
	public final void constant_string_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_string_info_AST = null;
		short string_index=0;
		
		string_index=u2();
		constant_string_info_AST = (AST)currentAST.root;
		constant_string_info_AST = new ShortAST( CONSTANT_STRINGINFO, string_index);
		currentAST.root = constant_string_info_AST;
		currentAST.child = constant_string_info_AST!=null &&constant_string_info_AST.getFirstChild()!=null ?
			constant_string_info_AST.getFirstChild() : constant_string_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_string_info_AST;
	}
	
	public final void constant_integer_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_integer_info_AST = null;
		int val=0;
		
		val=u4();
		constant_integer_info_AST = (AST)currentAST.root;
		constant_integer_info_AST = new ObjectAST(CONSTANT_INTEGERINFO, new Integer(val));
		currentAST.root = constant_integer_info_AST;
		currentAST.child = constant_integer_info_AST!=null &&constant_integer_info_AST.getFirstChild()!=null ?
			constant_integer_info_AST.getFirstChild() : constant_integer_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_integer_info_AST;
	}
	
	public final void constant_float_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_float_info_AST = null;
		int bytes=0;
		
		bytes=u4();
		constant_float_info_AST = (AST)currentAST.root;
		constant_float_info_AST = new ObjectAST(CONSTANT_FLOATINFO, new Double(Float.intBitsToFloat(bytes)));
		currentAST.root = constant_float_info_AST;
		currentAST.child = constant_float_info_AST!=null &&constant_float_info_AST.getFirstChild()!=null ?
			constant_float_info_AST.getFirstChild() : constant_float_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_float_info_AST;
	}
	
	public final void constant_long_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_long_info_AST = null;
		int high_bytes=0, low_bytes=0; long val = 0L;
		
		high_bytes=u4();
		low_bytes=u4();
		constant_long_info_AST = (AST)currentAST.root;
		constant_long_info_AST = new ObjectAST(CONSTANT_LONGINFO, new Long((long)high_bytes | ((long)low_bytes & 0xFFFFL)));
		currentAST.root = constant_long_info_AST;
		currentAST.child = constant_long_info_AST!=null &&constant_long_info_AST.getFirstChild()!=null ?
			constant_long_info_AST.getFirstChild() : constant_long_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_long_info_AST;
	}
	
	public final void constant_double_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_double_info_AST = null;
		int high_bytes=0, low_bytes=0;
		
		high_bytes=u4();
		low_bytes=u4();
		constant_double_info_AST = (AST)currentAST.root;
		constant_double_info_AST = new ObjectAST(CONSTANT_DOUBLEINFO, new Double(Double.longBitsToDouble( (long)high_bytes | ((long)low_bytes & 0xFFFFL))));
		
		currentAST.root = constant_double_info_AST;
		currentAST.child = constant_double_info_AST!=null &&constant_double_info_AST.getFirstChild()!=null ?
			constant_double_info_AST.getFirstChild() : constant_double_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_double_info_AST;
	}
	
	public final void constant_name_and_type_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_name_and_type_info_AST = null;
		short name_index=0, descriptor_index=0;
		
		name_index=u2();
		descriptor_index=u2();
		constant_name_and_type_info_AST = (AST)currentAST.root;
		
			     constant_name_and_type_info_AST = new ShortAST(CONSTANT_NAME_TYPE_INFO,name_index);
			     constant_name_and_type_info_AST.addChild(new ShortAST(CONSTANT_STRINGINFO,descriptor_index));
			
		currentAST.root = constant_name_and_type_info_AST;
		currentAST.child = constant_name_and_type_info_AST!=null &&constant_name_and_type_info_AST.getFirstChild()!=null ?
			constant_name_and_type_info_AST.getFirstChild() : constant_name_and_type_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_name_and_type_info_AST;
	}
	
	public final void constant_utf8_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_utf8_info_AST = null;
		
		short length=0;
		byte [] bytes;
		byte bytebuf=0;
		int bytepos=0;
		
		
		length=u2();
		bytes = new byte[length];
		{
		_loop21:
		do {
			if (((LA(1)==BYTE))&&(length > 0)) {
				bytebuf=u1();
				bytes[bytepos++] = bytebuf; length--;
			}
			else {
				break _loop21;
			}
			
		} while (true);
		}
		if (!(length==0))
		  throw new SemanticException("length==0");
		constant_utf8_info_AST = (AST)currentAST.root;
		
		String name = new String(bytes);
			    name= name.replace('/','.'); 
			    if(name.startsWith("[") && name.endsWith("]")) {
				name = name.substring(1,name.length()-1) + "[]";
			    }
			    constant_utf8_info_AST = (AST)astFactory.create(CONSTANT_UTF8STRING,name);
		
		currentAST.root = constant_utf8_info_AST;
		currentAST.child = constant_utf8_info_AST!=null &&constant_utf8_info_AST.getFirstChild()!=null ?
			constant_utf8_info_AST.getFirstChild() : constant_utf8_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = constant_utf8_info_AST;
	}
	
	public final void access_modifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST access_modifiers_AST = null;
		short modifiers=0;
		
		modifiers=u2();
		access_modifiers_AST = (AST)currentAST.root;
		access_modifiers_AST = new ShortAST( ACCESS_MODIFIERS, modifiers);
		currentAST.root = access_modifiers_AST;
		currentAST.child = access_modifiers_AST!=null &&access_modifiers_AST.getFirstChild()!=null ?
			access_modifiers_AST.getFirstChild() : access_modifiers_AST;
		currentAST.advanceChildToEnd();
		returnAST = access_modifiers_AST;
	}
	
	public final void class_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST class_info_AST = null;
		short class_info_index = 0;
		
		class_info_index=u2();
		class_info_AST = (AST)currentAST.root;
		
		String class_name = getConstant(((ShortAST)getConstant(class_info_index)).getShortValue()).getText();
			    setClassName(class_name);
			    class_info_AST = (AST)astFactory.create(IDENT,class_name);
			
		currentAST.root = class_info_AST;
		currentAST.child = class_info_AST!=null &&class_info_AST.getFirstChild()!=null ?
			class_info_AST.getFirstChild() : class_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = class_info_AST;
	}
	
	public final void superclass_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST superclass_info_AST = null;
		short class_info_index = 0;
		
		class_info_index=u2();
		superclass_info_AST = (AST)currentAST.root;
		
		String class_name = getConstant(((ShortAST)getConstant(class_info_index)).getShortValue()).getText();
			    superclass_info_AST = (AST)astFactory.create(IDENT,class_name);
			
		currentAST.root = superclass_info_AST;
		currentAST.child = superclass_info_AST!=null &&superclass_info_AST.getFirstChild()!=null ?
			superclass_info_AST.getFirstChild() : superclass_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = superclass_info_AST;
	}
	
	public final void interface_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interface_block_AST = null;
		short interfaces_count=0;
		
		interfaces_count=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop28:
		do {
			if (((LA(1)==BYTE))&&(interfaces_count > 0)) {
				interface_info();
				astFactory.addASTChild(currentAST, returnAST);
				interfaces_count--;
			}
			else {
				break _loop28;
			}
			
		} while (true);
		}
		if (!(interfaces_count==0))
		  throw new SemanticException("interfaces_count==0");
		interface_block_AST = (AST)currentAST.root;
		returnAST = interface_block_AST;
	}
	
	public final void interface_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST interface_info_AST = null;
		short interface_index=0;
		
		interface_index=u2();
		interface_info_AST = (AST)currentAST.root;
		
		String interface_name = getConstant(((ShortAST)getConstant(interface_index)).getShortValue()).getText();
			     interface_info_AST = (AST)astFactory.create(IDENT,interface_name);
			
		currentAST.root = interface_info_AST;
		currentAST.child = interface_info_AST!=null &&interface_info_AST.getFirstChild()!=null ?
			interface_info_AST.getFirstChild() : interface_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = interface_info_AST;
	}
	
	public final void field_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST field_info_AST = null;
		
		short access_flags=0;
		short name_index=0;
		short descriptor_index=0;
		short attributes_count;
		
		
		access_flags=u2();
		astFactory.addASTChild(currentAST, returnAST);
		name_index=u2();
		astFactory.addASTChild(currentAST, returnAST);
		descriptor_index=u2();
		astFactory.addASTChild(currentAST, returnAST);
		attributes_count=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop35:
		do {
			if (((LA(1)==BYTE))&&(attributes_count > 0)) {
				attribute_info();
				astFactory.addASTChild(currentAST, returnAST);
				attributes_count--;
			}
			else {
				break _loop35;
			}
			
		} while (true);
		}
		if (!(attributes_count==0))
		  throw new SemanticException("attributes_count==0");
		field_info_AST = (AST)currentAST.root;
		
			     AST access = new ShortAST(ACCESS_MODIFIERS,access_flags);
			     String typeIdent = convertDescriptor(getConstant(descriptor_index).getText());
			     String name = getConstant(name_index).getText();
			     field_info_AST = (AST)astFactory.make( (new ASTArray(5)).add(field_info_AST).add((AST)astFactory.create(VARIABLE_DEF)).add(access).add((AST)astFactory.create(TYPE,typeIdent)).add((AST)astFactory.create(IDENT,name)));
			
		currentAST.root = field_info_AST;
		currentAST.child = field_info_AST!=null &&field_info_AST.getFirstChild()!=null ?
			field_info_AST.getFirstChild() : field_info_AST;
		currentAST.advanceChildToEnd();
		field_info_AST = (AST)currentAST.root;
		returnAST = field_info_AST;
	}
	
	public final void attribute_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST attribute_info_AST = null;
		AST cattr_AST = null;
		AST cvattr_AST = null;
		AST exattr_AST = null;
		AST lnattr_AST = null;
		AST lattr_AST = null;
		AST sattr_AST = null;
		
		short attribute_name_index=0;
		int attribute_length=0;
		String attribute_name=null;
		byte [] info;
		int bytepos=0;
		byte bytebuf=0;
		
		
		try {      // for error handling
			attribute_name_index=u2();
			if (!(getConstant(attribute_name_index).getType()==CONSTANT_UTF8STRING))
			  throw new SemanticException("getConstant(attribute_name_index).getType()==CONSTANT_UTF8STRING");
			attribute_name = getConstant(attribute_name_index).getText();
			attribute_length=u4();
			{
			if (((LA(1)==BYTE))&&("Code".equals(attribute_name))) {
				code_attribute();
				cattr_AST = (AST)returnAST;
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = cattr_AST;
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else if (((LA(1)==BYTE))&&("ConstantValue".equals(attribute_name))) {
				constantValue_attribute();
				cvattr_AST = (AST)returnAST;
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = cvattr_AST;
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else if (((LA(1)==BYTE))&&("Exceptions".equals(attribute_name))) {
				exceptions_attribute();
				exattr_AST = (AST)returnAST;
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = exattr_AST;
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else if (((LA(1)==BYTE))&&("LineNumberTable".equals(attribute_name))) {
				lineNumberTable_attribute();
				lnattr_AST = (AST)returnAST;
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = lnattr_AST;
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else if (((LA(1)==BYTE))&&("LocalVariableTable".equals(attribute_name))) {
				localVariableTable_attribute();
				lattr_AST = (AST)returnAST;
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = lattr_AST;
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else if (((LA(1)==BYTE))&&(attribute_length==2 && "SourceFile".equals(attribute_name))) {
				sourcefile_attribute();
				sattr_AST = (AST)returnAST;
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = sattr_AST;
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else if ((LA(1)==EOF||LA(1)==BYTE)) {
				info = new byte[attribute_length];
				{
				_loop49:
				do {
					if (((LA(1)==BYTE))&&(bytepos < attribute_length)) {
						bytebuf=u1();
						info[bytepos++] = bytebuf;
					}
					else {
						break _loop49;
					}
					
				} while (true);
				}
				if (!(bytepos==attribute_length))
				  throw new SemanticException("bytepos==attribute_length");
				attribute_info_AST = (AST)currentAST.root;
				attribute_info_AST = (AST)astFactory.create(UNKNOWN_ATTRIBUTE,attribute_name);
				currentAST.root = attribute_info_AST;
				currentAST.child = attribute_info_AST!=null &&attribute_info_AST.getFirstChild()!=null ?
					attribute_info_AST.getFirstChild() : attribute_info_AST;
				currentAST.advanceChildToEnd();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (SemanticException se) {
		}
		returnAST = attribute_info_AST;
	}
	
	public final void method_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST method_info_AST = null;
		AST attr_AST = null;
		
		short access_flags=0;
		short name_index=0;
		short descriptor_index=0;
		short attributes_count=0;
		AST exceptions = (AST)astFactory.create(THROWS);  // Create a empty exception clause.
		
		
		access_flags=u2();
		name_index=u2();
		descriptor_index=u2();
		attributes_count=u2();
		{
		_loop42:
		do {
			if (((LA(1)==BYTE))&&(attributes_count > 0)) {
				attribute_info();
				attr_AST = (AST)returnAST;
				{
				if (((LA(1)==BYTE))&&(attr_AST != null && THROWS == attr_AST.getType())) {
					exceptions = attr_AST;
				}
				else if ((LA(1)==BYTE)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				attributes_count--;
			}
			else {
				break _loop42;
			}
			
		} while (true);
		}
		if (!(attributes_count==0))
		  throw new SemanticException("attributes_count==0");
		method_info_AST = (AST)currentAST.root;
		
			      String [] method_descriptor = convertMethodDescriptor(getConstant(descriptor_index).getText());
			      AST parameters = new CommonAST();
			      parameters.setType(PARAMETERS);
			      for(int i=1; i < method_descriptor.length; i++) {
				 ShortAST access = new ShortAST(ACCESS_MODIFIERS, (short)0);
				 String paramType = method_descriptor[i];
				 String paramIdent = "param" + i;
				 AST param = (AST)astFactory.make( (new ASTArray(4)).add((AST)astFactory.create(PARAMETER_DEF)).add(access).add((AST)astFactory.create(TYPE,paramType)).add((AST)astFactory.create(IDENT,paramIdent)));
				 parameters.addChild(param);
			      }
		
			      AST access = new ShortAST(ACCESS_MODIFIERS,access_flags);
			      String ident = getConstant(name_index).getText();
			      if( "<init>".equals(ident)) {  // is this a constructor?
				  ident = getClassName();  // Use the class name as the constructor's method name.
				  method_info_AST = (AST)astFactory.make( (new ASTArray(5)).add((AST)astFactory.create(CTOR_DEF)).add(access).add((AST)astFactory.create(IDENT,ident)).add(parameters).add(exceptions));
			      } else {
			          String retType = method_descriptor[0];
			          method_info_AST = (AST)astFactory.make( (new ASTArray(6)).add((AST)astFactory.create(METHOD_DEF)).add(access).add((AST)astFactory.create(TYPE,retType)).add((AST)astFactory.create(IDENT,ident)).add(parameters).add(exceptions));
			      }
			
		currentAST.root = method_info_AST;
		currentAST.child = method_info_AST!=null &&method_info_AST.getFirstChild()!=null ?
			method_info_AST.getFirstChild() : method_info_AST;
		currentAST.advanceChildToEnd();
		returnAST = method_info_AST;
	}
	
	public final void code_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST code_attribute_AST = null;
		
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
		
		
		max_stack=u2();
		astFactory.addASTChild(currentAST, returnAST);
		max_locals=u2();
		astFactory.addASTChild(currentAST, returnAST);
		code_length=u4();
		astFactory.addASTChild(currentAST, returnAST);
		code = new byte[code_length];
		{
		_loop54:
		do {
			if (((LA(1)==BYTE))&&(codepos < code_length)) {
				bytebuf=u1();
				astFactory.addASTChild(currentAST, returnAST);
				code[codepos++] = bytebuf;
			}
			else {
				break _loop54;
			}
			
		} while (true);
		}
		if (!(codepos==code_length))
		  throw new SemanticException("codepos==code_length");
		exception_table_length=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop56:
		do {
			if (((LA(1)==BYTE))&&(exceptionpos < ((int)exception_table_length & 0xffff))) {
				exception_table_entry();
				astFactory.addASTChild(currentAST, returnAST);
				exceptionpos++;
			}
			else {
				break _loop56;
			}
			
		} while (true);
		}
		if (!(exceptionpos==((int)exception_table_length & 0xffff)))
		  throw new SemanticException("exceptionpos==((int)exception_table_length & 0xffff)");
		attribute_count=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop58:
		do {
			if (((LA(1)==BYTE))&&(attributepos < ((int)attribute_count & 0xffff))) {
				attribute_info();
				astFactory.addASTChild(currentAST, returnAST);
				attributepos++;
			}
			else {
				break _loop58;
			}
			
		} while (true);
		}
		if (!(attributepos==((int)attribute_count & 0xffff)))
		  throw new SemanticException("attributepos==((int)attribute_count & 0xffff)");
		code_attribute_AST = (AST)currentAST.root;
		returnAST = code_attribute_AST;
	}
	
	public final void constantValue_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constantValue_attribute_AST = null;
		short constantvalue_index = 0;
		
		constantvalue_index=u2();
		constantValue_attribute_AST = (AST)currentAST.root;
		constantValue_attribute_AST = new ShortAST(ATTRIBUTE_CONSTANT, constantvalue_index);
		currentAST.root = constantValue_attribute_AST;
		currentAST.child = constantValue_attribute_AST!=null &&constantValue_attribute_AST.getFirstChild()!=null ?
			constantValue_attribute_AST.getFirstChild() : constantValue_attribute_AST;
		currentAST.advanceChildToEnd();
		returnAST = constantValue_attribute_AST;
	}
	
	public final void exceptions_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exceptions_attribute_AST = null;
		
		short number_of_exceptions = 0;
		int indexpos=0;
		
		
		number_of_exceptions=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop62:
		do {
			if (((LA(1)==BYTE))&&(indexpos < ((int)number_of_exceptions & 0xffff))) {
				exception_index_entry();
				astFactory.addASTChild(currentAST, returnAST);
				indexpos++;
			}
			else {
				break _loop62;
			}
			
		} while (true);
		}
		if (!(indexpos==((int)number_of_exceptions & 0xffff)))
		  throw new SemanticException("indexpos==((int)number_of_exceptions & 0xffff)");
		exceptions_attribute_AST = (AST)currentAST.root;
		exceptions_attribute_AST = (AST)astFactory.make( (new ASTArray(2)).add((AST)astFactory.create(THROWS)).add(exceptions_attribute_AST));
		currentAST.root = exceptions_attribute_AST;
		currentAST.child = exceptions_attribute_AST!=null &&exceptions_attribute_AST.getFirstChild()!=null ?
			exceptions_attribute_AST.getFirstChild() : exceptions_attribute_AST;
		currentAST.advanceChildToEnd();
		exceptions_attribute_AST = (AST)currentAST.root;
		returnAST = exceptions_attribute_AST;
	}
	
	public final void lineNumberTable_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST lineNumberTable_attribute_AST = null;
		
		short line_number_table_length = 0; 
		int entrypos = 0;
		
		
		line_number_table_length=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop66:
		do {
			if (((LA(1)==BYTE))&&(entrypos < ((int)line_number_table_length & 0xffff))) {
				lineNumberTableEntry();
				astFactory.addASTChild(currentAST, returnAST);
				entrypos++;
			}
			else {
				break _loop66;
			}
			
		} while (true);
		}
		if (!(entrypos==((int)line_number_table_length & 0xffff)))
		  throw new SemanticException("entrypos==((int)line_number_table_length & 0xffff)");
		lineNumberTable_attribute_AST = (AST)currentAST.root;
		returnAST = lineNumberTable_attribute_AST;
	}
	
	public final void localVariableTable_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST localVariableTable_attribute_AST = null;
		
		short local_variable_table_length = 0; 
		int entrypos=0;
		
		
		local_variable_table_length=u2();
		astFactory.addASTChild(currentAST, returnAST);
		{
		_loop70:
		do {
			if (((LA(1)==BYTE))&&(entrypos < ((int)local_variable_table_length & 0xffff))) {
				localVariableTableEntry();
				astFactory.addASTChild(currentAST, returnAST);
				entrypos++;
			}
			else {
				break _loop70;
			}
			
		} while (true);
		}
		if (!(entrypos==((int)local_variable_table_length & 0xffff)))
		  throw new SemanticException("entrypos==((int)local_variable_table_length & 0xffff)");
		localVariableTable_attribute_AST = (AST)currentAST.root;
		returnAST = localVariableTable_attribute_AST;
	}
	
	public final void sourcefile_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sourcefile_attribute_AST = null;
		short sourcefile_index = 0;
		
		sourcefile_index=u2();
		sourcefile_attribute_AST = (AST)currentAST.root;
		
			     String sourcefile_name = getConstant(sourcefile_index).getText();
			     sourcefile_attribute_AST = (AST)astFactory.create(SOURCEFILE,sourcefile_name);
			
		currentAST.root = sourcefile_attribute_AST;
		currentAST.child = sourcefile_attribute_AST!=null &&sourcefile_attribute_AST.getFirstChild()!=null ?
			sourcefile_attribute_AST.getFirstChild() : sourcefile_attribute_AST;
		currentAST.advanceChildToEnd();
		returnAST = sourcefile_attribute_AST;
	}
	
	public final void exception_table_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exception_table_entry_AST = null;
		
		short start_pc = 0;
		short end_pc = 0;
		short handler_pc = 0;
		short catch_type = 0;
		
		
		start_pc=u2();
		astFactory.addASTChild(currentAST, returnAST);
		end_pc=u2();
		astFactory.addASTChild(currentAST, returnAST);
		handler_pc=u2();
		astFactory.addASTChild(currentAST, returnAST);
		catch_type=u2();
		astFactory.addASTChild(currentAST, returnAST);
		exception_table_entry_AST = (AST)currentAST.root;
		returnAST = exception_table_entry_AST;
	}
	
	public final void exception_index_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exception_index_entry_AST = null;
		short index=0;
		
		index=u2();
		if (!(index != 0))
		  throw new SemanticException("index != 0");
		exception_index_entry_AST = (AST)currentAST.root;
		
			     // The index references a Class_info structure in the constant pool,
			     // that we can use to get the name of the exception (class).
		String exception_name = getConstant(((ShortAST)getConstant(index)).getShortValue()).getText();
			     exception_index_entry_AST = (AST)astFactory.create(IDENT,exception_name);
			
		currentAST.root = exception_index_entry_AST;
		currentAST.child = exception_index_entry_AST!=null &&exception_index_entry_AST.getFirstChild()!=null ?
			exception_index_entry_AST.getFirstChild() : exception_index_entry_AST;
		currentAST.advanceChildToEnd();
		returnAST = exception_index_entry_AST;
	}
	
	public final void lineNumberTableEntry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST lineNumberTableEntry_AST = null;
		short start_pc=0, line_number=0;
		
		start_pc=u2();
		astFactory.addASTChild(currentAST, returnAST);
		line_number=u2();
		astFactory.addASTChild(currentAST, returnAST);
		lineNumberTableEntry_AST = (AST)currentAST.root;
		returnAST = lineNumberTableEntry_AST;
	}
	
	public final void localVariableTableEntry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST localVariableTableEntry_AST = null;
		short start_pc = 0, length = 0, name_index = 0, descriptor_index = 0, index = 0;
		
		start_pc=u2();
		astFactory.addASTChild(currentAST, returnAST);
		length=u2();
		astFactory.addASTChild(currentAST, returnAST);
		name_index=u2();
		astFactory.addASTChild(currentAST, returnAST);
		descriptor_index=u2();
		astFactory.addASTChild(currentAST, returnAST);
		index=u2();
		astFactory.addASTChild(currentAST, returnAST);
		localVariableTableEntry_AST = (AST)currentAST.root;
		returnAST = localVariableTableEntry_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"ACCESS_MODIFIERS",
		"ATTRIBUTE_CONSTANT",
		"CLASS_DEF",
		"CONSTANT_CLASSINFO",
		"CONSTANT_DOUBLEINFO",
		"CONSTANT_FIELDINFO",
		"CONSTANT_FLOATINFO",
		"CONSTANT_INTEGERINFO",
		"CONSTANT_INTERFACE_METHODINFO",
		"CONSTANT_LONGINFO",
		"CONSTANT_METHODINFO",
		"CONSTANT_NAME_TYPE_INFO",
		"CONSTANT_STRINGINFO",
		"CONSTANT_UTF8STRING",
		"CTOR_DEF",
		"EXTENDS_CLAUSE",
		"IDENT",
		"IMPLEMENTS_CLAUSE",
		"INTERFACE_DEF",
		"MAGIC",
		"METHOD_DEF",
		"PARAMETERS",
		"PARAMETER_DEF",
		"SOURCEFILE",
		"THROWS",
		"TYPE",
		"UNKNOWN_ATTRIBUTE",
		"VARIABLE_DEF",
		"VERSION",
		"BYTE"
	};
	
	
	}
