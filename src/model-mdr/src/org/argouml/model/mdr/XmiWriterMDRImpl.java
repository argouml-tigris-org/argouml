// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.jmi.reflect.RefObject;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiExtensionWriter;
import org.argouml.model.XmiWriter;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.OutputConfig;
import org.omg.uml.UmlPackage;
import org.omg.uml.modelmanagement.Model;

/**
 * XmiWriter implementation for MDR.
 * 
 * This implementation is clumsy because the specified Writer interface wants
 * characters, while the XmiWriter wants an OutputStream dealing in bytes. We
 * could easily create a Writer from an OutputStream, but the reverse is not
 * true. 
 * 
 * TODO: Change interface to use OutputStream instead of Writer and change this
 * to match
 * 
 * @author lmaitre
 * 
 */
public class XmiWriterMDRImpl implements XmiWriter {

    private Logger LOG = Logger.getLogger(XmiWriterMDRImpl.class);

    private MDRModelImplementation parent;

    private Object model;
    
    private OutputConfig config;

    private Writer writer;
    
    private static final String ENCODING = "UTF-8";
    
    private static final String XMI_VERSION = "1.2";
    
    private XmiExtensionWriter xmiExtensionWriter;

    /*
     * If true, change write semantics to write all top level model elements
     * except for the profile model(s), ignoring the model specified by the 
     * caller.
     */
    private static final boolean WRITE_ALL = false;

    /**
     * Create an XMI writer for the given model or extent.
     * 
     * @param theParent
     *            The ModelImplementation
     * @param theModel
     *            The Model to write. If null, write all top-level model
     *            elements.
     * @param theWriter
     *            The writer to write to
     * @param version the ArgoUML version
     */
    public XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
            Writer theWriter, String version) {
        this.parent = theParent;
        this.model = theModel;
        this.writer = theWriter;
        config = new OutputConfig();
        config.setEncoding(ENCODING);
        config.setReferenceProvider(new XmiReferenceProviderImpl(parent
                .getObjectToId()));
        config.setHeaderProvider(new XmiHeaderProviderImpl(version));
    }

    /**
     * @see org.argouml.model.XmiWriter#write()
     */
    public void write() throws UmlException {
        XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter(
                config);
        try {
            ArrayList elements = new ArrayList();
            if (model != null && !WRITE_ALL) {
                elements.add(model);
                LOG.info("Saving model '" + ((Model) model).getName() + "'");
            } else {
                RefObject profile = parent.getProfileModel();
                UmlPackage pkg = parent.getUmlPackage();
                for (Iterator it = pkg.getCore().getElement().refAllOfType()
                        .iterator(); it.hasNext();) {
                    RefObject obj = (RefObject) it.next();
                    // Find top level objects which aren't part of profile
                    if (obj.refImmediateComposite() == null ) {
                        if (!obj.equals(profile)) {
                            elements.add(obj);
                        }
                    }
                }
                LOG.info("Saving " + elements.size() 
                        + " top level model elements");
            }
     
            xmiWriter.write(
                    new WriterOuputStream(writer), elements, XMI_VERSION);
        } catch (IOException e) {
            throw new UmlException(e);
        }
    }

    /**
     * Class which wraps a Writer into an OutputStream.
     * 
     * (this can go away when/if org.argouml.model.XmiWriter
     * interface changes - see ToDo in header)
     * 
     * @author lmaitre
     */
    public class WriterOuputStream extends OutputStream {

        private Writer myWriter;
        private boolean inTag = false;
        private StringBuffer tagName = new StringBuffer(12);

        /**
         * Constructor.
         * @param wrappedWriter The myWriter which will be wrapped
         */
        public WriterOuputStream(Writer wrappedWriter) {
            LOG.info("Constructing WriterOutputStream");
            this.myWriter = wrappedWriter;
        }

        /**
         * @see java.io.OutputStream#close()
         */
        public void close() throws IOException {
            myWriter.close();
        }

        /**
         * @see java.io.OutputStream#flush()
         */
        public void flush() throws IOException {
            myWriter.flush();
        }

        /**
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(byte[] b, int off, int len) throws IOException {
            while (off < len) {
                write(b[off++]);
            }
        }

        /**
         * @see java.io.OutputStream#write(byte[])
         */
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        /**
         * @see java.io.OutputStream#write(int)
         */
        public void write(int b) throws IOException {
            myWriter.write((byte) (b & 255));
            if (xmiExtensionWriter != null) {
                if (inTag) {
                    if (b == '>') {
                        inTag = false;
                        if (tagName.toString().equals("/XMI.content")) {
                            LOG.info("Calling extension writer");
                            xmiExtensionWriter.write(myWriter);
                        }
                    } else {
                        tagName.append((char) b);
                    }
                }
                
                if (b == '<') {
                    inTag = true;
                    tagName.delete(0, tagName.length());
                }
            }
        }
    }

    public void setXmiExtensionWriter(XmiExtensionWriter xmiExtensionWriter) {
        LOG.info("Extension writer set to " + xmiExtensionWriter);
        this.xmiExtensionWriter = xmiExtensionWriter;
    }
    
    
}
