package org.argouml.persistence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import java.util.StringTokenizer;

import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;

import org.argouml.gef.HandlerStack;
import org.argouml.gef.FigGroupHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class PGMLStackParser extends org.argouml.gef.PGMLStackParser {

    private static final Logger LOG = Logger.getLogger(PGMLStackParser.class);

    private HashMap translationTable = new HashMap();

    /**
     * Constructor.
     * @param modelElementsByUuid a map of model elements indexed
     *                            by a unique string identifier.
     */
    public PGMLStackParser(Map modelElementsByUuid) {
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
     * @see org.tigris.gef.xml.pgml.PGMLStackParser#translateClassName(java.lang.String)
     */
    public String translateClassName(String oldName) {
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

    /**
     * @see org.argouml.gef.HandlerFactory#getHandler
     */
    public DefaultHandler getHandler( HandlerStack stack,
                                             Object container,
                                             String uri,
                                             String localname,
                                             String qname,
                                             Attributes attributes)
    throws SAXException
    {
/*
        // Ignore things within nodes
        if ( qname.equals( "group") && container instanceof
             FigGroupHandler)
        {
            FigGroup group=((FigGroupHandler)container).getFigGroup();
            if ( group instanceof AttributesCompartmentContainer ||
                group instanceof OperationsCompartmentContainer)
            {
                String description=attributes.getValue( "description");
                if ( description.indexOf( "Compartment")!= -1)
                    return null;
            }
        }
        // Don't load text within node
        if ( qname.equals( "text") && container instanceof
             FigGroupHandler)
        {
            FigGroup group=((FigGroupHandler)container).getFigGroup();
            if ( group instanceof FigNode)
                return null;
        }
 */
        if ( container instanceof FigGroupHandler)
        {
            FigGroup group=((FigGroupHandler)container).getFigGroup();
            if ( group instanceof FigNode && ! qname.equals( "private"))
                return null;
        }
        DefaultHandler result=super.getHandler(
            stack, container, uri, localname, qname, attributes);

        if ( result instanceof FigGroupHandler)
        {
            FigGroup group=((FigGroupHandler)result).getFigGroup();
            {
                String clsNameBounds = attributes.getValue("description");
                if ( clsNameBounds!=null)
                {
                    StringTokenizer st = new StringTokenizer(clsNameBounds,
                        ",;[] ");
                    // Discard class name, x y w h
                    if ( st.hasMoreElements()) st.nextToken();
                    if ( st.hasMoreElements()) st.nextToken();
                    if ( st.hasMoreElements()) st.nextToken();
                    if ( st.hasMoreElements()) st.nextToken();
                    if ( st.hasMoreElements()) st.nextToken();

                    Map attributeMap = interpretStyle(st);
                    setStyleAttributes( group, attributeMap);
                }
            }
        }

        return result;
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
            }
            else {
                name = namevaluepair.substring(0, equalsPos);
                value = namevaluepair.substring(equalsPos + 1);
            }

            map.put(name, value);
        }
        return map;
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
}
