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

import java.util.List;
import java.util.ArrayList;

import org.argouml.ui.AbstractGoRule;
import org.argouml.application.api.Argo;
/**
 * Represents a perspective (or view) of the uml model for display in the
 * explorer.
 *
 * This class replaces the old NavPerspective class. This is much simpler.
 *
 * The rules in the perspective generate child nodes for any given parent
 * node in the explorer tree view. Those nodes are then stored as user objects
 * in the ExplorerTreeModel for efficient rendering.
 *
 * @author  alexb
 * @since 0.15.2, Created on 27 September 2003, 09:32
 */
public class ExplorerPerspective {
    
    List rules;
    String name;
    
    /** Creates a new instance of ExplorerPerspective */
    public ExplorerPerspective(String newName) {
        
        name = Argo.localize("Tree", newName);
        rules = new ArrayList();
    }
    
    public void addRule(AbstractGoRule rule){
        
        rules.add(rule);
    }
    
    public Object[] getRulesArray(){
        
        return rules.toArray();
    }
    
    public String toString(){
        
        return name;
    }
}
