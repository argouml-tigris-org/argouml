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

package org.argouml.ui;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import org.tigris.gef.base.*;

import org.argouml.kernel.Project;
import org.argouml.uml.ui.VetoablePropertyChange;
import org.argouml.util.*;

public class ArgoDiagram extends Diagram implements VetoablePropertyChange {

  ////////////////////////////////////////////////////////////////
  // constructors

  public ArgoDiagram() { }

  public ArgoDiagram(String diagramName ) {
    try { setName(diagramName); }
    catch (PropertyVetoException pve) { }
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setName(String n) throws PropertyVetoException {
    super.setName(n);
  }

  ////////////////////////////////////////////////////////////////
  // event management

  public void addChangeRegistryAsListener( ChangeRegistry change ) {
	  getGraphModel().addGraphEventListener( change );
  }

  public void removeChangeRegistryAsListener( ChangeRegistry change ) {
	  getGraphModel().removeGraphEventListener( change );
  }

  static final long serialVersionUID = -401219134410459387L;

    /**
     * @see org.argouml.uml.ui.VetoablePropertyChange#getVetoMessage(String)
     */
    public String getVetoMessage(String propertyName) {
    	if (propertyName.equals("name")) {
    		return "Name of diagram may not exist allready";
    	}
        return null;
    }

    /**
     * @see org.argouml.uml.ui.VetoablePropertyChange#vetoCheck(String, Object[])
     */
    public boolean vetoCheck(String propertyName, Object[] args) {
    	if (propertyName.equals("name")) {
    		if (args.length == 1) {
    			String newName = (String)args[0];
    			// 2002-07-18
  				// Jaap Branderhorst
  				// check the new name if it does not exist as a diagram name
  				// patch for issue 738
  				Project project = ProjectBrowser.TheInstance.getProject();
  				if (project != null) {
  					Vector diagrams = project.getDiagrams();
  					Iterator it = diagrams.iterator();
  					while (it.hasNext()) {
  						ArgoDiagram diagram = (ArgoDiagram)it.next();
  						if ((diagram.getName() != null ) && 
  							(diagram.getName().equals(newName)) && 
  							!getName().equals(newName)) {
  							return true;
  						}
  					}
  				}
    		}
    	}
        return false;
    }

} /* end class ArgoDiagram */
