// $Id$
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

package org.argouml.ui.explorer;

import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

import org.argouml.model.ModelFacade;

/**
 * Sorts explorer nodes by their user object name.
 *
 * @author  alexb
 * @since 0.15.2, Created on 28 September 2003, 10:02
 */
public class NameOrder 
 implements Comparator{
    
    /** Creates a new instance of NameOrder */
    public NameOrder() {
    }
    
    public int compare(Object obj, Object obj1) {
        
        ExplorerTreeNode node = (ExplorerTreeNode)obj;
        ExplorerTreeNode node1 = (ExplorerTreeNode)obj1;
        
        Object userObject = node.getUserObject();
        Object userObject1 = node1.getUserObject();
        
        return compareUserObjects(userObject,userObject1);
    }
    
    /**
     * alphabetic ordering of user object names instead of type names
     */
    protected int compareUserObjects(Object obj, Object obj1){
        
        if((ModelFacade.isADiagram(obj) ||
        ModelFacade.isABase(obj) )&&
        (ModelFacade.isADiagram(obj1) ||
        ModelFacade.isABase(obj1) )){
            
            String name = ModelFacade.getName(obj)==null?"":ModelFacade.getName(obj);
            String name1 = ModelFacade.getName(obj1)==null?"":ModelFacade.getName(obj1);
            
            int ret = name.compareToIgnoreCase(name1);
            return ret;
        }
        else
            return 0;
    }
    
    public String toString(){
        return "Order By Name";
    }
}