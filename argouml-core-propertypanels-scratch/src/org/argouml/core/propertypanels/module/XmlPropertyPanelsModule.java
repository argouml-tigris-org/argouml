// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.module;

import java.util.ArrayList;
import java.util.List;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.core.propertypanels.panel.XmlPropertyPanel;
import org.argouml.moduleloader.DetailsTabProvider;
import org.argouml.moduleloader.ModuleInterface;


public class XmlPropertyPanelsModule implements ModuleInterface, DetailsTabProvider {

    public boolean disable() {
        return true;
    }

    public boolean enable() {        
        return true;
    }

    public List<AbstractArgoJPanel> getDetailsTabs() {        
        List<AbstractArgoJPanel> result = new ArrayList<AbstractArgoJPanel>();
        result.add(XmlPropertyPanel.getInstance());
        return result;
    }

    public String getInfo(int type) {
        switch (type){
        case AUTHOR:
            return "Christian Lopez Espinola";
        case DESCRIPTION:
            return "Module for adding property panels based on XML";
        case DOWNLOADSITE:
            return "Not released. Prototyping yet.";
        case VERSION:
            return "0.1";
        default:
            return null;
        }
    }

    public String getName() {        
        return "Xml Property Panels Module";
    }

}
