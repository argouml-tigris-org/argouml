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

public class UMLGeneralizationListModel extends UMLModelElementListModel  {

    final private static String _nullLabel = "null";
    
    public UMLGeneralizationListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    public Collection getGeneralizations() {
        Collection generalizations = null;
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            generalizations = ((MGeneralizableElement) target).getGeneralizations();
        }
        return generalizations;
    }

    protected int recalcModelElementSize() {
        int size = 0;
        Collection generalizations = getGeneralizations();
        if(generalizations != null) {
            size = generalizations.size();
        }
        return size;
    }
    
    protected MModelElement getModelElementAt(int index) {
        return elementAtUtil(getGeneralizations(),index,MGeneralization.class);
    }
    
    public Object formatElement(MModelElement element) {
        Object value = _nullLabel;
        if(element != null) {
            MGeneralization gen = (MGeneralization) element;
            MGeneralizableElement target = gen.getParent();
            if(target != null) {
                value = super.formatElement(target);
            }
        }
        return value;
    }
    
    public void open(int index) {
        MModelElement generalization = getModelElementAt(index);
        if(generalization != null) {
            navigateTo(generalization);
        }
    }    

    public void add(int index) {
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;
            MGeneralization newGen = new MGeneralizationImpl();
            newGen.setChild(genElem);
            if(index == getSize()) {    
                genElem.addGeneralization(newGen);
            }
            else {
                genElem.setGeneralizations(addAtUtil(genElem.getGeneralizations(),newGen,index));
            }
            fireIntervalAdded(this,index,index);
            navigateTo(newGen);
        }
    }
    
    public void delete(int index) {
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;
            MModelElement modElem = getModelElementAt(index);
            if(modElem instanceof MGeneralization) {
                MGeneralization gen = (MGeneralization) modElem;
                genElem.removeGeneralization(gen);
                gen.setChild(null);
                MGeneralizableElement otherElem = gen.getParent();
                otherElem.removeSpecialization(gen);
                gen.setParent(null);
                fireIntervalRemoved(this,index,index);
            }
        }
    }
    
    public void moveUp(int index) {
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;
            genElem.setGeneralizations(moveUpUtil(genElem.getGeneralizations(),index));
            fireContentsChanged(this,index-1,index);
        }
    }
    
    public void moveDown(int index) {
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;
            genElem.setGeneralizations(moveDownUtil(genElem.getGeneralizations(),index));
            fireContentsChanged(this,index,index+1);
        }
    }
    

}


