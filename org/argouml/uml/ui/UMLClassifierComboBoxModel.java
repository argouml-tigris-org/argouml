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

package org.argouml.uml.ui;
import org.argouml.uml.*;
import javax.swing.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import ru.novosoft.uml.*;
import java.awt.event.*;
import java.util.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

public class UMLClassifierComboBoxModel extends AbstractListModel implements ComboBoxModel, UMLUserInterfaceComponent {

    private UMLUserInterfaceContainer _container;
    private String _property;
    private Class _itemClass;
    private Class _excludeClass;
    private Method _getMethod;
    private Method _setMethod;
    private Set _classifiers;
    private static final Object[] _getArgs = {};
    private boolean _showVoid;
    private ArrayList _comboList;
    private boolean _shouldBeEnabled;
    private Object _selectedItem;
    
    /** Creates new BooleanChangeListener */
    public UMLClassifierComboBoxModel(UMLUserInterfaceContainer container,Class itemClass,Class excludeClass,String property,String getMethod,String setMethod,boolean showVoid) {
        super();
        _container = container;
        _property = property;
        _itemClass = itemClass;
        _excludeClass = excludeClass;
        _showVoid = showVoid;
        _shouldBeEnabled = true;
        
        if(getMethod != null) {
            Class[] getArgs = {};
            try {
                _getMethod = container.getClass().getMethod(getMethod,getArgs);
            }
            catch(Exception e) {
                _shouldBeEnabled = false;
                System.out.println(getMethod + " not found in UMLClassifierComboBox(): " + e.toString());
            }
        }
        
        if(setMethod != null) {
            Class[] setArgs = { itemClass };
            try {
                _setMethod = container.getClass().getMethod(setMethod,setArgs);
            }
            catch(Exception e) {
                _shouldBeEnabled = false;
                System.out.println(setMethod + " not found in UMLClassifierComboBox(): " + e.toString());
            }
        }
    }

    public void targetChanged() {
        _classifiers = null;
        _selectedItem = null;
    }

    public void targetReasserted() {
    }
    
    public void roleAdded(final MElementEvent event) {
        String eventName = event.getName();
        if(eventName != null && eventName.equals("ownedElement")) {
            if(isAcceptible(event.getAddedValue())) {
                _classifiers = null;
                _selectedItem = null;
            }
        }
    }
    
    
    public void recovered(final MElementEvent p1) {
    }
    public void roleRemoved(final MElementEvent p1) {
    }
    public void listRoleItemSet(final MElementEvent p1) {
    }
    
    public void removed(final MElementEvent event) {
        Object source = event.getSource();
        if(isAcceptible(source)) {
            _classifiers = null;
            _comboList = null;
            _selectedItem = null;
        }
    }

    public void propertySet(final MElementEvent event) {
        String eventName = event.getName();        
        if(eventName != null && eventName.equals(_property)) {
            _selectedItem = null;
        }
    }
    
    
    private void update() {
        int oldSize = 0;
        if(_comboList != null) {
            oldSize = _comboList.size();
        }
        if(_classifiers == null) {
            _classifiers = new TreeSet(new UMLClassifierNameComparator());
        }
        else {
            _classifiers.clear();
        }

        Object target = _container.getTarget();
        if(target instanceof MModelElement) {
            MModelElement element = (MModelElement) target;
            MModel model = element.getModel();
            
            if(model == null) {
                MNamespace ns = element.getNamespace();
                if(ns != null) {
                    model = ns.getModel();
                }
            }
            
            //
            //   second chance, since attributes do not seem to return model
            //
            if(model == null) {
                if(element instanceof MFeature) {
                    MClassifier classifier = ((MFeature) element).getOwner();
                    if(classifier != null) {
                        model = classifier.getModel();
                    }
                }
            }
            
            if(model == null && element instanceof MAssociationEnd) {
                MAssociationEnd end = (MAssociationEnd) element;
                MClassifier type = end.getType();
                if(type != null) {
                    model = type.getModel();
                }
                else {
                    MAssociation assoc = end.getAssociation();
                    if(assoc != null) {
                        MNamespace ns = assoc.getNamespace();
                        if(ns != null) {
                            model = ns.getModel();
                        }
                    }
                }
            }
            
            if(model != null) {
                collectClassifiers(model,_classifiers);
                
                Profile profile = _container.getProfile();
                profile.addBuiltinClassifiers(model,_itemClass,_excludeClass,_classifiers,_showVoid);

                //
                //   copy classifiers to Vector
                //       only expand names that collide
                Iterator iter = _classifiers.iterator();
                _comboList = new ArrayList(_classifiers.size());
                String prevName = null;
                String currentName = null;
                Object current = null;
                Object prev = null;
                boolean wasMatch = false;
                if(iter.hasNext()) {
                    current = iter.next();
                    currentName = getName(current);
                }
                while(iter.hasNext()) {
                    prev = current;
                    prevName = currentName;
                    current = iter.next();
                    currentName = getName(current);
                    if(wasMatch || prevName.equals(currentName)) {
                        _comboList.add(getFullName(prev,profile));
                        if(wasMatch) {
                            wasMatch = prevName.equals(currentName);
                        }
                        else {
                            wasMatch = true;
                        }
                    }
                    else {
                        _comboList.add(prevName);
                    }
                }
                if(currentName.equals(prevName)) {
                    _comboList.add(getFullName(current,profile));
                }
                else {
                    _comboList.add(currentName);
                }

                Object currentValue = null;
                try {
                    currentValue = _getMethod.invoke(_container,_getArgs);
                }
                catch(Exception e) {
                    System.out.println(e.toString() + " in UMLClassifierComboBox.update");
                }
                if(currentValue == null) {
                    ProfileClassifier defaultClassifier = null;
                    if(_showVoid) {
                        defaultClassifier = profile.getVoidClassifier();
                    }
                    else {
                        defaultClassifier = profile.getDefaultClassifier();
                    }
                    if(defaultClassifier != null) {
                        currentValue = defaultClassifier.getClassifier(model);
                        if(currentValue == null) {
                            currentValue = defaultClassifier;
                        }
                    }
                }
            }
        }
        int newSize = 0;
        if(_comboList != null) {
            newSize = _comboList.size();
        }
        if(newSize > oldSize) {
            fireContentsChanged(this,0,newSize);
        }
        else {
            fireContentsChanged(this,0,oldSize);
        }
    }
    
