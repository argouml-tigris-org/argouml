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

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;

/**
 * Abstract ProfileModelLoader which loads models from an InputStream.
 *
 * @author Marcos Aurélio
 */
public abstract class StreamModelLoader implements ProfileModelLoader {

    private static final Logger LOG = Logger.getLogger(StreamModelLoader.class);

    /**
     * @param inputStream the stream from where the model should be loaded
     * @return the model
     * @throws ProfileException if the XMIReader couldn't read the input stream
     */
    @Deprecated
    public Collection loadModel(InputStream inputStream) 
        throws ProfileException {
        
        if (inputStream != null) {
            try {
                XmiReader xmiReader = Model.getXmiReader();
                InputSource inputSource = new InputSource(inputStream);
                Collection elements = xmiReader.parse(inputSource, true);
                return elements;
            } catch (UmlException e) {
                LOG.error("Exception while loading profile ", e);
                throw new ProfileException("Invalid XMI data!");
            }
        }
        LOG.error("Profile not found");
        throw new ProfileException("Profile not found!");
    }

    /**
     * @param inputStream the stream from where the model should be loaded
     * @param publicReference the URL to be used as the public reference of 
     * the profile that will be loaded.
     * @return the model
     * @throws ProfileException if the XMIReader couldn't read the input stream
     */
    public Collection loadModel(InputStream inputStream, URL publicReference)
        throws ProfileException {
        
        if (inputStream != null) {
            try {
                XmiReader xmiReader = Model.getXmiReader();
                InputSource inputSource = new InputSource(inputStream);
                inputSource.setPublicId(publicReference.toString());
                Collection elements = xmiReader.parse(inputSource, true);
                return elements;
            } catch (UmlException e) {
                LOG.error("Exception while loading profile ", e);
                throw new ProfileException("Invalid XMI data!");
            }
        }
        LOG.error("Profile not found");
        throw new ProfileException("Profile not found!");
    }
}
