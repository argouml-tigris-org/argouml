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

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.model.uml.AbstractWellformednessRule;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.AssociationEndAggregationWellformednessRule;
import org.argouml.model.uml.foundation.core.AssociationEndNamespaceWellformednessRule;
import org.argouml.swingext.LabelledLayout;
import org.argouml.swingext.Orientation;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLComboBox;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLMultiplicityComboBox;
import org.argouml.uml.ui.UMLRadioButton;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.UMLVisibilityPanel;
import org.argouml.util.ConfigLoader;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;


public class PropPanelAssociationEnd extends PropPanelModelElement {

    protected JLabel associationsLabel;

    public PropPanelAssociationEnd(String name, ImageIcon icon, Orientation orientation) {
        super(name, icon, orientation);
    }
    
    public PropPanelAssociationEnd() {
        super("AssociationEnd",_assocEndIcon, ConfigLoader.getTabPropsOrientation());
        Class mclass = MAssociationEnd.class;
      
      //   this will cause the components on this page to be notified
      //      anytime a stereotype, namespace, operation, etc
      //      has its name changed or is removed anywhere in the model
      Class[] namesToWatch = { MStereotype.class,MNamespace.class, MAssociation.class,MClassifier.class };
        setNameEventListening(namesToWatch);
      makeFields(mclass);
    }

  protected void makeFields(Class mclass) {

    addField(Argo.localize("UMLMenu", "label.name"), nameField);
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));

    UMLComboBoxModel model = new UMLComboBoxModel(this,"isAcceptibleType",
        "type","getType","setType",false,MClassifier.class,true);
    UMLComboBox box = new UMLComboBox(model);
    box.setToolTipText("Warning: Do not use this to change an end that is already in a diagram.");
    addField(Argo.localize("UMLMenu", "label.type"),new UMLComboBox2(new UMLAssociationEndTypeComboBoxModel(), ActionSetAssociationEndType.SINGLETON));

    addField(Argo.localize("UMLMenu", "label.multiplicity"),new UMLMultiplicityComboBox(this,mclass));

    JList associationList = new UMLList(new UMLReflectionListModel(this,"association",false,"getAssociation",null,null,null),true);
    associationList.setBackground(getBackground());
    associationList.setForeground(Color.blue);
    associationList.setVisibleRowCount(1);
    associationsLabel = addField(Argo.localize("UMLMenu", "label.association"),new JScrollPane(associationList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    add(LabelledLayout.getSeperator());
    
    addField("Navigable:", new UMLCheckBox(localize("navigable"),this,
      new UMLReflectionBooleanProperty("navigable",mclass,
        "isNavigable","setNavigable")));

    // Build order radio buttons
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

    UMLRadioButton sorted = new UMLRadioButton(Argo.localize("UMLMenu", "button.sorted"),this,
      new UMLEnumerationBooleanProperty("ordering",mclass,"getOrdering",
      "setOrdering",MOrderingKind.class,MOrderingKind.SORTED,null));

    orderingGroup.add(sorted);
    orderingPanel.add(sorted);
    addField(Argo.localize("UMLMenu", "label.ordering"), orderingPanel);

    AbstractWellformednessRule[] wellformednessrules = new AbstractWellformednessRule[] {new AssociationEndAggregationWellformednessRule()};
   
    // Build aggregation radio buttons
    JPanel aggregationPanel = new JPanel(new GridLayout(0,1));
    ButtonGroup aggregationGroup = new ButtonGroup();

    UMLRadioButton none = new UMLRadioButton(localize("none"),this,
      new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation",
        "setAggregation",MAggregationKind.class,MAggregationKind.NONE,null,wellformednessrules),true);

    aggregationGroup.add(none);
    aggregationPanel.add(none);

    UMLRadioButton aggregation = new UMLRadioButton(localize("aggregation"),this,
      new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation",
        "setAggregation",MAggregationKind.class,MAggregationKind.AGGREGATE,null, wellformednessrules));
    aggregationGroup.add(aggregation);
    aggregationPanel.add(aggregation);

    UMLRadioButton composite = new UMLRadioButton(localize("composite"),this,
      new UMLEnumerationBooleanProperty("aggregation",mclass,"getAggregation",
        "setAggregation",MAggregationKind.class,MAggregationKind.COMPOSITE,null, wellformednessrules));
    aggregationGroup.add(composite);
    aggregationPanel.add(composite);
    addField("Aggregation:",aggregationPanel);

    add(LabelledLayout.getSeperator());
    
    //
    addField("Scope:",new UMLCheckBox(localize("classifier"),this,
      new UMLEnumerationBooleanProperty("targetScope",mclass,"getTargetScope",
        "setTargetScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)));

    // Build changability radio buttons
    ButtonGroup changeabilityGroup = new ButtonGroup();
    JPanel changeabilityPanel = new JPanel(new GridLayout(0,1));

    UMLRadioButton changeable = new UMLRadioButton(Argo.localize("UMLMenu", "radiobutton.changeable"),this,
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
    addField("Changeability:", changeabilityPanel);

    //
    addField(Argo.localize("UMLMenu", "label.visibility"),new UMLVisibilityPanel(this,mclass,1,false));

    //does this make sense?? new PropPanelButton(this,buttonPanel,_classIcon, Argo.localize("UMLMenu", "button.add-new-class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
    //does this make sense?? new PropPanelButton(this,buttonPanel,_interfaceIcon, Argo.localize("UMLMenu", "button.add-new-interface"),"newInterface",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_assocEndIcon,localize("Go to other end"),"gotoOther",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-association-end"), "removeElement", null);

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
    	boolean wellformed = false;
    	if (type instanceof MClassifier) {
    		wellformed = new AssociationEndNamespaceWellformednessRule().isWellformed((MAssociationEnd)getTarget(), ((MClassifier)type).getNamespace());
    	}
        return (type instanceof MClassifier) &&
            !(type instanceof MDataType) && wellformed;
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
            while(iter.hasNext()) {
                item = iter.next();
                if(item == end) {
                    if(other != null) {
                        navigateTo(other);
                        return;
                    }
                }
                else {
                    other = item;
                }
            }
            //
            //   if previous end was the first, then navigate to the last
            navigateTo(other);
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
    
	/**
	 * Deletes the association end. If the associationend's owner (the association)
	 * has one associationend left after this delete, it is also deleted.
	 */
    public void removeElement() {
    	MAssociationEnd end = (MAssociationEnd)getTarget();
    	MAssociation assoc = end.getAssociation();
    	Collection ends = assoc.getConnections();
    	if (ends.size() > 2) {   		
    		Iterator it = Project.getCurrentProject().findFigsForMember(end.getAssociation()).iterator();
    		while (it.hasNext()) {
    			// should do here something if the association end is shown
    		}
    		UmlFactory.getFactory().delete(end);
    		navigateTo(assoc);
    	} else {
    		ProjectBrowser.TheInstance.setDetailsTarget(assoc);
    		ActionRemoveFromModel.SINGLETON.actionPerformed(new ActionEvent(this, 0, null));
    	}
    		
    }
    
    


} /* end class PropPanelAssociationEnd */

