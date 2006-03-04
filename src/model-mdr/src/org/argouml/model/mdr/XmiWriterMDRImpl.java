// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.OutputConfig;
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

    /**
     * Constructor.
     * @param theParent The ModelImplementation
     * @param theModel The Model to write
     * @param theWriter The writer to write to
     */
    public XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
            Writer theWriter) {
        this.parent = theParent;
        this.model = theModel;
        this.writer = theWriter;
        config = new OutputConfig();
        config.setEncoding(ENCODING);
        config.setReferenceProvider(new XmiReferenceProviderImpl());
        config.setHeaderProvider(new XmiHeaderProviderImpl());
    }

    /**
     * @see org.argouml.model.XmiWriter#write()
     */
    public void write() throws UmlException {
        XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter(
                config);
        try {
            Vector toSerialize = new Vector();
            // Model
            Model m = (Model) model;
            toSerialize.add(model);
            LOG.info("Saving model '" + m.getName() + "'");
            // Write
            xmiWriter.write(new WriterOuputStream(writer), toSerialize,
                    XMI_VERSION);
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
            myWriter.write(c, 0, c.length);
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
    }
}
