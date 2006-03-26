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
import java.util.Collection;
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
                Collection propertyChangeListeners =
                    currentProject.findFigsForMember(classifier);
                Object model = currentProject.getModel();
                Object voidType = currentProject.findType("void");
                Object newOp =
                    Model.getCoreFactory()
                    	.buildOperation(classifier, model, voidType,
                    	        propertyChangeListeners);
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
        int i = new ArrayList(Model.getFacade().getFeatures(classifier)).indexOf(attribute);
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
    public Object parseExtensionPoint(String text) {

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
            Collection propertyChangeListeners =
                ProjectManager.getManager()
                	.getCurrentProject().findFigsForMember(op);
            param =
                Model.getCoreFactory()
                	.buildParameter(
                	        op, model, voidType,
                	        propertyChangeListeners);
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
    public void parseAttribute(
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
                                "Attribute cannot have two sets of stereotypes", 
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

    /**
     * Parses a ClassifierRole represented by the following line of the format:
     *
     * <pre>
     * baselist := [base] [, base]*
     * classifierRole := [name] [/ role] [: baselist]
     * </pre>
     *
     * <code>role </code> and <code>baselist</code> can be given in
     * any order.<p>
     *
     * This syntax is compatible with the UML 1.3 specification.
     *
     * (formerly: "name: base" )
     *
     * @param cls the classifier role to apply any changes to
     * @param s   the String to parse
     * @throws java.text.ParseException
     *            when it detects an error in the attribute string. See also
     *            ParseError.getErrorOffset().
     */
    public void parseClassifierRole(Object cls, String s)
        throws ParseException {
        String name = null;
        String token;
        String role = null;
        String base = null;
        Vector bases = null;
        boolean hasColon = false;
        boolean hasSlash = false;

        try {
            MyTokenizer st = new MyTokenizer(s, " ,\t,/,:,\\,");

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (" ".equals(token) || "\t".equals(token)) {
                    // Do nothing
                } else if ("/".equals(token)) {
                    hasSlash = true;
                    hasColon = false;

                    if (base != null) {
                        if (bases == null) {
                            bases = new Vector();
                        }
                        bases.add(base);
                    }
                    base = null;
                } else if (":".equals(token)) {
                    hasColon = true;
                    hasSlash = false;

                    if (bases == null) {
                        bases = new Vector();
                    }
                    if (base != null) {
                        bases.add(base);
                    }
                    base = null;
                } else if (",".equals(token)) {
                    if (base != null) {
                        if (bases == null) {
                            bases = new Vector();
                        }
                        bases.add(base);
                    }
                    base = null;
                } else if (hasColon) {
                    if (base != null) {
                        throw new ParseException(
                                "Extra text in Classifier Role", st
                                        .getTokenIndex());
                    }

                    base = token;
                } else if (hasSlash) {
                    if (role != null) {
                        throw new ParseException(
                                "Extra text in Classifier Role", st
                                        .getTokenIndex());
                    }

                    role = token;
                } else {
                    if (name != null) {
                        throw new ParseException(
                                "Extra text in Classifier Role", st
                                        .getTokenIndex());
                    }

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException("Unexpected end of attribute", s.length());
        }

        if (base != null) {
            if (bases == null) {
                bases = new Vector();
            }
            bases.add(base);
        }

        // TODO: What to do about object name???
        //    if (name != null)
        //	;

        if (role != null) {
            Model.getCoreHelper().setName(cls, role.trim());
        }

        if (bases != null) {
            // Remove bases that aren't there anymore

            // copy - can't iterate modify live collection while iterating it
            Collection b = new ArrayList(Model.getFacade().getBases(cls));
            Iterator it = b.iterator();
            Object c;
            Object ns = Model.getFacade().getNamespace(cls);
            if (ns != null && Model.getFacade().getNamespace(ns) != null) {
                ns = Model.getFacade().getNamespace(ns);
            } else {
                ns = Model.getFacade().getModel(cls);
            }

            while (it.hasNext()) {
                c = it.next();
                if (!bases.contains(Model.getFacade().getName(c))) {
                    Model.getCollaborationsHelper().removeBase(cls, c);
                }
            }

            it = bases.iterator();
        addBases:
            while (it.hasNext()) {
                String d = ((String) it.next()).trim();

                Iterator it2 = b.iterator();
                while (it2.hasNext()) {
                    c = it2.next();
                    if (d.equals(Model.getFacade().getName(c))) {
                        continue addBases;
                    }
                }
                c = NotationUtilityUml.getType(d, ns);
                if (Model.getFacade().isACollaboration(
                        Model.getFacade().getNamespace(c))) {
                    Model.getCoreHelper().setNamespace(c, ns);
                }
                Model.getCollaborationsHelper().addBase(cls, c);
            }
        }
    }

    /**
     * Parse a Message textual description.<p>
     *
     * TODO: - This method is too complex, lets break it up. <p>
     *
     * Parses a message line of the form:
     *
     * <pre>
     * intno := integer|name
     * seq := intno ['.' intno]*
     * recurrance := '*'['//'] | '*'['//']'[' <i>iteration </i>']' | '['
     * <i>condition </i>']'
     * seqelem := {[intno] ['['recurrance']']}
     * seq2 := seqelem ['.' seqelem]*
     * ret_list := lvalue [',' lvalue]*
     * arg_list := rvalue [',' rvalue]*
     * message := [seq [',' seq]* '/'] seq2 ':' [ret_list :=] name ([arg_list])
     * </pre>
     *
     * Which is rather complex, so a few examples:<ul>
     * <li> 2: display(x, y)
     * <li> 1.3.1: p := find(specs)
     * <li> [x &lt; 0] 4: invert(color)
     * <li> A3, B4/ C3.1*: update()
     * </ul>
     *
     * This syntax is compatible with the UML 1.3 specification.<p>
     *
     * Actually, only a subset of this syntax is currently supported, and some
     * is not even planned to be supported. The exceptions are intno, which
     * allows a number possibly followed by a sequence of letters in the range
     * 'a' - 'z', seqelem, which does not allow a recurrance, and message, which
     * does allow one recurrance near seq2. (formerly: name: action )
     *
     * @param mes the MMessage to apply any changes to
     * @param s   the String to parse
     * @throws ParseException
     *            when it detects an error in the attribute string. See also
     *            ParseError.getErrorOffset().
     */
    public void parseMessage(Object mes, String s) throws ParseException {
        String fname = null;
        String guard = null;
        String paramExpr = null;
        String token;
        String varname = null;
        Vector predecessors = new Vector();
        Vector seqno = null;
        Vector currentseq = new Vector();
        Vector args = null;
        boolean mustBePre = false;
        boolean mustBeSeq = false;
        boolean parallell = false;
        boolean iterative = false;
        boolean mayDeleteExpr = false;
        boolean refindOperation = false;
        boolean hasPredecessors = false;
        int i;

        currentseq.add(null);
        currentseq.add(null);

        try {
            MyTokenizer st = new MyTokenizer(s, " ,\t,*,[,],.,:,=,/,\\,",
                    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if (" ".equals(token) || "\t".equals(token)) {
                    if (currentseq == null) {
                        if (varname != null && fname == null) {
                            varname += token;
                        }
                    }
                } else if ("[".equals(token)) {
                    if (mustBePre) {
                        throw new ParseException("Predecessors cannot be "
                                + "qualified", st.getTokenIndex());
                    }
                    mustBeSeq = true;

                    if (guard != null) {
                        throw new ParseException("Messages cannot have several"
                                + " guard or iteration specifications", st
                                .getTokenIndex());
                    }

                    guard = "";
                    while (true) {
                        token = st.nextToken();
                        if ("]".equals(token)) {
                            break;
                        }
                        guard += token;
                    }
                } else if ("*".equals(token)) {
                    if (mustBePre) {
                        throw new ParseException("Predecessors cannot be "
                                + "iterated", st.getTokenIndex());
                    }
                    mustBeSeq = true;

                    if (currentseq != null) {
                        iterative = true;
                    }
                } else if (".".equals(token)) {
                    if (currentseq == null) {
                        throw new ParseException("Unexpected dot ('.')", st
                                .getTokenIndex());
                    }
                    if (currentseq.get(currentseq.size() - 2) != null
                            || currentseq.get(currentseq.size() - 1) != null) {
                        currentseq.add(null);
                        currentseq.add(null);
                    }
                } else if (":".equals(token)) {
                    if (st.hasMoreTokens()) {
                        String t = st.nextToken();
                        if ("=".equals(t)) {
                            st.putToken(":=");
                            continue;
                        }
                        st.putToken(t);
                    }

                    if (mustBePre) {
                        throw new ParseException("Predecessors must be "
                                + "terminated with \'/\' and not with \':\'",
                                st.getTokenIndex());
                    }

                    if (currentseq != null) {
                        if (currentseq.size() > 2
                            && currentseq.get(currentseq.size() - 2) == null
                            && currentseq.get(currentseq.size() - 1) == null) {
                            currentseq.remove(currentseq.size() - 1);
                            currentseq.remove(currentseq.size() - 1);
                        }

                        seqno = currentseq;
                        currentseq = null;
                        mayDeleteExpr = true;
                    }
                } else if ("/".equals(token)) {
                    if (st.hasMoreTokens()) {
                        String t = st.nextToken();
                        if ("/".equals(t)) {
                            st.putToken("//");
                            continue;
                        }
                        st.putToken(t);
                    }

                    if (mustBeSeq) {
                        throw new ParseException("The sequence number must be "
                                + "terminated with \':\' and not with \'/\'",
                                st.getTokenIndex());
                    }

                    mustBePre = false;
                    mustBeSeq = true;

                    if (currentseq.size() > 2
                            && currentseq.get(currentseq.size() - 2) == null
                            && currentseq.get(currentseq.size() - 1) == null) {
                        currentseq.remove(currentseq.size() - 1);
                        currentseq.remove(currentseq.size() - 1);
                    }

                    if (currentseq.get(currentseq.size() - 2) != null
                            || currentseq.get(currentseq.size() - 1) != null) {

                        predecessors.add(currentseq);

                        currentseq = new Vector();
                        currentseq.add(null);
                        currentseq.add(null);
                    }
                    hasPredecessors = true;
                } else if ("//".equals(token)) {
                    if (mustBePre) {
                        throw new ParseException("Predecessors cannot be "
                                + "parallellized", st.getTokenIndex());
                    }
                    mustBeSeq = true;

                    if (currentseq != null) {
                        parallell = true;
                    }
                } else if (",".equals(token)) {
                    if (currentseq != null) {
                        if (mustBeSeq) {
                            throw new ParseException("Messages cannot have "
                                    + "many sequence numbers", st
                                    .getTokenIndex());
                        }
                        mustBePre = true;

                        if (currentseq.size() > 2
                             && currentseq.get(currentseq.size() - 2) == null
                             && currentseq.get(currentseq.size() - 1) == null) {

                            currentseq.remove(currentseq.size() - 1);
                            currentseq.remove(currentseq.size() - 1);
                        }

                        if (currentseq.get(currentseq.size() - 2) != null
                            || currentseq.get(currentseq.size() - 1) != null) {

                            predecessors.add(currentseq);

                            currentseq = new Vector();
                            currentseq.add(null);
                            currentseq.add(null);
                        }
                        hasPredecessors = true;
                    } else {
                        if (varname == null && fname != null) {
                            varname = fname + token;
                            fname = null;
                        } else if (varname != null && fname == null) {
                            varname += token;
                        } else {
                            throw new ParseException(
                                    "Unexpected character (,)", st
                                            .getTokenIndex());
                        }
                    }
                } else if ("=".equals(token) || ":=".equals(token)) {
                    if (currentseq == null) {
                        if (varname == null) {
                            varname = fname;
                            fname = "";
                        } else {
                            fname = "";
                        }
                    }
                } else if (currentseq == null) {
                    if (paramExpr == null && token.charAt(0) == '(') {
                        if (token.charAt(token.length() - 1) != ')') {
                            throw new ParseException("Malformed parameters", st
                                    .getTokenIndex());
                        }
                        if (fname == null || "".equals(fname)) {
                            throw new ParseException("Must be a function name "
                                    + "before the parameters", st
                                    .getTokenIndex());
                        }
                        if (varname == null) {
                            varname = "";
                        }
                        paramExpr = token.substring(1, token.length() - 1);
                    } else if (varname != null && fname == null) {
                        varname += token;
                    } else if (fname == null || fname.length() == 0) {
                        fname = token;
                    } else {
                        throw new ParseException("Unexpected token (" + token
                                + ")", st.getTokenIndex());
                    }
                } else {
                    boolean hasVal =
                        currentseq.get(currentseq.size() - 2) != null;
                    boolean hasOrd =
                        currentseq.get(currentseq.size() - 1) != null;
                    boolean assigned = false;
                    int bp = findMsgOrderBreak(token);

                    if (!hasVal && !assigned && bp == token.length()) {
                        try {
                            currentseq.set(currentseq.size() - 2, new Integer(
                                    token));
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!hasOrd && !assigned && bp == 0) {
                        try {
                            currentseq.set(currentseq.size() - 1, new Integer(
                                    parseMsgOrder(token)));
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!hasVal && !hasOrd && !assigned && bp > 0
                            && bp < token.length()) {
                        Integer nbr, ord;
                        try {
                            nbr = new Integer(token.substring(0, bp));
                            ord = new Integer(
                                    parseMsgOrder(token.substring(bp)));
                            currentseq.set(currentseq.size() - 2, nbr);
                            currentseq.set(currentseq.size() - 1, ord);
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!assigned) {
                        throw new ParseException("Unexpected token (" + token
                                + ")", st.getTokenIndex());
                    }
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException("Unexpected end of message", s.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (paramExpr != null) {
            MyTokenizer st = new MyTokenizer(paramExpr, "\\,",
                    parameterCustomSep);
            args = new Vector();
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if (",".equals(token)) {
                    if (args.size() == 0) {
                        args.add(null);
                    }
                    args.add(null);
                } else {
                    if (args.size() == 0) {
                        if (token.trim().length() == 0) {
                            continue;
                        }
                        args.add(null);
                    }
                    String arg = (String) args.get(args.size() - 1);
                    if (arg != null) {
                        arg = arg + token;
                    } else {
                        arg = token;
                    }
                    args.set(args.size() - 1, arg);
                }
            }
        } else if (mayDeleteExpr) {
            args = new Vector();
        }

        if (LOG.isDebugEnabled()) {
            StringBuffer buf = new StringBuffer();
            buf.append("ParseMessage: " + s + "\n");
            buf.append("Message: ");
            for (i = 0; seqno != null && i + 1 < seqno.size(); i += 2) {
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(seqno.get(i) + " (" + seqno.get(i + 1) + ")");
            }
            buf.append("\n");
            buf.append("predecessors: " + predecessors.size() + "\n");
            for (i = 0; i < predecessors.size(); i++) {
                int j;
                Vector v = (Vector) predecessors.get(i);
                buf.append("    Predecessor: ");
                for (j = 0; v != null && j + 1 < v.size(); j += 2) {
                    if (j > 0) {
                        buf.append(", ");
                    }
                    buf.append(v.get(j) + " (" + v.get(j + 1) + ")");
                }
            }
            buf.append("guard: " + guard + " it: " + iterative + " pl: "
                    + parallell + "\n");
            buf.append(varname + " := " + fname + " ( " + paramExpr + " )"
                    + "\n");
            LOG.debug(buf);
        }

        if (Model.getFacade().getAction(mes) == null) {
            Object a = Model.getCommonBehaviorFactory()
                    .createCallAction();
            Model.getCoreHelper().addOwnedElement(Model.getFacade().getContext(
                    Model.getFacade().getInteraction(mes)), a);
            Model.getCollaborationsHelper().setAction(mes, a);
        }

        if (guard != null) {
            guard = "[" + guard.trim() + "]";
            if (iterative) {
                if (parallell) {
                    guard = "*//" + guard;
                } else {
                    guard = "*" + guard;
                }
            }
            Object expr =
                Model.getDataTypesFactory().createIterationExpression(
                        Notation.getConfigueredNotation().toString(), guard);
            Model.getCommonBehaviorHelper().setRecurrence(
                    Model.getFacade().getAction(mes), expr);
        }

        if (fname == null) {
            if (!mayDeleteExpr
                    && Model.getFacade().getScript(
                            Model.getFacade().getAction(mes))
                                            != null) {
                String body =
                    (String) Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes)));

                int idx = body.indexOf(":=");
                if (idx >= 0) {
                    idx++;
                } else {
                    idx = body.indexOf("=");
                }

                if (idx >= 0) {
                    fname = body.substring(idx + 1);
                } else {
                    fname = body;
                }
            } else {
                fname = "";
            }
        }

        if (varname == null) {
            if (!mayDeleteExpr
                    && Model.getFacade().getScript(
                            Model.getFacade().getAction(mes))
                                            != null) {
                String body =
                    (String) Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes)));
                int idx = body.indexOf(":=");
                if (idx < 0) {
                    idx = body.indexOf("=");
                }

                if (idx >= 0) {
                    varname = body.substring(0, idx);
                } else {
                    varname = "";
                }
            } else {
                varname = "";
            }
        }

        if (fname != null) {
            String expr = fname.trim();
            if (varname.length() > 0) {
                expr = varname.trim() + " := " + expr;
            }

            if (Model.getFacade().getScript(
                    Model.getFacade().getAction(mes)) == null
                    || !expr.equals(Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes))))) {
                Object e =
                    Model.getDataTypesFactory()
                        .createActionExpression(
                                Notation.getConfigueredNotation().toString(),
                                expr.trim());
                Model.getCommonBehaviorHelper().setScript(
                        Model.getFacade().getAction(mes), e);
                refindOperation = true;
            }
        }

        if (args != null) {
            Collection c = new ArrayList(
                Model.getFacade().getActualArguments(
                        Model.getFacade().getAction(mes)));
            Iterator it = c.iterator();
            for (i = 0; i < args.size(); i++) {
                Object arg = (it.hasNext() ? /* (MArgument) */it.next() : null);
                if (arg == null) {
                    arg = Model.getCommonBehaviorFactory()
                            .createArgument();
                    Model.getCommonBehaviorHelper().addActualArgument(
                            Model.getFacade().getAction(mes), arg);
                    refindOperation = true;
                }
                if (Model.getFacade().getValue(arg) == null
                        || !args.get(i).equals(
                              Model.getFacade().getBody(
                                      Model.getFacade().getValue(arg)))) {
                    String value = (args.get(i) != null ? (String) args.get(i)
                            : "");
                    Object e =
                        Model.getDataTypesFactory().createExpression(
                            Notation.getConfigueredNotation().toString(),
                            value.trim());
                    Model.getCommonBehaviorHelper().setValue(arg, e);
                }
            }

            while (it.hasNext()) {
                Model.getCommonBehaviorHelper()
                	.removeActualArgument(Model.getFacade().getAction(mes),
                	        it.next());
                refindOperation = true;
            }
        }

        if (seqno != null) {
            Object/* MMessage */root;
            // Find the preceding message, if any, on either end of the
            // association.
            String pname = "";
            String mname = "";
            String gname =
                GeneratorDisplay.getInstance().generateMessageNumber(mes);
            boolean swapRoles = false;
            int majval = 0;
            if (seqno.get(seqno.size() - 2) != null) {
                majval =
                    Math.max(((Integer) seqno.get(seqno.size() - 2)).intValue()
                             - 1,
                             0);
            }
            int minval = 0;
            if (seqno.get(seqno.size() - 1) != null) {
                minval =
                    Math.max(((Integer) seqno.get(seqno.size() - 1)).intValue(),
                            0);
            }

            for (i = 0; i + 1 < seqno.size(); i += 2) {
                int bv = 1;
                if (seqno.get(i) != null) {
                    bv = Math.max(((Integer) seqno.get(i)).intValue(), 1);
                }

                int sv = 0;
                if (seqno.get(i + 1) != null) {
                    sv = Math.max(((Integer) seqno.get(i + 1)).intValue(), 0);
                }

                if (i > 0) {
                    mname += ".";
                }
                mname += Integer.toString(bv) + (char) ('a' + sv);

                if (i + 3 < seqno.size()) {
                    if (i > 0) {
                        pname += ".";
                    }
                    pname += Integer.toString(bv) + (char) ('a' + sv);
                }
            }

            root = null;
            if (pname.length() > 0) {
                root = findMsg(Model.getFacade().getSender(mes), pname);
                if (root == null) {
                    root = findMsg(Model.getFacade().getReceiver(mes), pname);
                    if (root != null) {
                        swapRoles = true;
                    }
                }
            } else if (!hasMsgWithActivator(Model.getFacade().getSender(mes),
                    			    null)
                    && hasMsgWithActivator(Model.getFacade().getReceiver(mes),
                            		   null)) {
                swapRoles = true;
            }

            if (compareMsgNumbers(mname, gname)) {
                ;// Do nothing
            } else if (isMsgNumberStartOf(gname, mname)) {
                throw new ParseException("Cannot move a message into the "
                        + "subtree rooted at self", 0);
            } else if (Model.getFacade().getPredecessors(mes).size() > 1
                    && Model.getFacade().getMessages3(mes).size() > 1) {
                throw new ParseException("Cannot move a message which is "
                        + "both start and end of several threads", 0);
            } else if (root == null && pname.length() > 0) {
                throw new ParseException("Cannot find the activator for the "
                        + "message", 0);
            } else if (swapRoles
                    && Model.getFacade().getMessages4(mes).size() > 0
                    && (Model.getFacade().getSender(mes)
                            != Model.getFacade().getReceiver(mes))) {
                throw new ParseException("Cannot reverse the direction of a "
                        + "message that is an activator", 0);
            } else {
                // Disconnect the message from the call graph
                // Make copies of returned live collections since we're modifying
                Collection c = new ArrayList(
                        Model.getFacade().getPredecessors(mes));
                Collection c2 = new ArrayList(
                        Model.getFacade().getMessages3(mes));
                Iterator it;

                it = c2.iterator();
                while (it.hasNext()) {
                    Model.getCollaborationsHelper().removeMessage3(mes,
                            /* (MMessage) */it.next());
                }

                it = c.iterator();
                while (it.hasNext()) {
                    Iterator it2 = c2.iterator();
                    Object pre = /* (MMessage) */it.next();
                    Model.getCollaborationsHelper().removePredecessor(mes, pre);
                    while (it2.hasNext()) {
                        Model.getCollaborationsHelper().addPredecessor(
                                it2.next(), pre);
                    }
                }

                // Connect the message at a new spot
                Model.getCollaborationsHelper().setActivator(mes, root);
                if (swapRoles) {
                    Object/* MClassifierRole */r =
                        Model.getFacade().getSender(mes);
                    Model.getCollaborationsHelper().setSender(mes,
                            Model.getFacade().getReceiver(mes));
                    Model.getCommonBehaviorHelper().setReceiver(mes, r);
                }

                if (root == null) {
                    c =
                        filterWithActivator(
                            Model.getFacade().getMessages2(
                                    Model.getFacade().getSender(mes)),
                                    null);
                } else {
                    c = Model.getFacade().getMessages4(root);
                }
                c2 = findCandidateRoots(c, root, mes);
                it = c2.iterator();
                // If c2 is empty, then we're done (or there is a
                // cycle in the message graph, which would be bad) If
                // c2 has more than one element, then the model is
                // crappy, but we'll just use one of them anyway
                if (majval <= 0) {
                    while (it.hasNext()) {
                        Model.getCollaborationsHelper().addMessage3(mes,
                                /* (MMessage) */it.next());
                    }
                } else if (it.hasNext()) {
                    Object/* MMessage */pre =
                        walk(/* (MMessage) */it.next(), majval - 1, false);
                    Object/* MMessage */post = successor(pre, minval);
                    if (post != null) {
                        Model.getCollaborationsHelper()
                            .removePredecessor(post, pre);
                        Model.getCollaborationsHelper()
                            .addPredecessor(post, mes);
                    }
                    insertSuccessor(pre, mes, minval);
                }
                refindOperation = true;
            }
        }

        if (fname != null && refindOperation) {
            Object role = Model.getFacade().getReceiver(mes);
            Vector ops =
                getOperation(
                        Model.getFacade().getBases(role),
                        fname.trim(),
                        Model.getFacade().getActualArguments(
                                Model.getFacade().getAction(mes)).size());

            // TODO: Should someone choose one, if there are more
            // than one?
            if (Model.getFacade().isACallAction(
                    Model.getFacade().getAction(mes))) {
                Object a = /* (MCallAction) */Model.getFacade().getAction(mes);
                if (ops.size() > 0) {
                    Model.getCommonBehaviorHelper().setOperation(a,
                            /* (MOperation) */ops.get(0));
                } else {
                    Model.getCommonBehaviorHelper().setOperation(a, null);
                }
            }
        }

        // TODO: Predecessors is not implemented, because it
        // causes some problems that I've not found an easy way to handle yet,
        // d00mst. The specific problem is that the notation currently is
        // ambiguous on second message after a thread split.

        // Why not implement it anyway? d00mst

        if (hasPredecessors) {
            Collection roots =
                findCandidateRoots(
                        Model.getFacade().getMessages(
                                Model.getFacade().getInteraction(mes)),
                        null,
                        null);
            Vector pre = new Vector();
            Iterator it;
        predfor:
            for (i = 0; i < predecessors.size(); i++) {
                it = roots.iterator();
                while (it.hasNext()) {
                    Object msg =
                        walkTree(it.next(), (Vector) predecessors.get(i));
                    if (msg != null && msg != mes) {
                        if (isBadPreMsg(mes, msg)) {
                            throw new ParseException(
                                    "One predecessor cannot be a predecessor "
                                            + "to this message", 0);
                        }
                        pre.add(msg);
                        continue predfor;
                    }
                }
                throw new ParseException("Could not find predecessor", 0);
            }
            GeneratorDisplay.MsgPtr ptr =
                GeneratorDisplay.getInstance().new MsgPtr();
            GeneratorDisplay.getInstance().recCountPredecessors(mes, ptr);
            if (ptr.message != null && !pre.contains(ptr.message)) {
                pre.add(ptr.message);
            }
            Model.getCollaborationsHelper().setPredecessors(mes, pre);
        }
    }

    /**
     * Examines the call tree from chld to see if ans is an ancestor.
     *
     * @param ans
     *            MMessage
     * @param chld
     *            MMessage
     */
    private boolean isBadPreMsg(Object ans, Object chld) {
        while (chld != null) {
            if (ans == chld) {
                return true;
            }
            if (isPredecessorMsg(ans, chld, 100)) {
                return true;
            }
            chld = Model.getFacade().getActivator(chld);
        }
        return false;
    }

    /**
     * Examines the call tree from suc to see if pre is a predecessor. This
     * function is recursive and md specifies the maximum level of recursions
     * allowed.
     *
     * @param pre
     *            MMessage
     * @param suc
     *            MMessage
     */
    private boolean isPredecessorMsg(Object pre, Object suc, int md) {
        Iterator it = Model.getFacade().getPredecessors(suc).iterator();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (m == pre) {
                return true;
            }
            if (md > 0 && isPredecessorMsg(pre, m, md - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Walks a call tree from a root node following the directions given in path
     * to a destination node. If the destination node cannot be reached, then
     * null is returned.
     *
     * @param root The root of the call tree.
     * @param path The path to walk in the call tree.
     * @return The message at the end of path, or <code>null</code>.
     */
    private Object walkTree(Object root, Vector path) {
        int i;
        for (i = 0; i + 1 < path.size(); i += 2) {
            int bv = 0;
            if (path.get(i) != null) {
                bv = Math.max(((Integer) path.get(i)).intValue() - 1, 0);
            }

            int sv = 0;
            if (path.get(i + 1) != null) {
                sv = Math.max(((Integer) path.get(i + 1)).intValue(), 0);
            }

            root = walk(root, bv - 1, true);
            if (root == null) {
                return null;
            }
            if (bv > 0) {
                root = successor(root, sv);
                if (root == null) {
                    return null;
                }
            }
            if (i + 3 < path.size()) {
                Iterator it =
                    findCandidateRoots(
                            Model.getFacade().getMessages4(root),
                            root,
                            null).iterator();
                // Things are strange if there are more than one candidate root,
                // it has no obvious interpretation in terms of a call tree.
                if (!it.hasNext()) {
                    return null;
                }
                root = /* (MMessage) */it.next();
            }
        }
        return root;
    }

    /**
     * Examines a collection to see if any message has the given message as an
     * activator.
     *
     * @param r
     *            MClassifierRole
     * @param m
     *            MMessage
     */
    private boolean hasMsgWithActivator(Object r, Object m) {
        Iterator it = Model.getFacade().getMessages2(r).iterator();
        while (it.hasNext()) {
            if (Model.getFacade().getActivator(it.next()) == m) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts message s as the p'th successor of message m.
     *
     * @param m
     *            MMessage
     * @param s
     *            MMessage
     */
    private void insertSuccessor(Object m, Object s, int p) {
        Vector v = new Vector(Model.getFacade().getMessages3(m));
        if (v.size() > p) {
            v.insertElementAt(s, p);
        } else {
            v.add(s);
        }
        Model.getCollaborationsHelper().setMessages3(m, v);
    }

    /**
     * Finds the steps'th successor of message r in the sense that it is a
     * direct successor of r. Returns null if r has fewer successors.
     */
    private Object/* MMessage */successor(Object/* MMessage */r, int steps) {
        Iterator it = Model.getFacade().getMessages3(r).iterator();
        while (it.hasNext() && steps > 0) {
            it.next();
            steps--;
        }
        if (it.hasNext()) {
            return /* (MMessage) */it.next();
        }
        return null;
    }

    /**
     * Finds the steps'th successor of r in the sense that it is a successor of
     * a successor of r (steps times). The first successor with the same
     * activator as r is used in each step. If there are not enough successors,
     * then struct determines the result. If struct is true, then null is
     * returned, otherwise the last successor found.
     */
    private Object walk(Object/* MMessage */r, int steps, boolean strict) {
        Object/* MMessage */act = Model.getFacade().getActivator(r);
        while (steps > 0) {
            Iterator it = Model.getFacade().getMessages3(r).iterator();
            do {
                if (!it.hasNext()) {
                    return (strict ? null : r);
                }
                r = /* (MMessage) */it.next();
            } while (Model.getFacade().getActivator(r) != act);
            steps--;
        }
        return r;
    }

    /**
     * Finds the root candidates in a collection c, ie the messages in c that
     * has the activator a (may be null) and has no predecessor with the same
     * activator. If veto isn't null, then the message in veto will not be
     * included in the Collection of candidates.
     *
     * @param c The collection.
     * @param a The message.
     * @param veto The veto message.
     * @return The found roots.
     */
    private Collection findCandidateRoots(Collection c, Object a,
            Object veto) {
        Iterator it = c.iterator();
        ArrayList v = new ArrayList();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (m == veto) {
                continue;
            }
            if (Model.getFacade().getActivator(m) != a) {
                continue;
            }
            Iterator it2 = Model.getFacade().getPredecessors(m).iterator();
            boolean candidate = true;
            while (it2.hasNext()) {
                Object m2 = /* (MMessage) */it2.next();
                if (Model.getFacade().getActivator(m2) == a) {
                    candidate = false;
                }
            }
            if (candidate) {
                v.add(m);
            }
        }
        return v;
    }

    /**
     * Finds the messages in Collection c that has message a as activator.
     */
    private Collection filterWithActivator(Collection c, Object/*MMessage*/a) {
        Iterator it = c.iterator();
        ArrayList v = new ArrayList();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (Model.getFacade().getActivator(m) == a) {
                v.add(m);
            }
        }
        return v;
    }

    /**
     * Finds the message in ClassifierRole r that has the message number written
     * in n. If it isn't found, null is returned.
     */
    private Object findMsg(Object/* MClassifierRole */r, String n) {
        Collection c = Model.getFacade().getMessages1(r);
        Iterator it = c.iterator();
        while (it.hasNext()) {
            Object msg = /* (MMessage) */it.next();
            String gname =
                GeneratorDisplay.getInstance()
                	.generateMessageNumber(msg);
            if (compareMsgNumbers(gname, n)) {
                return msg;
            }
        }
        return null;
    }

    /**
     * Compares two message numbers with each other to see if they are equal, in
     * the sense that they refer to the same position in a call tree.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return <code>true</code> if they are the same.
     */
    private boolean compareMsgNumbers(String n1, String n2) {
        return isMsgNumberStartOf(n1, n2) && isMsgNumberStartOf(n2, n1);
    }

    /**
     * Compares two message numbers n1, n2 with each other to determine if n1
     * specifies a the same position as n2 in a call tree or n1 specifies a
     * position that is a father of the position specified by n2.
     */
    private boolean isMsgNumberStartOf(String n1, String n2) {
        int i, j, len, jlen;
        len = n1.length();
        jlen = n2.length();
        i = 0;
        j = 0;
        for (; i < len;) {
            int ibv, isv;
            int jbv, jsv;

            ibv = 0;
            for (; i < len; i++) {
                char c = n1.charAt(i);
                if (c < '0' || c > '9') {
                    break;
                }
                ibv *= 10;
                ibv += c - '0';
            }
            isv = 0;
            for (; i < len; i++) {
                char c = n1.charAt(i);
                if (c < 'a' || c > 'z') {
                    break;
                }
                isv *= 26;
                isv += c - 'a';
            }

            jbv = 0;
            for (; j < jlen; j++) {
                char c = n2.charAt(j);
                if (c < '0' || c > '9') {
                    break;
                }
                jbv *= 10;
                jbv += c - '0';
            }
            jsv = 0;
            for (; j < jlen; j++) {
                char c = n2.charAt(j);
                if (c < 'a' || c > 'z') {
                    break;
                }
                jsv *= 26;
                jsv += c - 'a';
            }

            if (ibv != jbv || isv != jsv) {
                return false;
            }

            if (i < len && n1.charAt(i) != '.') {
                return false;
            }
            i++;

            if (j < jlen && n2.charAt(j) != '.') {
                return false;
            }
            j++;
        }
        return true;
    }

    /**
     * Finds the operations in Collection c with name name and params number of
     * parameters. If no operation is found, one is created. The applicable
     * operations are returned.
     *
     * @param c the collection of operations to be searched
     * @param name the name of the operation to be found
     * @param params the number of parameters of the operation to be found
     * @return the sought operation
     */
    private Vector getOperation(Collection c, String name, int params) {
        Vector options = new Vector();
        Iterator it;

        if (name == null || name.length() == 0) {
            return options;
        }

        it = c.iterator();
        while (it.hasNext()) {
            Object clf = /* (MClassifier) */it.next();
            Collection oe = Model.getFacade().getFeatures(clf);
            Iterator it2 = oe.iterator();
            while (it2.hasNext()) {
                Object me = /* (MModelElement) */it2.next();
                if (!(Model.getFacade().isAOperation(me))) {
                    continue;
                }

                Object op = /* (MOperation) */me;
                if (!name.equals(Model.getFacade().getName(op))) {
                    continue;
                }
                if (params != countParameters(op)) {
                    continue;
                }
                options.add(op);
            }
        }
        if (options.size() > 0) {
            return options;
        }

        it = c.iterator();
        if (it.hasNext()) {
            String expr = name + "(";
            int i;
            for (i = 0; i < params; i++) {
                if (i > 0) {
                    expr += ", ";
                }
                expr += "param" + (i + 1);
            }
            expr += ")";
            // Jaap Branderhorst 2002-23-09 added next lines to link
            // parameters and operations to the figs that represent
            // them
            Object cls = /* (MClassifier) */it.next();
            Collection propertyChangeListeners =
                ProjectManager.getManager()
                	.getCurrentProject().findFigsForMember(cls);
            Object model =
                ProjectManager.getManager()
                	.getCurrentProject().getModel();
            Object voidType =
                ProjectManager.getManager()
                	.getCurrentProject().findType("void");
            Object op =
                Model.getCoreFactory()
                	.buildOperation(cls, model, voidType,
                	        propertyChangeListeners);

            try {
                parseOperation(expr, op);
            } catch (ParseException pe) {
                LOG.error("Unexpected ParseException in getOperation: " + pe,
                        pe);

            }
            options.add(op);
        }
        return options;
    }

    /**
     * Counts the number of parameters that are not return values.
     */
    private int countParameters(Object bf) {
        Collection c = Model.getFacade().getParameters(bf);
        Iterator it = c.iterator();
        int count = 0;

        while (it.hasNext()) {
            Object p = it.next();
            if (Model.getFacade().isReturn(p)) {
                continue;
            }
            count++;
        }

        return count;
    }

    /**
     * Parses a message order specification.
     */
    private static int parseMsgOrder(String s) {
        int i, t;
        int v = 0;

        t = s.length();
        for (i = 0; i < t; i++) {
            char c = s.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new NumberFormatException();
            }
            v *= 26;
            v += c - 'a';
        }

        return v;
    }

    /**
     * Finds the break between message number and (possibly) message order.
     *
     * @return The position of the end of the number.
     */
    private static int findMsgOrderBreak(String s) {
        int i, t;

        t = s.length();
        for (i = 0; i < t; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                break;
            }
        }
        return i;
    }

} /* end class ParserDisplay */
