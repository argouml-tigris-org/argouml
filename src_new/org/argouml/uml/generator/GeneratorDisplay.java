// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import org.argouml.application.api.Notation;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;

/**
 * Generator2 subclass to generate code for display in diagrams in
 * text fields in the ArgoUML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for other languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. <p>
 *
 * TODO: always check for null!!!
 *
 * @stereotype singleton
 * @author jrobbins@ics.uci.edu
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
                "Uml",
                "1.3",
                Argo.lookupIconResource("UmlNotation")));
    }

    /**
     * <p>Generate the display for an extension point.</p>
     *
     * <p>The representation is "name: location". "name: " is omitted if there
     *   is no name given.</p>
     *
     * @param ep  The extension point.
     *
     * @return    The string representing the extension point.
     */

    public String generateExtensionPoint(Object ep) {

        // The string to build

        String s = "";

        // Get the fields we want

        String epName = ModelFacade.getName(ep);
        String epLocation = ModelFacade.getLocation(ep);

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
     *  @see org.argouml.application.api.NotationProvider2#generateOperation(
     *          Object, boolean)
     */
    public String generateOperation(Object op, boolean documented) {
        String stereoStr = generateStereotype(ModelFacade.getStereotypes(op));
        String visStr =
	    generateVisibility(op);
        String nameStr = generateName(ModelFacade.getName(op));

        // the parameters
        StringBuffer parameterListBuffer = new StringBuffer();
        Collection coll = ModelFacade.getParameters(op);
        Iterator it = coll.iterator();
        int counter = 0;
        while (it.hasNext()) {
            Object parameter = it.next();
            if (!ModelFacade.hasReturnParameterDirectionKind(parameter)) {
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
        coll = Model.getUmlHelper().getCore().getReturnParameters(op);
        StringBuffer returnParasSb = new StringBuffer();
        if (coll != null && coll.size() > 0) {
            returnParasSb.append(": ");
            Iterator it2 = coll.iterator();
            while (it2.hasNext()) {
                Object type = ModelFacade.getType(it2.next());
                if (type != null) {
                    returnParasSb.append(ModelFacade.getName(type));
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
        if (ModelFacade.isQuery(op)) {
            propertySb.append("query,");
        }
        if (ModelFacade.isRoot(op)) {
            propertySb.append("root,");
        }
        if (ModelFacade.isLeaf(op)) {
            propertySb.append("leaf,");
        }
        if (ModelFacade.getConcurrency(op) != null) {
            propertySb.append(ModelFacade.getName(
		    ModelFacade.getConcurrency(op)));
	    propertySb.append(',');
        }
        Iterator it3 = ModelFacade.getTaggedValues(op);
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
            genStr.append(visStr).append(" ");
        }
        if ((nameStr != null) && (nameStr.length() > 0)) {
            genStr.append(nameStr);
        }
        genStr.append(parameterStr).append(" ");
        if ((returnParasSb != null) && (returnParasSb.length() > 0)) {
            genStr.append(returnParasSb).append(" ");
        }
        if ((propertySb != null)
            && (propertySb.length() > 0)
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
     * initial value and properties are shown/not shown.
     *
     * @see org.argouml.application.api.NotationProvider2#generateAttribute(
     *          Object, boolean)
     */
    public String generateAttribute(Object attr, boolean documented) {
        String visibility = generateVisibility(attr);
        String stereo = generateStereotype(ModelFacade.getStereotypes(attr));
        //cat.debug("Stereotype: " + stereo);
        String name = ModelFacade.getName(attr);
        String multiplicity =
	    generateMultiplicity(ModelFacade.getMultiplicity(attr));
        String type = ""; // fix for loading bad projects
        if (ModelFacade.getType(attr) != null) {
            type = ModelFacade.getName(ModelFacade.getType(attr));
        }
        String initialValue = "";
        if (ModelFacade.getInitialValue(attr) != null) {
            initialValue =
		(String) ModelFacade.getBody(
			ModelFacade.getInitialValue(attr));
        }
        String changeableKind = "";
        if (ModelFacade.getChangeability(attr) != null) {
            if (ModelFacade.FROZEN_CHANGEABLEKIND.equals(
	            ModelFacade.getChangeability(attr))) {
                changeableKind = "frozen";
	    } else if (ModelFacade.ADD_ONLY_CHANGEABLEKIND.equals(
		    ModelFacade.getChangeability(attr))) {
                changeableKind = "addOnly";
	    }
        }
        StringBuffer properties = new StringBuffer();
        if (changeableKind.length() > 0) {
            properties.append("{ ").append(changeableKind).append(" }");
        }

        StringBuffer sb = new StringBuffer(20);
        if ((visibility != null)
            && (visibility.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY)) {
            sb.append(visibility).append(" ");
        }
        if ((stereo != null) && (stereo.length() > 0)) {
            sb.append(stereo).append(" ");
        }
        if ((name != null) && (name.length() > 0)) {
            sb.append(name).append(" ");
        }
        if ((multiplicity != null)
            && (multiplicity.length() > 0)
            && Configuration.getBoolean(Notation.KEY_SHOW_MULTIPLICITY)) {
            sb.append("[").append(multiplicity).append("]").append(" ");
        }
        if ((type != null) && (type.length() > 0)) {
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
     * @see org.argouml.application.api.NotationProvider2#generateParameter(java.lang.Object)
     */
    public String generateParameter(Object parameter) {
        StringBuffer s = new StringBuffer();
        s.append(generateKind(ModelFacade.getKind(parameter)));
        s.append(" ");
        s.append(generateName(ModelFacade.getName(parameter)));
        String classRef = generateClassifierRef(ModelFacade.getType(parameter));
        if (classRef.length() > 0) {
            s.append(" : ");
            s.append(classRef);
        }
        String defaultValue =
	    generateExpression(ModelFacade.getDefaultValue(parameter));
        if (defaultValue.length() > 0) {
            s.append(" = ");
            s.append(defaultValue);
        }
        return s.toString();
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generatePackage(java.lang.Object)
     */
    public String generatePackage(Object p) {
        String s = "package ";
        String packName = generateName(ModelFacade.getName(p));

        java.util.Stack stack = new java.util.Stack();
        Object ns = ModelFacade.getNamespace(p);
        while (ns != null) {
            stack.push(ModelFacade.getName(ns));
            ns = ModelFacade.getNamespace(ns);
        }
        while (!stack.isEmpty())
            s += (String) stack.pop() + ".";

        if (s.endsWith(".")) {
            int lastIndex = s.lastIndexOf(".");
            s = s.substring(0, lastIndex);
        }
        s += "." + packName + " {\n";

        Collection ownedElements = ModelFacade.getOwnedElements(p);
        if (ownedElements != null) {
            Iterator ownedEnum = ownedElements.iterator();
            while (ownedEnum.hasNext()) {
                s += generate(/*(MModelElement)*/ ownedEnum.next());
                s += "\n\n";
            }
        } else {
            s += "(no elements)";
        }
        s += "\n}\n";
        return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateClassifier(java.lang.Object)
     */
    public String generateClassifier(Object cls) {
        String generatedName = generateName(ModelFacade.getName(cls));
        String classifierKeyword;
        if (org.argouml.model.ModelFacade.isAClass(cls))
            classifierKeyword = "class";
        else if (org.argouml.model.ModelFacade.isAInterface(cls))
            classifierKeyword = "interface";
        else
            return ""; // actors and use cases
        String s = "";
        s += generateVisibility(cls);
        if (ModelFacade.isAbstract(cls))
            s += "abstract ";
        if (ModelFacade.isLeaf(cls))
            s += "final ";
        s += classifierKeyword + " " + generatedName + " ";
        String baseClass =
            generateGeneralization(ModelFacade.getGeneralizations(cls), false);
        if (!baseClass.equals(""))
            s += "extends " + baseClass + " ";

        //nsuml: realizations!  String interfaces =
        //generateRealization(cls.getRealizations(), true); if
        //(!interfaces.equals("")) s += "implements " + interfaces + "
        //";
        s += "{\n";

        Collection strs = ModelFacade.getStructuralFeatures(cls);
        if (strs != null) {
            s += "\n";
            s += INDENT + "// Attributes\n";
            Iterator strEnum = strs.iterator();
            while (strEnum.hasNext())
                s += INDENT + generate(strEnum.next()) + ";\n";
        }

        Collection ends = ModelFacade.getAssociationEnds(cls);
        if (ends != null) {
            s += "\n";
            s += INDENT + "// Associations\n";
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                Object ae = /*(MAssociationEnd)*/ endEnum.next();
                Object a = ModelFacade.getAssociation(ae);
                s += INDENT + generateAssociationFrom(a, ae);
            }
        }

        // TODO: constructors

        Collection behs = ModelFacade.getOperations(cls);
        if (behs != null) {
            s += "\n";
            s += INDENT + "// Operations\n";
            Iterator behEnum = behs.iterator();
            String terminator = " {\n" + INDENT + "}";
            if (org.argouml.model.ModelFacade.isAInterface(cls))
                terminator = ";";
            while (behEnum.hasNext())
                s += INDENT + generate(behEnum.next()) + terminator + "\n";
        }
        s += "\n";
        s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
        return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateTaggedValue(java.lang.Object)
     */
    public String generateTaggedValue(Object tv) {
        if (tv == null)
            return "";
        return generateName(ModelFacade.getTagOfTag(tv))
            + "="
            + generateUninterpreted(ModelFacade.getValueOfTag(tv));
    }

    /**
     * Generates the textual number of MMessage m. The number is a string
     * of numbers separated by points which describes the message's order
     * and level in a collaboration.
     *
     * <p>If you plan to modify this number, make sure that
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
        if (kind == null || kind == ModelFacade.IN_PARAMETERDIRECTIONKIND) {
            s.append("in");
        } else if (kind == ModelFacade.INOUT_PARAMETERDIRECTIONKIND) {
            s.append("inout");
        } else if (kind == ModelFacade.RETURN_PARAMETERDIRECTIONKIND) {
            ;// return nothing
        } else if (kind == ModelFacade.OUT_PARAMETERDIRECTIONKIND) {
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

        if (m == null)
            return null;

        act = ModelFacade.getActivator(m);
        if (act != null)
            mname = generateMessageNumber(act);

        if (pre != null) {
            c = ModelFacade.getMessages3(pre);
            submax = c.size();
            it = c.iterator();
            while (it.hasNext() && it.next() != m)
                subpos++;
        }

        if (mname.length() > 0) {
            if (submax > 1)
                return mname + "." + position + (char) ('a' + subpos);
            return mname + "." + position;
        }

        if (submax > 1)
            return Integer.toString(position) + (char) ('a' + subpos);
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

        act = ModelFacade.getActivator(message);
        c = ModelFacade.getPredecessors(message);
        it = c.iterator();
        while (it.hasNext()) {
            Object msg = /*(MMessage)*/ it.next();
            if (ModelFacade.getActivator(msg) != act)
                continue;
            int p = recCountPredecessors(msg, null) + 1;
            if (p > pre) {
                pre = p;
                maxmsg = msg;
            }
            local++;
        }

        if (ptr != null)
            ptr.message = maxmsg;

        return Math.max(pre, local);
    }

    int countSuccessors(Object/*MMessage*/ m) {
        int count = 0;
        Object act = ModelFacade.getActivator(m);
        Collection coll = ModelFacade.getMessages3(m);
        if (coll != null) {
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                if (ModelFacade.getActivator(msg) != act)
                    continue;
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
        if (expr == null)
            return "";

        return ModelFacade.getBody(expr).toString();
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

        if (m == null)
            return "";

        ptr = new MsgPtr();
        lpn = recCountPredecessors(m, ptr) + 1;
        rt = ModelFacade.getActivator(m);

        pre = ModelFacade.getPredecessors(m);
        it = (pre != null) ? pre.iterator() : null;
        if (it != null && it.hasNext()) {
            MsgPtr ptr2 = new MsgPtr();
            int precnt = 0;

            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                int mpn = recCountPredecessors(msg, ptr2) + 1;

                if (mpn == lpn - 1
                    && rt == ModelFacade.getActivator(msg)
                    && ModelFacade.getPredecessors(msg).size() < 2
                    && (ptr2.message == null
                        || countSuccessors(ptr2.message) < 2)) {
                    continue;
                }

                if (predecessors.length() > 0)
                    predecessors += ", ";
                predecessors += generateMessageNumber(msg, ptr2.message, mpn);
                precnt++;
            }

            if (precnt > 0)
                predecessors += " / ";
        }

        number = generateMessageNumber(m, ptr.message, lpn);

        act = ModelFacade.getAction(m);
        if (act != null) {
            if (ModelFacade.getRecurrence(act) != null)
                number =
		    generateRecurrence(ModelFacade.getRecurrence(act))
		    + " "
		    + number;

            action = generateAction(act);
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
        Collection connections = ModelFacade.getConnections(a);
        if (connections == null)
            return s;
        Iterator connEnum = connections.iterator();
        while (connEnum.hasNext()) {
            Object ae2 = /*(MAssociationEnd)*/ connEnum.next();
            if (ae2 != ae)
                s += generateAssociationEnd(ae2);
        }
        return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociation(java.lang.Object)
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
     * @see org.argouml.application.api.NotationProvider2#generateAssociationEnd(java.lang.Object)
     */
    public String generateAssociationEnd(Object ae) {
        if (!ModelFacade.isNavigable(ae))
            return "";
        String s = "protected ";
        if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(
		ModelFacade.getTargetScope(ae))) {
            s += "static ";
	}

        //     String n = ae.getName(); if (n != null &&
        //     !String.UNSPEC.equals(n)) s += generateName(n) + " ";
        //     if (ModelFacade.isNavigable(ae)) s += "navigable "; if
        //     (ae.getIsOrdered()) s += "ordered ";
        Object m = ModelFacade.getMultiplicity(ae);
        if (ModelFacade.M1_1_MULTIPLICITY.equals(m)
	        || ModelFacade.M0_1_MULTIPLICITY.equals(m)) {
            s += generateClassifierRef(ModelFacade.getType(ae));
	} else {
            s += "Vector "; //generateMultiplicity(m) + " ";
	}

        s += " ";

        String n = ModelFacade.getName(ae);
        Object asc = ModelFacade.getAssociation(ae);
        String ascName = ModelFacade.getName(asc);
        if (n != null && n != null && n.length() > 0) {
            s += generateName(n);
        } else if (
            ascName != null && ascName != null && ascName.length() > 0) {
            s += generateName(ascName);
        } else {
            s += "my" + generateClassifierRef(ModelFacade.getType(ae));
        }

        return s + ";\n";
    }

    /**
     * @param me the given ModelElement
     * @return a string representing the constraints for the given modelelements
     */
    public String generateConstraints(Object me) {
        Collection constr = ModelFacade.getConstraints(me);
        if (constr == null || constr.size() == 0)
            return "";
        String s = "{";
        Iterator conEnum = constr.iterator();
        while (conEnum.hasNext()) {
            s += generateConstraint(conEnum.next());
            if (conEnum.hasNext())
                s += "; ";
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
     *  / name : name of the base association
     *
     * @param assocRole the given associationrole
     * @return the generated name
     *
     * @see org.argouml.application.api.NotationProvider2#generateAssociationRole(java.lang.Object)
     */
    public String generateAssociationRole(Object assocRole) {
        //get the associationRole name
        String text = "/" + ModelFacade.getName(assocRole) + ":";
        //get the base association name
        Object assoc = ModelFacade.getBase(assocRole);
        if (assoc != null) {
            text = text + ModelFacade.getName(assoc);
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
        if (generalizations == null)
            return "";
        Iterator gens = generalizations.iterator();
        while (gens.hasNext()) {
            Object g = /*(MGeneralization)*/ gens.next();
            Object ge = ModelFacade.getPowertype(g);
            // assert ge != null
            if (ge != null) {
                if (impl) {
                    if (ModelFacade.isAInterface(ge)) {
                        classes.add(ge);
                    }
                } else if (!(org.argouml.model.ModelFacade.isAInterface(ge))) {
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
        if (classifiers == null)
            return "";
        Iterator clsEnum = classifiers.iterator();
        while (clsEnum.hasNext()) {
            s += generateClassifierRef(/*(MClass)*/ clsEnum.next());
            if (clsEnum.hasNext())
                s += ", ";
        }
        return s;
    }

    /** Returns a visibility String eihter for a MVisibilityKind (according to
     * the definition in NotationProvider2), but also for a model element.
     * @see org.argouml.application.api.NotationProvider2#generateVisibility(java.lang.Object)
     */
    public String generateVisibility(Object o) {
        if (o == null)
            return "";
        if (ModelFacade.isAModelElement(o)) {
            if (ModelFacade.isPublic(o))
                return "+";
            if (ModelFacade.isPrivate(o))
                return "-";
            if (ModelFacade.isProtected(o))
                return "#";
        }
        if (ModelFacade.isAVisibilityKind(o)) {
            if (ModelFacade.PUBLIC_VISIBILITYKIND.equals(o))
                return "+";
            if (ModelFacade.PRIVATE_VISIBILITYKIND.equals(o))
                return "-";
            if (ModelFacade.PROTECTED_VISIBILITYKIND.equals(o))
                return "#";
        }
        return "";
    }

    /**
     * @param f the given feature
     * @return a string representing the scope of the feature or ""
     */
    public String generateScope(Object f) {
        Object scope = ModelFacade.getOwnerScope(f);
        //if (scope == null) return "";
        if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(scope))
            return "static ";
        return "";
    }

    /**
     * @param sf the given structural feature
     * @return a string representing the scope of the structural feature or ""
     */
    public String generateChangability(Object sf) {
        Object ck = ModelFacade.getChangeability(sf);
        //if (ck == null) return "";
        if (ModelFacade.FROZEN_CHANGEABLEKIND.equals(ck))
            return "final ";
        //if (ModelFacade.ADD_ONLY_CHANGEABLEKIND.equals(ck)) return "final ";
        return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateMultiplicity(
     *          Object)
     */
    public String generateMultiplicity(Object m) {
        if (m == null) {
            return "";
        }
        if (ModelFacade.M0_N_MULTIPLICITY.equals(m))
            return ANY_RANGE;
        String s = "";
        Iterator rangeIter = ModelFacade.getRanges(m);
        if (rangeIter == null)
            return s;
        boolean first = true;
        while (rangeIter.hasNext()) {
            Object mr = rangeIter.next();
            if (!(ModelFacade.getLower(mr) == 1
		  && ModelFacade.getUpper(mr) == 1
		  && first
		  && !rangeIter.hasNext())) {
                s += generateMultiplicityRange(mr);
                s += ",";
            }
            first = false;
        }
        if (s.length() > 0 && s.lastIndexOf(',') == s.length() - 1) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * <code>ANY_RANGE</code> stands for "0..*"
     */
    public static final String ANY_RANGE = "0..*";

    /**
     * Generates a multiplicity range. The standard getLower and
     * getUpper defined on MMultiplicityRange give a -1 if the
     * multiplicity is n or *. This method circumvents that behaviour.
     * @param mr the given MultiplicityRange object
     * @return String
     */
    //public static final String ANY_RANGE = "*";
    // TODO: user preference between "*" and "0..*"

    protected String generateMultiplicityRange(Object mr) {
        int lower = ModelFacade.getLower(mr);
        String lowerStr = "" + lower;
        int upper = ModelFacade.getUpper(mr);
        String upperStr = "" + upper;
        if (lower == -1) {
            lowerStr = "*";
        }
        if (upper == -1) {
            upperStr = "*";
        }
        if (lower == upper)
            return lowerStr;
        return lowerStr + ".." + upperStr;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateState(java.lang.Object)
     */
    public String generateState(Object m) {
        return ModelFacade.getName(m);
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateObjectFlowState(java.lang.Object)
     */
    public String generateObjectFlowState(Object m) {
        Object c = ModelFacade.getType(m);
        if (c == null) return "";
        return ModelFacade.getName(c);
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateStateBody(java.lang.Object)
     */
    public String generateStateBody(Object m) {
        StringBuffer s = new StringBuffer();

        Object entryAction = ModelFacade.getEntry(m);
        Object exitAction = ModelFacade.getExit(m);
        Object doAction = ModelFacade.getDoActivity(m);
        if (entryAction != null) {
            String entryStr = generate(entryAction);
            s.append("entry /").append(entryStr);
        }
        if (doAction != null) {
            String doStr = generate(doAction);
            if (s.length() > 0)
                s.append("\n");
            s.append("do /").append(doStr);

        }
        if (exitAction != null) {
            String exitStr = generate(exitAction);
            if (s.length() > 0)
                s.append("\n");
            s.append("exit /").append(exitStr);
        }
        Collection internaltrans = ModelFacade.getInternalTransitions(m);
        if (internaltrans != null) {
            Iterator iter = internaltrans.iterator();
            while (iter.hasNext()) {
                if (s.length() > 0)
                    s.append("\n");
                Object trans = /*(MTransition)*/ iter.next();
                s.append(/*ModelFacade.getName(trans)).append(" /").append(*/
                    generateTransition(trans));
            }
        }
        return s.toString();
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateTransition(java.lang.Object)
     */
    public String generateTransition(Object m) {
        String t = generate(ModelFacade.getTrigger(m));
        String g = generate(ModelFacade.getGuard(m));
        String e = generate(ModelFacade.getEffect(m));
        if (g.length() > 0)
            t += " [" + g + "]";
        if (e.length() > 0)
            t += " / " + e;
        return t;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAction(java.lang.Object)
     */
    public String generateAction(Object m) {
        Collection c;
        Iterator it;
        String s;
        String p;
        boolean first;

        Object script = ModelFacade.getScript(m);

        if ((script != null) && (ModelFacade.getBody(script) != null))
            s = ModelFacade.getBody(script).toString();
        else
            s = "";

        p = "";
        c = ModelFacade.getActualArguments(m);
        if (c != null) {
            it = c.iterator();
            first = true;
            while (it.hasNext()) {
                Object arg = /*(MArgument)*/ it.next();
                if (!first)
                    p += ", ";

                if (ModelFacade.getValue(arg) != null) {
                    p += generateExpression(ModelFacade.getValue(arg));
		}
                first = false;
            }
        }
        if (s.length() == 0 && p.length() == 0)
            return "";

        /* If there are no arguments, then do not show the ().
         * This solves issue 1758.
         * Arguments are not supported anyhow in the UI yet.
         * These brackets are easily confused with the brackets
         * for the Operation of a CallAction.
         */
        if (p.length() == 0) return s;

        return s + " (" + p + ")";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateGuard(java.lang.Object)
     */
    public String generateGuard(Object m) {
        if (ModelFacade.getExpression(m) != null)
            return generateExpression(ModelFacade.getExpression(m));
        return "";
    }

    /**
     * Generates the text for a (trigger) event.
     *
     * @param m Object of any MEvent kind
     * @return the string representing the event
     *
     * @see org.argouml.application.api.NotationProvider2#generateEvent(java.lang.Object)
     */
    public String generateEvent(Object m) {
        StringBuffer event = new StringBuffer();
        if (ModelFacade.isAChangeEvent(m)) {
            event.append("when(");
            event.append(generateExpression(ModelFacade.getExpression(m)));
            event.append(")");
        } else if (ModelFacade.isATimeEvent(m)) {
            event.append("after(");
            event.append(generateExpression(ModelFacade.getExpression(m)));
            event.append(")");
        } else if (ModelFacade.isASignalEvent(m)) {
            event.append(generateName(ModelFacade.getName(m)));
        } else if (ModelFacade.isACallEvent(m)) {
            event.append(generateName(ModelFacade.getName(
                    ModelFacade.getOperation(m))));
            event.append(generateParameterList(m));
        }
        return event.toString();
    }

    /**
     * Generates a list of parameters. The parameters belong to the
     * given object.  The returned string will have the following
     * syntax:<p>
     *
     * (param1, param2, param3, ..., paramN)
     *
     * <p> If there are no parameters, then "()" is returned.
     *
     * @param parameterListOwner the 'owner' of the parameters
     * @return the generated parameter list
     */
    private String generateParameterList(Object parameterListOwner) {
        Iterator it = ModelFacade.getParameters(parameterListOwner).iterator();
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
     * @see org.argouml.application.api.NotationProvider2#generateActionState(java.lang.Object)
     */
    public String generateActionState(Object actionState) {
        String ret = "";
        Object action = ModelFacade.getEntry(actionState);
        if (action != null) {
            Object expression = ModelFacade.getScript(action);
            if (expression != null)
                ret = generateExpression(expression);
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
