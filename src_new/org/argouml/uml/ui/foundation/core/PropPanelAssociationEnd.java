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

package org.argouml.uml.ui.foundation.core;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;
import java.util.*;

public class PropPanelAssociationEnd extends PropPanelModelElement {

   public PropPanelAssociationEnd() {
    super("AssociationEnd", _assocEndIcon,3);

    Class mclass = MAssociationEnd.class;

    addCaption("Name:",1,0,0);
    addField(nameField,1,0,0);

    addCaption("Stereotype:",2,0,0);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),2,0,0);


    addCaption("Type:",3,0,0);
    UMLComboBoxModel model = new UMLComboBoxModel(this,"isAcceptibleType",
        "type","getType","setType",false,MClassifier.class,true);
    UMLComboBox box = new UMLComboBox(model);
    box.setToolTipText("Warning: Do not use this to change an end that is already in a diagram.");
    addField(new UMLComboBoxNavigator(this,"NavClass",box),3,0,0);

    addCaption("Multiplicity:",4,0,0);
    addField(new UMLMultiplicityComboBox(this,MAssociationEnd.class),4,0,0);

    addCaption("Association:",5,0,1);
    JList namespaceList = new UMLList(
				      new UMLReflectionListModel(this,"association",false,"getAssociation",null,null,null),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    namespaceList.setVisibleRowCount(1);
    addField(new JScrollPane(namespaceList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),5,0,0);

    addCaption("Navigable:",0,1,0);
    addField(new UMLCheckBox(localize("navigable"),this,
      new UMLReflectionBooleanProperty("navigable",mclass,
        "isNavigable","setNavigable")),0,1,0);


    addCaption("Ordering:",1,1,0);
    JPanel orderingPanel = new JPanel(new GridLayout(0,1));
    ButtonGroup orderingGroup = new ButtonGroup();

    UMLRadioButton unordered = new UMLRadioButton(localize("unordered"),this,
      new UMLEnumerationBooleanProperty("ordering",mclass,
        "getOrdering","setOrdering",MOrderingKind.class,MOrderingKind.UNORDERED,null),true);

    orderingGroup.add(unordered);
    orderingPanel.add(unordered);


    UMLRadioButton ordered = new UMLRadioButton(localize("ordered"),this,
      new UMLEnumerationBooleanProperty("ordering",mclass,"getOrdering",
      "setOrdering",MOrderingKind.class,MOrderingKind.ORDERED,null));

    orderingGroup.add(ordered);
    orderingPanel.add(ordered);

    UMLRadioButton sorted = new UMLRadioButton(localize("sorted"),this,
      new UMLEnumerationBooleanProperty("ordering",mclass,"getOrdering",
      "setOrdering",MOrderingKind.class,MOrderingKind.SORTED,null));

    orderingGroup.add(sorted);
    orderingPanel.add(sorted);
    addField(orderingPanel,1,1,0);

    addCaption("Aggregation:",2,1,1);
    JPanel aggregationPanel = new JPanel(new GridLayout(0,1));
    ButtonGroup aggregationGroup = new ButtonGroup();

    UMLRadioButton none = new UMLRadioButton(localize("none"),this,
      new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation",
        "setAggregation",MAggregationKind.class,MAggregationKind.NONE,null),true);

    aggregationGroup.add(none);
    aggregationPanel.add(none);

    UMLRadioButton aggregation = new UMLRadioButton(localize("aggregation"),this,
      new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation",
        "setAggregation",MAggregationKind.class,MAggregationKind.AGGREGATE,null));
    aggregationGroup.add(aggregation);
    aggregationPanel.add(aggregation);

    UMLRadioButton composite = new UMLRadioButton(localize("composite"),this,
      new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation",
        "setAggregation",MAggregationKind.class,MAggregationKind.COMPOSITE,null));

    aggregationGroup.add(composite);
    aggregationPanel.add(composite);
    addField(aggregationPanel,2,1,0);


    addCaption("Scope:",0,2,0);
    addField(new UMLCheckBox(localize("classifier"),this,
      new UMLEnumerationBooleanProperty("targetScope",mclass,"getTargetScope",
        "setTargetScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)),0,2,0);

    addCaption("Changeability:",1,2,0);
    ButtonGroup changeabilityGroup = new ButtonGroup();
    JPanel changeabilityPanel = new JPanel(new GridLayout(0,1));

    UMLRadioButton changeable = new UMLRadioButton(localize("changeable"),this,
      new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability",
        "setChangeability",MChangeableKind.class,MChangeableKind.CHANGEABLE,null), true);

    changeabilityGroup.add(changeable);
    changeabilityPanel.add(changeable);

    UMLRadioButton frozen = new UMLRadioButton(localize("frozen"),this,
      new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability",
        "setChangeability",MChangeableKind.class,MChangeableKind.FROZEN,null));
    changeabilityGroup.add(frozen);
    changeabilityPanel.add(frozen);


    UMLRadioButton addOnly = new UMLRadioButton(localize("add only"),this,
      new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability",
        "setChangeability",MChangeableKind.class,MChangeableKind.ADD_ONLY,null));

    changeabilityGroup.add(addOnly);
    changeabilityPanel.add(addOnly);
    addField(changeabilityPanel,1,2,0);


    addCaption("Visibility:",2,2,1);
    addField(new UMLVisibilityPanel(this,mclass,1,false),2,2,0);
    
    //does this make sense?? new PropPanelButton(this,buttonPanel,_classIcon,localize("New class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateUp",null);
    //does this amke sense?? new PropPanelButton(this,buttonPanel,_interfaceIcon,localize("New interface"),"newInterface",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_assocEndIcon,localize("Go to other end"),"gotoOther",null);

  }

    public void newClass() {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            MAssociationEnd assocEnd = (MAssociationEnd) target;
            MAssociation assoc = assocEnd.getAssociation();
            if(assoc != null) {
                MNamespace ns = assoc.getNamespace();
                if(ns != null) {
                    MClass newClass = ns.getFactory().createClass();
                    ns.addOwnedElement(newClass);
                    assocEnd.setType(newClass);
                    navigateTo(newClass);
                }
            }
        }
    }

    public void newInterface() {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            MAssociationEnd assocEnd = (MAssociationEnd) target;
            MAssociation assoc = assocEnd.getAssociation();
            if(assoc != null) {
                MNamespace ns = assoc.getNamespace();
                if(ns != null) {
                    MInterface newClass = ns.getFactory().createInterface();
                    ns.addOwnedElement(newClass);
                    //
                    //   while we are at it, we should set all the other ends
                    //      to not navigable
                    assocEnd.setType(newClass);
                    java.util.List ends = assoc.getConnections();
                    MAssociationEnd otherEnd;
                    if(ends != null) {
                        Iterator iter = ends.iterator();
                        while(iter.hasNext()) {
                            otherEnd = (MAssociationEnd) iter.next();
                            otherEnd.setNavigable(otherEnd == assocEnd);
                        }
                    }
                    navigateTo(newClass);
                }
            }
        }
    }

    public Object getAssociation() {
        Object assoc = null;
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            assoc = ((MAssociationEnd) target).getAssociation();
        }
        return assoc;
    }



    public MClassifier getType() {
        MClassifier type = null;
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            type = ((MAssociationEnd) target).getType();
        }
        return type;
    }

    public void setType(MClassifier type) {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            ((MAssociationEnd) target).setType(type);
        }
    }

    public boolean isAcceptibleType(MModelElement type) {
        return (type instanceof MClassifier) &&
            !(type instanceof MDataType);
    }

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            MAssociation assoc = ((MAssociationEnd) target).getAssociation();
            if(assoc != null) {
                navigateTo(assoc);
            }
        }
    }

    public void gotoOther() {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            MAssociationEnd end = (MAssociationEnd) target;
            MAssociation assoc = end.getAssociation();
            Collection connections = assoc.getConnections();
            Iterator iter = connections.iterator();
            Object other = null;
            Object item = null;
            //
            //    always go to the one before match or to end
            //
            boolean didNav = false;
            while(iter.hasNext()) {
                item = iter.next();
                if(item == end) {
                   if(other != null) {
                      didNav = true;
                      navigateTo(other);
                      break;
                    }
                }
                else {
                  other = item;
                }
              }
              //
              //   if previous end was the first, then navigate to the last
              if(!didNav) {
                navigateTo(other);
              }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("AssociationEnd");
    }


    protected MNamespace getDisplayNamespace(Object target) {
        MNamespace ns = null;
        if(target instanceof MAssociationEnd) {
             MAssociationEnd end = (MAssociationEnd) target;
             MAssociation assoc = end.getAssociation();
             if(assoc != null) {
                 ns = assoc.getNamespace();
             }
        }
        return ns;
    }



} /* end class PropPanelAssociationEnd */

