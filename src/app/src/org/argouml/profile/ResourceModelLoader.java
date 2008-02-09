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

import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * Loads models using the default ClassLoader or a provided one.
 *
 * @author maurelio1234
 */
public class ResourceModelLoader extends URLModelLoader {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(ResourceModelLoader.class);
    
    private Class clazz;
    
    /**
     * The default constructor for this class. Loads resources from the same 
     * ClassLoader that loaded this class.
     */
    public ResourceModelLoader() {
        this.clazz = this.getClass();
    }
    
    /**
     * Loads resources from the ClassLoader that loaded the given class
     * 
     * @param c the reference class
     */
    public ResourceModelLoader(Class c) {
        clazz = c;
    }


    public Collection loadModel(String path) throws ProfileException {
        LOG.info("Loading profile from resource'" + path + "'");
        return super.loadModel(clazz.getResource(path));
    }

}
