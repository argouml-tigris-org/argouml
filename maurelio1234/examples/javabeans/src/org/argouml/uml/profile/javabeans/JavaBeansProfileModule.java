//$Id$
//Copyright (c) 2007 The Regents of the University of California. All
//Rights Reserved. Permission to use, copy, modify, and distribute this
//software and its documentation without fee, and without a written
//agreement is hereby granted, provided that the above copyright notice
//and this paragraph appear in all copies. This software program and
//documentation are copyrighted by The Regents of the University of
//California. The software program and documentation are supplied "AS
//IS", without any accompanying services from The Regents. The Regents
//does not warrant that the operation of the program will be
//uninterrupted or error-free. The end-user understands that the program
//was developed for research purposes and is advised not to rely
//exclusively on the program for any reason. IN NO EVENT SHALL THE
//UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
//SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
//ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
//THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
//WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
//MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
//PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
//CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
//UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.profile.javabeans;

import org.argouml.cognitive.critics.Agency;
import org.argouml.model.Model;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.uml.profile.ProfileManagerImpl;
import org.argouml.uml.profile.cognitive.critics.CrJavaBeanPropertyApplication;
import org.tigris.gef.undo.UndoableAction;


public class JavaBeansProfileModule extends UndoableAction implements ModuleInterface  {
    private ProfileJavaBeans profile = ProfileJavaBeans.getInstance();
    
    public JavaBeansProfileModule() {
        super ("JavaBeans UML Profile");        
    }
    
    public boolean enable() {
	ProfileManagerImpl.getInstance().registerProfile(profile);
	Agency.register(new CrJavaBeanPropertyApplication(), Model
		.getMetaTypes().getAttribute());
	
        return true;
    }
    
    public boolean disable() {
	ProfileManagerImpl.getInstance().removeProfile(profile);

	// TODO find a way to unregister critics!
        return true;
    }
    
    public String getName() {
        return "JavaBeans UML Profile";
    }
    
    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "JavaBeans UML Profile";
        case AUTHOR:
            return "Marcos Aurélio Almeida da Silva";
        case VERSION:
            return "0.1";
        case DOWNLOADSITE:
            return "http://argouml.tigris.org";
        default:
            return null;
        }    
    }
    
    private static final long serialVersionUID = 1L;
}
