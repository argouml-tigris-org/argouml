// $Id: ResourceModelLoader.java 13040 2007-07-10 20:00:25Z linus $
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

package org.argouml.uml.profile;

import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * Loads models using the default class loader or a provided one.
 *
 * @author maurelio1234
 */
public class ResourceModelLoader extends StreamModelLoader {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
	    .getLogger(ResourceModelLoader.class);
    
    private Class classloader;
    
    /**
     * The default constructor for this class. Loads resources from the same 
     * classloader that loaded this class.
     */
    public ResourceModelLoader() {
	this.classloader = this.getClass();
    }
    
    /**
     * Loads resources from the classloader that loaded the given class
     * 
     * @param c the reference class
     */
    public ResourceModelLoader(Class c) {
	this.classloader = c;
    }

    /**
     * @param path the model path
     * @return the model
     * @throws ProfileException if the profile could not be loaded 
     * @see org.argouml.uml.profile.StreamModelLoader#loadModel(java.lang.String)
     */
    public Collection loadModel(String path) throws ProfileException {
        LOG.info("Loading profile from resource'" + path + "'");
        return super.loadModel(this.classloader.getResourceAsStream(path));
    }

}
