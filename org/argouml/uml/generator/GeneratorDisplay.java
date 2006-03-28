// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.uml.notation.uml.NotationUtilityUml;

/**
 * Generator2 subclass to generate notation for display in diagrams in
 * text fields in the ArgoUML user interface.  The generated code
 * is UML. <p>
 *
 * In contrary to its name, this is not the only class
 * to generate for the "Display". Similarely to the GeneratorJava class,
 * this class would better have been named GeneratorUML.
 *
 * @stereotype singleton
 * @author jrobbins@ics.uci.edu
 * @deprecated since V0.19.8 by mvw. Replaced by a split architecture:
 * see for the notation part
 * http://argouml.tigris.org/proposals/notation/index.html,
 * and for the code part issue 3546.
 */
public class GeneratorDisplay extends Generator2 {

    private static GeneratorDisplay singleton;

    /**
     * @return get the singleton
     */
    public static GeneratorDisplay getInstance() {
        if (singleton == null) {
            singleton = new GeneratorDisplay();
        }
        return singleton;
    }

    /**
     * The constructor.
     */
    private GeneratorDisplay() {
        super(
            Notation.makeNotation(
                "UML",
                "1.4",
                Argo.lookupIconResource("UmlNotation")));
    }

    /**
     * Generate the display for an extension point.<p>
     *
     * The representation is "name: location". "name: " is omitted if there
     * is no name given.<p>
     *
     * @param ep  The extension point.
     *
     * @return    The string representing the extension point.
     */
    public String generateExtensionPoint(Object ep) {

        // The string to build

        String s = "";

        // Get the fields we want

        String epName = Model.getFacade().getName(ep);
        String epLocation = Model.getFacade().getLocation(ep);

        // Put in the name field if it's there

        if ((epName != null) && (epName.length() > 0)) {
            s += epName + ": ";
        }

        // Put in the location field if it's there

        if ((epLocation != null) && (epLocation.length() > 0)) {
            s += epLocation;
        }

        return s;
    }

