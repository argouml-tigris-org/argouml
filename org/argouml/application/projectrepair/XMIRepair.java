package org.argouml.application.projectrepair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.argouml.uml.UUIDManager;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XMIRepair extends AbstractRepairImpl {

    public static final String XMI_ID = "xmi.id";
    public static final String XMI_REF = "xmi.idref";
    public static final String XMI_UUID = "xmi.uuid";

    Map _xmiRefs = new HashMap();

    /**
     * Constructor for XMIRepair.
     */
    public XMIRepair() {
        super();
    }

    protected boolean hasXMIid(Node node) {
        NamedNodeMap map = node.getAttributes();
        if (map != null && map.getNamedItem(XMI_ID) != null)
            return true;
        return false;
    }

    protected String getXMIid(Node node) {
        return node.getAttributes().getNamedItem(XMI_ID).getNodeValue();
    }

    protected void parseForXMIids(Node node) {
        if (hasXMIid(node)) {
            _xmiRefs.put(getXMIid(node), node);
        }
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            parseForXMIids(children.item(i));
        }
    }

    /**
     * Removes a node from the XMI. 
     * @param node
     */
    protected void removeFromXMI(Node node) {
        /*
        while (true) { // search the nodes
            Node parentNode = node.getParentNode();
            if (hasXMIid(parentNode)) {
                parentNode.removeChild(node);
                break;
            }
            node = parentNode;
        }
        */
        Node parent = node.getParentNode();
        Node grandParentNode = parent.getParentNode();
        grandParentNode.removeChild(parent);
        // node.getParentNode().getParentNode().removeChild(node);
    }

    protected void parseForXMIidRefsAndRemove(Node node) {
        NamedNodeMap map = node.getAttributes();
        if (map != null && map.getLength()>0) {
            Node xmiRef = map.getNamedItem(XMI_REF);
            if (xmiRef != null
                && _xmiRefs.get(xmiRef.getNodeValue()) == null) {
                // ouch we got a rotten one
                removeFromXMI(node);
            }
        }
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            parseForXMIidRefsAndRemove(children.item(i));
        }
    }


protected void updateXMIidsWithUUIDs() {
    Iterator it = _xmiRefs.values().iterator();
    while (it.hasNext()) {
        Node node = (Node) it.next();
        NamedNodeMap map = node.getAttributes();
        if (map.getNamedItem(XMI_UUID) == null) {
            Attr attribute = _document.createAttribute(XMI_UUID);
            attribute.setNodeValue(UUIDManager.SINGLETON.getNewUUID());
            map.setNamedItem(attribute);
        }
    }
}

/**
 * @see org.argouml.filerepair.AbstractRepairImpl#internalRepairDocument()
 */
protected void internalRepairDocument() {
    // Lets remove references to faulty xmi.ids.
    // first we have to find all xmi.ids
    // then we have to find all references and remove those that are non-
    // existing.
    parseForXMIids(_document);
    parseForXMIidRefsAndRemove(_document);
    // Lets add UUID refs to all elements with an XMI.id.
    // This WILL change the model and is actually only needed for elements
    // that can be displayed since it is the linking pin between the 
    // diagrams and the XMI.
    updateXMIidsWithUUIDs();
}

}
