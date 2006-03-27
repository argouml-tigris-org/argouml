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

package org.argouml.uml.notation.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.uml.Profile;
import org.argouml.uml.ProfileException;
import org.argouml.util.MyTokenizer;

/**
 * This class is a utility for the UML notation. <p>
 *
 * TODO: No need to make this class public.
 *
 * @author mvw@tigris.org
 */
public final class NotationUtilityUml {

    /**
     * The vector of CustomSeparators to use when tokenizing parameters.
     */
    private static Vector parameterCustomSep;

    /**
     * The constructor.
     */
    private NotationUtilityUml() {}
    
    static {
        parameterCustomSep = new Vector();
        parameterCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
    }


    /**
     * This function shall replace the previous set of stereotypes
     * of the given modelelement with a new set,
     * given in the form of a "," seperated string of stereotype names.
     *
     * @param umlobject the UML element to adapt
     * @param stereotype comma seperated stereotype names
     * @param full false if stereotypes are only added,
     *             true if removal should be done, too.
     */
    public static void dealWithStereotypes(Object umlobject, String stereotype,
            boolean full) {
        String token;
        MyTokenizer mst;
        Collection stereotypes = new ArrayList();

        /* Convert the string (e.g. "aaa,bbb,ccc")
         * into seperate stereotype-names (e.g. "aaa", "bbb", "ccc").
         */
        if (stereotype != null) {
            mst = new MyTokenizer(stereotype, " ,\\,");
            while (mst.hasMoreTokens()) {
                token = mst.nextToken();
                if (!",".equals(token) && !" ".equals(token)) {
                    stereotypes.add(token);
                }
            }
        }

        if (full) {
            // collect the to be removed stereotypes
            Collection toBeRemoved = new ArrayList();
            Iterator i = Model.getFacade().getStereotypes(umlobject).iterator();
            while (i.hasNext()) {
                String stereotypename = Model.getFacade().getName(i.next());
                if (stereotypename != null
                        && !stereotypes.contains(stereotypename)) {
                    toBeRemoved.add(getStereotype(umlobject, stereotypename));
                }
            }

            // and now remove them
            i = toBeRemoved.iterator();
            while (i.hasNext()) {
                Model.getCoreHelper().removeStereotype(umlobject, i.next());
            }
        }

        // add stereotypes
        if (!stereotypes.isEmpty()) {
            Iterator i = stereotypes.iterator();
            while (i.hasNext()) {
                String stereotypename = (String) i.next();
                Object umlstereo = getStereotype(umlobject, stereotypename);
                if (umlstereo != null) {
                    Model.getCoreHelper().addStereotype(umlobject, umlstereo);
                }
            }
        }
    }

    /**
     * Finds a stereotype named name either in the subtree of the model rooted
     * at root, or in the the ProfileJava model.<p>
     *
     * TODO: Should create the stereotype under root if it isn't found.
     *
     * @param obj
     *            A ModelElement to find a suitable stereotype for.
     * @param name
     *            The name of the stereotype to search for.
     * @return A stereotype named name, or possibly null.
     */
    public static Object getStereotype(Object obj, String name) {
        Object root = Model.getFacade().getModel(obj);
        Object stereo;

        stereo = recFindStereotype(obj, root, name);
        if (stereo != null) {
            return stereo;
        }

        try {
            Project project = ProjectManager.getManager().getCurrentProject();
            Profile profile = project.getProfile();
            stereo = recFindStereotype(obj, profile.getProfileModel(), name);
        } catch (ProfileException e) {
            // TODO: How are we going to handle exceptions here?
            // I suspect the profile should be part of the project
            // and not a singleton.
        }

        if (stereo != null) {
            return Model.getModelManagementHelper().getCorrespondingElement(
                    stereo, root);
        }

        if (root != null && name.length() > 0) {
            stereo =
                Model.getExtensionMechanismsFactory().buildStereotype(
                    obj, name, root);
        }

        return stereo;
    }

