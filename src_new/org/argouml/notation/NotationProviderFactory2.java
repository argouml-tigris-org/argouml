// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.notation.NotationProvider;
import org.argouml.uml.notation.java.InitNotationJava;
import org.argouml.uml.notation.uml.InitNotationUml;

/**
 *  The NotationProviderFactory2 is a singleton, 
 *  since it is the accesspoint for all Figs 
 *  to access the textual representation of modelobjects, 
 *  and since plugin modules can add extra languages.
 *  
 * @author mvw@tigris.org
 */
public final class NotationProviderFactory2 {

    private static final Logger LOG = 
        Logger.getLogger(NotationProviderFactory2.class);
    
    /**
     * TYPE_NAME the name of the modelelement, e.g. class, package, state
     */
    public static final int TYPE_NAME = 1;

    /**
     * TYPE_TRANSITION the main text shown above the transition.
     */
    public static final int TYPE_TRANSITION = 2;

    /**
     * TYPE_STATEBODY the multiline text shown inside the state body.
     */
    public static final int TYPE_STATEBODY = 3;

    /**
     * TYPE_ACTIONSTATE the text shown in an actionstate.
     */
    public static final int TYPE_ACTIONSTATE = 4;

    /**
     * TYPE_ATTRIBUTE the text shown in a attribute compartment (1 attrib only).
     */
    public static final int TYPE_ATTRIBUTE = 5;

    /**
     * TYPE_OPERATION the text shown in a operation compartment (1 oper only).
     */
    public static final int TYPE_OPERATION = 6;

    /**
     * TYPE_OBJECT the text shown on an object.
     */
    public static final int TYPE_OBJECT = 7;

    /**
     * TYPE_COMPONENTINSTANCE the text shown on a componentInstance.
     */
    public static final int TYPE_COMPONENTINSTANCE = 8;

    /**
     * TYPE_NODEINSTANCE the text shown on a componentInstance.
     */
    public static final int TYPE_NODEINSTANCE = 9;
    
    /**
     * TYPE_TYPE_OBJECTFLOWSTATE_TYPE the text shown on a 
     * objectflowstate's type, i.e. its classifier name.
     */
    public static final int TYPE_OBJECTFLOWSTATE_TYPE = 10;

    /**
     * TYPE_OBJECTFLOWSTATE_STATE the text shown on a 
     * objectflowstate's state.
     */
    public static final int TYPE_OBJECTFLOWSTATE_STATE = 11;

    /**
     * TYPE_CALLSTATE the text shown on a 
     * callstate's state.
     */
    public static final int TYPE_CALLSTATE = 12;

    /**
     * TYPE_CLASSIFIERROLE the text shown on a 
     * classifierrole.
     */
    public static final int TYPE_CLASSIFIERROLE = 13;
    
    /**
     * TYPE_MESSAGE the text shown on a Message 
     * in a Collaborations diagram.
     */
    public static final int TYPE_MESSAGE = 14;

    /**
     * TYPE_EXTENSION_POINT the text shown on a usecase
     * representing the extensionpoint.
     */
    public static final int TYPE_EXTENSION_POINT = 15;

    /**
     * The text shown at the association end that represents the role.
     */
    public static final int TYPE_ASSOCIATION_END_NAME = 16;

    /**
     * The text shown for the association role name.
     */
    public static final int TYPE_ASSOCIATION_ROLE = 17;

    /**
     * defaultLanguage the Notation language used by default, i.e. UML
     */
    private NotationName defaultLanguage;

    /**
     * allLanguages is a HashMap with as key the notationName,
     * and as value a second HashMap. This latter HashMap has as key the "type"
     * converted to Integer, and as value the provider (NotationProvider4).
     */
    private Map allLanguages;

    /**
     * The instance is the singleton.
     */
    private static NotationProviderFactory2 instance;

    /**
     * The constructor.
     */
    private NotationProviderFactory2() {
        super();
        allLanguages = new HashMap();
    }

