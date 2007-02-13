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
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.notation.OperationNotation;
import org.argouml.util.MyTokenizer;

/**
 * The UML notation for an Operation.
 * 
 * @author mvw@tigris.org
 */
public class OperationNotationUml extends OperationNotation {

    /**
     * The constructor.
     *
     * @param operation the operation that is represented
     */
    public OperationNotationUml(Object operation) {
        super(operation);
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseOperationFig(Model.getFacade().getOwner(modelElement), 
                    modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.operation";
            Object[] args = {
                pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()),
            };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
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
        int end = NotationUtilityUml.indexOfNextCheckedSemicolon(text, start);
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
        end = NotationUtilityUml.indexOfNextCheckedSemicolon(text, start);
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
                            Model.getCoreHelper().addFeature(
                                    classifier, ++i, newOp);
                        } else {
                            Model.getCoreHelper().addFeature(
                                    classifier, newOp);
                        }
                    } catch (ParseException ex) {
                        if (pex == null) {
                            pex = ex;
                        }
                    }
                }
            }
            start = end + 1;
            end = NotationUtilityUml.indexOfNextCheckedSemicolon(text, start);
        }
        if (pex != null) {
            throw pex;
        }
    }


    /**
     * Parse a line of text and aligns the Operation to the specification
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

        if (s.length() > 0 
                && NotationUtilityUml.VISIBILITYCHARS.indexOf(s.charAt(0)) 
                    >= 0) {
            visibility = s.substring(0, 1);
            s = s.substring(1);
        }

        try {
            st = new MyTokenizer(s, " ,\t,<<,\u00AB,\u00BB,>>,:,=,{,},\\,",
                    NotationUtilityUml.operationCustomSep);
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (" ".equals(token) || "\t".equals(token)
                        || ",".equals(token)) {
                    continue; // Do nothing
                } else if ("<<".equals(token) || "\u00AB".equals(token)) {
                    if (stereotype != null) {
                        parseError("operation.stereotypes", 
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
                } else if ("{".equals(token)) {
                    properties = tokenOpenBrace(st, properties);
                } else if (":".equals(token)) {
                    hasColon = true;
                } else if ("=".equals(token)) {
                    parseError("operation.default-values", st.getTokenIndex());
                } else if (token.charAt(0) == '(' && !hasColon) {
                    if (parameterlist != null) {
                        parseError("operation.two-parameter-lists", 
                                st.getTokenIndex());
                    }

                    parameterlist = token;
                } else {
                    if (hasColon) {
                        if (type != null) {
                            parseError("operation.two-types", 
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0
                                && (token.charAt(0) == '\"'
                                    || token.charAt(0) == '\'')) {
                            parseError("operation.type-quoted",
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0 && token.charAt(0) == '(') {
                            parseError("operation.type-expr", 
                                    st.getTokenIndex());
                        }

                        type = token;
                    } else {
                        if (name != null && visibility != null) {
                            parseError("operation.extra-text",
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0
                                && (token.charAt(0) == '\"'
                                    || token.charAt(0) == '\'')) {
                            parseError("operation.name-quoted",
                                    st.getTokenIndex());
                        }

                        if (token.length() > 0 && token.charAt(0) == '(') {
                            parseError("operation.name-expr", 
                                    st.getTokenIndex());
                        }

                        if (name == null
                                && visibility == null
                                && token.length() > 1
                                && NotationUtilityUml.VISIBILITYCHARS.indexOf(
                                        token.charAt(0))
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
            } // end while loop
        } catch (NoSuchElementException nsee) {
            parseError("operation.unexpected-end-operation", 
                    s.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (parameterlist != null) {
            // parameterlist is guaranteed to contain at least "("
            if (parameterlist.charAt(parameterlist.length() - 1) != ')') {
                parseError("operation.parameter-list-incomplete",
                        paramOffset + parameterlist.length() - 1);
            }

            paramOffset++;
            parameterlist = parameterlist.substring(1,
                    parameterlist.length() - 1);
            NotationUtilityUml.parseParamList(op, parameterlist, paramOffset);
        }

        if (visibility != null) {
            Model.getCoreHelper().setVisibility(op,
                    NotationUtilityUml.getVisibility(visibility.trim()));
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
            NotationUtilityUml.setProperties(op, properties, 
                    NotationUtilityUml.operationSpecialStrings);
        }

        NotationUtilityUml.dealWithStereotypes(op, stereotype, true);
    }

    /**
     * Convenience method to signal a parser error.
     * 
     * @param message
     *            string containing error message literal. It will be appended
     *            to the base "parser.error." and localized.
     * @param offset
     *            offset to where error occurred
     * @throws ParseException
     */
    private void parseError(String message, int offset)
        throws ParseException {

        throw new ParseException(
                Translator.localize("parsing.error." + message), 
                offset);
    }

    /**
     * Parse tokens following an open brace (properties).
     * 
     * @param st tokenizer being used
     * @param properties current properties vector
     * @return updated vector of properties
     * @throws ParseException
     */
    private Vector tokenOpenBrace(MyTokenizer st, Vector properties)
        throws ParseException {
        String token;
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
                    String msg = 
                        "parsing.error.operation.prop-stereotypes";
                    Object[] args = {propname};
                    throw new ParseException(
                    		Translator.localize(msg, 
                            args), 
                            st.getTokenIndex());
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
        return properties;
    }


    /**
     * Sets the return parameter of op to be of type type. If there is none, one
     * is created. If there are many, all but one are removed.
     *
     * @param op the operation
     * @param type the type of the return parameter
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

    /*
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.operation";
    }

    /*
     * Generates an operation according to the UML notation:
     *
     *         stereotype visibility name (parameter-list) :
     *                         return-type-expression {property-string}
     *
     * For the return-type-expression: only the types of the return parameters
     * are shown.  Depending on settings in Notation, visibility and
     * properties are shown/not shown.
     *
     * @author jaap.branderhorst@xs4all.nl
     *
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        Project p = ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = p.getProjectSettings();

        try {
            String stereoStr = NotationUtilityUml.generateStereotype(
                    Model.getFacade().getStereotypes(modelElement));
            String visStr = NotationUtilityUml.generateVisibility(modelElement);
            String nameStr = Model.getFacade().getName(modelElement);

            // the parameters
            StringBuffer parameterListBuffer = new StringBuffer();
            Collection coll = Model.getFacade().getParameters(modelElement);
            Iterator it = coll.iterator();
            int counter = 0;
            while (it.hasNext()) {
                Object parameter = it.next();
                if (!Model.getFacade().hasReturnParameterDirectionKind(
                        parameter)) {
                    counter++;
                    parameterListBuffer.append(
                            NotationUtilityUml.generateParameter(parameter));
                    parameterListBuffer.append(",");
                }
            }
            if (counter > 0) {
                parameterListBuffer.delete(
                        parameterListBuffer.length() - 1,
                        parameterListBuffer.length());
            }

            StringBuffer parameterStr = new StringBuffer();
            parameterStr.append("(").append(parameterListBuffer).append(")");

            // the returnparameters
            coll = Model.getCoreHelper().getReturnParameters(modelElement);
            StringBuffer returnParasSb = new StringBuffer();
            if (coll != null && coll.size() > 0) {
                returnParasSb.append(": ");
                Iterator it2 = coll.iterator();
                while (it2.hasNext()) {
                    Object type = Model.getFacade().getType(it2.next());
                    if (type != null) {
                        returnParasSb.append(Model.getFacade().getName(type));
                    }
                    returnParasSb.append(",");
                }
                returnParasSb.delete(
                        returnParasSb.length() - 1,
                        returnParasSb.length());
            }

            // the properties
            StringBuffer propertySb = new StringBuffer().append("{");
            // the query state
            if (Model.getFacade().isQuery(modelElement)) {
                propertySb.append("query,");
            }
            if (Model.getFacade().isRoot(modelElement)) {
                propertySb.append("root,");
            }
            if (Model.getFacade().isLeaf(modelElement)) {
                propertySb.append("leaf,");
            }
            if (Model.getFacade().getConcurrency(modelElement) != null) {
                propertySb.append(Model.getFacade().getName(
                        Model.getFacade().getConcurrency(modelElement)));
                propertySb.append(',');
            }
            Iterator it3 = Model.getFacade().getTaggedValues(modelElement);
            StringBuffer taggedValuesSb = new StringBuffer();
            if (it3 != null && it3.hasNext()) {
                while (it3.hasNext()) {
                    taggedValuesSb.append(
                            NotationUtilityUml.generateTaggedValue(it3.next()));
                    taggedValuesSb.append(",");
                }
                taggedValuesSb.delete(
                        taggedValuesSb.length() - 1,
                        taggedValuesSb.length());
            }
            if (propertySb.length() > 1) {
                propertySb.delete(propertySb.length() - 1, propertySb.length());
                // remove last ,
                propertySb.append("}");
            } else {
                propertySb = new StringBuffer();
            }

            // lets concatenate it to the resulting string (genStr)
            StringBuffer genStr = new StringBuffer(30);
            if ((stereoStr != null) && (stereoStr.length() > 0)) {
                genStr.append(stereoStr).append(" ");
            }
            if ((visStr != null)
                    && (visStr.length() > 0)
                    && ps.getShowVisibilityValue()) {
                genStr.append(visStr);
            }
            if ((nameStr != null) && (nameStr.length() > 0)) {
                genStr.append(nameStr);
            }
            /* The "show types" defaults to TRUE, to stay compatible with older
             * ArgoUML versions that did not have this setting: */
            if (ps.getShowTypesValue()) {
                genStr.append(parameterStr).append(" ");
                if ((returnParasSb != null) && (returnParasSb.length() > 0)) {
                    genStr.append(returnParasSb).append(" ");
                }
            } else {
                genStr.append("()");
            }
            if ((propertySb.length() > 0)
                    && ps.getShowPropertiesValue()) {
                genStr.append(propertySb);
            }
            return genStr.toString().trim();
        } catch (InvalidElementException e) {
            // The model element was deleted while we were working on it
            return "";   
        }

    }

}
