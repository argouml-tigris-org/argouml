package org.argouml.ocl;

import java.util.*;

import tudresden.ocl.check.types.*;
import tudresden.ocl.check.*;

import ru.novosoft.uml.foundation.core.*;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.MMUtil;

public class ArgoFacade implements ModelFacade {

    public MClassifier target;

    public ArgoFacade(Object target) {
	if (target instanceof MClassifier)
	    this.target = (MClassifier)target;
    }

    public Any getClassifier(String name) {
      Project p = ProjectBrowser.TheInstance.getProject();
      if (target != null && target.getName() == name ) {
	  return new ArgoAny(target);
      }
      // else we have a problem: this is not clean!
      else {
	  MClassifier classifier = p.findTypeInModel(name, p.getCurrentNamespace());
	  if (classifier == null) {
	      throw new OclTypeException("cannot find classifier: "+name);
	  }
	  return new ArgoAny(classifier);
      }
    }
}

class ArgoAny implements Any {

    MClassifier classifier;

    ArgoAny (MClassifier classifier) {
      this.classifier = classifier;
    }

    public Type navigateQualified(String name, Type[] qualifiers) {

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
      java.util.Collection attributes = MMUtil.SINGLETON.getAttributesInh(classifier);
      Iterator iter = attributes.iterator();
      while (iter.hasNext() && foundAttribType == null) {
        MAttribute attr = (MAttribute)iter.next();
        if (attr.getName().equals(name)) {
          foundAttribType = attr.getType();
        }
      }

      // look for associations
      java.util.Collection associationEnds = MMUtil.SINGLETON.getAssociateEndsInh(classifier);
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

      Type result=null;

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

    public Type navigateParameterized(String name, Type[] params) {
      if (classifier == null) {
          throw new OclTypeException("attempting to access features of Void");
      }

      Type type = Basic.navigateAnyParameterized(name, params);
      if (type != null) return type;

      MOperation foundOp = null;
      java.util.Collection operations = MMUtil.SINGLETON.getOperations(classifier);
      Iterator iter = operations.iterator();
      while (iter.hasNext() && foundOp == null){
          MOperation op = (MOperation)iter.next();
          if (op.getName().equals(name)) {
            foundOp = op;
          }
      }

      if (foundOp == null) { throw new OclTypeException("operation "+name+" not found in classifier "+toString());}

      MParameter rp = MMUtil.SINGLETON.getReturnParameter(foundOp);

      if (rp == null || rp.getType() == null) {
          System.out.println("WARNING: supposing return type void!");
          return new ArgoAny(null);
      }
      MClassifier returnType = rp.getType();

      if (returnType.getName().equals("int") || returnType.getName().equals("Integer")) {
          return Basic.INTEGER;
      }

      if (returnType.getName().equals("float") || returnType.getName().equals("double")) {
          return Basic.REAL;
      }

      if (returnType.getName().equals("bool") || returnType.getName().equals("Boolean")) {
          return Basic.BOOLEAN;
      }

      if (returnType.getName().equals("String")){
          return Basic.STRING;
      }

      return new ArgoAny(returnType);
    }

    public boolean conformsTo(Type type) {
      return false;
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
      System.out.println("ArgoAny.hasState() has been called, but is not implemented yet!");
      return false;
    }
}


