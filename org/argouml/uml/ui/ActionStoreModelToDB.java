// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.persistence.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import java.awt.event.*;
import javax.swing.*;


/* class ActionStoreModelToDB */
/** @stereotype singleton 
 */
public class ActionStoreModelToDB extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionStoreModelToDB SINGLETON = new ActionStoreModelToDB(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionStoreModelToDB() {
	super("Store model to DB", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent e) {

	// when the action is performed, i.e. someone clicked on the menuitem,
	// create a new writer, get the Project and its curent Model,
	// then store it. Simple as this.

	DBWriter writer = new DBWriter();

	if (writer.hasConnection()) {
	    ProjectBrowser pb = ProjectBrowser.TheInstance;
	    Project p =  pb.getProject();

	    MNamespace nm = p.getCurrentNamespace();
	    if (!(nm instanceof MModel)) {
		JOptionPane.showMessageDialog(null, "Error", "Current Namespace is not a Model", JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    writer.store((MModel)nm);
	}

	// one could also store all models of the project,
	//don't know which is better.

	// 	  Vector models = p.getModels();
	//  	  for (int i = 0; i< models.size(); i++) {
	// 		  MNamespace nm = (MNamespace)models.elementAt(i);
	// 		  if (!(nm instanceof MModel)) {
	// 			  JOptionPane.showMessageDialog(null, "Error", "Current Namespace is not a Model", JOptionPane.ERROR_MESSAGE);
	// 			  return;
	// 		  }
	// 		  pb.showStatus("Wrote " + ((MModel)models.elementAt(i)).getName());
	// 		  writer.store((MModel)models.elementAt(i));
	// 	  }
    }

    public boolean shouldBeEnabled() {
    	return false;

	// my way of finding out whether these actions should be enabled
	// is to look for the Properties file. Not nice, but working.
	// should be replaecd by a proper plug-in mechanism as soon as there
	// is one for Argo

// Jaap Branderhorst
// Killed this mechanism
/*
	java.util.Properties props =  new java.util.Properties();
	String configFile =  System.getProperty("argo.dbconfig", "/db.ini");
	try {
	    java.io.InputStream is = new java.io.FileInputStream(configFile);
	    props.load(is);
	}
	catch (java.io.IOException e) {
	    return false;
	}
	return true;
	*/
    }
    
} /* end class ActionStoreModelToDB */
