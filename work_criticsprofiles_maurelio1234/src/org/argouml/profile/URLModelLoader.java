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

import java.net.URL;
import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;

/**
 * Abstract ProfileModelLoader which loads models from a URL.
 *
 * @author Tom Morris
 */
public class URLModelLoader implements ProfileModelLoader {

    //private static final Logger LOG = Logger.getLogger(URLModelLoader.class);

    /**
     * @param url the url/system id to load
     * @param publicId the publicId for which the model will be known - must be 
     * equal in different machines in order to be possible to load the model.
     * @return the model
     * @throws ProfileException if the XMIReader couldn't read the profile
     */
    public Collection loadModel(URL url, URL publicId) 
        throws ProfileException {
        if (url == null) {
            throw new ProfileException("Null profile URL");
        }
        try {
            XmiReader xmiReader = Model.getXmiReader();
            InputSource inputSource = new InputSource(url.toExternalForm());
            inputSource.setPublicId(publicId.toString());
            Collection elements = xmiReader.parse(inputSource, true);
            return elements;
        } catch (UmlException e) {
            throw new ProfileException("Invalid XMI data!", e);
        }
    }

    public Collection loadModel(ProfileReference reference) throws ProfileException {
        return loadModel(reference.getPublicReference(), reference.getPublicReference());
    }
}
