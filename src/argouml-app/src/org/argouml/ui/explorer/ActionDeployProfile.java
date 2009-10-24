// $Id$
// Copyright (c) 2009 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFileView;
import org.argouml.persistence.UmlFilePersister;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.util.ArgoFrame;

/**
 * Explorer action for deploying a user editable profile. This is only needed
 * since UML2. An undeployed editable profile cannot be applied. A deployed
 * profile is no more editable, but can be applied to a model.
 *
 * @author Thomas Neustupny
 */
public class ActionDeployProfile extends AbstractAction {

    private static final Logger LOG = Logger
            .getLogger(ActionDeployProfile.class);

    private Object undeployedProfile;

    /**
     * Default Constructor
     * 
     * @param profile the selected profile
     */
    public ActionDeployProfile(Object profile) {
        //super(Translator.localize("action.deploy-profile"));
        super ("Deploy Profile");
        undeployedProfile = profile;
    }

    public void actionPerformed(ActionEvent arg0) {
        Object profile = Model.getExtensionMechanismsHelper()
            .makeProfileApplicable(undeployedProfile);
        LOG.debug("REMOVE ME: " + profile.toString());
        // TODO: save it, maybe put it into the profile configuration
    }
}
