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

package org.argouml.uml.diagram.state;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.behavior.state_machines.*;

import org.apache.commons.logging.Log;
import org.argouml.uml.*;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;

public class TableModelTransByProps extends TableModelComposite {
    protected static Log logger = 
        org.apache.commons.logging.LogFactory.getLog(TableModelTransByProps.class);
  ////////////////
  // constructor
  public TableModelTransByProps() { }

  public void initColumns() {
    addColumn(ColumnDescriptor.Name);
    addColumn(ColumnDescriptor.Source);
    addColumn(ColumnDescriptor.Target);
    addColumn(ColumnDescriptor.Trigger);
    addColumn(ColumnDescriptor.MGuard);
    addColumn(ColumnDescriptor.Effect);
    addColumn(ColumnDescriptor.MStereotype);
  }

  public Vector rowObjectsFor(Object t) {
    if (!(t instanceof UMLStateDiagram)) return new Vector();
    UMLStateDiagram d = (UMLStateDiagram) t;
    Vector edges = d.getEdges();
    Vector res = new Vector();
    int size = edges.size();
    for (int i = 0; i < size; i++) {
      Object edge = edges.elementAt(i);
      if (edge instanceof MTransition) res.addElement(edge);
    }
    return res;
  }

  public String toString() { return "Transitions vs. Properties"; }
} /* end class TableModelTransByProps */

