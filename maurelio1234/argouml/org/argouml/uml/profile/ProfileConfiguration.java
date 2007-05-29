// $Id: Profile.java 11516 2006-11-25 04:30:15Z tfmorris $
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


package org.argouml.uml.profile;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.argouml.uml.profile.java.ProfileJava;
import org.tigris.gef.presentation.FigNode;
/**
 *   This class captures the configurable behavior of Argo.
 *
 *   @author Marcos Aurélio
 */
public class ProfileConfiguration {

    private FormatingStrategy formatingStrategy;
    private Vector	      figNodeStrategies = new Vector();
    
    private Profile defaultProfile; 
    private Vector profiles = new Vector();
    private Vector profileModels = new Vector();
    
    public ProfileConfiguration() {
	defaultProfile = new ProfileJava();

	formatingStrategy = defaultProfile.getFormatingStrategy();
	
	addProfile(defaultProfile);	
    }
    
    public FormatingStrategy getFormatingStrategy() {
        return formatingStrategy;
    }
    
    public void setFormatingStrategy(FormatingStrategy formatingStrategy) {
        this.formatingStrategy = formatingStrategy;
    }

    public Vector getProfiles() {
        return profiles;
    }

    public Profile getDefaultProfile() {
        return defaultProfile;
    }
    
    public void addProfile(Profile p) {
	profiles.add(p);
	profileModels.add(p.getModel());
	
	FigNodeStrategy fns = p.getFigureStrategy();
	if (fns!=null) {
	    figNodeStrategies.add(p);
	}
    }
    
    public void setDefaultProfile(Profile defaultProfile) {
        this.defaultProfile = defaultProfile;
    }
    
    public Vector getProfileModels() {
        return profileModels;
    }

    public void removeProfile(Profile p) {
	if (p != defaultProfile) {
	    profiles.remove(p);
	    profileModels.remove(p.getModel());
	    
		FigNodeStrategy fns = p.getFigureStrategy();
		if (fns!=null) {
		    figNodeStrategies.remove(p);
		}
	    
	}
    }
    
    private FigNodeStrategy compositeFigNodeStrategy = new FigNodeStrategy() {

	public FigNode getFigNodelForElement(Object element, int x, int y, Map styleAttributes) {
	    FigNode res = null;
	    
	    Iterator it = figNodeStrategies.iterator();
	    while(it.hasNext()) {
		FigNodeStrategy strat = (FigNodeStrategy) it.next();
		res = strat.getFigNodelForElement(element, x, y, styleAttributes);
		if (res != null) {
		    break;		    
		}
	    }
	    return res;
	}
	
    };
    
    public FigNodeStrategy getFigNodeStrategy() {
	return compositeFigNodeStrategy; 
    }
}
