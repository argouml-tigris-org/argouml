// $ANTLR 2.7.1: "classfile.tree.g" -> "ClassfileTreeParser.java"$

package org.argouml.uml.reveng.classfile;

import org.argouml.uml.reveng.java.*;
import java.util.*;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


/*************************************
 * A tree parser for a Java classfile.
 *************************************/
public class ClassfileTreeParser extends antlr.TreeParser
       implements ClassfileTreeParserTokenTypes
 {

    // The modeller to create the meta model objects.
    private Modeller _modeller;

    /**
     * Return the modeller of this parser.
     *
     * @return The modeller of this parser.
     */
    public final Modeller getModeller() {
        return _modeller;
    }

    /**
     * Set the modeller of this parser.
     *
     * @param modeller The new modeller of this parser.
     */
    public final void setModeller( Modeller modeller) {
        _modeller = modeller;
    }

    /**
     * Split class and package name and set package.
     * 
     * @param classname The fully qualified classname.
     *
     * @return The class name.
     */
    private final String splitPackageFromClass(String classname) {
	int lastDot = classname.lastIndexOf('.');
	if(lastDot != -1) {
	    getModeller().addPackage(classname.substring(0,lastDot));
	    classname = classname.substring(lastDot + 1);
	}
	return classname;
    }
public ClassfileTreeParser() {
	tokenNames = _tokenNames;
}

	public final void classfile(AST _t,
		Modeller modeller
	) throws RecognitionException {
		
		AST classfile_AST_in = (AST)_t;
		setModeller(modeller);
		
		magic_number(_t);
		_t = _retTree;
		version_number(_t);
		_t = _retTree;
		typeDefinition(_t);
		_t = _retTree;
		attribute_block(_t);
		_t = _retTree;
		method_block(_t);
		_t = _retTree;
		getModeller().popClassifier();
		_retTree = _t;
	}
	
	public final void magic_number(AST _t) throws RecognitionException {
		
		AST magic_number_AST_in = (AST)_t;
		
		AST tmp1_AST_in = (AST)_t;
		match(_t,MAGIC);
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final void version_number(AST _t) throws RecognitionException {
		
		AST version_number_AST_in = (AST)_t;
		
		AST tmp2_AST_in = (AST)_t;
		match(_t,VERSION);
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final void typeDefinition(AST _t) throws RecognitionException {
		
		AST typeDefinition_AST_in = (AST)_t;
		
		short modifiers=0;
		String class_name=null;
		String superclass_name=null;
		Vector interfaces = new Vector();
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case INTERFACE_DEF:
		{
			AST __t5 = _t;
			AST tmp3_AST_in = (AST)_t;
			match(_t,INTERFACE_DEF);
			_t = _t.getFirstChild();
			modifiers=access_modifiers(_t);
			_t = _retTree;
			class_name=class_info(_t);
			_t = _retTree;
			AST __t6 = _t;
			AST tmp4_AST_in = (AST)_t;
			match(_t,EXTENDS_CLAUSE);
			_t = _t.getFirstChild();
			interface_block(_t,interfaces);
			_t = _retTree;
			_t = __t6;
			_t = _t.getNextSibling();
			_t = __t5;
			_t = _t.getNextSibling();
			getModeller().addInterface( splitPackageFromClass(class_name), modifiers, interfaces, null);
			break;
		}
		case CLASS_DEF:
		{
			AST __t7 = _t;
			AST tmp5_AST_in = (AST)_t;
			match(_t,CLASS_DEF);
			_t = _t.getFirstChild();
			modifiers=access_modifiers(_t);
			_t = _retTree;
			class_name=class_info(_t);
			_t = _retTree;
			AST __t8 = _t;
			AST tmp6_AST_in = (AST)_t;
			match(_t,EXTENDS_CLAUSE);
			_t = _t.getFirstChild();
			superclass_name=class_info(_t);
			_t = _retTree;
			_t = __t8;
			_t = _t.getNextSibling();
			AST __t9 = _t;
			AST tmp7_AST_in = (AST)_t;
			match(_t,IMPLEMENTS_CLAUSE);
			_t = _t.getFirstChild();
			interface_block(_t,interfaces);
			_t = _retTree;
			_t = __t9;
			_t = _t.getNextSibling();
			_t = __t7;
			_t = _t.getNextSibling();
			
				       if( "java.lang.Object".equals(superclass_name)) {
					   superclass_name=null;  
				       }
				       getModeller().addClass( splitPackageFromClass(class_name), modifiers, superclass_name, interfaces, null);
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
	}
	
	public final void attribute_block(AST _t) throws RecognitionException {
		
		AST attribute_block_AST_in = (AST)_t;
		
		{
		_loop17:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==VARIABLE_DEF)) {
				attribute_info(_t);
				_t = _retTree;
			}
			else {
				break _loop17;
			}
			
		} while (true);
		}
		_retTree = _t;
	}
	
	public final void method_block(AST _t) throws RecognitionException {
		
		AST method_block_AST_in = (AST)_t;
		
		{
		_loop21:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CTOR_DEF:
			{
				ctorDef(_t);
				_t = _retTree;
				break;
			}
			case METHOD_DEF:
			{
				methodDecl(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				break _loop21;
			}
			}
		} while (true);
		}
		_retTree = _t;
	}
	
	public final short  access_modifiers(AST _t) throws RecognitionException {
		short modifiers;
		
		AST access_modifiers_AST_in = (AST)_t;
		
		AST tmp8_AST_in = (AST)_t;
		match(_t,ACCESS_MODIFIERS);
		_t = _t.getNextSibling();
		modifiers=((ShortAST)tmp8_AST_in).getShortValue();
		_retTree = _t;
		return modifiers;
	}
	
	public final String  class_info(AST _t) throws RecognitionException {
		String name;
		
		AST class_info_AST_in = (AST)_t;
		
		AST tmp9_AST_in = (AST)_t;
		match(_t,IDENT);
		_t = _t.getNextSibling();
		name = tmp9_AST_in.getText();
		_retTree = _t;
		return name;
	}
	
	public final void interface_block(AST _t,
		Vector interfaces
	) throws RecognitionException {
		
		AST interface_block_AST_in = (AST)_t;
		
		{
		_loop14:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==IDENT)) {
				AST tmp10_AST_in = (AST)_t;
				match(_t,IDENT);
				_t = _t.getNextSibling();
				interfaces.addElement( tmp10_AST_in.getText());
			}
			else {
				break _loop14;
			}
			
		} while (true);
		}
		_retTree = _t;
	}
	
	public final void attribute_info(AST _t) throws RecognitionException {
		
		AST attribute_info_AST_in = (AST)_t;
		
		AST tmp11_AST_in = (AST)_t;
		match(_t,VARIABLE_DEF);
		_t = _t.getNextSibling();
		AST tmp12_AST_in = (AST)_t;
		match(_t,ACCESS_MODIFIERS);
		_t = _t.getNextSibling();
		AST tmp13_AST_in = (AST)_t;
		match(_t,TYPE);
		_t = _t.getNextSibling();
		AST tmp14_AST_in = (AST)_t;
		match(_t,IDENT);
		_t = _t.getNextSibling();
		// Add the attribute to the model element, that holds
			      // the class/interface info.
			      getModeller().addAttribute( ((ShortAST)tmp12_AST_in).getShortValue(), 
							  tmp13_AST_in.getText(),
							  tmp14_AST_in.getText(),
							  null,	     // I parse no initializers yet.
							  null);     // And there's no javadoc info available.
			
		_retTree = _t;
	}
	
	public final void ctorDef(AST _t) throws RecognitionException {
		
		AST ctorDef_AST_in = (AST)_t;
		Vector params = null;
		
		AST __t23 = _t;
		AST tmp15_AST_in = (AST)_t;
		match(_t,CTOR_DEF);
		_t = _t.getFirstChild();
		AST tmp16_AST_in = (AST)_t;
		match(_t,ACCESS_MODIFIERS);
		_t = _t.getNextSibling();
		AST tmp17_AST_in = (AST)_t;
		match(_t,IDENT);
		_t = _t.getNextSibling();
		params=parameters(_t);
		_t = _retTree;
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case THROWS:
		{
			exceptions(_t);
			_t = _retTree;
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		_t = __t23;
		_t = _t.getNextSibling();
		
			    getModeller().addOperation( ((ShortAST)tmp16_AST_in).getShortValue(),
							null,
							tmp17_AST_in.getText(),
							params,
							null);
			
		_retTree = _t;
	}
	
	public final void methodDecl(AST _t) throws RecognitionException {
		
		AST methodDecl_AST_in = (AST)_t;
		Vector params = null;
		
		AST __t26 = _t;
		AST tmp18_AST_in = (AST)_t;
		match(_t,METHOD_DEF);
		_t = _t.getFirstChild();
		AST tmp19_AST_in = (AST)_t;
		match(_t,ACCESS_MODIFIERS);
		_t = _t.getNextSibling();
		AST tmp20_AST_in = (AST)_t;
		match(_t,TYPE);
		_t = _t.getNextSibling();
		AST tmp21_AST_in = (AST)_t;
		match(_t,IDENT);
		_t = _t.getNextSibling();
		params=parameters(_t);
		_t = _retTree;
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case THROWS:
		{
			exceptions(_t);
			_t = _retTree;
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		_t = __t26;
		_t = _t.getNextSibling();
		
			    getModeller().addOperation( ((ShortAST)tmp19_AST_in).getShortValue(),
							tmp20_AST_in.getText(),
							tmp21_AST_in.getText(),
							params,
							null);
			
		_retTree = _t;
	}
	
	public final Vector  parameters(AST _t) throws RecognitionException {
		Vector params;
		
		AST parameters_AST_in = (AST)_t;
		
		params = new Vector(); 
		Vector currentParam = null;
		
		
		AST __t29 = _t;
		AST tmp22_AST_in = (AST)_t;
		match(_t,PARAMETERS);
		_t = _t.getFirstChild();
		{
		_loop31:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==PARAMETER_DEF)) {
				currentParam=parameterDef(_t);
				_t = _retTree;
				params.add(currentParam);
			}
			else {
				break _loop31;
			}
			
		} while (true);
		}
		_t = __t29;
		_t = _t.getNextSibling();
		_retTree = _t;
		return params;
	}
	
	public final void exceptions(AST _t) throws RecognitionException {
		
		AST exceptions_AST_in = (AST)_t;
		
		AST __t35 = _t;
		AST tmp23_AST_in = (AST)_t;
		match(_t,THROWS);
		_t = _t.getFirstChild();
		{
		_loop37:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==IDENT)) {
				AST tmp24_AST_in = (AST)_t;
				match(_t,IDENT);
				_t = _t.getNextSibling();
			}
			else {
				break _loop37;
			}
			
		} while (true);
		}
		_t = __t35;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final Vector  parameterDef(AST _t) throws RecognitionException {
		Vector param;
		
		AST parameterDef_AST_in = (AST)_t;
		param = new Vector();
		
		AST __t33 = _t;
		AST tmp25_AST_in = (AST)_t;
		match(_t,PARAMETER_DEF);
		_t = _t.getFirstChild();
		AST tmp26_AST_in = (AST)_t;
		match(_t,ACCESS_MODIFIERS);
		_t = _t.getNextSibling();
		AST tmp27_AST_in = (AST)_t;
		match(_t,TYPE);
		_t = _t.getNextSibling();
		AST tmp28_AST_in = (AST)_t;
		match(_t,IDENT);
		_t = _t.getNextSibling();
		_t = __t33;
		_t = _t.getNextSibling();
		
			    param.add(new Short(((ShortAST)tmp26_AST_in).getShortValue()));
			    param.add(tmp27_AST_in.getText());
			    param.add(tmp28_AST_in.getText());
			
		_retTree = _t;
		return param;
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
	
