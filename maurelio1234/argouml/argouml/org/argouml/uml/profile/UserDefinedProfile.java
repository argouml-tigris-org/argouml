// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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

import java.io.File;

/**
 * Represents a profile defined by the user
 *
 * @author Marcos Aurélio
 */
public class UserDefinedProfile extends Profile {

    private String displayName;
    private File modelFile;
    private ProfileModelLoader modelLoader;
    
    /**
     * The default constructor for this class
     * 
     * @param file the file from where the model should be read  
     */
    public UserDefinedProfile(File file) {
	this.displayName = file.getName();
	this.modelLoader = new FileModelLoader();
	this.modelFile = file;
    }
    
    /**
     * @return the string that should represent this profile in the GUI.
     * 
     * @see org.argouml.uml.profile.Profile#getDisplayName()
     */
    public String getDisplayName() {
	return displayName;
    }

    /**
     * @return null
     * @see org.argouml.uml.profile.Profile#getFormatingStrategy()
     */
    public FormatingStrategy getFormatingStrategy() {
	return null;
    }

    /**
     * @return the model loaded from the filesystem
     * @see org.argouml.uml.profile.Profile#getModel()
     */
    public Object getModel() {
	return modelLoader.loadModel(modelFile.getPath());
    }

    /**
     * @return null
     * @see org.argouml.uml.profile.Profile#getFigureStrategy()
     */
    public FigNodeStrategy getFigureStrategy() {
	return null;
    }

    /**
     * @return the file passed at the constructor
     */
    public File getModelFile() {
        return modelFile;
    }
}
