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


/*
 * UMLToDoItem.java
 *
 * Created on December 14, 2003, 4:19 PM
 */

package org.argouml.uml.cognitive;

import org.argouml.cognitive.critics.Critic;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDiagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.Highlightable;
import org.tigris.gef.util.VectorSet;
import java.util.Enumeration;

/** UMLToDoItem is the preferred class for newly created ToDoItems within 
 * ArgoUML. It knows more about possible designmaterial and can for example
 * highlight offenders when they are ModelElements by finding the according Fig
 * in the current diagram for them.
 *
 * @see org.argouml.cognitive.ToDoItem
 * @since 0.15.3
 * @author  mkl
 */
public class UMLToDoItem extends org.argouml.cognitive.ToDoItem {
    
    public UMLToDoItem(Poster poster, String h, int p, String d, String m,
    VectorSet offs) {
        super(poster, h, p, d, m, offs);
    }
    
    public UMLToDoItem(Poster poster, String h, int p, String d, String m) {
        super(poster, h, p, d, m);
    }
    
    public UMLToDoItem(Critic c, Object dm, Designer dsgr) {
        super(c, dm, dsgr);
    }
    
    public UMLToDoItem(Critic c, VectorSet offs, Designer dsgr) {
        super(c, offs, dsgr);
    }
    
    public UMLToDoItem(Critic c) {
        super(c);
    }
    
    public void deselect() {
        Enumeration offs = getOffenders().elements();
        Project p = ProjectManager.getManager().getCurrentProject();
        ArgoDiagram diag = null;
        if (p != null) diag = p.getActiveDiagram();
        while (offs.hasMoreElements()) {
            Object dm =  offs.nextElement();    
            if (dm instanceof Highlightable)
                ((Highlightable) dm).setHighlight(false);
            else if (diag != null) {
                Fig f = diag.getLayer().presentationFor(dm);
                if (f instanceof Highlightable) 
                    ((Highlightable)f).setHighlight(false);
            }
        }
    }
    
    public void select() {
        Enumeration offs = getOffenders().elements();
        Project p = ProjectManager.getManager().getCurrentProject();
        ArgoDiagram diag = null;
        if (p != null) diag = p.getActiveDiagram();
        while (offs.hasMoreElements()) {
            Object dm = offs.nextElement();
            if (dm instanceof Highlightable)
                ((Highlightable) dm).setHighlight(true);
            else if (diag != null) {
                Fig f = diag.getLayer().presentationFor(dm);
                if (f instanceof Highlightable) 
                    ((Highlightable)f).setHighlight(true);
            }
        }
    }
}
