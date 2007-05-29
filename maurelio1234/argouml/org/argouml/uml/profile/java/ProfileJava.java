// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.profile.java;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;
import org.argouml.uml.profile.FormatingStrategy;
import org.argouml.uml.profile.Profile;
import org.argouml.uml.profile.ProfileModelLoader;
import org.argouml.uml.profile.FileModelLoader;
import org.argouml.uml.profile.ResourceModelLoader;

/**
 * This class implements the abstract class Profile for use in modelling
 * Java language projects.  Eventually, this class may be replaced by
 * a configurable profile.
 *
 * TODO: (MVW) Document the use of "argo.defaultModel" in
 * the argo.user.properties file.
 * 
 * @author Marcos Aurélio
 */
public class ProfileJava extends Profile {

    private FormatingStrategy formatingStrategy;
    private ProfileModelLoader profileModelLoader;
    private Object model;

    public ProfileJava() {
	    formatingStrategy = new JavaFormatingStrategy();
	    profileModelLoader = new ResourceModelLoader();
	    model = profileModelLoader.loadModel("/org/argouml/model/mdr/profiles/default-uml14.xmi");
	    
	    if (model == null) {
		model = Model.getModelManagementFactory().createModel();
	    }
    }    

    @Override
    public FormatingStrategy getFormatingStrategy() {
	return formatingStrategy;
    }

    @Override
    public Object getModel() {
	return model;
    }

    @Override
    public String getDisplayName() {
	return "Java";
    }

    @Override
    public FigNodeStrategy getFigureStrategy() {
	return null;
    }
    
}
