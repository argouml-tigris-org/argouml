// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.argouml.profile.CoreProfileReference;
import org.argouml.profile.FileModelLoader;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ResourceModelLoader;
import org.argouml.profile.UserProfileReference;

/**
 * Tests for the {@link ProfileConfigurationFilePersister} class.
 * 
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileConfigurationFilePersister extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Demonstrates that XmiWriterMDRImpl fails to write a profile 
     * (i.e., the file will contain no model) previously loaded from a file.
     * 
     * @throws ProfileException if the loading of the profile fails.
     * @throws IOException if an IO error occurs while writing or reading a 
     * file.
     * @throws FileNotFoundException if a file isn't found.
     * @throws UmlException if an error occurs while writing the profile in 
     * XMI format.
     */
    public void testWritePreviouslyLoadedProfile() 
        throws ProfileException, FileNotFoundException, IOException, 
        UmlException {
        
        ProfileModelLoader loader = new ResourceModelLoader();
        Collection models = loader.loadModel(
                new CoreProfileReference("default-uml14.xmi"));
        Object umlModel = models.iterator().next();
        final String umlModelName = Model.getFacade().getName(umlModel);
        assertNotNull(umlModelName);
        File tempFile = File.createTempFile(umlModelName, ".xmi");
        FileOutputStream stream = new FileOutputStream(tempFile);
        XmiWriter xmiWriter = Model.getXmiWriter(umlModel, stream, "version-x");
        xmiWriter.write();
        stream.close();
        FileModelLoader fileModelLoader = new FileModelLoader();
        Collection loadedModel = fileModelLoader.loadModel(
                new UserProfileReference(tempFile.getPath()));
        assertEquals(1, loadedModel.size());
        umlModel = loadedModel.iterator().next();
        assertEquals(umlModelName, Model.getFacade().getName(umlModel));
    }

}
