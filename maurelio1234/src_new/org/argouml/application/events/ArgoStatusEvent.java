// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.application.events;

/**
 * The Status Event is used to notify interested parties of a status
 * change.  The status can be arbitrary text, or the name of a project
 * which was saved or loaded.
 *
 * @author Tom Morris  <tfmorris@gmail.com>
 */
public class ArgoStatusEvent extends ArgoEvent {

    private String text;

    /**
     * @param eventType reported by this event.
     * @param src object that caused the event.
     * @param message the status text string (already translated) to be shown
     */
    public ArgoStatusEvent(int eventType, Object src, String message) {
        super(eventType, src);
        text = message;
    }

    /**
     * Indicates the start of the 100-digit range for status events.
     *
     * @return the first id reserved for events.
     */
    @Override
    public int getEventStartRange() {
        return ANY_STATUS_EVENT;
    }

    /**
     * @return Returns the event text containing either a status message or
     * the name of the project that was saved/loaded.
     */
    public String getText() {
        return text;
    }
}
