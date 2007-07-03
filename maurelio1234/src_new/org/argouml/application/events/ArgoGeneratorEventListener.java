// $Id: ArgoGeneratorEventListener.java 10736 2006-06-11 17:30:38Z mvw $
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

package org.argouml.application.events;
import org.argouml.application.api.ArgoEventListener;

/** An interface that objects interested in GeneratorEvent
 *  notifications must extend.
 *
 *  @author Thierry Lach
 *  @since 0.9.4
 */

public interface ArgoGeneratorEventListener extends ArgoEventListener {

    /**
     * Invoked when any aspect of the generator has been changed.
     * @param e <code>ArgoGeneratorEvent</code> describing the change.
     */
    public void generatorChanged(ArgoGeneratorEvent e);

    /**
     * Invoked when a CodeGenerator has been added.
     * @param e <code>ArgoGeneratorEvent</code> describing the added notation.
     */
    public void generatorAdded(ArgoGeneratorEvent e);

    /**
     * Invoked when a CodeGenerator has been removed.
     * @param e <code>ArgoGeneratorEvent</code> describing the removed notation.
     */
    public void generatorRemoved(ArgoGeneratorEvent e);

}

