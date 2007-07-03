// $Id: NotationUtilityUml.java 12731 2007-05-30 19:06:18Z tfmorris $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.StereotypeUtility;
import org.argouml.util.MyTokenizer;

/**
 * This class is a utility for the UML notation.
 *
 * @author mvw@tigris.org
 */
public final class NotationUtilityUml {
    /**
     * The array of special properties for attributes.
     */
    static PropertySpecialString[] attributeSpecialStrings;

    /**
     * The vector of CustomSeparators to use when tokenizing attributes.
     */
    static Vector attributeCustomSep;

    /**
     * The array of special properties for operations.
     */
    static PropertySpecialString[] operationSpecialStrings;

    /**
     * The vector of CustomSeparators to use when tokenizing attributes.
     */
    static Vector operationCustomSep;

    /**
     * The vector of CustomSeparators to use when tokenizing parameters.
     */
    private static Vector parameterCustomSep;

    /**
     * The character with a meaning as a visibility at the start
     * of an attribute.
     */
    static final String VISIBILITYCHARS = "+#-~";

    /**
     * The constructor.
     */
    public NotationUtilityUml() { }

    /* TODO: Can we put the static block within the init()? */
    static {
        attributeSpecialStrings = new PropertySpecialString[2];

        attributeCustomSep = new Vector();
        attributeCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        attributeCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        attributeCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

        operationSpecialStrings = new PropertySpecialString[8];

        operationCustomSep = new Vector();
        operationCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        operationCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        operationCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

        parameterCustomSep = new Vector();
        parameterCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
    }

