package org.argouml.ocl;

import java.util.*;

import tudresden.ocl.check.types.*;
import tudresden.ocl.check.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;

import org.apache.log4j.Category;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.*;
import org.argouml.uml.MMUtil;

public class ArgoFacade implements ModelFacade {
    
    public MClassifier target;

    public ArgoFacade(Object target) {
	if (target instanceof MClassifier)
	    this.target = (MClassifier)target;
    }

    public Any getClassifier(String name) {
      Project p = ProjectManager.getManager().getCurrentProject();
      
      if (target != null && target.getName().equals(name) ) {
        return new ArgoAny(target);
      }
      // else we have a problem: this is not clean!
      else {
        /*
         * Changed 2001-10-18 STEFFEN ZSCHALER
         *
         * Was:
         *
	  MClassifier classifier = p.findTypeInModel(name, p.getCurrentNamespace());
         *
         */
        MClassifier classifier = p.findTypeInModel(name, p.getModel());
        
        if (classifier == null) {
          /**
           * Added search in defined types 2001-10-18 STEFFEN ZSCHALER.
           */
          classifier = (MClassifier) p.findType(name, false);
          
          if (classifier == null) {
            throw new OclTypeException("cannot find classifier: "+name);
          }
        }
        
        return new ArgoAny(classifier);
      }
    }
}

class ArgoAny implements Any, Type2 {
    protected static Category cat = Category.getInstance(ArgoAny.class);

    MClassifier classifier;

    ArgoAny (MClassifier classifier) {
      this.classifier = classifier;
    }

    public Type navigateQualified (String name, Type[] qualifiers)
      throws OclTypeException {

      if (classifier == null) {
          throw new OclTypeException("attempting to access features of Void");
      }


      if (qualifiers != null) {
          throw new OclTypeException("qualified associations not supported yet!");
      }

      Type type = Basic.navigateAnyQualified(name, this, qualifiers);
      if (type != null) return type;

      MClassifier foundAssocType=null, foundAttribType=null;
      boolean isSet=false, isSequence=false; // cannot be Bag

      // first search for appropriate attributes
      java.util.Collection attributes = UmlHelper.getHelper().getCore().getAttributesInh(classifier);
      Iterator iter = attributes.iterator();
      while (iter.hasNext() && foundAttribType == null) {
        MAttribute attr = (MAttribute)iter.next();
        if (attr.getName().equals(name)) {
          foundAttribType = attr.getType();
        }
      }

      // look for associations
      java.util.Collection associationEnds = UmlHelper.getHelper().getCore().getAssociateEndsInh(classifier);
      Iterator asciter = associationEnds.iterator();
      while (asciter.hasNext() && foundAssocType == null) {
        MAssociationEnd ae = (MAssociationEnd)asciter.next();
        if (ae.getName()!=null && name.equals(ae.getName())) {
          foundAssocType = ae.getType();
        } else if (ae.getName()==null || ae.getName().equals("")) {
          String oppositeName = ae.getType().getName();
          if (oppositeName != null) {
            String lowerOppositeName = oppositeName.substring(0,1).toLowerCase();
            lowerOppositeName += oppositeName.substring(1);
            if (lowerOppositeName.equals(name))
              foundAssocType = ae.getType();
          }
        }
        if (foundAssocType!=null) {
          if (ae.getMultiplicity()!=null &&
              (ae.getMultiplicity().getUpper() > 1 || ae.getMultiplicity().getUpper()==-1)
             ) {
            // to do: think about the condition of this if-statement
            // ordered association end -> Sequence; otherwise -> Set
            if ( ae.getStereotype()!=null && ae.getStereotype().toString()!=null &&
                 "ordered".equals(ae.getStereotype().toString()) ) {
              isSequence=true;
            } else {
              isSet=true;
            }
          }
        }
      }

      if (foundAssocType!=null && foundAttribType!=null) {
        throw new OclTypeException(
          "cannot access feature "+name+" of classifier "+toString()+" because both "+
          "an attribute and an association end of this name where found"
        );
      }

      MClassifier foundType = (foundAssocType==null) ? foundAttribType : foundAssocType;

      if (foundType == null) {
        throw new OclTypeException("attribute "+name+" not found in classifier "+toString());
      }

      Type result = getOclRepresentation(foundType);

      if (isSet) {
        result=new tudresden.ocl.check.types.Collection(
          tudresden.ocl.check.types.Collection.SET, result
        );
      }
      if (isSequence) {
        result=new tudresden.ocl.check.types.Collection(
          tudresden.ocl.check.types.Collection.SEQUENCE, result
        );
      }

      return result;
    }

