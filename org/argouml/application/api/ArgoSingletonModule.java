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

/**  An interface which identifies an ArgoUML plug-in which behaves
 *   as a singleton, but may be swapped with another plugin singleton
 *   of the same type.  Only one singleton module can be considered
 *   active at the same time.
 *   
 *   @author Will Howery
 *   @author Thierry Lach
 *   @since  0.9.4
 */
public interface ArgoSingletonModule extends ArgoModule {

    /** Allows verification that the current singleton can be
     *  activated.  This is useful for determining menu entry
     *  availability, among other things.
     */
    public boolean canActivateSingleton();

    /** Allows verification that the current singleton can be
     *  deactivated.  This is useful for determining menu entry
     *  availability, among other things.
     */
    public boolean canDeactivateSingleton();

    /** Callback by which the active singleton is notified that it
     *  is being deactivated.  This is called prior to calling
     *  activateSingleton() on the new singleton.
     *
     *  After the call to deactivateSingleton() and
     *  before the call to activateSingleton(), the previously
     *  active singleton is considered to be the active singleton
     *  even though it is not marked as active.
     */
    public void deactivateSingleton();

    /** Callback by which the singleton being activated is notified that it
     *  is being activated.  This is called after calling
     *  deactivateSingleton() on the previous singleton.
     */
    public void activateSingleton();

    public Class getSingletonType();

} /* end interface ArgoSingletonModule */