    public void init() {
	int assPos = 0;
        attributeSpecialStrings[assPos++] =
            new PropertySpecialString("frozen",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        if (Model.getFacade().isAStructuralFeature(element)) {
                            if ("false".equalsIgnoreCase(value)) {
                                Model.getCoreHelper().setReadOnly(element, true);
                            } else {
                                Model.getCoreHelper().setReadOnly(element, false);
                            }
                        }
                    }
                });
        
        // TODO: AddOnly has been removed in UML 2.x, so we should phase out
        // support of it - tfm - 20070529
        attributeSpecialStrings[assPos++] =
            new PropertySpecialString("addonly",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        if (Model.getFacade().isAStructuralFeature(element)) {
                            if ("false".equalsIgnoreCase(value)) {
                                Model.getCoreHelper().setReadOnly(element, true);
                            } else {
                                Model.getCoreHelper().setChangeability(element,
                                    Model.getChangeableKind().getAddOnly());
                            }
                        }
                    }
                });

        assert assPos == attributeSpecialStrings.length;

        operationSpecialStrings = new PropertySpecialString[8];
        int ossPos = 0;
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("sequential",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setConcurrency(element,
                                Model.getConcurrencyKind().getSequential());
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("guarded",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        Object kind = Model.getConcurrencyKind().getGuarded();
                        if (value != null && value.equalsIgnoreCase("false")) {
                            kind = Model.getConcurrencyKind().getSequential();
                        }
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setConcurrency(element, kind);
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("concurrent",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        Object kind =
                            Model.getConcurrencyKind().getConcurrent();
                        if (value != null && value.equalsIgnoreCase("false")) {
                            kind = Model.getConcurrencyKind().getSequential();
                        }
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setConcurrency(element, kind);
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("concurrency",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        Object kind =
                            Model.getConcurrencyKind().getSequential();
                        if ("guarded".equalsIgnoreCase(value)) {
                            kind = Model.getConcurrencyKind().getGuarded();
                        } else if ("concurrent".equalsIgnoreCase(value)) {
                            kind = Model.getConcurrencyKind().getConcurrent();
                        }
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setConcurrency(element, kind);
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("abstract",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        boolean isAbstract = true;
                        if (value != null && value.equalsIgnoreCase("false")) {
                            isAbstract = false;
                        }
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setAbstract(
                                    element,
                                    isAbstract);
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("leaf",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        boolean isLeaf = true;
                        if (value != null && value.equalsIgnoreCase("false")) {
                            isLeaf = false;
                        }
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setLeaf(element, isLeaf);
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("query",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        boolean isQuery = true;
                        if (value != null && value.equalsIgnoreCase("false")) {
                            isQuery = false;
                        }
                        if (Model.getFacade().isABehavioralFeature(element)) {
                            Model.getCoreHelper().setQuery(element, isQuery);
                        }
                    }
                });
        operationSpecialStrings[ossPos++] =
            new PropertySpecialString("root",
                new PropertyOperation() {
                    public void found(Object element, String value) {
                        boolean isRoot = true;
                        if (value != null && value.equalsIgnoreCase("false")) {
                            isRoot = false;
                        }
                        if (Model.getFacade().isAOperation(element)) {
                            Model.getCoreHelper().setRoot(element, isRoot);
                        }
                    }
                });

        assert ossPos == operationSpecialStrings.length;
    }

    /**
     * Parse a string on the format:
     * <pre>
     *     [ &lt;&lt; stereotype &gt;&gt;] [+|-|#|~] [name]
     * </pre>
     * 
     * @param me   The ModelElement <em>text</em> describes.
     * @param text A String on the above format.
     * @throws ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     */
    protected static void parseModelElement(Object me, String text)
        throws ParseException {
        MyTokenizer st;

        Vector path = null;
        String name = null;
        String stereotype = null;
        String token;

        try {
            st = new MyTokenizer(text, "<<,\u00AB,\u00BB,>>,::");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if ("<<".equals(token) || "\u00AB".equals(token)) {
                    if (stereotype != null) {
                        String msg = 
                            "parsing.error.model-element-name.twin-stereotypes";
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
                } else if ("::".equals(token)) {
                    if (name != null) {
                        name = name.trim();
                    }

                    if (path != null && (name == null || "".equals(name))) {
                        String msg = 
                            "parsing.error.model-element-name.anon-qualifiers";
                        throw new ParseException(Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    if (path == null) {
                        path = new Vector();
                    }
                    if (name != null) {
                        path.add(name);
                    }
                    name = null;
                } else {
                    if (name != null) {
                        String msg = 
                            "parsing.error.model-element-name.twin-names";
                        throw new ParseException(Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            String msg = 
                "parsing.error.model-element-name.unexpected-name-element";
            throw new ParseException(Translator.localize(msg),
                    text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (name != null) {
            name = name.trim();
        }

        if (path != null && (name == null || "".equals(name))) {
            String msg = "parsing.error.model-element-name.must-end-with-name";
            throw new ParseException(Translator.localize(msg), 0);
        }

        if (name != null && name.startsWith("+")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getPublic());
        }
        if (name != null && name.startsWith("-")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getPrivate());
        }
        if (name != null && name.startsWith("#")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getProtected());
        }
        if (name != null && name.startsWith("~")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getPackage());
        }
        if (name != null) {
            Model.getCoreHelper().setName(me, name);
        }

        NotationUtilityUml.dealWithStereotypes(me, stereotype, false);

        if (path != null) {
            Object nspe =
                Model.getModelManagementHelper().getElement(
                        path,
                        Model.getFacade().getModel(me));

            if (nspe == null || !(Model.getFacade().isANamespace(nspe))) {
                String msg = 
                        "parsing.error.model-element-name.namespace-unresolved";
                throw new ParseException(Translator.localize(msg), 
                        0);
            }
            Object model =
                ProjectManager.getManager().getCurrentProject().getRoot();
            if (!Model.getCoreHelper().getAllPossibleNamespaces(me, model)
                        .contains(nspe)) {
                String msg = 
                        "parsing.error.model-element-name.namespace-invalid";
                throw new ParseException(Translator.localize(msg), 
                        0);
            }

            Model.getCoreHelper().addOwnedElement(nspe, me);
        }
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
     * at root, or in the the ProfileJava model.
     *
     * @param obj
     *            A ModelElement to find a suitable stereotype for.
     * @param name
     *            The name of the stereotype to search for.
     * @return A stereotype named name, or possibly null.
     */
    private static Object getStereotype(Object obj, String name) {
        Object root = Model.getFacade().getModel(obj);
        Object stereo = null;

        Set stereoSet = StereotypeUtility.getAvailableStereotypes(obj);
        Iterator it = stereoSet.iterator();
        
        while(it.hasNext()) {
            stereo = it.next();
            if (Model.getFacade().getName(stereo).equals(name)) {
        	break;
            } else {
        	stereo = null;
            }
        }
        
        if (stereo != null) {
            return stereo;
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
     *
     * @param o a modelelement or a visibilitykind
     * @return a string, guaranteed not null
     */
    public static String generateVisibility(Object o) {
        if (o == null) {
            return "";
        }
        Project p = ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = p.getProjectSettings();
        if (!ps.getShowVisibilityValue()) {
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
     * @param modelElement the UML element to generate for
     * @return a string which represents the path
     */
    protected static String generatePath(Object modelElement) {
        String s = "";
        Object p = modelElement;
        Stack stack = new Stack();
        Object ns = Model.getFacade().getNamespace(p);
        while (ns != null && !Model.getFacade().isAModel(ns)) {
            stack.push(Model.getFacade().getName(ns));
            ns = Model.getFacade().getNamespace(ns);
        }
        while (!stack.isEmpty()) {
            s += (String) stack.pop() + "::";
        }

        if (s.length() > 0 && !s.endsWith(":")) {
            s += "::";
        }
        return s;
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
    static void parseParamList(Object op, String param, int paramOffset)
        throws ParseException {
        MyTokenizer st =
            new MyTokenizer(param, " ,\t,:,=,\\,", parameterCustomSep);
        // Copy returned parameters because it will be a live collection for MDR
        Collection origParam =
            new ArrayList(Model.getFacade().getParameters(op));
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
                    	String msg =
                            "parsing.error.notation-utility.two-default-values";
                        throw new ParseException(Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }
                    hasEq = true;
                    hasColon = false;
                    value = "";
                } else if (hasColon) {
                    if (type != null) {
                        String msg = "parsing.error.notation-utility.two-types";
                        throw new ParseException(Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '\'' || tok.charAt(0) == '\"') {
                        String msg =
                            "parsing.error.notation-utility.type-quoted";
                        throw new ParseException(Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '(') {
                        String msg =
                            "parsing.error.notation-utility.type-expr";
                        throw new ParseException(Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }

                    type = tok;
                } else if (hasEq) {
                    value += tok;
                } else {
                    if (name != null && kind != null) {
                        String msg =
                            "parsing.error.notation-utility.extra-text";
                        throw new ParseException(Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '\'' || tok.charAt(0) == '\"') {
                        String msg =
                            "parsing.error.notation-utility.name-kind-quoted";
                        throw new ParseException(
                                Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }

                    if (tok.charAt(0) == '(') {
                        String msg =
                            "parsing.error.notation-utility.name-kind-expr";
                        throw new ParseException(
                                Translator.localize(msg),
                                paramOffset + st.getTokenIndex());
                    }

                    kind = name;
                    name = tok;
                }
            }

            if (p == null) {
                Object returnType =
                    ProjectManager.getManager()
                        .getCurrentProject().findType("void");
                p = Model.getCoreFactory().buildParameter(
                            op,
                            returnType);
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
                Project project =
                    ProjectManager.getManager().getCurrentProject();
                ProjectSettings ps = project.getProjectSettings();

                Object initExpr =
                    Model.getDataTypesFactory()
                        .createExpression(
                                // TODO: Find a better default language
                                ps.getNotationLanguage(),
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
     * @param name
     *            The name of the type to get.
     * @param defaultSpace
     *            The default name-space to place the type in.
     * @return The classifier associated with the name.
     */
    static Object getType(String name, Object defaultSpace) {
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

    /**
     * Applies a Vector of name value pairs of properties to a model element.
     * The name is treated as the tag of a tagged value unless it is one of the
     * PropertySpecialStrings, in which case the action of the
     * PropertySpecialString is invoked.
     *
     * @param elem
     *            An model element to apply the properties to.
     * @param prop
     *            A Vector with name, value pairs of properties.
     * @param spec
     *            An array of PropertySpecialStrings to use.
     */
    static void setProperties(Object elem, Vector prop,
            PropertySpecialString[] spec) {
        String name;
        String value;
        int i, j;

    nextProp:
        for (i = 0; i + 1 < prop.size(); i += 2) {
            name = (String) prop.get(i);
            value = (String) prop.get(i + 1);

            if (name == null) {
                continue;
            }

            name = name.trim();
            if (value != null) {
                value = value.trim();
            }

            for (j = i + 2; j < prop.size(); j += 2) {
                String s = (String) prop.get(j);
                if (s != null && name.equalsIgnoreCase(s.trim())) {
                    continue nextProp;
                }
            }

            if (spec != null) {
                for (j = 0; j < spec.length; j++) {
                    if (spec[j].invoke(elem, name, value)) {
                        continue nextProp;
                    }
                }
            }

            Model.getCoreHelper().setTaggedValue(elem, name, value);
        }
    }


    /**
     * Interface specifying the operation to take when a
     * PropertySpecialString is matched.
     *
     * @author Michael Stockman
     * @since 0.11.2
     * @see PropertySpecialString
     */
    interface PropertyOperation {
        /**
         * Invoked by PropertySpecialString when it has matched a property name.
         *
         * @param element
         *            The element on which the property was set.
         * @param value
         *            The value of the property,
         *            may be null if no value was given.
         */
        void found(Object element, String value);
    }

    /**
     * Declares a string that should take special action when it is found
     * as a property in
     * {@link ParserDisplay#setProperties ParserDisplay.setProperties}.<p>
     *
     * <em>Example:</em>
     *
     * <pre>
     * attributeSpecialStrings[0] =
     *     new PropertySpecialString(&quot;frozen&quot;,
     *         new PropertyOperation() {
     *             public void found(Object element, String value) {
     *                 if (Model.getFacade().isAStructuralFeature(element))
     *                     Model.getFacade().setChangeable(element,
     *                          (value != null &amp;&amp; value
     *                             .equalsIgnoreCase(&quot;false&quot;)));
     *             }
     *         });
     * </pre>
     *
     * Taken from the (former) ParserDisplay constructor.
     * It creates a PropertySpecialString that is invoken when the String
     * "frozen" is found as a property name. Then
     * the found mehod in the anonymous inner class
     * defined on the 2nd line is invoked and performs
     * a custom action on the element on which the property was
     * specified by the user. In this case it does a setChangeability
     * on an attribute instead of setting a tagged value,
     * which would not have the desired effect.
     *
     * @author Michael Stockman
     * @since 0.11.2
     * @see PropertyOperation
     * @see ParserDisplay#setProperties
     */
    static class PropertySpecialString {
        private String name;

        private PropertyOperation op;

        /**
         * Constructs a new PropertySpecialString that will invoke the
         * action in op when {@link #invoke(Object, String, String)} is
         * called with name equal to str and then return true from invoke.
         *
         * @param str
         *            The name of this PropertySpecialString.
         * @param propop
         *            An object containing the method to invoke on a match.
         */
        public PropertySpecialString(String str, PropertyOperation propop) {
            name = str;
            op = propop;
        }

        /**
         * Called by {@link ParserDisplay#setProperties(Object, Vector,
         * PropertySpecialString[])} while searching for an action to
         * invoke for a property. If it returns true, then setProperties
         * may assume that all required actions have been taken and stop
         * searching.
         *
         * @param pname
         *            The name of a property.
         * @param value
         *            The value of a property.
         * @return <code>true</code> if an action is performed, otherwise
         *         <code>false</code>.
         */
        public boolean invoke(Object element, String pname, String value) {
            if (!name.equalsIgnoreCase(pname)) {
                return false;
            }
            op.found(element, value);
            return true;
        }
    }

    /**
     * Checks for ';' in Strings or chars in ';' separated tokens in order to
     * return an index to the next attribute or operation substring, -1
     * otherwise (a ';' inside a String or char delimiters is ignored).
     *
     * @param s The string to search.
     * @param start The position to start at.
     * @return the index to the next attribute
     */
    static int indexOfNextCheckedSemicolon(String s, int start) {
        if (s == null || start < 0 || start >= s.length()) {
            return -1;
        }
        int end;
        boolean inside = false;
        boolean backslashed = false;
        char c;
        for (end = start; end < s.length(); end++) {
            c = s.charAt(end);
            if (!inside && c == ';') {
                return end;
            } else if (!backslashed && (c == '\'' || c == '\"')) {
                inside = !inside;
            }
            backslashed = (!backslashed && c == '\\');
        }
        return end;
    }

    /**
     * Finds a visibility for the visibility specified by name. If no known
     * visibility can be deduced, private visibility is used.
     *
     * @param name
     *            The Java name of the visibility.
     * @return A visibility corresponding to name.
     */
    static Object getVisibility(String name) {
        if ("+".equals(name) || "public".equals(name)) {
            return Model.getVisibilityKind().getPublic();
        } else if ("#".equals(name) || "protected".equals(name)) {
            return Model.getVisibilityKind().getProtected();
        } else if ("~".equals(name) || "package".equals(name)) {
            return Model.getVisibilityKind().getPackage();
        } else {
            /* if ("-".equals(name) || "private".equals(name)) */
            return Model.getVisibilityKind().getPrivate();
        }
    }

    /**
     * @param st a stereotype UML object
     *                 or a string
     *                 or a collection of stereotypes
     *                 or a modelelement of which the stereotypes are retrieved
     * @return a string representing the given stereotype(s)
     */
    public static String generateStereotype(Object st) {
        if (st == null) {
            return "";
        }
        Project project =
            ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = project.getProjectSettings();

        if (st instanceof String) {
            return formatSingleStereotype((String) st, ps);
        }
        if (Model.getFacade().isAStereotype(st)) {
            return formatSingleStereotype(Model.getFacade().getName(st), ps);
        }

        if (Model.getFacade().isAModelElement(st)) {
            st = Model.getFacade().getStereotypes(st);
        }
        if (st instanceof Collection) {
            Object o;
            StringBuffer sb = new StringBuffer(10);
            boolean first = true;
            Iterator iter = ((Collection) st).iterator();
            while (iter.hasNext()) {
                if (!first) {
                    sb.append(',');
                }
                o = iter.next();
                if (o != null) {
                    sb.append(Model.getFacade().getName(o));
                    first = false;
                }
            }
            if (!first) {
                return ps.getLeftGuillemot()
                    + sb.toString()
                    + ps.getRightGuillemot();
            }
        }
        return "";
    }

    private static String formatSingleStereotype(String name, 
            ProjectSettings ps) {
        if (name == null || name.length() == 0) {
            return "";
        }
        return ps.getLeftGuillemot() + name + ps.getRightGuillemot();
    }

    /**
     * Generates the representation of a parameter on the display (diagram). The
     * string to be returned will have the following syntax:
     * <p>
     * 
     * kind name : type-expression = default-value
     * 
     * @see org.argouml.notation.NotationProvider2#generateParameter(java.lang.Object)
     */
    static String generateParameter(Object parameter) {
        StringBuffer s = new StringBuffer();
        s.append(generateKind(Model.getFacade().getKind(parameter)));
        if (s.length() > 0) {
            s.append(" ");
        }
        s.append(Model.getFacade().getName(parameter));
        String classRef =
            generateClassifierRef(Model.getFacade().getType(parameter));
        if (classRef.length() > 0) {
            s.append(" : ");
            s.append(classRef);
        }
        String defaultValue =
            generateExpression(Model.getFacade().getDefaultValue(parameter));
        if (defaultValue.length() > 0) {
            s.append(" = ");
            s.append(defaultValue);
        }
        return s.toString();
    }

    private static String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr)) {
            return generateUninterpreted(
                    (String) Model.getFacade().getBody(expr));
        } else if (Model.getFacade().isAConstraint(expr)) {
            return generateExpression(Model.getFacade().getBody(expr));
        }
        return "";
    }

    private static String generateUninterpreted(String un) {
        if (un == null) {
            return "";
        }
        return un;
    }

    private static String generateClassifierRef(Object cls) {
        if (cls == null) {
            return "";
        }
        return Model.getFacade().getName(cls);
    }

    private static String generateKind(Object /*Parameter etc.*/ kind) {
        StringBuffer s = new StringBuffer();
        if (kind == null /* "in" is the default */
                || kind == Model.getDirectionKind().getInParameter()) {
            s.append(/*"in"*/ ""); /* See issue 3421. */
        } else if (kind == Model.getDirectionKind().getInOutParameter()) {
            s.append("inout");
        } else if (kind == Model.getDirectionKind().getReturnParameter()) {
            // return nothing
        } else if (kind == Model.getDirectionKind().getOutParameter()) {
            s.append("out");
        }
        return s.toString();
    }

    /**
     * @param tv a tagged value
     * @return a string that represents the tagged value
     */
    static String generateTaggedValue(Object tv) {
        if (tv == null) {
            return "";
        }
        return Model.getFacade().getTagOfTag(tv)
            + "="
            + generateUninterpreted(Model.getFacade().getValueOfTag(tv));
    }

    /**
     * Generate the text of a multiplicity.
     *
     * @param m the given multiplicity
     * @return a string (guaranteed not null)
     */
    public static String generateMultiplicity(Object m) {
        Project p = ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = p.getProjectSettings();
        String s = Model.getFacade().toString(m);
        if (m == null
                || (!ps.getShowSingularMultiplicitiesValue() 
                        && "1".equals(s))) {
            return "";
        }
        return s;
    }
    
    /**
     * @param m the action
     * @return the generated text
     */
    static String generateAction(Object m) {
        Collection c;
        Iterator it;
        String s;
        String p;
        boolean first;
        if (m == null) {
            return "";
        }

        Object script = Model.getFacade().getScript(m);

        if ((script != null) && (Model.getFacade().getBody(script) != null)) {
            s = Model.getFacade().getBody(script).toString();
        } else {
            s = "";
        }

        p = "";
        c = Model.getFacade().getActualArguments(m);
        if (c != null) {
            it = c.iterator();
            first = true;
            while (it.hasNext()) {
                Object arg = it.next();
                if (!first) {
                    p += ", ";
                }

                if (Model.getFacade().getValue(arg) != null) {
                    p += generateExpression(Model.getFacade().getValue(arg));
                }
                first = false;
            }
        }
        if (s.length() == 0 && p.length() == 0) {
            return "";
        }

        /* If there are no arguments, then do not show the ().
         * This solves issue 1758.
         * Arguments are not supported anyhow in the UI yet.
         * These brackets are easily confused with the brackets
         * for the Operation of a CallAction.
         */
        if (p.length() == 0) {
            return s;
        }

        return s + " (" + p + ")";
    }

    static String generateActionSequence(Object a) {
        if (Model.getFacade().isAActionSequence(a)) {
            StringBuffer str = new StringBuffer("");
            Collection actions = Model.getFacade().getActions(a);
            Iterator i = actions.iterator();
            if (i.hasNext()) {
                str.append(generateAction(i.next()));
            }
            while (i.hasNext()) {
                str.append("; ");
                str.append(generateAction(i.next()));
            }
            return str.toString();
        } else {
            return generateAction(a);
        }
    }
}
