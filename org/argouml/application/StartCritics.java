// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.application;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.cognitive.critics.ChildGenUML;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.util.Trash;
import org.argouml.application.api.Argo;

import java.util.Locale;

import ru.novosoft.uml.model_management.MModel;

/** StartCritics is a thread which helps to start the critiquing thread
 */
public class StartCritics implements Runnable {
    public void run() {
        Designer dsgr = Designer.theDesigner();
        org.argouml.uml.cognitive.critics.Init.init();
        org.argouml.uml.cognitive.checklist.Init.init(Locale.getDefault());
        Project p = ProjectManager.getManager().getCurrentProject();
        dsgr.spawnCritiquer(p);
        dsgr.setChildGenerator(new ChildGenUML());
        java.util.Enumeration models = (p.getUserDefinedModels()).elements();
        while (models.hasMoreElements()) {
            Object o = models.nextElement();    
            // UmlModelEventPump.getPump().removeModelEventListener(dsgr, (MModel)o);
            UmlModelEventPump.getPump().addModelEventListener(dsgr, (MModel)o); 
        }
        Argo.log.info("spawned critiquing thread");

        // should be in logon wizard?
        dsgr.startConsidering(org.argouml.uml.cognitive.critics.CrUML.decINHERITANCE);
        dsgr.startConsidering(org.argouml.uml.cognitive.critics.CrUML.decCONTAINMENT);
        Designer._userWorking = true;
    }

} /* end class StartCritics */





