// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import org.argouml.application.api.Notation;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.activitygraphs.ActivityGraphsFactory;
import org.argouml.model.uml.behavioralelements.activitygraphs.ActivityGraphsHelper;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.foundation.datatypes.DataTypesFactory;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ProfileJava;
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
    public void found(Object element, String value);
}

/**
 * Declares a string that should take special action when it is found as a
 * property in {@link ParserDisplay#setProperties ParserDisplay.setProperties}.
 * 
 * <p>
 * <b>Example: </b>
 * 
 * <pre>
 * _attributeSpecialStrings[0] = new PropertySpecialString(&quot;frozen&quot;,
 *         new PropertyOperation() {
 *             public void found(Object element, String value) {
 *                 if (ModelFacade.isAStructuralFeature(element))
 *                     ModelFacade.setChangeable(element, 
 *                          (value != null &amp;&amp; value
 *                             .equalsIgnoreCase(&quot;false&quot;)));
 *             }
 *         });
 * </pre>
 * 
 * <p>
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
     * @return <tt>true</tt> if an action is performed, otherwise
     *         <tt>false</tt>.
     */
    public boolean invoke(Object element, String pname, String value) {
        if (!name.equalsIgnoreCase(pname))
            return false;
        op.found(element, value);
        return true;
    }
}

/**
 * This class is responsible for the parsing of the text that the user entered
 * on the display, i.e. the diagram. Consequently, the UML elements represented
 * by the text are created or adapted.
 * 
 * There is a certain relation in namegiving with the class GeneratorDisplay,
 * which generates a textual representation of UML elements for displaying on
 * diagrams.
 * 
 * @stereotype singleton
 */

public class ParserDisplay extends Parser {
    /** The one and only ParserDisplay */
    public static final ParserDisplay SINGLETON = new ParserDisplay();

    /**
     * The standard error etc. logger
     */
    private static final Logger LOG = Logger.getLogger(ParserDisplay.class);

    /** The array of special properties for attributes */
    private PropertySpecialString attributeSpecialStrings[];

    /** The vector of CustomSeparators to use when tokenizing attributes */
    private Vector attributeCustomSep;

    /** The array of special properties for operations */
    private PropertySpecialString operationSpecialStrings[];

    /** The vector of CustomSeparators to use when tokenizing attributes */
    private Vector operationCustomSep;

    /** The vector of CustomSeparators to use when tokenizing parameters */
    private Vector parameterCustomSep;

    /**
     * The character with a meaning as a visibility at the start of an attribute
     */
    private static final String VISIBILITYCHARS = "+#-";

