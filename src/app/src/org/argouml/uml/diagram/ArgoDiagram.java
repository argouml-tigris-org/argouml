// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.argouml.cognitive.ItemUID;
import org.argouml.kernel.Project;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * An interface to decouple ArgoUML from GEF and to untangle the Project <->
 * ArgoDiagram ball of string.
 * <p>
 * Although this interface is implemented by {@link ArgoDiagramImpl}, it does
 * <em>NOT</em> extend any GEF interfaces (since GEF is made up entirely of
 * concrete classes), so any new methods which are added to
 * {@link org.tigris.gef.base.Diagram} and which are needed by ArgoUML will need
 * to be added to this interface manually.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 * @since 0.25.4 when it replaced the implementation class of the same name
 */
public interface ArgoDiagram {

    /*
     * @see org.tigris.gef.base.Diagram#setName(java.lang.String)
     */
    public void setName(String n) throws PropertyVetoException;

    /**
     * @param i the new id
     */
    public void setItemUID(ItemUID i);

    /**
     * USED BY pgml.tee!!
     * @return the item UID
     */
    public ItemUID getItemUID();

    /** The bean property name denoting the diagram's namespace. 
     * Value is a String. */
    public static final String NAMESPACE_KEY = "namespace";

    /**
     * TODO: The reference to the method
     * org.argouml.uml.ui.VetoablePropertyChange#getVetoMessage(String)
     * was here but the class does exist anymore. Where is it?
     * This method is never used!
     *
     * @param propertyName is the name of the property
     * @return a message or null if not applicable.
     */
    public String getVetoMessage(String propertyName);

    /**
     * Finds the presentation (the Fig) for some object. If the object
     * is a modelelement that is contained in some other modelelement
     * that has its own fig, that fig is returned. It extends
     * presentationFor that only gets the fig belonging to the node
     * obj.<p>
     *
     * @author jaap.branderhorst@xs4all.nl
     * @return the Fig for the object
     * @param obj is th object
     */
    public Fig getContainingFig(Object obj);

    /**
     * This will mark the entire visible area of all Editors to be repaired
     *  from any damage - i.e. repainted.
     */
    public void damage();

    /**
     * Get all the model elements in this diagram that are represented
     * by a FigEdge.
     * @see org.tigris.gef.base.Diagram#getEdges()
     */
    public List getEdges();

    /**
     * Get all the model elements in this diagram that are represented
     * by a FigNode.
     * @see org.tigris.gef.base.Diagram#getNodes()
     */
    public List getNodes();


    /**
     * We hang our heads in shame. There are still bugs in ArgoUML
     * and/or GEF that cause corruptions in the model.
     * Before a save takes place we repair the model in order to
     * be as certain as possible that the saved file will reload.
     * TODO: Split into small inner classes for each fix.
     *
     * @return A text that explains what is repaired.
     */
    public String repair();

    /**
     * Find all the Figs that visualise the given model element in
     * this layer, or null if there is none.
     * 
     * TODO: once GEF includes this same method in Diagram then this can go
     * 
     * @see org.tigris.gef.base.Diagram#presentationsFor(java.lang.Object)
     */
    public List presentationsFor(Object obj);

    /**
     * Remove this diagram from existence.
     * 
     * TODO: Move to GEF
     */
    public void remove();

    /**
     * Keeps track of the project that contains this diagram. 
     * The Project determines many settings that reflect 
     * the way the diagram is painted, such as font size.
     * 
     * @param p the project that contains this diagram
     */
    public void setProject(Project p);

    /**
     * Called when the user releases a dragged FigNode.
     * 
     * @param enclosed the enclosed FigNode that was dragged into the encloser
     * @param oldEncloser the previous encloser
     * @param newEncloser the FigNode that encloses the dragged FigNode
     */
    public void encloserChanged(FigNode enclosed, FigNode oldEncloser,
            FigNode newEncloser);

    /**
     * This method shall return any UML modelelements
     * that should be deleted when the diagram gets deleted,
     * or null if there are none. The default implementation returns null;
     * e.g. a statechart diagram should return its statemachine.
     *
     * @author mvw@tigris.org
     *
     * @return the dependent element - in the general case there aren't, so null
     */
    public Object getDependentElement();

    /**
     * TODO: MVW: I am not completely sure of the following:<p>
     * The "namespace" of the diagram is e.g. used when creating new elements
     * that are shown on the diagram; they will have their namespace set
     * according this. It is NOT necessarily equal to the "owner". 
     * 
     * @return the namespace for the diagram
     */
    public Object getNamespace();

