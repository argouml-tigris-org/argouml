// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.model.uml;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * This internal facade allows simulating the operations provided by
 * javax.jmi.reflect.RefBaseObject upon ru.novosoft.uml.MBase objects.
 */
public class MofHelper {

    static MNamespace outermostPackage = null;

    /**
     * Temporary hook to allow simulation of the outermost package
     * functionality when initializing a mof repository.
     * 
     * When ArgoUML goes to a full JMI-based implementation,
     * this will become obsolete.
     * 
     * In the current implementation, this object must be set
     * to the results of
     * <code>ProjectManager.getManager().getCurrentProject().getRoot()</code>
     * 
     * @param object the outermost package
     */
	public static void setOutermostPackage(MNamespace object) {
		outermostPackage = object;
	}

	public static Object refImmediatePackage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	public static MNamespace refOutermostPackage() {
		return outermostPackage;
	}

	public static String refMofId(Object object) {
		if (object instanceof MBase) {
			return ((MBase)object).getUUID();
		}
		return null;
	}

//	These last operations do not need to be simulated

//	 public static Object refMetaObject(Object object);
//	public static Collection refVerifyConstraints(Object object, boolean arg0);

}
