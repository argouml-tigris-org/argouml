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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ItemUID;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The PGML parser.
 *
 */
public class PGMLParser extends org.tigris.gef.xml.pgml.PGMLParser {

    /**
     * HACK to handle issue 2719.
     */
    private boolean nestedGroupFlag = false;

    /**
     * HACK to handle issue 2719.
     */
    private Fig figGroup = null;

    private static final Logger LOG = Logger.getLogger(PGMLParser.class);

    private int privateTextDepth = 0;
    private StringBuffer privateText = new StringBuffer();
    ////////////////////////////////////////////////////////////////
    // static variables

    //    private static final PGMLParser INSTANCE = new PGMLParser();

    private HashMap translationTable = new HashMap();

    /**
     * Constructor.
     * @param modelElementsByUuid a map of model elements indexed
     *                            by a unique string identifier.
     */
    public PGMLParser(Map modelElementsByUuid) {
        super(modelElementsByUuid);
        // TODO: I think this is so old we don't need it any more
        // This goes way back to pre-zargo days
        translationTable.put("uci.uml.visual.UMLClassDiagram",
            "org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram");
        translationTable.put("uci.uml.visual.UMLUseCaseDiagram",
            "org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram");
        translationTable.put("uci.uml.visual.UMLActivityDiagram",
            "org.argouml.uml.diagram.activity.ui.UMLActivityDiagram");
        translationTable.put("uci.uml.visual.UMLCollaborationDiagram",
            "org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram");
        translationTable.put("uci.uml.visual.UMLDeploymentDiagram",
            "org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram");
        translationTable.put("uci.uml.visual.UMLStateDiagram",
            "org.argouml.uml.diagram.state.ui.UMLStateDiagram");
        translationTable.put("uci.uml.visual.UMLSequenceDiagram",
            "org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram");
        translationTable.put("uci.uml.visual.FigAssociation",
            "org.argouml.uml.diagram.ui.FigAssociation");
        translationTable.put("uci.uml.visual.FigRealization",
            "org.argouml.uml.diagram.ui.FigRealization");
        translationTable.put("uci.uml.visual.FigGeneralization",
            "org.argouml.uml.diagram.ui.FigGeneralization");
        translationTable.put("uci.uml.visual.FigCompartment",
            "org.argouml.uml.diagram.ui.FigCompartment");
        translationTable.put("uci.uml.visual.FigDependency",
            "org.argouml.uml.diagram.ui.FigDependency");
        translationTable.put("uci.uml.visual.FigEdgeModelElement",
            "org.argouml.uml.diagram.ui.FigEdgeModelElement");
        translationTable.put("uci.uml.visual.FigMessage",
            "org.argouml.uml.diagram.ui.FigMessage");
        translationTable.put("uci.uml.visual.FigNodeModelElement",
            "org.argouml.uml.diagram.ui.FigNodeModelElement");
        translationTable.put("uci.uml.visual.FigNodeWithCompartments",
            "org.argouml.uml.diagram.ui.FigNodeWithCompartments");
        translationTable.put("uci.uml.visual.FigNote",
            "org.argouml.uml.diagram.ui.FigNote");
        translationTable.put("uci.uml.visual.FigTrace",
            "org.argouml.uml.diagram.ui.FigTrace");
        translationTable.put("uci.uml.visual.FigClass",
            "org.argouml.uml.diagram.static_structure.ui.FigClass");
        translationTable.put("uci.uml.visual.FigInterface",
            "org.argouml.uml.diagram.static_structure.ui.FigInterface");
        translationTable.put("uci.uml.visual.FigInstance",
            "org.argouml.uml.diagram.static_structure.ui.FigInstance");
        translationTable.put("uci.uml.visual.FigLink",
            "org.argouml.uml.diagram.static_structure.ui.FigLink");
        translationTable.put("uci.uml.visual.FigPackage",
            "org.argouml.uml.diagram.static_structure.ui.FigPackage");
        translationTable.put("uci.uml.visual.FigActionState",
            "org.argouml.uml.diagram.activity.ui.FigActionState");
        translationTable.put("uci.uml.visual.FigAssociationRole",
            "org.argouml.uml.diagram.collaboration.ui.FigAssociationRole");
        translationTable.put("uci.uml.visual.FigClassifierRole",
            "org.argouml.uml.diagram.collaboration.ui.FigClassifierRole");
        translationTable.put("uci.uml.visual.FigComponent",
            "org.argouml.uml.diagram.deployment.ui.FigComponent");
        translationTable.put("uci.uml.visual.FigComponentInstance",
            "org.argouml.uml.diagram.deployment.ui.FigComponentInstance");
        translationTable.put("uci.uml.visual.FigMNode",
            "org.argouml.uml.diagram.deployment.ui.FigMNode");
        translationTable.put("uci.uml.visual.FigMNodeInstance",
            "org.argouml.uml.diagram.deployment.ui.FigMNodeInstance");
        translationTable.put("uci.uml.visual.FigObject",
            "org.argouml.uml.diagram.deployment.ui.FigObject");
        translationTable.put("uci.uml.visual.FigBranchState",
            "org.argouml.uml.diagram.state.ui.FigBranchState");
        translationTable.put("uci.uml.visual.FigCompositeState",
            "org.argouml.uml.diagram.state.ui.FigCompositeState");
        translationTable.put("uci.uml.visual.FigDeepHistoryState",
            "org.argouml.uml.diagram.state.ui.FigDeepHistoryState");
        translationTable.put("uci.uml.visual.FigFinalState",
            "org.argouml.uml.diagram.state.ui.FigFinalState");
        translationTable.put("uci.uml.visual.FigForkState",
            "org.argouml.uml.diagram.state.ui.FigForkState");
        translationTable.put("uci.uml.visual.FigHistoryState",
            "org.argouml.uml.diagram.state.ui.FigHistoryState");
        translationTable.put("uci.uml.visual.FigInitialState",
            "org.argouml.uml.diagram.state.ui.FigInitialState");
        translationTable.put("uci.uml.visual.FigJoinState",
            "org.argouml.uml.diagram.state.ui.FigJoinState");
        translationTable.put("uci.uml.visual.FigShallowHistoryState",
            "org.argouml.uml.diagram.state.ui.FigShallowHistoryState");
        translationTable.put("uci.uml.visual.FigSimpleState",
            "org.argouml.uml.diagram.state.ui.FigSimpleState");
        translationTable.put("uci.uml.visual.FigActionState",
            "org.argouml.uml.diagram.activity.ui.FigActionState");
        translationTable.put("uci.uml.visual.FigStateVertex",
            "org.argouml.uml.diagram.state.ui.FigStateVertex");
        translationTable.put("uci.uml.visual.FigTransition",
            "org.argouml.uml.diagram.state.ui.FigTransition");
        translationTable.put("uci.uml.visual.FigActor",
            "org.argouml.uml.diagram.use_case.ui.FigActor");
        translationTable.put("uci.uml.visual.FigUseCase",
            "org.argouml.uml.diagram.use_case.ui.FigUseCase");
        translationTable.put("uci.uml.visual.FigSeqLink",
            "org.argouml.uml.diagram.sequence.ui.FigSeqLink");
        translationTable.put("uci.uml.visual.FigSeqObject",
            "org.argouml.uml.diagram.sequence.ui.FigSeqObject");
        translationTable.put("uci.uml.visual.FigSeqStimulus",
            "org.argouml.uml.diagram.sequence.ui.FigSeqStimulus");
    }

