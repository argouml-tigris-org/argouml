 // Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.reveng.java;

import java.io.*;
import antlr.collections.AST;
import antlr.collections.impl.*;
import antlr.*;
import org.argouml.kernel.*;
import org.argouml.uml.reveng.*;
import org.argouml.util.logging.*;
import org.argouml.application.api.*;
import ru.novosoft.uml.model_management.*;
import org.tigris.gef.base.*;

import javax.swing.*;
import java.awt.*;

/**
 * This is the main class for Java reverse engineering. It's based
 * on the Antlr Java example.
 *
 * $Revision$
 * $Date$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class JavaImport {

    static private JPanel configPanel = null;

    static private JRadioButton attribute;

    static private JRadioButton datatype;

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     */
    public static JComponent getConfigPanel() {

	if(configPanel == null) {
	    configPanel = new JPanel();
	    configPanel.setLayout(new GridBagLayout());

	    JLabel attributeLabel = new JLabel("Java attributes modelled as");
	    configPanel.add(attributeLabel,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(5, 5, 0, 5),
						   0, 0));			    
	    ButtonGroup group1 = new ButtonGroup();
	    attribute =
		new JRadioButton("UML attributes.");
	    attribute.setSelected(true);
	    group1.add(attribute);
	    configPanel.add(attribute,
		      new GridBagConstraints(GridBagConstraints.RELATIVE,
					     GridBagConstraints.RELATIVE,
					     GridBagConstraints.REMAINDER,
					     1,
					     1.0, 0.0,
					     GridBagConstraints.NORTHWEST,
					     GridBagConstraints.NONE,
					     new Insets(0, 5, 0, 5),
					     0, 0));
	    JRadioButton association =
		new JRadioButton("UML associations.");
	    group1.add(association);
	    configPanel.add(association,
		      new GridBagConstraints(GridBagConstraints.RELATIVE,
					     GridBagConstraints.RELATIVE,
					     GridBagConstraints.REMAINDER,
					     1,
					     1.0, 0.0,
					     GridBagConstraints.NORTHWEST,
					     GridBagConstraints.NONE,
					     new Insets(0, 5, 5, 5),
					     0, 0));

	    ButtonGroup group2 = new ButtonGroup();
	    datatype =
		new JRadioButton("Arrays modelled as datatypes.");
	    datatype.setSelected(true);
	    group2.add(datatype);
	    configPanel.add(datatype,
		      new GridBagConstraints(GridBagConstraints.RELATIVE,
					     GridBagConstraints.RELATIVE,
					     GridBagConstraints.REMAINDER,
					     1,
					     1.0, 0.0,
					     GridBagConstraints.NORTHWEST,
					     GridBagConstraints.NONE,
					     new Insets(5, 5, 0, 5),
					     0, 0));
	    JRadioButton multi =
		new JRadioButton("Arrays modelled with multiplicity 1..n.");
	    group2.add(multi);
	    configPanel.add(multi,
		      new GridBagConstraints(GridBagConstraints.RELATIVE,
					     GridBagConstraints.RELATIVE,
					     GridBagConstraints.REMAINDER,
					     GridBagConstraints.REMAINDER,
					     1.0, 1.0,
					     GridBagConstraints.NORTHWEST,
					     GridBagConstraints.NONE,
					     new Insets(0, 5, 5, 5),
					     0, 0));
	}
	return configPanel;
    }

    /**
     * This method parses 1 Java file.
     *
     * @param f The input file for the parser.
     * @exception Exception Parser exception.
     */
    public static void parseFile( Project p, File f, DiagramInterface diagram)
	throws Exception {
	// Create a scanner that reads from the input stream passed to us
	JavaLexer lexer = new JavaLexer(new BufferedInputStream(new FileInputStream( f)));
	
	// We use a special Argo token, that stores the preceding
	// whitespaces.
	lexer.setTokenObjectClass( "org.argouml.uml.reveng.java.ArgoToken");     
	
	// Create a parser that reads from the scanner
	JavaRecognizer parser = new JavaRecognizer( lexer);
	
	// Create a modeller for the parser
	Modeller modeller = new Modeller((MModel)p.getModel(),
					 diagram,
					 attribute.isSelected(),
					 datatype.isSelected());   
	
	// Print the name of the current file, so we can associate
	// exceptions to the file.
	Console.info("Parsing " + f.getAbsolutePath());
	
	// start parsing at the compilationUnit rule
	parser.compilationUnit(modeller, lexer);

	// Was there an exception thrown during modelling?
	Exception e = modeller.getException();
	if(e != null) {
	    throw e;
	}
    }
}







