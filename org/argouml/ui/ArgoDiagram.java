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

import java.beans.PropertyVetoException;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.cognitive.ItemUID;
import org.argouml.kernel.ProjectManager;
import org.argouml.util.ChangeRegistry;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.presentation.Fig;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MOperation;

public class ArgoDiagram extends Diagram {

  ItemUID _id;

  // hack to use vetocheck in constructing names
  protected static ArgoDiagram TheInstance = new ArgoDiagram(); 
  
  ////////////////////////////////////////////////////////////////
  // constructors

  public ArgoDiagram() { 
  	super();    
  }

  public ArgoDiagram(String diagramName ) {
  	// next line patch to issue 596 (hopefully)
  	super(diagramName);
    try { setName(diagramName); }
    catch (PropertyVetoException pve) { }
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setName(String n) throws PropertyVetoException {
    super.setName(n);
  }

  public void setItemUID(ItemUID id) {
    _id = id;
  }

  public ItemUID getItemUID() {
    return _id;
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
     * @return a message or null if not applicable.
     */
    public String getVetoMessage(String propertyName) {
    	if (propertyName.equals("name")) {
    		return "Name of diagram may not exist allready";
    	}
        return null;
    }

  

    /**
     * Finds the presentation (the fig) for some object. If the object is a 
     * modelelement that is contained in some other modelelement that has its 
     * own fig, that fig is returned. It extends presentationFor that only gets
     * the fig belonging to the node obj.
     * @author jaap.branderhorst@xs4all.nl
     */
    public Fig getContainingFig(Object obj) {
        Fig fig = super.presentationFor(obj);
        if (fig == null && obj instanceof MModelElement) { // maybe we have a modelelement
            // that is part of some other fig
            if (obj instanceof MOperation || obj instanceof MAttribute) {
                // get all the classes from the diagram
                Iterator it = getNodes().iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof MClassifier) {
                        MClassifier cl = (MClassifier)o;
                        if (cl.getFeatures().contains(obj)) return presentationFor(cl);
                    }
                }
            }
        }
        return fig;
    }

	/**
	 * @see org.tigris.gef.base.Diagram#initialize(Object)
	 */
	public void initialize(Object owner) {
		super.initialize(owner);
		ProjectManager.getManager().getCurrentProject().setActiveDiagram(this);
	}
    
    public void damage() {
        if (_lay != null && _lay.getEditors() != null) {
            Iterator it = _lay.getEditors().iterator();
            while (it.hasNext()) {
                ((Editor)it.next()).damageAll();
            }
        }
    }

    /**
     * @see org.tigris.gef.base.Diagram#getEdges()
     */
    public Vector getEdges() {
        if (getGraphModel() != null) {
            return getGraphModel().getEdges();
        }
        return super.getEdges();
    }

    /**
     * @see org.tigris.gef.base.Diagram#getNodes()
     */
    public Vector getNodes() {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return super.getNodes();
    }
    

} /* end class ArgoDiagram */