    /**
     * @param from the class name to be "translated", i.e. replaced
     *             by something else
     * @param to   the resulting name
     */
    public void addTranslation(String from, String to) {
        translationTable.put(from, to);
    }

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#translateClassName(java.lang.String)
     */
    protected String translateClassName(String oldName) {
        // TODO: Use stylesheet to convert or wait till we use Fig
        // factories in diagram subsystem.
        // What is the last version that used FigNote?
        if ("org.argouml.uml.diagram.static_structure.ui.FigNote"
            .equals(oldName)) {
            return "org.argouml.uml.diagram.static_structure.ui.FigComment";
        }
        
        // TODO: Use stylesheet to convert or wait till we use Fig
        // factories in diagram subsystem.
        // What is the last version that used FigState?
	if ("org.argouml.uml.diagram.state.ui.FigState".equals(oldName)) {
	    return "org.argouml.uml.diagram.state.ui.FigSimpleState";
	}
    
        // TODO: I think this is so old we don't need it any more
        // This goes way back to pre-zargo days
        if (oldName.startsWith("uci.gef.")) {
	    String className = oldName.substring(oldName.lastIndexOf(".") + 1);
	    return ("org.tigris.gef.presentation." + className);
        }

        String translated = (String) translationTable.get(oldName);
        LOG.debug("old = " + oldName + " / new = " + translated);
        
        if (translated == null) {
            return oldName;
        }
        
        return translated;
    }

