// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.argouml.model.uml.XmiReader;
import org.argouml.model.uml.XmiWriter;
import org.argouml.uml.ProjectMemberModel;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The file persister for the UML model.
 * @author Bob Tarling
 */
public class ModelMemberFilePersister extends MemberFilePersister {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ModelMemberFilePersister.class);

    /**
     * Loads a model (XMI only) from an input source. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.<p>
     *
     * If there is a problem with the xmi file, an error is set in the
     * getLastLoadStatus() field. This needs to be examined by the
     * calling function.<p>
     *
     * @see org.argouml.persistence.MemberFilePersister#load(org.argouml.kernel.Project,
     * java.io.InputStream)
     */
    public void load(Project project, InputStream inputStream)
        throws OpenException {

        InputSource source = new InputSource(inputStream);
        Object mmodel = null;

        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XmiReader xmiReader = null;
        try {
            xmiReader = new XmiReader();
            source.setEncoding("UTF-8");
            mmodel = xmiReader.parseToModel(source);
        } catch (SAXException e) { // duh, this must be caught and handled
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LastLoadInfo.getInstance().setLastLoadMessage(
                    "SAXException parsing XMI.");
            LOG.error("SAXException parsing XMI.");
            LOG.error("SAXException caught", e);
            throw new OpenException(e);
        } catch (ParserConfigurationException e) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LastLoadInfo.getInstance().setLastLoadMessage(
                    "ParserConfigurationException parsing XMI.");
            LOG.error("ParserConfigurationException parsing XMI.");
            LOG.error("ParserConfigurationException caught", e);
            throw new OpenException(e);
        } catch (IOException e) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LastLoadInfo.getInstance().setLastLoadMessage(
                    "IOException parsing XMI.");
            LOG.error("IOException parsing XMI.");
            LOG.error("IOException caught", e);
            throw new OpenException(e);
        }

        // This should probably be inside xmiReader.parse
        // but there is another place in this source
        // where XMIReader is used, but it appears to be
        // the NSUML XMIReader.  When Argo XMIReader is used
        // consistently, it can be responsible for loading
        // the listener.  Until then, do it here.
        Model.getUmlHelper().addListenersToModel(mmodel);

        project.addMember(mmodel);

        project.setUUIDRefs(new HashMap(xmiReader.getXMIUUIDToObjectMap()));
    }

    /**
     * @see org.argouml.persistence.MemberFilePersister#getTag()
     */
    public String getMainTag() {
        return "XMI";
    }

    /**
     * Save the project model to XMI.
     *
     * @see org.argouml.persistence.MemberFilePersister#save(
     *         org.argouml.kernel.ProjectMember, java.io.Writer,
     *         java.lang.Integer)
     */
    public void save(ProjectMember member, Writer w, Integer indent)
    	throws SaveException {

        if (w == null) {
            throw new IllegalArgumentException("No Writer specified!");
        }

        ProjectMemberModel pmm = (ProjectMemberModel) member;
        Object model = pmm.getModel();

        File tempFile = null;
        Writer writer = null;
        if (indent != null) {
            try {
                tempFile = File.createTempFile("xmi", null);
                tempFile.deleteOnExit();
                writer = new FileWriter(tempFile);
            } catch (IOException e) {
                throw new SaveException(e);
            }
        } else {
            writer = w;
        }

        try {
            XmiWriter xmiWriter = new XmiWriter(model, writer);
            xmiWriter.write();
        } catch (SAXException ex) {
            throw new SaveException(ex);
        }
        if (indent != null) {
            addXmlFileToWriter((PrintWriter) w, tempFile, indent.intValue());
        }
    }
}
