// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.checklist;

import org.argouml.cognitive.checklist.*;

/** Exacly like Checklist, but makes UMLCheckItems by default. */

public class UMLChecklist extends Checklist {

    /**
     *   This method initializes a UMLChecklist from an
     *   String[][] retrieved from UMLCognitiveResourceBundle
     */
    public UMLChecklist(String[][] categories) {
	String[] names;
	for (int i = 0; i < categories.length; i++) {
	    names = categories[i];
	    super.setNextCategory(names[0]);
	    for (int j = 1; j < names.length; j++) {
		CheckItem item = new UMLCheckItem(_nextCategory, names[j]);
		_items.addElement(item);
	    }
	}
    }

    /**
     * @deprecated As of ArgoUml version unknown (before 0.13.5),
     *             Previously used by ChActor et al,
     *             replaced by (presumably) {@link #UMLChecklist(String[][])}
     */
    public UMLChecklist() {
	/*       used to capture checklist contents
		 String className = getClass().getName();
		 className = className.substring(className.lastIndexOf('.')+1);
		 CrUML.log("    }\n},\n{ \"" + className + "\",");
	*/
    }

    /**
     *   @deprecated As of ArgoUml version unknown (before 0.13.5),
     *   Previously used by ChActor et al
     */
    public void addItem(String description) {
	CheckItem item = new UMLCheckItem(_nextCategory, description);
	_items.addElement(item);
	/*
	  CrUML.log(" \"" +
	  CrUML.escape(CrUML.escape(CrUML.escape(description,'\"',"\\\""),'\n',"\\n"),'\r',"")
	  + "\",");
	*/
    }

    /**
     *   @deprecated As of ArgoUml version unknown (before 0.13.5),
     *   Previously used by ChActor et al
     */
    public void setNextCategory(String category) {
	super.setNextCategory(category);
	/*
	  CrUML.log("    },\n    new String[] { \"" + category + "\",");
	*/
    }

} /* end class UMLChecklist */
