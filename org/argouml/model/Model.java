// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.model;

/**
 * This is the root class of the Model subsystem. All other subsystems
 * can retreive the correct version of the API from this class.<p>
 * 
 * Currently these API:s are hard-coded to work with NSUML but eventually
 * another model component will replace them and then all these API:s
 * will instead work against that component.<p>
 *
 * Notice that all API:s returned from this class are to be NSUML-free.<p>
 *  
 * @stereotype utility
 * @since 0.15.5
 * @author Linus Tolke
 */
public final class Model {
    /**
     * The ModelEventPump instance.
     */
    private static ModelEventPump thePumpInstance;

    /**
     * Get the event pump.
     * 
     * @return the current ModelEventPump.
     */
    public static synchronized ModelEventPump getPump() {
        if (thePumpInstance == null) {
            thePumpInstance = new NSUMLModelEventPump();
        }
        return thePumpInstance;
    }
}

