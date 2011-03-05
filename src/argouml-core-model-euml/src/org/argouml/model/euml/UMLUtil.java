// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial API and implementation
 *******************************************************************************/
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
    public static final URI DEFAULT_URI = 
        URI.createURI("http://argouml.tigris.org/euml/resource/default_uri.xmi"); //$NON-NLS-1$
        
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
        copyCommand.setLabel("Copy a tree of UML elements to a destination"); //$NON-NLS-1$
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
    static Resource getResource(
            EUMLModelImplementation modelImplementation, URI uri,
            boolean readOnly) {
        if (!"xmi".equals(uri.fileExtension()) //$NON-NLS-1$
                && !"uml".equals(uri.fileExtension())) { //$NON-NLS-1$
            // Make sure we have a recognized file extension
            uri = uri.appendFileExtension("xmi"); //$NON-NLS-1$
        }
        Resource r = modelImplementation.getEditingDomain().getResourceSet()
                .getResource(uri, false);
        if (r == null) {
            r = modelImplementation.getEditingDomain().getResourceSet()
                    .createResource(uri);
        }
        if (r == null) {
            throw new NullPointerException("Failed to create resource for URI "  //$NON-NLS-1$
                    + uri);
        }
        modelImplementation.getReadOnlyMap().put(r, Boolean.valueOf(readOnly));
        return r;
    }


    @SuppressWarnings("unchecked")
    static void checkArgs(Object[] args, Class[] types) {
        if (args.length != types.length) {
            throw new IllegalArgumentException(
                    "Mismatched array lengths for args and types"); //$NON-NLS-1$
        }
        for (int i = 0; i < args.length; i++) {
            if (!types[i].isAssignableFrom(args[i].getClass())) {
                throw new IllegalArgumentException(
                        "Parameter " + i + " must be of type " + types[i]);  //$NON-NLS-1$//$NON-NLS-2$
            }
        }
    }
}
