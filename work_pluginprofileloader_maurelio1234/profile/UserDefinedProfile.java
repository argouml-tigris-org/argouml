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
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.Collection;

/**
 * Represents a profile defined by the user
 *
 * @author Marcos Aur�lio
 */
public class UserDefinedProfile extends Profile {

    private String displayName;
    private File modelFile;
    private Collection model;
    private boolean fromZargo;

    /**
     * The default constructor for this class
     * 
     * @param file the file from where the model should be read  
     * @throws ProfileException if the profile could not be loaded
     */
    public UserDefinedProfile(File file) throws ProfileException {
        displayName = file.getName();
        modelFile = file;
        ProfileReference reference = null;
        try {
            reference = new UserProfileReference(file.getPath());
        } catch (MalformedURLException e) {
            throw new ProfileException(
                "Failed to create the ProfileReference.", e);
        }
        model = new FileModelLoader().loadModel(reference);
        fromZargo = false;
    }

    
    /**
     * A constructor that takes a file name and a reader, being the reader the 
     * input method to get the profile model.
     * 
     * @param fileName name of the profile model file.
     * @param reader a reader opened from where the profile model will be 
     * loaded. 
     * @throws ProfileException if something goes wrong in initializing the 
     * profile.
     */
    public UserDefinedProfile(String fileName, Reader reader) 
        throws ProfileException {
        displayName = fileName;
        ProfileReference reference = null;
        try {
            reference = new UserProfileReference(fileName);
        } catch (MalformedURLException e) {
            throw new ProfileException(
                "Failed to create the ProfileReference.", e);
        }
        model = new ReaderModelLoader(reader).loadModel(reference);
        fromZargo = true;
    }


    /**
     * @return the string that should represent this profile in the GUI. An
     *         start (*) is placed on it if it comes from the currently opened
     *         zargo file.
     */
    public String getDisplayName() {
        return displayName + (fromZargo ? "*" : "");
    }


    /**
     * Returns null.  This profile has no formatting strategy.
     * @return null.
     */
    @Override
    public FormatingStrategy getFormatingStrategy() {
        return null;
    }

    /**
     * Returns null.  This profile has no figure strategy.
     * @return null.
     */
    @Override
    public FigNodeStrategy getFigureStrategy() {
        return null;
    }

    /**
     * @return the file passed at the constructor
     */
    public File getModelFile() {
        return modelFile;
    }

    /**
     * @return the name of the model and the file name
     */
    @Override
    public String toString() {
        // TODO: I18N
        return super.toString() + " [" + getModelFile() + "]";
    }


    @Override
    public Collection getProfilePackages() {
        return model;
    }
}
