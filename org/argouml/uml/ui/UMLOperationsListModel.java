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

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.lang.reflect.*;

import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

public class UMLOperationsListModel extends UMLModelElementCachedListModel  {

    final private static String _nullLabel = "null";
    private java.util.List _operations;


    public UMLOperationsListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }

    protected void resetCache() {
        _operations = null;
    }

    public boolean isProperClass(Object obj) {
        return obj instanceof MOperation;
    }

    public Collection getRawCollection() {
        Collection raw = null;
        Object target = getTarget();
        if(target instanceof MClassifier) {
            raw = ((MClassifier) target).getFeatures();
        }
        return raw;
    }

    protected java.util.List getCache() {
        if(_operations == null) {
            _operations = buildCache();
        }
        return _operations;
    }



/**
 *   Adds a new operation, updating both the cache and underlying collection
 *   and navigating to the new operation.  Called by "Add" popup menu item.
 * Calls addElement to do the actual updating of the collection and cache.
 *
 * 	Modified: July 19, 2001 - psager
 *  Modified: Dec  06, 2001 - thn
 *
 *   @param  index position of new operation (zero-based) but only refers
 *           to the index position in the particular list box, not the collection.
 */

    public void add(int index){
        Object target = getTarget();

        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            Collection oldFeatures = classifier.getFeatures();
            MOperation newOp = UmlFactory.getFactory().getCore().buildOperation(classifier);
            newOp.addMElementListener((FigNodeModelElement)(ProjectBrowser.TheInstance.getActiveDiagram().presentationFor(classifier)));
            classifier.setFeatures(addElement(oldFeatures, index, newOp,
                                   _operations.isEmpty()?null:_operations.get(index)));
            fireContentsChanged(this,index-1,index);
            navigateTo(newOp);
        }
    }  // ...end of add()...


    public void delete(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier && _operations != null) {
            Object operation = _operations.get(index);
            if(operation != null) {
                _operations.remove(index);
                ((MClassifier) target).removeFeature((MOperation) operation);
                resetSize();
                fireIntervalRemoved(this,index,index);
            }
        }
    }

    public void moveUp(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier && _operations != null) {
            MClassifier classifier = (MClassifier) target;
            Collection oldFeatures = classifier.getFeatures();
            classifier.setFeatures(swap(oldFeatures,index-1,_operations.get(index-1),_operations.get(index)));
            fireContentsChanged(this,index-1,index);
        }
    }

    public void moveDown(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier && _operations != null) {
            MClassifier classifier = (MClassifier) target;
            Collection oldFeatures = classifier.getFeatures();
            classifier.setFeatures(swap(oldFeatures,index,_operations.get(index),_operations.get(index+1)));
            fireContentsChanged(this,index,index+1);
        }
    }
    
    /**
     *  This method builds a context (pop-up) menu for the list.  
     *
     *  @param popup popup menu
     *  @param index index of selected list item
     *  @return "true" if popup menu should be displayed
     */
    public boolean buildPopup(JPopupMenu popup,int index) {
        UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"),this,"delete",index);
        if(getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        UMLListMenuItem add =new UMLListMenuItem(container.localize("New"),this,"add",index);
        if(_upper >= 0 && getModelElementSize() >= _upper) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);
        /*
        UMLListMenuItem moveUp = new UMLListMenuItem(container.localize("Move Up"),this,"moveUp",index);
        if(index == 0) moveUp.setEnabled(false);
        popup.add(moveUp);
        UMLListMenuItem moveDown = new UMLListMenuItem(container.localize("Move Down"),this,"moveDown",index);
        if(index == getSize()-1) moveDown.setEnabled(false);
        popup.add(moveDown);
        */
        return true;
    }
}





