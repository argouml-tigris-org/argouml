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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * Loads model data from a Zip file
 * 
 * @author maurelio1234
 */
public class ZipModelLoader extends StreamModelLoader {

    private static final Logger LOG = Logger.getLogger(ZipModelLoader.class);

   
    public Collection loadModel(ProfileReference reference) 
        throws ProfileException {
        LOG.info("Loading profile from ZIP '" + reference.getPath() + "'");

        InputStream is = null;
        File modelFile = new File(reference.getPath());
        // TODO: This is in the wrong place.  It's not profile specific.
        // It needs to be moved to main XMI reading code. - tfm 20060326
        if (reference.getPath().endsWith("zip")) {
            String filename = modelFile.getName();
            String extension = filename.substring(filename.indexOf('.'),
                    filename.lastIndexOf('.'));
            String path = modelFile.getParent();
            // Add the path of the model to the search path, so we can
            // read dependent models
            if (path != null) {
                System.setProperty("org.argouml.model.modules_search_path",
                        path);
            }
            try {
                is = openZipStreamAt(modelFile.toURI().toURL(), extension);
            } catch (MalformedURLException e) {
                LOG.error("Exception while loading profile '"
                        + reference.getPath() + "'", e);
                throw new ProfileException(e);
            } catch (IOException e) {
                LOG.error("Exception while loading profile '"
                        + reference.getPath() + "'", e);
                throw new ProfileException(e);
            }

            if (is != null) {
                return super.loadModel(is, reference.getPublicReference());
            }
        }
        
        throw new ProfileException("Profile could not be loaded!");
    }

    /**
     * Open a ZipInputStream to the first file found with a given extension.
     *
     * TODO: Remove since this is a duplicate of ZipFilePersister method
     * when we have refactored the Persister subsystem.
     *
     * @param url
     *            The URL of the zip file.
     * @param ext
     *            The required extension.
     * @return the zip stream positioned at the required location.
     * @throws IOException
     *             if there is a problem opening the file.
     */
    private ZipInputStream openZipStreamAt(URL url, String ext)
        throws IOException {
        ZipInputStream zis = new ZipInputStream(url.openStream());
        ZipEntry entry = zis.getNextEntry();
        while (entry != null && !entry.getName().endsWith(ext)) {
            entry = zis.getNextEntry();
        }
        return zis;
    }

}