    /**
     * Sets the namespace of the Diagram, and
     * adds the diagram as a listener of its namespace in the UML model
     * (so that it can delete itself when the model element is deleted).
     *
     * @param ns the namespace for the diagram
     */
    public void setNamespace(Object ns);

    /**
     * Set the namespace of a model element to the owner of
     * the given namespace. If the namespace is null
     * the namespace of the diagram is used instead.
     * If the modelElement is not valid in the given namespace
     * this method takes no action.
     * @param modelElement the model element
     * @param ns the namespace
     */
    public void setModelElementNamespace(Object modelElement, Object ns);

    /**
     * This diagram listens to events from its namespace ModelElement;
     * when the modelelement is removed, we also want to delete this
     * diagram.  <p>
     *
     * There is also a risk that if this diagram was the one shown in
     * the diagram panel, then it will remain after it has been
     * deleted. So we need to deselect this diagram. 
     * There are other things to take care of, so all this is delegated to 
     * {@link org.argouml.kernel.Project#moveToTrash(Object)}.
     * 
     * @param evt A PropertyChangeEvent object describing the event source
     * and the property that has changed.
     *
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt);

    /**
     * The owner of a diagram is the modelelement that is saved
     * with the project in the pgml file, and binds it to the model. <p>
     * 
     * This value is shown in the diagram's properties panel
     * as the "Home model".
     *
     * @return the home model
     */
    public Object getOwner();
    
    /**
     * @return an iterator which iterates over all Figs in Diagram
     */
    public Iterator<Fig> getFigIterator();
    
    ///////////////// GEF Methods ////////////////////////////
    // TODO: These should really be picked up automatically from
    // some GEF interface that we extend, but there is no such
    // thing.  NOTE: We've only added methods used by ArgoUML,
    // so it's possible that external consumers need other methods.
    
    /**
     * @return an enumeration of the contents of the fig.
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getFigIterator()}.
     * @see org.tigris.gef.base.Diagram#elements()
     */
    @Deprecated
    public Enumeration elements();
    /**
     * @param listener
     * @see org.tigris.gef.base.Diagram#addVetoableChangeListener(VetoableChangeListener)
     */
    public void addVetoableChangeListener(VetoableChangeListener listener);

    /**
     * @param listener
     * @see org.tigris.gef.base.Diagram#removeVetoableChangeListener(VetoableChangeListener)
     */
    public void removeVetoableChangeListener(VetoableChangeListener listener);

    /**
     * @param property
     * @param listener
     * @see org.tigris.gef.base.Diagram#addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String property,
            PropertyChangeListener listener);

    /**
     * @param property
     * @param listener
     * @see org.tigris.gef.base.Diagram#removePropertyChangeListener(String, PropertyChangeListener)
     */
    public void removePropertyChangeListener(String property,
            PropertyChangeListener listener);
    
    /**
     * @return the GEF graphmodel for this diagram
     * @see org.tigris.gef.base.Diagram#getGraphModel()
     */
    public GraphModel getGraphModel();
    
    /**
     * @return the GEF LayerPerspective of this diagram
     * @see org.tigris.gef.base.Diagram#getLayer()
     */
    public LayerPerspective getLayer();
    
    /**
     * @param figures list of Figures to check for in diagram
     * @return count of figures contained in this diagram
     * @see org.tigris.gef.base.Diagram#countContained(List)
     */
    public int countContained(List figures);
    
    /**
     * @param o The object which owns the fig
     * @return the corresponding fig
     * @see org.tigris.gef.base.Diagram#presentationFor(Object)
     */
    public Fig presentationFor(Object o);
    
    /**
     * @param f Fig to be added
     * @see org.tigris.gef.base.Diagram#add(Fig)
     */
    public void add(Fig f);
    
    /**
     * @return the name of the diagram
     * @see org.tigris.gef.base.Diagram#getName()
     */
    public String getName();
    
    /**
     * Perform any pre-save actions.
     * @see org.tigris.gef.base.Diagram#preSave()
     */
    public void preSave();
    
    /**
     * Perform any post-save actions.
     * @see org.tigris.gef.base.Diagram#postSave()
     */
    public void postSave();
    
    /**
     * Perform any post-load actions.
     * @see org.tigris.gef.base.Diagram#postLoad()
     */
    public void postLoad();
    /////////////////// End GEF methods ////////////////////////

}