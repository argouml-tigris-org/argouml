// $Id: UMLUtil.java 13394 2007-08-18 12:48:34Z b00__1 $
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.StrictCompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * This class exposes protected methods from
 * {@link org.eclipse.uml2.uml.UMLUtil} and adds additional util methods
 * 
 * @author Bogdan Pistol
 */
public class UMLUtil extends org.eclipse.uml2.uml.util.UMLUtil {

    /**
     * The default URI used for eUML
     */
    public static final URI DEFAULT_URI = URI.createURI("http://argouml.tigris.org/euml/resource/default_uri.xmi"); //$NON-NLS-1$
        
    /**
     * Getter for the attributes of a Type
     * 
     * @param type
     *                The Type that owns the attributes
     * @return the attributes or null
     */
    public static EList<Property> getOwnedAttributes(Type type) {
        if (type instanceof AssociationClass) {
            return ((AssociationClass) type).getOwnedAttributes();
        } else if (type instanceof Association) {
            return ((Association) type).getOwnedEnds();
        } else {
            return org.eclipse.uml2.uml.util.UMLUtil.getOwnedAttributes(type);
        }
    }

    /**
     * Getter for the operations of a Type
     * 
     * @param type
     *                The Type that owns the operations
     * @return the operations or null
     */
    public static EList<Operation> getOwnedOperations(Type type) {
        return org.eclipse.uml2.uml.util.UMLUtil.getOwnedOperations(type);
    }

    /**
     * Copy a tree of UML elements into a destination location
     * 
     * @param modelImplementation
     *                the eUML model implementation
     * @param source
     *                the tree of UML elements to be copied
     * @param destination
     *                the destination container
     * @return the root of the newly copied tree of UML elements or null
     */
    public static Element copy(EUMLModelImplementation modelImplementation,
            Element source, Element destination) {
        Command copyToClipboard = CopyToClipboardCommand.create(
                modelImplementation.getEditingDomain(), source);
        Command pasteFromClipboard = PasteFromClipboardCommand.create(
                modelImplementation.getEditingDomain(), destination, null);
        StrictCompoundCommand copyCommand = new StrictCompoundCommand() {
            {
                isPessimistic = true;
            }
        };
        copyCommand.append(copyToClipboard);
        copyCommand.append(pasteFromClipboard);
        copyCommand.setLabel("Copy a tree of UML elements to a destination");
        if (copyCommand.canExecute()) {
            modelImplementation.getModelEventPump().getRootContainer().setHoldEvents(
                    true);
            modelImplementation.getEditingDomain().getCommandStack().execute(
                    copyCommand);
            if (modelImplementation.getEditingDomain().getCommandStack().getMostRecentCommand().getAffectedObjects().size() == 1) {
                modelImplementation.getModelEventPump().getRootContainer().setHoldEvents(
                        false);
                return (Element) modelImplementation.getEditingDomain().getCommandStack().getMostRecentCommand().getAffectedObjects().iterator().next();
            } else {
                modelImplementation.getEditingDomain().getCommandStack().undo();
                modelImplementation.getModelEventPump().getRootContainer().clearHeldEvents();
                modelImplementation.getModelEventPump().getRootContainer().setHoldEvents(
                        false);
            }
        }
        return null;
    }
    
    /**
     * Returns information about an Object.
     * <p>
     * If it's an Element it returns its metaclass name, and if it's a
     * NamedElement it appends its name.
     * 
     * @param o
     *                The object
     * @return the String description
     */
    public static String toString(Object o) {
        if (o == null) {
            return "null"; //$NON-NLS-1$
        }
        if (!(o instanceof Element)) {
            return o.toString();
        }
        
        StringBuilder sb = new StringBuilder("'"); //$NON-NLS-1$
        boolean named = false;
        
        if (o instanceof NamedElement && ((NamedElement) o).getName() != null
                && !((NamedElement) o).getName().equals("")) { //$NON-NLS-1$
            named = true;
            sb.append(((NamedElement) o).getName() + " ["); //$NON-NLS-1$
        }
        
        sb.append(((Element) o).eClass().getName());
        
        if (named) {
            sb.append("]"); //$NON-NLS-1$
        }
        
        sb.append("'"); //$NON-NLS-1$
        
        return sb.toString();
    }
    
    /**
     * Returns the Resource associated with an URI or creates the resource if it
     * does not exist
     * 
     * @param modelImplementation
     *                the eUML implementation
     * @param uri
     *                the URI which identifies the resource
     * @return the retrieved or created resource
     */
    public static Resource getResource(
            EUMLModelImplementation modelImplementation, URI uri) {
        Resource r = modelImplementation.getEditingDomain().getResourceSet().getResource(
                uri, false);
        if (r == null) {
            r = modelImplementation.getEditingDomain().getResourceSet().createResource(
                    uri);
        }
        return r;
    }

}
