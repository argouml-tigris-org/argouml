// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.profile;

/**
 * The <a href="http://en.wikipedia.org/wiki/Facade_pattern">Facade</a> of the 
 * profile subsystem. 
 * It provides a simplified interface to the subsystem, and access to objects 
 * of the subsystem when the methods it provides directly aren't enough.
 *
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.25.4
 */
public class ProfileFacade {

    /**
     * Register a profile in the {@link ProfileManager}.
     * @param profile the profile to be registered
     */
    public static void register(Profile profile) {
        getManager().registerProfile(profile);
    }

    /**
     * Remove or unregister the profile from the {@link ProfileManager}.
     * @param profile the profile to be removed
     */
    public static void remove(Profile profile) {
        getManager().removeProfile(profile);
    }

    /**
     * @return the profile manager
     */
    public static ProfileManager getManager() {
        if (manager == null)
            notInitialized("manager");
        return manager;
    }

    private static void notInitialized(String string) {
        throw new RuntimeException("ProfileFacade's " + string  
                + " isn't initialized!");
    }

    /**
     * @param profileManager the manager of the profiles to be used
     */
    public static void setManager(ProfileManager profileManager) {
        manager = profileManager;
    }

    static void reset() {
        manager = null;
    }

    private static ProfileManager manager;

    /**
     * @return true is subsystem is initialized or false otherwise
     */
    public static boolean isInitiated() {
        return manager != null;
    }
    
}
