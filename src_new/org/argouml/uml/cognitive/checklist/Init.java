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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.argouml.cognitive.checklist.CheckManager;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MOperation;


/** Registers Checklists for different kinds of model elements. If you
 *  add a new checklist, a line must be added here.
 *
 *  @see org.argouml.cognitive.checklist.CheckManager */

public class Init {


    /** static initializer, register all appropriate critics */
    public static void init(Locale locale) {
	ResourceBundle bundle =
	    ResourceBundle
	    .getBundle("org.argouml.i18n.UMLCognitiveResourceBundle", locale);
	addChecklist(bundle,  MClass.class, "ChClass");
	addChecklist(bundle,  MInterface.class, "ChInterface");
	addChecklist(bundle,  MAttribute.class, "ChAttribute");
	addChecklist(bundle,  MOperation.class, "ChOperation");
	addChecklist(bundle,  MAssociation.class, "ChAssociation");
	addChecklist(bundle,  MAssociationClass.class, "ChAssociation");
	addChecklist(bundle,  MState.class, "ChState");
	addChecklist(bundle,  MTransition.class, "ChTransition");
	addChecklist(bundle,  MUseCase.class, "ChUseCase");
	addChecklist(bundle,  MActor.class, "ChActor");
    }

    private static void addChecklist(ResourceBundle bundle,
				     Class cls, String key) {
        try {
            UMLChecklist checklist =
		new UMLChecklist((String[][]) bundle.getObject(key));
            CheckManager.register(cls, checklist);
        }
        catch (MissingResourceException e) {
            e.printStackTrace();
        }
    }

} /* end class Init */