    public Type navigateParameterizedQuery (String name, Type[] qualifiers) 
      throws OclTypeException {
      return internalNavigateParameterized (name, qualifiers, true);
    }
    
    public Type navigateParameterized (String name, Type[] qualifiers) 
      throws OclTypeException {
      return internalNavigateParameterized (name, qualifiers, false);
    }
      
    public Type internalNavigateParameterized (final String name,
                                               final Type[] params,
                                               boolean fCheckIsQuery)
      throws OclTypeException {
      if (classifier == null) {
          throw new OclTypeException("attempting to access features of Void");
      }

      Type type = Basic.navigateAnyParameterized(name, params);
      if (type != null) return type;

      MOperation foundOp = null;
      java.util.Collection operations = UmlHelper.getHelper().getCore().getOperations(classifier);
      Iterator iter = operations.iterator();
      while (iter.hasNext() && foundOp == null){
          MOperation op = (MOperation)iter.next();
          if ( operationMatchesCall(op, name, params) ) {
            foundOp = op;
          }
      }

      if (foundOp == null) { throw new OclTypeException("operation "+name+" not found in classifier "+toString());}

      if (fCheckIsQuery) {
        /* Query checking added 05/21/01, sz9 */
        if (! foundOp.isQuery()) {
          throw new OclTypeException ("Non-query operations cannot be used in OCL expressions. (" + name + ")");
        }
      }
      
      MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(foundOp);

      if (rp == null || rp.getType() == null) {
          cat.warn("WARNING: supposing return type void!");
          return new ArgoAny(null);
      }
      MClassifier returnType = rp.getType();

      return getOclRepresentation(returnType);
    }

    public boolean conformsTo(Type type) {
			if (type instanceof ArgoAny)
			{
				ArgoAny other = (ArgoAny) type;
				return equals(type) || UmlHelper.getHelper().getCore().getAllSupertypes(classifier).contains(other.classifier);
			}
			else
			{
				return false;
			}
    }

    public boolean equals(Object o) {
      ArgoAny any = null;
      if (o instanceof ArgoAny) {
        any = (ArgoAny)o;
        return (any.classifier == classifier);
      }
      return false;
    }

    public int hashCode() {
      if (classifier == null) return 0;
      return classifier.hashCode();
    }

    public String toString() {
      if (classifier == null) return "Void";
      return classifier.getName();
    }

    public boolean hasState(String name) {
      cat.warn("ArgoAny.hasState() has been called, but is not implemented yet!");
      return false;
    }

    protected Type getOclRepresentation(MClassifier foundType)
    {
      Type result = null;
      
      if (foundType.getName().equals("int") || foundType.getName().equals("Integer")) {
          result = Basic.INTEGER;
      }

      if (foundType.getName().equals("float") || foundType.getName().equals("double")) {
          result = Basic.REAL;
      }

      if (foundType.getName().equals("bool") || foundType.getName().equals("Boolean") ||
          foundType.getName().equals("boolean")) {
          result = Basic.BOOLEAN;
      }

      if (foundType.getName().equals("String")){
          result = Basic.STRING;
      }

      if (result==null) {
        result=new ArgoAny(foundType);
      }
      
      return result;
    
    }
        
    /**
     *	@return true if the given MOperation names and parameters match the given name 
     *		and parameters
     */
    protected boolean operationMatchesCall(MOperation operation, String callName, Type[] callParams)
    {
      if ( ! callName.equals(operation.getName()) )
      {
        return false;
      }
      List operationParameters = operation.getParameters();
      if (
        ! (((MParameter)operationParameters.get(0)).getKind().getValue()==MParameterDirectionKind._RETURN)
      ) 
      {
        System.err.println(
	  			"ArgoFacade$ArgoAny expects the first operation parameter to be the return type; this isn't the case"
				);
      }
      if (
        ! (
				  ((MParameter)operationParameters.get(0)).getKind().getValue()==MParameterDirectionKind._RETURN
          && operationParameters.size()==(callParams.length+1)
				)
      )
      {
        return false;
      }
      Iterator paramIter = operationParameters.iterator();
      paramIter.next(); // skip first parameter == return type
      int index = 0;
      while (paramIter.hasNext())
      {
			
        MParameter nextParam = (MParameter) paramIter.next();
				MClassifier paramType = nextParam.getType();
				Type operationParam = getOclRepresentation(paramType);
				if ( ! callParams[index].conformsTo(operationParam) )
				{
				  return false;
				}
        index++;
			}
      return true;
    }
}



