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



// File: PropPanelTransition.java
// Classes: PropPanelTransition
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import org.argouml.uml.ui.*;
import java.util.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;

public class PropPanelTransition extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // contructors
    public PropPanelTransition() {
        super("Transition Properties",2);

        Class mclass = MTransition.class;
    
        addCaption("Name:",0,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

        addCaption("Stereotype:",1,0,0);
        addField(new UMLStereotypeComboBox(this),1,0,0);

        addCaption("State Machine:",2,0,0);
        addLinkField(new UMLList(new UMLReflectionListModel(this,"statemachine",false,"getStateMachine",null,null,null),true),2,0,0);
        
        addCaption("Namespace:",3,0,1);
        addLinkField(new UMLList(new UMLNamespaceListModel(this),true),3,0,0);
    
        addCaption("Trigger:",0,1,0);
        UMLModelElementListModel trigModel = new UMLReflectionListModel(this,"trigger",true,"getTrigger",null,null,"deleteTrigger");
        trigModel.setUpperBound(1);
        addLinkField(new UMLList(trigModel,true),0,1,0);
        
        addCaption("Guard:",1,1,0);
        UMLModelElementListModel guardModel = new UMLReflectionListModel(this,"guard",true,"getGuard",null,"addGuard","deleteGuard");
        guardModel.setUpperBound(1);
        addLinkField(new UMLList(guardModel,true),1,1,0);

        addCaption("Effect:",2,1,1);
        UMLModelElementListModel effectModel = new UMLReflectionListModel(this,"effect",true,"getEffect",null,"addEffect","deleteEffect");
        effectModel.setUpperBound(1);
        addLinkField(new UMLList(effectModel,true),2,1,0);
    
  }

    public MStateMachine getStateMachine() {
        MStateMachine machine = null;
        Object target = getTarget();
        if(target instanceof MTransition) {
            machine = ((MTransition) target).getStateMachine();
        }
        return machine;
    }
    
    
    public MEvent getTrigger() {
        MEvent trigger = null;
        Object target = getTarget();
        if(target instanceof MTransition) {
            trigger= ((MTransition) target).getTrigger();
        }
        return trigger;
    }
    
    
    public void deleteTrigger(Integer index) {
        Object target = getTarget();
        if(target instanceof MTransition) {
            ((MTransition) target).setTrigger(null);
        }
    }

    
    public MGuard getGuard() {
        MGuard guard = null;
        Object target = getTarget();
        if(target instanceof MTransition) {
            guard = ((MTransition) target).getGuard();
        }
        return guard;
    }
    
    public MGuard addGuard(Integer index) {
        MGuard guard = null;
        Object target = getTarget();
        if(target instanceof MTransition) {
            guard = new MGuardImpl();
            ((MTransition) target).setGuard(guard);
        }
        return guard;
    }
    
    public void deleteGuard(Integer index) {
        Object target = getTarget();
        if(target instanceof MTransition) {
            ((MTransition) target).setGuard(null);
        }
    }

    public MAction getEffect() {
        MAction effect = null;
        Object target = getTarget();
        if(target instanceof MTransition) {
            effect = ((MTransition) target).getEffect();
        }
        return effect;
    }
    
    public MAction addEffect(Integer index) {
        MAction effect = null;
        Object target = getTarget();
        if(target instanceof MTransition) {
            effect = new MActionImpl();
            ((MTransition) target).setEffect(effect);
        }
        return effect;
    }
    
    public void deleteEffect(Integer index) {
        Object target = getTarget();
        if(target instanceof MTransition) {
            ((MTransition) target).setEffect(null);
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Transition");
    }
  

    
    
} /* end class PropPanelTransition */

