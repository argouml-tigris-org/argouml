/**
 * 
 */
package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.sequence.ui.FigClassifierRole.TempFig;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.HandlerFactory;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.persistence.pgml.PGMLStackParser;
import org.tigris.gef.persistence.pgml.UnknownHandler;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class FigLifeLine extends FigGroup implements HandlerFactory {

    final static int WIDTH = 20;
    final static int HEIGHT = 1000;
    
    private FigRect rect;
    private FigLine line;
    
    /**
     * The set of activation figs.
     */
    private Set activationFigs;
    
    FigLifeLine(int x, int y) {
        super();
        rect = new FigRect(x, y, WIDTH, HEIGHT);
        rect.setFilled(false);
        rect.setLineWidth(0);
        line = new FigLine(x + WIDTH / 2, y, x+ WIDTH / 2, HEIGHT, Color.black);
        line.setDashed(true);
        addFig(rect);
        addFig(line);
        activationFigs = new HashSet();
    }
    
    public Dimension getMinimumSize() {
        return new Dimension(20, 100);
    }
    
    public void setBoundsImpl(int x, int y, int w, int h) {
        rect.setBounds(x, y, WIDTH, h);
        line.setLocation(x + w / 2, y);
        for (Iterator figIt = getFigs().iterator(); figIt.hasNext();) {
            Fig fig = (Fig) figIt.next();
            if (activationFigs.contains(fig)) {
                fig.setLocation(getX(), y - getY() + fig.getY());
            }
            if (fig instanceof FigMessagePort) {
                fig.setLocation(getX(), y - getY() + fig.getY());
            }
        }
        calcBounds();
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        _x = rect.getX();
        _y = rect.getY();
        _w = rect.getWidth();
        _h = rect.getHeight();
        firePropChange("bounds", null, null);
    }

    final void removeActivations() {
        List activations = new ArrayList(activationFigs);
        activationFigs.clear();
        for (Iterator it = activations.iterator();
              it.hasNext();
         ) {
            removeFig((Fig) it.next());
    }
        calcBounds();
    }

    final void addActivationFig(Fig f) {
        addFig(f);
        activationFigs.add(f);
    }

    /**
     * Removes the fig from both the figs list as from the
     * activationFigs set.  This insures
     * that removal will indeed remove all 'pointers' to the object.<p>
     *
     * @see org.tigris.gef.presentation.FigGroup#removeFig(Fig)
     */
    final public void removeFig(Fig f) {
        super.removeFig(f);
        activationFigs.remove(f);
    }
    
    /**
     * Change a node to point to an actual FigMessagePort.
     */
    final FigMessagePort createFigMessagePort(Object message, TempFig tempFig) {
        final MessageNode node = (MessageNode) tempFig.getOwner();
        final FigMessagePort fmp =
            new FigMessagePort(message, tempFig.getX1(), tempFig.getY1(),
                   tempFig.getX2());
        node.setFigMessagePort(fmp);
        fmp.setNode(node);
        addFig(fmp);

        return fmp;
    }
    
    final int getYCoordinate(int nodeIndex) {
        return
            nodeIndex * SequenceDiagramLayout.LINK_DISTANCE
                + getY()
                + SequenceDiagramLayout.LINK_DISTANCE / 2;
    }
    
    /**
     * @see org.tigris.gef.persistence.pgml.HandlerFactory#getHandler(
     * org.tigris.gef.persistence.pgml.HandlerStack, java.lang.Object,
     * java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    public DefaultHandler getHandler(HandlerStack stack,
                               Object container,
                               String uri,
                               String localname,
                               String qname,
                               Attributes attributes)
    throws SAXException {

        PGMLStackParser parser = (PGMLStackParser) stack;
        StringTokenizer st =
        new StringTokenizer(attributes.getValue("description"), ",;[] ");
        if (st.hasMoreElements()) {
            st.nextToken();
        }
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
        if (xStr != null && !xStr.equals("")) {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            int w = Integer.parseInt(wStr);
            int h = Integer.parseInt(hStr);
            setBounds(x, y, w, h);
        }
        PGMLStackParser.setCommonAttrs(this, attributes);
        String ownerRef = attributes.getValue("href");
        if (ownerRef != null) {
            Object owner = parser.findOwner(ownerRef);
            if (owner != null) {
                setOwner(owner);
            }
        }
        parser.registerFig(this, attributes.getValue("name"));
        ((Container) container).addObject(this);
        return new FigLifeLineHandler(parser, this);
    }
    
    static class FigLifeLineHandler extends FigGroupHandler {
        /**
         * Constructor.
         *
         * @param parser
         * @param classifierRole
         */
        FigLifeLineHandler(PGMLStackParser parser,
                FigLifeLine lifeLine) {
            super(parser, lifeLine);
            System.out.println("Creating a FigLifeLineHandler");
        }

        /**
         * @see org.tigris.gef.persistence.pgml.BaseHandler#getElementHandler(
         *         org.tigris.gef.persistence.pgml.HandlerStack,
         *         java.lang.Object, java.lang.String, java.lang.String,
         *         java.lang.String, org.xml.sax.Attributes)
         */
        protected DefaultHandler getElementHandler(
                HandlerStack stack,
                Object container,
                String uri,
                String localname,
                String qname,
                Attributes attributes) throws SAXException {

            DefaultHandler result = null;
            String description = attributes.getValue("description");
            // Handle stereotype groups in Figs
            if (qname.equals("group")
                    && description != null
                    && description.startsWith(FigMessagePort.class.getName())) {
                PGMLStackParser parser = (PGMLStackParser) stack;
                String ownerRef = attributes.getValue("href");
                Object owner = parser.findOwner(ownerRef);
                FigMessagePort fmp = new FigMessagePort(owner);
                ((FigGroupHandler) container).getFigGroup().addFig(fmp);
                result = new FigGroupHandler((PGMLStackParser) stack, fmp);
                PGMLStackParser.setCommonAttrs(fmp, attributes);
                parser.registerFig(fmp, attributes.getValue("name"));
            } else {
                result = new UnknownHandler(stack);
            }
            return result;
        }
    }
}