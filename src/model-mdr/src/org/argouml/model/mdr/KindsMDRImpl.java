// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.DirectionKind;
import org.argouml.model.OrderingKind;
import org.argouml.model.PseudostateKind;
import org.argouml.model.ScopeKind;
import org.argouml.model.VisibilityKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.CallConcurrencyKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

/**
 * Class that contains enums in the Model.
 */
class KindsMDRImpl implements ChangeableKind, AggregationKind,
        PseudostateKind, ScopeKind, ConcurrencyKind, DirectionKind,
        OrderingKind, VisibilityKind {

    private MDRModelImplementation modelImplementation;

    /**
     * Constructor.
     *
     * @param mi The MDRModelImplementation.
     */
    KindsMDRImpl(MDRModelImplementation mi) {
        modelImplementation = mi;
    }

    /*
     * @see org.argouml.model.ChangeableKind#getAddOnly()
     */
    public Object getAddOnly() {
        return ChangeableKindEnum.CK_ADD_ONLY;
    }

    /*
     * @see org.argouml.model.AggregationKind#getAggregate()
     */
    public Object getAggregate() {
        return AggregationKindEnum.AK_AGGREGATE;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getChoice()
     */
    public Object getChoice() {
        return PseudostateKindEnum.PK_CHOICE;
    }

    /*
     * @see org.argouml.model.ChangeableKind#getChangeable()
     */
    public Object getChangeable() {
        return ChangeableKindEnum.CK_CHANGEABLE;
    }

    /*
     * @see org.argouml.model.ScopeKind#getClassifier()
     */
    public Object getClassifier() {
        return ScopeKindEnum.SK_CLASSIFIER;
    }

    /*
     * @see org.argouml.model.AggregationKind#getComposite()
     */
    public Object getComposite() {
        return AggregationKindEnum.AK_COMPOSITE;
    }

    /*
     * @see org.argouml.model.ConcurrencyKind#getConcurrent()
     */
    public Object getConcurrent() {
        return CallConcurrencyKindEnum.CCK_CONCURRENT;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getDeepHistory()
     */
    public Object getDeepHistory() {
        return PseudostateKindEnum.PK_DEEP_HISTORY;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getFork()
     */
    public Object getFork() {
        return PseudostateKindEnum.PK_FORK;
    }

    /*
     * @see org.argouml.model.ChangeableKind#getFrozen()
     */
    public Object getFrozen() {
        return ChangeableKindEnum.CK_FROZEN;
    }

    /*
     * @see org.argouml.model.ConcurrencyKind#getGuarded()
     */
    public Object getGuarded() {
        return CallConcurrencyKindEnum.CCK_GUARDED;
    }

    /*
     * @see org.argouml.model.DirectionKind#getInParameter()
     */
    public Object getInParameter() {
        return ParameterDirectionKindEnum.PDK_IN;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getInitial()
     */
    public Object getInitial() {
        return PseudostateKindEnum.PK_INITIAL;
    }

    /*
     * @see org.argouml.model.DirectionKind#getInOutParameter()
     */
    public Object getInOutParameter() {
        return ParameterDirectionKindEnum.PDK_INOUT;
    }

    /*
     * @see org.argouml.model.ScopeKind#getInstance()
     */
    public Object getInstance() {
        return ScopeKindEnum.SK_INSTANCE;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getJoin()
     */
    public Object getJoin() {
        return PseudostateKindEnum.PK_JOIN;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getJunction()
     */
    public Object getJunction() {
        return PseudostateKindEnum.PK_JUNCTION;
    }

    /*
     * @see org.argouml.model.AggregationKind#getNone()
     */
    public Object getNone() {
        return AggregationKindEnum.AK_NONE;
    }

    /*
     * @see org.argouml.model.OrderingKind#getOrdered()
     */
    public Object getOrdered() {
        return OrderingKindEnum.OK_ORDERED;
    }

    /*
     * @see org.argouml.model.DirectionKind#getOutParameter()
     */
    public Object getOutParameter() {
        return ParameterDirectionKindEnum.PDK_OUT;
    }

    /*
     * @see org.argouml.model.VisibilityKind#getPackage()
     */
    public Object getPackage() {
        return VisibilityKindEnum.VK_PACKAGE;
    }

    /*
     * @see org.argouml.model.VisibilityKind#getPrivate()
     */
    public Object getPrivate() {
        return VisibilityKindEnum.VK_PRIVATE;
    }

    /*
     * @see org.argouml.model.VisibilityKind#getProtected()
     */
    public Object getProtected() {
        return VisibilityKindEnum.VK_PROTECTED;
    }

    /*
     * @see org.argouml.model.VisibilityKind#getPublic()
     */
    public Object getPublic() {
        return VisibilityKindEnum.VK_PUBLIC;
    }

    /*
     * @see org.argouml.model.DirectionKind#getReturnParameter()
     */
    public Object getReturnParameter() {
        return ParameterDirectionKindEnum.PDK_RETURN;
    }

    /*
     * @see org.argouml.model.ConcurrencyKind#getSequential()
     */
    public Object getSequential() {
        return CallConcurrencyKindEnum.CCK_SEQUENTIAL;
    }

    /*
     * @see org.argouml.model.PseudostateKind#getShallowHistory()
     */
    public Object getShallowHistory() {
        return PseudostateKindEnum.PK_SHALLOW_HISTORY;
    }

    /*
     * @see org.argouml.model.OrderingKind#getUnordered()
     */
    public Object getUnordered() {
        return OrderingKindEnum.OK_UNORDERED;
    }

}

