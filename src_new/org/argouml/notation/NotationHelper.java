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

package org.argouml.notation;

import org.argouml.application.api.Configuration;

/**
 * A helper for notation related functions. Currently, it only contains
 * 2 functions to obtain the guillemet characters or
 * their double bracket alternative,
 * based on the choice made by the user in the Settings menu.
 */
public final class NotationHelper {
    /**
     * The constructor.
     */
    private NotationHelper() {
    }

    /**
     * @return the left pointing guillemot, i.e. << or the one-character symbol
     */
    public static String getLeftGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00ab"
	    : "<<";

    }

    /**
     * @return the right pointing guillemot, i.e. >> or the one-character symbol
     */
    public static String getRightGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00bb"
	    : ">>";
    }

} /* end class NotationHelper */
