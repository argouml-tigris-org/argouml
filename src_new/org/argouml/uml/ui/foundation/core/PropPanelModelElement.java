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

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Icons for extend and
// include relationships added.


package org.argouml.uml.ui.foundation.core;


import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import org.argouml.application.api.*;
import org.argouml.uml.ui.*;

import org.tigris.gef.util.*;
import java.util.*;

abstract public class PropPanelModelElement extends PropPanel {

    ////////////////////////////////////////////////////////////////
    // constants

    protected static ImageIcon _objectIcon = ResourceLoader.lookupIconResource("Object");
    protected static ImageIcon _componentInstanceIcon = ResourceLoader.lookupIconResource("ComponentInstance");
    protected static ImageIcon _nodeInstanceIcon = ResourceLoader.lookupIconResource("NodeInstance");
    protected static ImageIcon _instanceIcon = ResourceLoader.lookupIconResource("Instance");
    protected static ImageIcon _linkIcon = ResourceLoader.lookupIconResource("Link");
    protected static ImageIcon _stimulusIcon = ResourceLoader.lookupIconResource("Stimulus");
    protected static ImageIcon _associationIcon = ResourceLoader.lookupIconResource("Association");
    protected static ImageIcon _assocEndIcon = ResourceLoader.lookupIconResource("AssociationEnd");
    protected static ImageIcon _generalizationIcon = ResourceLoader.lookupIconResource("Generalization");
    protected static ImageIcon _realizationIcon = ResourceLoader.lookupIconResource("Realization");
    protected static ImageIcon _classIcon = ResourceLoader.lookupIconResource("Class");
    protected static ImageIcon _interfaceIcon = ResourceLoader.lookupIconResource("Interface");
    protected static ImageIcon _addOpIcon = ResourceLoader.lookupIconResource("AddOperation");
    protected static ImageIcon _addAttrIcon = ResourceLoader.lookupIconResource("AddAttribute");
    protected static ImageIcon _addAssocIcon = ResourceLoader.lookupIconResource("Association");
    protected static ImageIcon _packageIcon = ResourceLoader.lookupIconResource("Package");
    protected static ImageIcon _modelIcon = ResourceLoader.lookupIconResource("Model");
    protected static ImageIcon _innerClassIcon = ResourceLoader.lookupIconResource("InnerClass");
    protected static ImageIcon _nodeIcon = ResourceLoader.lookupIconResource("Node");
    protected static ImageIcon _componentIcon = ResourceLoader.lookupIconResource("Component");
    protected static ImageIcon _dataTypeIcon = ResourceLoader.lookupIconResource("DataType");
    protected static ImageIcon _actorIcon = ResourceLoader.lookupIconResource("Actor");
    protected static ImageIcon _useCaseIcon = ResourceLoader.lookupIconResource("UseCase");
    protected static ImageIcon _extendIcon = ResourceLoader.lookupIconResource("Extend");
    protected static ImageIcon _extensionPointIcon = ResourceLoader.lookupIconResource("ExtensionPoint");
    protected static ImageIcon _includeIcon = ResourceLoader.lookupIconResource("Include");
    protected static ImageIcon _dependencyIcon = ResourceLoader.lookupIconResource("Dependency");
    protected static ImageIcon _parameterIcon = ResourceLoader.lookupIconResource("Parameter");
    protected static ImageIcon _operationIcon = ResourceLoader.lookupIconResource("Operation");
    protected static ImageIcon _signalIcon = ResourceLoader.lookupIconResource("SignalSending");
    protected static ImageIcon _stereotypeIcon = ResourceLoader.lookupIconResource("Stereotype");
    protected static ImageIcon _guardIcon = ResourceLoader.lookupIconResource("Guard");
    protected static ImageIcon _transitionIcon = ResourceLoader.lookupIconResource("Transition");
    protected static ImageIcon _classifierRoleIcon = ResourceLoader.lookupIconResource("ClassifierRole");
    protected static ImageIcon _associationRoleIcon = ResourceLoader.lookupIconResource("AssociationRole");
    protected static ImageIcon _callActionIcon = ResourceLoader.lookupIconResource("CallAction");

    protected JList namespaceList;
    protected JScrollPane namespaceScroll;
    protected UMLTextField nameField;
    protected JComboBox stereotypeBox;
    ////////////////////////////////////////////////////////////////
    // constructors
    public PropPanelModelElement(String name, int columns) {
        this(name,null,columns);
    }

    public PropPanelModelElement(String name, ImageIcon icon, int columns) {
        super(name,icon,columns);

        Class mclass = MModelElement.class;

        nameField=new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName"));

        stereotypeBox = new UMLStereotypeComboBox(this);

        namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
        namespaceList.setBackground(getBackground());
        namespaceList.setForeground(Color.blue);
        
	namespaceScroll = new JScrollPane(namespaceList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MNamespace namespace = ((MModelElement) target).getNamespace();
            if(namespace != null) {
                navigateTo(namespace);
            }
        }
    }

    public void addRealization() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement modelElement = (MModelElement) target;
            MNamespace ns = modelElement.getNamespace();
            if(ns != null) {
//                MGeneralization newGen = ns.getFactory().createGeneralization();
//                if(newGen != null) {
//                    newGen.addSpecialization(genElem);
//                    ns.addOwnedElement(newGen);
//                    navigateTo(newGen);
//                }
            }
        }
    }

    public void addDataType() {
        Object target = getTarget();
        if(target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = ns.getFactory().createDataType();
            ns.addOwnedElement(ownedElem);
            navigateTo(ownedElem);
        }
    }

    public void navigateNamespace() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement elem = (MModelElement) target;
            MNamespace ns = elem.getNamespace();
            if(ns != null) {
                navigateTo(ns);
            }
        }
    }

    //
    // Pluggable Property Panel support
    //
    // THIS CLASS MUST NOT IMPLEMENT PluggablePropertyPanel.  These
    // are present to provide default implementations for any
    // property panel that extends this class.
    public PropPanel getPropertyPanel() { return this; } 
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }
    public boolean initializeModule() {
        ArgoModule.cat.debug("initializeModule()");
        return true;
    }
    public void setModuleEnabled(boolean enabled) { }
    public boolean inContext(Object[] o) { return true; }
}