    /**
     *  Generates an operation according to the UML 1.3 notation:
     *
     *          stereotype visibility name (parameter-list) :
     *                          return-type-expression {property-string}
     *
     *  For the return-type-expression: only the types of the return parameters
     *  are shown.  Depending on settings in Notation, visibility and
     *  properties are shown/not shown.
     *
     *  @author jaap.branderhorst@xs4all.nl
     *  @see org.argouml.notation.NotationProvider2#generateOperation(
     *          Object, boolean)
     */
    public String generateOperation(Object op, boolean documented) {
        String stereoStr =
            generateStereotype(Model.getFacade().getStereotypes(op));
        String visStr =
            NotationUtilityUml.generateVisibility(op);
        String nameStr = generateName(Model.getFacade().getName(op));

        // the parameters
        StringBuffer parameterListBuffer = new StringBuffer();
        Collection coll = Model.getFacade().getParameters(op);
        Iterator it = coll.iterator();
        int counter = 0;
        while (it.hasNext()) {
            Object parameter = it.next();
            if (!Model.getFacade().hasReturnParameterDirectionKind(parameter)) {
                counter++;
                parameterListBuffer.append(generateParameter(parameter));
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
        coll = Model.getCoreHelper().getReturnParameters(op);
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
        if (Model.getFacade().isQuery(op)) {
            propertySb.append("query,");
        }
        if (Model.getFacade().isRoot(op)) {
            propertySb.append("root,");
        }
        if (Model.getFacade().isLeaf(op)) {
            propertySb.append("leaf,");
        }
        if (Model.getFacade().getConcurrency(op) != null) {
            propertySb.append(Model.getFacade().getName(
		    Model.getFacade().getConcurrency(op)));
	    propertySb.append(',');
        }
        Iterator it3 = Model.getFacade().getTaggedValues(op);
        StringBuffer taggedValuesSb = new StringBuffer();
        if (it3 != null && it3.hasNext()) {
            while (it3.hasNext()) {
                taggedValuesSb.append(
                    generateTaggedValue(it3.next()));
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
            && Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY)) {
            genStr.append(visStr);
        }
        if ((nameStr != null) && (nameStr.length() > 0)) {
            genStr.append(nameStr);
        }
        /* The "show types" defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting: */
        if (Configuration.getBoolean(Notation.KEY_SHOW_TYPES, true)) {
            genStr.append(parameterStr).append(" ");
            if ((returnParasSb != null) && (returnParasSb.length() > 0)) {
                genStr.append(returnParasSb).append(" ");
            }
        } else {
            genStr.append("()");
        }
        if ((propertySb.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_PROPERTIES)) {
            genStr.append(propertySb);
        }
        return genStr.toString().trim();
    }

    /**
     * Generates a string representation for the provided
     * attribute. The string representation will be of the form:
     *          visibility name [multiplicity] : type-expression =
     *                          initial-value {property-string}
     * Depending on settings in Notation, visibility, multiplicity,
     * type-expression, initial value and properties are shown/not shown.
     *
     * @see org.argouml.notation.NotationProvider2#generateAttribute(
     *          Object, boolean)
     */
    public String generateAttribute(Object attr, boolean documented) {
        String visibility = NotationUtilityUml.generateVisibility(attr);
        // generateStereotype accepts a collection, despite its name
        String stereo =
            generateStereotype(Model.getFacade().getStereotypes(attr));
        String name = Model.getFacade().getName(attr);
        String multiplicity =
	    generateMultiplicity(Model.getFacade().getMultiplicity(attr));
        String type = ""; // fix for loading bad projects
        if (Model.getFacade().getType(attr) != null) {
            type = Model.getFacade().getName(Model.getFacade().getType(attr));
        }
        String initialValue = "";
        if (Model.getFacade().getInitialValue(attr) != null) {
            initialValue =
		(String) Model.getFacade().getBody(
			Model.getFacade().getInitialValue(attr));
        }
        String changeableKind = "";
        if (Model.getFacade().getChangeability(attr) != null) {
            if (Model.getChangeableKind().getFrozen().equals(
	            Model.getFacade().getChangeability(attr))) {
                changeableKind = "frozen";
	    } else if (Model.getChangeableKind().getAddOnly().equals(
		    Model.getFacade().getChangeability(attr))) {
                changeableKind = "addOnly";
	    }
        }
        StringBuffer properties = new StringBuffer();
        if (changeableKind.length() > 0) {
            properties.append("{ ").append(changeableKind).append(" }");
        }

        StringBuffer sb = new StringBuffer(20);
        if ((stereo != null) && (stereo.length() > 0)) {
            sb.append(stereo).append(" ");
        }
        if ((visibility != null)
            && (visibility.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY)) {
            sb.append(visibility);
        }
        if ((name != null) && (name.length() > 0)) {
            sb.append(name).append(" ");
        }
        if ((multiplicity != null)
            && (multiplicity.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_MULTIPLICITY)) {
            sb.append("[").append(multiplicity).append("]").append(" ");
        }
        if ((type != null) && (type.length() > 0)
            /* The "show types" defaults to TRUE, to stay compatible with older
             * ArgoUML versions that did not have this setting: */
            && Configuration.getBoolean(Notation.KEY_SHOW_TYPES, true)) {
            sb.append(": ").append(type).append(" ");
        }
        if ((initialValue != null)
            && (initialValue.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_INITIAL_VALUE)) {
            sb.append(" = ").append(initialValue).append(" ");
        }
        if ((properties.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_PROPERTIES)) {
            sb.append(properties);
        }
        return sb.toString().trim();

    }

    /**
     * Generates the representation of a parameter on the display
     * (diagram). The string to be returned will have the following
     * syntax:<p>
     *
     * kind name : type-expression = default-value
     *
     * @see org.argouml.notation.NotationProvider2#generateParameter(java.lang.Object)
     */
    public String generateParameter(Object parameter) {
        StringBuffer s = new StringBuffer();
        s.append(generateKind(Model.getFacade().getKind(parameter)));
        if (s.length() > 0) s.append(" ");
        s.append(generateName(Model.getFacade().getName(parameter)));
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

    /**
     * @see org.argouml.notation.NotationProvider2#generatePackage(java.lang.Object)
     */
    public String generatePackage(Object p) {
        String s = generateStereotype( Model.getFacade().getStereotypes(p));
        s += " ";
        s += NotationUtilityUml.generateVisibility(Model.getFacade().getVisibility(p));
        s += generateName(Model.getFacade().getName(p));
        return s.trim();
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateClassifier(java.lang.Object)
     */
    public String generateClassifier(Object cls) {
        String generatedName = generateName(Model.getFacade().getName(cls));
        String classifierKeyword;
        if (Model.getFacade().isAClass(cls)) {
            classifierKeyword = "class";
        } else if (Model.getFacade().isAInterface(cls)) {
            classifierKeyword = "interface";
        } else {
            return ""; // actors and use cases
        }
        String s = "";
        s += NotationUtilityUml.generateVisibility(cls);
        if (Model.getFacade().isAbstract(cls)) {
            s += "abstract ";
        }
        if (Model.getFacade().isLeaf(cls)) {
            s += "final ";
        }
        s += classifierKeyword + " " + generatedName + " ";
        String baseClass = generateGeneralization(Model.getFacade()
                .getGeneralizations(cls), false);
        if (!baseClass.equals("")) {
            s += "extends " + baseClass + " ";
        }

        //UML: realizations!  String interfaces =
        //generateRealization(cls.getRealizations(), true); if
        //(!interfaces.equals("")) s += "implements " + interfaces + "
        //";
        s += "{\n";

        Collection strs = Model.getFacade().getStructuralFeatures(cls);
        if (strs != null) {
            s += "\n";
            s += INDENT + "// Attributes\n";
            Iterator strEnum = strs.iterator();
            while (strEnum.hasNext()) {
                s += INDENT + generate(strEnum.next()) + ";\n";
            }
        }

        Collection ends = Model.getFacade().getAssociationEnds(cls);
        if (ends != null) {
            s += "\n";
            s += INDENT + "// Associations\n";
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                Object ae = /*(MAssociationEnd)*/ endEnum.next();
                Object a = Model.getFacade().getAssociation(ae);
                s += INDENT + generateAssociationFrom(a, ae);
            }
        }

        // TODO: constructors

        Collection behs = Model.getFacade().getOperations(cls);
        if (behs != null) {
            s += "\n";
            s += INDENT + "// Operations\n";
            Iterator behEnum = behs.iterator();
            String terminator = " {\n" + INDENT + "}";
            if (Model.getFacade().isAInterface(cls)) {
                terminator = ";";
            }
            while (behEnum.hasNext()) {
                s += INDENT + generate(behEnum.next()) + terminator + "\n";
            }
        }
        s += "\n";
        s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
        return s;
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateTaggedValue(java.lang.Object)
     */
    public String generateTaggedValue(Object tv) {
        if (tv == null) {
            return "";
        }
        return generateName(Model.getFacade().getTagOfTag(tv))
            + "="
            + generateUninterpreted(Model.getFacade().getValueOfTag(tv));
    }

    /**
     * Generates the textual number of MMessage m. The number is a string
     * of numbers separated by points which describes the message's order
     * and level in a collaboration.<p>
     *
     * If you plan to modify this number, make sure that
     * ParserDisplay.parseMessage is adapted to the change.
     *
     * @param m A Message to generate the number for.
     * @return A String with the message number of m.
     */
    public String generateMessageNumber(Object/*MMessage*/ m) {
        MsgPtr ptr = new MsgPtr();
        int pos = recCountPredecessors(m, ptr) + 1;
        return generateMessageNumber(m, ptr.message, pos);
    }

    private String generateKind(Object /*Parameter etc.*/ kind) {
        StringBuffer s = new StringBuffer();
        if (kind == null /* "in" is the default */
                || kind == Model.getDirectionKind().getInParameter()) {
            s.append(/*"in"*/ ""); /* See issue 3421. */
        } else if (kind == Model.getDirectionKind().getInOutParameter()) {
            s.append("inout");
        } else if (kind == Model.getDirectionKind().getReturnParameter()) {
            ;// return nothing
        } else if (kind == Model.getDirectionKind().getOutParameter()) {
            s.append("out");
        }
        return s.toString();
    }

    private String generateMessageNumber(
        Object/*MMessage*/ m,
        Object/*MMessage*/ pre,
        int position) {
        Collection c;
        Iterator it;
        String mname = "";
        Object act;
        int subpos = 0, submax = 1;

        if (m == null) {
            return null;
        }

        act = Model.getFacade().getActivator(m);
        if (act != null) {
            mname = generateMessageNumber(act);
        }

        if (pre != null) {
            c = Model.getFacade().getMessages3(pre);
            submax = c.size();
            it = c.iterator();
            while (it.hasNext() && it.next() != m) {
                subpos++;
            }
        }

        if (mname.length() > 0) {
            if (submax > 1) {
                return mname + "." + position + (char) ('a' + subpos);
            }
            return mname + "." + position;
        }

        if (submax > 1) {
            return Integer.toString(position) + (char) ('a' + subpos);
        }
        return Integer.toString(position);
    }

    class MsgPtr {
        public Object/*MMessage*/ message;
    }

    int recCountPredecessors(Object message, MsgPtr ptr) {
        Collection c;
        Iterator it;
        int pre = 0;
        int local = 0;
        Object/*MMessage*/ maxmsg = null;
        Object/*MMessage*/ act;

        if (message == null) {
            ptr.message = null;
            return 0;
        }

        act = Model.getFacade().getActivator(message);
        c = Model.getFacade().getPredecessors(message);
        it = c.iterator();
        while (it.hasNext()) {
            Object msg = it.next();
            if (Model.getFacade().getActivator(msg) != act) {
                continue;
            }
            int p = recCountPredecessors(msg, null) + 1;
            if (p > pre) {
                pre = p;
                maxmsg = msg;
            }
            local++;
        }

        if (ptr != null) {
            ptr.message = maxmsg;
        }

        return Math.max(pre, local);
    }

    int countSuccessors(Object/*MMessage*/ m) {
        int count = 0;
        Object act = Model.getFacade().getActivator(m);
        Collection coll = Model.getFacade().getMessages3(m);
        if (coll != null) {
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                if (Model.getFacade().getActivator(msg) != act) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    /**
     * Generates a textual description of a MIterationExpression.
     *
     * @param expr the given expression
     * @return the string
     */
    public String generateRecurrence(Object expr) {
        if (expr == null) {
            return "";
        }

        return Model.getFacade().getBody(expr).toString();
    }

    /**
     * Generates a textual description for a MMessage m.
     *
     * @param m A MMessage to generate a description for.
     * @return A String suitable to show in a collaboration diagram.
     */
    public String generateMessage(Object m) {
        Iterator it;
        Collection pre;
        Object act;
        Object/*MMessage*/ rt;
        MsgPtr ptr;

        String action = "";
        String number;
        String predecessors = "";
        int lpn;

        if (m == null) {
            return "";
        }

        ptr = new MsgPtr();
        lpn = recCountPredecessors(m, ptr) + 1;
        rt = Model.getFacade().getActivator(m);

        pre = Model.getFacade().getPredecessors(m);
        it = (pre != null) ? pre.iterator() : null;
        if (it != null && it.hasNext()) {
            MsgPtr ptr2 = new MsgPtr();
            int precnt = 0;

            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                int mpn = recCountPredecessors(msg, ptr2) + 1;

                if (mpn == lpn - 1
                    && rt == Model.getFacade().getActivator(msg)
                    && Model.getFacade().getPredecessors(msg).size() < 2
                    && (ptr2.message == null
                        || countSuccessors(ptr2.message) < 2)) {
                    continue;
                }

                if (predecessors.length() > 0) {
                    predecessors += ", ";
                }
                predecessors += generateMessageNumber(msg, ptr2.message, mpn);
                precnt++;
            }

            if (precnt > 0) {
                predecessors += " / ";
            }
        }

        number = generateMessageNumber(m, ptr.message, lpn);

        act = Model.getFacade().getAction(m);
        if (act != null) {
            if (Model.getFacade().getRecurrence(act) != null) {
                number =
		    generateRecurrence(Model.getFacade().getRecurrence(act))
		    + " "
		    + number;
            }

            action = generateAction(act);
            /* Dirty fix for issue 1758 (Needs to be amended
             * when we start supporting parameters): */
            if (!action.endsWith(")")) action = action + "()";
        }

        return predecessors + number + " : " + action;
    }

    /**
     * @param a the association
     * @param ae the associationend
     * @return a string representing the association-end
     */
    public String generateAssociationFrom(Object a, Object ae) {
        // TODO: does not handle n-ary associations
        String s = "";
        Collection connections = Model.getFacade().getConnections(a);
        if (connections == null) {
            return s;
        }
        Iterator connEnum = connections.iterator();
        while (connEnum.hasNext()) {
            Object ae2 = /*(MAssociationEnd)*/ connEnum.next();
            if (ae2 != ae) {
                s += generateAssociationEnd(ae2);
            }
        }
        return s;
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateAssociation(java.lang.Object)
     */
    public String generateAssociation(Object a) {
        String s = "";
        //     String generatedName = generateName(a.getName());
        //     s += "MAssociation " + generatedName + " {\n";

        //     Iterator endEnum = a.getConnection().iterator();
        //     while (endEnum.hasNext()) {
        //       MAssociationEnd ae = (MAssociationEnd)endEnum.next();
        //       s += generateAssociationEnd(ae);
        //       s += ";\n";
        //     }
        //     s += "}\n";
        return s;
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateAssociationEnd(java.lang.Object)
     */
    public String generateAssociationEnd(Object ae) {
        if (!Model.getFacade().isNavigable(ae)) {
            return "";
        }
        String s = "protected ";
        if (Model.getScopeKind().getClassifier().equals(
		Model.getFacade().getTargetScope(ae))) {
            s += "static ";
	}

        //     String n = ae.getName(); if (n != null &&
        //     !String.UNSPEC.equals(n)) s += generateName(n) + " ";
        //     if (Model.getFacade().isNavigable(ae)) s += "navigable "; if
        //     (ae.getIsOrdered()) s += "ordered ";
        if (Model.getFacade().getUpper(ae) != 1 ) {
            s += generateClassifierRef(Model.getFacade().getType(ae));
	} else {
            s += "Vector "; //generateMultiplicity(m) + " ";
	}

        s += " ";

        String n = Model.getFacade().getName(ae);
        Object asc = Model.getFacade().getAssociation(ae);
        String ascName = Model.getFacade().getName(asc);
        if (n != null && n != null && n.length() > 0) {
            s += generateName(n);
        } else if (
            ascName != null && ascName != null && ascName.length() > 0) {
            s += generateName(ascName);
        } else {
            s += "my" + generateClassifierRef(Model.getFacade().getType(ae));
        }

        return s + ";\n";
    }

    /**
     * @param me the given ModelElement
     * @return a string representing the constraints for the given modelelements
     */
    public String generateConstraints(Object me) {
        Collection constr = Model.getFacade().getConstraints(me);
        if (constr == null || constr.size() == 0) {
            return "";
        }
        String s = "{";
        Iterator conEnum = constr.iterator();
        while (conEnum.hasNext()) {
            s += generateConstraint(conEnum.next());
            if (conEnum.hasNext()) {
                s += "; ";
            }
        }
        s += "}";
        return s;
    }

    /**
     * @param c the given constraint
     * @return a string representing the given constraint
     */
    public String generateConstraint(Object c) {
        return generateExpression(c);
    }

    /**
     * generate the name of an association role of the form:
     *  / name : name of the base association.
     *
     * @param assocRole the given associationrole
     * @return the generated name
     *
     * @see org.argouml.notation.NotationProvider2#generateAssociationRole(java.lang.Object)
     */
    public String generateAssociationRole(Object assocRole) {
        //get the associationRole name
        String text = "/" + Model.getFacade().getName(assocRole) + ":";
        //get the base association name
        Object assoc = Model.getFacade().getBase(assocRole);
        if (assoc != null) {
            text = text + Model.getFacade().getName(assoc);
        }
        return text;
    }

    ////////////////////////////////////////////////////////////////
    // internal methods?

    /**
     * @param generalizations the given collection of generalizations
     * @param impl
     * @return a string representing the g.
     */
    private String generateGeneralization(
        Collection generalizations,
        boolean impl) {
        Collection classes = new ArrayList();
        if (generalizations == null) {
            return "";
        }
        Iterator gens = generalizations.iterator();
        while (gens.hasNext()) {
            Object g = /*(MGeneralization)*/ gens.next();
            Object ge = Model.getFacade().getPowertype(g);
            // assert ge != null
            if (ge != null) {
                if (impl) {
                    if (Model.getFacade().isAInterface(ge)) {
                        classes.add(ge);
                    }
                } else if (!(Model.getFacade().isAInterface(ge))) {
                    classes.add(ge);
                }
            }
        }
        return generateClassList(classes);
    }

    /**
     * @param classifiers the given collection of classifiers
     * @return the names, separated by commas
     */
    public String generateClassList(Collection classifiers) {
        String s = "";
        if (classifiers == null) {
            return "";
        }
        Iterator clsEnum = classifiers.iterator();
        while (clsEnum.hasNext()) {
            s += generateClassifierRef(/*(MClass)*/ clsEnum.next());
            if (clsEnum.hasNext()) {
                s += ", ";
            }
        }
        return s;
    }

    /**
     * Returns a visibility String eihter for a VisibilityKind (according to
     * the definition in NotationProvider2), but also for a model element.
     * 
     * TODO: This function is replaced. Do not use it. 
     * 
     * @deprecated by MVW before 0.21.2. See issue 3546 and
     * the cookbook for the replacement architecture.
     * @see org.argouml.notation.NotationProvider2#generateVisibility(java.lang.Object)
     */
    public String generateVisibility(Object o) {
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
     * @param f the given feature
     * @return a string representing the scope of the feature or ""
     */
    public String generateScope(Object f) {
        Object scope = Model.getFacade().getOwnerScope(f);
        //if (scope == null) return "";
        if (Model.getScopeKind().getClassifier().equals(scope)) {
            return "static ";
        }
        return "";
    }

    /**
     * @param sf the given structural feature
     * @return a string representing the scope of the structural feature or ""
     */
    public String generateChangability(Object sf) {
        Object ck = Model.getFacade().getChangeability(sf);
        //if (ck == null) return "";
        if (Model.getChangeableKind().getFrozen().equals(ck)) {
            return "final ";
        }
        //if (Model.getFacade().ADD_ONLY_CHANGEABLEKIND.equals(ck))
        //    return "final ";
        return "";
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateMultiplicity(
     *          Object)
     */
    public String generateMultiplicity(Object m) {
        if (m == null || "1".equals(Model.getFacade().toString(m))) {
            return "";
        }
        return Model.getFacade().toString(m);
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateState(java.lang.Object)
     */
    public String generateState(Object m) {
        return Model.getFacade().getName(m);
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateSubmachine(java.lang.Object)
     */
    public String generateSubmachine(Object m) {
        Object c = Model.getFacade().getSubmachine(m);
        if (c == null) {
            return "include / ";
        }
        if (Model.getFacade().getName(c) == null) {
            return "include / ";
        }
        if (Model.getFacade().getName(c).length() == 0) {
            return "include / ";
        }
        return ("include / " + generateName(Model.getFacade().getName(c)));
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateObjectFlowState(java.lang.Object)
     */
    public String generateObjectFlowState(Object m) {
        // This method is never used.
        return "";
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateStateBody(java.lang.Object)
     */
    public String generateStateBody(Object m) {
        StringBuffer s = new StringBuffer();

        Object entryAction = Model.getFacade().getEntry(m);
        Object exitAction = Model.getFacade().getExit(m);
        Object doAction = Model.getFacade().getDoActivity(m);
        if (entryAction != null) {
            String entryStr = generate(entryAction);
            s.append("entry /").append(entryStr);
        }
        if (doAction != null) {
            String doStr = generate(doAction);
            if (s.length() > 0) {
                s.append("\n");
            }
            s.append("do /").append(doStr);

        }
        if (exitAction != null) {
            String exitStr = generate(exitAction);
            if (s.length() > 0) {
                s.append("\n");
            }
            s.append("exit /").append(exitStr);
        }
        Collection internaltrans = Model.getFacade().getInternalTransitions(m);
        if (internaltrans != null) {
            Iterator iter = internaltrans.iterator();
            while (iter.hasNext()) {
                if (s.length() > 0) {
                    s.append("\n");
                }
                Object trans = iter.next();
                s.append(generateTransition(trans));
            }
        }
        return s.toString();
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateTransition(java.lang.Object)
     */
    public String generateTransition(Object m) {
        String t = generate(Model.getFacade().getTrigger(m));
        String g = generate(Model.getFacade().getGuard(m));
        String e = generate(Model.getFacade().getEffect(m));
        if (g.length() > 0) {
            t += " [" + g + "]";
        }
        if (e.length() > 0) {
            t += " / " + e;
        }
        return t;
    }

    /**
     * @see org.argouml.notation.NotationProvider2#generateAction(java.lang.Object)
     */
    public String generateAction(Object m) {
        Collection c;
        Iterator it;
        String s;
        String p;
        boolean first;

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
                Object arg = /*(MArgument)*/ it.next();
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

    /**
     * @see org.argouml.notation.NotationProvider2#generateGuard(java.lang.Object)
     */
    public String generateGuard(Object m) {
        if (Model.getFacade().getExpression(m) != null) {
            return generateExpression(Model.getFacade().getExpression(m));
        }
        return "";
    }

    /**
     * Generates the text for a (trigger) event.
     *
     * @param m Object of any MEvent kind
     * @return the string representing the event
     *
     * @see org.argouml.notation.NotationProvider2#generateEvent(java.lang.Object)
     */
    public String generateEvent(Object m) {
        StringBuffer event = new StringBuffer();
        if (Model.getFacade().isAChangeEvent(m)) {
            event.append("when(");
            event.append(
                    generateExpression(Model.getFacade().getExpression(m)));
            event.append(")");
        } else if (Model.getFacade().isATimeEvent(m)) {
            event.append("after(");
            event.append(
                    generateExpression(Model.getFacade().getExpression(m)));
            event.append(")");
        } else if (Model.getFacade().isASignalEvent(m)) {
            event.append(generateName(Model.getFacade().getName(m)));
        } else if (Model.getFacade().isACallEvent(m)) {
            event.append(generateName(Model.getFacade().getName(m)));
            event.append(generateParameterList(m));
        }
        return event.toString();
    }

    /**
     * Generates a list of parameters. The parameters belong to the
     * given object.  The returned string will have the following
     * syntax:<p>
     *
     * (param1, param2, param3, ..., paramN)<p>
     *
     * If there are no parameters, then "()" is returned.
     *
     * @param parameterListOwner the 'owner' of the parameters
     * @return the generated parameter list
     */
    private String generateParameterList(Object parameterListOwner) {
        Iterator it =
            Model.getFacade().getParameters(parameterListOwner).iterator();
        StringBuffer list = new StringBuffer();
        list.append("(");
        if (it.hasNext()) {
            while (it.hasNext()) {
                Object param = it.next();
                list.append(generateParameter(param));
                if (it.hasNext()) {
                    list.append(", ");
                }
            }
        }
        list.append(")");
        return list.toString();
    }




    /**
     * @see org.argouml.notation.NotationProvider2#generateActionState(java.lang.Object)
     */
    public String generateActionState(Object actionState) {
        String ret = "";
        Object action = Model.getFacade().getEntry(actionState);
        if (action != null) {
            Object expression = Model.getFacade().getScript(action);
            if (expression != null) {
                ret = generateExpression(expression);
            }
        }
        return ret;
    }

    // public NotationName getNotation() {
    // return Notation.NOTATION_ARGO;
    // }

    /**
     * This function is never used, so I commented it out.
     *
     * @return
     */
    /*public boolean canParse() {
        return true;
    }*/

    /**
     * This function is never used, so I commented it out.
     *
     * @param o
     * @return
     */
    /*public boolean canParse(Object o) {
        return true;
    }*/

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() {
        return "GeneratorDisplay";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "Uml 1.3 Notation Generator";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() {
        return "ArgoUML Core";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() {
        return ArgoVersion.getVersion();
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() {
        return "module.language.uml.generator";
    }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] o) {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() { return true; }


} /* end class GeneratorDisplay */
