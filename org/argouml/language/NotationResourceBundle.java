// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.language;
import org.argouml.application.api.*;
import org.argouml.application.helpers.*;

public class NotationResourceBundle extends ResourceBundleHelper
implements PluggableResourceBundle {

   static final Object[][] _contents = {
        {"label_use_guillemots", "Use guillemots (\u00ab \u00bb) for stereotypes" },
        {"label_uml_notation_only", "Only allow strict UML notation text" },
        {"tab_notation", "Notation" }  
   };

     public Object[][] getContents() {
        return _contents;
     }

    public String getModuleName() {
        return "NotationResourceBundle";
    }

    public String getModuleDescription() {
        return "Generic Resource Bundle for Notations";
    }

    public String getModuleAuthor() {
        return "ArgoUML Core";
    }

    /** This should call on the version number from a central xml file
     * me thinks...
     * @return
     */    
    public String getModuleVersion() {
        return "0.9.4";
    }

    public String getModuleKey() {
        return "module.resources.notation";
    }
}

