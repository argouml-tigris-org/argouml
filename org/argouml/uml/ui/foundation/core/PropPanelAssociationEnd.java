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


import org.tigris.gef.util.Util;


public class PropPanelAssociationEnd extends PropPanel {
    
    protected static ImageIcon _classIcon = Util.loadIconResource("Class");
    protected static ImageIcon _interfaceIcon = Util.loadIconResource("Interface");
    protected static ImageIcon _assocEndIcon = Util.loadIconResource("AssociationEnd");
    

  ////////////////////////////////////////////////////////////////
  // constants
  ////////////////////////////////////////////////////////////////

  // contructors
  public PropPanelAssociationEnd() {
    super("AssociationEnd Properties",3);

    Class mclass = MAssociationEnd.class;

    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    addCaption(new JLabel("Stereotype:"),1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,1,0,0);


    addCaption(new JLabel("Type:"),2,0,0);
    addField(new UMLClassifierComboBox(this,MClassifier.class,MDataType.class,"type","getType","setType",false),2,0,0);

    addCaption(new JLabel("Multiplicity:"),3,0,0);
    addField(new UMLMultiplicityComboBox(this,MAssociationEnd.class),3,0,0);
    
    addCaption(new JLabel("Association:"),4,0,1);
    JList namespaceList = new UMLList(new UMLReflectionListModel(this,"association",false,"getAssociation",null,null,null),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,4,0,0);
    
    addCaption(new JLabel("Navigable:"),0,1,0);
    addField(new UMLCheckBox("navigable",this,new UMLReflectionBooleanProperty("navigable",mclass,"isNavigable","setNavigable")),0,1,0);
    

    addCaption(new JLabel("Ordering:"),1,1,0);
    JPanel orderingPanel = new JPanel(new GridLayout(0,1));
    ButtonGroup orderingGroup = new ButtonGroup();
    UMLRadioButton unordered = new UMLRadioButton("unordered",this,new UMLEnumerationBooleanProperty("ordering",mclass,"getOrdering","setOrdering",MOrderingKind.class,MOrderingKind.UNORDERED,null));
    orderingGroup.add(unordered);
    orderingPanel.add(unordered);

    
    UMLRadioButton ordered = new UMLRadioButton("ordered",this,new UMLEnumerationBooleanProperty("ordering",mclass,"getOrdering","setOrdering",MOrderingKind.class,MOrderingKind.ORDERED,null));
    orderingGroup.add(ordered);
    orderingPanel.add(ordered);

    UMLRadioButton sorted = new UMLRadioButton("sorted",this,new UMLEnumerationBooleanProperty("ordering",mclass,"getOrdering","setOrdering",MOrderingKind.class,MOrderingKind.SORTED,null));
    orderingGroup.add(sorted);
    orderingPanel.add(sorted);
    addField(orderingPanel,1,1,0);

    addCaption(new JLabel("Aggregation:"),2,1,1);
    JPanel aggregationPanel = new JPanel(new GridLayout(0,1));
    ButtonGroup aggregationGroup = new ButtonGroup();
    UMLRadioButton none = new UMLRadioButton("none",this,new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation","setAggregation",MAggregationKind.class,MAggregationKind.NONE,null));
    aggregationGroup.add(none);
    aggregationPanel.add(none);

    UMLRadioButton aggregation = new UMLRadioButton("aggregation",this,new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation","setAggregation",MAggregationKind.class,MAggregationKind.AGGREGATE,null));
    aggregationGroup.add(aggregation);
    aggregationPanel.add(aggregation);

    UMLRadioButton composite = new UMLRadioButton("composite",this,new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation","setAggregation",MAggregationKind.class,MAggregationKind.COMPOSITE,null));
    aggregationGroup.add(composite);
    aggregationPanel.add(composite);
    addField(aggregationPanel,2,1,0);
    
    
    addCaption(new JLabel("Scope:"),0,2,0);
    addField(new UMLCheckBox("classifier",this,new UMLEnumerationBooleanProperty("targetScope",mclass,"getTargetScope","setTargetScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)),0,2,0);

    addCaption(new JLabel("Changeability:"),1,2,0);
    ButtonGroup changeabilityGroup = new ButtonGroup();
    JPanel changeabilityPanel = new JPanel(new GridLayout(0,1));

    UMLRadioButton changeable = new UMLRadioButton("changeable",this,new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability","setChangeability",MChangeableKind.class,MChangeableKind.CHANGEABLE,null));
    changeabilityGroup.add(changeable);
    changeabilityPanel.add(changeable);

    UMLRadioButton frozen = new UMLRadioButton("frozen",this,new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability","setChangeability",MChangeableKind.class,MChangeableKind.FROZEN,null));
    changeabilityGroup.add(frozen);
    changeabilityPanel.add(frozen);

    
    UMLRadioButton addOnly = new UMLRadioButton("add only",this,new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability","setChangeability",MChangeableKind.class,MChangeableKind.ADD_ONLY,null));
    changeabilityGroup.add(addOnly);
    changeabilityPanel.add(addOnly);
    addField(changeabilityPanel,1,2,0);
    
    
    addCaption(new JLabel("Visibility:"),2,2,1);
    addField(new UMLVisibilityPanel(this,mclass,1,false),2,2,0);

    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);
    
    
    new PropPanelButton(this,buttonPanel,_classIcon,"New class","newClass",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,"Go up","navigateUp",null);
    new PropPanelButton(this,buttonPanel,_interfaceIcon,"New interface","newInterface",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_interfaceIcon,"Delete association end","removeElement",null);
    new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_assocEndIcon,"New association end","newAssociationEnd",null);
    
    
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

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            MAssociation assoc = ((MAssociationEnd) target).getAssociation();
            if(assoc != null) {
                navigateTo(assoc);
            }
        }
    }
    
    public void newAssociationEnd() {
        Object target = getTarget();
        if(target instanceof MAssociationEnd) {
            MAssociation assoc = ((MAssociationEnd) target).getAssociation();
            if(assoc != null) {
                MAssociationEnd newEnd = assoc.getFactory().createAssociationEnd();
                assoc.addConnection(newEnd);
                navigateTo(newEnd);
            }
        }
    }

} /* end class PropPanelAssociation */

