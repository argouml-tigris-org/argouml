// $Id: ProfileUML.java 13040 2007-07-10 20:00:25Z linus $
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

package org.argouml.profile.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.FormatingStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;

/**
 * This class represents the default UML profile
 *
 * @author Marcos Aurélio
 */
public class ProfileUML extends Profile {
    
    private static final String PROFILE_FILE = "/org/argouml/default-uml14.xmi";

    static final String NAME = "UML 1.4";
    
    private FormatingStrategy formatingStrategy;
    private ProfileModelLoader profileModelLoader;
    private Collection model;
    
    /**
     * The default constructor for this class 
     * @throws ProfileException 
     */
    @SuppressWarnings("unchecked")
    ProfileUML() throws ProfileException {
        formatingStrategy = new JavaFormatingStrategy();
	profileModelLoader = new ResourceModelLoader();
	model = profileModelLoader
		.loadModel(PROFILE_FILE);

        if (model == null) {
            model = new ArrayList();
            model.add(Model.getModelManagementFactory().createModel());
        }        
    }    

    
    /**
     * @return the Java formating strategy
     * @see org.argouml.uml.profile.Profile#getFormatingStrategy()
     */
    public FormatingStrategy getFormatingStrategy() {
	return formatingStrategy;
    }

    /*
     * @return "UML 1.4"
     * @see org.argouml.uml.profile.Profile#getDisplayName()
     */
    public String getDisplayName() {
	return NAME;
    }

    /*
     * @see org.argouml.uml.profile.Profile#getProfilePackages()
     */
    @Override
    public Collection getProfilePackages() throws ProfileException {
        return model;
    }
    
    /*
     * @see org.argouml.uml.profile.Profile#getDefaultTypeStrategy()
     */
    @Override
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return new DefaultTypeStrategy() {
            public Object getDefaultAttributeType() {
                return ModelUtils.findTypeInModel("Integer", model.iterator()
                        .next());
            }

            public Object getDefaultParameterType() {
                return ModelUtils.findTypeInModel("Integer", model.iterator()
                        .next());
            }

            public Object getDefaultReturnType() {
                return null;
            }

        };
    }
}
