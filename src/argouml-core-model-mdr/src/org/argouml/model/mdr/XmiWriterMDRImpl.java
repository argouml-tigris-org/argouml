/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.logging.Logger;

import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.argouml.model.UmlException;
import org.argouml.model.XmiExtensionWriter;
import org.argouml.model.XmiWriter;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.OutputConfig;

/**
 * XmiWriter implementation for MDR.
 *
 * This implementation is clumsy because the specified Writer interface wants
 * characters, while the XmiWriter wants an OutputStream dealing in bytes. We
 * could easily create a Writer from an OutputStream, but the reverse is not
 * true.
 *
 * TODO: The old Writer based interface can be removed when the deprecated
 * ModelImplementation.getXmiWriter is removed.
 *
 * @author lmaitre
 *
 */
class XmiWriterMDRImpl implements XmiWriter {

    private static final Logger LOG =
        Logger.getLogger(XmiWriterMDRImpl.class.getName());

    private MDRModelImplementation modelImpl;

    private Object model;

    private OutputConfig config;

    private Writer writer;

    private OutputStream oStream;

    private static final String ENCODING = "UTF-8";

    private static final String XMI_VERSION = "1.2";

    private XmiExtensionWriter xmiExtensionWriter;

    private static final char[] TARGET = "/XMI.content".toCharArray();

    /*
     * Private constructor for common work needed by both public
     * constructors.
     */
    private XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
             String version) {
        if (theModel == null) {
            throw new IllegalArgumentException("A model must be provided");
        }
        if (theParent == null) {
            throw new IllegalArgumentException("A parent must be provided");
        }
        this.modelImpl = theParent;
        this.model = theModel;
        config = new OutputConfig();
        config.setEncoding(ENCODING);
        config.setReferenceProvider(new XmiReferenceProviderImpl(modelImpl
                .getObjectToId()));
        config.setHeaderProvider(new XmiHeaderProviderImpl(version));
    }


    /**
     * Create an XMI writer for the given model.
     *
     * @param theParent
     *            The ModelImplementation
     * @param theModel
     *            The Model to write. If null, write all top-level model
     *            elements.
     * @param theStream
     *            The OutputStream to write to.
     * @param version
     *            the ArgoUML version
     * @throws IllegalArgumentException
     *             if no output stream is provided
     * @since 0.25.4
     */
    public XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
            OutputStream theStream, String version) {
        this(theParent, theModel, version);
        if (theStream == null) {
            throw new IllegalArgumentException("A writer must be provided");
        }
        oStream = theStream;
    }


    public void write() throws UmlException {
        XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter(
                config);
        try {
            modelImpl.getRepository().beginTrans(false);
            try {
                RefPackage extent = ((RefObject) model).refOutermostPackage();
                xmiWriter.write(oStream, "file:///ThisIsADummyName.xmi", extent,
                        XMI_VERSION);
            } finally {
                // end our transaction
                modelImpl.getRepository().endTrans();
            }
        } catch (IOException e) {
            throw new UmlException(e);
        }
    }

    public void setXmiExtensionWriter(XmiExtensionWriter theWriter) {
        xmiExtensionWriter = theWriter;
    }
}
