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
import ru.novosoft.uml.behavior.use_cases.*;
import java.util.*;
import java.awt.*;

public class UMLExtendListModel extends UMLModelElementListModel  {

    private final static String _nullLabel = "(null)";
    
    public UMLExtendListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    protected int recalcModelElementSize() {
        int size = 0;
        Collection xtends = getExtends();
        if(xtends != null) {
            size = xtends.size();
        }
        return size;
    }
    
    protected MModelElement getModelElementAt(int index) {
        MModelElement elem = null;
        Collection xtends = getExtends();
        if(xtends != null) {
            elem = elementAtUtil(xtends,index,MExtend.class);
        }
        return elem;
    }
            
        
    private Collection getExtends() {
        Collection xtends = null;
        Object target = getTarget();
        if(target instanceof MUseCase) {
            MUseCase useCase = (MUseCase) target;
            xtends = useCase.getExtends();
        }
        return xtends;
    }
    
    
    public void add(int index) {
        Object target = getTarget();
        if(target instanceof MUseCase) {
            MUseCase useCase = (MUseCase) target;
            MExtend newExtend = new MExtendImpl();
            newExtend.setExtension(useCase);
            useCase.addExtend(newExtend);
            
            fireIntervalAdded(this,index,index);
            navigateTo(newExtend);
        }
    }
    
    public void delete(int index) {
        Object target = getTarget();
        if(target instanceof MUseCase) {
            MUseCase useCase = (MUseCase) target;
            MExtend xtend = (MExtend) getModelElementAt(index);
            xtend.setExtension(null);
            useCase.removeExtend(xtend);
            MUseCase base = xtend.getBase();
            if(base != null) {
                xtend.setBase(null);
                base.removeExtend2(xtend);
            }
            fireIntervalRemoved(this,index,index);
        }
    }
    

    public boolean buildPopup(JPopupMenu popup,int index) {
        UMLListMenuItem open = new UMLListMenuItem("Open",this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem("Delete",this,"delete",index);
        if(getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        popup.add(new UMLListMenuItem("Add",this,"add",index));
        popup.add(delete);

        return true;
    }
    
    
}