    private String getName(Object obj) {
        String name = null;
        if(obj instanceof MModelElement) {
            name = ((MModelElement) obj).getName();
        }
        if(name == null) {
            name = obj.toString();
        }
        return name;
    }

    private String getFullName(Object obj,Profile profile) {
        String fullName = null;
        if(obj instanceof MClassifier) {
            fullName = profile.formatElement((MClassifier) obj,null);
        }
        else {
            if(obj instanceof ProfileClassifier) {
                fullName = ((ProfileClassifier) obj).getFullName();
            }
            else {
                fullName = obj.toString();
            }
        }
        return fullName;
    }
            
    
    private void collectClassifiers(MNamespace ns,Set itemSet) {
        Collection ownedElements = ns.getOwnedElements();
        if(ownedElements != null) {
            Iterator iter = ownedElements.iterator();
            Object obj = null;
            while(iter.hasNext()) {
                obj = iter.next();
                if(_itemClass.isInstance(obj)) {
                    if(_excludeClass == null || !_excludeClass.isInstance(obj)) {
                        itemSet.add(obj);
                    }
                }
                if(obj instanceof MNamespace) {
                    collectClassifiers((MNamespace) obj,itemSet);
                }
            }
        }
    }
    
    public void setSelectedItem(final java.lang.Object selection) {
        if(_classifiers == null) {
            update();
        }
        if(_comboList != null) {
            int index = _comboList.indexOf(selection);
            if(index >= 0) {
                Iterator iter = _classifiers.iterator();
                Object obj = null;
                for(int i = 0; iter.hasNext(); i++) {
                    obj = iter.next();
                    if(i == index) {
                        MClassifier classifier = null;
                        if(obj instanceof MClassifier) {
                            classifier = (MClassifier) obj;
                        }
                        else {
                            if(obj instanceof ProfileClassifier) {
                                Object target = _container.getTarget();
                                MModel model = null;
                                if(target instanceof MModelElement) {
                                    model = ((MModelElement) target).getModel();
                                }
                                if(model == null && target instanceof MFeature) {
                                    MClassifier owner = ((MFeature) target).getOwner();
                                    if(owner != null) model = owner.getModel();
                                }
                                classifier = ((ProfileClassifier) obj).createClassifier(model);
                                _classifiers = null;
                            }       
                        }
                        try {
                            _setMethod.invoke(_container,new Object[] { classifier });
                        }
                        catch(Exception e) {
                            System.out.println(e.toString() + " in UMLClassifierComboBox.actionPerformed()");
                        }
                        break;
                    }
                }
            }
        }
    }
    
    
    public java.lang.Object getSelectedItem() {
        if(_classifiers == null) {
            _selectedItem = null;
            update();
        }
        if(_selectedItem == null && _comboList != null) {
            if(_getMethod != null) {
                try {
                    Object classifier = _getMethod.invoke(_container,_getArgs);
                    if(classifier != null) {
                        int index = 0;
                        Iterator iter = _classifiers.iterator();
                        while(iter.hasNext()) {
                            if(classifier == iter.next()) break;
                            index++;
                        }
                        if(index >= 0 && index < _comboList.size()) {
                            _selectedItem = _comboList.get(index);
                        }
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                    System.out.println(e.toString() + " UMLClassiferComboBoxModel.getSelectedItem()");
                }
            }
        }
        return _selectedItem;
    }
    
    
    public void removeListDataListener(final javax.swing.event.ListDataListener p1) {
    }
    
    public int getSize() {
        if(_classifiers == null) {
            update();
        }
        int size = 0;
        if(_comboList != null) {
            size = _comboList.size();
        }
        return size;
    }
    
    
    public void addListDataListener(final javax.swing.event.ListDataListener p1) {
    }
    
    
    public java.lang.Object getElementAt(int index) {
        if(_classifiers == null) {
            update();
        }
        Object element = null;
        if(_comboList == null) {
            update();
        }
        if(_comboList != null) {
            if(index >= 0 && index < _comboList.size()) {
                element = _comboList.get(index);
            }
        }
        return element;
    }
    
    private boolean isAcceptible(Object obj) {
        boolean acceptible = false;
        if(_itemClass.isInstance(obj)) {
            if(_excludeClass == null || _excludeClass.isInstance(obj)) {
                acceptible = true;
            }
        }
        return acceptible;
    }
            
}