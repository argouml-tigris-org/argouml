// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Author: Marcus Andersson andersson@users.sourceforge.net
*/


package org.argouml.language.java.generator;

import org.argouml.model.uml.UmlFactory;

import java.io.*;
import java.util.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

/**
   This code piece represents an interface declaration.
*/
public class InterfaceCodePiece extends NamedCodePiece
{
    /** The code piece this interface represents. */
    private CodePiece interfaceDef;

    /** The name of the interface. */
    private String name;

    /**
       Constructor.

       @param interfaceDef The code piece this interface represents.
       @param name The name of the interface.
    */
    public InterfaceCodePiece(CodePiece interfaceDef,
                              String name)
    {
	this.interfaceDef = interfaceDef;
	this.name = name;
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return interfaceDef.getText();
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return interfaceDef.getStartPosition();
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return interfaceDef.getEndPosition();
    }

    /**
	Return the start line
    */
    public int getStartLine()
    {
	return interfaceDef.getStartLine();
    }

    /**
	Return the end line
    */
    public int getEndLine()
    {
	return interfaceDef.getEndLine();
    }

    /**
       Write the code this piece represents to file. This will add one
       level to the stack if the interface is in the model.
    */
    public void write(BufferedReader reader,
                      BufferedWriter writer,
                      Stack parseStateStack) throws Exception
    {
        ParseState parseState = (ParseState)parseStateStack.peek();
        MInterface mInterface = (MInterface)parseState.newClassifier(name);

       if (mInterface != null) {
           parseStateStack.push(new ParseState(mInterface));
           StringBuffer sbText = GeneratorJava.getInstance().generateClassifierStart(mInterface);
           if (sbText != null) {
               writer.write (sbText.toString());
           }
            // dispose code piece in reader
            ffCodePiece(reader,null);
        } else {
            // not in model, so write the original code
            ffCodePiece(reader,writer);
        }
    }
}
