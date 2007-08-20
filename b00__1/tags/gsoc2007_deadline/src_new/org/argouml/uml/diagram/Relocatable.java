// $Id:Relocatable.java 12910 2007-06-27 15:36:24Z mvw $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

/**
 * Interface which is used by the Explorer to determine if a diagram can
 * change its location to a new model element.
 *
 * @author MarkusK
 *
 */
public interface Relocatable {

    /**
     * This function should return true if it is allowed to relocate
     * this type of diagram to the given modelelement.
     *
     * @param base the given modelelement
     * @return true if adding a diagram here is allowed
     */
    boolean isRelocationAllowed(Object base);

    /**
     * Relocate this diagram,
     * e.g. for a class diagram assign it a new namespace,
     * e.g. for a statechart move it together with the
     * statemachine to a new operation/classifier. <p>
     *
     * Precondition: isRelocationAllowed(base) is true.
     *
     * @param base the new location, i.e. base modelelement
     * @return true if successful
     */
    boolean relocate(Object base);

}
