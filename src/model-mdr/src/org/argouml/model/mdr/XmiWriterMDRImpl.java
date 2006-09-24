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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(XmiWriterMDRImpl.class);

    private MDRModelImplementation parent;

    private Object model;

    private OutputConfig config;

    private Writer writer;

    private static final String ENCODING = "UTF-8";

    private static final String XMI_VERSION = "1.2";

    private XmiExtensionWriter xmiExtensionWriter;

    private static final char[] TARGET = "/XMI.content".toCharArray();

    /**
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
        XMIWriter xmiWriter =
            XMIWriterFactory.getDefault().createXMIWriter(
                config);
        try {
            List elements = new ArrayList();
            if (model != null && !WRITE_ALL) {
                elements.add(model);
                LOG.info("Saving model '" + ((Model) model).getName() + "'");
            } else {
                RefObject profile = parent.getProfileModel();
                UmlPackage pkg = parent.getUmlPackage();
                for (Iterator it =
                        pkg.getCore().getElement().refAllOfType().iterator();
                    it.hasNext();) {
                    RefObject obj = (RefObject) it.next();
                    // Find top level objects which aren't part of profile
                    if (obj.refImmediateComposite() == null) {
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
        private char[] tagName = new char[12];
        private int tagLength = 0;

        /**
         * Constructor.
         * @param wrappedWriter The myWriter which will be wrapped
         */
        public WriterOuputStream(Writer wrappedWriter) {
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
            char[] c = new String(b, off, len, ENCODING).toCharArray();
            if (xmiExtensionWriter != null) {
                write(c);
            } else {
                myWriter.write(c, 0, c.length);
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
            write(new byte[] {(byte) (b & 255)}, 0, 1);
        }

        /**
         * @see java.io.OutputStream#write(int)
         */
        private void write(char[] ca) throws IOException {

            int len = ca.length;
            for (int i = 0; i < len; ++i) {
                char ch = ca[i];
                if (inTag) {
                    if (ch == '>') {
                        inTag = false;
                        if (Arrays.equals(tagName, TARGET)) {
                            if (i > 0) {
                                myWriter.write(ca, 0, i + 1);
                            }
                            xmiExtensionWriter.write(myWriter);
                            xmiExtensionWriter = null;
                            if (i + 1 != len - 1) {
                                myWriter.write(ca, i + 1, (len - i) - 1);
                            }
                            return;
                        }
                    } else if (tagLength == 12) {
                        inTag = false;
                    } else {
                        tagName[tagLength++] = ch;
                    }
                }

                if (ch == '<') {
                    inTag = true;
                    Arrays.fill(tagName, ' ');
                    tagLength = 0;
                }
            }
            myWriter.write(ca, 0, ca.length);
        }
    }

    /**
     * @see org.argouml.model.XmiWriter#setXmiExtensionWriter(
     *         org.argouml.model.XmiExtensionWriter)
     */
    public void setXmiExtensionWriter(XmiExtensionWriter extWriter) {
        xmiExtensionWriter = extWriter;
    }
}
