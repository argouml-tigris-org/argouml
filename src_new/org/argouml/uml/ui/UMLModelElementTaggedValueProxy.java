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

package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.ModelEventPump;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;

/**
 * This class provides a text field that can be used to access
 * tagged values of a ModelElement object.  Because TaggedValues
 * are separated ModelElements themselves, it acts as a proxy
 * intermediary to an instance of UMLPlainTextDocument and handles
 * the indirection.
 * UMLModelElementTaggedValueDocument is especially useful when
 * using LabelledLayout.
 *
 * @since 15 Feb 2006
 * @author Tom Morris (tfmorris@gmail.com)
 */
public class UMLModelElementTaggedValueProxy implements UMLDocument {

    private static final Logger LOG =
        Logger.getLogger(UMLModelElementTaggedValueProxy.class);

    /**
     * The target of the propertypanel that's behind this property.
     */
    private Object panelTarget = null;

    /**
     * The name (tagType) of the type (TagDefinition) of the TaggedValue
     * that this document shows.
     */
    private String tagName = null;
    private static final String EVENT_NAME = "taggedValue";
    
    private UMLModelElementTaggedValueDocument document;

    /**
     * Creates a UMLPlainTextDocument object that represents a tagged value of
     * an ModelElement object.
     *
     * @param taggedValue the tagged value
     */
    public UMLModelElementTaggedValueProxy(String taggedValue) {
        tagName = taggedValue;
        document = new UMLModelElementTaggedValueDocument("");
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt instanceof AddAssociationEvent) {
            Object tv = evt.getNewValue();
            Object td = Model.getFacade().getTagDefinition(tv);
            if (td != null) {
                String name = (String) Model.getFacade().getType(td);
                if (tagName != null && tagName.equals(name)) {
                    document.setTarget(tv);
                }
            }
        } else if (evt instanceof RemoveAssociationEvent) {
            Object tv = evt.getOldValue();
            Object td = Model.getFacade().getTagDefinition(tv);
            if (td != null) {
                String name = (String) Model.getFacade().getType(td);
                if (tagName != null && tagName.equals(name)) {
                    document.setTarget(null);
                }      
            }
        } else {
            document.propertyChange(evt);
        }
    }
    
    /**
     * Sets the tagged value to given String.
     *
     * @param text the property
     */
    protected void setProperty(String text) {
        document.setProperty(text);
    }

    /**
     *
     * @return the value of the tagged value
     */
    protected String getProperty() {
        return document.getProperty();
    }
    
    /**
     * Returns the target.
     * @return Object
     */
    public final Object getTarget() {
        return panelTarget;
    }

    /**
     * Sets the target.
     * @param target The target to set
     */
    public final void setTarget(Object target) {
        target = target instanceof Fig ? ((Fig) target).getOwner() : target;
        if (Model.getFacade().isAModelElement(target)) {
            if (target != panelTarget) {
                ModelEventPump eventPump = Model.getPump();
                if (panelTarget != null) {
                    eventPump.removeModelEventListener(this, panelTarget,
                            EVENT_NAME);
                }
                panelTarget = target;
                eventPump.addModelEventListener(this, panelTarget, EVENT_NAME);
                // TODO: see if the new target has a TV that we can proxy
                document.setTarget(Model.getFacade().getTaggedValue(
                        panelTarget, tagName));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////
    // Proxy methods for real UMLModelElementTaggedValue
    ///////////////////////////////////////////////////////////////////////
    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see javax.swing.text.Document#insertString(
     *         int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException {
        document.insertString(offset, str, a);
    }

    /**
     * @see javax.swing.text.Document#remove(int, int)
     */
    public void remove(int offs, int len) throws BadLocationException {
        document.remove(offs, len);
    }

    /**
     * @see javax.swing.text.Document#getDefaultRootElement()
     */
    public Element getDefaultRootElement() {
        return document.getDefaultRootElement();
    }

    /**
     * @see javax.swing.text.Document#getLength()
     */
    public int getLength() {
        return document.getLength();
    }

    /**
     * @see javax.swing.text.Document#render(Runnable r)
     */
    public void render(Runnable r) {
        document.render(r);
    }

    /**
     * @see javax.swing.text.Document#getText(int, int)
     */
    public String getText(int offset, int length) throws BadLocationException {
        return document.getText(offset, length);
    }

    /**
     * @see javax.swing.text.Document#addDocumentListener(javax.swing.event.DocumentListener)
     */
    public void addDocumentListener(DocumentListener listener) {
        document.addDocumentListener(listener);
    }

    /**
     * @see javax.swing.text.Document#removeDocumentListener(javax.swing.event.DocumentListener)
     */
    public void removeDocumentListener(DocumentListener listener) {
        document.removeDocumentListener(listener);
    }

    /**
     * @see javax.swing.text.Document#addUndoableEditListener(javax.swing.event.UndoableEditListener)
     */
    public void addUndoableEditListener(UndoableEditListener listener) {
        document.addUndoableEditListener(listener);
    }

    /**
     * @see javax.swing.text.Document#removeUndoableEditListener(javax.swing.event.UndoableEditListener)
     */
    public void removeUndoableEditListener(UndoableEditListener listener) {
        document.removeUndoableEditListener(listener);
    }

    /**
     * @see javax.swing.text.Document#getRootElements()
     */
    public Element[] getRootElements() {
        return document.getRootElements();
    }

    /**
     * @see javax.swing.text.Document#getEndPosition()
     */
    public Position getEndPosition() {
        return document.getEndPosition();
    }

    /**
     * @see javax.swing.text.Document#getStartPosition()
     */
    public Position getStartPosition() {
        return document.getStartPosition();
    }

    /**
     * @see javax.swing.text.Document#createPosition(int)
     */
    public Position createPosition(int offs) throws BadLocationException {
        return document.createPosition(offs);
    }

    /**
     * @see javax.swing.text.Document#getText(int, int, javax.swing.text.Segment)
     */
    public void getText(int offset, int length, Segment txt)
            throws BadLocationException {
        document.getText(offset, length, txt);
    }

    /**
     * @see javax.swing.text.Document#getProperty(java.lang.Object)
     */
    public Object getProperty(Object key) {
        return document.getProperty(key);
    }

    /**
     * @see javax.swing.text.Document#putProperty(java.lang.Object, java.lang.Object)
     */
    public void putProperty(Object key, Object value) {
        document.putProperty(key, value);
    }


}
