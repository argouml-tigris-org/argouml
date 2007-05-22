// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Icon;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * This critic checks whether a given name in the Model resembles or matches
 * a reserved UML keyword or java keyword.
 */
public class CrReservedName extends CrUML {

    ////////////////////////////////////////////////////////////////
    // static variables

    private static Vector umlReserved = new Vector();
    private static Vector javaReserved = new Vector();

    static {
	umlReserved.addElement("none");
	umlReserved.addElement("interface");
	umlReserved.addElement("sequential");
	umlReserved.addElement("guarded");
	umlReserved.addElement("concurrent");
	umlReserved.addElement("frozen");
	umlReserved.addElement("aggregate");
	umlReserved.addElement("composite");
    }

    static {
	umlReserved.addElement("becomes");
	umlReserved.addElement("call");
	umlReserved.addElement("component");
	//_umlReserved.addElement("copy");
	//_umlReserved.addElement("create");
	umlReserved.addElement("deletion");
	umlReserved.addElement("derived");
	//_umlReserved.addElement("document");
	umlReserved.addElement("enumeration");
	umlReserved.addElement("extends");
    }

    static {
	umlReserved.addElement("facade");
	//_umlReserved.addElement("file");
	umlReserved.addElement("framework");
	umlReserved.addElement("friend");
	umlReserved.addElement("import");
	umlReserved.addElement("inherits");
	umlReserved.addElement("instance");
	umlReserved.addElement("invariant");
	umlReserved.addElement("library");
	//_umlReserved.addElement("node");
	umlReserved.addElement("metaclass");
	umlReserved.addElement("powertype");
	umlReserved.addElement("private");
	umlReserved.addElement("process");
	umlReserved.addElement("requirement");
	//_umlReserved.addElement("send");
	umlReserved.addElement("stereotype");
	umlReserved.addElement("stub");
	umlReserved.addElement("subclass");
	umlReserved.addElement("subtype");
	umlReserved.addElement("system");
	umlReserved.addElement("table");
	umlReserved.addElement("thread");
	umlReserved.addElement("type");
    }

    static {
	umlReserved.addElement("useCaseModel");
	umlReserved.addElement("uses");
	umlReserved.addElement("utility");
	//_umlReserved.addElement("destroy");
	umlReserved.addElement("implementationClass");
	umlReserved.addElement("postcondition");
	umlReserved.addElement("precondition");
	umlReserved.addElement("topLevelPackage");
	umlReserved.addElement("subtraction");

	//     _umlReserved.addElement("initial");
	//     _umlReserved.addElement("final");
	//     _umlReserved.addElement("fork");
	//     _umlReserved.addElement("join");
	//     _umlReserved.addElement("history");
    }

    static {
	javaReserved.addElement("public");
	javaReserved.addElement("private");
	javaReserved.addElement("protected");
	javaReserved.addElement("package");
	javaReserved.addElement("import");
	javaReserved.addElement("java");
	javaReserved.addElement("class");
	javaReserved.addElement("interface");
	javaReserved.addElement("extends");
	javaReserved.addElement("implements");
	javaReserved.addElement("native");
	javaReserved.addElement("boolean");
	javaReserved.addElement("void");
	javaReserved.addElement("int");
	javaReserved.addElement("char");
	javaReserved.addElement("float");
	javaReserved.addElement("long");
	javaReserved.addElement("short");
	javaReserved.addElement("byte");
	javaReserved.addElement("double");
	javaReserved.addElement("String");
	javaReserved.addElement("Vector");
	javaReserved.addElement("Hashtable");
	javaReserved.addElement("Properties");
    }

    static {
	javaReserved.addElement("null");
	javaReserved.addElement("true");
	javaReserved.addElement("false");
	javaReserved.addElement("rest");
	javaReserved.addElement("operator");
	javaReserved.addElement("inner");
	javaReserved.addElement("outer");
	javaReserved.addElement("this");
	javaReserved.addElement("super");
	javaReserved.addElement("byvalue");
	javaReserved.addElement("cast");
	javaReserved.addElement("const");
	javaReserved.addElement("future");
	javaReserved.addElement("generic");
	javaReserved.addElement("goto");
	javaReserved.addElement("throws");
	javaReserved.addElement("try");
	javaReserved.addElement("catch");
	javaReserved.addElement("finally");
	javaReserved.addElement("new");
    }

    static {
	javaReserved.addElement("synchronized");
	javaReserved.addElement("static");
	javaReserved.addElement("final");
	javaReserved.addElement("abstract");
	javaReserved.addElement("for");
	javaReserved.addElement("if");
	javaReserved.addElement("else");
	javaReserved.addElement("while");
	javaReserved.addElement("return");
	javaReserved.addElement("continue");
	javaReserved.addElement("break");
	javaReserved.addElement("do");
	javaReserved.addElement("until");
	javaReserved.addElement("switch");
	javaReserved.addElement("case");
	javaReserved.addElement("default");
	javaReserved.addElement("instanceof");
	javaReserved.addElement("var");
	javaReserved.addElement("volatile");
	javaReserved.addElement("transient");

	javaReserved.addElement("assert");
    }




    ////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public CrReservedName() {
        setupHeadAndDesc();
	setPriority(ToDoItem.HIGH_PRIORITY);
	addSupportedDecision(UMLDecision.NAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("name");
	addTrigger("feature_name");
    }

    ////////////////////////////////////////////////////////////////
    // Critic implementation

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isPrimaryObject(dm))) {
            return NO_PROBLEM;
        }

        if (!(Model.getFacade().isAModelElement(dm))) {
            return NO_PROBLEM;
        }
        String meName = Model.getFacade().getName(dm);
        if (meName == null || meName.equals("")) {
            return NO_PROBLEM;
        }
        String nameStr = meName;
        if (nameStr == null || nameStr.length() == 0) {
            return NO_PROBLEM;
        }

	if (isBuiltin(nameStr)) {
            return NO_PROBLEM;
        }

        Enumeration names = umlReserved.elements();
        while (names.hasMoreElements()) {
            String word = (String) names.nextElement();
            if (word.equalsIgnoreCase(nameStr)) {
                return PROBLEM_FOUND;
            }
        }

        return NO_PROBLEM;
    }

    /**
     * Dont critique the built-in java types, they are supposed to
     * have those "reserved" names.
     *
     * @param name The name of the type to test.
     * @return true if it is a builtin.
     */
    private boolean isBuiltin(String name) {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object type = p.findTypeInDefaultModel(name);
        return type != null;
    }


    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    public Icon getClarifier() { return ClClassName.getTheInstance(); }

    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *         org.argouml.cognitive.ui.Wizard)
     */
    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    String sug =
	        Model.getFacade().getName(item.getOffenders().elementAt(0));
	    String ins = super.getInstructions();
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	    ((WizMEName) w).setMustEdit(true);
	}
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5839267391209851505L;
} /* end class CrReservedName */

