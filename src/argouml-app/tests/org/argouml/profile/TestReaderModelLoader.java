/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.profile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.FileHelper;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

/**
 * Tests for the {@link ReaderModelLoader} class.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestReaderModelLoader extends TestCase {
    /**
     * Test the constructor.
     */
    public void testCtor() {
        Reader reader = new StringReader("dummy string");
        new ReaderModelLoader(reader);
    }

    /**
     * Test {@link ReaderModelLoader#loadModel(ProfileReference)}.
     *
     * @throws IOException upon IO errors.
     * @throws ProfileException upon problems with profiles.
     */
    public void testLoad() throws IOException, ProfileException {
        InitializeModel.initializeDefault();
        ProfileMother mother = new ProfileMother();
        Object model = mother.createSimpleProfileModel();
        File testDir = FileHelper.setUpDir4Test(getClass());
        File file = new File(testDir, "testSaveProfileModel.xmi");
        mother.saveProfileModel(model, file);
        Reader reader = new FileReader(file);
        ProfileReference profileReference = new UserProfileReference(
            file.toURL().toExternalForm());
        Collection models = new ReaderModelLoader(reader).
            loadModel(profileReference);
        assertNotNull(models);
        assertTrue(models.size() >= 1);
        Model.getUmlFactory().deleteExtent(models.iterator().next());
    }
}
