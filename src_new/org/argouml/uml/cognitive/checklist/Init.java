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

package org.argouml.uml.cognitive.checklist;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.cognitive.checklist.*;

/** Registers Checklists for different kinds of model elements. If you
 *  add a new checklist, a line must be added here.
 *
 *  @see org.argouml.cognitive.checklist.CheckManager */

public class Init {
  // domain independent
  //public static Critic crTooManyDisabled = new CrTooManyDisabled();
  //public static Critic crTooMuchFeedback = new CrTooMuchFeedback();

  // UML specific
  public static Checklist chClass        = new ChClass();
  public static Checklist chAttribute    = new ChAttribute();
  public static Checklist chOperation    = new ChOperation();
  public static Checklist chAssociation  = new ChAssociation();
  public static Checklist chInterface    = new ChInterface();
  public static Checklist chInstance     = new ChInstance();
  public static Checklist chLink         = new ChLink();
  public static Checklist chState        = new ChState();
  public static Checklist chTransition   = new ChTransition();
  public static Checklist chUseCase      = new ChUseCase();
  public static Checklist chActor        = new ChActor();


  /** static initializer, register all appropriate critics */
  public static void init() {
    java.lang.Class modelCls       = MModel.class;
    java.lang.Class classCls       = MClass.class;
    java.lang.Class classifierCls  = MClassifier.class;
    java.lang.Class interfaceCls   = MInterface.class;
    java.lang.Class attrCls        = MAttribute.class;
    java.lang.Class operCls        = MOperation.class;
    java.lang.Class iassocCls      = MAssociation.class;
    java.lang.Class assocCls       = MAssociation.class;
    java.lang.Class assocClassCls  = MAssociationClass.class;
    java.lang.Class namespaceCls   = MNamespace.class;
    java.lang.Class instanceCls    = MInstance.class;
    java.lang.Class linkCls        = MLink.class;
    java.lang.Class stateCls       = MState.class;
    java.lang.Class transitionCls  = MTransition.class;
    java.lang.Class useCaseCls     = MUseCase.class;
    java.lang.Class actorCls       = MActor.class;
    java.lang.Class genElementCls  = MGeneralizableElement.class;
    java.lang.Class genCls         = MGeneralization.class;
    java.lang.Class datatypeCls    = MDataType.class;

    CheckManager.register(operCls, chOperation);
    CheckManager.register(attrCls, chAttribute);
    CheckManager.register(classCls, chClass);
    CheckManager.register(assocCls, chAssociation);
    CheckManager.register(assocClassCls, chAssociation);
    CheckManager.register(interfaceCls, chInterface);
    CheckManager.register(stateCls, chState);
    CheckManager.register(transitionCls, chTransition);
    CheckManager.register(useCaseCls, chUseCase);
    CheckManager.register(actorCls, chActor);
  }


} /* end class Init */
