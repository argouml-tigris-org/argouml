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

package org.argouml.uml.ui.foundation.core;


import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import org.argouml.uml.ui.*;

import org.tigris.gef.util.Util;
import java.util.*;

abstract public class PropPanelModelElement extends PropPanel {

    ////////////////////////////////////////////////////////////////
    // constants

    protected static ImageIcon _objectIcon = Util.loadIconResource("Object");
    protected static ImageIcon _componentInstanceIcon = Util.loadIconResource("ComponentInstance");
    protected static ImageIcon _nodeInstanceIcon = Util.loadIconResource("NodeInstance");
    protected static ImageIcon _instanceIcon = Util.loadIconResource("Instance");
    protected static ImageIcon _linkIcon = Util.loadIconResource("Link");
    protected static ImageIcon _stimulusIcon = Util.loadIconResource("Stimulus");
    protected static ImageIcon _associationIcon = Util.loadIconResource("Association");
    protected static ImageIcon _assocEndIcon = Util.loadIconResource("AssociationEnd");
    protected static ImageIcon _generalizationIcon = Util.loadIconResource("Generalization");
    protected static ImageIcon _realizationIcon = Util.loadIconResource("Realization");
    protected static ImageIcon _classIcon = Util.loadIconResource("Class");
    protected static ImageIcon _interfaceIcon = Util.loadIconResource("Interface");
    protected static ImageIcon _addOpIcon = Util.loadIconResource("AddOperation");
    protected static ImageIcon _addAttrIcon = Util.loadIconResource("AddAttribute");
    protected static ImageIcon _addAssocIcon = Util.loadIconResource("Association");
    protected static ImageIcon _packageIcon = Util.loadIconResource("Package");
    protected static ImageIcon _innerClassIcon = Util.loadIconResource("InnerClass");
    protected static ImageIcon _nodeIcon = Util.loadIconResource("Node");
    protected static ImageIcon _componentIcon = Util.loadIconResource("Component");
    protected static ImageIcon _dataTypeIcon = Util.loadIconResource("DataType");
    protected static ImageIcon _actorIcon = Util.loadIconResource("Actor");
    protected static ImageIcon _useCaseIcon = Util.loadIconResource("UseCase");
    protected static ImageIcon _dependencyIcon = Util.loadIconResource("Dependency");
    protected static ImageIcon _parameterIcon = Util.loadIconResource("Parameter");
    protected static ImageIcon _operationIcon = Util.loadIconResource("Operation");
    protected static ImageIcon _signalIcon = Util.loadIconResource("SignalSending");
    protected static ImageIcon _stereotypeIcon = Util.loadIconResource("Stereotype");
    protected static ImageIcon _guardIcon = Util.loadIconResource("Guard");
    protected static ImageIcon _transitionIcon = Util.loadIconResource("Transition");
    protected static ImageIcon _classifierRoleIcon = Util.loadIconResource("ClassifierRole");
    protected static ImageIcon _associationRoleIcon = Util.loadIconResource("AssociationRole");
    protected static ImageIcon _callActionIcon = Util.loadIconResource("CallAction");

    private static ResourceBundle _umlBundle = null;

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

    /**
     * Loads the resource bundle for all UML related PropPanel's
     * if not already loaded.
     */
    public ResourceBundle getResourceBundle() {
        if(_umlBundle == null) {
            _umlBundle =
                ResourceBundle.getBundle
                    ("org.argouml.uml.ui.UMLResourceBundle",Locale.getDefault());
        }
        return _umlBundle;
    }

}
