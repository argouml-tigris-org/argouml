// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.swingext.GridLayout2;
import org.argouml.swingext.Orientation;
import org.argouml.swingext.GridLayout2;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramGeneralizationEdge;
import org.argouml.uml.ui.ActionAddOperation;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.uml.ui.UMLAttributesListModel;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLClassifiersListModel;
import org.argouml.uml.ui.UMLClientDependencyListModel;
import org.argouml.uml.ui.UMLConnectionListModel;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLGeneralizationListModel;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLModelElementListModel;
import org.argouml.uml.ui.UMLOperationsListModel;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.UMLSpecializationListModel;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

abstract public class PropPanelClassifier extends PropPanelNamespace {

    protected JPanel _modifiersPanel;
    protected JScrollPane extendsScroll;
    protected JScrollPane implementsScroll;
    protected JScrollPane derivedScroll;
    protected JScrollPane opsScroll;
    protected JScrollPane attrScroll;
    protected JScrollPane connectScroll;
    protected JScrollPane innerScroll;
    
    private UMLReflectionListModel _implementsModel = null;
    private UMLReflectionListModel _connectModel = null;

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClassifier(String name, int columns) {
      this(name, null, columns);
  }
  
  public PropPanelClassifier(String title, Orientation orientation) {
  	super(title, orientation);
  	initialize();
  }

  public PropPanelClassifier(String name, ImageIcon icon, int columns) {
      super(name,icon,columns);
      initialize();
  }


