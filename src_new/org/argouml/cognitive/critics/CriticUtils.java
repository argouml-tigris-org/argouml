// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: CriticUtils.java
// Classes: CriticUtils
// Original Author: mail@jeremybennett.com
// $Id$

// 4 Feb 2002: Original version by Jeremy Bennett (mail@jeremybennett.com)

package org.argouml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;


/** 
 * <p>An abstract class providing a series of static utility functions that are
 * of help when writing constructors.</p>
 *
 * <p>Declared as abstract, since it is not intended to be instantiated (and
 * hence there is no constructor). It is a utility class providing functions
 * for general use.</p>
 */

public abstract class CriticUtils {


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MClass
     * MClass} has any outgoing associations.</p>
     *
     * @param cls the {@link ru.novosoft.uml.foundation.core.MClass MClass}
     *            being tested.
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} has
     *            outgoing associations, otherwise <code>false</code>.
     */

    public static boolean hasOutgoingAssociations(MClass cls) {

        // Get all the features (attributes and operations)

        Collection ends = cls.getAssociationEnds();

        // If no associations, can have no outgoing assocations, so fail.

        if (ends == null) {
            return false;
        }

        // Loop through looking for outgoing associations.

        Iterator endEnum = ends.iterator();

        while (endEnum.hasNext()) {

            // Get the association end (the class end), and hence find its
            // association

	    MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	    MAssociation    a  = ae.getAssociation();

            // If there is no association, continue with the next association

            if (a == null) {
                continue;
            }

            // If this association has a navigable other end, we have suceeded.

            if (hasOutgoingEnd(a, ae)) {
                return true;
            }
        }

        // If we drop through here, there were no outgoing associations, so
        // return failure

        return false;
    }


    /**
     * <p>Tests if the given {@link
     * ru.novosoft.uml.foundation.core.MAssociation MAssociation} has any
     * outgoing ends that are navigable.</p>
     *
     * @param assoc the {@link ru.novosoft.uml.foundation.core.MAssociation
     *              MAssociation} being tested.
     *
     * @param inEnd the {@link ru.novosoft.uml.foundation.core.MAssociationEnd
     *              MAssociationEnd} that is the inbound end of the link.
     *
     * @return      <code>true</code> if the {@link
     *              ru.novosoft.uml.foundation.core.MAssociation MAssociation}
     *              has outgoing ends, otherwise <code>false</code>.
     */

    public static boolean hasOutgoingEnd(MAssociation    assoc,
                                         MAssociationEnd inEnd) {

        // Get all the association ends hanging off this association. If there
        // are none (should never happen, because we got it from ourself!)
        // continue with the next association

        Collection allEnds = assoc.getConnections();

        if (allEnds == null) {
            return false ;
        }

        // Loop through all the ends to see if any (other than ourself of
        // course) is navigable, and hence represents an outward
        // association from the class.

        Iterator iter = allEnds.iterator();

        while (iter.hasNext()) {

            // Find what is attached to the object

            Object obj = iter.next();

            // If its not an AssociationEnd (it should be), skip it, otherwise
            // cast is to an AssociatonEnd

            if (!(obj instanceof MAssociationEnd)) {
                continue;
            }

            MAssociationEnd otherEnd = (MAssociationEnd)obj;

            // If it is ourself, skip it. We can use straight == here -
            // we've looking for the same object.

            if (otherEnd == inEnd) {
                continue;
            }

            // Otherwise if it is navigable, we can return success

            if (otherEnd.isNavigable()) {
                return true;
            }
        }

        // If we drop through here, there were no outgoing ends, so
        // return failure

        return false;
    }


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MClass
     * MClass} has any instance variables (non-static attributes in UML
     * terms).</p> 
     *
     * @param cls the {@link ru.novosoft.uml.foundation.core.MClass MClass}
     *            being tested.
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} has
     *            instance variables, otherwise <code>false</code>.
     */

    public static boolean hasInstanceVariables(MClass cls) {

        // Get all the features (attributes and operations)

        Collection featureList       = cls.getFeatures();

        // If we have no features (and so no attributes) we cannot have
        // instance variables, so return failure.
        
        if (featureList == null) {
            return false;
        }

        // Loop through all the features looking for attributes that are
        // instance variables.

        Iterator iter = featureList.iterator();

        while (iter.hasNext()) {

            Object feature = iter.next();

            // Only look at anything that is an attribute. If it is an
            // unitialized instance variable, return success.

            if (feature instanceof MAttribute) {

                // Cast to an attribute and see if its an uninitialised
                // instance variable. If so return success.

                MAttribute attr = (MAttribute) feature;

                if (isInstanceVariable(attr)) {
                    return true;
                }
            }
        }

        // If we drop out we didn't find any instance variables,
        // so return false.

        return false;
    }

    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MAttribute
     * MAttribute} is an instance variable (non-sttic attribute in UML
     * terms).</p>
     *
     * @param attr the {@link ru.novosoft.uml.foundation.core.MAttribute
     *             MAttribute} being tested.
     *
     * @return     <code>true</code> if the {@link
     *             ru.novosoft.uml.foundation.core.MAttribute MAttribute} is an
     *             instance variable, otherwise <code>false</code>.
     */

    public static boolean isInstanceVariable(MAttribute attr) {

        // Get the owner scope

        MScopeKind  sk   = attr.getOwnerScope();

        // Return whether it is an instance variable.

        return MScopeKind.INSTANCE.equals(sk);
    }


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MClass
     * MClass} has any unitialised instance variables (non-static attributes in
     * UML terms).</p> 
     *
     * @param cls the {@link ru.novosoft.uml.foundation.core.MClass MClass}
     *            being tested.
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} has
     *            uninitialised instance variables, otherwise
     *            <code>false</code>.
     */

    public static boolean hasUninitInstanceVariables(MClass cls) {

        // Get all the features (attributes and operations)

        Collection featureList       = cls.getFeatures();

        // If we have no features (and so no attributes) we cannot have
        // uninitialised instance variables, so return failure.
        
        if (featureList == null) {
            return false;
        }

        // Loop through all the features looking for attributes that are
        // uninitialised instance variables.

        Iterator iter = featureList.iterator();

        while (iter.hasNext()) {

            Object feature = iter.next();

            // Only look at anything that is an attribute. If it is an
            // unitialized instance variable, return success.

            if (feature instanceof MAttribute) {

                // Cast to an attribute and see if its an uninitialised
                // instance variable. If so return success.

                MAttribute attr = (MAttribute) feature;

                if (isUninitInstanceVariable(attr)) {
                    return true;
                }
            }
        }

        // If we drop out we didn't find any uninitialized instance variables,
        // so return false.

        return false;
    }

    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MAttribute
     * MAttribute} is an unitialised instance variable.</p> 
     *
     * @param attr the {@link ru.novosoft.uml.foundation.core.MAttribute
     *             MAttribute} being tested.
     *
     * @return     <code>true</code> if the {@link
     *             ru.novosoft.uml.foundation.core.MAttribute MAttribute} is an
     *             uninitialised instance variable, otherwise
     *             <code>false</code>.
     */

    public static boolean isUninitInstanceVariable(MAttribute attr) {

        // Get the owner scope and initial value of the attribute

        MScopeKind  sk   = attr.getOwnerScope();
        MExpression init = attr.getInitialValue();

        // If it's an instance variable see if the initialisation
        // expression actually yields a value, and if not we have found an
        // unitialised instance variable so return true. Otherwise return
        // false.

        return MScopeKind.INSTANCE.equals(sk) &&
               ((init == null ||
                 init.getBody() == null ||
                 init.getBody().trim().length() == 0));
    }


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MClass
     * MClass} has a static attribute with the itself as type.</p>
     *
     * <p>Useful for Singletons, which need such an attribute to hold their
     * instance.</p>
     *
     * @param cls the {@link ru.novosoft.uml.foundation.core.MClass MClass}
     *            being tested.
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} has a static
     *            attribute with itself as type, otherwise <code>false</code>.
     */

    public static boolean hasStaticAttrForClass(MClass cls) {

        // Get all the features (attributes and operations)

        Collection featureList       = cls.getFeatures();

        // If we have no features (and so no attributes) we cannot have
        // a suitable static attribute, so return failure.
        
        if (featureList == null) {
            return false;
        }

        // Loop through looking for an attribute that is static and has the
        // the class as its type.

        String className = cls.getName();
        Iterator iter    = featureList.iterator();

        while (iter.hasNext()) {

            Object feature = iter.next();

            // Only look at anything that is an attribute

            if (!(feature instanceof MAttribute)) {
                continue;
            }

            // Cast to an attribute and see if it is a static attribute for our
            // class. If so return success

            MAttribute attr = (MAttribute) feature;

            if( isStaticAttrForClass(attr,className)) {
                return true ;
            }
        }

        // We've finished looking at the attributes. If we fell out at the end
        // of the loop, then we didn't find a static attribute with the right
        // type, so return failure.

        return false;
    }


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MAttribute
     * MAttribute} is a static attribute with the given {@link
     * ru.novosoft.uml.foundation.core.MClass MClass} as type.</p>
     *
     * <p>Useful for Singletons, which need such an attribute to hold their
     * instance.</p>
     *
     * <p>Behaves cleanly if given null arguments (returns false).</p>
     *
     * @param attr      the {@link ru.novosoft.uml.foundation.core.MAttribute
     *                  MAttribute} being tested.
     *
     * @param className the name of the desired {@link
     *                  ru.novosoft.uml.foundation.core.MClass MClass} for the
     *                  type of <code>bf</code>.
     *
     * @return          <code>true</code> if the {@link
     *                  ru.novosoft.uml.foundation.core.MAttribute MAttribute}
     *                  is a static attribute with the given name of the {@link
     *                  ru.novosoft.uml.foundation.core.MClass MClass} as type,
     *                  otherwise <code>false</code>.
     */

    public static boolean isStaticAttrForClass(MAttribute attr,
                                               String     className) {

        // Deal with the special cases where the attribute argument or
        // className is null In either case we fail. In the latter case we have
        // an anonymous class, and since we can't have an anonymous attribute,
        // we must fail.

        if ((attr == null) || (className == null)) {
            return false;
        }

        // Get the owner scope kind (i.e. is this static or an instance
        // variable). If it's not static (i.e. has CLASSIFIER scope kind), then
        // we have not got we want, so return failure. Note that the way round
        // of the test works fine if sk is null (should never happen).

        MScopeKind sk    = attr.getOwnerScope();

        if (!(MScopeKind.CLASSIFIER.equals(sk))) {
            return false;
        }

        // See if it has the same type as the given class name. It must be both
        // a class (not e.g. a DataType) and have the same name.

        MClassifier attrType = attr.getType() ;

        // If it isn't a class, not what we want, so return failure

        if (!(attrType instanceof MClass)) {
            return false;
        }

        // If the class doesn't have the same name, its not what we want,
        // so return false, otherwise return success.

        return className.equals(attrType.getName());
    }


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MClass
     * MClass} has only private constructors, and at least one of them.<p>
     *
     * <p>A constructor is any operation with stereotype &laquo;create&raquo;
     * (the UML view of the world, which is preferred). We'll also accept
     * &laquo;Create&raquo;, although it's not strictly UML standard.</p>
     *
     * <p>We also accept a constructor defined as an operation with the same
     * name as the class, which is not static and which returns no result (the
     * Java view of the world).</p>
     *
     * @param cls the name of the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} for which we
     *            seek a constructor. 
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} has a
     *            constructor, otherwise <code>false</code>.
     */

    public static boolean hasOnlyPrivateConstructors(MClass cls) {

        // Get all the features (attributes and operations) and define a flag
        // to see if we have found a constructor.

        Collection featureList        = cls.getFeatures();
        boolean    privateConstrFound = false;

        // If we have no features (and so no operations) we can have no
        // constructor, so return failure
        
        if (featureList == null) {
            return false;
        }

        // We need to look for all BehavioralFeatures that are
        // constructors. Get the name of the class to compare against and loop
        // through all the features, looking for BehavioralFeatures to
        // test. If there are any that are constructors, then check that they
        // are private (if not we have failed).

        String   className = cls.getName();
        Iterator iter      = featureList.iterator();

        while (iter.hasNext()) {

            Object feature = iter.next();

            // If it's not a BehavioralFeature skip on to the next in the
            // list.

            if (!(feature instanceof MBehavioralFeature)) {
                continue;
            }
            
            // If it's not a constructor, skip on to the next in the list.

            MBehavioralFeature bf = (MBehavioralFeature) feature;

            if (!(isConstructor(bf,className))) {
                continue;
            }

            // Check if the constructor is private. Look at its visibility
            // kind. If it is not private, we have a non-private constructor
            // and can return failure.

            MVisibilityKind vk = bf.getVisibility();

            if (!(MVisibilityKind.PRIVATE.equals(vk))) {
                return false;
            }

            // We have a private constructor, so can mark that there is at
            // least one.

            privateConstrFound = true;
        }

        // If we drop out here, then any constructors found were
        // private. Return successfully if we found at least one.

        return privateConstrFound;
    }


    /**
     * <p>Tests if the given {@link ru.novosoft.uml.foundation.core.MClass
     * MClass} has a constructor.<p>
     *
     * <p>A constructor is any operation with stereotype &laquo;create&raquo;
     * (the UML view of the world, which is preferred). We'll also accept
     * &laquo;Create&raquo;, although it's not strictly UML standard.</p>
     *
     * <p>We also accept a constructor defined as an operation with the same
     * name as the class, which is not static and which returns no result (the
     * Java view of the world).</p>
     *
     * @param cls the name of the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} for which we
     *            seek a constructor. 
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MClass MClass} has a
     *            constructor, otherwise <code>false</code>.
     */

    public static boolean hasConstructor(MClass cls) {

        // Get all the features (attributes and operations)

        Collection featureList = cls.getFeatures();

        // If we have no features (and so no operations) we can have no
        // constructor, so return failure.
        
        if (featureList == null) {
            return false;
        }

        // We need to look for a BehavioralFeature that is a constructor. Get
        // the name of the class to compare against and loop through all the
        // features, looking for BehavioralFeatures to test.

        String   className = cls.getName();
        Iterator iter      = featureList.iterator();

        while (iter.hasNext()) {

            Object feature = iter.next();

            // If its a BehavioralFeature, see if it is a constructor, and if
            // so 

            if (feature instanceof MBehavioralFeature) {
                MBehavioralFeature bf = (MBehavioralFeature) feature;

                if (isConstructor(bf,className)) {
                    return true;
                }
            }
        }

        // If we drop out here, then we didn't have a constructor

        return false;
    }


    /**
     * <p>Tests if the given {@link
     * ru.novosoft.uml.foundation.core.MBehavioralFeature MBehavioralFeature}
     * is a constructor of the given class.</p>
     *
     * <p>A constructor is any operation with stereotype &laquo;create&raquo;
     * (the UML view of the world, which is preferred). We'll also accept
     * &laquo;Create&raquo;, although it's not strictly UML standard.</p>
     *
     * <p>We also accept a constructor defined as an operation with the same
     * name as the class, which is not static and which returns no result (the
     * Java view of the world).</p>
     *
     * @param bf        the {@link
     *                  ru.novosoft.uml.foundation.core.MBehavioralFeature
     *                  MBehavioralFeature} being tested as a constructor.
     *
     * @param className the name of the {@link
     *                  ru.novosoft.uml.foundation.core.MClass MClass} for
     *                  which we seek a constructor. 
     *
     * @return          <code>true</code> if the {@link
     *                  ru.novosoft.uml.foundation.core.MBehavioralFeature
     *                  MBehavioralFeature} is a constructor for
     *                  <code>className</code>, otherwise <code>false</code>.
     */
    
    public static boolean isConstructor(MBehavioralFeature bf,
                                        String className) {

        // We have a constructor if the stereotype name is <<create>>. We'll
        // also accept <<Create>>, although its not strictly in the UML
        // standard.

        if (hasStereotype(bf,"create") || hasStereotype(bf,"Create")) {
            return true;
        }

        // Not stereotyped as create, so we need to look if it meets the
        // Java/C++ convention for a constructor. First get its name.

        String operName       = bf.getName();

        // If the operation name is not the same as the class name this is
        // not a constructor.

        if (!operName.equals(className)) {
            return false;
        }

        // If the operation is static, it is not a constructor.

        MScopeKind sk = bf.getOwnerScope();

        if (!MScopeKind.INSTANCE.equals(sk)) {
            return false;
        }

        // If there is no return parameter, we have a constructor, otherwise we
        // don't

        return (getReturnParam(bf) == null);
    }


    /**
     * <p>Tests if the given {@link
     * ru.novosoft.uml.foundation.core.MModelElement MModelElement} (and hence
     * any subclass) has the given named stereotype.</p>
     *
     * <p>This does work correctly if you give a null string as an argument.
     * It will succeed if the {@link
     * ru.novosoft.uml.foundation.core.MModelElement MModelElement} has no
     * stereotype or a stereotype with no name. So this can be used to test for
     * no stereotype at all.</p>
     *
     * @param me  the {@link ru.novosoft.uml.foundation.core.MModelElement
     *            MModelElement} whose stereotype we are testing.
     *
     * @param stn the stereotype name we are testing for.
     *
     * @return    <code>true</code> if the {@link
     *            ru.novosoft.uml.foundation.core.MModelElement MModelElement}
     *            (or subclass) has the given named stereotype.  */

    public static boolean hasStereotype(MModelElement me, String stn)
    {
        // Fail if we have a null element

        if (me == null) {
            return false;
        }

        // Get the stereotype

        MStereotype meSt = me.getStereotype();

        // We will actually work with the peculiar case that the given string
        // is null and there is no stereotype declared. We'll succeed if the me
        // has no stereotype, or has a stereotype with no name

        if (meSt == null) {
            return stn == null;
        }

        // Now get the stereotype name

        String meStn = meSt.getName() ;

        // Deal with the special case that the stereotype name is null (not
        // clear that this can ever happen). We'll allow this to succeed if we
        // were asked to look for a null name.

        if (meStn == null) {
            return stn == null;
        }

        // Otherwise just check if the name are the same.

        return meStn.equals(stn);
    }


    /**
     * <p>Finds the return parameter in a {@link
     * ru.novosoft.uml.foundation.core.MBehavioralFeature
     * MBehavioralFeature}.</p>
     *
     * <p>Loops through all the parameters until it finds one whose
     * {@link ru.novosoft.uml.foundation.data_types.MParameterDirectionKind
     * MParameterDirectionKind} is {@link
     * ru.novosoft.uml.foundation.data_types.MParameterDirectionKind#RETURN
     * RETURN} and returns that parameter.</p>
     *
     * @param bf the {@link
     *           ru.novosoft.uml.foundation.core.MBehavioralFeature
     *           MBehavioralFeature} whose return parameter is wanted 
     *
     * @return   the parameter found (of type {@link
     *           ru.novosoft.uml.foundation.core.MParameter MParameter}), or
     *           <code>null</code> if there is no return parameter
     */

    public static MParameter getReturnParam(MBehavioralFeature bf) {

        // Get the parameters and loop through them looking for a return
        // parameter. Check first we actually have some parameters

        Collection parameters = bf.getParameters();

        if (parameters == null) {
            return null ;
        }

        Iterator iter = parameters.iterator();

        while (iter.hasNext()) {
            MParameter param = (MParameter)iter.next();

            // If the parameter is a return parameter, return its type.

            if (MParameterDirectionKind.RETURN.equals(param.getKind())) {
                return param;
            }
        }

        // If there was no return parameter return null

        return null;
    }

} /* end class CriticUtils */
