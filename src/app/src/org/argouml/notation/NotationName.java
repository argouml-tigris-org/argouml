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

import javax.swing.Icon;

/**
 * Encapsulates specific attributes about a notation.
 *
 * @author Thierry Lach
 * @since  ARGO0.9.4
 */
public interface NotationName {

    /**
     * Returns the name of the language.
     *
     * @return the language name.
     */
    String getName();

    /**
     * Returns the version of the language if applicable, otherwise
     * <code>null</code>.
     *
     * @return The language version or <code>null</code>.
     */
    String getVersion();

    /**
     * Returns a textual title for the notation. The name and version.
     *
     * @return a title for the language.
     */
    String getTitle();

    /**
     * Returns an icon for the notation, or <code>null</code> if no icon
     * is available.
     *
     * @return An {@link Icon} for the language or <code>null</code>.
     */
    Icon getIcon();

    /**
     * Returns the text string stored in the configuration
     * for this notation.  This string is used to determine
     * notation equality in {@link #sameNotationAs(NotationName)}.<p>
     *
     * This should incorporate both the name and the version if one
     * exists.
     *
     * @return the value for this notation to be stored in the configuration.
     */
    String getConfigurationValue();

    /**
     * Ease-of-use helper for usage in swing.
     * Usually, it is equal to <code>getConfigurationValue()</code>.
     *
     * @return a String representation of the notation name.
     */
    String toString();

    /**
     * Ease-of-use helper to determine
     * if this notation is the same as another.
     * This must be equivalent to
     * <code>getConfigurationValue().equals(nn.getConfigurationValue())</code>
     *
     * @param notationName to compare
     * @return boolean indicating equality or inequality.
     */
    boolean sameNotationAs(NotationName notationName);
}
