// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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
import java.io.Writer;

import org.apache.log4j.Logger;
import org.netbeans.lib.jmi.xmi.WriterBase;
import org.netbeans.lib.jmi.xmi.XMIHeaderProvider;

/**
 * Write a header for the XMI file which contains information about
 * version, etc.
 */
public class XmiHeaderProviderImpl implements XMIHeaderProvider {
    
    private static final String UML_VERSION = "1.4";
    private static final Logger LOG = Logger
            .getLogger(XmiHeaderProviderImpl.class);

    private String version;
    
    /**
     * Constructor.
     * @param version the version of ArgoUML that saved the XMI
     */
    public XmiHeaderProviderImpl(String version) {
        this.version = version;
    }

    /**
     * @see org.netbeans.lib.jmi.xmi.XMIHeaderProvider#writeHeader(java.io.Writer)
     */
    public void writeHeader (Writer ps) {
        // NOTE: The <XMI.header></XMI.header> is provided for us
        String header =
              "    <XMI.documentation>\n"
            + "      <XMI.exporter>ArgoUML"
                    + " (using "  + WriterBase.EXPORTER_NAME 
                    + " version " + WriterBase.EXPORTER_VERSION 
                    + ")</XMI.exporter>\n"
            + "      <XMI.exporterVersion>" + version
                    + " revised on $Date$ "
                    + "</XMI.exporterVersion>\n"
            + "    </XMI.documentation>\n"
            + "    <XMI.metamodel xmi.name=\"UML\" xmi.version=\""
                    + UML_VERSION + "\"/>";
        
        try {
            ps.write(header);
        } catch (IOException e) {
            LOG.error("Exception while writing XMI header + ", e);
        }
    }

}