    /**
     * Constructs the object contained in SINGLETON and initializes some
     * instance variables.
     * 
     * @see #SINGLETON
     */
    private ParserDisplay() {
        attributeSpecialStrings = new PropertySpecialString[2];
        attributeSpecialStrings[0] = new PropertySpecialString("frozen",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    if (ModelFacade.isAStructuralFeature(element)) {
                        if ("false".equalsIgnoreCase(value)) {
                            ModelFacade.setChangeability(element,
                                        ModelFacade.CHANGEABLE_CHANGEABLEKIND);
                        } else {
                            ModelFacade.setChangeability(element,
                                        ModelFacade.FROZEN_CHANGEABLEKIND);
                        }
                    }
                }
            });
        attributeSpecialStrings[1] = new PropertySpecialString("addonly",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    if (ModelFacade.isAStructuralFeature(element)) {
                        if ("false".equalsIgnoreCase(value)) {
                            ModelFacade.setChangeability(element,
                                        ModelFacade.CHANGEABLE_CHANGEABLEKIND);
                        } else {
                            ModelFacade.setChangeability(element,
                                        ModelFacade.ADD_ONLY_CHANGEABLEKIND);
                        }
                    }
                }
            });
        attributeCustomSep = new Vector();
        attributeCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        attributeCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        attributeCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

        operationSpecialStrings = new PropertySpecialString[8];
        operationSpecialStrings[0] = new PropertySpecialString("sequential",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    if (ModelFacade.isAOperation(element)) {
                        ModelFacade.setConcurrency(element,
                                    ModelFacade.SEQUENTIAL_CONCURRENCYKIND);
                    }
                }
            });
        operationSpecialStrings[1] = new PropertySpecialString("guarded",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    Object kind = ModelFacade.GUARDED_CONCURRENCYKIND;
                    if (value != null && value.equalsIgnoreCase("false"))
                            kind = ModelFacade.SEQUENTIAL_CONCURRENCYKIND;
                    if (ModelFacade.isAOperation(element))
                            ModelFacade.setConcurrency(element, kind);
                }
            });
        operationSpecialStrings[2] = new PropertySpecialString("concurrent",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    Object kind = ModelFacade.CONCURRENT_CONCURRENCYKIND;
                    if (value != null && value.equalsIgnoreCase("false"))
                            kind = ModelFacade.SEQUENTIAL_CONCURRENCYKIND;
                    if (ModelFacade.isAOperation(element))
                            ModelFacade.setConcurrency(element, kind);
                }
            });
        operationSpecialStrings[3] = new PropertySpecialString("concurrency",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    Object kind = ModelFacade.SEQUENTIAL_CONCURRENCYKIND;
                    if ("guarded".equalsIgnoreCase(value))
                            kind = ModelFacade.GUARDED_CONCURRENCYKIND;
                    else if ("concurrent".equalsIgnoreCase(value))
                            kind = ModelFacade.CONCURRENT_CONCURRENCYKIND;
                    if (ModelFacade.isAOperation(element))
                            ModelFacade.setConcurrency(element, kind);
                }
            });
        operationSpecialStrings[4] = new PropertySpecialString("abstract",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    boolean isAbstract = true;
                    if (value != null && value.equalsIgnoreCase("false")) {
                        isAbstract = false;
                    }
                    if (ModelFacade.isAOperation(element)) {
                        ModelFacade.setAbstract(element, isAbstract);
                    }
                }
            });
        operationSpecialStrings[5] = new PropertySpecialString("leaf",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    boolean isLeaf = true;
                    if (value != null && value.equalsIgnoreCase("false")) {
                        isLeaf = false;
                    }
                    if (ModelFacade.isAOperation(element)) {
                        ModelFacade.setLeaf(element, isLeaf);
                    }
                }
            });
        operationSpecialStrings[6] = new PropertySpecialString("query",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    boolean isQuery = true;
                    if (value != null && value.equalsIgnoreCase("false"))
                            isQuery = false;
                    if (ModelFacade.isABehavioralFeature(element))
                            ModelFacade.setQuery(element, isQuery);
                }
            });
        operationSpecialStrings[7] = new PropertySpecialString("root",
                new PropertyOperation() {
                public void found(Object element, String value) {
                    boolean isRoot = true;
                    if (value != null && value.equalsIgnoreCase("false"))
                            isRoot = false;
                    if (ModelFacade.isAOperation(element))
                            ModelFacade.setRoot(element, isRoot);
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
     * <p>
     * Parse an extension point.
     * <p>
     * 
     * <p>
     * The syntax is "name: location", "name:", "location" or "". The fields of
     * the extension point are updated appropriately.
     * </p>
     * 
     * @param useCase
     *            The use case that owns this extension point
     * 
     * @param ep
     *            The extension point concerned
     * 
     * @param text
     *            The text to parse
     *  
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
            ModelFacade.removeExtensionPoint(useCase, ep);
        } else {
            ModelFacade.setName(ep, ModelFacade.getName(newEp));
            ModelFacade.setLocation(ep, ModelFacade.getLocation(newEp));
        }
    }

    /**
     * Parses a model element, ie reads a string on the format: <br>[ < <
     * stereotype >>] [name] <br>
     * and assigns the properties to the passed MModelElement.
     * 
     * @param me
     *            The MModelElement <i>text </i> describes.
     * @param text
     *            A String on the above format.
     * @throws java.text.ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     */
    public void parseModelElement(Object/* MModelElement */me, String text)
        throws ParseException {
        MyTokenizer st;

        Vector path = null;
        String name = null;
        String stereotype = null;
        String token;

        try {
            st = new MyTokenizer(text, "<<,>>,::");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if ("<<".equals(token)) {
                    if (stereotype != null)
                        throw new ParseException("Element cannot have "
                                + "two stereotypes", st.getTokenIndex());

                    stereotype = "";
                    while (true) {
                        token = st.nextToken();
                        if (">>".equals(token))
                            break;
                        stereotype += token;
                    }
                } else if ("::".equals(token)) {
                    if (name != null)
                        name = name.trim();

                    if (path != null && (name == null || "".equals(name)))
                        throw new ParseException("Element cannot have "
                                + "anonymous qualifiers", st.getTokenIndex());

                    if (path == null)
                        path = new Vector();
                    if (name != null)
                        path.add(name);
                    name = null;
                } else {
                    if (name != null)
                        throw new ParseException("Element cannot have "
                                + "two word names or qualifiers", st
                                .getTokenIndex());

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException("Unexpected end of element", 
                    text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (name != null)
            name = name.trim();

        if (path != null && (name == null || "".equals(name)))
            throw new ParseException("Qualified names must end with a name", 0);

        if (name != null)
            ModelFacade.setName(me, name);

        if (stereotype != null) {
            stereotype = stereotype.trim();
            Object stereo = getStereotype(me, stereotype);

            if (stereo != null)
                ModelFacade.setStereotype(me, stereo);
            else if ("".equals(stereotype))
                ModelFacade.setStereotype(me, null);
        }

        if (path != null) {
            Object nspe = ModelManagementHelper.getHelper().getElement(path,
                    ModelFacade.getModel(me));

            if (nspe == null || !(ModelFacade.isANamespace(nspe)))
                throw new ParseException("Unable to resolve namespace", 0);
            if (!CoreHelper.getHelper().getAllPossibleNamespaces(me).contains(
                    nspe))
                throw new ParseException("Invalid namespace for element", 0);

            ModelFacade.addOwnedElement(nspe, me);
        }
    }

    /**
     * Checks for ';' in Strings or chars in ';' separated tokens in order to
     * return an index to the next attribute or operation substring, -1
     * otherwise (a ';' inside a String or char delimiters is ignored)
     */
    private int indexOfNextCheckedSemicolon(String s, int start) {
        if (s == null || start < 0 || start >= s.length())
            return -1;
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
     * @param cls
     *            MClassifier The classifier the operation(s) belong to
     * @param op
     *            MOperation The operation on which the editing happened
     * @param text
     *            The string to parse
     * @throws ParseException for invalid input
     */
    public void parseOperationFig(Object cls, Object op, String text)
        throws ParseException {
        if (cls == null || op == null) {
            return;
        }
        ParseException pex = null;
        int start = 0;
        int end = indexOfNextCheckedSemicolon(text, start);
        if (end == -1) {
            //no text? remove op!
            ModelFacade.removeFeature(cls, op);
            return;
        }
        String s = text.substring(start, end).trim();
        if (s == null || s.length() == 0) {
            //no non-whitechars in text? remove op!
            ModelFacade.removeFeature(cls, op);
            return;
        }
        parseOperation(s, op);
        int i = new ArrayList(ModelFacade.getFeatures(cls)).indexOf(op);
        // check for more operations (';' separated):
        start = end + 1;
        end = indexOfNextCheckedSemicolon(text, start);
        while (end > start && end <= text.length()) {
            s = text.substring(start, end).trim();
            if (s != null && s.length() > 0) {
                // yes, there are more:
                Object newOp = UmlFactory.getFactory().getCore()
                        .buildOperation(cls);
                if (newOp != null) {
                    try {
                        parseOperation(s, newOp);
                        //newOp.setOwnerScope(op.getOwnerScope()); //
                        //not needed in case of operation
                        if (i != -1) {
                            ModelFacade.addFeature(cls, ++i, newOp);
                        } else {
                            ModelFacade.addFeature(cls, newOp);
                        }
                    } catch (ParseException ex) {
                        if (pex == null)
                            pex = ex;
                    }
                }
            }
            start = end + 1;
            end = indexOfNextCheckedSemicolon(text, start);
        }
        if (pex != null)
            throw pex;
    }

    /**
     * Parse a string representing one ore more ';' separated attributes. The
     * case that a String or char contains a ';' (e.g. in an initializer) is
     * handled, but not other occurences of ';'.
     * 
     * @param cls
     *            MClassifier The classifier the attribute(s) belong to
     * @param at
     *            MAttribute The attribute on which the editing happened
     * @param text
     *            The string to parse
     * @throws ParseException for invalid input
     */
    public void parseAttributeFig(Object cls, Object at, String text)
        throws ParseException {
        if (cls == null || at == null) {
            return;
        }
        ParseException pex = null;
        int start = 0;
        int end = indexOfNextCheckedSemicolon(text, start);
        if (end == -1) {
            //no text? remove attr!
            ModelFacade.removeFeature(cls, at);
            return;
        }
        String s = text.substring(start, end).trim();
        if (s == null || s.length() == 0) {
            //no non-whitechars in text? remove attr!
            ModelFacade.removeFeature(cls, at);
            return;
        }
        parseAttribute(s, at);
        int i = new ArrayList(ModelFacade.getFeatures(cls)).indexOf(at);
        // check for more attributes (';' separated):
        start = end + 1;
        end = indexOfNextCheckedSemicolon(text, start);
        while (end > start && end <= text.length()) {
            s = text.substring(start, end).trim();
            if (s != null && s.length() > 0) {
                // yes, there are more:
                Object newAt = UmlFactory.getFactory().getCore()
                        .buildAttribute();
                if (newAt != null) {
                    try {
                        parseAttribute(s, newAt);
                        ModelFacade.setOwnerScope(newAt, ModelFacade
                                .getOwnerScope(at));
                        if (i != -1) {
                            ModelFacade.addFeature(cls, ++i, newAt);
                        } else {
                            ModelFacade.addFeature(cls, newAt);
                        }
                    } catch (ParseException ex) {
                        if (pex == null)
                            pex = ex;
                    }
                }
            }
            start = end + 1;
            end = indexOfNextCheckedSemicolon(text, start);
        }
        if (pex != null)
            throw pex;
    }

    /**
     * <p>
     * Parse a string representing an extension point and return a new extension
     * point.
     * </p>
     * 
     * <p>
     * The syntax is "name: location", "name:", "location" or "". <em>Note</em>.
     * If either field is blank, it will be set to null in the extension point.
     * </p>
     * 
     * <p>
     * We break up the string into tokens at the ":". We must keep the ":" as a
     * token, so we can distinguish between "name:" and "location". The number
     * of tokens will distinguish our four cases.
     * </p>
     * 
     * TODO: This method needs to be replaced, since it by design cannot cope
     * with the current design of the model component.
     * 
     * @param text
     *            The string to parse
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
        Object ep = UmlFactory.getFactory().getUseCases()
                .createExtensionPoint();

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
                ModelFacade.setName(ep, null);
                ModelFacade.setLocation(ep, null);
            } else {
                ModelFacade.setName(ep, null);
                ModelFacade.setLocation(ep, epLocation);
            }

            break;

        case 2:

            // A string of the form "name:"

            epName = st.nextToken().trim();

            ModelFacade.setName(ep, epName);
            ModelFacade.setLocation(ep, null);

            break;

        case 3:

            // A string of the form "name:location". Discard the middle token
            // (":")

            epName = st.nextToken().trim();
            st.nextToken(); // Read past the colon.
            epLocation = st.nextToken().trim();

            ModelFacade.setName(ep, epName);
            ModelFacade.setLocation(ep, epLocation);

            break;
        }

        return ep;
    }

    /**
     * Parse a line of text and aligns the MOperation to the specification
     * given. The line should be on the following form:
     * 
     * <br>
     * visibility name (parameter list) : return-type-expression
     * {property-string}
     * 
     * <p>
     * All elements are optional and, if left unspecified, will preserve their
     * old values. <br>
     * A <b>stereotype </b> can be given between any element in the line on the
     * form: &lt;&lt;stereotype&gt;&gt;
     * 
     * <p>
     * The following properties are recognized to have special meaning:
     * abstract, concurrency, concurrent, guarded, leaf, query, root and
     * sequential.
     * 
     * <p>
     * This syntax is compatible with the UML 1.3 spec.
     * 
     * (formerly visibility name (parameter list) : return-type-expression
     * {property-string} ) (formerly 2nd: [visibility] [keywords] returntype
     * name(params)[;] )
     * 
     * @param s
     *            The String to parse.
     * @param op
     *            The MOperation to adjust to the spcification in s.
     * @throws java.text.ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     *
     * @see org.argouml.uml.generator.Parser#parseOperation(String, Object)
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
                    if (stereotype != null)
                        throw new ParseException("Operations cannot have two "
                                + "stereotypes", st.getTokenIndex());
                    stereotype = "";
                    while (true) {
                        token = st.nextToken();
                        if (">>".equals(token))
                            break;
                        stereotype += token;
                    }
                } else if ("{".equals(token)) {
                    String propname = "";
                    String propvalue = null;

                    if (properties == null)
                        properties = new Vector();
                    while (true) {
                        token = st.nextToken();
                        if (",".equals(token) || "}".equals(token)) {
                            if (propname.length() > 0) {
                                properties.add(propname);
                                properties.add(propvalue);
                            }
                            propname = "";
                            propvalue = null;

                            if ("}".equals(token))
                                break;
                        } else if ("=".equals(token)) {
                            if (propvalue != null)
                                throw new ParseException("Property " + propname
                                        + " cannot have two values", st
                                        .getTokenIndex());
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
                    if (parameterlist != null)
                        throw new ParseException("Operations cannot have two "
                                + "parameter lists", st.getTokenIndex());

                    parameterlist = token;
                } else {
                    if (hasColon) {
                        if (type != null)
                            throw new ParseException("Operations cannot have "
                                    + "two types", st.getTokenIndex());

                        if (token.length() > 0
                                && (token.charAt(0) == '\"' 
                                    || token.charAt(0) == '\''))
                            throw new ParseException("Type cannot be quoted",
                                    st.getTokenIndex());

                        if (token.length() > 0 && token.charAt(0) == '(')
                            throw new ParseException("Type cannot be an "
                                    + "expression", st.getTokenIndex());

                        type = token;
                    } else {
                        if (name != null && visibility != null)
                            throw new ParseException("Extra text in Operation",
                                    st.getTokenIndex());

                        if (token.length() > 0
                                && (token.charAt(0) == '\"' 
                                    || token.charAt(0) == '\''))
                            throw new ParseException(
                                    "Name or visibility cannot" + " be quoted",
                                    st.getTokenIndex());

                        if (token.length() > 0 && token.charAt(0) == '(')
                            throw new ParseException(
                                    "Name or visibility cannot"
                                            + " be an expression", st
                                            .getTokenIndex());

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
            if (parameterlist.charAt(parameterlist.length() - 1) != ')')
                throw new ParseException("The parameter list was incomplete",
                        paramOffset + parameterlist.length() - 1);

            paramOffset++;
            parameterlist = parameterlist.substring(1,
                    parameterlist.length() - 1);
            parseParamList(op, parameterlist, paramOffset);
        }

        if (visibility != null) {
            short vis = getVisibility(visibility.trim());
            ModelFacade.setVisibility(op, vis);
        }

        if (name != null)
            ModelFacade.setName(op, name.trim());
        else if (ModelFacade.getName(op) == null
                || "".equals(ModelFacade.getName(op)))
            ModelFacade.setName(op, "anonymous");

        if (type != null) {
            Object ow = ModelFacade.getOwner(op);
            Object ns = null;
            if (ow != null && ModelFacade.getNamespace(ow) != null)
                ns = ModelFacade.getNamespace(ow);
            else
                ns = ModelFacade.getModel(op);
            Object mtype = getType(type.trim(), ns);
            setReturnParameter(op, mtype);
        }

        if (properties != null)
            setProperties(op, properties, operationSpecialStrings);

        if (stereotype != null) {
            stereotype = stereotype.trim();
            Object stereo = getStereotype(op, stereotype);
            if (stereo != null)
                ModelFacade.setStereotype(op, stereo);
            else if ("".equals(stereotype))
                ModelFacade.setStereotype(op, null);
        }
    }

    /**
     * Parses a parameter list and aligns the parameter list in op to that
     * specified in param. A parameter list generally has the following syntax:
     * 
     * <br>
     * param := [inout] [name] [: type] [= initial value] <br>
     * list := [param] [, param]*
     * 
     * <p>
     * <b>inout </b> is optional and if omitted the old value preserved. If no
     * value has been assigned, then <b>in </b> is assumed. <br>
     * <b>name </b>, <b>type </b> and <b>initial value </b> are optional and if
     * omitted the old value preserved. <br>
     * <b>type </b> and <b>initial value </b> can be given in any order. <br>
     * Unspecified properties is carried over by position, so if a parameter is
     * inserted into the list, then it will inherit properties from the
     * parameter that was there before for unspecified properties.
     * 
     * <p>
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
    private void parseParamList(Object op, String param, int paramOffset)
        throws ParseException {
        MyTokenizer st = new MyTokenizer(param, " ,\t,:,=,\\,",
                parameterCustomSep);
        Collection origParam = ModelFacade.getParameters(op);
        Object ns = ModelFacade.getModel(op);
        if (ModelFacade.isAOperation(op)) {
            Object ow = ModelFacade.getOwner(op);

            if (ow != null && ModelFacade.getNamespace(ow) != null)
                ns = ModelFacade.getNamespace(ow);
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
                if (ModelFacade.isReturn(p))
                    p = null;
            }

            while (st.hasMoreTokens()) {
                tok = st.nextToken();

                if (",".equals(tok)) {
                    break;
                } else if (" ".equals(tok) || "\t".equals(tok)) {
                    if (hasEq)
                        value += tok;
                } else if (":".equals(tok)) {
                    hasColon = true;
                    hasEq = false;
                } else if ("=".equals(tok)) {
                    if (value != null)
                        throw new ParseException("Parameters cannot have two "
                                + "default values", paramOffset
                                + st.getTokenIndex());
                    hasEq = true;
                    hasColon = false;
                    value = "";
                } else if (hasColon) {
                    if (type != null)
                        throw new ParseException("Parameters cannot have two "
                                + "types", paramOffset + st.getTokenIndex());

                    if (tok.charAt(0) == '\'' || tok.charAt(0) == '\"')
                        throw new ParseException("Parameter type cannot be "
                                + "quoted", paramOffset + st.getTokenIndex());

                    if (tok.charAt(0) == '(')
                        throw new ParseException("Parameter type cannot be an "
                                + "expression", paramOffset
                                + st.getTokenIndex());

                    type = tok;
                } else if (hasEq) {
                    value += tok;
                } else {
                    if (name != null && kind != null)
                        throw new ParseException("Extra text in parameter",
                                paramOffset + st.getTokenIndex());

                    if (tok.charAt(0) == '\'' || tok.charAt(0) == '\"')
                        throw new ParseException(
                                "Parameter name/kind cannot be" + " quoted",
                                paramOffset + st.getTokenIndex());

                    if (tok.charAt(0) == '(')
                        throw new ParseException(
                                "Parameter name/kind cannot be"
                                        + " an expression", paramOffset
                                        + st.getTokenIndex());

                    kind = name;
                    name = tok;
                }
            }

            if (p == null) {
                p = CoreFactory.getFactory().buildParameter(op);
                // op.addParameter(p);
            }

            if (name != null)
                ModelFacade.setName(p, name.trim());

            if (kind != null)
                setParamKind(p, kind.trim());

            if (type != null)
                ModelFacade.setType(p, getType(type.trim(), ns));

            if (value != null) {
                Object initExpr = UmlFactory.getFactory().getDataTypes()
                        .createExpression(
                                Notation.getDefaultNotation().toString(),
                                value.trim());
                ModelFacade.setDefaultValue(p, initExpr);
            }
        }

        while (it.hasNext()) {
            Object p = it.next();
            if (!ModelFacade.isReturn(p))
                ModelFacade.removeParameter(op, p);
        }
    }

    /**
     * Sets the return parameter of op to be of type type. If there is none, one
     * is created. If there are many, all but one are removed.
     */
    private void setReturnParameter(Object op, Object type) {
        Object param = null;
        Iterator it = ModelFacade.getParameters(op).iterator();
        while (it.hasNext()) {
            Object p = it.next();
            if (ModelFacade.isReturn(p)) {
                param = p;
                break;
            }
        }
        while (it.hasNext()) {
            Object p = it.next();
            if (ModelFacade.isReturn(p))
                ModelFacade.removeParameter(op, p);
        }
        if (param == null) {
            param = UmlFactory.getFactory().getCore().buildParameter(op);
        }
        ModelFacade.setType(param, type);
    }

    private void setParamKind(Object p, String s) {
        if ("out".equalsIgnoreCase(s))
            ModelFacade.setKindToOut(p);
        else if ("inout".equalsIgnoreCase(s))
            ModelFacade.setKindToInOut(p);
        else
            ModelFacade.setKindToIn(p);
    }


    /**
     * Parse a line on the form: <br>
     * visibility name [: type-expression] [= initial-value]
     * <ul>
     * <li>If only one of visibility and name is given, then it is assumed to
     * be the name and the visibility is left unchanged.
     * <li>Type and initial value can be given in any order.
     * <li>Properties can be given between any element on the form {[name] [=
     * [value]] [, ...]}.
     * <li>Multiplicity can be given between any element except after the
     * initial-value and before the type or end (to allow java-style array
     * indexing in the initial value). It must be given on form [multiplicity]
     * with the square brackets included.
     * <li>Stereotype can be given between any element except after the
     * initial-value and before the type or end (to allow java-style bit-shifts
     * in the initial value). It must be given on form
     * &lt;&lt;stereotype&gt;&gt;.
     * </ul>
     * 
     * <p>
     * The following properties are recognized to have special meaning: frozen.
     * 
     * <p>
     * This syntax is compatible with the UML 1.3 spec.
     * 
     * (formerly: visibility name [multiplicity] : type-expression =
     * initial-value {property-string} ) (2nd formerly: [visibility] [keywords]
     * type name [= init] [;] )
     * 
     * @param s
     *            The String to parse.
     * @param attr
     *            The attribute to modify to comply with the instructions in s.
     * @throws java.text.ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     * 
     * @see org.argouml.uml.generator.Parser#parseAttribute(String, Object)
     */
    public void parseAttribute(String s, Object attr) throws ParseException {

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
        MyTokenizer st;

        s = s.trim();
        if (s.length() > 0 && VISIBILITYCHARS.indexOf(s.charAt(0)) >= 0) {
            visibility = s.substring(0, 1);
            s = s.substring(1);
        }

        try {
            st = new MyTokenizer(s, " ,\t,<<,>>,[,],:,=,{,},\\,",
                    attributeCustomSep);
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (" ".equals(token) || "\t".equals(token)
                        || ",".equals(token)) {
                    if (hasEq)
                        value += token;
                } else if ("<<".equals(token)) {
                    if (hasEq) {
                        value += token;
                    } else {
                        if (stereotype != null)
                            throw new ParseException(
                                    "Attribute cannot have two"
                                            + " stereotypes", st
                                            .getTokenIndex());
                        stereotype = "";
                        while (true) {
                            token = st.nextToken();
                            if (">>".equals(token))
                                break;
                            stereotype += token;
                        }
                    }
                } else if ("[".equals(token)) {
                    if (hasEq) {
                        value += token;
                    } else {
                        if (multiplicity != null)
                            throw new ParseException(
                                    "Attribute cannot have two"
                                            + " multiplicities", st
                                            .getTokenIndex());
                        multiplicity = "";
                        multindex = st.getTokenIndex() + 1;
                        while (true) {
                            token = st.nextToken();
                            if ("]".equals(token))
                                break;
                            multiplicity += token;
                        }
                    }
                } else if ("{".equals(token)) {
                    String propname = "";
                    String propvalue = null;

                    if (properties == null)
                        properties = new Vector();
                    while (true) {
                        token = st.nextToken();
                        if (",".equals(token) || "}".equals(token)) {
                            if (propname.length() > 0) {
                                properties.add(propname);
                                properties.add(propvalue);
                            }
                            propname = "";
                            propvalue = null;

                            if ("}".equals(token))
                                break;
                        } else if ("=".equals(token)) {
                            if (propvalue != null)
                                throw new ParseException("Property " + propname
                                        + " cannot have two values", st
                                        .getTokenIndex());
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
                    if (value != null)
                        throw new ParseException("Attribute cannot have two "
                                + "default values", st.getTokenIndex());
                    value = "";
                    hasColon = false;
                    hasEq = true;
                } else {
                    if (hasColon) {
                        if (type != null)
                            throw new ParseException(
                                    "Attribute cannot have two" + " types", st
                                            .getTokenIndex());
                        if (token.length() > 0
                                && (token.charAt(0) == '\"' 
                                    || token.charAt(0) == '\''))
                            throw new ParseException("Type cannot be quoted",
                                    st.getTokenIndex());
                        if (token.length() > 0 && token.charAt(0) == '(')
                            throw new ParseException("Type cannot be an "
                                    + "expression", st.getTokenIndex());
                        type = token;
                    } else if (hasEq) {
                        value += token;
                    } else {
                        if (name != null && visibility != null)
                            throw new ParseException("Extra text in Attribute",
                                    st.getTokenIndex());
                        if (token.length() > 0
                                && (token.charAt(0) == '\"' 
                                    || token.charAt(0) == '\''))
                            throw new ParseException(
                                    "Name or visibility cannot" + " be quoted",
                                    st.getTokenIndex());
                        if (token.length() > 0 && token.charAt(0) == '(')
                            throw new ParseException(
                                    "Name or visibility cannot"
                                            + " be an expression", st
                                            .getTokenIndex());

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
            throw new ParseException("Unexpected end of attribute", s.length());
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
            short vis = getVisibility(visibility.trim());
            ModelFacade.setVisibility(attr, vis);
        }

        if (name != null)
            ModelFacade.setName(attr, name.trim());
        else if (ModelFacade.getName(attr) == null
                || "".equals(ModelFacade.getName(attr)))
            ModelFacade.setName(attr, "anonymous");

        if (type != null) {
            Object ow = ModelFacade.getOwner(attr);
            Object ns = null;
            if (ow != null && ModelFacade.getNamespace(ow) != null)
                ns = ModelFacade.getNamespace(ow);
            else
                ns = ModelFacade.getModel(attr);
            ModelFacade.setType(attr, getType(type.trim(), ns));
        }

        if (value != null) {
            Object initExpr = UmlFactory.getFactory().getDataTypes()
                    .createExpression(Notation.getDefaultNotation().toString(),
                            value.trim());
            ModelFacade.setInitialValue(attr, initExpr);
        }

        if (multiplicity != null) {
            try {
                ModelFacade
                        .setMultiplicity(attr, UmlFactory.getFactory()
                                .getDataTypes().createMultiplicity(
                                        multiplicity.trim()));
            } catch (IllegalArgumentException iae) {
                throw new ParseException("Bad multiplicity (" + iae + ")",
                        multindex);
            }
        }

        if (properties != null)
            setProperties(attr, properties, attributeSpecialStrings);

        if (stereotype != null) {
            stereotype = stereotype.trim();
            Object stereo = getStereotype(attr, stereotype);
            if (stereo != null)
                ModelFacade.setStereotype(attr, stereo);
            else if ("".equals(stereotype))
                ModelFacade.setStereotype(attr, null);
        }
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
    private Object getType(String name, Object defaultSpace) {
        Object type = null;
        Project p = ProjectManager.getManager().getCurrentProject();
        // Should we be getting this from the GUI? BT 11 aug 2002
        type = p.findType(name, false);
        if (type == null) { // no type defined yet
            type = UmlFactory.getFactory().getCore().buildClass(name,
                    defaultSpace);
        }
        if (ModelFacade.getModel(type) != p.getModel()
                && !ModelManagementHelper.getHelper().getAllNamespaces(
                       p.getModel()).contains(ModelFacade.getNamespace(type))) {
            type = ModelManagementHelper.getHelper().getCorrespondingElement(
                    type, ModelFacade.getModel(defaultSpace));
        }
        return type;
    }

    /**
     * Finds a visibility for the visibility specified by name. If no known
     * visibility can be deduced, private visibility is used.
     * 
     * @param name
     *            The Java name of the visibility.
     * @return A visibility corresponding to name.
     */
    private short getVisibility(String name) {
        if ("+".equals(name) || "public".equals(name))
            return ModelFacade.ACC_PUBLIC;
        else if ("#".equals(name) || "protected".equals(name))
            return ModelFacade.ACC_PROTECTED;
        else
            /* if ("-".equals(name) || "private".equals(name)) */
            return ModelFacade.ACC_PRIVATE;
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
            PropertySpecialString spec[]) {
        String name;
        String value;
        int i, j;

    nextProp: 
        for (i = 0; i + 1 < prop.size(); i += 2) {
            name = (String) prop.get(i);
            value = (String) prop.get(i + 1);

            if (name == null)
                continue;

            name = name.trim();
            if (value != null)
                value = value.trim();

            for (j = i + 2; j < prop.size(); j += 2) {
                String s = (String) prop.get(j);
                if (s != null && name.equalsIgnoreCase(s.trim()))
                    continue nextProp;
            }

            if (spec != null) {
                for (j = 0; j < spec.length; j++)
                    if (spec[j].invoke(elem, name, value))
                        continue nextProp;
            }

            ModelFacade.setTaggedValue(elem, name, value);
        }
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
    private Object recFindStereotype(Object obj, Object root, String name) {
        Object stereo;

        if (root == null)
            return null;

        if (ModelFacade.isAStereotype(root)
                && name.equals(ModelFacade.getName(root))) {
            if (ExtensionMechanismsHelper.getHelper().isValidStereoType(obj,
            /* (MStereotype) */root))
                return root;
            else
                LOG
                        .debug("Missed stereotype "
                                + ModelFacade.getBaseClass(root));
        }

        if (!ModelFacade.isANamespace(root))
            return null;

        Collection ownedElements = ModelFacade.getOwnedElements(root);

        if (ownedElements == null) {
            return null;
        }

        // Loop through each element in the namespace, recursing.

        Iterator iter = ownedElements.iterator();

        while (iter.hasNext()) {
            stereo = recFindStereotype(obj, iter.next(), name);
            if (stereo != null)
                return stereo;
        }
        return null;
    }

    /**
     * Finds a stereotype named name either in the subtree of the model rooted
     * at root, or in the the ProfileJava model.
     * 
     * <p>
     * TODO: Should create the stereotype under root if it isn't found.
     * 
     * @param obj
     *            A MModelElements to find a suitable stereotype for.
     * @param name
     *            The name of the stereotype to search for.
     * @return A stereotype named name, or possibly null.
     */
    private Object getStereotype(Object obj, String name) {
        Object root = ModelFacade.getModel(obj);
        Object stereo;

        stereo = recFindStereotype(obj, root, name);
        if (stereo != null)
            return stereo;

        stereo = recFindStereotype(obj, ProfileJava.getInstance()
                .getProfileModel(), name);

        if (stereo != null)
            return ModelManagementHelper.getHelper().getCorrespondingElement(
                    stereo, root);

        if (root != null && name.length() > 0) {
            stereo = ExtensionMechanismsFactory.getFactory().buildStereotype(
                    obj, name, root);
        }

        return stereo;
    }




    /**
     * Parse user input for state bodies and assign the individual lines to
     * according actions or transistions. The user input consists of multiple
     * lines like: action-label / action-expression or the format of a regular
     * transition - see parseTransition(). "action-label" stands for one of
     * "entry", "do" and "exit". The words "entry", "do" and "exit" are
     * case-independent.
     * 
     * @param st
     *            The State object.
     * @param s
     *            The string to parse.
     */
    public void parseStateBody(Object st, String s) {
        boolean foundEntry = false;
        boolean foundExit = false;
        boolean foundDo = false;

        Collection transitionList = new ArrayList();
        java.util.StringTokenizer lines = new java.util.StringTokenizer(s,
                "\n\r");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.toLowerCase().startsWith("entry")) {
                parseStateEntryAction(st, line);
                foundEntry = true;
            } else if (line.toLowerCase().startsWith("exit")) {
                parseStateExitAction(st, line);
                foundExit = true;
            } else if (line.toLowerCase().startsWith("do")) {
                parseStateDoAction(st, line);
                foundDo = true;
            } else {
                Object t = UmlFactory.getFactory().getStateMachines()
                        .createTransition();
                //.buildInternalTransition(st);
                /*
                 * TODO: Why does buildInternalTransition() not work? (MVW) If I
                 * try, then the Target and Source get reset to null before they
                 * are displayed...
                 */
                if (t == null)
                    continue;
                ModelFacade.setTarget(t, st);
                ModelFacade.setSource(t, st);
                parseTransition(t, line);
                //LOG.debug("just parsed:" + GeneratorDisplay.Generate(t));
                transitionList.add(t);
            }
        }

        if (!foundEntry) {
            delete(ModelFacade.getEntry(st));
        }
        if (!foundExit) {
            delete(ModelFacade.getExit(st));
        }
        if (!foundDo) {
            delete(ModelFacade.getDoActivity(st));
        }

        Vector internals = new Vector(ModelFacade.getInternalTransitions(st));
        Vector oldinternals = new Vector(ModelFacade
                .getInternalTransitions(st));
        internals.removeAll(oldinternals); //now the vector is empty

        // don't forget to remove old internals!
        for (int i = 0; i < oldinternals.size(); i++)
            UmlFactory.getFactory().delete(
                    /* (MTransition) */oldinternals.elementAt(i));
        internals.addAll(transitionList);
        ModelFacade.setInternalTransitions(st, transitionList);
    }

    /**
     * Parse a line of the form: "entry /action" and create an action. The word
     * "entry" is case-independent.
     * 
     * @param st
     *            the state object
     * @param s
     *            the string to be parsed
     */
    public void parseStateEntryAction(Object st, String s) {
        if (s.toLowerCase().startsWith("entry") && s.indexOf("/") > -1)
            s = s.substring(s.indexOf("/") + 1).trim();
        Object oldEntry = ModelFacade.getEntry(st);
        if (oldEntry == null) {
            ModelFacade.setEntry(st, buildNewCallAction(s));
        } else {
            updateAction(oldEntry, s);
        }
    }

    /**
     * Parse a line of the form: "exit /action" and create an action. The word
     * "exit" is case-independent.
     * 
     * @param st
     *            the state object
     * @param s
     *            the string to be parsed
     */
    public void parseStateExitAction(Object st, String s) {
        if (s.toLowerCase().startsWith("exit") && s.indexOf("/") > -1)
            s = s.substring(s.indexOf("/") + 1).trim();
        Object oldExit = ModelFacade.getExit(st);
        if (oldExit == null) {
            ModelFacade.setExit(st, buildNewCallAction(s));
        } else {
            updateAction(oldExit, s);
        }
    }

    /**
     * Parse a line of the form: "do /action" and create an action. The word
     * "do" is case-independent.
     * 
     * @param st
     *            the state object
     * @param s
     *            the string to be parsed
     */
    public void parseStateDoAction(Object st, String s) {
        if (s.toLowerCase().startsWith("do") && s.indexOf("/") > -1)
            s = s.substring(s.indexOf("/") + 1).trim();
        Object oldDo = ModelFacade.getDoActivity(st);
        if (oldDo == null) {
            ModelFacade.setDoActivity(st, buildNewCallAction(s));
        } else {
            updateAction(oldDo, s);
        }
    }

    /**
     * Parse a transition description line of the form: "event-signature
     * [guard-condition] / action-expression" If the last character of this line
     * is a ";", then it is ignored. The "event-signature" may be one of the 4
     * formats: 
     *      ChangeEvent: "when(condition)" 
     *      TimeEvent: "after(duration)"
     *      CallEvent: "a(parameter-list)". 
     *      SignalEvent: TODO: Add support for signal events.
     * 
     * @param trans
     *            The transition object to which this string applies.
     * @param s
     *            The string to be parsed.
     * @return the transition object
     * 
     * @see org.argouml.uml.generator.Parser#parseTransition(Object, String)
     */
    public Object parseTransition(Object trans, String s) {
        s = s.trim();
        StringTokenizer tokenizer = new StringTokenizer(s, "[/");
        String eventSignature = null;
        String guardCondition = null;
        String actionExpression = null;
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            if (nextToken.endsWith("]")) {
                guardCondition = nextToken.substring(0, nextToken.length() - 1);
            } else {
                if (s.startsWith(nextToken)) {
                    eventSignature = nextToken;
                }
                else
                    if (s.endsWith(nextToken)) {
                        actionExpression = nextToken;
                    }
            }
        }
           
        if (eventSignature != null) {
            try {
                parseEventSignature(trans, eventSignature);
            } catch (ParseException e) {
                LOG.warn(e);
            }
        }
      
        if (guardCondition != null) {
            Object guard = parseGuard(guardCondition.substring(guardCondition
                    .indexOf('[') + 1));
            ModelFacade.setGuard(trans, guard);
        }
       
        if (actionExpression != null) {
            Object action = parseAction(actionExpression.trim());
            ModelFacade.setEffect(trans, action);
        }
        return trans;

        
        // //DO NOT DELETE!
        // //MVW: I still need this to help me create 
        // //the functionality that is now missing!
        //
        //	// strip any trailing semi-colons
        //	s = s.trim();
        //	while (s.length() > 0 && s.charAt(s.length() - 1) == ';')
        //	    s = s.substring(0, s.length() - 1).trim();
        //
        //        // strip off the name, and the ":"
        //	String name = "";
        //	String trigger = "";
        //	String guard = "";
        //	String actions = "";
        //	if (s.indexOf(":", 0) > -1) {
        //	    String s1 = s.substring(0, s.indexOf(":"));
        //	    // the name may not contain a "(", for the case of: a(b:c)
        //	    // the name may not contain a "/", 
        //          // for when the action contains a ":"
        //	    if ((s1.indexOf("(") < 0) && (s1.indexOf("/") < 0)) {
        //	        name = s1.trim();
        //	        s = s.substring(s.indexOf(":") + 1).trim();
        //	    }
        //        }
        //
        //        // get the guard from between the []
        //	if (s.indexOf("[", 0) > -1 && s.indexOf("]", 0) > -1) {
        //	    guard = s.substring(s.indexOf("[", 0) + 1, 
        //              s.indexOf("]")).trim();
        //	    s = s.substring(0, s.indexOf("[")) 
        //                  + s.substring(s.indexOf("]") + 1);
        //	    s = s.trim();
        //	}
        //
        //        // everything behind the "/" is the action
        //	if (s.indexOf("/", 0) > -1) {
        //	    actions = s.substring(s.indexOf("/") + 1).trim();
        //	    s = s.substring(0, s.indexOf("/")).trim();
        //	}
        //
        //        // and the remainder is the trigger name
        //	trigger = s;
        //
        //	// let's look for a TimeEvent, ChangeEvent, 
        //      // CallEvent or SignalEvent
        //	boolean timeEvent = false;
        //	boolean changeEvent = false;
        //	boolean callEvent = false;
        //	boolean signalEvent = false;
        //	String operationName = "";
        //	if ((s.toLowerCase().startsWith("after"))
        //	    && (s.indexOf("(", 0) > -1)
        //	    && (s.indexOf(")", 0) > -1))
        //	{ //s shall contain the TimeExpression
        //	    s = s.substring(s.indexOf("(") + 1).trim();
        //	    s = s.substring(0, s.lastIndexOf(")")).trim();
        //	    timeEvent = true;
        //	} else if ((s.toLowerCase().startsWith("when"))
        //		   && (s.indexOf("(", 0) > -1)
        //		   && (s.indexOf(")", 0) > -1))
        //	{ // s shall contain the ChangeExpression
        //	    s = s.substring(s.indexOf("(") + 1).trim();
        //	    s = s.substring(0, s.lastIndexOf(")")).trim();
        //	    changeEvent = true;
        //	} else if ((s.indexOf("(", 0) > -1)
        //            && (s.indexOf(")", 0) > -1))
        //        { // s shall contain the operation
        //	    operationName = s.substring(0, s.indexOf("(")).trim();
        //	    callEvent = true;
        //        } else { // s shall contain the signal
        //            signalEvent = true;
        //        }
        //
        //	LOG.debug("name=|" + name + "|");
        //	LOG.debug("trigger=|" + trigger + "|");
        //	LOG.debug("guard=|" + guard + "|");
        //	LOG.debug("actions=|" + actions + "|");
        //
        //        // use the name we found to (re)name the transition
        //	ModelFacade.setName(trans, name);
        //
        //// /* The following handles the Event that is the trigger of
        //// this transition.
        //// We can distinct between 4 cases:
        //// 1. A trigger is given. None exists yet.
        //// 2. The name of the trigger was present, but is (the same or)
        // altered.
        //// 3. A trigger is not given. None exists yet.
        //// 4. The name of the trigger was present, but is removed.
        //// The reaction in these cases should be:
        //// 1. Create a new trigger, name it, and hook it to the transition.
        //// 2. Rename the trigger.
        //// 3. Nop.
        //// 4. Unhook and erase the existing trigger.
        ////
        //// TODO:
        //// In fact it could be made even more complicated for case 1:
        //// If the transition did not have a trigger before,
        //// a trigger-name is given, and a trigger already
        //// existed with that name,
        //// then we have to use the existing trigger object!
        //// */
        //        Object evt = ModelFacade.getTrigger(trans);
        //        boolean created_evt = false;
        //	if (trigger.length() > 0) {
        //            // case 1 and 2
        //            if (evt == null) {
        //                // case 1
        //                if (timeEvent) { // after(...)
        //                    evt = UmlFactory.getFactory().getStateMachines()
        //                                                .buildTimeEvent(s);
        //                }
        //                if (changeEvent) { // when(...)
        //                    evt = UmlFactory.getFactory().getStateMachines()
        //                                                .buildChangeEvent(s);
        //                }
        //                if (callEvent) { // operation(paramlist)
        //                    evt = UmlFactory.getFactory().getStateMachines()
        //			            .buildCallEvent(trans, trigger);
        //                }
        //                if (signalEvent) { // signalname
        //                    evt = UmlFactory.getFactory().getStateMachines()
        //                                   .buildSignalEvent(trigger);
        //                }
        //                created_evt = true;
        //            } else {
        //                // case 2
        //                if (!ModelFacade.getName(evt).equals(trigger)) {
        //                    ModelFacade.setName(evt, trigger);
        //                    if (timeEvent && !ModelFacade.isATimeEvent(evt)) {
        //                        UmlFactory.getFactory().delete(evt);
        //                        evt = UmlFactory.getFactory()
        //                            .getStateMachines().buildTimeEvent(s);
        //                        created_evt = true;
        //                    }
        //                    if (changeEvent && !ModelFacade
        //                        .isAChangeEvent(evt)) {
        //                        UmlFactory.getFactory().delete(evt);
        //                        evt = UmlFactory.getFactory()
        //                            .getStateMachines().buildChangeEvent(s);
        //                        created_evt = true;
        //                    }
        //                    if (callEvent && !ModelFacade.isACallEvent(evt)) {
        //                        UmlFactory.getFactory().delete(evt);
        //                        evt = UmlFactory.getFactory()
        //                            .getStateMachines()
        //                            .buildCallEvent(trans, trigger);
        //                        created_evt = true;
        //                    }
        //                    if (signalEvent && !ModelFacade
        //                        .isASignalEvent(evt)) {
        //                        UmlFactory.getFactory().delete(evt);
        //                        evt = UmlFactory.getFactory()
        //                            .getStateMachines()
        //                            .buildSignalEvent(trigger);
        //                        created_evt = true;
        //                    }
        //                }
        //            }
        //            if (created_evt && (evt != null)) {
        //                StateMachinesHelper.getHelper()
        //                    .setEventAsTrigger(trans, evt);
        //                
        //                /* The next part is explained by the following
        //                 * quote from the UML spec:
        //                 * "The event declaration has scope within
        //                 * the package it appears in and may be used in
        //                 * state diagrams for classes that have visibility
        //                 * inside the package. An event is not local to
        //                 * a single class."
        //                 */
        //                Object enclosing = StateMachinesHelper.getHelper()
        //                                            .getStateMachine(trans);
        //                while ((!ModelFacade.isAPackage(enclosing))
        //		       && (enclosing != null)) {
        //                    enclosing = ModelFacade.getNamespace(enclosing);
        //		}
        //                if (enclosing != null) {
        //		    ModelFacade.setNamespace(evt, enclosing);
        //		}
        //            }
        //	} else {
        //            // case 3 and 4
        //            if (evt == null) {
        //                ;// case 3
        //	    } else {
        //                // case 4
        //                UmlFactory.getFactory().delete(evt); // erase it
        //            }
        //        }
        //
        //	/* handle the Guard
        //	We can distinct between 4 cases:
        //        1. A guard is given. None exists yet.
        //        2. The expression of the guard was present, but is altered.
        //        3. A guard is not given. None exists yet.
        //        4. The expression of the guard was present, but is removed.
        //        The reaction in these cases should be:
        //        1. Create a new guard, set its name, language & expression,
        //	   and hook it to the transition.
        //        2. Change the guard's expression. Leave the name & language
        // untouched.
        //        3. Nop.
        //        4. Unhook and erase the existing guard.
        //        */
        //	Object g = ModelFacade.getGuard(trans);
        //	if (guard.length() > 0) {
        //	    if (g == null) {
        //                // case 1
        //	        /*TODO: In the next line, I should use buildGuard(),
        //	         * but it doesn't show the guard on the diagram...
        //	         * Why? (MVW)*/
        //		g = UmlFactory.getFactory().getStateMachines()
        //                  .createGuard();
        //		if (g != null) {
        //		    ModelFacade.setExpression(g, UmlFactory.getFactory()
        //		    	.getDataTypes().createBooleanExpression(
        //                                                   "Java", guard));
        //		    ModelFacade.setName(g, "anon");
        //		    ModelFacade.setTransition(g, trans);
        //		    ModelFacade.setGuard(trans, g);
        //		}
        //	    } else {
        //                // case 2
        //		
        //		/* TODO: This does not work! Why not? (MVW)
        //	        Object expr = ModelFacade.getExpression(g);
        //		ModelFacade.setBody(expr,guard);
        //		ModelFacade.setExpression(g,expr); */
        //	        
        //	        //hence a less elegant workaround that works:
        //	        String language = ModelFacade.getLanguage(
        //                  ModelFacade.getExpression(g));
        //	        ModelFacade.setExpression(g, UmlFactory.getFactory()
        //                    .getDataTypes().createBooleanExpression(
        //                                              language, guard));
        //                /* TODO: In this case, the properties panel 
        //                  is not updated
        //		    with the changed expression! */
        //	    }
        //	} else {
        //	    if (g == null) {
        //                ;// case 3
        //	    } else {
        //                // case 4
        //		UmlFactory.getFactory().delete(g); // erase it
        //	    }
        //	}
        //
        //        /* handle the Effect (Action)
        //        We can distinct between 4 cases:
        //        1. An effect is given. None exists yet.
        //        2. The expression of the effect was present, but is altered.
        //        3. An effct is not given. None exists yet.
        //        4. The expression of the effect was present, but is removed.
        //        The reaction in these cases should be:
        //        1. Create a new CallAction, set its name, language & 
        //        expression, and hook it to the transition.
        //        2. Change the effect's expression. Leave the actiontype, name 
        //	  & language untouched.
        //        3. Nop.
        //        4. Unhook and erase the existing effect.
        //        */
        //	Object effect = ModelFacade.getEffect(trans);
        //	if (actions.length() > 0) {
        //	    if (effect == null) { // case 1
        //	        effect = UmlFactory.getFactory().getCommonBehavior()
        //		    .createCallAction();
        //	        ModelFacade.setScript(effect, UmlFactory.getFactory()
        //		    .getDataTypes().createActionExpression(
        //                                                   "Java", actions));
        //	        ModelFacade.setName(effect, "anon");
        //	        ModelFacade.setEffect(trans, effect);
        //	    } else { // case 2
        //                String language = ModelFacade.getLanguage(ModelFacade
        //		    .getScript(effect));
        //                ModelFacade.setScript(effect, UmlFactory.getFactory()
        //		    .getDataTypes().createActionExpression(
        //                                            language, actions));
        //	    }
        //	} else { // case 3 & 4
        //	    if (effect == null) {
        //                ;// case 3
        //            } else {
        //                // case 4
        //                UmlFactory.getFactory().delete(effect); // erase it
        //            }
        //	}
        //
        //	return trans;
    }

    /**
     * Parses a line on the form:
     * 
     * <br>
     * baselist := [base] [, base]* <br>
     * classifierRole := [name] [/ role] [: baselist]
     * 
     * <p>
     * <b>role </b> and <b>baselist </b> can be given in any order.
     * 
     * <p>
     * This syntax is compatible with the UML 1.3 specification.
     * 
     * (formerly: "name: base" )
     * 
     * @param cls
     *            The classifier role to apply any changes to.
     * @param s
     *            The String to parse.
     * @throws java.text.ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
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
                    ;// Do nothing
                } else if ("/".equals(token)) {
                    hasSlash = true;
                    hasColon = false;

                    if (base != null) {
                        if (bases == null)
                            bases = new Vector();
                        bases.add(base);
                    }
                    base = null;
                } else if (":".equals(token)) {
                    hasColon = true;
                    hasSlash = false;

                    if (bases == null)
                        bases = new Vector();
                    if (base != null)
                        bases.add(base);
                    base = null;
                } else if (",".equals(token)) {
                    if (base != null) {
                        if (bases == null)
                            bases = new Vector();
                        bases.add(base);
                    }
                    base = null;
                } else if (hasColon) {
                    if (base != null)
                        throw new ParseException(
                                "Extra text in Classifier Role", st
                                        .getTokenIndex());

                    base = token;
                } else if (hasSlash) {
                    if (role != null)
                        throw new ParseException(
                                "Extra text in Classifier Role", st
                                        .getTokenIndex());

                    role = token;
                } else {
                    if (name != null)
                        throw new ParseException(
                                "Extra text in Classifier Role", st
                                        .getTokenIndex());

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException("Unexpected end of attribute", s.length());
        } catch (ParseException pre) {
            LOG.error("parseexception", pre);

            throw pre;
        }

        if (base != null) {
            if (bases == null)
                bases = new Vector();
            bases.add(base);
        }

        // TODO: What to do about object name???
        //    if (name != null)
        //	;

        if (role != null)
            ModelFacade.setName(cls, role.trim());

        if (bases != null) {
            // Remove bases that aren't there anymore
            Collection b = ModelFacade.getBases(cls);
            Iterator it = b.iterator();
            Object c;
            Object ns = ModelFacade.getNamespace(cls);
            if (ns != null && ModelFacade.getNamespace(ns) != null)
                ns = ModelFacade.getNamespace(ns);
            else
                ns = ModelFacade.getModel(cls);

            while (it.hasNext()) {
                c = it.next();
                if (!bases.contains(ModelFacade.getName(c)))
                    ModelFacade.removeBase(cls, c);
            }

            it = bases.iterator();
        addBases: 
            while (it.hasNext()) {
                String d = ((String) it.next()).trim();

                Iterator it2 = b.iterator();
                while (it2.hasNext()) {
                    c = it2.next();
                    if (d.equals(ModelFacade.getName(c)))
                        continue addBases;
                }
                c = getType(d, ns);
                if (ModelFacade.isACollaboration(ModelFacade.getNamespace(c)))
                    ModelFacade.setNamespace(c, ns);
                ModelFacade.addBase(cls, c);
            }
        }
    }

    /**
     * TODO: - This method is too complex, lets break it up. Parses a message
     * line on the form:
     * 
     * <br>
     * intno := integer|name <br>
     * seq := intno ['.' intno]* <br>
     * recurrance := '*'['//'] | '*'['//']'[' <i>iteration </i>']' | '['
     * <i>condition </i>']' <br>
     * seqelem := {[intno] ['['recurrance']']} <br>
     * seq2 := seqelem ['.' seqelem]* <br>
     * ret_list := lvalue [',' lvalue]* <br>
     * arg_list := rvalue [',' rvalue]* <br>
     * message := [seq [',' seq]* '/'] seq2 ':' [ret_list :=] name ([arg_list])
     * 
     * <p>
     * Which is rather complex, so a few examples: <br>
     * 2: display(x, y)<br>
     * 1.3.1: p := find(specs) <br>
     * [x < 0] 4: invert(color) <br>
     * A3, B4/ C3.1*: update()
     * 
     * <p>
     * This syntax is compatible with the UML 1.3 specification.
     * 
     * <p>
     * Actually, only a subset of this syntax is currently supported, and some
     * is not even planned to be supported. The exceptions are intno, which
     * allows a number possibly followed by a sequence of letters in the range
     * 'a' - 'z', seqelem, which does not allow a recurrance, and message, which
     * does allow one recurrance near seq2. (formerly: name: action )
     * 
     * @param mes
     *            The MMessage to apply any changes to.
     * @param s
     *            The String to parse.
     * @throws ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
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
                    if (mustBePre)
                        throw new ParseException("Predecessors cannot be "
                                + "qualified", st.getTokenIndex());
                    mustBeSeq = true;

                    if (guard != null)
                        throw new ParseException("Messages cannot have several"
                                + " guard or iteration specifications", st
                                .getTokenIndex());

                    guard = "";
                    while (true) {
                        token = st.nextToken();
                        if ("]".equals(token))
                            break;
                        guard += token;
                    }
                } else if ("*".equals(token)) {
                    if (mustBePre)
                        throw new ParseException("Predecessors cannot be "
                                + "iterated", st.getTokenIndex());
                    mustBeSeq = true;

                    if (currentseq != null)
                        iterative = true;
                } else if (".".equals(token)) {
                    if (currentseq == null)
                        throw new ParseException("Unexpected dot ('.')", st
                                .getTokenIndex());
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

                    if (mustBePre)
                        throw new ParseException("Predecessors must be "
                                + "terminated with \'/\' and not with \':\'",
                                st.getTokenIndex());

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
                    if (mustBePre)
                        throw new ParseException("Predecessors cannot be "
                                + "parallellized", st.getTokenIndex());
                    mustBeSeq = true;

                    if (currentseq != null)
                        parallell = true;
                } else if (",".equals(token)) {
                    if (currentseq != null) {
                        if (mustBeSeq)
                            throw new ParseException("Messages cannot have "
                                    + "many sequence numbers", st
                                    .getTokenIndex());
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
                        if (token.charAt(token.length() - 1) != ')')
                            throw new ParseException("Malformed parameters", st
                                    .getTokenIndex());
                        if (fname == null || "".equals(fname))
                            throw new ParseException("Must be a function name "
                                    + "before the parameters", st
                                    .getTokenIndex());
                        if (varname == null)
                            varname = "";
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
                    if (args.size() == 0)
                        args.add(null);
                    args.add(null);
                } else {
                    if (args.size() == 0) {
                        if (token.trim().length() == 0)
                            continue;
                        args.add(null);
                    }
                    String arg = (String) args.get(args.size() - 1);
                    if (arg != null)
                        arg = arg + token;
                    else
                        arg = token;
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
                if (i > 0)
                    buf.append(", ");
                buf.append(seqno.get(i) + " (" + seqno.get(i + 1) + ")");
            }
            buf.append("\n");
            buf.append("predecessors: " + predecessors.size() + "\n");
            for (i = 0; i < predecessors.size(); i++) {
                int j;
                Vector v = (Vector) predecessors.get(i);
                buf.append("    Predecessor: ");
                for (j = 0; v != null && j + 1 < v.size(); j += 2) {
                    if (j > 0)
                        buf.append(", ");
                    buf.append(v.get(j) + " (" + v.get(j + 1) + ")");
                }
            }
            buf.append("guard: " + guard + " it: " + iterative + " pl: "
                    + parallell + "\n");
            buf.append(varname + " := " + fname + " ( " + paramExpr + " )"
                    + "\n");
            LOG.debug(buf);
        }

        if (ModelFacade.getAction(mes) == null) {
            Object a = UmlFactory.getFactory().getCommonBehavior()
                    .createCallAction();
            ModelFacade.addOwnedElement(ModelFacade.getContext(ModelFacade
                    .getInteraction(mes)), a);
            ModelFacade.setAction(mes, a);
        }

        if (guard != null) {
            guard = "[" + guard.trim() + "]";
            if (iterative) {
                if (parallell)
                    guard = "*//" + guard;
                else
                    guard = "*" + guard;
            }
            Object/* MIterationExpression */expr = UmlFactory.getFactory()
                    .getDataTypes().createIterationExpression(
                            Notation.getDefaultNotation().toString(), guard);
            ModelFacade.setRecurrence(ModelFacade.getAction(mes), expr);
        }

        if (fname == null) {
            if (!mayDeleteExpr
                    && ModelFacade.getScript(ModelFacade.getAction(mes)) 
                                            != null) {
                String body = (String) ModelFacade.getBody(ModelFacade
                        .getScript(ModelFacade.getAction(mes)));

                int idx = body.indexOf(":=");
                if (idx >= 0)
                    idx++;
                else
                    idx = body.indexOf("=");

                if (idx >= 0)
                    fname = body.substring(idx + 1);
                else
                    fname = body;
            } else
                fname = "";
        }

        if (varname == null) {
            if (!mayDeleteExpr
                    && ModelFacade.getScript(ModelFacade.getAction(mes)) 
                                            != null) {
                String body = (String) ModelFacade.getBody(ModelFacade
                        .getScript(ModelFacade.getAction(mes)));
                int idx = body.indexOf(":=");
                if (idx < 0)
                    idx = body.indexOf("=");

                if (idx >= 0)
                    varname = body.substring(0, idx);
                else
                    varname = "";
            } else
                varname = "";
        }

        if (fname != null) {
            String expr = fname.trim();
            if (varname != null && varname.length() > 0)
                expr = varname.trim() + " := " + expr;

            if (ModelFacade.getScript(ModelFacade.getAction(mes)) == null
                    || !expr.equals(ModelFacade.getBody(ModelFacade
                            .getScript(ModelFacade.getAction(mes))))) {
                Object e = UmlFactory.getFactory().getDataTypes()
                        .createActionExpression(
                                Notation.getDefaultNotation().toString(),
                                expr.trim());
                ModelFacade.setScript(ModelFacade.getAction(mes), e);
                refindOperation = true;
            }
        }

        if (args != null) {
            Collection c = ModelFacade.getActualArguments(ModelFacade
                    .getAction(mes));
            Iterator it = c.iterator();
            for (i = 0; i < args.size(); i++) {
                Object arg = (it.hasNext() ? /* (MArgument) */it.next() : null);
                if (arg == null) {
                    arg = UmlFactory.getFactory().getCommonBehavior()
                            .createArgument();
                    ModelFacade.addActualArgument(ModelFacade.getAction(mes),
                            arg);
                    refindOperation = true;
                }
                if (ModelFacade.getValue(arg) == null
                        || !args.get(i).equals(
                              ModelFacade.getBody(ModelFacade.getValue(arg)))) {
                    String value = (args.get(i) != null ? (String) args.get(i)
                            : "");
                    Object e = UmlFactory.getFactory().getDataTypes()
                            .createExpression(
                                    Notation.getDefaultNotation().toString(),
                                    value.trim());
                    ModelFacade.setValue(arg, e);
                }
            }

            while (it.hasNext()) {
                ModelFacade.removeActualArgument(ModelFacade.getAction(mes),
                /* (MArgument) */it.next());
                refindOperation = true;
            }
        }

        if (seqno != null) {
            Object/* MMessage */root;
            // Find the preceding message, if any, on either end of the
            // association.
            String pname = "";
            String mname = "";
            String gname = GeneratorDisplay.getInstance()
                    .generateMessageNumber(mes);
            boolean swapRoles = false;
            int majval = (seqno.get(seqno.size() - 2) != null ? Math.max(
                    ((Integer) seqno.get(seqno.size() - 2)).intValue() - 1, 0)
                    : 0);
            int minval = (seqno.get(seqno.size() - 1) != null ? Math.max(
                    ((Integer) seqno.get(seqno.size() - 1)).intValue(), 0) : 0);

            for (i = 0; i + 1 < seqno.size(); i += 2) {
                int bv = (seqno.get(i) != null ? Math.max(((Integer) seqno
                        .get(i)).intValue(), 1) : 1);
                int sv = (seqno.get(i + 1) != null ? Math.max(((Integer) seqno
                        .get(i + 1)).intValue(), 0) : 0);

                if (i > 0)
                    mname += ".";
                mname += Integer.toString(bv) + (char) ('a' + sv);

                if (i + 3 < seqno.size()) {
                    if (i > 0)
                        pname += ".";
                    pname += Integer.toString(bv) + (char) ('a' + sv);
                }
            }

            root = null;
            if (pname.length() > 0) {
                root = findMsg(ModelFacade.getSender(mes), pname);
                if (root == null) {
                    root = findMsg(ModelFacade.getReceiver(mes), pname);
                    if (root != null) {
                        swapRoles = true;
                    }
                }
            } else if (!hasMsgWithActivator(ModelFacade.getSender(mes), null)
                    && hasMsgWithActivator(ModelFacade.getReceiver(mes), null))
                swapRoles = true;

            if (compareMsgNumbers(mname, gname)) {
                ;// Do nothing
            } else if (isMsgNumberStartOf(gname, mname)) {
                throw new ParseException("Cannot move a message into the "
                        + "subtree rooted at self", 0);
            } else if (ModelFacade.getPredecessors(mes).size() > 1
                    && ModelFacade.getMessages3(mes).size() > 1) {
                throw new ParseException("Cannot move a message which is "
                        + "both start and end of several threads", 0);
            } else if (root == null && pname.length() > 0) {
                throw new ParseException("Cannot find the activator for the "
                        + "message", 0);
            } else if (swapRoles
                    && ModelFacade.getMessages4(mes).size() > 0
                    && (ModelFacade.getSender(mes) != ModelFacade
                            .getReceiver(mes))) {
                throw new ParseException("Cannot reverse the direction of a "
                        + "message that is an activator", 0);
            } else {
                // Disconnect the message from the call graph
                Collection c = ModelFacade.getPredecessors(mes);
                Collection c2 = ModelFacade.getMessages3(mes);
                Iterator it;

                it = c2.iterator();
                while (it.hasNext()) {
                    ModelFacade.removeMessage3(mes, /* (MMessage) */it.next());
                }

                it = c.iterator();
                while (it.hasNext()) {
                    Iterator it2 = c2.iterator();
                    Object pre = /* (MMessage) */it.next();
                    ModelFacade.removePredecessor(mes, pre);
                    while (it2.hasNext()) {
                        ModelFacade.addPredecessor(it2.next(), pre);
                    }
                }

                // Connect the message at a new spot
                ModelFacade.setActivator(mes, root);
                if (swapRoles) {
                    Object/* MClassifierRole */r = ModelFacade.getSender(mes);
                    ModelFacade.setSender(mes, ModelFacade.getReceiver(mes));
                    ModelFacade.setReceiver(mes, r);
                }

                if (root == null) {
                    c = filterWithActivator(ModelFacade
                            .getMessages2(ModelFacade.getSender(mes)), null);
                } else {
                    c = ModelFacade.getMessages4(root);
                }
                c2 = findCandidateRoots(c, root, mes);
                it = c2.iterator();
                // If c2 is empty, then we're done (or there is a
                // cycle in the message graph, which would be bad) If
                // c2 has more than one element, then the model is
                // crappy, but we'll just use one of them anyway
                if (majval <= 0) {
                    while (it.hasNext())
                        ModelFacade.addMessage3(mes, /* (MMessage) */it.next());
                } else if (it.hasNext()) {
                    Object/* MMessage */pre = walk(/* (MMessage) */it.next(),
                            majval - 1, false);
                    Object/* MMessage */post = successor(pre, minval);
                    if (post != null) {
                        ModelFacade.removePredecessor(post, pre);
                        ModelFacade.addPredecessor(post, mes);
                    }
                    insertSuccessor(pre, mes, minval);
                }
                refindOperation = true;
            }
        }

        if (fname != null && refindOperation) {
            Object role = ModelFacade.getReceiver(mes);
            Vector ops = getOperation(ModelFacade.getBases(role), fname.trim(),
                    ModelFacade.getActualArguments(ModelFacade.getAction(mes))
                            .size());

            // TODO: Should someone choose one, if there are more
            // than one?
            if (ModelFacade.isACallAction(ModelFacade.getAction(mes))) {
                Object a = /* (MCallAction) */ModelFacade.getAction(mes);
                if (ops.size() > 0)
                    ModelFacade.setOperation(a, /* (MOperation) */ops.get(0));
                else
                    ModelFacade.setOperation(a, null);
            }
        }

        // TODO: Predecessors is not implemented, because it
        // causes some problems that I've not found an easy way to handle yet,
        // d00mst. The specific problem is that the notation currently is
        // ambiguous on second message after a thread split.

        // Why not implement it anyway? d00mst

        if (hasPredecessors) {
            Collection roots = findCandidateRoots(ModelFacade
                    .getMessages(ModelFacade.getInteraction(mes)), null, null);
            Vector pre = new Vector();
            Iterator it;
        predfor: 
            for (i = 0; i < predecessors.size(); i++) {
                it = roots.iterator();
                while (it.hasNext()) {
                    Object/* MMessage */msg = walkTree(/*(MMessage)*/it.next(),
                            (Vector) predecessors.get(i));
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
            if (ptr.message != null && !pre.contains(ptr.message))
                pre.add(ptr.message);
            ModelFacade.setPredecessors(mes, pre);
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
            if (ans == chld)
                return true;
            if (isPredecessorMsg(ans, chld, 100))
                return true;
            chld = ModelFacade.getActivator(chld);
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
        Iterator it = ModelFacade.getPredecessors(suc).iterator();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (m == pre)
                return true;
            if (md > 0 && isPredecessorMsg(pre, m, md - 1))
                return true;
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
     * @return The message at the end of path, or <tt>null</tt>.
     */
    private Object walkTree(Object root, Vector path) {
        int i;
        for (i = 0; i + 1 < path.size(); i += 2) {
            int bv = (path.get(i) != null ? Math.max(((Integer) path.get(i))
                    .intValue() - 1, 0) : 0);
            int sv = (path.get(i + 1) != null ? Math.max(((Integer) path
                    .get(i + 1)).intValue(), 0) : 0);
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
                Iterator it = findCandidateRoots(
                        ModelFacade.getMessages4(root), root, null).iterator();
                // Things are strange if there are more than one candidate root,
                // it has no obvious interpretation in terms of a call tree.
                if (!it.hasNext())
                    return null;
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
        Iterator it = ModelFacade.getMessages2(r).iterator();
        while (it.hasNext()) {
            if (ModelFacade.getActivator(it.next()) == m) {
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
        Vector v = new Vector(ModelFacade.getMessages3(m));
        if (v.size() > p)
            v.insertElementAt(s, p);
        else
            v.add(s);
        ModelFacade.setMessages3(m, v);
    }

    /**
     * Finds the steps'th successor of message r in the sense that it is a
     * direct successor of r. Returns null if r has fewer successors.
     */
    private Object/* MMessage */successor(Object/* MMessage */r, int steps) {
        Iterator it = ModelFacade.getMessages3(r).iterator();
        while (it.hasNext() && steps > 0) {
            it.next();
            steps--;
        }
        if (it.hasNext())
            return /* (MMessage) */it.next();
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
        Object/* MMessage */act = ModelFacade.getActivator(r);
        while (steps > 0) {
            Iterator it = ModelFacade.getMessages3(r).iterator();
            do {
                if (!it.hasNext())
                    return (strict ? null : r);
                r = /* (MMessage) */it.next();
            } while (ModelFacade.getActivator(r) != act);
            steps--;
        }
        return r;
    }

    /**
     * Finds the root candidates in a collection c, ie the messages in c that
     * has the activator a (may be null) and has no predecessor with the same
     * activator. If veto isn't null, then the message in veto will not be
     * included in the Collection of candidates.
     */
    private Collection findCandidateRoots(Collection c, Object/* MMessage */a,
            Object/* MMessage */veto) {
        Iterator it = c.iterator();
        Vector v = new Vector();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (m == veto)
                continue;
            if (ModelFacade.getActivator(m) != a)
                continue;
            Iterator it2 = ModelFacade.getPredecessors(m).iterator();
            boolean candidate = true;
            while (it2.hasNext()) {
                Object m2 = /* (MMessage) */it2.next();
                if (ModelFacade.getActivator(m2) == a)
                    candidate = false;
            }
            if (candidate)
                v.add(m);
        }
        return v;
    }

    /**
     * Finds the messages in Collection c that has message a as activator.
     */
    private Collection filterWithActivator(Collection c, Object/*MMessage*/a) {
        Iterator it = c.iterator();
        Vector v = new Vector();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (ModelFacade.getActivator(m) == a)
                v.add(m);
        }
        return v;
    }

    /**
     * Finds the message in ClassifierRole r that has the message number written
     * in n. If it isn't found, null is returned.
     */
    private Object findMsg(Object/* MClassifierRole */r, String n) {
        Collection c = ModelFacade.getMessages1(r);
        Iterator it = c.iterator();
        while (it.hasNext()) {
            Object msg = /* (MMessage) */it.next();
            String gname = GeneratorDisplay.getInstance()
                    .generateMessageNumber(msg);
            if (compareMsgNumbers(gname, n))
                return msg;
        }
        return null;
    }

    /**
     * Compares two message numbers with each other to see if they are equal, in
     * the sense that they refer to the same position in a call tree.
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
                if (c < '0' || c > '9')
                    break;
                ibv *= 10;
                ibv += c - '0';
            }
            isv = 0;
            for (; i < len; i++) {
                char c = n1.charAt(i);
                if (c < 'a' || c > 'z')
                    break;
                isv *= 26;
                isv += c - 'a';
            }

            jbv = 0;
            for (; j < jlen; j++) {
                char c = n2.charAt(j);
                if (c < '0' || c > '9')
                    break;
                jbv *= 10;
                jbv += c - '0';
            }
            jsv = 0;
            for (; j < jlen; j++) {
                char c = n2.charAt(j);
                if (c < 'a' || c > 'z')
                    break;
                jsv *= 26;
                jsv += c - 'a';
            }

            if (ibv != jbv || isv != jsv) {
                return false;
            }

            if (i < len && n1.charAt(i) != '.') {
                return false;
            } else
                i++;

            if (j < jlen && n2.charAt(j) != '.') {
                return false;
            } else
                j++;
        }
        return true;
    }

    /**
     * Finds the operations in Collection c with name name and params number of
     * parameters. If no operation is found, one is created. The applicable
     * operations are returned.
     */
    private Vector getOperation(Collection c, String name, int params) {
        Vector options = new Vector();
        Iterator it;

        if (name == null || name.length() == 0)
            return options;

        it = c.iterator();
        while (it.hasNext()) {
            Object clf = /* (MClassifier) */it.next();
            Collection oe = ModelFacade.getFeatures(clf);
            Iterator it2 = oe.iterator();
            while (it2.hasNext()) {
                Object me = /* (MModelElement) */it2.next();
                if (!(ModelFacade.isAOperation(me)))
                    continue;

                Object op = /* (MOperation) */me;
                if (!name.equals(ModelFacade.getName(op)))
                    continue;
                if (params != countParameters(op))
                    continue;
                options.add(op);
            }
        }
        if (options.size() > 0)
            return options;

        it = c.iterator();
        if (it.hasNext()) {
            String expr = name + "(";
            int i;
            for (i = 0; i < params; i++) {
                if (i > 0)
                    expr += ", ";
                expr += "param" + (i + 1);
            }
            expr += ")";
            // Jaap Branderhorst 2002-23-09 added next lines to link
            // parameters and operations to the figs that represent
            // them
            Object cls = /* (MClassifier) */it.next();
            Object op = UmlFactory.getFactory().getCore().buildOperation(cls);

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
        Collection c = ModelFacade.getParameters(bf);
        Iterator it = c.iterator();
        int count = 0;

        while (it.hasNext()) {
            Object p = it.next();
            if (ModelFacade.isReturn(p))
                continue;
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
            if (c < 'a' || c > 'z')
                throw new NumberFormatException();
            v *= 26;
            v += c - 'a';
        }

        return v;
    }

    /**
     * Finds the break between message number and (possibly) message order.
     */
    private static int findMsgOrderBreak(String s) {
        int i, t;

        t = s.length();
        for (i = 0; i < t; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')
                break;
        }
        return i;
    }

    /**
     * Parse a line of the form: "name: action"
     * 
     * @param sti
     *            the stimulus object to which the string applies
     * @param s
     *            the string to be parsed.
     */
    public void parseStimulus(Object sti, String s) {
        // strip any trailing semi-colons
        s = s.trim();
        if (s.length() == 0)
            return;
        if (s.charAt(s.length() - 1) == ';')
            s = s.substring(0, s.length() - 2);

        //cut trailing string "new Action"
        s = s.trim();
        if (s.length() == 0)
            return;
        if (s.endsWith("new Action"))
            s = s.substring(0, s.length() - 10);

        String name = "";
        String action = "";
        String actionfirst = "";
        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":")).trim();
            actionfirst = s.substring(s.indexOf(":") + 1).trim();
            if (actionfirst.indexOf(":", 0) > 1) {
                action = actionfirst.substring(0, actionfirst.indexOf(":"))
                        .trim();
            } else
                action = actionfirst;
        } else
            name = s;

        Object act = ModelFacade.getDispatchAction(sti);
        ModelFacade.setName(act, action);
        ModelFacade.setName(sti, name);
    }

    /**
     * @deprecated Since 0.15.1, this is essentially a String constructor. It
     *             breaks the idea that the parser is editing preexisting
     *             objects, which is bad. It is not used within core ArgoUML.
     *             d00mst.
     *             It is used in {@link #parseTransition(Object, String)}
     *             above.
     */
    public Object parseAction(String s) {
        Object a = UmlFactory.getFactory().getCommonBehavior()
                .createCallAction();

        ModelFacade.setScript(a, UmlFactory.getFactory().getDataTypes()
                .createActionExpression("", s));

        return a;
    }

    /**
     * @deprecated Since 0.15.1, this is essentially a String constructor. It
     *             breaks the idea the idea that the parser is editing
     *             preexisting objects, which is bad. It is not used within core
     *             ArgoUML. d00mst.
     *             It is used in {@link #parseTransition(Object, String)}
     *             above.
     */
    public Object parseGuard(String s) {
        Object g = UmlFactory.getFactory().getStateMachines().createGuard();
        ModelFacade.setExpression(g, UmlFactory.getFactory().getDataTypes()
                .createBooleanExpression("", s));

        return g;
    }


    /**
     * Parses the event-signature. An event-signature has the following syntax:
     * <table>
     * <tr>
     * <td>Event-type</td>
     * <td>Event-signature</td>
     * </tr>
     * <tr>
     * <td>Call-event</td>
     * <td>name(parameter-list)</td>
     * </tr>
     * <tr>
     * <td>Change-event</td>
     * <td>when(expression-body)</td>
     * </tr>
     * <tr>
     * <td>Time-event</td>
     * <td>after(expression-body)</td>
     * </tr>
     * </table>
     * 
     * @param transition
     *            the transition the event signature belongs to
     * @param s
     * @return
     */
    private void parseEventSignature(Object transition, String s)
        throws ParseException {
        Object currentEvent = ModelFacade.getTrigger(transition);
        if (currentEvent != null 
                && !(ModelFacade.getTransitions(currentEvent).size() > 1 
                        || ModelFacade.getStates(currentEvent).size() > 0)) {
            Project p = ProjectManager.getManager().getCurrentProject();
            p.moveToTrash(currentEvent);
        }
        Object event = null;
        StringTokenizer tokenizer = new StringTokenizer(s, "()");
        String name = tokenizer.nextToken();
        if (name.equalsIgnoreCase("after")) {
            if (tokenizer.hasMoreTokens())
                event = StateMachinesFactory.getFactory().buildTimeEvent(
                        tokenizer.nextToken());
            else
                event = StateMachinesFactory.getFactory().buildTimeEvent();
        } else if (name.equalsIgnoreCase("when")) {
            if (tokenizer.hasMoreTokens()) {
                event = StateMachinesFactory.getFactory().buildChangeEvent(
                        tokenizer.nextToken());
            } else
                event = StateMachinesFactory.getFactory().buildChangeEvent();
        } else {
            event = StateMachinesFactory.getFactory().buildCallEvent();
            ModelFacade.setName(event, name);
            if (tokenizer.hasMoreTokens()) {
                parseParamList(event, tokenizer.nextToken(), 0);
            }
        }
        ModelFacade.setTrigger(transition, event);
    }

    /**
     * Parse a line of the form: "name: base-class"
     * 
     * @param obj
     *            the MObject to be parsed.
     * @param s
     *            the string to be parsed.
     */
    public void parseObject(Object/* MObject */obj, String s) {
        // strip any trailing semi-colons
        s = s.trim();

        if (s.length() == 0)
            return;
        if (s.charAt(s.length() - 1) == ';')
            s = s.substring(0, s.length() - 2);

        String name = "";
        String bases = "";
        StringTokenizer baseTokens = null;

        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":", 0)).trim();
            bases = s.substring(s.indexOf(":", 0) + 1).trim();
            baseTokens = new StringTokenizer(bases, ",");
        } else {
            name = s;
        }

        ModelFacade.setName(obj, name);

        ModelFacade.setClassifiers(obj, new Vector());
        if (baseTokens != null) {
            while (baseTokens.hasMoreElements()) {
                String typeString = baseTokens.nextToken();
                Object type =
                /* (MClassifier) */ProjectManager.getManager()
                        .getCurrentProject().findType(typeString);
                ModelFacade.addClassifier(obj, type);
            }
        }
    }

    /**
     * Parse a line of the form: "name : base-node".
     * 
     * @param noi
     *            the node instance on which the string applies.
     * @param s
     *            the string to be parsed.
     */
    public void parseNodeInstance(Object noi, String s) {
        // strip any trailing semi-colons
        s = s.trim();
        if (s.length() == 0)
            return;
        if (s.charAt(s.length() - 1) == ';')
            s = s.substring(0, s.length() - 2);

        String name = "";
        String bases = "";
        StringTokenizer tokenizer = null;

        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":")).trim();
            bases = s.substring(s.indexOf(":") + 1).trim();
        } else {
            name = s;
        }

        tokenizer = new StringTokenizer(bases, ",");

        Vector v = new Vector();
        Object ns = ModelFacade.getNamespace(noi);
        if (ns != null) {
            while (tokenizer.hasMoreElements()) {
                String newBase = tokenizer.nextToken();
                Object cls = ModelFacade.lookupIn(ns, newBase.trim());
                if (cls != null)
                    v.add(cls);
            }
        }

        ModelFacade.setClassifiers(noi, v);
        ModelFacade.setName(noi, new String(name));

    }

    /**
     * Parse a line of the form: "name : base-component"
     * 
     * @param coi
     *            the component instance on which the string applies.
     * @param s
     *            the string to parse.
     */
    public void parseComponentInstance(Object coi, String s) {
        // strip any trailing semi-colons
        s = s.trim();
        if (s.length() == 0)
            return;
        if (s.charAt(s.length() - 1) == ';')
            s = s.substring(0, s.length() - 2);

        String name = "";
        String bases = "";
        StringTokenizer tokenizer = null;

        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":")).trim();
            bases = s.substring(s.indexOf(":") + 1).trim();
        } else {
            name = s;
        }

        tokenizer = new StringTokenizer(bases, ",");

        Vector v = new Vector();
        Object ns = ModelFacade.getNamespace(coi);
        if (ns != null) {
            while (tokenizer.hasMoreElements()) {
                String newBase = tokenizer.nextToken();
                Object cls = ModelFacade.lookupIn(ns, newBase.trim());
                if (cls != null)
                    v.add(cls);
            }
        }

        ModelFacade.setClassifiers(coi, v);
        ModelFacade.setName(coi, new String(name));

    }

    /**
     * This builds a CallAction with default attributes. But without Operation!
     * 
     * @author MVW
     * @param s
     *            string representing the Script of the Action
     */
    private Object buildNewCallAction(String s) {
        Object a = UmlFactory.getFactory().getCommonBehavior()
                .createCallAction();
        Object ae = UmlFactory.getFactory().getDataTypes()
                .createActionExpression("Java", s);
        ModelFacade.setScript(a, ae);
        ModelFacade.setName(a, "anon");
        return a;
    }

    /**
     * Update an existing Action with a new Script.
     * 
     * @author MVW
     * @param old
     *            the Action
     * @param s
     *            a string representing a new Script for the ActionExpression
     */
    private void updateAction(Object old, String s) {
        Object ae = ModelFacade.getScript(old);
        String language = "java";
        if (ae != null)
            language = ModelFacade.getLanguage(ae);
        ae = UmlFactory.getFactory().getDataTypes().createActionExpression(
                language, s);
        ModelFacade.setScript(old, ae);
    }

    /**
     * this deletes modelelements, and swallows null without barking
     * 
     * @author MVW
     * @param obj
     *            the modelelement to be deleted
     */
    private void delete(Object obj) {
        if (obj != null)
            UmlFactory.getFactory().delete(obj);
    }
    
    

    /**
     * An action state figure shows the action expression of the entry
     * action according to the UML spec.
     *
     * @see org.argouml.uml.generator.Parser#parseActionState(
     *         java.lang.String, java.lang.Object)
     */
    public Object parseActionState(String s, Object actionState) {
        Object entry = ModelFacade.getEntry(actionState);
        String language = "";
        if (entry == null) {
            entry = CommonBehaviorFactory.getFactory()
                .buildUninterpretedAction(actionState);
        } else {
            language = ModelFacade.getLanguage(ModelFacade.getScript(entry));
        }
        Object actionExpression = DataTypesFactory.getFactory()
            .createActionExpression(language, s);
        ModelFacade.setScript(entry, actionExpression);
       return actionState;
    }
    
    /**
     * An objectFlowState is represented on a diagram by 2 strings:
     * 1. Classifier name
     * 2. State name
     * This function solely handles 1.
     * 
     * @param s the string to be parsed
     * @param objectFlowState the input object
     * @throws ParseException when the input should be rejected 
     *         (i.e. when the classifier does not exist)
     */
    public void parseObjectFlowState1(String s, Object objectFlowState)
        throws ParseException {
        
        Object c = ActivityGraphsHelper.getHelper()
                    .findClassifierByName(objectFlowState, s);
        if (c != null) {
            ModelFacade.setType(objectFlowState, c);
        } else {
            throw new ParseException("Classifier not found", 0);
        }
    }
    
    /**
     * An objectFlowState is represented on a diagram by 2 strings:
     * 1. Classifier name
     * 2. State name
     * This function solely handles 2.
     * 
     * @param s the string to be parsed
     * @param objectFlowState the input object
     * @throws ParseException when the input should be rejected 
     *         (i.e. when the classifier or the state do not exist)
     */
    public void parseObjectFlowState2(String s, Object objectFlowState) 
        throws ParseException {
        
        Object c = ModelFacade.getType(objectFlowState); // get the classifier
        if (c != null) {
            if (ModelFacade.isAClassifierInState(c)) {
                if ((s == "") || (s == null)) {
                    // the State of a ClassifierInState is removed, 
                    // so let's reduce it to a Classifier.
                    ModelFacade.setType(objectFlowState, 
                            ModelFacade.getType(c));
                    UmlFactory.getFactory().delete(c);
                    return; // the model is changed - our job is done
                }
                Collection states = ModelFacade.getInStates(c);
                Iterator i = states.iterator();
                while (i.hasNext()) { 
                    Object state = i.next();
                    if (ModelFacade.getName(state) == s) {
                        // nothing changed - the state already exists
                        return;
                    }
                }
                /* Now we have to see if any state in any statemachine of c is
                named s. If so, then we only have to link the state to c. */
                Object state = 
                    ActivityGraphsHelper.getHelper().findStateByName(c, s);
                if (state != null) {
                    ModelFacade.addInState(c, state); 
                    // the model is changed - our job is done
                } else {
                    // no state named s is found, so we have to 
                    // reject the user's input
                    throw new ParseException("State not found", 0);
                }
            } else { // then c is a "normal" Classifier
                // let's create a new ClassifierInState with the correct links
                Object state = 
                    ActivityGraphsHelper.getHelper().findStateByName(c, s);
                if (state != null) {
                    ActivityGraphsFactory.getFactory()
                                    .buildClassifierInState(c, state);
                    // the model is changed - our job is done
                } else {
                    // no state named s is found, so we have to 
                    // reject the user's input
                    throw new ParseException("State not found", 0);
                }
            }
        } else {
            // if no classifier has been set, then entering a state is 
            // not useful, so the user's input has to be rejected.
            throw new ParseException("Classifier not found", 0);
        }
    }
} /* end class ParserDisplay */
