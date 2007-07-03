// $Id: CrReservedName.java 12950 2007-07-01 08:10:04Z tfmorris $
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

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
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

    private static List<String> names;
    
    private static List<String> umlReserved = new ArrayList<String>();

    static {
	umlReserved.add("none");
	umlReserved.add("interface");
	umlReserved.add("sequential");
	umlReserved.add("guarded");
	umlReserved.add("concurrent");
	umlReserved.add("frozen");
	umlReserved.add("aggregate");
	umlReserved.add("composite");
    }

    static {
	umlReserved.add("becomes");
	umlReserved.add("call");
	umlReserved.add("component");
	//umlReserved.add("copy");
	//umlReserved.add("create");
	umlReserved.add("deletion");
	umlReserved.add("derived");
	//umlReserved.add("document");
	umlReserved.add("enumeration");
	umlReserved.add("extends");
    }

    static {
	umlReserved.add("facade");
	//umlReserved.add("file");
	umlReserved.add("framework");
	umlReserved.add("friend");
	umlReserved.add("import");
	umlReserved.add("inherits");
	umlReserved.add("instance");
	umlReserved.add("invariant");
	umlReserved.add("library");
	//umlReserved.add("node");
	umlReserved.add("metaclass");
	umlReserved.add("powertype");
	umlReserved.add("private");
	umlReserved.add("process");
	umlReserved.add("requirement");
	//umlReserved.add("send");
	umlReserved.add("stereotype");
	umlReserved.add("stub");
	umlReserved.add("subclass");
	umlReserved.add("subtype");
	umlReserved.add("system");
	umlReserved.add("table");
	umlReserved.add("thread");
	umlReserved.add("type");
    }

    static {
	umlReserved.add("useCaseModel");
	umlReserved.add("uses");
	umlReserved.add("utility");
	//umlReserved.add("destroy");
	umlReserved.add("implementationClass");
	umlReserved.add("postcondition");
	umlReserved.add("precondition");
	umlReserved.add("topLevelPackage");
	umlReserved.add("subtraction");

	//     umlReserved.add("initial");
	//     umlReserved.add("final");
	//     umlReserved.add("fork");
	//     umlReserved.add("join");
	//     umlReserved.add("history");
    }


    /**
     * Default constructor.  Builds a critic that checks UML reserved names.
     */
    public CrReservedName() {
        this(umlReserved);
    }
    
    /**
     * Construct a critic that checks against the given list of reserved names.
     * 
     * @param reservedNames the list of reserved names to check against.
     */
    public CrReservedName(List<String> reservedNames) {
        setupHeadAndDesc();
        setPriority(ToDoItem.HIGH_PRIORITY);
        addSupportedDecision(UMLDecision.NAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("name");
        addTrigger("feature_name");
        names = reservedNames;
    }


    @Override
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

        for (String name : names) {
            if (name.equalsIgnoreCase(nameStr)) {
                return PROBLEM_FOUND;
            }
        }

        return NO_PROBLEM;
    }

    /**
<<<<<<< .mine
     * Dont critique the built-in types, they are supposed to
     * have those "reserved" names.
=======
     * Don't critique elements from the profile, they may have names
     * which are nominally "reserved."
>>>>>>> .r12950
     *
     * @param name The name of the type to test.
     * @return true if it is a builtin.
     */
    protected boolean isBuiltin(String name) {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object type = p.findTypeInProfileModels(name);
        return type != null;
    }


    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    @Override
    public Icon getClarifier() {
        return ClClassName.getTheInstance();
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#initWizard(
     *         org.argouml.cognitive.ui.Wizard)
     */
    @Override
    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = (ToDoItem) w.getToDoItem();
	    String sug =
	        Model.getFacade().getName(item.getOffenders().get(0));
	    String ins = super.getInstructions();
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	    ((WizMEName) w).setMustEdit(true);
	}
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#getWizardClass(org.argouml.cognitive.ToDoItem)
     */
    @Override
    public Class getWizardClass(ToDoItem item) {
        return WizMEName.class;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5839267391209851505L;
}

