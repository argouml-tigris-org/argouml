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

package org.argouml.uml.profile;

import java.util.HashSet;
import java.util.Set;



/**
 * Represents a Profile
 * 
 * @author Marcos Aurélio
 */
public abstract class Profile {
    
    private Set<Profile> importedProfiles  = new HashSet<Profile>();
    private Set<Profile> importingProfiles = new HashSet<Profile>();
        
    /**
     * A profile should call this method to state that it depends of <code>p</code>
     * 
     * @param p the profile
     * @throws IllegalArgumentException if there is some cycle on the dependency graph
     */
    protected final void addProfileDependency(Profile p) throws IllegalArgumentException {
        System.out.println(this + " ## ADDING PROFILE DEPENDENCY TO " + p);
        
        if (importingProfiles.contains(p)) {
            throw new IllegalArgumentException();
        } else {
            importedProfiles.add(p);
            importedProfiles.addAll(p.importedProfiles);

            for (Profile importedProfile : importedProfiles) {
                importedProfile.importingProfiles.add(this);
            }
        }

        System.out.println("IMPORTED PROFILES: " + importedProfiles);
        System.out.println("IMPORTING PROFILES: " + importingProfiles);
    }    

    /**
     * @return the dependencies
     */
    public final Set<Profile> getDependencies() {
        return importedProfiles;
    }
    
    /**
     * @return the name for this profile 
     */
    public abstract String getDisplayName();
    
    /**
     * @return the UML Model that contains the profile model
     */
    public abstract Object getModel();

    /**
     * @return the formating strategy offered by this profile, if any. <br>
     * 	       <b>null</b> if this profile has no formating strategy.
     */
    public abstract FormatingStrategy getFormatingStrategy();
    
    /**
     * @return the FigNodeStrategy offered by this profile, if any. <br>
     * 	       <b>null</b> if this profile has no FigNodeStrategy.
     */
    public abstract FigNodeStrategy   getFigureStrategy();

    /**
     * @return the display name
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getDisplayName();
    }
}
