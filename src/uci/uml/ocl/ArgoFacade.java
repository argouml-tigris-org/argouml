package uci.uml.ocl;

import tudresden.ocl.check.types.*;
import tudresden.ocl.check.*;

import ru.novosoft.uml.foundation.core.*;

import uci.uml.util.MMUtil;
import uci.uml.ui.*;
import com.sun.java.util.collections.*;

public class ArgoFacade implements ModelFacade {

    public Any getClassifier(String name) {
	Project p = ProjectBrowser.TheInstance.getProject();
	MClassifier classifier = p.findTypeInModel(name, p.getCurrentNamespace());
	if (classifier == null) {
	    throw new OclTypeException("cannot find classifier: "+name);
	}
	ArgoAny result = new ArgoAny(classifier);
	return result;
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

	MClassifier foundType = null;

	// first search for appropriate attributes:
System.out.println("I'll look for attributes now");
	com.sun.java.util.collections.Collection attributes = MMUtil.SINGLETON.getAttributesInh(classifier);
	Iterator iter = attributes.iterator();
	while (iter.hasNext() && foundType == null) {
	    MAttribute attr = (MAttribute)iter.next();
	    if (attr.getName().equals(name)) {
			foundType = attr.getType();
	    }
	}

	// if no attribute was found, look for associations:
	if (foundType == null) {
		System.out.println("I'll look for associations now");
		com.sun.java.util.collections.Collection associationEnds = MMUtil.SINGLETON.getAssociateEndsInh(classifier);
		Iterator asciter = associationEnds.iterator();
		while (asciter.hasNext() && foundType == null) {
			MAssociationEnd ae = (MAssociationEnd)asciter.next();
			if (name.equals(ae.getName())) 
				foundType = ae.getType();
			else {
				String oppositeName = ae.getType().getName();
				if (oppositeName != null) {
					String lowerOppositeName = oppositeName.substring(0,1).toLowerCase();
					lowerOppositeName += oppositeName.substring(1);
					System.out.println("lowerOppositeName is: "+lowerOppositeName);
					if (lowerOppositeName.equals(name))
						foundType = ae.getType();
				}
			}
		}
	}

	if (foundType == null) { throw new OclTypeException("attribute "+name+" not found in classifier "+toString());}

	if (foundType.getName().equals("int") || foundType.getName().equals("Integer")) {
	    return Basic.INTEGER;
	}

	if (foundType.getName().equals("float") || foundType.getName().equals("double")) {
	    return Basic.REAL;
	}

	if (foundType.getName().equals("bool") || foundType.getName().equals("Boolean")) {
	    return Basic.BOOLEAN;
	}

	if (foundType.getName().equals("String")){
	    return Basic.STRING;
	}
	
	return new ArgoAny(foundType);
    }
    
    public Type navigateParameterized(String name, Type[] params) {
	if (classifier == null) {
	    throw new OclTypeException("attempting to access features of Void");
	}

       	Type type = Basic.navigateAnyParameterized(name, params);
	if (type != null) return type;

	MOperation foundOp = null;
	com.sun.java.util.collections.Collection operations = MMUtil.SINGLETON.getOperations(classifier);
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