    /**
     * Recursively search a hive of a model for a stereotype with the name given
     * in name.
     *
     * @param obj
     *            The model element to be suitable for.
     * @param root
     *            The model element to search from.
     * @param name
     *            The name of the stereotype to search for.
     * @return An stereotype named name, or null if none is found.
     */
    private static Object recFindStereotype(
            Object obj, Object root, String name) {
        Object stereo;

        if (root == null) {
            return null;
        }

        if (Model.getFacade().isAStereotype(root)
                && name.equals(Model.getFacade().getName(root))) {
            if (Model.getExtensionMechanismsHelper().isValidStereoType(obj,
            /* (MStereotype) */root)) {
                return root;
            }
        }

        if (!Model.getFacade().isANamespace(root)) {
            return null;
        }

        Collection ownedElements = Model.getFacade().getOwnedElements(root);

        if (ownedElements == null) {
            return null;
        }

        // Loop through each element in the namespace, recursing.

        Iterator iter = ownedElements.iterator();

        while (iter.hasNext()) {
            stereo = recFindStereotype(obj, iter.next(), name);
            if (stereo != null) {
                return stereo;
            }
        }
        return null;
    }

    /**
     * Returns a visibility String eihter for a MVisibilityKind (according to
     * the definition in NotationProvider2), but also for a model element.
     * @see org.argouml.notation.NotationProvider2#generateVisibility(java.lang.Object)
     */
    public static String generateVisibility(Object o) {
        if (o == null) {
            return "";
        }
        if (!Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY, true)) {
            return "";
        }
        if (Model.getFacade().isAModelElement(o)) {
            if (Model.getFacade().isPublic(o)) {
                return "+";
            }
            if (Model.getFacade().isPrivate(o)) {
                return "-";
            }
            if (Model.getFacade().isProtected(o)) {
                return "#";
            }
            if (Model.getFacade().isPackage(o)) {
                return "~";
            }
        }
        if (Model.getFacade().isAVisibilityKind(o)) {
            if (Model.getVisibilityKind().getPublic().equals(o)) {
                return "+";
            }
            if (Model.getVisibilityKind().getPrivate().equals(o)) {
                return "-";
            }
            if (Model.getVisibilityKind().getProtected().equals(o)) {
                return "#";
            }
            if (Model.getVisibilityKind().getPackage().equals(o)) {
                return "~";
            }
        }
        return "";
    }

    /**
     * Parses a parameter list and aligns the parameter list in op to that
     * specified in param. A parameter list generally has the following syntax:
     *
     * <pre>
     * param := [inout] [name] [: type] [= initial value]
     * list := [param] [, param]*
     * </pre>
     *
     * <code>inout</code> is optional and if omitted the old value preserved.
     * If no value has been assigned, then <code>in </code> is assumed.<p>
     *
     * <code>name</code>, <code>type</code> and <code>initial value</code>
     * are optional and if omitted the old value preserved.<p>
     *
     * <code>type</code> and <code>initial value</code> can be given
     * in any order.<p>
     *
     * Unspecified properties is carried over by position, so if a parameter is
     * inserted into the list, then it will inherit properties from the
     * parameter that was there before for unspecified properties.<p>
     *
     * This syntax is compatible with the UML 1.3 specification.
     *
     * @param op
     *            The operation the parameter list belongs to.
     * @param param
     *            The parameter list, without enclosing parentheses.
     * @param paramOffset
     *            The offset to the beginning of the parameter list. Used for
     *            error reports.
     * @throws java.text.ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     */
    public static void parseParamList(Object op, String param, int paramOffset)
        throws ParseException {
        MyTokenizer st =
            new MyTokenizer(param, " ,\t,:,=,\\,", parameterCustomSep);
        // Copy returned parameters because it will be a live collection for MDR
        Collection origParam = new ArrayList(Model.getFacade().getParameters(op));
        Object ns = Model.getFacade().getModel(op);
        if (Model.getFacade().isAOperation(op)) {
            Object ow = Model.getFacade().getOwner(op);

            if (ow != null && Model.getFacade().getNamespace(ow) != null) {
                ns = Model.getFacade().getNamespace(ow);
            }
        }

        Iterator it = origParam.iterator();
        while (st.hasMoreTokens()) {
            String kind = null;
            String name = null;
            String tok;
            String type = null;
            String value = null;
            Object p = null;
            boolean hasColon = false;
            boolean hasEq = false;

            while (it.hasNext() && p == null) {
                p = it.next();
                if (Model.getFacade().isReturn(p)) {
                    p = null;
                }
            }

            while (st.hasMoreTokens()) {
                tok = st.nextToken();

                if (",".equals(tok)) {
                    break;
                } else if (" ".equals(tok) || "\t".equals(tok)) {
                    if (hasEq) {
                        value += tok;
                    }
                } else if (":".equals(tok)) {
                    hasColon = true;
                    hasEq = false;
                } else if ("=".equals(tok)) {
                    if (value != null) {
                        throw new ParseException("Parameters cannot have two "
                                + "default values", paramOffset
                                + st.getTokenIndex());
                    }
                    hasEq = true;
                    hasColon = false;
                    value = "";
                } else if (hasColon) {
                    if (type != null) {
                        throw new ParseException("Parameters cannot have two "
                                + "types", paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '\'' || tok.charAt(0) == '\"') {
                        throw new ParseException("Parameter type cannot be "
                                + "quoted", paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '(') {
                        throw new ParseException("Parameter type cannot be an "
                                + "expression", paramOffset
                                + st.getTokenIndex());
                    }

                    type = tok;
                } else if (hasEq) {
                    value += tok;
                } else {
                    if (name != null && kind != null) {
                        throw new ParseException("Extra text in parameter",
                                paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '\'' || tok.charAt(0) == '\"') {
                        throw new ParseException(
                                "Parameter name/kind cannot be" + " quoted",
                                paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '(') {
                        throw new ParseException(
                                "Parameter name/kind cannot be"
                                        + " an expression", paramOffset
                                        + st.getTokenIndex());
                    }

                    kind = name;
                    name = tok;
                }
            }

            if (p == null) {
                Object model =
                    ProjectManager.getManager().getCurrentProject().getModel();
                Object voidType =
                    ProjectManager.getManager()
                        .getCurrentProject().findType("void");
                Collection propertyChangeListeners =
                    ProjectManager.getManager()
                        .getCurrentProject().findFigsForMember(op);
                p =
                    Model.getCoreFactory().buildParameter(
                            op,
                            model,
                            voidType,
                            propertyChangeListeners);
                // op.addParameter(p);
            }

            if (name != null) {
                Model.getCoreHelper().setName(p, name.trim());
            }

            if (kind != null) {
                setParamKind(p, kind.trim());
            }

            if (type != null) {
                Model.getCoreHelper().setType(p, getType(type.trim(), ns));
            }

            if (value != null) {
                Object initExpr =
                    Model.getDataTypesFactory()
                        .createExpression(
                                Notation.getConfigueredNotation().toString(),
                                value.trim());
                Model.getCoreHelper().setDefaultValue(p, initExpr);
            }
        }

        while (it.hasNext()) {
            Object p = it.next();
            if (!Model.getFacade().isReturn(p)) {
                Model.getCoreHelper().removeParameter(op, p);
            }
        }
    }

    /**
     * Set a parameters kind according to a string description of
     * that kind.
     * @param parameter the parameter
     * @param description the string description
     */
    private static void setParamKind(Object parameter, String description) {
        Object kind;
        if ("out".equalsIgnoreCase(description)) {
            kind = Model.getDirectionKind().getOutParameter();
        } else if ("inout".equalsIgnoreCase(description)) {
            kind = Model.getDirectionKind().getInOutParameter();
        } else {
            kind = Model.getDirectionKind().getInParameter();
        }
        Model.getCoreHelper().setKind(parameter, kind);
    }

    /**
     * Finds the classifier associated with the type named in name.
     * 
     * TODO: Make private.
     *
     * @param name
     *            The name of the type to get.
     * @param defaultSpace
     *            The default name-space to place the type in.
     * @return The classifier associated with the name.
     */
    public static Object getType(String name, Object defaultSpace) {
        Object type = null;
        Project p = ProjectManager.getManager().getCurrentProject();
        // Should we be getting this from the GUI? BT 11 aug 2002
        type = p.findType(name, false);
        if (type == null) { // no type defined yet
            type = Model.getCoreFactory().buildClass(name,
                    defaultSpace);
        }
        if (Model.getFacade().getModel(type) != p.getModel()
                && !Model.getModelManagementHelper().getAllNamespaces(
                       p.getModel()).contains(
                               Model.getFacade().getNamespace(type))) {
            type = Model.getModelManagementHelper().getCorrespondingElement(
                    type, Model.getFacade().getModel(defaultSpace));
        }
        return type;
    }


}
