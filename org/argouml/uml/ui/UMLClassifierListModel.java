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


package org.argouml.uml.ui;

import org.apache.log4j.Category;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.lang.reflect.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLClassifierListModel extends UMLModelElementCachedListModel  {
    protected static Category cat = 
        Category.getInstance(UMLClassifierListModel.class);
    

    final private static String _nullLabel = "null";
    private java.util.List _classes;
    private Class _classifierType;
    private Class _implClass;
    
    public UMLClassifierListModel(UMLUserInterfaceContainer container, String property, boolean showNone, Class classifierType, Class implClass) {
        super(container, property, showNone);
        _classifierType = classifierType;
        _implClass = implClass;
    }
    
    protected void resetCache() {
        _classes = null;
    }
    
    public boolean isProperClass(Object obj) {
        return _classifierType.isInstance(obj);
    }

    public Collection getRawCollection() {
        Collection raw = null;
        Object target = getTarget();
        if (target instanceof MNamespace) {
            raw = ((MNamespace) target).getOwnedElements();
        }
        return raw;
    }
    
    protected java.util.List getCache() {
        if (_classes == null) {
            _classes = buildCache();
        }
        return _classes;
    }
            
    public void add(int index) {
        Object target = getTarget();
        if (target instanceof MNamespace) {
            MNamespace namespace = (MNamespace) target;
            
            MClassifier newClass = null;
            try {
                Class[] noClasses = {};
                Constructor constructor = _implClass.getConstructor(noClasses);
                Object[] noArgs = {};
                newClass = (MClassifier) constructor.newInstance(noArgs);
                newClass.setNamespace(namespace);
                int oldSize = getModelElementSize();
                namespace.addOwnedElement(newClass);
                if (_classes != null) {
                    _classes.add(newClass);
                }
                fireIntervalAdded(this, oldSize, oldSize);
                navigateTo(newClass);
            }
            catch (Exception e) {
                cat.error(e.toString() + " in UMLClassifierListModel.add()", e);
            }
        }
    }

    
    public void delete(int index) {
        Object target = getTarget();
        if (target instanceof MNamespace && _classes != null) {
            Object clas = _classes.get(index);
            if (clas != null) {
                _classes.remove(index);
                ((MNamespace) target).removeOwnedElement((MClassifier) clas);
                resetSize();
                fireIntervalRemoved(this, index, index);
            }
        }
    }
    
    public boolean buildPopup(JPopupMenu popup, int index) {
        UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"), this, "open", index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"), this, "delete", index);
        if (getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        popup.add(new UMLListMenuItem(container.localize("Add"), this, "add", index));
        popup.add(delete);

        return true;
    }

    java.util.Collection createCollection(int initialSize) {
        return new TreeSet(new UMLClassifierNameComparator());
    }
    
}


