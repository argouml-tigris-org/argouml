// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.api.model.uml;

/**
 * This class contains identifiers for all of the different
 * UML elements identified by ArgoUML.
 * 
 * @author Thierry Lach
 */
public final class Uml {

	// Types of line
	public static Class ABSTRACTION      = Uml.TypeAbstraction.class;
	public static Class ASSOCIATION      = Uml.TypeAssociation.class;
	public static Class ASSOCIATION_ROLE = Uml.TypeAssociationRole.class;
	public static Class DEPENDENCY       = Uml.TypeDependency.class;
	public static Class EXTEND           = Uml.TypeExtend.class;
	public static Class GENERALIZATION   = Uml.TypeGeneralization.class;
	public static Class INCLUDE          = Uml.TypeInclude.class;
	public static Class LINK             = Uml.TypeLink.class;
	public static Class PERMISSION       = Uml.TypePermission.class;
	public static Class USAGE            = Uml.TypeUsage.class;
	public static Class TRANSITION       = Uml.TypeTransition.class;

	// Types of node
	public static Class ACTOR              = Uml.TypeActor.class;
	public static Class CLASS              = Uml.TypeClass.class;
	public static Class CLASSIFIER         = Uml.TypeClassifier.class;
	public static Class CLASSIFIER_ROLE    = Uml.TypeClassifierRole.class;
	public static Class COMPONENT          = Uml.TypeComponent.class;
	public static Class COMPONENT_INSTANCE = Uml.TypeComponentInstance.class;
	public static Class INSTANCE           = Uml.TypeInstance.class;
	public static Class INTERFACE          = Uml.TypeInterface.class;
	public static Class NODE               = Uml.TypeNode.class;
	public static Class NODE_INSTANCE      = Uml.TypeNodeInstance.class;
	public static Class OBJECT             = Uml.TypeObject.class;
	public static Class PACKAGE            = Uml.TypePackage.class;
	public static Class STATE              = Uml.TypeState.class;
	public static Class COMPOSITESTATE     = Uml.TypeCompositeState.class;
	public static Class STATE_VERTEX        = Uml.TypeStateVertex.class;
	public static Class PSEUDOSTATE        = Uml.TypePseudostate.class;
	public static Class USE_CASE           = Uml.TypeUseCase.class;
    
	// Invisible model elements
	public static Class ACTION             = Uml.TypeAction.class;
	public static Class ACTION_EXPRESSION  = Uml.TypeActionExpression.class;
	public static Class ASSOCIATION_END    = Uml.TypeAssociationEnd.class;
	public static Class CALL_ACTION        = Uml.TypeCallAction.class;
	public static Class NAMESPACE          = Uml.TypeNamespace.class;
	public static Class RECEPTION          = Uml.TypeReception.class;
	public static Class STEREOTYPE         = Uml.TypeStereotype.class;

	public static Class ATTRIBUTE          = Uml.TypeAttribute.class;
	public static Class OPERATION          = Uml.TypeOperation.class;

	public static Class EVENT              = Uml.TypeEvent.class;
    
	public static Class MODEL_ELEMENT      = Uml.TypeModelElement.class;
	public static Class DATA_TYPE          = Uml.TypeDataType.class;
    
    /** An interface which all ArgoUML-recognized model types
     * must implement.
     * 
     * @author Thierry Lach
     */
	public interface Entity { }
	
	private class TypeAttribute implements Uml.Entity { }
	private class TypeOperation implements Uml.Entity { }
	private class TypeStereotype implements Uml.Entity { }
	private class TypeReception implements Uml.Entity { }
	private class TypeModelElement implements Uml.Entity { }
	private class TypeAbstraction implements Uml.Entity { }
	private class TypeAssociation implements Uml.Entity { }
	private class TypeAssociationRole implements Uml.Entity { }
	private class TypeDependency implements Uml.Entity { }
	private class TypeExtend implements Uml.Entity { }
	private class TypeGeneralization implements Uml.Entity { }
	private class TypeInclude implements Uml.Entity { }
	private class TypeLink implements Uml.Entity { }
	private class TypePermission implements Uml.Entity { }
	private class TypeUsage implements Uml.Entity { }
	private class TypeTransition implements Uml.Entity { }
	private class TypeActor implements Uml.Entity { }
	private class TypeClass implements Uml.Entity { }
	private class TypeClassifier implements Uml.Entity { }
	private class TypeComponent implements Uml.Entity { }
	private class TypeComponentInstance implements Uml.Entity { }
	private class TypeInstance implements Uml.Entity { }
	private class TypeInterface implements Uml.Entity { }
	private class TypeNode implements Uml.Entity { }
	private class TypeNodeInstance implements Uml.Entity { }
	private class TypeClassifierRole implements Uml.Entity { }
	private class TypeObject implements Uml.Entity { }
	private class TypePackage implements Uml.Entity { }
	private class TypeState implements Uml.Entity { }
	private class TypeCompositeState implements Uml.Entity { }
	private class TypeStateVertex implements Uml.Entity { }
	private class TypePseudostate implements Uml.Entity { }
	private class TypeUseCase implements Uml.Entity { }
	private class TypeAction implements Uml.Entity { }
	private class TypeActionExpression implements Uml.Entity { }
	private class TypeAssociationEnd implements Uml.Entity { }
	private class TypeCallAction implements Uml.Entity { }
	private class TypeNamespace implements Uml.Entity { }
	private class TypeEvent implements Uml.Entity { }
	private class TypeDataType implements Uml.Entity { }
}
