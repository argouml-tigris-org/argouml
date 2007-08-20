// $Id:OffenderXMLHelper.java 11098 2006-08-28 10:18:23Z bobtarling $
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

package org.argouml.persistence;

/**
 * Helper class to make an offender fit right in the XML file during save.<p>
 *
 * An offender is a subject that is (partly) responsible for trigging a
 * critic. These need to be saved to keep track of in which contexts a
 * critic has been resolved.
 * Used by todo.tee
 * This is not to be considered as part of the peristence interface.
 *
 * @author	Michael Stockman
 */
public class OffenderXMLHelper {
    /**
     * A description of the offender.
     */
    private final String item;

    /**
     * Creates a new OffenderXMLHelper describing the offender
     * described by item.
     *
     * @param	offender	A description of an offender.
     * @throws	IllegalArgumentException if item is null.
     */
    public OffenderXMLHelper(String offender) {
        if (offender == null) {
            throw new IllegalArgumentException(
                    "An offender string must be supplied");
        }
        item = offender;
    }

    /**
     * Gets the description of this offender.
     *
     * @return	The description as a String.
     */
    public String getOffender() {
        return item;
    }
}

