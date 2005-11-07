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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.XmiReader;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.xml.sax.InputSource;

/**
 * XMI is an XML based exchange format between UML tools.
 * ArgoUML uses this as standard saving mechanism so that easy interchange
 * with other tools and compliance with open standards are secured.
 * XMI version 1.0 for UML 1.3 is used. To convert older models in XMI
 * (Argo 0.7 used XMI 1.0 for UML1.1) to the latest version,
 * Meta Integration provides a free key to their Model Bridge.
 * This also permits you to convert Rational Rose models to ArgoUML!
 * This currently only includes model information, but no graphical
 * information (like layout of diagrams).
 *
 */
public class XMIParser {

    ////////////////////////////////////////////////////////////////
    // static variables

    /** logger */
    private static final Logger LOG = Logger.getLogger(XMIParser.class);

    private static XMIParser singleton = new XMIParser();

    ////////////////////////////////////////////////////////////////
    // instance variables

    private Object curModel = null;
    private Project proj = null;
    private HashMap uUIDRefs = null;

    /**
     * The constructor.
     *
     */
    protected XMIParser() { /* super(); */
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the current model
     */
    public Object/*MModel*/ getCurModel() {
        return curModel;
    }

    /**
     * @param p the project
     */
    public void setProject(Project p) {
        proj = p;
    }

    /**
     * @return the UUID
     */
    public HashMap getUUIDRefs() {
        return uUIDRefs;
    }

    ////////////////////////////////////////////////////////////////
    // main parsing methods

    /**
     * The main parsing method.
     *
     * @param p the project
     * @param url the URL
     * @throws OpenException when there is an IO error
     */
    public synchronized void readModels(Project p, URL url) 
        throws OpenException {

        proj = p;

        LOG.info("=======================================");
        LOG.info("== READING MODEL " + url);
        try {
            XmiReader reader = Model.getXmiReader();
            InputSource source = new InputSource(url.openStream());
            source.setSystemId(url.toString());
            Collection elements = reader.parse(source);
            registerModelAndDiagrams(elements);
            uUIDRefs = new HashMap(reader.getXMIUUIDToObjectMap());
        } catch (Exception ex) {
            throw new OpenException(ex);
        }
        LOG.info("=======================================");


    }

    protected void registerModelAndDiagrams(Collection elements) {
        Facade facade = Model.getFacade();
        Collection diagramsElement = new ArrayList();
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            Object element = it.next();
            if (facade.isAModel(element)) {
                proj.addModel(element);
                curModel = element;
                diagramsElement.addAll(Model.getModelManagementHelper().
                        getAllModelElementsOfKind(element,
                                Model.getMetaTypes().getStateMachine()));
                Collection ownedElements = Model.getFacade().getOwnedElements(element);
                Iterator oeIterator = ownedElements.iterator();
                while (oeIterator.hasNext()) {
                    Object me = oeIterator.next();
                    if (Model.getFacade().getName(me) == null)
                        Model.getCoreHelper().setName(me, "");
                }
            } else if (facade.isAStateMachine(element)) {
                diagramsElement.add(element);
            }
        }
        //
        it = diagramsElement.iterator();
        while (it.hasNext()) {
            Object element = it.next();
            Object namespace = null;
            if (facade.getNamespace(element) == null) {
                namespace = facade.getContext(element);
                Model.getCoreHelper().setNamespace(element,namespace);
            } else {
                namespace = facade.getNamespace(element);
            }
            ArgoDiagram diagram = null;
            if (facade.isAActivityGraph(element)){
                LOG.info("Creating activity diagram for "+facade.getUMLClassName(element)+"<<"+facade.getName(element)+">>");
                diagram = new UMLActivityDiagram( namespace , element );
            } else {
                LOG.info("Creating state diagram for "+facade.getUMLClassName(element)+"<<"+facade.getName(element)+">>");                
                diagram = new UMLStateDiagram( namespace , element );
            }                    
            if (diagram!=null)
                proj.addMember(diagram);
        }
    }
    
    /**
     * @return Returns the singleton.
     */
    public static XMIParser getSingleton() {
        return singleton;
    }
} /* end class XMIParser */
