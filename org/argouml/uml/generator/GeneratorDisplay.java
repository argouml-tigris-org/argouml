// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: GeneratorDisplay.java
// Classes: GeneratorDisplay
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 5 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Return text for
// operations that have no return parameter made "" rather than ": void??"

// 10 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// extension points.

package org.argouml.uml.generator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MArgument;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced.
 * @stereotype singleton
 */

// TODO: always check for null!!!

public class GeneratorDisplay extends Generator implements PluggableNotation {

    private static GeneratorDisplay SINGLETON;

    public static GeneratorDisplay getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new GeneratorDisplay();
        }
        return SINGLETON;
    }

    private GeneratorDisplay() {
        super(Notation.makeNotation("Uml", "1.3", Argo.lookupIconResource("UmlNotation")));
    }

    public static String Generate(Object o) {
        return SINGLETON.generate(o);
    }

    /**
     * <p>Generate the display for an extension point.</p>
     *
     * <p>The representation is "name: location". "name :" is omitted if there
     *   is no name given.</p>
     *
     * @param ep  The extension point.
     *
     * @return    The string representing the extension point.
     */

    public String generateExtensionPoint(MExtensionPoint ep) {

        // The string to build

        String s = "";

        // Get the fields we want

        String epName = ep.getName();
        String epLocation = ep.getLocation();

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

    /*
    public String generateConcurrency(MCallConcurrencyKind concurrency) {
    	concurrency.ge
    */
    /**
     * Generates an operation accordin to the UML 1.3 notation:
     * stereotype visibility name (parameter-list) : return-type-expression {property-string}
     * For the return-type-expression: only the types of the return parameters are shown.
     * Depending on settings in Notation visibility and properties are shown/not shown.
     * @author jaap.branderhorst@xs4all.nl
     * @see org.argouml.application.api.NotationProvider#generateOperation(MOperation, boolean)
     */
    public String generateOperation(MOperation op, boolean documented) {
        String stereoStr = generateStereotype(op.getStereotype());
        String visStr = generateVisibility(op.getVisibility());
        String nameStr = generateName(op.getName());

        // the parameters
        StringBuffer parameterListBuffer = new StringBuffer();
        Collection parameters = op.getParameters();
        Iterator it = parameters.iterator();
        int counter = 0;
        while (it.hasNext()) {
            MParameter parameter = (MParameter) it.next();
            if (!parameter.getKind().equals(MParameterDirectionKind.RETURN)) {
                counter++;
                parameterListBuffer.append(generateParameter(parameter)).append(",");
            }
        }
        if (counter > 0) {
            parameterListBuffer.delete(parameterListBuffer.length() - 1, parameterListBuffer.length());
        }
        String parameterStr = "(" + parameterListBuffer.toString() + ")";

        // the returnparameters
        Collection returnParas = UmlHelper.getHelper().getCore().getReturnParameters(op);
        StringBuffer returnParasSb = new StringBuffer();
        if (returnParas.size() > 0) {
            returnParasSb.append(": ");
            Iterator it2 = returnParas.iterator();
            while (it2.hasNext()) {
                MParameter param = (MParameter) it2.next();
                if (param.getType() != null) {
                    returnParasSb.append(param.getType().getName());
                }
                returnParasSb.append(",");
            }
            returnParasSb.delete(returnParasSb.length() - 1, returnParasSb.length());
        }
        String returnParasStr = returnParasSb.toString();

        // the properties
        StringBuffer propertySb = new StringBuffer().append("{");
        // the query state
        if (op.isQuery()) {
            propertySb.append("query,");
        }
        if (op.isRoot()) {
            propertySb.append("root,");
        }
        if (op.isLeaf()) {
            propertySb.append("leaf,");
        }
        if (op.getConcurrency() != null) {
            propertySb.append(op.getConcurrency().getName().toString()).append(",");
        }
        Collection taggedValues = op.getTaggedValues();
        StringBuffer taggedValuesSb = new StringBuffer();
        if (taggedValues.size() > 0) {
            Iterator it3 = taggedValues.iterator();
            while (it3.hasNext()) {
                taggedValuesSb.append(generateTaggedValue((MTaggedValue) it3.next()));
                taggedValuesSb.append(",");
            }
            taggedValuesSb.delete(taggedValuesSb.length() - 1, taggedValuesSb.length());
        }
        if (propertySb.length() > 1) {
            propertySb.delete(propertySb.length() - 1, propertySb.length()); // remove last ,
            propertySb.append("}");
        } else {
            propertySb = new StringBuffer();
        }
        String propertiesStr = propertySb.toString();

        // lets concatenate it to the resulting string (genStr)
        StringBuffer genStr = new StringBuffer();
        if ((stereoStr != null) && (stereoStr.length() > 0)) {
            genStr.append(stereoStr).append(" ");
        }
        if ((visStr != null) && (visStr.length() > 0) && Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY)) {
            genStr.append(visStr).append(" ");
        }
        if ((nameStr != null) && (nameStr.length() > 0)) {
            genStr.append(nameStr);
        }
        genStr.append(parameterStr).append(" ");
        if ((returnParasStr != null) && (returnParasStr.length() > 0)) {
            genStr.append(returnParasStr).append(" ");
        }
        if ((propertiesStr != null) && (propertiesStr.length() > 0) && Configuration.getBoolean(Notation.KEY_SHOW_PROPERTIES)) {
            genStr.append(propertiesStr);
        }
        return genStr.toString().trim();
    }

    /**
     * Generates a string representation for the provided attribute. The string
     * representation will be of the form:
     * visibility name [multiplicity] : type-expression = initial-value {property-string}
     * Depending on settings in Notation visibility,
     * multiplicity, initial value and properties are shown/not shown.
     * @see org.argouml.application.api.NotationProvider#generateAttribute(MAttribute, boolean)
     */
    public String generateAttribute(MAttribute attr, boolean documented) {
        String visibility = generateVisibility(attr.getVisibility());
        String stereo = generateStereotype(attr.getStereotype());
        cat.debug("Stereotype: " + stereo);
        String name = attr.getName();
        String multiplicity = generateMultiplicity(attr.getMultiplicity());
        String type = ""; // fix for loading bad projects
        if (attr.getType() != null) {
            type = attr.getType().getName();
        }
        String initialValue = "";
        if (attr.getInitialValue() != null) {
            initialValue = attr.getInitialValue().getBody();
        }
        String changeableKind = "";
        if (attr.getChangeability() != null) {
            if (attr.getChangeability().equals(MChangeableKind.FROZEN))
                changeableKind = "frozen";
            else if (attr.getChangeability().equals(MChangeableKind.ADD_ONLY))
                changeableKind = "addOnly";
        }
        String properties = "";
        if (changeableKind.length() > 0) {
            properties = "{ " + changeableKind + " }";
        }

        StringBuffer sb = new StringBuffer();
        if ((visibility != null) && (visibility.length() > 0) && Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY)) {
            sb.append(visibility).append(" ");
        }
        if ((stereo != null) && (stereo.length() > 0)) {
            sb.append(stereo).append(" ");
        }
        if ((name != null) && (name.length() > 0)) {
            sb.append(name).append(" ");
        }
        if ((multiplicity != null) && (multiplicity.length() > 0) && Configuration.getBoolean(Notation.KEY_SHOW_MULTIPLICITY)) {
            sb.append("[").append(multiplicity).append("]").append(" ");
        }
        if ((type != null) && (type.length() > 0)) {
            sb.append(": ").append(type).append(" ");
        }
        if ((initialValue != null) && (initialValue.length() > 0) && Configuration.getBoolean(Notation.KEY_SHOW_INITIAL_VALUE)) {
            sb.append(" = ").append(initialValue).append(" ");
        }
        if ((properties.length() > 0) && Configuration.getBoolean(Notation.KEY_SHOW_PROPERTIES)) {
            sb.append(properties);
        }
        return sb.toString().trim();

    }

    public String generateParameter(MParameter param) {
        String s = "";
        //TODO: qualifiers (e.g., const)
        //TODO: stereotypes...
        s += generateName(param.getName()) + ": ";
        s += generateClassifierRef(param.getType());
        //TODO: initial value
        return s;
    }

    public String generatePackage(MPackage p) {
        String s = "package ";
        String packName = generateName(p.getName());

        java.util.Stack stack = new java.util.Stack();
        MNamespace ns = p.getNamespace();
        while (ns != null) {
            stack.push(ns.getName());
            ns = ns.getNamespace();
        }
        while (!stack.isEmpty())
            s += (String) stack.pop() + ".";

        if (s.endsWith(".")) {
            int lastIndex = s.lastIndexOf(".");
            s = s.substring(0, lastIndex);
        }
        s += "." + packName + " {\n";

        Collection ownedElements = p.getOwnedElements();
        if (ownedElements != null) {
            Iterator ownedEnum = ownedElements.iterator();
            while (ownedEnum.hasNext()) {
                s += generate((MModelElement) ownedEnum.next());
                s += "\n\n";
            }
        } else {
            s += "(no elements)";
        }
        s += "\n}\n";
        return s;
    }

    public String generateClassifier(MClassifier cls) {
        String generatedName = generateName(cls.getName());
        String classifierKeyword;
        if (cls instanceof MClass)
            classifierKeyword = "class";
        else if (cls instanceof MInterface)
            classifierKeyword = "interface";
        else
            return ""; // actors and use cases
        String s = "";
        s += generateVisibility(cls.getVisibility());
        if (cls.isAbstract())
            s += "abstract ";
        if (cls.isLeaf())
            s += "final ";
        s += classifierKeyword + " " + generatedName + " ";
        String baseClass = generateGeneralization(cls.getGeneralizations(), false);
        if (!baseClass.equals(""))
            s += "extends " + baseClass + " ";

        //nsuml: realizations!
        //     String interfaces = generateRealization(cls.getRealizations(), true);
        //     if (!interfaces.equals("")) s += "implements " + interfaces + " ";
        s += "{\n";

        Collection strs = ModelFacade.getStructuralFeatures(cls);
        if (strs != null) {
            s += "\n";
            //s += "////////////////////////////////////////////////////////////////\n";
            s += INDENT + "// Attributes\n";
            Iterator strEnum = strs.iterator();
            while (strEnum.hasNext())
                s += INDENT + generate(strEnum.next()) + ";\n";
        }

        Collection ends = cls.getAssociationEnds();
        if (ends != null) {
            s += "\n";
            //s += "////////////////////////////////////////////////////////////////\n";
            s += INDENT + "// Associations\n";
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                MAssociationEnd ae = (MAssociationEnd) endEnum.next();
                MAssociation a = ae.getAssociation();
                s += INDENT + generateAssociationFrom(a, ae);
            }
        }

        // TODO: constructors

        Collection behs = ModelFacade.getOperations(cls);
        if (behs != null) {
            s += "\n";
            //s += "////////////////////////////////////////////////////////////////\n";
            s += INDENT + "// Operations\n";
            Iterator behEnum = behs.iterator();
            String terminator = " {\n" + INDENT + "}";
            if (cls instanceof MInterface)
                terminator = ";";
            while (behEnum.hasNext())
                s += INDENT + generate(behEnum.next()) + terminator + "\n";
        }
        s += "\n";
        s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
        return s;
    }

    public String generateTaggedValue(MTaggedValue tv) {
        if (tv == null)
            return "";
        return generateName(tv.getTag()) + "=" + generateUninterpreted(tv.getValue());
    }

    /**
     * Generates the textual number of MMessage m. The number is a string
     * of numbers separated by points which describes the message's order
     * and level in a collaboration.
     *
     * <p>If you plan to modify this number, make sure that
     * ParserDisplay.parseMessage is adapted to the change.
     *
     * @param m A MMessage to generate the number for.
     * @return A String with the message number of m.
     */
    public String generateMessageNumber(MMessage m) {
        MsgPtr ptr = new MsgPtr();
        int pos = recCountPredecessors(m, ptr) + 1;
        return generateMessageNumber(m, ptr.message, pos);
    }

    private String generateMessageNumber(MMessage m, MMessage pre, int position) {
        Collection c;
        Iterator it;
        String mname = "";
        MMessage act;
        int subpos = 0, submax = 1;

        if (m == null)
            return null;

        act = m.getActivator();
        if (act != null)
            mname = generateMessageNumber(act);

        if (pre != null) {
            c = pre.getMessages3();
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
        public MMessage message;
    }

    int recCountPredecessors(MMessage m, MsgPtr ptr) {
        Collection c;
        Iterator it;
        int pre = 0;
        int local = 0;
        MMessage maxmsg = null;
        MMessage act;

        if (m == null) {
            ptr.message = null;
            return 0;
        }

        act = m.getActivator();
        c = m.getPredecessors();
        it = c.iterator();
        while (it.hasNext()) {
            MMessage msg = (MMessage) it.next();
            if (msg.getActivator() != act)
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

    int countSuccessors(MMessage m) {
        MMessage act = m.getActivator();
        Iterator it = m.getMessages3().iterator();
        int count = 0;
        while (it.hasNext()) {
            MMessage msg = (MMessage) it.next();
            if (msg.getActivator() != act)
                continue;
            count++;
        }
        return count;
    }

    /**
     * Generates a textual description of a MIterationExpression.
     */
    public String generateRecurrence(MIterationExpression expr) {
        if (expr == null)
            return "";

        return expr.getBody();
    }

    /**
     * Generates a textual description for a MMessage m.
     *
     * @param m A MMessage to generate a description for.
     * @return A String suitable to show in a collaboration diagram.
     */
    public String generateMessage(MMessage m) {
        Iterator it;
        Collection pre;
        MAction act;
        MMessage rt;
        MsgPtr ptr;

        String action = "";
        String number;
        String predecessors = "";
        int lpn;

        if (m == null)
            return "";

        ptr = new MsgPtr();
        lpn = recCountPredecessors(m, ptr) + 1;
        rt = m.getActivator();

        pre = m.getPredecessors();
        it = pre.iterator();
        if (it.hasNext()) {
            MsgPtr ptr2 = new MsgPtr();
            int precnt = 0;

            while (it.hasNext()) {
                MMessage msg = (MMessage) it.next();
                int mpn = recCountPredecessors(msg, ptr2) + 1;

                if (mpn == lpn - 1 && rt == msg.getActivator() && msg.getPredecessors().size() < 2 && (ptr2.message == null || countSuccessors(ptr2.message) < 2)) {
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

        act = m.getAction();
        if (act != null) {
            if (act.getRecurrence() != null)
                number = generateRecurrence(act.getRecurrence()) + " " + number;

            action = generateAction(act);
        }

        return predecessors + number + " : " + action;
    }

    public String generateAssociationFrom(MAssociation a, MAssociationEnd ae) {
        // TODO: does not handle n-ary associations
        String s = "";
        Collection connections = a.getConnections();
        Iterator connEnum = connections.iterator();
        while (connEnum.hasNext()) {
            MAssociationEnd ae2 = (MAssociationEnd) connEnum.next();
            if (ae2 != ae)
                s += generateAssociationEnd(ae2);
        }
        return s;
    }

    public String generateAssociation(MAssociation a) {
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

    public String generateAssociationEnd(MAssociationEnd ae) {
        if (!ae.isNavigable())
            return "";
        String s = "protected ";
        if (MScopeKind.CLASSIFIER.equals(ae.getTargetScope()))
            s += "static ";
        //     String n = ae.getName();
        //     if (n != null && !String.UNSPEC.equals(n)) s += generateName(n) + " ";
        //     if (ae.isNavigable()) s += "navigable ";
        //     if (ae.getIsOrdered()) s += "ordered ";
        MMultiplicity m = ae.getMultiplicity();
        if (MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals(m))
            s += generateClassifierRef(ae.getType());
        else
            s += "Vector "; //generateMultiplicity(m) + " ";

        s += " ";

        String n = ae.getName();
        MAssociation asc = ae.getAssociation();
        String ascName = asc.getName();
        if (n != null && n != null && n.length() > 0) {
            s += generateName(n);
        } else if (ascName != null && ascName != null && ascName.length() > 0) {
            s += generateName(ascName);
        } else {
            s += "my" + generateClassifierRef(ae.getType());
        }

        return s + ";\n";
    }

    public String generateConstraints(MModelElement me) {
        Collection constr = me.getConstraints();
        if (constr == null || constr.size() == 0)
            return "";
        String s = "{";
        Iterator conEnum = constr.iterator();
        while (conEnum.hasNext()) {
            s += generateConstraint((MConstraint) conEnum.next());
            if (conEnum.hasNext())
                s += "; ";
        }
        s += "}";
        return s;
    }

    public String generateConstraint(MConstraint c) {
        return generateExpression(c);
    }

    /**
     * generate the name of an association role of the form:
     *  / name : name of the base association
     * @return the generated name
     **/
    public String generateAssociationRole(MAssociationRole assocRole) {
        //get the associationRole name
        String text = "/" + assocRole.getName() + ":";
        //get the base association name
        MAssociation assoc = assocRole.getBase();
        if (assoc != null) {
            text = text + assoc.getName();
        }
        return text;
    }

    ////////////////////////////////////////////////////////////////
    // internal methods?

    public String generateGeneralization(Collection generalizations, boolean impl) {
        Collection classes = new ArrayList();
        if (generalizations == null)
            return "";
        Iterator enum = generalizations.iterator();
        while (enum.hasNext()) {
            MGeneralization g = (MGeneralization) enum.next();
            MGeneralizableElement ge = g.getPowertype();
            // assert ge != null
            if (ge != null) {
                if (impl) {
                    if (ge instanceof MInterface)
                        classes.add(ge);
                } else {
                    if (!(ge instanceof MInterface))
                        classes.add(ge);
                }
            }
        }
        return generateClassList(classes);
    }

    public String generateClassList(Collection classifiers) {
        String s = "";
        if (classifiers == null)
            return "";
        Iterator clsEnum = classifiers.iterator();
        while (clsEnum.hasNext()) {
            s += generateClassifierRef((MClass) clsEnum.next());
            if (clsEnum.hasNext())
                s += ", ";
        }
        return s;
    }

    public String generateVisibility(MVisibilityKind vis) {
        if (vis == null)
            return "";
        //if (vis == null) return "";
        if (MVisibilityKind.PUBLIC.equals(vis))
            return "+";
        if (MVisibilityKind.PRIVATE.equals(vis))
            return "-";
        if (MVisibilityKind.PROTECTED.equals(vis))
            return "#";
        return "";
    }

    public String generateVisibility(MFeature f) {
        return generateVisibility(f.getVisibility());
    }

    public String generateScope(MFeature f) {
        MScopeKind scope = f.getOwnerScope();
        //if (scope == null) return "";
        if (MScopeKind.CLASSIFIER.equals(scope))
            return "static ";
        return "";
    }

    public String generateChangability(MStructuralFeature sf) {
        MChangeableKind ck = sf.getChangeability();
        //if (ck == null) return "";
        if (MChangeableKind.FROZEN.equals(ck))
            return "final ";
        //if (MChangeableKind.ADDONLY.equals(ck)) return "final ";
        return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider#generateMultiplicity(MMultiplicity)
     */
    public String generateMultiplicity(MMultiplicity m) {
        if (m == null) {
            return "";
        }
        if (MMultiplicity.M0_N.equals(m))
            return ANY_RANGE;
        String s = "";
        Collection v = m.getRanges();
        if (v == null)
            return s;
        Iterator rangeIter = v.iterator();
        while (rangeIter.hasNext()) {
            MMultiplicityRange mr = (MMultiplicityRange) rangeIter.next();
            if (!(mr.getLower() == 1 && mr.getUpper() == 1 && v.size() == 1)) {
                s += generateMultiplicityRange(mr);
                s += ",";
            }
        }
        if (s.length() > 0 && s.lastIndexOf(',') == s.length() - 1) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static final String ANY_RANGE = "0..*";

    /**
     * Generates a multiplicity range. The standard getLower and getUpper defined on
     * MMultiplicityRange give a -1 if the multiplicity is n or *. This method
     * circumvents that behaviour.
     * @param mr
     * @return String
     */
    //public static final String ANY_RANGE = "*";
    // TODO: user preference between "*" and "0..*"

    protected String generateMultiplicityRange(MMultiplicityRange mr) {

        // 2002-07-25
        // Jaap Branderhorst
        // this does not work when the multiplicity is *
        /*
        return mr.toString();
        */
        mr.toString();
        int lower = mr.getLower();
        String lowerStr = "" + lower;
        int upper = mr.getUpper();
        String upperStr = "" + upper;
        if (lower == MMultiplicity.N) {
            lowerStr = "*";
        }
        if (upper == MMultiplicity.N) {
            upperStr = "*";
        }
        if (lower == upper)
            return lowerStr;
        return lowerStr + ".." + upperStr;
    }

    //     Integer lower = new Integer(mr.getLower());
    //     Integer upper = new Integer(mr.getUpper());
    //     if (lower == null && upper == null) return ANY_RANGE;
    //     if (lower == null) return "*.."+ upper.toString();
    //     if (upper == null) return lower.toString() + "..*";
    //     if (lower.intValue() == upper.intValue()) return lower.toString();
    //     return lower.toString() + ".." + upper.toString();

    public String generateState(MState m) {
        return m.getName();
    }

    public String generateStateBody(MState m) {
        StringBuffer s = new StringBuffer();

        MAction entryAction = m.getEntry();
        MAction doAction = m.getDoActivity();
        MAction exitAction = m.getExit();
        if (entryAction != null) {
            String entryStr = Generate(entryAction);
            s.append("entry /").append(entryStr);
        }
        if (doAction != null) {
            String doStr = Generate(doAction);
            if (s.length() > 0)
                s.append("\n");
            s.append("do /").append(doStr);

        }
        if (exitAction != null) {
            String exitStr = Generate(exitAction);
            if (s.length() > 0)
                s.append("\n");
            s.append("exit /").append(exitStr);
        }
        Collection internaltrans = m.getInternalTransitions();
        if (internaltrans != null) {
            Iterator iter = internaltrans.iterator();
            while (iter.hasNext()) {
                if (s.length() > 0)
                	s.append("\n");
                MTransition trans = (MTransition)iter.next();
                s.append(trans.getName()).append(" /").append(generateTransition(trans));
            }
        }
        return s.toString();
    }

    public String generateTransition(MTransition m) {
        String s = generate(m.getName());
        String t = generate(m.getTrigger());
        String g = generate(m.getGuard());
        String e = generate(m.getEffect());
        if (s.length() > 0)
            s += ": ";
        s += t;
        if (g.length() > 0)
            s += " [" + g + "]";
        if (e.length() > 0)
            s += " / " + e;
        return s;
    }

    public String generateAction(MAction m) {
        Collection c;
        Iterator it;
        String s;
        String p;
        boolean first;

        if ((m.getScript() != null) && (m.getScript().getBody() != null))
            s = m.getScript().getBody();
        else
            s = "";

        p = "";
        c = m.getActualArguments();
        it = c.iterator();
        first = true;
        while (it.hasNext()) {
            MArgument arg = (MArgument) it.next();
            if (!first)
                p += ", ";

            if (arg.getValue() != null)
                p += generateExpression(arg.getValue());
            first = false;
        }

        if (s.length() == 0 && p.length() == 0)
            return "";

        return s + " (" + p + ")";
    }

    public String generateGuard(MGuard m) {
        if (m.getExpression() != null)
            return generateExpression(m.getExpression());
        return "";
    }

    // public NotationName getNotation() {
    // return Notation.NOTATION_ARGO;
    // }

    public boolean canParse() {
        return true;
    }

    public boolean canParse(Object o) {
        return true;
    }

    public String getModuleName() {
        return "GeneratorDisplay";
    }
    public String getModuleDescription() {
        return "Uml 1.3 Notation Generator";
    }
    public String getModuleAuthor() {
        return "ArgoUML Core";
    }
    public String getModuleVersion() {
        return ArgoVersion.getVersion();
    }
    public String getModuleKey() {
        return "module.language.uml.generator";
    }

} /* end class GeneratorDisplay */
