/*
 * OldLayoutConverter.java
 *
 * Created on 21. Januar 2003, 09:23
 */

package org.argouml.uml.diagram.sequence.specification_level;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.uml.diagram.sequence.ui.FigSeqLink;
import org.argouml.uml.diagram.sequence.ui.FigSeqObject;
import org.argouml.uml.diagram.sequence.ui.FigSeqStimulus;

import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqConstants;

import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;

import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.FigDynPort;

/**
 *
 * @author  Administrator
 */
public class OldLayoutConverter implements FigSeqConstants {

    /** Creates a new instance of OldLayoutConverter */
    private OldLayoutConverter() {
    }

    private static Vector getFigSeqObjects(Layer lay) {
        Vector   r = new Vector();
        Iterator i = lay.getContents().iterator();
        Object   o = null;
        while (i.hasNext()) {
            o = i.next();
            if (o instanceof FigSeqObject) {
                r.add(o);
            }
        }
        return r;
    }

    private static Vector getFigSeqLinks(Layer lay) {
        Vector   r = new Vector();
        Iterator i = lay.getContents().iterator();
        Object   o = null;
        while (i.hasNext()) {
            o = i.next();
            if (o instanceof FigSeqLink) {
                r.add(o);
            }
        }
        return r;
    }

    private static Vector getFigSeqStimuli(Layer lay) {
        Vector   r = new Vector();
        Iterator i = lay.getContents().iterator();
        Object   o = null;
        while (i.hasNext()) {
            o = i.next();
            if (o instanceof FigSeqStimulus) {
                r.add(o);
            }
        }
        return r;
    }

    private static int getMaxStimulusWidth(Layer lay, FigSeqObject o1, FigSeqObject o2) {
        // Maximum length of stimuli not needed, because layout is only
        // done to determine message sequence ordering
        return OBJECT_DEFAULT_DIST;
    }

