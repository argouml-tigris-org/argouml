// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.notation.uml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.AssociationEndNameNotation;
import org.argouml.util.MyTokenizer;

/**
 * The UML notation for an associationend name (i.e. the  role).
 * 
 * @author michiel
 */
public class AssociationEndNameNotationUml extends AssociationEndNameNotation {

	/**
	 * Logger
	 */
	private static Logger LOG =
		Logger.getLogger(AssociationEndNameNotationUml.class);
	
    /**
     * The constructor.
     *
     * @param assocEnd the UML associationEnd
     */
    public AssociationEndNameNotationUml(Object assocEnd) {
        super(assocEnd);
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#addListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void addListener(PropertyChangeListener listener, 
            Object modelElement) {
        Model.getPump().addModelEventListener(
                listener, 
                modelElement, 
                new String[] {"name", "visibility", "stereotype"});
        Collection st = Model.getFacade().getStereotypes(modelElement);
        Iterator iter = st.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            Model.getPump().addModelEventListener(
                    listener, 
                    o, 
                    new String[] {"name", "remove"});
        }
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#removeListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void removeListener(PropertyChangeListener listener, 
            Object modelElement) {
        Model.getPump().removeModelEventListener(
                listener, 
                modelElement, 
                new String[] {"name", "visibility", "stereotype"});
        if (Model.getUmlFactory().isRemoved(modelElement)) {
        	LOG.warn("Can't get stereotypes of removed AssociationEnd " +
        			"There will be listeners left in the event pump");
        	return;
        }
        Collection st = Model.getFacade().getStereotypes(modelElement);
        Iterator iter = st.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            Model.getPump().removeModelEventListener(
                    listener, 
                    o, 
                    new String[] {"name", "remove"});
        }
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#updateListener(java.beans.PropertyChangeListener, java.lang.Object, java.beans.PropertyChangeEvent)
     */
    public void updateListener(PropertyChangeListener listener, 
            Object modelElement,
            PropertyChangeEvent pce) {
        Object obj = pce.getSource();
        if ((obj == modelElement) 
                && "stereotype".equals(pce.getPropertyName())) {
            if (pce instanceof AddAssociationEvent 
                    && Model.getFacade().isAStereotype(pce.getNewValue())) {
                // new stereotype
                Model.getPump().addModelEventListener(
                        listener, 
                        pce.getNewValue(), 
                        new String[] {"name", "remove"});
            }
            if (pce instanceof RemoveAssociationEvent 
                    && Model.getFacade().isAStereotype(pce.getOldValue())) {
                // removed stereotype
                Model.getPump().removeModelEventListener(
                        listener, 
                        pce.getOldValue(), 
                        new String[] {"name", "remove"});
            }
        }
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-association-end-name";
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseAssociationEnd(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.association-end-name";
            Object[] args = {
                pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()),
            };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                Translator.messageFormat(msg, args));
        }
    }

    /**
     * @param role   The AssociationEnd <em>text</em> describes.
     * @param text A String on the above format.
     * @throws ParseException
     *             when it detects an error in the role string. See also
     *             ParseError.getErrorOffset().
     */
    protected void parseAssociationEnd(Object role, String text)
        throws ParseException {
        MyTokenizer st;

        String name = null;
        String stereotype = null;
        String token;

        try {
            st = new MyTokenizer(text, "<<,\u00AB,\u00BB,>>");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if ("<<".equals(token) || "\u00AB".equals(token)) {
                    if (stereotype != null) {
                    	String msg = 
                            "parsing.error.association-name.twin-stereotypes";
                        throw new ParseException(Translator.localize(msg), 
                        		st.getTokenIndex());
                    }

                    stereotype = "";
                    while (true) {
                        token = st.nextToken();
                        if (">>".equals(token) || "\u00BB".equals(token)) {
                            break;
                        }
                        stereotype += token;
                    }
                } else {
                    if (name != null) {
                    	String msg = 
                    		"parsing.error.association-name.twin-names";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }
                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
    	    String ms = "parsing.error.association-name.unexpected-end-element";
            throw new ParseException(Translator.localize(ms),
                    text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (name != null) {
            name = name.trim();
        }

        if (name != null && name.startsWith("+")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getPublic());
        }
        if (name != null && name.startsWith("-")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getPrivate());
        }
        if (name != null && name.startsWith("#")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getProtected());
        }
        if (name != null && name.startsWith("~")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getPackage());
        }
        if (name != null) {
            Model.getCoreHelper().setName(role, name);
        }

        NotationUtilityUml.dealWithStereotypes(role, stereotype, true);
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        String name = Model.getFacade().getName(modelElement);
        if (name == null) {
            name = "";
        }

        Object visi = Model.getFacade().getVisibility(modelElement);
        String visibility = "";
        if (visi != null) {
            visibility = NotationUtilityUml.generateVisibility(visi);
        }
        if (name.length() < 1) {
            visibility = "";
            //this is the temporary solution for issue 1011
        }

        String stereoString = 
            NotationUtilityUml.generateStereotype(modelElement);

        return stereoString + visibility + name;
    }

}
