// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.xml.pgml;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.AttributeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.argouml.cognitive.ItemUID;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.HasAttributesCompartment;
import org.argouml.uml.diagram.ui.HasFeaturesCompartment;
import org.argouml.uml.diagram.ui.HasOperationsCompartment;
import org.apache.log4j.Logger;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;

public class PGMLParser extends org.tigris.gef.xml.pgml.PGMLParser {
    
    /**
     * HACK to handle issue 2719.
     */
    private boolean nestedGroupFlag = false;
    
    /**
     * HACK to handle issue 2719
     */
    private Fig figGroup = null;
    
    private static final Logger LOG = Logger.getLogger(PGMLParser.class);

    private int privateTextDepth = 0;
    private StringBuffer privateText = new StringBuffer();

    ////////////////////////////////////////////////////////////////
    // static variables

    public static final PGMLParser SINGLETON = new PGMLParser();

    private HashMap translationTable = new HashMap();

    /**
     * Constructor
     */
    protected PGMLParser() {
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


    protected String translateClassName(String oldName) {
        if ("org.argouml.uml.diagram.static_structure.ui.FigNote"
            .equals(oldName))
            return "org.argouml.uml.diagram.static_structure.ui.FigComment";
	if ("org.argouml.uml.diagram.state.ui.FigState".equals(oldName))
	    return "org.argouml.uml.diagram.state.ui.FigSimpleState";
        if ( oldName.startsWith("org.") ) return oldName;

        if ( oldName.startsWith("uci.gef.") ) {
	    String className = oldName.substring(oldName.lastIndexOf(".") + 1);
	    return ("org.tigris.gef.presentation." + className);
        }

        String translated = (String) translationTable.get(oldName);
        LOG.debug( "old = " + oldName + " / new = " + translated );
        return translated;
    }

    private String[] entityPaths = {
        "/org/argouml/xml/dtd/",
        "/org/tigris/gef/xml/dtd/" 
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
     *          java.lang.String, org.xml.sax.AttributeList)
     *
     * Called by the XML framework when an entity starts.
     */
    public void startElement(String elementName, AttributeList attrList) {

        String descr = null;
        if (attrList != null) {
            descr = attrList.getValue("description");
        }
        if (descr != null) {
            descr = descr.trim();
        }
        if (_elementState == NODE_STATE && elementName.equals("group") 
                && _currentNode instanceof HasOperationsCompartment
                && isOperationsXml(attrList)) {
            if (isHiddenXml(descr)) {
                ((HasOperationsCompartment) _currentNode).setOperationsVisible(false);
            }
            previousNode = _currentNode;
        } else if (_elementState == DEFAULT_STATE && elementName.equals("group") 
                && previousNode instanceof HasAttributesCompartment
                    && isAttributesXml(attrList)) {
            _elementState = NODE_STATE;
            _currentNode = previousNode;
            if (isHiddenXml(descr)) {
                ((HasAttributesCompartment) previousNode).setAttributesVisible(false);
            }
        } else {
            // The following is required only for backwards
            // compatability to before fig compartments existed
            if (_elementState == NODE_STATE && elementName.equals("group") 
                    && _currentNode != null && attrList != null 
                    && (_currentNode instanceof FigClass  
                        || _currentNode instanceof FigInterface)) {
                // compartment of class figure detected
                if (isHiddenXml(descr)) {
                    // the detected compartment need to be hidden
                    ((FigNodeModelElement) _currentNode).enableSizeChecking(false);
                    if (_currentNode != previousNode) {
                        // it's the first compartment of the class:
                        if (_currentNode instanceof FigClass) {
                            ((FigClass) _currentNode).setAttributesVisible(false);
                        } else {
                            ((FigInterface) _currentNode).setOperationsVisible(false);
                        }
                    } else {
                        // never reached due to bug in GEF (see below)
                        ((FigClass) _currentNode).setOperationsVisible(false);
                    }
                    ((FigNodeModelElement) _currentNode).enableSizeChecking(true);
                }
                previousNode = _currentNode; // remember for next compartment
            } else if (_elementState == DEFAULT_STATE && elementName.equals("group") 
                    && previousNode != null && _nestedGroups > 0) { 
                // The following should not be necessary, but because of a bug in GEF's
                // PGMLParser, the second FigGroup (which is the operations compartment)
                // is parsed in the wrong state (DEFAULT_STATE). Result: _currentNode is
                // lost (set to null). Solution: use saved version in _previousNode and
                // watch _nestedGroups in order to decide which compartment is parsed.
                // This code should work even with a fixed PGMLParser of GEF.
                // (_elementState) DEFAULT_STATE(=0) is private :-(
                // NODE_STATE = 4
                
                _elementState = NODE_STATE;
                _currentNode = previousNode;
                if (isHiddenXml(descr) && previousNode instanceof FigClass) {
            	    ((FigNodeModelElement) previousNode).enableSizeChecking(false);
            	    ((FigClass) previousNode).setOperationsVisible(false);
            	    ((FigNodeModelElement) previousNode).enableSizeChecking(true);
                }
            }
        }
        
        if ("private".equals(elementName)) {
            privateTextDepth++;
        }
        super.startElement(elementName, attrList);
        if (nestedGroupFlag) {
            _diagram.remove(figGroup);
            figGroup = null;
            nestedGroupFlag = false;
        }
        
    }
    
    private boolean isAttributesXml(AttributeList attrList) {
        if (attrList == null) {
            return false;
        }
        String descr = attrList.getValue("description").trim();
        return (descr.indexOf("FigAttributesCompartment[") > 0);
    }

    private boolean isOperationsXml(AttributeList attrList) {
        if (attrList == null) {
            return false;
        }
        String descr = attrList.getValue("description").trim();
        return (descr.indexOf("FigOperationsCompartment[") > 0);
    }

    /**
     * The fig is denoted as being hidden in the XML if
     * its description contains co-ordinates and size all
     * of zero
     * @param text
     * @return if the XML denotes a hidden Fig.
     */
    private boolean isHiddenXml(String text) {
        return text.endsWith("[0, 0, 0, 0]") || text.endsWith("[0,0,0,0]");
    }

    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#characters(char[], int, int)
     *
     * Called by the PGML framework when there are characters inside an XML
     * entity. We need to save them if it would turn out to be a private
     * entity.
     */
    public void characters(char[] ch, int start, int length) {
	if (privateTextDepth == 1)
	    privateText.append(ch, start, length);
	super.characters(ch, start, length);
    }

    /**
     * Sets the ItemUID value of the current element in the file.
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
		((FigNodeModelElement) _currentNode).setItemUID(new ItemUID(id));
	    }
	    //cat.debug("SetUID: node: " + _currentNode);
	    break;

	case PRIVATE_EDGE_STATE:
	    if (_currentEdge instanceof FigEdgeModelElement) {
		((FigEdgeModelElement) _currentEdge).setItemUID(new ItemUID(id));
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
	String name;
	String value;
    }

    /**
     * Splits a name value pair into a NameVal instance. A name value pair is
     * a String on the form <name = ["] value ["]>.
     * 
     * @param str A String with a name value pair.
     * @return A NameVal, or null if they could not be split.
     */
    protected NameVal splitNameVal(String str) {
	NameVal rv = null;
	int lqpos, rqpos;
	int eqpos = str.indexOf('=');

	if (eqpos < 0)
	    return null;

	lqpos = str.indexOf('"', eqpos);
	rqpos = str.lastIndexOf('"');

	if (lqpos < 0 || rqpos <= lqpos)
	    return null;

	rv = new NameVal();
	rv.name = str.substring(0, eqpos);
	rv.value = str.substring(lqpos + 1, rqpos);

	return rv;
    }
  
    /**
     * @see org.tigris.gef.xml.pgml.PGMLParser#readDiagram(
     *          java.io.InputStream, boolean)
     */
    public synchronized Diagram readDiagram(InputStream is,
            				    boolean closeStream) {
        String errmsg = "Exception in readDiagram";
        
        //initialise parsing attributes:
        _figRegistry = new HashMap();
        InputSource source = new InputSource(is);
        _nestedGroups = 0; //issue 2452
            
        try {
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
            }
            else {
                LOG.debug("leaving stream OPEN!");
            }
            return _diagram;
        }
        catch (SAXException saxEx) {
            //
            //  a SAX exception could have been generated
            //    because of another exception.
            //    Get the initial exception to display the
            //    location of the true error
            Exception ex = saxEx.getException();
            if (ex == null) {
                LOG.error(errmsg, saxEx);
            }
            else {
                LOG.error(errmsg, ex);
            }
        }
        catch (Exception ex) {
            LOG.error(errmsg, ex);
        }
        return null;
    }

