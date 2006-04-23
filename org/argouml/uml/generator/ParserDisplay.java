// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.notation.uml.NotationUtilityUml;
import org.argouml.util.MyTokenizer;


/**
 * Interface specifying the operation to take when a PropertySpecialString is
 * matched.
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
     *            The value of the property, may be null if no value was given.
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
 * attributeSpecialStrings[0] = new PropertySpecialString(&quot;frozen&quot;,
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
 * Taken from the ParserDisplay constructor. It creates a PropertySpecialString
 * that is invoken when the String "frozen" is found as a property name. Then
 * the found mehod in the anonymous inner class defined on the 2nd line is
 * invoked and performs a custom action on the element on which the property was
 * specified by the user. In this case it does a setChangeability on an
 * attribute instead of setting a tagged value, which would not have the desired
 * effect.
 *
 * @author Michael Stockman
 * @since 0.11.2
 * @see PropertyOperation
 * @see ParserDisplay#setProperties
 */
class PropertySpecialString {
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
 * This class is responsible for the parsing of the text that the user entered
 * on the display, i.e. the diagram. Consequently, the UML elements represented
 * by the text are created or adapted.<p>
 *
 * There is a certain relation in namegiving with the class GeneratorDisplay,
 * which generates a textual representation of UML elements for displaying on
 * diagrams.
 *
 * @stereotype singleton
 * @deprecated since V0.19.8 by mvw. Replaced by a new notation architecture:
 * see http://argouml.tigris.org/proposals/notation/index.html.

 */
public final class ParserDisplay {
    /**
     * The one and only ParserDisplay.
     */
    public static final ParserDisplay SINGLETON = new ParserDisplay();

    /**
     * The standard error etc. logger
     */
    private static final Logger LOG = Logger.getLogger(ParserDisplay.class);

    /**
     * The array of special properties for attributes.
     */
    private PropertySpecialString[] attributeSpecialStrings;

    /**
     * The vector of CustomSeparators to use when tokenizing attributes.
     */
    private Vector attributeCustomSep;

    /**
     * The array of special properties for operations.
     */
    private PropertySpecialString[] operationSpecialStrings;

    /**
     * The vector of CustomSeparators to use when tokenizing attributes.
     */
    private Vector operationCustomSep;

    /**
     * The vector of CustomSeparators to use when tokenizing parameters.
     */
    private Vector parameterCustomSep;

    /**
     * The character with a meaning as a visibility at the start
     * of an attribute.
     */
    private static final String VISIBILITYCHARS = "+#-~";

