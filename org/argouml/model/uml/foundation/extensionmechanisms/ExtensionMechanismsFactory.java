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

package org.argouml.model.uml.foundation.extensionmechanisms;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;

/**
 * Factory to create UML classes for the UML
 * Foundation::ExtensionMechanisms package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */

public class ExtensionMechanismsFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static ExtensionMechanismsFactory SINGLETON =
                   new ExtensionMechanismsFactory();

    /** Singleton instance access method.
     */
    public static ExtensionMechanismsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ExtensionMechanismsFactory() {
    }

    /** Create an empty but initialized instance of a UML Stereotype.
     *  
     *  @return an initialized UML Stereotype instance.
     */
    public MStereotype createStereotype() {
        MStereotype modelElement = MFactory.getDefaultFactory().createStereotype();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML TaggedValue.
     *  
     *  @return an initialized UML TaggedValue instance.
     */
    public MTaggedValue createTaggedValue() {
        MTaggedValue modelElement = MFactory.getDefaultFactory().createTaggedValue();
	super.initialize(modelElement);
	return modelElement;
    }
    
    /**
     * Builds a stereotype for some kind of modelelement.
     * @deprecated This method should not be used any more since it leads to
     * faulty models if the namespace is not in the model the user is 
     * working on.
     */
    public MStereotype buildStereotype(MModelElement m, String text, MNamespace ns) {
    	if (m == null || text == null || ns == null) throw new IllegalArgumentException("In buildStereotype: one of the arguments is null");
    	MStereotype stereo = createStereotype();
    	stereo.setName(text);
    	stereo.setBaseClass(ExtensionMechanismsHelper.getHelper().getMetaModelName(m));
    	MStereotype stereo2 = ExtensionMechanismsHelper.getHelper().getStereotype(ns, stereo);
    	if (stereo2 != null) {
    		stereo2.addExtendedElement(m);
    		UmlFactory.getFactory().delete(stereo);
    		return stereo2;
    	} else {
    		ns.addOwnedElement(stereo);
    		stereo.addExtendedElement(m);
    		return stereo;
    	}
    }
    
    public MStereotype buildStereotype(MModelElement m, String text) {
        // if (m == null && text == null) throw new IllegalArgumentException("In buildStereotype: one of the arguments is null");
        MStereotype stereo = createStereotype();
        stereo.setName(text);
        stereo.setBaseClass(ExtensionMechanismsHelper.getHelper().getMetaModelName(m));
        MStereotype stereo2 = ExtensionMechanismsHelper.getHelper().getStereotype(stereo);
        if (stereo2 != null) {
            stereo2.addExtendedElement(m);
            UmlFactory.getFactory().delete(stereo);
            return stereo2;
        } else {
            ProjectManager.getManager().getCurrentProject().getModel().addOwnedElement(stereo);
            if (m != null)
                stereo.addExtendedElement(m);
            return stereo;
        }
    }
    
    
    public void deleteStereotype(MStereotype elem) {}
    
    public void deleteTaggedValue(MTaggedValue elem) {}
}