    private String[] entityPaths = {
        "/org/argouml/persistence/",
        "/org/tigris/gef/xml/dtd/",
    };

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#getEntityPaths()
     */
    protected String[] getEntityPaths() {
        return entityPaths;
    }

    // --------- restoring visibility of node compartments -----------

    private FigNode previousNode = null;

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#startElement(
     *          java.lang.String, java.lang.String,
     *          java.lang.String, org.xml.sax.Attributes)
     *
     * Called by the XML framework when an entity starts.
     */
    public void startElement(String uri,
                String localname,
                String elementName,
                Attributes attrList)
    	throws SAXException {

        String descr = null;
        if (attrList != null) {
            descr = attrList.getValue("description");
        }
        if (descr != null) {
            descr = descr.trim();
        }

        if (_elementState == NODE_STATE
                && elementName.equals("group")
                && _currentNode instanceof OperationsCompartmentContainer
                && isOperationsXml(attrList)) {
            // TODO: Is this still useful?
            previousNode = _currentNode;
        } else if (_elementState == DEFAULT_STATE
                && elementName.equals("group")
                && previousNode instanceof AttributesCompartmentContainer
                && isAttributesXml(attrList)) {
            // TODO: Is this still useful?
            _elementState = NODE_STATE;
            _currentNode = previousNode;
        } else {
            // The following is required only for backwards
            // compatability to before fig compartments were
            // introduced in version 0.17
            // TODO: Is this still useful?
            if (_elementState == NODE_STATE
                    && elementName.equals("group")
                    && _currentNode != null
                    && attrList != null
                    && (_currentNode instanceof FigClass
                        || _currentNode instanceof FigInterface)) {
                // compartment of class figure detected
                previousNode = _currentNode; // remember for next compartment
            } else if (_elementState == DEFAULT_STATE
                    && elementName.equals("group")
                    && previousNode != null && _nestedGroups > 0) {
                /* The following should not be necessary, but because of a bug
                   in GEF's PGMLParser, the second FigGroup (which is the
                   operations compartment) is parsed in the wrong state
                   (DEFAULT_STATE). Result: _currentNode is lost (set to null).
                   Solution: use saved version in _previousNode and
                   watch _nestedGroups in order to decide which compartment
                   is parsed. This code should work even with a fixed
                   PGMLParser of GEF.
                   (_elementState) DEFAULT_STATE(=0) is private :-(
                   NODE_STATE = 4
                 */

                _elementState = NODE_STATE;
                _currentNode = previousNode;
            }
        }

        if ("private".equals(elementName)) {
            privateTextDepth++;
        }
        super.startElement(uri, localname, elementName, attrList);
        if (nestedGroupFlag) {
            _diagram.remove(figGroup);
            figGroup = null;
            nestedGroupFlag = false;
        }
    }

    private boolean isAttributesXml(Attributes attrList) {
        if (attrList == null) {
            return false;
        }
        String descr = attrList.getValue("description").trim();
        return (descr.indexOf("FigAttributesCompartment[") > 0);
    }

    private boolean isOperationsXml(Attributes attrList) {
        if (attrList == null) {
            return false;
        }
        String descr = attrList.getValue("description").trim();
        return (descr.indexOf("FigOperationsCompartment[") > 0);
    }

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#characters(char[], int, int)
     *
     * Called by the PGML framework when there are characters inside an XML
     * entity. We need to save them if it would turn out to be a private
     * entity.
     */
    public void characters(char[] ch, int start, int length) {
	if (privateTextDepth == 1) {
	    privateText.append(ch, start, length);
        }
	super.characters(ch, start, length);
    }

    /**
     * Sets the ItemUID value of the current element in the file.
     *
     * @param id the given id
     */
    protected void setElementItemUID(String id) {
	switch (_elementState) {
	case DEFAULT_STATE:
	    if (_diagram instanceof ArgoDiagram) {
		((ArgoDiagram) _diagram).setItemUID(new ItemUID(id));
	    }
	    //cat.debug("SetUID: diagram: " + _diagram);
	    break;

	case PRIVATE_NODE_STATE:
	    if (_currentNode instanceof FigNodeModelElement) {
		((FigNodeModelElement) _currentNode)
		    .setItemUID(new ItemUID(id));
	    }
	    //cat.debug("SetUID: node: " + _currentNode);
	    break;

	case PRIVATE_EDGE_STATE:
	    if (_currentEdge instanceof FigEdgeModelElement) {
	        ((FigEdgeModelElement) _currentEdge)
                    .setItemUID(new ItemUID(id));
	    }
	    //cat.debug("SetUID: edge: " + _currentEdge);
	    break;

	default:
	    LOG.debug("SetUID state: " + _elementState);
	}
    }

    /**
     * Utility class to pair a name and a value String together.
     */
    protected class NameVal {
        private String name;
        private String value;

        /**
         * The constructor.
         *
         * @param n the name
         * @param v the value
         */
        NameVal(String n, String v) {
            name = n.trim();
            value = v.trim();
        }

        /**
         * @return returns the name
         */
        String getName() {
            return name;
        }

        /**
         * @return returns the value
         */
        String getValue() {
            return value;
        }
    }

    /**
     * Splits a name value pair into a NameVal instance. A name value pair is
     * a String on the form &lt; name = ["] value ["] &gt;.
     *
     * @param str A String with a name value pair.
     * @return A NameVal, or null if they could not be split.
     */
    protected NameVal splitNameVal(String str) {
	NameVal rv = null;
	int lqpos, rqpos;
	int eqpos = str.indexOf('=');

	if (eqpos < 0) {
	    return null;
	}

	lqpos = str.indexOf('"', eqpos);
	rqpos = str.lastIndexOf('"');

	if (lqpos < 0 || rqpos <= lqpos) {
	    return null;
	}

	rv = new NameVal(str.substring(0, eqpos),
            str.substring(lqpos + 1, rqpos));

	return rv;
    }

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#readDiagram(
     *          java.io.InputStream, boolean)
     */
    public synchronized Diagram readDiagram (
            InputStream is,
            boolean closeStream) throws SAXException {
        
        try {
            String errmsg = "Exception in readDiagram";
    
            //initialise parsing attributes:
            _figRegistry = new HashMap();
            InputSource source = new InputSource(is);
            _nestedGroups = 0; //issue 2452
    
            LOG.info("=======================================");
            LOG.info("== READING DIAGRAM");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            initDiagram("org.tigris.gef.base.Diagram");
            SAXParser pc = factory.newSAXParser();
            source.setSystemId(systemId);
            source.setEncoding("UTF-8");
    
            // what is this for?
            // source.setSystemId(url.toString());
            pc.parse(source, this);
            // source = null;
            if (closeStream) {
                LOG.debug("closing stream now (in PGMLParser.readDiagram)");
                is.close();
            } else {
                LOG.debug("leaving stream OPEN!");
            }
            return _diagram;
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(
     *         java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localname, String name)
        throws SAXException {

        if ("private".equals(name)) {
            if (privateTextDepth == 1) {
                String str = privateText.toString();
                StringTokenizer st = new StringTokenizer(str, "\n");

                while (st.hasMoreElements()) {
                    str = st.nextToken();
                    NameVal nval = splitNameVal(str);

                    if (nval != null) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Private Element: \"" + nval.getName()
                                      + "\" \"" + nval.getValue() + "\"");
                        }
                        if ("ItemUID".equals(nval.getName())
                                && nval.getValue().length() > 0) {
                            setElementItemUID(nval.getValue());
                        }
                    }
                }
            }

            privateTextDepth--;
            if (privateTextDepth == 0) {
                privateText = new StringBuffer();
            }
        }

        switch (_elementState) {
        case NODE_STATE:
            Object own = _currentNode.getOwner();
            if (!_diagram.getNodes(null).contains(own)) {
                _diagram.getNodes(null).add(own);
            }
            break;
        case EDGE_STATE:
            own = _currentEdge.getOwner();
            if (!_diagram.getEdges(null).contains(own)) {
                _diagram.getEdges(null).add(own);
            }
            break;
        case POLY_EDGE_STATE:
            if ("path".equals(name)
                    && _currentPoly != null
                    && _currentPoly.getPointsList().size() == 1) {
                LOG.warn("An edge has been detected with only one point");
                _currentPoly.addPoint(0, 0);
            }
            break;
        }

        super.endElement(uri, localname, name);
    }

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#handleGroup(
     *         org.xml.sax.Attributes)
     *
     * This is a correct implementation of handleGroup and will add
     * FigGroups to the diagram ONLY if they are
     * not a FigNode AND if they are not part of a FigNode.
     */
    protected Fig handleGroup(Attributes attrList) throws SAXException {

        Fig f = null;
        // The description is "figclass[bounds]style"
        String clsNameBounds = attrList.getValue("description");
        if (LOG.isInfoEnabled()) {
            LOG.info(
                "Reading pgml group "
                + attrList.getValue("name")
                + " for class " + clsNameBounds);
        }
        StringTokenizer st = new StringTokenizer(clsNameBounds, ",;[] ");
        String clsName = translateClassName(st.nextToken());
        String xStr = null;
        String yStr = null;
        String wStr = null;
        String hStr = null;
        if (st.hasMoreElements()) {
            xStr = st.nextToken();
            yStr = st.nextToken();
            wStr = st.nextToken();
            hStr = st.nextToken();
        }

        Map attributeMap = interpretStyle(st);

            // TODO: This block should be replaced to use the factories
            // in the Diagram subsystem. The model element type
            // should be determined from the href attribute and then the
            // diagram renderers called as described in issue 859
        try {
            Class nodeClass = Class.forName(translateClassName(clsName));
            f = (Fig) nodeClass.newInstance();
            setStyleAttributes(f, attributeMap);
            LOG.info("Created a " + f.getClass().getName());
        } catch (IllegalAccessException e) {
            // TODO: Change to SAXException on next release of GEF
            LOG.error("IllegalAccessException caught ", e);
            throw new IllegalStateException();
        } catch (InstantiationException e) {
            // TODO: Change to SAXException on next release of GEF
            LOG.error("InstantiationException caught ", e);
            throw new IllegalStateException();
        } catch (ClassNotFoundException e) {
            // TODO: Change to SAXException on next release of GEF
            LOG.error("ClassNotFoundException caught ", e);
            throw new IllegalStateException();
        }
            // End block
    /*
        Object modelElement = getModelElement(attrList);
        if (xStr != null) {
            // The only clue that we have a node is that we have bounds
            // info. Thats what sticking with PGML gives us.
            GraphNodeRenderer figNodeRenderer
                = _diagram.getLayer().getGraphNodeRenderer();
            f = figNodeRenderer.getFigNodeFor(modelElement, attributeMap);
        } else {
            // Otherwise we can only assume this is an edge. But we need to
            // bodge up recognision of comment edges until some stylesheet
            // is in place to make sure they have the correct uuid.
            if (modelElement == null
                    && clsNameBounds.endsWith(".FigEdgeNote")) {
                f = new FigEdgeNote();
            } else {
                GraphEdgeRenderer figEdgeRenderer
                    = _diagram.getLayer().getGraphEdgeRenderer();
                f = figEdgeRenderer.getFigEdgeFor(
                        modelElement,
                        attributeMap);
            }
        }
     */
        
        if (xStr != null && !xStr.equals("")) {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            int w = Integer.parseInt(wStr);
            int h = Integer.parseInt(hStr);
            f.setBounds(x, y, w, h);
        }

        if (f instanceof FigNode) {
            FigNode fn = (FigNode) f;
            _currentNode = fn;
            _elementState = NODE_STATE;
            _textBuf = new StringBuffer();
        }
        if (f instanceof FigNode || f instanceof FigEdge) {
            _diagram.add(f);
        } else {
            // nested group flag is a flag to repair
            // the ^*&(*^*& implementation of GEF's parser
            nestedGroupFlag = true;
            figGroup = f;
            if (_currentNode != null) {
                _currentNode.addFig(f);
            }
        }

        if (f instanceof FigEdge) {
            _currentEdge = (FigEdge) f;
            _elementState = EDGE_STATE;
        }

        setAttrs(f, attrList);
        return f;
    }
    
    /**
     * Set the fig style attributes. This should move into
     * the render factories as described in issue 859.
     * @param fig the fig to style.
     * @param attributeMap a map of name value pairs
     */
    private void setStyleAttributes(Fig fig, Map attributeMap) {
        String name;
        String value;
        Iterator it = attributeMap.keySet().iterator();
        while (it.hasNext()) {
            name = (String) it.next();
            value = (String) attributeMap.get(name);
            
            if ("operationsVisible".equals(name)) {
                ((OperationsCompartmentContainer) fig)
                    .setOperationsVisible(value.equalsIgnoreCase("true"));
            } else if ("attributesVisible".equals(name)) {
                ((AttributesCompartmentContainer) fig)
                    .setAttributesVisible(value.equalsIgnoreCase("true"));
            }
        }
    }
    
    /**
     * Return the model element being referred to by interogating
     * the attributes of the XML group node.
     * @param attrList a collection of name value pairs
     */
    private Object getModelElement(Attributes attrList) {
        String href = attrList.getValue("href");
        return _ownerRegistry.get(href);
    }
    

    /**
     * The StringTokenizer is expected to be positioned at the start a a string
     * of style identifiers in the format name=value;name=value;name=value....
     * Each name value pair is interpreted and the Fig configured accordingly.
     * The value is optional and will default to a value applicable for its
     * name.
     * The current applicable names are operationsVisible and attributesVisible
     * and are used to show or hide the compartments within Class and Interface.
     * The default values are true.
     * @param st The StrinkTokenizer positioned at the first style identifier
     * @return a map of attributes
     */
    private Map interpretStyle(StringTokenizer st) {
        Map map = new HashMap();
        String name;
        String value;
        while (st.hasMoreElements()) {
            String namevaluepair = st.nextToken();
            int equalsPos = namevaluepair.indexOf('=');
            if (equalsPos < 0) {
                name = namevaluepair;
                value = "true";
            } else {
                name = namevaluepair.substring(0, equalsPos);
                value = namevaluepair.substring(equalsPos + 1);
            }
            
            map.put(name, value);
        }
        return map;
    }

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#handlePGML(org.xml.sax.Attributes)
     */
    protected void handlePGML(Attributes attrList) throws SAXException {
        LOG.info("attrList " + attrList);
        super.handlePGML(attrList);
        LOG.info("Diagram name is " + _diagram.getName());
    }
    
    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#privateStateEndElement(java.lang.String)
     */
    protected void privateStateEndElement(String tagName) {
        try {
            if (_currentNode != null) {
                if (_currentEdge != null) {
                    _currentEdge = null;
                }

                String body = _textBuf.toString();
                StringTokenizer st2 = new StringTokenizer(body, "=\"' \t\n");
                while (st2.hasMoreElements()) {
                    String t = st2.nextToken();
                    String v = "no such fig";
                    if (st2.hasMoreElements()) {
                        v = st2.nextToken();
                    }

                    if (t.equals("enclosingFig")) {
                        _currentEncloser = findFig(v);
                    }
                }
            }

            if (_currentEdge != null) {
                Fig spf = null;
                Fig dpf = null;
                FigNode sfn = null;
                FigNode dfn = null;
                String body = _textBuf.toString();
                StringTokenizer st2 = new StringTokenizer(body, "=\"' \t\n");
                while (st2.hasMoreElements()) {
                    String t = st2.nextToken();
                    String v = st2.nextToken();

                    if (t.equals("sourceFigNode")) {
                        sfn = (FigNodeModelElement) _figRegistry.get(v);
                        spf = (Fig) sfn.getPortFigs().get(0);
                    }

                    if (t.equals("destFigNode")) {
                        dfn = (FigNodeModelElement) _figRegistry.get(v);
                        dpf = (Fig) dfn.getPortFigs().get(0);
                    }
                }

                if (spf == null || dpf == null || sfn == null || dfn == null) {
                    setDetectedFailure(true);
                }
                else {
                    _currentEdge.setSourcePortFig(spf);
                    _currentEdge.setDestPortFig(dpf);
                    _currentEdge.setSourceFigNode(sfn);
                    _currentEdge.setDestFigNode(dfn);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#findFig(java.lang.String)
     */
    protected Fig findFig(String uri) {
        Fig f = null;
        if (uri.indexOf(".") == -1) {
            f = (Fig) _figRegistry.get(uri);
        }
        else {
            StringTokenizer st = new StringTokenizer(uri, ".");
            String figNum = st.nextToken();
            f = (Fig) _figRegistry.get(figNum);
            if (f == null) {
                return null;
            }

//            if(f instanceof FigEdge) {
//                return ((FigEdge)f).getFig();
//            }

            while (st.hasMoreElements()) {
                String subIndex = st.nextToken();
                if (f instanceof FigGroup) {
                    FigGroup fg = (FigGroup) f;
                    int i = Integer.parseInt(subIndex);
                    f = (Fig) fg.getFigAt(i);
                }
            }
        }

        return f;
    }
    
} /* end class PGMLParser */

