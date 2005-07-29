// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.beans.PropertyChangeSupport;

import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.EventAdapter;
import org.argouml.model.RemoveAssociationEvent;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * Event adaptor for the explorer to decouple the explorer from the nsuml model.
 *
 * @author  alexb
 */
public final class ExplorerNSUMLEventAdaptor
    extends PropertyChangeSupport
    implements MElementListener, EventAdapter {

    private static final Logger LOG =
        Logger.getLogger(ExplorerNSUMLEventAdaptor.class);
    
    /**
     * Creates a new instance of ExplorerUMLEventAdaptor.
     */
    public ExplorerNSUMLEventAdaptor() {
        // PropertyChangeSupport needs a source object
        super(UmlModelEventPump.getPump());
    }

    /**
     * This is called whenever a model element refencing multiple other model
     * elements has one of the mdeo lements replaced.<p>
     * For example an attribute in a class is replaced by another.<p>
     * Operations of this sort do not take place in ArgoUML and so we make no
     * effort to convert them.
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
//        if (e.getAddedValue() != null
//            || e.getRemovedValue() != null
//            || (e.getNewValue() != null
//                && !e.getNewValue().equals(e.getOldValue()))) {
//            firePropertyChanged("umlModelStructureChanged", e.getSource());
//        }
        // If anyone sees this error produced then please inform me. Bob.
        LOG.error("ExplorerNSUMLEventAdaptor.listRoleItemSet is purposely not"
                + "implemented. Do not expect to be here.");
    }

    /**
     * This is called whenever a property of a model element is changed.
     * For example when the name of a class is changed.<p>
     * These are converted to AttributeChangeEvents which are recognised by the
     * rest of ArgoUML.
     * @see ru.novosoft.uml.MElementListener#propertySet(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {

            
//	    firePropertyChanged("modelElementChanged", e.getSource());
            AttributeChangeEvent event =
                new AttributeChangeEvent(e.getSource(),
                        e.getName(),
                        e.getOldValue(),
                        e.getNewValue(),
                        e);

            this.firePropertyChange(event);
        }

    }

    /**
     * I can find no documentation of when this is used. During debuging I have
     * never found it called (Bob Tarling).
     * My assumption is this is triggered when an item is recovered from some
     * trash and not relevent to us so we make no effort to convert them.
     * @see
     * ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
//        if (e.getAddedValue() != null
//            || e.getRemovedValue() != null
//            || (e.getNewValue() != null
//                && !e.getNewValue().equals(e.getOldValue()))) {
//            firePropertyChanged("modelElementAdded", e.getSource());
//        }
        // If anyone sees this error produced then please inform me. Bob.
        LOG.error("ExplorerNSUMLEventAdaptor.recovered is purposely not "
                + "implemented. Do not expect to be here.");
    }

    /**
     * This is called whenever a model element is removed from the repository
     * (in our case deleted). This appears to be of no interest to the
     * explorer, presumably it detects this in some other way.
     * So we make no effort to convert these events.
     * @see ru.novosoft.uml.MElementListener#removed(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            //firePropertyChanged("modelElementRemoved", e.getRemovedValue());
            ;
        }

    }

    /**
     * This is called whenever a model element referencing collections other
     * model elements has has a model element added to one of those
     * collections.
     * For example an existing attribute is added to an existing class.<p>
     * We convert these to AddAssociationEvents.
     * @see
     * ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            //firePropertyChanged("modelElementAdded", e.getSource());
            AddAssociationEvent event =
                new AddAssociationEvent(e.getSource(),
                        e.getName(),
                        e.getOldValue(),
                        e.getNewValue(),
                        e.getAddedValue(),
                        e);

            this.firePropertyChange(event);
        }

    }

    /**
     * This is called whenever a model element referencing collections of other
     * model elements has a model element removed from one of those
     * collections.
     * For example an attribute is removed from a class.<p>
     * It does not mean that the attribute has also been removed from the
     * repository<p>
     * We convert these to RemoveAssociationEvents.
     *
     * TODO: Checking if an extendedElement is being removed from a stereotype
     * is somewhat bogus since that would break an Explorer view where
     * extended elements are children of stereotypes. Fortunately there is
     * none such currently.
     *
     * @see ru.novosoft.uml.MElementListener#roleRemoved(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (e.getSource() instanceof MStereotype
            && "extendedElement".equals(e.getName())) {
            return;
        }

        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            //firePropertyChanged("modelElementRemoved", e.getRemovedValue());
            RemoveAssociationEvent event =
                new RemoveAssociationEvent(e.getSource(),
                        e.getName(),
                        e.getOldValue(),
                        e.getNewValue(),
                        e.getRemovedValue(),
                        e);

            this.firePropertyChange(event);
        }
    }
}