    public void addOperation() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MOperation newOper = UmlFactory.getFactory().getCore().buildOperation((MClassifier)target);
            navigateTo(newOper);
        }
    }

    public void addAttribute() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
	    MClassifier cls = (MClassifier) target;
	    MAttribute attr = UmlFactory.getFactory().getCore().buildAttribute(cls);
	      navigateTo(attr);
        }
    }


    /**
	 * Opens a new window where existing interfaces can be added to the class as interfaces.
	 * realized by this class.
	 * If the interface and the class are on the same diagram, a figrealization is drawn.
	 * @param index
	 */
    public void addRealization(Integer index) {
    	Object target = getTarget();
    	if (target instanceof MClassifier) {
    		MClassifier clazz = (MClassifier)target;	
	    	Vector choices = new Vector();
	    	Vector selected = new Vector();
	    	choices.addAll(CoreHelper.getHelper().getAllInterfaces());
	    	selected.addAll(CoreHelper.getHelper().getRealizedInterfaces(clazz));
	    	UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-realized-interfaces"), true, true);
	    	int returnValue = dialog.showDialog(ProjectBrowser.TheInstance);
	    	if (returnValue == JOptionPane.OK_OPTION) {
	    		Iterator it = dialog.getSelected().iterator();
	    		while (it.hasNext()) {
	    			MInterface interf = (MInterface)it.next();
	    			if (!selected.contains(interf)) {
	    				ProjectBrowser pb = ProjectBrowser.TheInstance;
	    				ArgoDiagram diagram = pb.getActiveDiagram();
	    				Fig figclass = diagram.getLayer().presentationFor(clazz);
	    				Fig figinterface = diagram.getLayer().presentationFor(interf);
	    				if (figclass != null && figinterface != null) {
	    					GraphModel gm = diagram.getGraphModel();
	    					if (gm instanceof MutableGraphModel) {
	    						((MutableGraphModel)gm).connect(clazz, interf, MAbstraction.class);
	    					}
	    				} else {
	    					CoreFactory.getFactory().buildRealization(clazz, interf);
	    				}
	    			}
	    		}
	    		it = selected.iterator();
	    		while (it.hasNext()) {
	    			MInterface interf = (MInterface)it.next();
	    			if (!dialog.getSelected().contains(interf)) {
	    				MAbstraction realization = CoreHelper.getHelper().getRealization(interf, clazz);
			    		Object pt = ProjectBrowser.TheInstance.getTarget();
			    		ProjectBrowser.TheInstance.setTarget(realization);
			    		ActionEvent event = new ActionEvent(this, 1, "delete");
			    		ActionRemoveFromModel.SINGLETON.actionPerformed(event);
			    		ProjectBrowser.TheInstance.setTarget(pt);
	    			}
	    		}
	    		// have to do it like this since propertyset for the elements in the model is not called
	    		_implementsModel.resetSize();	
	    	}
    	}
    }
    
	/**
	 * Returns all interfaces this class realizes.
	 * @return Collection
	 */
    public Collection getRealizations() {
    	Object target = getTarget();
    	if (target instanceof MClassifier) {
    		return CoreHelper.getHelper().getRealizedInterfaces((MClassifier)target);
    	}
    	return new Vector();
    }
    
	/**
	 * Deletes the realization between this class and an interface.
	 * @param index
	 */
    public void deleteRealization(Integer index) {
    	Object target = getTarget();
    	if (target instanceof MClassifier) {
    		MClassifier clazz = (MClassifier)target;
    		MInterface interf = (MInterface)UMLModelElementListModel.elementAtUtil(CoreHelper.getHelper().getRealizedInterfaces(clazz), index.intValue(), null);
    		MAbstraction abstraction = CoreHelper.getHelper().getRealization(interf, clazz);
    		if (abstraction.getClients().size() == 1 && abstraction.getSuppliers().size() == 1) {
    			Object pt = ProjectBrowser.TheInstance.getTarget();
    			ProjectBrowser.TheInstance.setTarget(abstraction);
    			ActionEvent event = new ActionEvent(this, 1, "delete");
    			ActionRemoveFromModel.SINGLETON.actionPerformed(event);
    			ProjectBrowser.TheInstance.setTarget(pt);
    			
    		} else {
    			interf.removeSupplierDependency(abstraction);
    			clazz.removeClientDependency(abstraction);
    		}
    	}
    }
    
    
   
   
    
    private void initialize() {
    	Class mclass = MClassifier.class;

      //
      //   this will cause the components on this page to be notified
      //      anytime a stereotype, namespace, operation, etc
      //      has its name changed or is removed anywhere in the model
      Class[] namesToWatch = { MStereotype.class,MNamespace.class,MOperation.class,
			       MParameter.class,MAttribute.class,MAssociation.class,MClassifier.class };
      setNameEventListening(namesToWatch);
		
	  UMLGeneralizationListModel _extendsModel = new UMLGeneralizationListModel(this, "generalization", true);
      // JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
      JList extendsList = new UMLList(_extendsModel,true);
      extendsList.setBackground(getBackground());
      extendsList.setForeground(Color.blue);
      extendsList.setVisibleRowCount(3);
      extendsScroll= new JScrollPane(extendsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		_implementsModel = new UMLReflectionListModel(this,"realizations",true,"getRealizations",null,"addRealization","deleteRealization");
	  	JList implementsList = new UMLList(_implementsModel,true);
 		implementsList.setBackground(getBackground());
        implementsList.setForeground(Color.blue);
        implementsList.setVisibleRowCount(3);
        implementsScroll=new JScrollPane(implementsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      /*
      JList implementsList = new UMLList(new UMLClientDependencyListModel(this,"implements",true),true);
      implementsList.setBackground(getBackground());
      implementsList.setForeground(Color.blue);
      implementsList.setVisibleRowCount(3);
      implementsScroll= new JScrollPane(implementsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	  */
	  
      _modifiersPanel = new JPanel(new GridLayout2(0, 2, GridLayout2.ROWCOLPREFERRED));

      _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.visibility.public-uc"),this,new UMLEnumerationBooleanProperty("visibility",mclass,"getVisibility","setVisibility",MVisibilityKind.class,MVisibilityKind.PUBLIC,null)));
      _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-uc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
      _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-uc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
      _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.root-uc"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));

	  UMLSpecializationListModel _derivedModel = new UMLSpecializationListModel(this,"specialization",true);
      JList derivedList = new UMLList(_derivedModel,true);
      derivedList.setForeground(Color.blue);
      derivedList.setVisibleRowCount(3);
      derivedScroll=new JScrollPane(derivedList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);      

      JList opsList = new UMLList(new UMLOperationsListModel(this,"feature",true),true);
      opsList.setForeground(Color.blue);
      opsList.setVisibleRowCount(3);
      opsScroll = new JScrollPane(opsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
      attrList.setForeground(Color.blue);
      attrList.setVisibleRowCount(3);
      attrScroll= new JScrollPane(attrList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	  // _connectModel = new UMLReflectionListModel(this,"associations",true,"getAssociations",null,"addAssociation","deleteAssociation");
      // TODO implement the methods for _connectModel.This is waiting on some advice
      JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
      connectList.setForeground(Color.blue);
      connectList.setVisibleRowCount(3);
      connectScroll= new JScrollPane(connectList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      JList innerList = new UMLList(new UMLClassifiersListModel(this,"ownedElement",true),true);
      innerList.setForeground(Color.blue);
      innerList.setVisibleRowCount(3);
      innerScroll= new JScrollPane(innerList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    } 
    
     public void addDataType() {
        Object target = getTarget();
        if(target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = CoreFactory.getFactory().createDataType();
            ns.addOwnedElement(ownedElem);
            navigateTo(ownedElem);
        }
    }

    
	

} /* end class PropPanelClassifier */
