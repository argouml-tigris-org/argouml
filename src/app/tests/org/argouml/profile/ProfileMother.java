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

package org.argouml.profile;

import static org.argouml.model.Model.getCoreHelper;
import static org.argouml.model.Model.getExtensionMechanismsFactory;
import static org.argouml.model.Model.getExtensionMechanismsHelper;
import static org.argouml.model.Model.getFacade;
import static org.argouml.model.Model.getModelManagementFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import org.argouml.model.Model;
import org.argouml.model.XmiWriter;
import org.argouml.persistence.UmlFilePersister;

/**
 * Based on the 
 * <a href="http://www.xpuniverse.com/2001/pdfs/Testing03.pdf">ObjectMother 
 * design pattern</a>, provides reusable facilities to create profiles.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class ProfileMother {
    
    private static final Logger LOG = Logger.getLogger(ProfileMother.class);

    /**
     * "profile"
     */
    public static final String STEREOTYPE_NAME_PROFILE = "profile";
    /**
     * "st" the example stereotype name.
     */
    public static final String STEREOTYPE_NAME_ST = "st";

    public Object createSimpleProfileModel() {
        Object model = getModelManagementFactory().createModel();
        Object profileStereotype = getProfileStereotype();
        getCoreHelper().addStereotype(model, profileStereotype);
        Object fooClass = Model.getCoreFactory().buildClass("foo", model);
        getExtensionMechanismsFactory().buildStereotype(fooClass, 
                STEREOTYPE_NAME_ST, model);
        return model;
    }

    Object getProfileStereotype() {
        Object umlProfile = getUmlProfileModel();
        Collection<Object> models = new ArrayList<Object>();
        models.add(umlProfile);
        Collection stereotypes = getExtensionMechanismsHelper().getStereotypes(
            models);
        for (Object stereotype : stereotypes) {
            if (STEREOTYPE_NAME_PROFILE.equals(
                    Model.getFacade().getName(stereotype)))
                return stereotype;
        }
        return null;
    }
    
    private Object umlProfileModel;

    Object getUmlProfileModel() {
        if (ProfileFacade.isInitiated()) {
            try {
                umlProfileModel = ProfileFacade.getManager().getUMLProfile().
                    getProfilePackages().iterator().next();
                TestCase.assertTrue(getFacade().isAModel(umlProfileModel));
            } catch (ProfileException e) {
                LOG.error("Exception", e);
            }
        }
        if (umlProfileModel == null) {
            umlProfileModel = getModelManagementFactory().createModel();
            getExtensionMechanismsFactory().buildStereotype(
                umlProfileModel, STEREOTYPE_NAME_PROFILE, umlProfileModel);
        }
        return umlProfileModel;
    }

    public void saveProfileModel(Object model, File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        try {
            XmiWriter xmiWriter = Model.getXmiWriter(model, fileOut, "x(" 
                    + UmlFilePersister.PERSISTENCE_VERSION + ")");
            xmiWriter.write();
            fileOut.flush();
        } catch (Exception e) {
            String msg = "Exception while saving profile model.";
            LOG.error(msg, e);
            throw new IOException(msg);
        } finally {
            fileOut.close();
        }
    }

}
