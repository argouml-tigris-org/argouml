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
public class UMLStereotypeComboBox extends JComboBox implements UMLUserInterfaceComponent, ActionListener, MouseListener {

    private UMLUserInterfaceContainer _container;
    private static String _noneStereotype = "";
    private Set _stereotypes;
    
    public UMLStereotypeComboBox(UMLUserInterfaceContainer container) {
        super();
        _container = container;
        addActionListener(this);
        addMouseListener(this);
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
            }
        }
    }

    private ComboBoxModel updateStereotypes(MModel model) {
        //
        //   create a sorted set of stereotypes
        //      we will manually place "" at top and "Create New Stereotype"
        //      at bottom
        if(_stereotypes == null) {
            _stereotypes = new TreeSet(new UMLClassifierNameComparator());
        }
        else {
            _stereotypes.clear();
        }
        Profile profile = _container.getProfile();
        MModelElement target = (MModelElement) getTarget();
        Class targetClass = target.getClass();
        if(model != null) {
            Collection modelElements = model.getOwnedElements();
            Iterator iter = modelElements.iterator();
            Object obj = null;
            while(iter.hasNext()) {
                obj = iter.next();
                if(obj instanceof MStereotype) {
                    if(profile.isAppropriateStereotype(targetClass,(MStereotype) obj)) {
                        _stereotypes.add(obj);
                    }
                }
            }
        }
        profile.addWellKnownStereotypes(targetClass,_stereotypes);
        
        //
        //  find current stereotype in list
        //
        int index = -1;
        MStereotype stereotype = target.getStereotype();
        if(stereotype != null) {
            Iterator iter = _stereotypes.iterator();
            for(int i = 0;iter.hasNext();i++) {
                if(iter.next() == stereotype) {
                    index = i;
                    break;
                }
            }
        }
                
        Vector comboEntries = new Vector(_stereotypes.size()+2);
        comboEntries.add(_noneStereotype);
        if(_stereotypes.size() > 0) {
            //
            //  this fairly complex code makes sure that
            //     identically named stereotypes get unambiguated
            //     by there package prefixes
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
                        comboEntries.add(profile.formatElement((MStereotype) stereotype,null));
                    }
                    else {
                        comboEntries.add(prevStereotype.toString());
                    }
                    if(wasDup) {
                        wasDup = prevName.equals(currentName);
                    }
                    else {
                        wasDup = true;
                    }
                }
                else {
                    comboEntries.add(prevName);
                }
            }
            //
            //   add the last member in the list
            //
            if(prevName != null && currentName.equals(prevName)) {
                if(currentStereotype instanceof MStereotype) {
                    comboEntries.add(profile.formatElement((MStereotype) currentStereotype,null));
                }
                else {
                    comboEntries.add(currentStereotype.toString());
                }
            }
            else {
                comboEntries.add(currentName);
            }
        }
        
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(comboEntries);
        Object selected = comboModel.getElementAt(index+1);
        comboModel.setSelectedItem(selected);
        
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
                for(int i = 0; iter.hasNext(); i++) {
                    if(iter.next() == stereotype) {
                        index = i+1;
                        break;
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
    
    public void actionPerformed(ActionEvent event) {
        Object target = getTarget();
        if(target instanceof MModelElement) {
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
                                stereotype = new MStereotypeImpl();
                                stereotype.setName(obj.toString());
                                MModel model = element.getModel();
                                if(model == null) {
                                    if(element instanceof MFeature) {
                                        MClassifier owner = ((MFeature) element).getOwner();
                                        if(owner != null) {
                                            model = owner.getModel();
                                        }
                                    }
                                }
                                _stereotypes.remove(obj);
                                _stereotypes.add(stereotype);
                                model.addOwnedElement(stereotype);
                            }
                            break;
                        }
                    }
                }
            }
            element.setStereotype(stereotype);
        }
    }

    public void mouseReleased(final java.awt.event.MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    public void mouseEntered(final java.awt.event.MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    public void mouseClicked(final java.awt.event.MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    public void mousePressed(final java.awt.event.MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    public void mouseExited(final java.awt.event.MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }
    
    private final void showPopup(MouseEvent event) {
        Point point = event.getPoint();
        JPopupMenu popup = new JPopupMenu();

        int index = getSelectedIndex();
        UMLListMenuItem open = new UMLListMenuItem("Open...",this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem("Delete",this,"delete",index);
        if(index == 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }
        popup.add(open);
        popup.add(new UMLListMenuItem("Add...",this,"add",index));
        popup.add(delete);
        popup.show(this,point.x,point.y);
    }
    
    public void open(int index) {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MStereotype stereotype = ((MModelElement) target).getStereotype();
            if(stereotype != null) {
                _container.navigateTo(stereotype);
            }
        }
    }
    
    public void add(int index) {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement element = (MModelElement) target;
            MModel model = element.getModel();
            MStereotype stereotype = new MStereotypeImpl();
            model.addOwnedElement(stereotype);
            element.setStereotype(stereotype);
            _container.navigateTo(stereotype);
        }
    }
    
    public void delete(int index) {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MStereotype stereotype = ((MModelElement) target).getStereotype();
            if(stereotype != null) {
                Collection extendedElements = stereotype.getExtendedElements();
                if(extendedElements != null) {
                    ArrayList tempList = new ArrayList(extendedElements);
                    Iterator iter = tempList.iterator();
                    while(iter.hasNext()) {
                        ((MModelElement) iter.next()).setStereotype(null);
                    }
                }
                 MNamespace namespace = stereotype.getNamespace();
                 if(namespace != null) {
                    namespace.removeOwnedElement(stereotype);
                }
            }
        }
    }
    
}