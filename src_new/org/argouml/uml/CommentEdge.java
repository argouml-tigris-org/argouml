// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.UUIDManager;


/**
 * An object tagged as being the owner of a FigEdgeNote. Has knowledge
 * about the source and destination of the FigEdgeNote. <p>
 *
 * The source and destination are ModelElements.
 * At least one of them is a Comment - but they may be both Comments.
 *
 * @since Jul 17, 2004
 * @author jaap.branderhorst@xs4all.nl
 */
public class CommentEdge extends NotificationBroadcasterSupport {
    private Object source;
    private Object dest;
    private Object uuid;
    private Object comment;
    private Object annotatedElement;

    public CommentEdge() {
        uuid = UUIDManager.getInstance().getNewUUID();
    }

    /**
     * Constructor.
     *
     * @param theSource the source
     * @param theDest the destination
     */
    public CommentEdge(Object theSource, Object theDest) {
        if (!(Model.getFacade().isAModelElement(theSource))) {
            throw new IllegalArgumentException(
                    "The source of the CommentEdge must be a model element");
        }
        if (!(Model.getFacade().isAModelElement(theDest))) {
            throw new IllegalArgumentException(
                    "The destination of the CommentEdge "
                            + "must be a model element");
        }
        if (Model.getFacade().isAComment(theSource)) {
            comment = theSource;
            annotatedElement = theDest;
        } else {
            comment = theDest;
            annotatedElement = theSource;
        }
        this.source = theSource;
        this.dest = theDest;
        uuid = UUIDManager.getInstance().getNewUUID();
    }

    /**
     * The source of this CommentEdge.
     *
     * @return the source
     */
    public Object getSource() {
        return source;
    }

    /**
     * The destination of this CommentEdge.
     *
     * @return the destination
     */
    public Object getDestination() {
        return dest;
    }

    /**
     * @return the uuid
     */
    public Object getUUID() {
        return uuid;
    }


    /**
     * @param destination The destination to set.
     */
    public void setDestination(Object destination) {
        if (destination == null) {
            throw new IllegalArgumentException(
                    "The destination of a comment edge cannot be null");
        }
        if (!(Model.getFacade().isAModelElement(destination))) {
            throw new IllegalArgumentException(
                    "The destination of the CommentEdge cannot be a "
                    + destination.getClass().getName());
        }
        dest = destination;
    }

    /**
     * @param theSource The source to set.
     */
    public void setSource(Object theSource) {
        if (theSource == null) {
            throw new IllegalArgumentException(
                    "The source of a comment edge cannot be null");
        }
        if (!(Model.getFacade().isAModelElement(theSource))) {
            throw new IllegalArgumentException(
                    "The source of the CommentEdge cannot be a "
                    + theSource.getClass().getName());
        }
        this.source = theSource;
    }

    /**
     * Commit suicide. Adapt the UML model.
     */
    public void delete() {
        if (Model.getFacade().isAComment(source)) {
            Model.getCoreHelper().removeAnnotatedElement(source, dest);
        } else {
            // not save to presume the destination is the comment
            if (Model.getFacade().isAComment(dest))
                Model.getCoreHelper().removeAnnotatedElement(dest, source);
        }
        this.sendNotification(new Notification("remove", this, 0));
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // This is the tooltip of a comment link
        return Translator.localize("misc.tooltip.commentlink");
    }

    public Object getAnnotatedElement() {
        return annotatedElement;
    }

    public void setAnnotatedElement(Object theAnnotatedElement) {
        if (theAnnotatedElement == null) {
            throw new IllegalArgumentException(
                    "An annotated element must be supplied");
        }
        if (Model.getFacade().isAComment(theAnnotatedElement)) {
            throw new IllegalArgumentException(
                    "An annotated element cannot be a comment");
        }
        this.annotatedElement = theAnnotatedElement;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object theComment) {
        if (theComment == null) {
            throw new IllegalArgumentException("A comment must be supplied");
        }
        if (!Model.getFacade().isAComment(theComment)) {
            throw new IllegalArgumentException("A comment cannot be a "
                    + theComment.getClass().getName());
        }
        this.comment = theComment;
    }
}
