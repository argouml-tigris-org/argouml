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
import javax.swing.*;
import ru.novosoft.uml.*;
import java.awt.event.*;
import java.awt.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import java.util.*;

/**
 *   This class implements a combo box for stereotypes.
 *   The class polls the model and profile for appropriate
 *   stereotypes for the target object.  A context popup menu
 *   allows for new stereotypes to be created and existing 
 *   stereotypes to be deleted.
 *
 *   @author Curt Arnold
 */
public class UMLStereotypeComboBox extends JComboBox implements UMLUserInterfaceComponent, ItemListener {

    private UMLUserInterfaceContainer _container;
    private static String _noneStereotype = "";
    private Set _stereotypes;
    private static HashMap _metaclasses;
    
    public UMLStereotypeComboBox(UMLUserInterfaceContainer container) {
        super();
        _container = container;
        addItemListener(this);
    }

    public Object getTarget() {
        return _container.getTarget();
    }
        
    public void targetChanged() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement element = (MModelElement) target;
            MModel model = element.getModel();
            if(model == null && element instanceof MFeature) {
                MClassifier owner = ((MFeature) element).getOwner();
                if(owner != null) {
                    model = owner.getModel();
                }
            }
            if(model != null) {
                setModel(updateStereotypes(model));
                updateSelection();
            }
        }
    }

    private void addStereotypes(MNamespace ns,Class metaclass,Collection stereotypes) {
        Collection ownedElements = ns.getOwnedElements();
        if(ownedElements != null) {
            Iterator iter = ownedElements.iterator();
            Object element;
            String metaName = metaclass.getName();
            String currentBase;

            while(iter.hasNext()) {
                element = iter.next();
                if(element instanceof MStereotype) {
                    currentBase = ((MStereotype) element).getBaseClass();
                    //
                    //   if base class is not supplied or is most generic
                    //      then add it to the list
                    if(currentBase == null || currentBase.length() == 0 || 
                        currentBase.equals("ModelElement") || 
                        metaName.endsWith(currentBase)) {
                        stereotypes.add(element);
                    }
                    else {
                        //
                        //   see if we can find this class in the map
                        //
                        Class currentMetaclass = findMetaclass(currentBase);
                        if(currentMetaclass.isAssignableFrom(metaclass)) {
                            stereotypes.add(element);
                        }
                    }
                 }
            }
        }
    }
    
    private Class findMetaclass(String name) {
        Class metaclass = null;
        if(_metaclasses != null) {
            metaclass = (Class) _metaclasses.get(name);
        }
        if(metaclass == null) {
            metaclass = findClass("ru.novosoft.uml.foundation.core.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.model_management.M"+ name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.foundation.data_types.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.foundation.extension_mechanisms.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.foundation.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.behavior.activity_graphs.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.behavior.collaborations.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.behavior.common_behavior.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.behavior.state_machines.M" + name);
            if(metaclass == null) metaclass = findClass("ru.novosoft.uml.behavior.use_cases.M" + name);
            //
            //  if a total mismatch, then let it apply to anything
            if(metaclass == null) metaclass = MModelElement.class;
            if(_metaclasses == null) {
                _metaclasses = new HashMap();
            }
            _metaclasses.put(name,metaclass);
        }
        return metaclass;
    }
    
    private Class findClass(String className) {
        try {
            return Class.forName(className);
        }
        catch(ClassNotFoundException e) {
        }
        return null;
    }
        
    
    private ComboBoxModel updateStereotypes(MModel model) {
        //
        //   create a sorted set of stereotypes
        //      we will manually place "" at top and "Create New Stereotype"
        //      at bottom
        if(_stereotypes == null) {
            _stereotypes = new TreeSet(new UMLModelElementNameComparator());
        }
        else {
            _stereotypes.clear();
        }
        Profile profile = _container.getProfile();
        MModelElement target = (MModelElement) getTarget();
        Class targetClass = target.getClass();
        if(model != null) {
            addStereotypes(model,targetClass,_stereotypes);
        }
        profile.addWellKnownStereotypes(targetClass,_stereotypes);
        
                
        Object[] comboEntries = new Object[_stereotypes.size()+1];
        int index = 0;
        comboEntries[index++] = _noneStereotype;
        if(_stereotypes.size() > 0) {
            //
            //  this fairly complex code makes sure that
            //     identically named stereotypes get unambiguated
            //     by their package prefixes
            Iterator iter = _stereotypes.iterator();
            String prevName = null;
            String currentName = null;
            boolean wasDup = false;
            Object prevStereotype = null;
            Object currentStereotype = null;
            if(iter.hasNext()) {
                currentStereotype = iter.next();
                if(currentStereotype instanceof MStereotype) {
                    currentName = ((MStereotype) currentStereotype).getName();
                }
                else {
                    currentName = currentStereotype.toString();
                }
            }
            while(iter.hasNext()) {
                prevName = currentName;
                prevStereotype = currentStereotype;
                currentStereotype = iter.next();
                if(currentStereotype instanceof MStereotype) {
                    currentName = ((MStereotype) currentStereotype).getName();
                }
                else {
                    currentName = currentStereotype.toString();
                }
                if(wasDup || prevName.equals(currentName)) {
                    if(prevStereotype instanceof MStereotype) {
                        comboEntries[index++] = profile.formatElement((MStereotype) prevStereotype,null);
                    }
                    else {
                        comboEntries[index++] = prevStereotype.toString();
                    }
                    if(wasDup) {
                        wasDup = prevName.equals(currentName);
                    }
                    else {
                        wasDup = true;
                    }
                }
                else {
                    comboEntries[index++] = prevName;
                }
            }
            //
            //   add the last member in the list
            //
            if(prevName != null && currentName.equals(prevName)) {
                if(currentStereotype instanceof MStereotype) {
                    comboEntries[index++] = profile.formatElement((MStereotype) currentStereotype,null);
                }
                else {
                    comboEntries[index++] = currentStereotype.toString();
                }
            }
            else {
                comboEntries[index++] = currentName;
            }
        }
        
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(comboEntries);
        
        return comboModel;
    }
                    
    
    public void roleAdded(final MElementEvent event) {
    }
    
    public void recovered(final MElementEvent event) {
    }
    
    public void roleRemoved(final MElementEvent event) {
    }
    
    public void listRoleItemSet(final MElementEvent event) {
    }
    
    public void removed(final MElementEvent event) {
    }

    public void propertySet(final MElementEvent event) {
        String propName = event.getName();
        if(propName == null || propName.equals("stereotype")) {
            updateSelection();
        }
    }
   
    private void updateSelection() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MStereotype stereotype = ((MModelElement) target).getStereotype();
            int index = 0;
            if(stereotype != null) {
                Iterator iter = _stereotypes.iterator();
                Object currentStereotype;
                for(int i = 0; iter.hasNext(); i++) {
                    currentStereotype = iter.next();
                    if(currentStereotype == stereotype) {
                        index = i+1;
                        break;
                    }
                    else {
                        if(currentStereotype instanceof ProfileStereotype &&
                            ((ProfileStereotype) currentStereotype).equals(stereotype)) {
                                index = i + 1;
                                break;
                        }
                    }
                }
            }
            //
            //  seems unnecessary, but was throwing exceptions in early testing
            //
            if(index < getItemCount()) {
                setSelectedIndex(index);
            }
        }
    }
    
    public void itemStateChanged(final java.awt.event.ItemEvent event) {
        Object target = getTarget();
        if(event.getStateChange() == ItemEvent.SELECTED && target instanceof MModelElement) {
            MModelElement element = (MModelElement) target;
            
            int index = getSelectedIndex();
            MStereotype stereotype = null;
            Object obj = null;
            if(index > 0) {
                index--;
                if(index < _stereotypes.size()) {
                    Iterator iter = _stereotypes.iterator();
                    for(int i = 0; iter.hasNext();i++) {
                        obj = iter.next();
                        if(index == i) {
                            if(obj instanceof MStereotype) {
                                stereotype = (MStereotype) obj;
                            }
                            else {
                                if(obj instanceof ProfileStereotype) {
                                    MModel model = element.getModel();
                                    if(model == null) {
                                        if(element instanceof MFeature) {
                                            MClassifier owner = ((MFeature) element).getOwner();
                                            if(owner != null) {
                                                model = owner.getModel();
                                            }
                                        }
                                    }
                                    if(model != null) {
                                        stereotype = ((ProfileStereotype) obj).createStereotype(model);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            element.setStereotype(stereotype);
        }
    }

}