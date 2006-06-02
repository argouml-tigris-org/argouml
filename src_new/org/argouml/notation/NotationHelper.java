// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeEvent;

import org.argouml.application.api.Configuration;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;

/**
 * A helper for notation related functions. <p>
 * 
 * This helper provides: <p>
 * 
 * 2 Functions to obtain the guillemet characters or
 * their double bracket alternative,
 * based on the choice made by the user in the Settings menu. <p>
 * 
 * A default NotationContext implementation that listens to notation changes.
 */
public final class NotationHelper {
    
    private static DefaultNC nc;

    /**
     * The constructor.
     */
    private NotationHelper() {
    }

    /**
     * @return the left pointing guillemot, i.e. << or the one-character symbol
     * @deprecated by MVW in V0.21.3. Use ProjectSettings instead.
     */
    public static String getLeftGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00ab"
	    : "<<";

    }

    /**
     * @return the right pointing guillemot, i.e. >> or the one-character symbol
     * @deprecated by MVW in V0.21.3. Use ProjectSettings instead.
     */
    public static String getRightGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00bb"
	    : ">>";
    }

    public static NotationContext getDefaultNotationContext() {
        if (nc == null) nc = new DefaultNC();
        return nc;
    }
    
    static class DefaultNC implements NotationContext, ArgoNotationEventListener {

        private NotationName currentNotationName;
        
        /**
         * Constructor.
         */
        public DefaultNC() {
            ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
            currentNotationName = Notation.getConfigueredNotation();
        }

        /**
         * @see org.argouml.notation.NotationContext#getContextNotation()
         */
        public NotationName getContextNotation() {
            return currentNotationName;
        }

        /**
         * @see org.argouml.notation.NotationContext#setContextNotation(org.argouml.notation.NotationName)
         */
        public void setContextNotation(NotationName nn) {
            currentNotationName = nn;
        }

        /**
         * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
         */
        public void notationChanged(ArgoNotationEvent event) {
            PropertyChangeEvent changeEvent =
                (PropertyChangeEvent) event.getSource();
            if (changeEvent.getPropertyName().equals("argo.notation.only.uml")) {
                if (changeEvent.getNewValue().equals("true")) {
                    setContextNotation(Notation.getConfigueredNotation());
                }
            } else {
                setContextNotation(
                    Notation.findNotation((String) changeEvent.getNewValue()));
            }
        }

        /**
         * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
         */
        public void notationAdded(ArgoNotationEvent event) {
        }

        /**
         * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
         */
        public void notationRemoved(ArgoNotationEvent event) {
        }

        /**
         * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
         */
        public void notationProviderAdded(ArgoNotationEvent event) {
        }

        /**
         * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
         */
        public void notationProviderRemoved(ArgoNotationEvent event) {
        }

    }
} /* end class NotationHelper */
