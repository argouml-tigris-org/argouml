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



// File: PropPanelInstance.java
// Classes: PropPanelInstance
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.common_behavior;
import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import org.argouml.uml.ui.*;
import ru.novosoft.uml.behavior.common_behavior.*;


public class PropPanelInstance extends PropPanel {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelInstance() {
    super("Instance Properties",2);

    Class mclass = MInstance.class;

    addCaption("Name:",0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);


    addCaption("Stereotype:",1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),1,0,0);

    addCaption("Classifier:",2,0,0);
    UMLComboBoxModel classifierModel = new UMLComboBoxModel(this,"isAcceptibleClassifier",
            "classifier","getClassifier","setClassifier",false,MClassifier.class,true);
    addField(new UMLComboBoxNavigator(this,"NavClass",new UMLComboBox(classifierModel)),2,0,0);

    addCaption("Namespace:",3,0,1);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,3,0,1);

    //
    //   temporary
    //
    addCaption("Related Elements",0,1,1);
    JTree tempTree = new JTree(new Object[] { "Slots", "Links", "Stimuli [Recieved, Sent, In Arg List]" });
    addField(tempTree,0,1,1);
  }

    public boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Instance");
    }

    public boolean isAcceptibleClassifier(MModelElement classifier) {
        return classifier instanceof MClassifier;
    }

    public MClassifier getClassifier() {
        MClassifier classifier = null;
        Object target = getTarget();
        if(target instanceof MInstance) {
        //    UML 1.3 apparently has this a 0..n multiplicity
        //    I'll have to figure out what that means
        //            classifier = ((MInstance) target).getClassifier();
        }
        return classifier;
    }

    public void setClassifier(MClassifier element) {
        Object target = getTarget();
        if(target instanceof MInstance) {
//            ((MInstance) target).setClassifier((MClassifier) element);
        }
    }

} /* end class PropPanelInstance */



