// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.ui.model_management;

import org.argouml.application.api.*;
import org.argouml.application.ArgoVersion;

import ru.novosoft.uml.model_management.*;

public class PropPanelModel extends PropPanelPackage
implements PluggablePropertyPanel {
  ////////////////////////////////////////////////////////////////
  // instance vars

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelModel() {
      super("Model", _modelIcon, 2);
  }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Model") ||
            baseClass.equals("Package") ||
            baseClass.equals("Namespace") ||
            baseClass.equals("GeneralizableElement");
    }

    public Class getClassForPanel() {
        return MModelImpl.class;
    }

    public String getModuleName() { return "PropPanelModel"; }
    public String getModuleDescription() { return "Property Panel for Model"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.propertypanel.model"; }

} /* end class PropPanelModel */
