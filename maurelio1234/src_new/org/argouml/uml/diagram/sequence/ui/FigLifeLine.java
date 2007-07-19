// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.sequence.ui.FigClassifierRole.TempFig;
import org.argouml.uml.diagram.ui.ArgoFigGroup;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.HandlerFactory;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.persistence.pgml.PGMLStackParser;
import org.tigris.gef.persistence.pgml.UnknownHandler;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class FigLifeLine extends ArgoFigGroup implements HandlerFactory {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1242239243040698287L;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigLifeLine.class);

    static final int WIDTH = 20;
    static final int HEIGHT = 1000;
    private FigRect rect;
    private FigLine line;

    /**
     * The set of activation figs.
     */
    private Set activationFigs;

    /**
     * Constructor.
     *
     * @param x
     * @param y
     */
    FigLifeLine(int x, int y) {
        super();
        rect = new FigRect(x, y, WIDTH, HEIGHT);
        rect.setFilled(false);
        rect.setLineWidth(0);
        line =
            new FigLine(x + WIDTH / 2, y, x + WIDTH / 2, HEIGHT, Color.black);
        line.setDashed(true);
        addFig(rect);
        addFig(line);
        activationFigs = new HashSet();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return new Dimension(20, 100);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
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

    /*
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
        for (Iterator it = activations.iterator(); it.hasNext();) {
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
    public final void removeFig(Fig f) {
        LOG.info("Removing " + f.getClass().getName());
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
            nodeIndex * SequenceDiagramLayer.LINK_DISTANCE
                + getY()
                + SequenceDiagramLayer.LINK_DISTANCE / 2;
    }

    /*
     * @see org.tigris.gef.persistence.pgml.HandlerFactory#getHandler(
     *      org.tigris.gef.persistence.pgml.HandlerStack, java.lang.Object,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.xml.sax.Attributes)
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
         */
        FigLifeLineHandler(PGMLStackParser parser,
                FigLifeLine lifeLine) {
            super(parser, lifeLine);
        }

        /*
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
