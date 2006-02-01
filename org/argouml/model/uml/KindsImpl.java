// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.DirectionKind;
import org.argouml.model.OrderingKind;
import org.argouml.model.PseudostateKind;
import org.argouml.model.ScopeKind;
import org.argouml.model.VisibilityKind;

import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MOrderingKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;

/**
 * Class that contains enums in the Model.
 */
public class KindsImpl
	implements ChangeableKind, AggregationKind, PseudostateKind,
	ScopeKind, ConcurrencyKind, DirectionKind,
	OrderingKind, VisibilityKind {

    /**
     * Constructor.
     */
    KindsImpl() {
    }

    /**
     * @return Returns the AddOnly ChangeableKind.
     */
    public Object getAddOnly() {
        return MChangeableKind.ADD_ONLY;
    }

    /**
     * @return Returns the Aggregate AggregationKind.
     */
    public Object getAggregate() {
        return MAggregationKind.AGGREGATE;
    }

    /**
     * @return Returns the Branch/Choice PseudostateKind.
     */
    public Object getBranch() {
        return MPseudostateKind.BRANCH;
    }

    /**
     * @return Returns the Branch/Choice PseudostateKind.
     */
    public Object getChoice() {
        return getBranch(); // NSUML uses Branch, not Choice
    }

    /**
     * @return Returns the Changeable ChangeableKind.
     */
    public Object getChangeable() {
        return MChangeableKind.CHANGEABLE;
    }

    /**
     * @return Returns the Classifier ScopeKind.
     */
    public Object getClassifier() {
        return MScopeKind.CLASSIFIER;
    }

    /**
     * @return Returns the Composite AggregationKind.
     */
    public Object getComposite() {
        return MAggregationKind.COMPOSITE;
    }

    /**
     * @return Returns the Concurrent CallConcurrencyKind.
     */
    public Object getConcurrent() {
        return MCallConcurrencyKind.CONCURRENT;
    }

    /**
     * @return Returns the DeepHistory PseudostateKind.
     */
    public Object getDeepHistory() {
        return MPseudostateKind.DEEP_HISTORY;
    }

    /**
     * @return Returns the Fork PseudostateKind.
     */
    public Object getFork() {
        return MPseudostateKind.FORK;
    }

    /**
     * @return Returns the Frozen ChangeableKind.
     */
    public Object getFrozen() {
        return MChangeableKind.FROZEN;
    }

    /**
     * @return Returns the Guarded CallConcurrencyKind.
     */
    public Object getGuarded() {
        return MCallConcurrencyKind.GUARDED;
    }

    /**
     * @return Returns the In ParameterDirectionKind.
     */
    public Object getInParameter() {
        return MParameterDirectionKind.IN;
    }

    /**
     * @return Returns the Initial PseudostateKind.
     */
    public Object getInitial() {
        return MPseudostateKind.INITIAL;
    }

    /**
     * @return Returns the Inout ParameterDirectionKind.
     */
    public Object getInOutParameter() {
        return MParameterDirectionKind.INOUT;
    }

    /**
     * @return Returns the Instance ScopeKind.
     */
    public Object getInstance() {
        return MScopeKind.INSTANCE;
    }

    /**
     * @return Returns the Join PseudostateKind.
     */
    public Object getJoin() {
        return MPseudostateKind.JOIN;
    }

    /**
     * @return Returns the Junction PseudostateKind.
     */
    public Object getJunction() {
        return MPseudostateKind.JUNCTION;
    }

    /**
     * @return Returns the 0 1 Multiplicity.
     */
    public Object get01() {
        return MMultiplicity.M0_1;
    }

    /**
     * @return Returns the 0 N Multiplicity.
     */
    public Object get0N() {
        return MMultiplicity.M0_N;
    }

    /**
     * @return Returns the 1 1 Multiplicity.
     */
    public Object get11() {
        return MMultiplicity.M1_1;
    }

    /**
     * @return Returns the 1 N Multiplicity.
     */
    public Object get1N() {
        return MMultiplicity.M1_N;
    }

    /**
     * @return Returns the None AggregationKind.
     */
    public Object getNone() {
        return MAggregationKind.NONE;
    }

    /**
     * @return Returns the Ordered OrderingKind.
     */
    public Object getOrdered() {
        return MOrderingKind.ORDERED;
    }

    /**
     * @return Returns the Out ParameterDirectionKind.
     */
    public Object getOutParameter() {
        return MParameterDirectionKind.OUT;
    }

    /**
     * @return Returns the Private VisibilityKind.
     */
    public Object getPrivate() {
        return MVisibilityKind.PRIVATE;
    }

    /**
     * @return Returns the Protected VisibilityKind.
     */
    public Object getProtected() {
        return MVisibilityKind.PROTECTED;
    }

    /**
     * @return Returns the Public VisibilityKind.
     */
    public Object getPublic() {
        return MVisibilityKind.PUBLIC;
    }

    /**
     * @return Returns the Return ParameterDirectionKind.
     */
    public Object getReturnParameter() {
        return MParameterDirectionKind.RETURN;
    }

    /**
     * @return Returns the Sequential CallConcurrencyKind.
     */
    public Object getSequential() {
        return MCallConcurrencyKind.SEQUENTIAL;
    }

    /**
     * @return Returns the ShallowHistory PseudostateKind.
     */
    public Object getShallowHistory() {
        return MPseudostateKind.SHALLOW_HISTORY;
    }

    /**
     * @return Returns the Unordered OrderingKind.
     */
    public Object getUnordered() {
        return MOrderingKind.UNORDERED;
    }

    public Object getPackage() {
        // TODO Auto-generated method stub
        return null;
    }

}

