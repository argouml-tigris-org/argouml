/*
 * Created on Nov 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.argouml.uml.diagram.sequence;

public class ActivationNode extends Node {
    private boolean _cutOffTop;

    private boolean _cutOffBottom;

    private boolean _end;

    private boolean _start;

    public ActivationNode() {}

    /**
     * @return
     */
    public boolean isStart() {
        return _start;
    }

    /**
     * @param b
     */
    public void setStart(boolean b) {
        _start = b;
    }

    public void setEnd(boolean end) {
        _end = end;
    }

    public boolean isEnd() {
        return _end;
    }

    /**
     * @return
     */
    public boolean isCutOffBottom() {
        return _cutOffBottom;
    }

    /**
     * @return
     */
    public boolean isCutOffTop() {
        return _cutOffTop;
    }

    /**
     * @param b
     */
    public void setCutOffBottom(boolean b) {
        if (b && !(_end)) {
            throw new IllegalArgumentException("Cannot cutoff an activationNode that is not an end");
        }
        _cutOffBottom = b;
    }

    /**
     * @param b
     */
    public void setCutOffTop(boolean b) {
        if (b && !(_start)) {
            throw new IllegalArgumentException("Cannot cutoff an activationNode that is not a start");
        }
        _cutOffTop = b;
    }

}