    /**
     * Constructs the object contained in SINGLETON and initializes some
     * instance variables.
     *
     * @see #SINGLETON
     */
    private ParserDisplay() {
        attributeSpecialStrings = new PropertySpecialString[2];
        attributeSpecialStrings[0] =
            new PropertySpecialString("frozen",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    if (Model.getFacade().isAStructuralFeature(element)) {
                        if ("false".equalsIgnoreCase(value)) {
                            Model.getCoreHelper().setChangeability(element,
                                    Model.getChangeableKind().getChangeable());
                        } else {
                            Model.getCoreHelper().setChangeability(element,
                                    Model.getChangeableKind().getFrozen());
                        }
                    }
                }
            });
        attributeSpecialStrings[1] =
            new PropertySpecialString("addonly",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    if (Model.getFacade().isAStructuralFeature(element)) {
                        if ("false".equalsIgnoreCase(value)) {
                            Model.getCoreHelper().setChangeability(element,
                                    Model.getChangeableKind().getChangeable());
                        } else {
                            Model.getCoreHelper().setChangeability(element,
                                    Model.getChangeableKind().getAddOnly());
                        }
                    }
                }
            });
        attributeCustomSep = new Vector();
        attributeCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        attributeCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        attributeCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

        operationSpecialStrings = new PropertySpecialString[8];
        operationSpecialStrings[0] =
            new PropertySpecialString("sequential",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    if (Model.getFacade().isAOperation(element)) {
                        Model.getCoreHelper().setConcurrency(element,
                                Model.getConcurrencyKind().getSequential());
                    }
                }
            });
        operationSpecialStrings[1] =
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
        operationSpecialStrings[2] =
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
        operationSpecialStrings[3] =
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
        operationSpecialStrings[4] =
            new PropertySpecialString("abstract",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    boolean isAbstract = true;
                    if (value != null && value.equalsIgnoreCase("false")) {
                        isAbstract = false;
                    }
                    if (Model.getFacade().isAOperation(element)) {
                        Model.getCoreHelper().setAbstract(element, isAbstract);
                    }
                }
            });
        operationSpecialStrings[5] =
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
        operationSpecialStrings[6] =
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
        operationSpecialStrings[7] =
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
        operationCustomSep = new Vector();
        operationCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        operationCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        operationCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

        parameterCustomSep = new Vector();
        parameterCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
    }

    ////////////////////////////////////////////////////////////////
    // parsing methods

    /**
     * Parse an extension point.<p>
     *
     * The syntax is "name: location", "name:", "location" or "". The fields of
     * the extension point are updated appropriately.
     *
     * @param useCase The use case that owns this extension point
     * @param ep      The extension point concerned
     * @param text    The text to parse
     */
    public void parseExtensionPointFig(Object useCase, Object ep, String text) {

        // We can do nothing if we don't have both the use case and extension
        // point.
        if ((useCase == null) || (ep == null)) {
            return;
        }

        // Parse the string to creat a new extension point.
        Object newEp = parseExtensionPoint(text);

        // If we got back null we interpret this as meaning delete the
        // reference to the extension point from the use case, otherwise we set
        // the fields of the extension point to the values in newEp.
        if (newEp == null) {
            ProjectManager.getManager().getCurrentProject().moveToTrash(ep);
            TargetManager.getInstance().setTarget(useCase);
        } else {
            Model.getCoreHelper().setName(ep, Model.getFacade().getName(newEp));
            Model.getUseCasesHelper().setLocation(ep,
                    Model.getFacade().getLocation(newEp));
        }
    }

    /**
     * Checks for ';' in Strings or chars in ';' separated tokens in order to
     * return an index to the next attribute or operation substring, -1
     * otherwise (a ';' inside a String or char delimiters is ignored).
     */
    private int indexOfNextCheckedSemicolon(String s, int start) {
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
     * Parse a string representing one ore more ';' separated operations. The
     * case that a String or char contains a ';' (e.g. in an initializer) is
     * handled, but not other occurences of ';'.
     *
     * @param classifier  Classifier The classifier the operation(s) belong to
     * @param operation   Operation The operation on which the editing happened
     * @param text The string to parse
     * @throws ParseException for invalid input
     */
    public void parseOperationFig(
            Object classifier,
            Object operation,
            String text) throws ParseException {

        if (classifier == null || operation == null) {
            return;
        }
        ParseException pex = null;
        int start = 0;
        int end = indexOfNextCheckedSemicolon(text, start);
        Project currentProject =
            ProjectManager.getManager().getCurrentProject();
        if (end == -1) {
            //no text? remove op!
            currentProject.moveToTrash(operation);
            TargetManager.getInstance().setTarget(classifier);
            return;
        }
        String s = text.substring(start, end).trim();
        if (s.length() == 0) {
            //no non-whitechars in text? remove op!
            currentProject.moveToTrash(operation);
            TargetManager.getInstance().setTarget(classifier);
            return;
        }
        parseOperation(s, operation);
        int i = new ArrayList(
                Model.getFacade().getFeatures(classifier)).indexOf(operation);
        // check for more operations (';' separated):
        start = end + 1;
        end = indexOfNextCheckedSemicolon(text, start);
        while (end > start && end <= text.length()) {
            s = text.substring(start, end).trim();
            if (s.length() > 0) {
                // yes, there are more:
                Object model = currentProject.getModel();
                Object voidType = currentProject.findType("void");
                Object newOp =
                    Model.getCoreFactory()
                    	.buildOperation(classifier, model, voidType);
                if (newOp != null) {
                    try {
                        parseOperation(s, newOp);
                        //newOp.setOwnerScope(op.getOwnerScope()); //
                        //not needed in case of operation
                        if (i != -1) {
                            Model.getCoreHelper().addFeature(classifier, ++i, newOp);
                        } else {
                            Model.getCoreHelper().addFeature(classifier, newOp);
                        }
                    } catch (ParseException ex) {
                        if (pex == null) {
                            pex = ex;
                        }
                    }
                }
            }
            start = end + 1;
            end = indexOfNextCheckedSemicolon(text, start);
        }
        if (pex != null) {
            throw pex;
        }
    }

    /**
     * Parse a string representing one ore more ';' separated attributes. The
     * case that a String or char contains a ';' (e.g. in an initializer) is
     * handled, but not other occurences of ';'.
     *
     * @param classifier  Classifier The classifier the attribute(s) belong to
     * @param attribute   Attribute The attribute on which the editing happened
     * @param text The string to parse
     * @throws ParseException for invalid input
     */
    public void parseAttributeFig(
            Object classifier,
            Object attribute,
            String text) throws ParseException {

        if (classifier == null || attribute == null) {
            return;
        }

        Project project = ProjectManager.getManager().getCurrentProject();

        ParseException pex = null;
        int start = 0;
        int end = indexOfNextCheckedSemicolon(text, start);
        if (end == -1) {
            //no text? remove attr!
            project.moveToTrash(attribute);
            TargetManager.getInstance().setTarget(classifier);
            return;
        }
        String s = text.substring(start, end).trim();
        if (s.length() == 0) {
            //no non-whitechars in text? remove attr!
            project.moveToTrash(attribute);
            TargetManager.getInstance().setTarget(classifier);
            return;
        }
        parseAttribute(s, attribute);
        int i = new ArrayList(
                Model.getFacade().getFeatures(classifier)).indexOf(attribute);
        // check for more attributes (';' separated):
        start = end + 1;
        end = indexOfNextCheckedSemicolon(text, start);
        while (end > start && end <= text.length()) {
            s = text.substring(start, end).trim();
            if (s.length() > 0) {
                // yes, there are more:
                Object model = project.getModel();
                Object intType = project.findType("int");
                Object newAttribute =
                    Model.getCoreFactory().buildAttribute(model, intType);
                if (newAttribute != null) {
                    try {
                        parseAttribute(s, newAttribute);
                        Model.getCoreHelper().setOwnerScope(
                                newAttribute,
                                Model.getFacade().getOwnerScope(attribute));
                        if (i != -1) {
                            Model.getCoreHelper().addFeature(
                                    classifier, ++i, newAttribute);
                        } else {
                            Model.getCoreHelper().addFeature(
                                    classifier, newAttribute);
                        }
                    } catch (ParseException ex) {
                        if (pex == null) {
                            pex = ex;
                        }
                    }
                }
            }
            start = end + 1;
            end = indexOfNextCheckedSemicolon(text, start);
        }
        if (pex != null) {
            throw pex;
        }
    }

    /**
     * Parse a string representing an extension point and return a new extension
     * point.<p>
     *
     * The syntax is "name: location", "name:", "location" or "".
     * <em>Note:</em> If either field is blank, it will be set to null
     * in the extension point.
     *
     * We break up the string into tokens at the ":". We must keep the ":" as a
     * token, so we can distinguish between "name:" and "location". The number
     * of tokens will distinguish our four cases.<p>
     *
     * TODO: This method needs to be replaced, since it by design cannot cope
     * with the current design of the model component.
     *
     * @param text The string to parse
     *
     * @return A new extension point, with fields set appropriately, or
     *         <code>null</code> if we are given <code>null</code> or a
     *         blank string. <em>Note</em>. The string ":" can be used to set
     *         both name and location to null.
     */
    private Object parseExtensionPoint(String text) {

        // If we are given the null string, return immediately

        if (text == null) {
            return null;
        }

        // Build a new extension point

        // This method has insufficient information to call buildExtensionPoint.
        // Thus we'll need to create one, and pray that whomever called us knows
        // what kind of mess they got.
        Object ep =
            Model.getUseCasesFactory().createExtensionPoint();

        StringTokenizer st = new StringTokenizer(text.trim(), ":", true);
        int numTokens = st.countTokens();

        String epLocation;
        String epName;

        switch (numTokens) {

        case 0:

            // The empty string. Return null

            ep = null;

            break;

        case 1:

            // A string of the form "location". This will be confused by the
            // string ":", so we pick this out as an instruction to clear both
            // name and location.

            epLocation = st.nextToken().trim();

            if (epLocation.equals(":")) {
                Model.getCoreHelper().setName(ep, null);
                Model.getUseCasesHelper().setLocation(ep, null);
            } else {
                Model.getCoreHelper().setName(ep, null);
                Model.getUseCasesHelper().setLocation(ep, epLocation);
            }

            break;

        case 2:

            // A string of the form "name:"

            epName = st.nextToken().trim();

            Model.getCoreHelper().setName(ep, epName);
            Model.getUseCasesHelper().setLocation(ep, null);

            break;

        case 3:

            // A string of the form "name:location". Discard the middle token
            // (":")

            epName = st.nextToken().trim();
            st.nextToken(); // Read past the colon.
            epLocation = st.nextToken().trim();

            Model.getCoreHelper().setName(ep, epName);
            Model.getUseCasesHelper().setLocation(ep, epLocation);

            break;
        }

        return ep;
    }

    /**
     * Parse a line of text and aligns the MOperation to the specification
     * given. The line should be on the following form:<ul>
     * <li> visibility name (parameter list) : return-type-expression
     * {property-string}
     * </ul>
     *
     * All elements are optional and, if left unspecified, will preserve their
     * old values.<p>
     *
     * <em>Stereotypes</em> can be given between any element in the line on the
     * form: &lt;&lt;stereotype1,stereotype2,stereotype3&gt;&gt;<p>
     *
     * The following properties are recognized to have special meaning:
     * abstract, concurrency, concurrent, guarded, leaf, query, root and
     * sequential.<p>
     *
     * This syntax is compatible with the UML 1.3 spec.<p>
     *
     * (formerly visibility name (parameter list) : return-type-expression
     * {property-string} ) (formerly 2nd: [visibility] [keywords] returntype
     * name(params)[;] )
     *
     * @param s   The String to parse.
     * @param op  The MOperation to adjust to the spcification in s.
     * @throws ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     */
    public void parseOperation(String s, Object op) throws ParseException {
        MyTokenizer st;
        boolean hasColon = false;
        String name = null;
        String parameterlist = null;
        String stereotype = null;
        String token;
        String type = null;
        String visibility = null;
        Vector properties = null;
        int paramOffset = 0;

        s = s.trim();

        if (s.length() > 0 && VISIBILITYCHARS.indexOf(s.charAt(0)) >= 0) {
            visibility = s.substring(0, 1);
            s = s.substring(1);
        }

        try {
            st = new MyTokenizer(s, " ,\t,<<,>>,:,=,{,},\\,",
                    operationCustomSep);
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (" ".equals(token) || "\t".equals(token)
                        || ",".equals(token)) {
                    ;// Do nothing
                } else if ("<<".equals(token)) {
                    if (stereotype != null) {
                        throw new ParseException("Operations cannot have two "
                                + "sets of stereotypes", st.getTokenIndex());
                    }
                    stereotype = "";
                    while (true) {
                        token = st.nextToken();
                        if (">>".equals(token)) {
                            break;
                        }
                        stereotype += token;
                    }
                } else if ("{".equals(token)) {
                    String propname = "";
                    String propvalue = null;

                    if (properties == null) {
                        properties = new Vector();
                    }
                    while (true) {
                        token = st.nextToken();
                        if (",".equals(token) || "}".equals(token)) {
                            if (propname.length() > 0) {
                                properties.add(propname);
                                properties.add(propvalue);
                            }
                            propname = "";
                            propvalue = null;

                            if ("}".equals(token)) {
                                break;
                            }
                        } else if ("=".equals(token)) {
                            if (propvalue != null) {
                                throw new ParseException("Property " + propname
                                        + " cannot have two values", st
                                        .getTokenIndex());
                            }
                            propvalue = "";
                        } else if (propvalue == null) {
                            propname += token;
                        } else {
                            propvalue += token;
                        }
                    }
                    if (propname.length() > 0) {
                        properties.add(propname);
                        properties.add(propvalue);
                    }
                } else if (":".equals(token)) {
                    hasColon = true;
                } else if ("=".equals(token)) {
                    throw new ParseException("Operations cannot have "
                            + "default values", st.getTokenIndex());
                } else if (token.charAt(0) == '(' && !hasColon) {
                    if (parameterlist != null) {
                        throw new ParseException("Operations cannot have two "
                                + "parameter lists", st.getTokenIndex());
                    }

                    parameterlist = token;
                } else {
                    if (hasColon) {
                        if (type != null) {
                            throw new ParseException("Operations cannot have "
                                    + "two types", st.getTokenIndex());
                        }

                        if (token.length() > 0
                                && (token.charAt(0) == '\"'
                                    || token.charAt(0) == '\'')) {
                            throw new ParseException("Type cannot be quoted",
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0 && token.charAt(0) == '(') {
                            throw new ParseException("Type cannot be an "
                                    + "expression", st.getTokenIndex());
                        }

                        type = token;
                    } else {
                        if (name != null && visibility != null) {
                            throw new ParseException("Extra text in Operation",
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0
                                && (token.charAt(0) == '\"'
                                    || token.charAt(0) == '\'')) {
                            throw new ParseException(
                                    "Name or visibility cannot" + " be quoted",
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0 && token.charAt(0) == '(') {
                            throw new ParseException(
                                    "Name or visibility cannot"
                                            + " be an expression", st
                                            .getTokenIndex());
                        }

                        if (name == null
                                && visibility == null
                                && token.length() > 1
                                && VISIBILITYCHARS.indexOf(token.charAt(0))
                                                    >= 0) {
                            visibility = token.substring(0, 1);
                            token = token.substring(1);
                        }

                        if (name != null) {
                            visibility = name;
                            name = token;
                        } else {
                            name = token;
                        }
                    }
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException("Unexpected end of operation", s.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (parameterlist != null) {
            // parameterlist is guaranteed to contain at least "("
            if (parameterlist.charAt(parameterlist.length() - 1) != ')') {
                throw new ParseException("The parameter list was incomplete",
                        paramOffset + parameterlist.length() - 1);
            }

            paramOffset++;
            parameterlist = parameterlist.substring(1,
                    parameterlist.length() - 1);
            NotationUtilityUml.parseParamList(op, parameterlist, paramOffset);
        }

        if (visibility != null) {
            Model.getCoreHelper().setVisibility(op,
                    getVisibility(visibility.trim()));
        }

        if (name != null) {
            Model.getCoreHelper().setName(op, name.trim());
        } else if (Model.getFacade().getName(op) == null
                || "".equals(Model.getFacade().getName(op))) {
            Model.getCoreHelper().setName(op, "anonymous");
        }

        if (type != null) {
            Object ow = Model.getFacade().getOwner(op);
            Object ns = null;
            if (ow != null && Model.getFacade().getNamespace(ow) != null) {
                ns = Model.getFacade().getNamespace(ow);
            } else {
                ns = Model.getFacade().getModel(op);
            }
            Object mtype = NotationUtilityUml.getType(type.trim(), ns);
            setReturnParameter(op, mtype);
        }

        if (properties != null) {
            setProperties(op, properties, operationSpecialStrings);
        }

        NotationUtilityUml.dealWithStereotypes(op, stereotype, true);
    }



    /**
     * Sets the return parameter of op to be of type type. If there is none, one
     * is created. If there are many, all but one are removed.
     */
    private void setReturnParameter(Object op, Object type) {
        Object param = null;
        Iterator it = Model.getFacade().getParameters(op).iterator();
        while (it.hasNext()) {
            Object p = it.next();
            if (Model.getFacade().isReturn(p)) {
                param = p;
                break;
            }
        }
        while (it.hasNext()) {
            Object p = it.next();
            if (Model.getFacade().isReturn(p)) {
                ProjectManager.getManager().getCurrentProject().moveToTrash(p);
            }
        }
        if (param == null) {
            Object model =
                ProjectManager.getManager()
                	.getCurrentProject().getModel();
            Object voidType =
                ProjectManager.getManager()
                	.getCurrentProject().findType("void");
            param =
                Model.getCoreFactory()
                	.buildParameter(
                	        op, model, voidType);
        }
        Model.getCoreHelper().setType(param, type);
    }



    /**
     * Parse a line on the form:<pre>
     *      visibility name [: type-expression] [= initial-value]
     * </pre>
     *
     * <ul>
     * <li>If only one of visibility and name is given, then it is assumed to
     * be the name and the visibility is left unchanged.
     * <li>Type and initial value can be given in any order.
     * <li>Properties can be given between any element on the form<pre>
     *      {[name] [= [value]] [, ...]}
     * </pre>
     * <li>Multiplicity can be given between any element except after the
     * initial-value and before the type or end (to allow java-style array
     * indexing in the initial value). It must be given on form [multiplicity]
     * with the square brackets included.
     * <li>Stereotypes can be given between any element except after the
     * initial-value and before the type or end (to allow java-style bit-shifts
     * in the initial value). It must be given on form
     * &lt;&lt;stereotype1,stereotype2,stereotype3&gt;&gt;.
     * </ul>
     *
     * The following properties are recognized to have special meaning:
     * frozen.<p>
     *
     * This syntax is compatible with the UML 1.3 spec.
     *
     * (formerly: visibility name [multiplicity] : type-expression =
     * initial-value {property-string} ) (2nd formerly: [visibility] [keywords]
     * type name [= init] [;] )
     *
     * @param text    The String to parse.
     * @param attribute The attribute to modify to comply with the instructions in s.
     * @throws ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     */
    protected void parseAttribute(
            String text,
            Object attribute) throws ParseException {
        String multiplicity = null;
        String name = null;
        Vector properties = null;
        String stereotype = null;
        String token;
        String type = null;
        String value = null;
        String visibility = null;
        boolean hasColon = false;
        boolean hasEq = false;
        int multindex = -1;
        MyTokenizer st, mst;

        text = text.trim();
        if (text.length() > 0 && VISIBILITYCHARS.indexOf(text.charAt(0)) >= 0) {
            visibility = text.substring(0, 1);
            text = text.substring(1);
        }

        try {
            st = new MyTokenizer(text, " ,\t,<<,>>,[,],:,=,{,},\\,",
                    attributeCustomSep);
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (" ".equals(token) || "\t".equals(token)
                        || ",".equals(token)) {
                    if (hasEq) {
                        value += token;
                    }
                } else if ("<<".equals(token)) {
                    if (hasEq) {
                        value += token;
                    } else {
                        if (stereotype != null) {
                            throw new ParseException(
                                "Attribute cannot have "
                                + "two sets of stereotypes", 
                                st.getTokenIndex());
                        }
                        stereotype = "";
                        while (true) {
                            token = st.nextToken();
                            if (">>".equals(token)) {
                                break;
                            }
                            stereotype += token;
                        }
                    }
                } else if ("[".equals(token)) {
                    if (hasEq) {
                        value += token;
                    } else {
                        if (multiplicity != null) {
                            throw new ParseException(
                                    "Attribute cannot have two"
                                            + " multiplicities", st
                                            .getTokenIndex());
                        }
                        multiplicity = "";
                        multindex = st.getTokenIndex() + 1;
                        while (true) {
                            token = st.nextToken();
                            if ("]".equals(token)) {
                                break;
                            }
                            multiplicity += token;
                        }
                    }
                } else if ("{".equals(token)) {
                    String propname = "";
                    String propvalue = null;

                    if (properties == null) {
                        properties = new Vector();
                    }
                    while (true) {
                        token = st.nextToken();
                        if (",".equals(token) || "}".equals(token)) {
                            if (propname.length() > 0) {
                                properties.add(propname);
                                properties.add(propvalue);
                            }
                            propname = "";
                            propvalue = null;

                            if ("}".equals(token)) {
                                break;
                            }
                        } else if ("=".equals(token)) {
                            if (propvalue != null) {
                                throw new ParseException("Property " + propname
                                        + " cannot have two values", st
                                        .getTokenIndex());
                            }
                            propvalue = "";
                        } else if (propvalue == null) {
                            propname += token;
                        } else {
                            propvalue += token;
                        }
                    }
                    if (propname.length() > 0) {
                        properties.add(propname);
                        properties.add(propvalue);
                    }
                } else if (":".equals(token)) {
                    hasColon = true;
                    hasEq = false;
                } else if ("=".equals(token)) {
                    if (value != null) {
                        throw new ParseException("Attribute cannot have two "
                                + "default values", st.getTokenIndex());
                    }
                    value = "";
                    hasColon = false;
                    hasEq = true;
                } else {
                    if (hasColon) {
                        if (type != null) {
                            throw new ParseException(
                                    "Attribute cannot have two" + " types", st
                                            .getTokenIndex());
                        }
                        if (token.length() > 0
                                && (token.charAt(0) == '\"'
                                    || token.charAt(0) == '\'')) {
                            throw new ParseException("Type cannot be quoted",
                                    st.getTokenIndex());
                        }
                        if (token.length() > 0 && token.charAt(0) == '(') {
                            throw new ParseException("Type cannot be an "
                                    + "expression", st.getTokenIndex());
                        }
                        type = token;
                    } else if (hasEq) {
                        value += token;
                    } else {
                        if (name != null && visibility != null) {
                            throw new ParseException("Extra text in Attribute",
                                    st.getTokenIndex());
                        }
                        if (token.length() > 0
                                && (token.charAt(0) == '\"'
                                    || token.charAt(0) == '\'')) {
                            throw new ParseException(
                                    "Name or visibility cannot" + " be quoted",
                                    st.getTokenIndex());
                        }
                        if (token.length() > 0 && token.charAt(0) == '(') {
                            throw new ParseException(
                                    "Name or visibility cannot"
                                            + " be an expression", st
                                            .getTokenIndex());
                        }

                        if (name == null
                                && visibility == null
                                && token.length() > 1
                                && VISIBILITYCHARS.indexOf(token.charAt(0))
                                                        >= 0) {
                            visibility = token.substring(0, 1);
                            token = token.substring(1);
                        }

                        if (name != null) {
                            visibility = name;
                            name = token;
                        } else {
                            name = token;
                        }
                    }
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException(
                    "Unexpected end of attribute", text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        LOG.debug("ParseAttribute [name: " + name + " visibility: "
                + visibility + " type: " + type + " value: " + value
                + " stereo: " + stereotype + " mult: " + multiplicity);

        if (properties != null) {
            for (int i = 0; i + 1 < properties.size(); i += 2) {
                LOG.debug("\tProperty [name: " + properties.get(i) + " = "
                        + properties.get(i + 1) + "]");
            }
        }

        if (visibility != null) {
            Model.getCoreHelper().setVisibility(attribute,
                    getVisibility(visibility.trim()));
        }

        if (name != null) {
            Model.getCoreHelper().setName(attribute, name.trim());
        } else if (Model.getFacade().getName(attribute) == null
                || "".equals(Model.getFacade().getName(attribute))) {
            Model.getCoreHelper().setName(attribute, "anonymous");
        }

        if (type != null) {
            Object ow = Model.getFacade().getOwner(attribute);
            Object ns = null;
            if (ow != null && Model.getFacade().getNamespace(ow) != null) {
                ns = Model.getFacade().getNamespace(ow);
            } else {
                ns = Model.getFacade().getModel(attribute);
            }
            Model.getCoreHelper().setType(attribute, 
                    NotationUtilityUml.getType(type.trim(), ns));
        }

        if (value != null) {
            Object initExpr = Model.getDataTypesFactory().createExpression(
                Notation.getConfigueredNotation().toString(), value.trim());
            Model.getCoreHelper().setInitialValue(attribute, initExpr);
        }

        if (multiplicity != null) {
            try {
                Model.getCoreHelper().setMultiplicity(
                        attribute,
                        Model.getDataTypesFactory()
                        	.createMultiplicity(multiplicity.trim()));
            } catch (IllegalArgumentException iae) {
                throw new ParseException("Bad multiplicity (" + iae + ")",
                        multindex);
            }
        }

        if (properties != null) {
            setProperties(attribute, properties, attributeSpecialStrings);
        }

        NotationUtilityUml.dealWithStereotypes(attribute, stereotype, true);
    }

    /**
     * Finds a visibility for the visibility specified by name. If no known
     * visibility can be deduced, private visibility is used.
     *
     * @param name
     *            The Java name of the visibility.
     * @return A visibility corresponding to name.
     */
    private Object getVisibility(String name) {
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
    private void setProperties(Object elem, Vector prop,
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

} /* end class ParserDisplay */
