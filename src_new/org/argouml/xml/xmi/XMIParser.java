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

package org.argouml.xml.xmi;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.beans.PropertyVetoException;

import org.xml.sax.*;

import ru.novosoft.uml.xmi.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

import org.argouml.model.uml.UmlFactory;
import org.argouml.application.api.*;
import org.argouml.kernel.Project;

/** XMI is an XML based exchange format between UML tools. 
 *ArgoUML uses this as standard saving mechanism so that easy interchange 
 *with other tools and compliance with open standards are secured. 
 *XMI version 1.0 for UML 1.3 is used. To convert older models in XMI 
 *(Argo 0.7 used XMI 1.0 for UML1.1) to the latest version, 
 *Meta Integration provides a free key to their Model Bridge. 
 *This also permits you to convert Rational Rose models to ArgoUML! 
 *This currently only includes model information, but no graphical 
 *information (like layout of diagrams).
 *
 */
public class XMIParser {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static XMIParser SINGLETON = new XMIParser();

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected MModel _curModel = null;
    protected Project _proj = null;
    protected HashMap _UUIDRefs = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    protected XMIParser() { /* super(); */
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public MModel getCurModel() {
        return _curModel;
    }
    public void setProject(Project p) {
        _proj = p;
    }
    public HashMap getUUIDRefs() {
        return _UUIDRefs;
    }

    ////////////////////////////////////////////////////////////////
    // main parsing methods

    public synchronized void readModels(Project p, URL url) throws IOException {

        _proj = p;

        Argo.logger.info("=======================================");
        Argo.logger.info("== READING MODEL " + url);
        try {
            XMIReader reader = new XMIReader();
            InputSource source = new InputSource(url.openStream());
            source.setSystemId(url.toString());
            _curModel = reader.parse(source);
            if (reader.getErrors()) {
            	throw new IOException("XMI file " + url.toString() + " could not be parsed.");
            }
            _UUIDRefs = new HashMap(reader.getXMIUUIDToObjectMap());

        }
        catch (SAXException saxEx) {
            //
            //  a SAX exception could have been generated
            //    because of another exception.
            //    Get the initial exception to display the
            //    location of the true error
            Exception ex = saxEx.getException();
            if (ex == null) {
                saxEx.printStackTrace();
            }
            else {
                ex.printStackTrace();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        Argo.logger.info("=======================================");

        try {
            _proj.addModel(
                (ru.novosoft.uml.foundation.core.MNamespace) _curModel);
        }
        catch (PropertyVetoException ex) {
            System.err.println(
                "An error occurred adding the model to the project!");
            ex.printStackTrace();
        }

        Collection ownedElements = _curModel.getOwnedElements();
        Iterator oeIterator = ownedElements.iterator();

        while (oeIterator.hasNext()) {
            MModelElement me = (MModelElement) oeIterator.next();
            if (me instanceof MClass) {
                _proj.defineType((MClass) me);
            }
            else
                if (me instanceof MDataType) {
                    _proj.defineType((MDataType) me);
                }
        }
    }

} /* end class XMIParser */
