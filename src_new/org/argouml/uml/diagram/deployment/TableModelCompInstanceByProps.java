// $Id$
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


// File: TableModelCompInstanceByProps.java
// Classes: TableModelCompInstanceByProps
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment;

import java.util.*;
import java.beans.*;
import org.argouml.model.ModelFacade;

import org.argouml.uml.*;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;

/**
 * @deprecated this class is deprecated since 0.15.1 and should be removed
 *             in 0.15.2, due to the fact that the TableModel classes
 *             have never been used, and are not maintained,
 *
 *             There is no reason why someone cannot try to complete the
 *             TableModel implementation, however, a higher priority
 *             at the moment is to clean argouml of un maintained code.
 */
public class TableModelCompInstanceByProps extends TableModelComposite {
    ////////////////
    // constructor
    public TableModelCompInstanceByProps() { }

    public void initColumns() {
	addColumn(ColumnDescriptor.Name);
	addColumn(ColumnDescriptor.BaseForComponentInstance); 
	addColumn(ColumnDescriptor.CompNodeInstance);
	//nsuml problem realization    addColumn(ColumnDescriptor.Implements);
	addColumn(ColumnDescriptor.MStereotype);
    }

    public Vector rowObjectsFor(Object t) {
	if (!(t instanceof UMLDeploymentDiagram || org.argouml.model.ModelFacade.isANodeInstance(t)))
	    return new Vector();
	if (t instanceof UMLDeploymentDiagram) {
	    UMLDeploymentDiagram d = (UMLDeploymentDiagram) t;
	    Vector nodes = d.getNodes();
	    Vector res = new Vector();
	    int size = nodes.size();
	    for (int i = 0; i < size; i++) {
		Object node = nodes.elementAt(i);
		if (org.argouml.model.ModelFacade.isAComponentInstance(node)) res.addElement(node);
	    }
	    return res;
	}
	else {
	    Object n = /*(MNodeInstance)*/ t;
	    Vector res = new Vector();
	    Collection residences = ModelFacade.getResidents(n);
	    Iterator it = residences.iterator();
	    while (it.hasNext()) {
		Object inst = /*(MInstance)*/ it.next();
		if (org.argouml.model.ModelFacade.isAComponentInstance(inst)) res.addElement(inst);
	    }
	    return res;
	}
    }

    public String toString() { return "ComponentInstances vs. Properties"; }
} /* end class TableModelCompInstanceByProps */