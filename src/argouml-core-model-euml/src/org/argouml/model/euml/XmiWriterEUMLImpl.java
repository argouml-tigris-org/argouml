// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *******************************************************************************/
package org.argouml.model.euml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.argouml.model.UmlException;
import org.argouml.model.XmiExtensionWriter;
import org.argouml.model.XmiWriter;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * Eclipse UML2 implementation of XmiWriter.
 * 
 * TODO: We need facilities for writing and reading stable IDs to/from either
 * xmi.id or xmi.uuid.
 * 
 * @author Tom Morris
 */
class XmiWriterEUMLImpl implements XmiWriter {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    private OutputStream oStream;
    
    private org.eclipse.uml2.uml.Package model;
    
    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     * @param model
     *            The project member model.
     * @param writer
     *            The writer.
     * @param version
     *            The version of ArgoUML.
     */
    public XmiWriterEUMLImpl(EUMLModelImplementation implementation,
            Object theModel, OutputStream stream, String version) {
        if (stream == null) {
            throw new IllegalArgumentException(
                    "An OutputStream must be provided"); //$NON-NLS-1$
        }
        if (!(theModel instanceof org.eclipse.uml2.uml.Package)) {
            throw new IllegalArgumentException("A container must be provided" //$NON-NLS-1$
                    + " and it must be a UML 2 Package"); //$NON-NLS-1$
        }
        if (implementation == null) {
            throw new IllegalArgumentException("A parent must be provided"); //$NON-NLS-1$
        }
        modelImpl = implementation;
        model = (org.eclipse.uml2.uml.Package) theModel;
        oStream = stream;
    }

    public void write() throws UmlException {
        if (model.eResource() == null) {
            throw new UmlException(
                    "Root container is not affiliated with any resource!"); //$NON-NLS-1$
        }

        // Do we need to get stereotype applications for each element? - tfm
//        for (Iterator allContents = UMLUtil.getAllContents(model, true,
//                false); allContents.hasNext();) {
//            EObject eObject = (EObject) allContents.next();
//            if (eObject instanceof Element) {
//                contents.addAll(((Element) eObject).getStereotypeApplications());
//            }
//        }
        Map<String, Integer> options = new HashMap<String, Integer>();
        options.put(XMLResource.OPTION_LINE_WIDTH, 100);

        // TODO: Is there an option we can use to save our ArgoUML version?

        try {
            modelImpl.getModelEventPump().stopPumpingEvents();
            model.eResource().save(oStream, options);
        } catch (IOException ioe) {
            throw new UmlException(ioe);
        } finally {
            modelImpl.getModelEventPump().startPumpingEvents();
        }

    }

    public void setXmiExtensionWriter(XmiExtensionWriter xmiExtensionWriter) {
        throw new NotYetImplementedException();
    }

}