    public static void computeOldLayoutData(Layer lay) {
        Hashtable figDimensions = new Hashtable();
        int i,j,k,width,height,x1,x2;
        Rectangle rect;

        int x = ORIGIN_X;
        int y = ORIGIN_Y;
        FigSeqObject obj,objLeft,objRight;

        Vector figSeqObjects = getFigSeqObjects(lay);
        Vector figSeqLinks   = getFigSeqLinks(lay);
        Vector figSeqStimuli = getFigSeqStimuli(lay);

        int objSize = figSeqObjects.size();
        for(i = 0; i < objSize; i++) {
            obj= (FigSeqObject) figSeqObjects.elementAt(i);
            width = obj.getNameFig().getMinimumSize().width;
            height = obj.getNameFig().getMinimumSize().height;
            rect = new Rectangle(x,y,width,height);
            figDimensions.put(obj, rect);
            x = x + OBJECT_DEFAULT_DIST + width;
        }

        for (i = 0; i < objSize - 1; i++) {
            objLeft = (FigSeqObject)figSeqObjects.elementAt(i);
            for  (j = i + 1; j < objSize; j++) {
                // calculate distance between two objects
                objRight = (FigSeqObject)figSeqObjects.elementAt(j);
                // search for stimulus (+link) between o1 and o2 with the max. width
                int maxStimWidth = getMaxStimulusWidth(lay, objLeft, objRight);
                // actual gap between the two objects
                x1 = ((Rectangle) figDimensions.get(objLeft)).x ;
                x2 = ((Rectangle) figDimensions.get(objRight)).x ;
                int objWidth =  ((Rectangle) figDimensions.get(objLeft)).width ;
                if ( maxStimWidth > (x2-(x1+objWidth)) ) {
                    // name of stimulus is too long -> the gap between every object objLeft,...,objRight
                    // has to be increased
                    int gap = 25+(maxStimWidth-(x2-(x1+objWidth)))/(j-i);
                    for  (k=i+1; k<objSize; k++) {
                        obj = (FigSeqObject) figSeqObjects.elementAt(k);
                        rect = (Rectangle) figDimensions.get(obj) ;
                        x = rect.x+gap;
                        y = rect.y;
                        rect.setLocation(x,y);
                    }
                }
            }
        }

        int linkSize = figSeqLinks.size();
        for (i=0; i<objSize; i++) {
            obj= (FigSeqObject) figSeqObjects.elementAt(i);
            rect = (Rectangle) figDimensions.get(obj) ;
            obj.startTrans();
            obj.setBounds(rect.x, rect.y, rect.width,rect.height, linkSize);
            obj.endTrans();
        }

        for (i=0; i<linkSize; i++) {
            FigSeqLink link = (FigSeqLink) figSeqLinks.elementAt(i);
            int portNumber = link.getPortNumber(lay.getContents());
            FigSeqObject sourcePort = (FigSeqObject) link.getSourceFigNode();
            FigSeqObject destPort = (FigSeqObject) link.getDestFigNode();

            if (link.getSourcePortFig() == sourcePort.getLifeline()) {
                // new link
                for (j=0;j<figSeqObjects.size(); j++) {
                    FigSeqObject fso = (FigSeqObject) figSeqObjects.elementAt(j);
                    Enumeration e = fso._ports.elements();
                    while (e.hasMoreElements()) {
                        FigDynPort fsp = (FigDynPort) e.nextElement();
                        int pos = fsp.getPosition();
                        if (pos >= portNumber) {
                            fsp.setPosition(pos+1);
                            int dynPos = fsp.getDynVectorPos();
                            fso._dynVector.removeElementAt(dynPos);
                            String newDynStr = "b|"+fsp.getPosition();
                            fso._dynVector.insertElementAt(newDynStr, dynPos);
                            fso._dynObjects = fso._dynVector.toString();
                        }
                    }
                    if (fso.isTerminated() && fso.getTerminateHeight() >= portNumber) fso.setTerminateHeight(fso.getTerminateHeight() + 1);
                    if (fso._created && fso._createHeight >= portNumber) {
                        fso._createHeight++;
                    }
                    link.setActivations(fso, sourcePort, destPort, portNumber);
                }
                // add a new port to source object and connect the link to it
                FigDynPort _port1 = new FigDynPort(10, 10, 15, 5, Color.black, Color.white, portNumber);
                sourcePort._ports.addElement(_port1);
                sourcePort.addFig(_port1);
                sourcePort.bindPort(sourcePort.getOwner(), _port1);
                link.setSourcePortFig(_port1);
                // the dynamic Vector has to be updated
                String dynStr = "b|"+_port1.getPosition();
                sourcePort._dynVector.addElement(dynStr);
                _port1.setDynVectorPos(sourcePort._dynVector.indexOf(dynStr));
                sourcePort._dynObjects = sourcePort._dynVector.toString();
            }
            if (link.getDestPortFig() == destPort.getLifeline()) {
                // new link -> add new port to destination object and connect the link to it
                FigDynPort _port2 = new FigDynPort(10, 10, 15, 5, Color.black, Color.white, portNumber);
                destPort._ports.addElement(_port2);
                destPort.addFig(_port2);
                destPort.bindPort(destPort.getOwner(), _port2);
                link.setDestPortFig(_port2);
                // update the dynVector
                String dynStr = "b|"+_port2.getPosition();
                destPort._dynVector.addElement(dynStr);
                _port2.setDynVectorPos(destPort._dynVector.indexOf(dynStr));
                destPort._dynObjects = destPort._dynVector.toString();
            }
            // after all objects are in place the stimuli has to be set in front of the layer
            // to keep it all selectable
            Collection col = ((MLink)link.getOwner()).getStimuli();
            MStimulus stimulus = null;
            Iterator it = col.iterator();
            while (it.hasNext()) {
                stimulus = (MStimulus) it.next();
            }
//            if (stimulus != null && this.presentationFor(stimulus) != null) {
//                this.bringToFront(this.presentationFor(stimulus) );
//            }
        }

        for (i=0; i<objSize; i++) {
            obj= (FigSeqObject) figSeqObjects.elementAt(i);
            rect = (Rectangle) figDimensions.get(obj) ;
            obj.startTrans();
            obj.setBounds(rect.x, rect.y, rect.width,rect.height, linkSize);
            obj.endTrans();
        }
        figDimensions=null;
    }

}
