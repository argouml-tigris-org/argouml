// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.application.api;

import javax.swing.*;

/** Encapsulates specific attributes about a notation.
 *
 *  @author Thierry Lach
 *  @since  ARGO0.9.4
 */
public interface NotationName {

    /** Returns the name of the language.
     */
    public String getName();

    /** Returns the version of the language if applicable, otherwise null
     */
    public String getVersion();

    /** Returns a textual title for the notation.
     */
    public String getTitle();

    /** Returns an icon for the notation, or null if no icon is available.
     */
    public Icon getIcon();

    /** Returns the text string stored in the configuration
     *  for this notation.  This string is used to determine
     *  notation equality in {@link #equals(NotationName)}.
     *
     *  This should incorporate both the name and the version if one
     *  exists.
     */
    public String getConfigurationValue();

    /** Ease-of-use helper for usage in swing.
     *  Usually, it is equal to <code>getConfigurationValue()</code>.
     */
    public String toString();

    /** Ease-of-use helper to determine
     *  if this notation is the same as another.
     *  This must be equivalent to
     *  <code>getConfigurationValue().equals(nn.getConfigurationValue())</code>
     */
    public boolean equals(NotationName nn);
}
