// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import org.apache.log4j.Category;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.UUIDManager;

import ru.novosoft.uml.MBase;

/**
 * Abstract Class that every model package factory should implement
 * to share the initialize() method.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public abstract class AbstractUmlModelFactory {

    /**
     * Flag to indicate if the gui is enabled. If the gui is
     * enabled modelelements that are added should also be added to the
     * navigatorpane.
     */
    private static boolean GuiEnabled = true;

    /** Log4j logging category.
     */
    private static Category logger =
        Category.getInstance("org.argouml.model.uml");

    /** Default constructor.
     */
    protected AbstractUmlModelFactory() {
    }

    /**
     * Initialized some new modelelement o
     * @param o The new modelelement
     */
    protected void initialize(Object o) {
        if (o instanceof MBase) {
            if (((MBase) o).getUUID() == null) {
                ((MBase) o).setUUID(UUIDManager.SINGLETON.getNewUUID());
            }            
            addListenersToModelElement(o);
            UmlModelEventPump pump = UmlModelEventPump.getPump();
            EventListenerList[] lists =
                pump.getClassListenerMap().getListenerList(o.getClass());
            for (int i = 0; i < lists.length; i++) {
                Object[] listenerList = lists[i]._listenerList;
                for (int j = 0; j < listenerList.length; j += 3) {
                    pump.addModelEventListener(
                        listenerList[j + 2],
                        o,
                        (String) listenerList[j + 1]);
                }
            }
        }
    }

    /**
     * Turns the flag for the gui on/off. Default the gui is on. Needed for test
     * modus.
     * @param gui flag to turn gui on off
     */
    public void setGuiEnabled(boolean gui) {
        GuiEnabled = gui;
    }

    /**
     * Checks if the gui is enabled.
     * @return the gui flag
     */
    public boolean isGuiEnabled() {
        return GuiEnabled;
    }

    /**
     * Adds all interested (and centralized) listeners to the given modelelement
     * handle.
     * @param handle the modelelement the listeners are interested in
     */
    public void addListenersToModelElement(Object handle) {
        if (handle instanceof MBase) {
            MBase base = (MBase) handle;
            UmlModelEventPump pump = UmlModelEventPump.getPump();
            ((MBase) handle).addMElementListener(pump);
            if (GuiEnabled) {
                ((MBase) handle).addMElementListener(
                    ProjectBrowser.getInstance().getNavigatorPane());
            }
            ((MBase) handle).addMElementListener(
                UmlModelListener.getInstance());
        }
    }

}
