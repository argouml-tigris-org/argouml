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
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;
import java.lang.reflect.*;

/**
 *   This class implements a list model for the attributes of a classifier.
 *   Used with a UMLList to display a list of attributes.  Since attributes
 *   may be mixed with other model elements in the features of a classifier,
 *   this class implements a cache of attributes that are keep synchronized
 *   with the features of the current classifier.
 *
 *   @author Curt Arnold
 *   @see UMLModelElementListModel
 *   @see UMLList
 */
public class UMLAttributesListModel extends UMLModelElementCachedListModel  {

    final private static String _nullLabel = "null";
    private java.util.List _attributes;

    /**
     *   Creates a new attribute list model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLAttributesListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    /**
     *   Called to indicate that the cache of attributes may have become invalid.
     */
    protected void resetCache() {
        _attributes = null;
    }
    
    /**
     *   Called to determine if a particular feauture of the underlying collection
     *   should be in the cached list of model elements.
     *   @param obj object to be considered.
     *   @return true if object is appropriate for this list.
     */
    public boolean isProperClass(Object obj) {
        return obj instanceof MAttribute;
    }

    /**
     *   returns the raw underlying collection from the current target 
     *   of the container.
     *
     *   @return underlying collection.
     */
    public Collection getRawCollection() {
        Collection raw = null;
        Object target = getTarget();
        if(target instanceof MClassifier) {
            raw = ((MClassifier) target).getFeatures();
        }
        return raw;
    }
    
    /**
     *    returns the cache of model elements, rebuilding the cache if invalidated.
     *    @return cache of model elements
     */
    protected java.util.List getCache() {
        if(_attributes == null) {
            _attributes = buildCache();
        }
        return _attributes;
    }
            
    /**
     *   Adds a new attribute, updating both the cache and underlying collection
     *   and navigating to new attribute.  Called by "Add" popup menu item.
     *   @param index position of new attribute (zero-based)
     */
    public void add(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MAttribute newAttr = new MAttributeImpl();
            int oldSize = getModelElementSize();
            java.util.List newAttrs = addElement(classifier.getFeatures(),index,newAttr);
            if(newAttrs == null) {
                classifier.addFeature(newAttr);
                fireIntervalAdded(this,oldSize,oldSize);
            }
            else {
                classifier.setFeatures(newAttrs);
                fireIntervalAdded(this,index+1,index+1);
            }
            navigateTo(newAttr);
        }
    }

    
    /**
     *   Deletes a specific attribute from both the cache and underlying
     *   collection.
     *   @param index position of attribute to be deleted.
     */
    public void delete(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier && _attributes != null) {
            Object attribute = _attributes.get(index);
            if(attribute != null) {
                 if(_attributes != null) {
                    _attributes.remove(index);
                }

                ((MClassifier) target).removeFeature((MAttribute) attribute);
                resetSize();
                fireIntervalRemoved(this,index,index);
            }
        }
    }
    
    /**
     *   Moves attribute up in the underlying collection.
     *   @param index element to move up
     */
    public void moveUp(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            Collection oldFeatures = classifier.getFeatures();
            classifier.setFeatures(swap(oldFeatures,index-1,_attributes.get(index-1),_attributes.get(index)));
            fireContentsChanged(this,index-1,index);
        }
    }
    
    /**
     *  Moves an attribute down in the underlying collection.
     *  @param index element to move down.
     */
    public void moveDown(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            Collection oldFeatures = classifier.getFeatures();
            classifier.setFeatures(swap(oldFeatures,index,_attributes.get(index),_attributes.get(index+1)));
            fireContentsChanged(this,index,index+1);
        }
    }
    
}


