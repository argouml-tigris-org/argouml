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

import static org.argouml.model.Model.getExtensionMechanismsHelper;
import static org.argouml.model.Model.getFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

import junit.framework.TestCase;

/**
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileMother extends TestCase {
    
    private ProfileMother mother;
    private File testDir;

    @Override
    protected void setUp() throws Exception {
        InitializeModel.initializeDefault();
        mother = new ProfileMother();
    }
    
    public void testCreateProfileModel() {
        Object model = mother.createSimpleProfileModel();
        assertNotNull(model);
        Collection profileStereotypes = getFacade().getStereotypes(model);
        assertEquals(1, profileStereotypes.size());
        assertEquals(ProfileMother.STEREOTYPE_NAME_PROFILE, 
            getFacade().getName(profileStereotypes.iterator().next()));
    }
    
    public void testCreateSimpleProfileModel() throws Exception {
        final Object model = mother.createSimpleProfileModel();
        Collection<Object> models = new ArrayList<Object>() { {
                add(model);
            }
        };
        Collection stereotypes = 
            getExtensionMechanismsHelper().getStereotypes(models);
        Object st = null;
        for (Object stereotype : stereotypes) {
            if (ProfileMother.STEREOTYPE_NAME_ST.equals(
                    getFacade().getName(stereotype))) {
                st = stereotype;
                break;
            }
        }
        assertNotNull("\"st\" stereotype not found in model.", st);
        assertTrue(Model.getExtensionMechanismsHelper().isStereotype(st, 
                ProfileMother.STEREOTYPE_NAME_ST, "Class"));
    }
    
    public void testSaveProfileModel() throws Exception {
        Object model = mother.createSimpleProfileModel();
        File file = new File(testDir, "testSaveProfileModel.xmi");
        mother.saveProfileModel(model, file);
        assertTrue("The file to where the file was supposed to be saved " 
                + "doesn't exist.", file.exists());
    }
    
}