    /**
     * @see org.xml.sax.DocumentHandler#endElement(java.lang.String)
     */
    public void endElement(String arg0)  {
	if ("private".equals(arg0)) {
	    if (privateTextDepth == 1) {
		String str = privateText.toString();
		StringTokenizer st = new StringTokenizer(str, "\n");

		while (st.hasMoreElements()) {
		    str = st.nextToken();
		    NameVal nval = splitNameVal(str);
		    //cat.debug("Private Element: \"" + str + "\"");

		    if (nval != null) {
			LOG.debug("Private Element: \"" + nval.name
			          + "\" \"" + nval.value + "\"");
			if ("ItemUID".equals(nval.name.trim())) {
			    nval.value = nval.value.trim();
			    if (nval.value.length() > 0)
				setElementItemUID(nval.value);
			}
		    }
		}
	    }

	    privateTextDepth--;
	    if (privateTextDepth == 0)
		privateText = new StringBuffer();
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
        }

        super.endElement(arg0);
    }
    
   /**
    * This is a correct implementation of handleGroup and will add FigGroups to the diagram ONLY if they are
    * not a FigNode AND if they are not part of a FigNode.
    * @see org.tigris.gef.xml.pgml.PGMLParser#handleGroup(org.xml.sax.AttributeList)
    */
 protected Fig handleGroup(AttributeList attrList) {
     //System.out.println("[PGMLParser]: handleGroup: ");
     Fig f = null;
     String clsNameBounds = attrList.getValue("description");
     StringTokenizer st = new StringTokenizer(clsNameBounds, ",;[] ");
     String clsName = translateClassName(st.nextToken());
     String xStr = null;
     String yStr = null;
     String wStr = null;
     String hStr = null;
     if(st.hasMoreElements()) {
         xStr = st.nextToken();
         yStr = st.nextToken();
         wStr = st.nextToken();
         hStr = st.nextToken();
     }

     try {
         Class nodeClass = Class.forName(translateClassName(clsName));
         f = (Fig)nodeClass.newInstance();
         if(xStr != null && !xStr.equals("")) {
             int x = Integer.parseInt(xStr);
             int y = Integer.parseInt(yStr);
             int w = Integer.parseInt(wStr);
             int h = Integer.parseInt(hStr);
             f.setBounds(x, y, w, h);
         }

         if(f instanceof FigNode) {
             FigNode fn = (FigNode)f;
             _currentNode = fn;
             _elementState = NODE_STATE;
             _textBuf = new StringBuffer();             
         } 
         if (f instanceof FigNode || f instanceof FigEdge) {
             _diagram.add(f);
         }
         else {
             // nested group flag is a flag to repair the ^*&(*^*& implementation of GEF's parser
             nestedGroupFlag = true;
             figGroup = f;
             if (_currentNode != null) {
                 _currentNode.addFig(f);
             }
         }

         if(f instanceof FigEdge) {
             _currentEdge = (FigEdge)f;
             _elementState = EDGE_STATE;
         }  
       
     } catch(Exception ex) {
         LOG.debug("Exception in handleGroup", ex);
         ex.printStackTrace();
     } catch(NoSuchMethodError ex) {
         LOG.debug("No constructor() in class " + clsName, ex);
         ex.printStackTrace();
     }

     setAttrs(f, attrList);
     return f;
 }
 
 


} /* end class PGMLParser */