    /**
     * @return returns the singleton instance
     */
    public static NotationProviderFactory2 getInstance() {
        if (instance == null) {
            instance = new NotationProviderFactory2();
            InitNotationUml.init();
            InitNotationJava.init();
        }
        return instance;
    }

    /**
     * Get a NotationProvider for the current project.
     * 
     * @param type the provider type
     * @return the provider
     * @param object the constructor parameter
     */
    public NotationProvider getNotationProvider(int type,
            Object object) {
        Project proj = ProjectManager.getManager().getCurrentProject();
        NotationName name = proj.getProjectSettings().getNotationName();
        Class clazz = getNotationProviderClass(type, name);
        if (clazz != null) {
            Class[] p = {Object.class};
            Constructor constructor = null;
            try {
                constructor = clazz.getConstructor(p);
            } catch (SecurityException e) {
                // TODO: Auto-generated catch block
                LOG.error(e);
            } catch (NoSuchMethodException e) {
                // TODO: Auto-generated catch block
                LOG.error(e);
            }
            Object[] params = {
                object,
            };

            try {
                return (NotationProvider) constructor.newInstance(params);
            } catch (IllegalArgumentException e) {
                // TODO: Auto-generated catch block
                LOG.error(e);
            } catch (InstantiationException e) {
                // TODO: Auto-generated catch block
                LOG.error(e);
            } catch (IllegalAccessException e) {
                // TODO: Auto-generated catch block
                LOG.error(e);
            } catch (InvocationTargetException e) {
                // TODO: Auto-generated catch block
                LOG.error(e);
            }
        }
        return null;
    }

    /**
     * @param type the provider type
     * @return the provider
     * @param object the constructor parameter
     * @param listener the fig
     * that refreshes after the NotationProvider has changed
     */
    public NotationProvider getNotationProvider(int type,
            Object object, PropertyChangeListener listener) {
        NotationProvider p = getNotationProvider(type, object);
        p.addListener(listener, object);
        return p;
    }

    /**
     * This function looks for the requested notation provider type.
     * It is guaranteed to deliver:<ul>
     * <li>the requested type of the requested notation language,
     * <li>the requested type of the default notation, or
     * <li><code>null</code>.
     * </ul>
     *
     * @param type the provider type
     * @param name the context (i.e. the notation name)
     * @return the provider
     */
    private Class getNotationProviderClass(int type, NotationName name) {
        if (allLanguages.containsKey(name)) {
            Map t = (Map) allLanguages.get(name);
            if (t.containsKey(new Integer(type))) {
                return (Class) t.get(new Integer(type));
            }
        }
        Map t = (Map) allLanguages.get(defaultLanguage);
        if (t.containsKey(new Integer(type))) {
            return (Class) t.get(new Integer(type));
        }
        return null;
    }

    /**
     * @param type the provider type
     * @param notationName the name of the notation (language)
     * @param provider the provider
     */
    public void addNotationProvider(int type,
            NotationName notationName, Class provider) {
        if (allLanguages.containsKey(notationName)) {
            Map t = (Map) allLanguages.get(notationName);
            t.put(new Integer(type), provider);
        } else {
            Map t = new HashMap();
            t.put(new Integer(type), provider);
            allLanguages.put(notationName, t);
        }
    }

    /**
     * @param notationName the UML notation that is to be used as default
     *                     if no other is found
     */
    public void setDefaultNotation(NotationName notationName) {
        if (allLanguages.containsKey(notationName)) {
            defaultLanguage = notationName;
        }
    }

    /**
     * We need this to remove modules.
     *
     * @param notationName the notation to be removed
     * @return true if the notation was removed
     */
    public boolean removeNotation(NotationName notationName) {
        if (defaultLanguage == notationName) {
            return false;
        }
        if (allLanguages.containsKey(notationName)) {
            //TODO: Remove it here
            return true;
        }
        return false;
    }
}
