// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.OperKeyword;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.MMClassKeyword;
import org.argouml.uml.diagram.static_structure.MMClassVisibility;
import org.argouml.uml.generator.GeneratorDisplay;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.GraphModel;

import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.state_machines.MGuard;

import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;

import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * @deprecated this class is deprecated since 0.15.1 and should be removed
 *             in 0.15.2, due to the fact that the TableModel classes
 *             have never been used, and are not maintained,
 *
 *             There is no reason why someone cannot try to complete the
 *             TableModel implementation, however, a higher priority
 *             at the moment is to clean argouml of un maintained code.
 */
public abstract class ColumnDescriptor {

    ////////////////////////////////////////////////////////////////
    // constants
    public static ColumnDescriptor Name       = new ColumnName();
    public static ColumnDescriptor Visibility = new ColumnVisibility();
    public static ColumnDescriptor FeatureVis = new ColumnFeatureVis();
    public static ColumnDescriptor MStereotype = new ColumnStereotype();

    public static ColumnDescriptor Abstract        = new ColumnAbstract();
    public static ColumnDescriptor Root            = new ColumnRoot();
    public static ColumnDescriptor Leaf            = new ColumnLeaf();
    public static ColumnDescriptor ClassVisibility =
	new ColumnClassVisibility();
    public static ColumnDescriptor ClassKeyword    = new ColumnClassKeyword();
    public static ColumnDescriptor Extends         = new ColumnExtends();
    //nsuml Realization problem
    //  public static ColumnDescriptor Implements      = new ColumnImplements();

    public static ColumnDescriptor SrcName = new ColumnSrcName();
    public static ColumnDescriptor SrcType = new ColumnSrcType();
    public static ColumnDescriptor SrcMult = new ColumnSrcMultiplicity();
    public static ColumnDescriptor SrcNav  = new ColumnSrcNavigability();

    public static ColumnDescriptor DstName = new ColumnDstName();
    public static ColumnDescriptor DstType = new ColumnDstType();
    public static ColumnDescriptor DstMult = new ColumnDstMultiplicity();
    public static ColumnDescriptor DstNav  = new ColumnDstNavigability();

    public static ColumnDescriptor Supplier = new ColumnSupplier();
    public static ColumnDescriptor Client = new ColumnClient();

    public static ColumnDescriptor SrcLinkType = new ColumnSrcLinkType();
    public static ColumnDescriptor DstLinkType = new ColumnDstLinkType();

    public static ColumnDescriptor Entry   = new ColumnEntry();
    public static ColumnDescriptor Exit    = new ColumnExit();
    public static ColumnDescriptor Parent  = new ColumnParent();

    public static ColumnDescriptor Source  = new ColumnSource();
    public static ColumnDescriptor Target  = new ColumnTarget();
    public static ColumnDescriptor Trigger = new ColumnTrigger();
    public static ColumnDescriptor MGuard   = new ColumnGuard();
    public static ColumnDescriptor Effect  = new ColumnEffect();

    public static ColumnDescriptor Return      = new ColumnReturn();
    public static ColumnDescriptor OperKeyword = new ColumnOperKeyword();
    public static ColumnDescriptor Query       = new ColumnQuery();

    public static ColumnDescriptor Type        = new ColumnType();
    public static ColumnDescriptor AttrKeyword = new ColumnAttrKeyword();

    public static ColumnDescriptor CompNode = new ColumnCompNode();
    public static ColumnDescriptor CompNodeInstance =
	new ColumnCompNodeInstance();
    public static ColumnDescriptor ImplLocation = new ColumnImplLocation();
    public static ColumnDescriptor ComponentInstance =
	new ColumnComponentInstance();
    public static ColumnDescriptor BaseForObject = new ColumnBaseForObject();
    public static ColumnDescriptor BaseForComponentInstance =
	new ColumnBaseForComponentInstance();
    public static ColumnDescriptor BaseForNodeInstance =
	new ColumnBaseForNodeInstance();

    public static ColumnDescriptor Communication = new ColumnCommunication();
    public static ColumnDescriptor ActionType = new ColumnActionType();
    public static ColumnDescriptor Action = new ColumnAction();

  
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected String _name;
    protected Class _cls;
    protected boolean _editable;

    ////////////////////////////////////////////////////////////////
    // constructor
    public ColumnDescriptor(String name, java.lang.Class cls,
			    boolean editable)
    {
	_name = name;
	_cls = cls;
	_editable = editable;
    }
  
    ////////////////////////////////////////////////////////////////
    // TableModel implementation
  
    public String getName() { return _name; }
    public java.lang.Class getColumnClass() { return _cls; }
    public boolean isEditable(Object rowObj) { return _editable; }
  
    public abstract Object getValueFor(Object target);
    public abstract void setValueFor(Object target, Object value);
  
} /* end class ColumnDescriptor */


class ColumnName extends ColumnDescriptor {
    ColumnName() { super("Name", String.class, true); }

    public boolean isEditable(Object rowObj) {
	return super.isEditable(rowObj) && !(ModelFacade.isAPseudostate(rowObj)); 
    }

  
    public Object getValueFor(Object target) {
	if (ModelFacade.isAModelElement(target)) {
	    String res = ModelFacade.getName(target);
	    String ocl = "";
	    if (ModelFacade.isAElement(target))
		ocl = ModelFacade.getUMLClassName(target);
	    if (res == null || res.length() == 0) res = "(anon " + ocl + ")";
	    return res;
	}
	if (target instanceof Diagram)
	    return ((Diagram) target).getName();
    
	return target.toString();
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAModelElement(target))) return;
	if (!(value instanceof String)) return;
	Object e = /*(MModelElement)*/ target;
	String s = (String) value;
	if (s.startsWith("(anon")) return;
	ModelFacade.setName(e, s); 
    }  
} /* end class ColumnName */


class ColumnVisibility extends ColumnDescriptor {
    ColumnVisibility() { super("Visibility", MVisibilityKind.class, true); }
  
    public Object getValueFor(Object target) {
	if (ModelFacade.isAModelElement(target)) {
	    Object vk = ModelFacade.getVisibility(target);
	    if (vk == null || ModelFacade.getName(vk) == null) return "N/A";
	    return ModelFacade.getName(vk);
	}
	return "N/A";
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAModelElement(target))
	    || !(ModelFacade.isAVisibilityKind(value)))
	    return;
	ModelFacade.setVisibility(target, value);
    }  
} /* end class ColumnVisibility */


class ColumnFeatureVis extends ColumnDescriptor {
    ColumnFeatureVis() { super("Visibility", MVisibilityKind.class, true); }
  
    public Object getValueFor(Object target) {
	if (ModelFacade.isAFeature(target)) {
	    Object vk = ModelFacade.getVisibility(target);
	    if (vk != null) {
		return vk.toString();
            }
	}
	return "N/A";
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAFeature(target))) return;
	if (!(ModelFacade.isAVisibilityKind(value))) return;
	ModelFacade.setVisibility(target, value);
    }  
} /* end class ColumnFeatureVis */


class ColumnStereotype extends ColumnDescriptor {
    ColumnStereotype() { super("Stereotype", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (ModelFacade.isAModelElement(target)) {
            Object stereotype = null;
            if (ModelFacade.getStereotypes(target).size() > 0) {
                stereotype = ModelFacade.getStereotypes(target).iterator().next();
            }
	    if (stereotype != null && ModelFacade.getName(stereotype) != null) {
		return ModelFacade.getName(stereotype);
            }
	}
	return "N/A";
    }
	
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAModelElement(target))) return;
	if (!(value instanceof String)) return;
	String stereoName = (String) value;
	MStereotype s =
	    UmlFactory.getFactory().getExtensionMechanisms()
	    .buildStereotype(target,
			     stereoName,
			     ModelFacade.getNamespace(target));
    }
} /* end class ColumnStereotype */


class ColumnSrcName extends ColumnDescriptor {
    ColumnSrcName() { super("SrcName", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return "N/A";
        if (ModelFacade.getConnectionCount(target) != 2) return "";
        
        Object ae = ModelFacade.getConnections(target).iterator().next();
        if (ae != null && ModelFacade.getName(ae) != null)
            return ModelFacade.getName(ae);
	return "";
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAssociation(target))) return;
	if (!(value instanceof String)) return;
        if (ModelFacade.getConnectionCount(target) == 2) {
            Object ae = ModelFacade.getConnections(target).iterator().next();
        ModelFacade.setName(ae, (String)value);
        }
    }  
} /* end class ColumnSrcName */


class ColumnSrcType extends ColumnDescriptor {
    ColumnSrcType() { super("SrcType", String.class, false); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return "N/A";
	if (ModelFacade.getConnectionCount(target) == 2) {
            Object ae = ModelFacade.getConnections(target).iterator().next();
            if (ae != null
		&& ModelFacade.getType(ae) != null
		&& ModelFacade.getName(ModelFacade.getType(ae)) != null)
	    {
		GeneratorDisplay g = GeneratorDisplay.getInstance();
		return g.generateClassifierRef(ModelFacade.getType(ae));
	    }
	}
	return "";
    }

    public void setValueFor(Object target, Object value) {
    }  
} /* end class ColumnSrcType */


class ColumnSrcMultiplicity extends ColumnDescriptor {
    ColumnSrcMultiplicity() { super("SrcMult", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return "N/A";
        if (ModelFacade.getConnectionCount(target) != 2) return "";
        
        Object ae = ModelFacade.getConnections(target).iterator().next();
        if (ae != null && ModelFacade.getMultiplicity(ae) != null)
            return GeneratorDisplay.Generate(ModelFacade.getMultiplicity(ae));
	return "";
    }
 
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAssociation(target))) return;
	if (!(value instanceof String)) return;
        if (!(ModelFacade.getConnectionCount(target) == 2)) return;
        
        Object ae = ModelFacade.getConnections(target).iterator().next();
        Object m = ParserDisplay.SINGLETON.parseMultiplicity((String)value);
        ModelFacade.setMultiplicity(ae, m);
    }
    
} /* end class ColumnSrcMultiplicity */


class ColumnSrcNavigability extends ColumnDescriptor {
    ColumnSrcNavigability() { super("SrcNav", Boolean.class, true); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return Boolean.FALSE;
        if (ModelFacade.getConnectionCount(target) != 2) return Boolean.FALSE;
        
        Object ae = ModelFacade.getConnections(target).iterator().next();
        boolean nav = ModelFacade.isNavigable(ae);
        return nav ? Boolean.TRUE : Boolean.FALSE;
    }
 
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAssociation(target))) return;
	if (!(value instanceof Boolean)) return;
        if (!(ModelFacade.getConnectionCount(target) == 2)) return;
        
	Boolean b = (Boolean) value;
        Object ae = ModelFacade.getConnections(target).iterator().next();
        ModelFacade.setNavigable(ae, b.booleanValue()); 
    }
} /* end class ColumnSrcNavigability */



class ColumnDstName extends ColumnDescriptor {
    ColumnDstName() { super("DstName", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return Boolean.FALSE;
        if (ModelFacade.getConnectionCount(target) != 2) return Boolean.FALSE;
        
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        if (ae != null && ModelFacade.getName(ae) != null)
            return ModelFacade.getName(ae);
        return "";
    }
 
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAssociation(target))) return;
	if (!(value instanceof String)) return;
        if (!(ModelFacade.getConnectionCount(target) == 2)) return;
        
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        ModelFacade.setName(ae, (String)value);
    }
} /* end class ColumnDstName */


class ColumnDstType extends ColumnDescriptor {
    ColumnDstType() { super("DstType", String.class, false); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return "N/A";
        if (ModelFacade.getConnectionCount(target) != 2) return "";
        
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        if (ae != null
                && ModelFacade.getType(ae) != null
                && ModelFacade.getName(ModelFacade.getType(ae)) != null) {
            GeneratorDisplay g = GeneratorDisplay.getInstance();
            return g.generateClassifierRef(ModelFacade.getType(ae));
        }
        return "";
    }
 
    public void setValueFor(Object target, Object value) {
    }
} /* end class ColumnDstType */


class ColumnDstMultiplicity extends ColumnDescriptor {
    ColumnDstMultiplicity() { super("DstMult", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return "N/A";
        if (ModelFacade.getConnectionCount(target) != 2) return "";
        
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        if (ae != null && ModelFacade.getMultiplicity(ae) != null) {
            return GeneratorDisplay.Generate(ModelFacade.getMultiplicity(ae));
        }
        return "";
    }
 
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAssociation(target))) return;
	if (!(value instanceof String)) return;
        if (!(ModelFacade.getConnectionCount(target) == 2)) return;
        
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        Object m = ParserDisplay.SINGLETON.parseMultiplicity((String)value);
        ModelFacade.setMultiplicity(ae, m);
    }
} /* end class ColumnDstMultiplicity */



class ColumnDstNavigability extends ColumnDescriptor {
    ColumnDstNavigability() { super("DstNav", Boolean.class, true); }
  
    public Object getValueFor(Object target) {
	if (!ModelFacade.isAAssociation(target)) return Boolean.FALSE;
        if (ModelFacade.getConnectionCount(target) != 2) return Boolean.FALSE;
        
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        boolean nav = ModelFacade.isNavigable(ae);
        return nav ? Boolean.TRUE : Boolean.FALSE;
    }
 
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAssociation(target))) return;
	if (!(value instanceof Boolean)) return;
        if (!(ModelFacade.getConnectionCount(target) == 2)) return;
        
	Boolean b = (Boolean) value;
        Iterator it = ModelFacade.getConnections(target).iterator();
        it.next();
        Object ae = it.next();
        ModelFacade.setNavigable(ae, b.booleanValue());
    }
} /* end class ColumnDstNavigability */

class ColumnSupplier extends ColumnDescriptor {
    ColumnSupplier() { super("Supplier", String.class, false); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isADependency(target))) return "N/A";
	String name = "";
	Collection conns = ModelFacade.getSuppliers(target);
	if (conns != null && (conns.size() == 1)) {
	    Iterator it = conns.iterator();
	    while (it.hasNext()) {
		Object element = /*(MModelElement)*/ it.next();
		if (element != null && ModelFacade.getName(element) != null) {	
		    name = ModelFacade.getName(element);
		}
	    }
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }
} /* end class ColumnSupplier */

class ColumnClient extends ColumnDescriptor {
    ColumnClient() { super("Client", String.class, false); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isADependency(target))) return "N/A";
	String name = "";
	Collection conns = ModelFacade.getClients(target);
	if (conns != null && (conns.size() == 1)) {
	    Iterator it = conns.iterator();
	    while (it.hasNext()) {
		Object element = /*(MModelElement)*/ it.next();
		if (element != null && ModelFacade.getName(element) != null) {	
		    name = ModelFacade.getName(element);
		}
	    }
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }
} /* end class ColumnClient */

class ColumnSrcLinkType extends ColumnDescriptor {
    ColumnSrcLinkType() { super("SrcType", String.class, false); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isALink(target))) return "N/A";
	String name = "";
	Vector conns = new Vector(ModelFacade.getConnections(target));
	if (conns.size() == 2) {
	    MLinkEnd le = (MLinkEnd) conns.elementAt(0);
	    if (le != null
		&& le.getInstance() != null
		&& le.getInstance().getName() != null)
	    {
		name = le.getInstance().getName();
	    }
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }  
} /* end class ColumnSrcLinkType */

class ColumnDstLinkType extends ColumnDescriptor {
    ColumnDstLinkType() { super("DstType", String.class, false); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isALink(target))) return "N/A";
	String name = "";
	Vector conns = new Vector(ModelFacade.getConnections(target));
	if (conns.size() == 2) {
	    MLinkEnd le = (MLinkEnd) conns.elementAt(1);
	    if (le != null
		&& le.getInstance() != null
		&& le.getInstance().getName() != null)
	    {
		name = le.getInstance().getName();
	    }
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }  
} /* end class ColumnDstLinkType */

class ColumnAbstract extends ColumnDescriptor {
    ColumnAbstract() { super("Abstract", Boolean.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return Boolean.FALSE;
	Object generalizableElement = /*(MGeneralizableElement)*/ target;
	boolean abs = ModelFacade.isAbstract(generalizableElement);
	return abs ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return;
	if (!(value instanceof Boolean)) return;
	boolean b = ((Boolean) value).booleanValue();
	Object ge = /*(MGeneralizableElement)*/ target;
	ModelFacade.setAbstract(ge, b);
    }  
} /* end class ColumnAbstract */


class ColumnRoot extends ColumnDescriptor {
    ColumnRoot() { super("Root", Boolean.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return Boolean.FALSE;
	Object ge = /*(MGeneralizableElement)*/ target;
	boolean root = ModelFacade.isRoot(ge);
	return root ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return;
	if (!(value instanceof Boolean)) return;
	boolean b = ((Boolean) value).booleanValue();
	Object ge = /*(MGeneralizableElement)*/ target;
	ModelFacade.setRoot(ge, b);
    }
} /* end class ColumnRoot */


class ColumnLeaf extends ColumnDescriptor {
    ColumnLeaf() { super("Leaf", Boolean.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return Boolean.FALSE;
	Object ge = /*(MGeneralizableElement)*/ target;
	boolean leaf = ModelFacade.isLeaf(ge);
	return leaf ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return;
	if (!(value instanceof Boolean)) return;
	boolean b = ((Boolean) value).booleanValue();
	Object ge = /*(MGeneralizableElement)*/ target;
	ModelFacade.setLeaf(ge, b);
    }  
} /* end class ColumnLeaf */


class ColumnClassVisibility extends ColumnDescriptor {
    ColumnClassVisibility() {
	super("Visibility", MMClassVisibility.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAClassifier(target))) return null;
	Object cls = /*(MClassifier)*/ target;
	return MMClassVisibility.VisibilityFor((MClassifier)cls);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAClassifier(target))) return;
	if (!(value instanceof MMClassVisibility)) return;
	MMClassVisibility cv = (MMClassVisibility) value;
	Object cls = /*(MClassifier)*/ target;
	cv.set((MClassifier)cls);
    }
} /* end class ColumnClassVisibility */


class ColumnClassKeyword extends ColumnDescriptor {
    ColumnClassKeyword() {
	super("Keyword", MMClassKeyword.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAClassifier(target))) return null;
	Object cls = /*(MClassifier)*/ target;
	return MMClassKeyword.KeywordFor((MClassifier)cls);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAClassifier(target))) return;
	if (!(value instanceof MMClassKeyword)) return;
	MMClassKeyword ck = (MMClassKeyword) value;
	Object cls = /*(MClassifier)*/ target;
	ck.set((MClassifier)cls);
    }  
} /* end class ColumnClassKeyword */


class ColumnExtends extends ColumnDescriptor {
    ColumnExtends() {
	super("Extends", String.class, false);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAGeneralizableElement(target))) return "";
	Object cls = /*(MGeneralizableElement)*/ target;
	Vector gen = new Vector(ModelFacade.getGeneralizations(cls));
	String res = "";
	if  (gen == null || gen.size() == 0) return res;
	int size = gen.size();
	GeneratorDisplay gd = GeneratorDisplay.getInstance();
	for (int i = 0; i < size; i++) {
	    Object generalization = /*(MGeneralization)*/ gen.elementAt(i);
	    Object base = /*(MClassifier)*/ ModelFacade.getParent(generalization);
	    res += gd.generateClassifierRef(base);
	    if (i < size - 1) res += ", ";
	}
	return res;
    }

    public void setValueFor(Object target, Object value) {  }  
} /* end class ColumnExtends */

/*
  class ColumnImplements extends ColumnDescriptor {
  ColumnImplements() {
  super("Implements", String.class, false);
  }
  
  public Object getValueFor(Object target) {
  if (!(target instanceof MClass)) return "";
  MClass cls = (MClass) target;
  Vector gen = cls.getSpecification();
  String res = "";
  if  (gen == null || gen.size() == 0) return res;
  int size = gen.size();
  GeneratorDisplay gd = GeneratorDisplay.getInstance();
  for (int i = 0; i < size; i++) {
  Realization g = (Realization) gen.elementAt(i);
  MClassifier base = (MClassifier) g.getParenttype();
  res += gd.generateClassifierRef(base);
  if (i < size-1) res += ", ";
  }
  return res;
  }
  
  public void setValueFor(Object target, Object value) {  }  
  }*/ /* end class ColumnImplements */

// TODO: states and use cases!

class ColumnEntry extends ColumnDescriptor {
    ColumnEntry() {
	super("Entry Action", String.class, true);
    }
	
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAState(target))) return "";
	Object state = /*(MState)*/ target;
	if (ModelFacade.getEntry(state) != null) {
	    Object acts = ModelFacade.getEntry(state);
	    return GeneratorDisplay.Generate(acts);
	}
	return "";
    }
	
    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAState(target))) return;
	if (!(value instanceof String)) return;


	Object state = /*(MState)*/ target;
	String s = (String) value;
	ParserDisplay pd = ParserDisplay.SINGLETON;
	pd.parseStateEntyAction(state, s);    
    }
} /* end class ColumnEntry */


class ColumnExit extends ColumnDescriptor {
    ColumnExit() {
	super("Exit Action", String.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAState(target))) return "";
	Object state = /*(MState)*/ target;
	if (ModelFacade.getExit(state) != null) {
	    Object acts = ModelFacade.getExit(state);
	    return GeneratorDisplay.Generate(acts);
	}
	return "";
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAState(target))) return;
	if (!(value instanceof String)) return;
	Object state = /*(MState)*/ target;
	String s = (String) value;
	ParserDisplay pd = ParserDisplay.SINGLETON;
	pd.parseStateExitAction(state, s);    
    }
} /* end class ColumnExit */


class ColumnParent extends ColumnDescriptor {
    ColumnParent() {
	super("Parent MState", String.class, false);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAStateVertex(target))) return "";
	Object stateVertex = /*(MStateVertex)*/ target;
	if (ModelFacade.getContainer(stateVertex) != null) {
	    Object cs = ModelFacade.getContainer(stateVertex);
	    return GeneratorDisplay.Generate(cs);
	}
	return "";
    }

    public void setValueFor(Object target, Object value) { }
} /* end class ColumnParent */


////////////////////////////////////////////////////////////////



class ColumnSource extends ColumnDescriptor {
    ColumnSource() {
	super("Source", String.class, false);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isATransition(target))) return "";
	Object t = /*(MTransition)*/ target;
	if (ModelFacade.getSource(t) != null) {
	    Object sv = ModelFacade.getSource(t);
	    return GeneratorDisplay.Generate(sv);
	}
	return "";
    }

    public void setValueFor(Object target, Object value) { }
} /* end class ColumnSource */


class ColumnTarget extends ColumnDescriptor {
    ColumnTarget() {
	super("Target", String.class, false);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isATransition(target))) return "";
	Object t = /*(MTransition)*/ target;
	if (ModelFacade.getTarget(t) != null) {
	    Object sv = ModelFacade.getTarget(t);
	    return GeneratorDisplay.Generate(sv);
	}
	return "";
    }

    public void setValueFor(Object target, Object value) { }
} /* end class ColumnTarget */



class ColumnTrigger extends ColumnDescriptor {
    ColumnTrigger() {
	super("Trigger", String.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isATransition(target))) return "";
	Object t = /*(MTransition)*/ target;
	Object trigger = ModelFacade.getTrigger(t);
	if (trigger == null) return "";
	return GeneratorDisplay.Generate(trigger);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isATransition(target))) return;
	if (!(value instanceof String)) return;
	Object tr = /*(MTransition)*/ target;
	String s = (String) value;
	ParserDisplay pd = ParserDisplay.SINGLETON;
	ModelFacade.setTrigger(tr, pd.parseEvent(s));
    }
} /* end class ColumnTrigger */



class ColumnGuard extends ColumnDescriptor {
    ColumnGuard() {
	super("Guard", String.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isATransition(target))) return "";
	Object t = /*(MTransition)*/ target;
	Object guard = ModelFacade.getGuard(t);
	if (guard == null) return "";
	return GeneratorDisplay.Generate(guard);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isATransition(target))) return;
	if (!(value instanceof String)) return;
	Object tr = /*(MTransition)*/ target;
	String s = (String) value;
	ParserDisplay pd = ParserDisplay.SINGLETON;
	ModelFacade.setGuard(tr, pd.parseGuard(s));
    }
} /* end class ColumnGuard */


class ColumnEffect extends ColumnDescriptor {
    ColumnEffect() {
	super("Effect", String.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isATransition(target))) return "";
	Object t = /*(MTransition)*/ target;
	Object effect = ModelFacade.getEffect(t);
	if (effect == null) return "";
	return GeneratorDisplay.Generate(effect);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isATransition(target))) return;
	if (!(value instanceof String)) return;
	Object tr = /*(MTransition)*/ target;
	String s = (String) value;
	ParserDisplay pd = ParserDisplay.SINGLETON;
	ModelFacade.setEffect(tr, pd.parseAction(s));
    }
} /* end class ColumnEffect */


class ColumnReturn extends ColumnDescriptor {
    ColumnReturn() {
	super("Return", String.class, true); //MClassifier.type?
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAOperation(target))) return "";
	Object operation = /*(MOperation)*/ target;
	MParameter returnParameter = UmlHelper.getHelper().getCore().getReturnParameter(operation);
	if (returnParameter != null && ModelFacade.getType(returnParameter) != null) {
	    Object returnType = ModelFacade.getType(returnParameter);
	    GeneratorDisplay gd = GeneratorDisplay.getInstance();
	    return gd.generateClassifierRef(returnType);
	}
	return "";
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAOperation(target))) return;
	if (!(value instanceof String)) return;
	Object operation = /*(MOperation)*/ target;
	String s = (String) value;
	Project p = ProjectManager.getManager().getCurrentProject();
	Object rt = /*(MClassifier)*/p.findType(s);
	ParserDisplay pd = ParserDisplay.SINGLETON;
	Object rp = UmlFactory.getFactory().getCore().buildParameter(operation);
	ModelFacade.setType(rp, rt);
	UmlHelper.getHelper().getCore().setReturnParameter((MOperation)operation, (MParameter)rp);
    }
} /* end class ColumnReturn */

class ColumnOperKeyword extends ColumnDescriptor {
    ColumnOperKeyword() {
	super("Keyword", org.argouml.uml.OperKeyword.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAOperation(target))) return null;
	Object oper = /*(MOperation)*/ target;
	return org.argouml.uml.OperKeyword.KeywordFor((MOperation)oper);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAOperation(target))) return;
	if (!(value instanceof OperKeyword)) return;
	OperKeyword ok = (OperKeyword) value;
	Object oper = /*(MOperation)*/ target;
	ok.set((MOperation)oper);
    }
} /* end class ColumnOperKeyword */


class ColumnQuery extends ColumnDescriptor {
    ColumnQuery() { super("Query", Boolean.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAOperation(target))) return Boolean.FALSE;
	Object oper = /*(MOperation)*/ target;
	boolean query = ModelFacade.isQuery(oper);
	return query ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAOperation(target))) return;
	if (!(value instanceof Boolean)) return;
	boolean b = ((Boolean) value).booleanValue();
	Object oper = /*(MOperation)*/ target;
	ModelFacade.setQuery(oper, b);
    }  
} /* end class ColumnQuery */


class ColumnType extends ColumnDescriptor {
    protected static Logger cat = Logger.getLogger(ColumnType.class);
    ColumnType() {
	super("Type", String.class, true);  //MClassifier.type?
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAAttribute(target))) return null;
	Object op = /*(MAttribute)*/ target;
	Object type = ModelFacade.getType(op);
	GeneratorDisplay gd = GeneratorDisplay.getInstance();
	return gd.generateClassifierRef(type);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAttribute(target))) return;
	if (!(value instanceof String)) return;
	Object op = /*(MAttribute)*/ target;
	String s = (String) value;
	Project p = ProjectManager.getManager().getCurrentProject();
	Object t = /*(MClassifier)*/p.findType(s);
	if (t == null) {
	    cat.warn("attribute type not found");
	    return;
	}
	ParserDisplay pd = ParserDisplay.SINGLETON;
	ModelFacade.setType(op, t);
    }
} /* end class ColumnType */

class ColumnAttrKeyword extends ColumnDescriptor {
    ColumnAttrKeyword() {
	super("Keyword", org.argouml.uml.AttrKeyword.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAAttribute(target))) return null;
	Object attr = /*(MAttribute)*/ target;
	return org.argouml.uml.AttrKeyword.KeywordFor((MAttribute)attr);
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAAttribute(target))) return;
	if (!(value instanceof AttrKeyword)) return;
	AttrKeyword ak = (AttrKeyword) value;
	Object attr = /*(MAttribute)*/ target;
	ak.set((MAttribute)attr);
    }  
} /* end class ColumnAttrKeyword */

class ColumnCompNode extends ColumnDescriptor {
    ColumnCompNode() { super("DeploymentLocation", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAComponent(target))) return null;
	Object co = /*(MComponent)*/ target;
	Collection nodes = ModelFacade.getDeploymentLocations(co);  
	Object node = null;
	if ((nodes != null) && (nodes.size() > 0)) {
	    Iterator it = nodes.iterator();
	    while (it.hasNext()) {
		node = /*(MNode)*/ it.next();
	    }
	}
	String name = "";
	if (node != null) {
	    name = ModelFacade.getName(node);
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }  

} /* end class ColumnCompNode */


class ColumnCompNodeInstance extends ColumnDescriptor {
    ColumnCompNodeInstance() { super("NodeInstance", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAComponentInstance(target))) return null;
	Object co = /*(MComponentInstance)*/ target;
	Object node = ModelFacade.getNodeInstance(co);
	String name = "";
	if (node != null) {
	    name = ModelFacade.getName(node);
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }  

} /* end class ColumnCompNodeInstance */

class ColumnImplLocation extends ColumnDescriptor {
    ColumnImplLocation() {
	super("ImplementationLocation", String.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAClassifier(target) || ModelFacade.isAObject(target)))
	    return null;
	String name = "";
	if (ModelFacade.isAClassifier(target)) {
	    Object co = /*(MClassifier)*/ target;
	    Collection residences = ModelFacade.getElementResidences(co);  
	    if (residences != null) {
		Iterator it = residences.iterator();
		while (it.hasNext()) {
		    Object residence = /*(MElementResidence)*/ it.next();
		    Object element = ModelFacade.getResident(residence);
		    if (element == co) {
			Object component =
			    ModelFacade.getImplementationLocation(residence);
			name = ModelFacade.getName(component);
		    }
		}
	    }
	}
	else if (ModelFacade.isAObject(target)) {
	    Object obj = /*(MObject)*/ target;
	    Collection residences = ModelFacade.getElementResidences(obj);  
	    if (residences != null) {
		Iterator it = residences.iterator();
		while (it.hasNext()) {
		    Object residence = /*(MElementResidence)*/ it.next();
		    Object element = ModelFacade.getResident(residence);
		    if (element == obj) {
			Object component =
			    ModelFacade.getImplementationLocation(residence);
			name = ModelFacade.getName(component);
		    }
		}
	    }
	}
      
	return name;
    }    

    public void setValueFor(Object target, Object value) {
    }  

} /* end class ColumnImplLocation */

class ColumnComponentInstance extends ColumnDescriptor {
    ColumnComponentInstance() {
	super("ComponentInstance", String.class, true);
    }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAObject(target))) return null;
	Object co = /*(MObject)*/ target;
	String name = "";
	Object comp = ModelFacade.getComponentInstance(co);
	if (comp != null) {
	    name = ModelFacade.getName(comp);
	}
	return name;
    }

    public void setValueFor(Object target, Object value) {
    }  

} /* end class ColumnComponentInstance */

class ColumnBaseForObject extends ColumnDescriptor {
    ColumnBaseForObject() { super("Base", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAInstance(target))) return null;
	Object in = /*(MInstance)*/ target;
	String instance_base = "";
	Collection col = ModelFacade.getClassifiers(in);
	if (col != null && (col.size() > 0)) {
	    Iterator it = col.iterator();
	    while (it.hasNext()) {
		Object cls = /*(MClassifier)*/ it.next();
		if (cls != null && (ModelFacade.getName(cls) != null)) {
		    instance_base = ModelFacade.getName(cls);
  
		}
	    }
	}
	return instance_base;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAInstance(target))) return;
	if (!(value instanceof String)) return;
	Object tt = /*(MObject)*/ target;
	String _value = (String) value;
	Object classifier = UmlFactory.getFactory().getCore().buildClass(); 
	Collection col = ModelFacade.getClassifiers(tt);
	if ((col != null) && (col.size() > 0)) { 
	    Iterator itcol = col.iterator(); 
	    while (itcol.hasNext()) { 
		ModelFacade.removeClassifier(tt, itcol.next()); 
	    } 
	} 

	Vector diagrams =
	    ProjectManager.getManager().getCurrentProject().getDiagrams();
	GraphModel model = null;
	Vector v = new Vector();
	int size = diagrams.size();
	for (int i = 0; i < size; i++) {
	    Object o = diagrams.elementAt(i);
	    if (!(o instanceof Diagram)) continue;
	    if (ModelFacade.isAModel(o)) continue;
	    Diagram d = (Diagram) o;
	    model = d.getGraphModel(); 

	    if (!(model instanceof ClassDiagramGraphModel
		  || model instanceof DeploymentDiagramGraphModel))
		continue;
       
	    Vector nodes = model.getNodes();
	    int s = nodes.size();
	    for (int j = 0; j < s; j++) {
		Object node = /*(MModelElement)*/ nodes.elementAt(j);
		if (node != null && (ModelFacade.isAClass(node))) {
		    Object mclass = /*(MClass)*/ node;
		    if (ModelFacade.getNamespace(mclass) != ModelFacade.getNamespace(tt)) continue;
		    String class_name = ModelFacade.getName(mclass);
		    if (class_name != null && (class_name.equals(_value))) {
			v.addElement(mclass);
			ModelFacade.setClassifiers(tt, v);
			return; 
		    }      
		}
	    }
	}

	ModelFacade.setName(classifier, _value);
	v.addElement(classifier);
	ModelFacade.setClassifiers(tt, v);
    
    }  

} /* end class ColumnBaseForObject */

class ColumnBaseForComponentInstance extends ColumnDescriptor {
    ColumnBaseForComponentInstance() { super("Base", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAInstance(target))) return null;
	Object instance = /*(MInstance)*/ target;
	String instance_base = "";
	Collection col = ModelFacade.getClassifiers(instance);
	if (col != null && (col.size() > 0)) {
	    Iterator it = col.iterator();
	    while (it.hasNext()) {
		Object classifier = /*(MClassifier)*/ it.next();
		if (classifier != null && (ModelFacade.getName(classifier) != null)) {
		    instance_base = ModelFacade.getName(classifier);
		}
	    }
	}
	return instance_base;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAInstance(target))) return;
	if (!(value instanceof String)) return;
	Object componentInstance = /*(MComponentInstance)*/ target;
	String _value = (String) value;
	Object classifier =
	    UmlFactory.getFactory().getCore().createComponent(); 
	Collection col = ModelFacade.getClassifiers(componentInstance);
	if ((col != null) && (col.size() > 0)) {
	    Iterator itcol = col.iterator(); 
	    while (itcol.hasNext()) { 
		Object cls = /*(MClassifier)*/ itcol.next(); 
		ModelFacade.removeClassifier(componentInstance, cls); 
	    }
	}

	Vector diagrams =
	    ProjectManager.getManager().getCurrentProject().getDiagrams();
	GraphModel model = null;
	Vector v = new Vector();
	int size = diagrams.size();
	for (int i = 0; i < size; i++) {
	    Object o = diagrams.elementAt(i);
	    if (!(o instanceof Diagram)) continue;
	    if (ModelFacade.isAModel(o)) continue;
	    Diagram d = (Diagram) o;
	    model = d.getGraphModel(); 

	    if (!(model instanceof DeploymentDiagramGraphModel)) continue;
       
	    Vector nodes = model.getNodes();
	    int s = nodes.size();
	    for (int j = 0; j < s; j++) {
		Object node = /*(MModelElement)*/ nodes.elementAt(j);
		if (node != null && (ModelFacade.isAComponent(node))) {
		    Object mcomponent = /*(MComponent)*/ node;
		    if (ModelFacade.getNamespace(mcomponent) != ModelFacade.getNamespace(componentInstance)) continue;
		    String comp_name = ModelFacade.getName(mcomponent);
		    if (comp_name != null && (comp_name.equals(_value))) {
			v.addElement(mcomponent);
			ModelFacade.setClassifiers(componentInstance, v);
			return; 
		    }      
		}
	    }
	}

	ModelFacade.setName(classifier, _value);
	v.addElement(classifier);
	ModelFacade.setClassifiers(componentInstance, v);
    
    }  

} /* end class ColumnBaseForComponentInstance */


class ColumnBaseForNodeInstance extends ColumnDescriptor {
    ColumnBaseForNodeInstance() { super("Base", String.class, true); }
  
    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAInstance(target))) return null;
	Object in = /*(MInstance)*/ target;
	String instance_base = "";
	Collection col = ModelFacade.getClassifiers(in);
	if (col != null && (col.size() > 0)) {
	    Iterator it = col.iterator();
	    while (it.hasNext()) {
		Object cls = /*(MClassifier)*/ it.next();
		if (cls != null && (ModelFacade.getName(cls) != null)) {
		    instance_base = ModelFacade.getName(cls);
		}
	    }
	}
	return instance_base;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAInstance(target))) return;
	if (!(value instanceof String)) return;
	Object tt = /*(MNodeInstance)*/ target;
	String _value = (String) value;
	Object classifier = UmlFactory.getFactory().getCore().createNode(); 
	Collection col = ModelFacade.getClassifiers(tt);
	if ((col != null) && (col.size() > 0)) { 
	    Iterator itcol = col.iterator(); 
	    while (itcol.hasNext()) { 
		Object cls = /*(MClassifier)*/ itcol.next(); 
		ModelFacade.removeClassifier(tt, cls); 
	    } 
	} 

	Vector diagrams =
	    ProjectManager.getManager().getCurrentProject().getDiagrams();
	GraphModel model = null;
	Vector v = new Vector();
	int size = diagrams.size();
	for (int i = 0; i < size; i++) {
	    Object o = diagrams.elementAt(i);
	    if (!(o instanceof Diagram)) continue;
	    if (ModelFacade.isAModel(o)) continue;
	    Diagram d = (Diagram) o;
	    model = d.getGraphModel(); 

	    if (!(model instanceof DeploymentDiagramGraphModel)) continue;
       
	    Vector nodes = model.getNodes();
	    int s = nodes.size();
	    for (int j = 0; j < s; j++) {
		Object node = /*(MModelElement)*/ nodes.elementAt(j);
		if (node != null && (ModelFacade.isANode(node))) {
		    Object mnode = /*(MNode)*/ node;
		    if (ModelFacade.getNamespace(mnode) != ModelFacade.getNamespace(tt)) continue;
		    String node_name = ModelFacade.getName(mnode);
		    if (node_name != null && (node_name.equals(_value))) {
			v.addElement(mnode);
			ModelFacade.setClassifiers(tt, v);
			return;
		    }
		}
	    }
	}

	ModelFacade.setName(classifier, _value);
	v.addElement(classifier);
	ModelFacade.setClassifiers(tt, v);
    
    }  

} /* end class ColumnBaseForNodeInstance */

class ColumnCommunication extends ColumnDescriptor {
    ColumnCommunication() { super("Communication", String.class, true); }

    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAAction(target))) return null;
	Object act = target;
	boolean isAsync = ModelFacade.isAsynchronous(act);
	String async = "";
	if (isAsync) async = "true";
	else async = "false";
	return async;
    }

    public void setValueFor(Object target, Object value) {
    }

} /* end class ColumnCommunication */

class ColumnActionType extends ColumnDescriptor {
    ColumnActionType() { super("Action Type", String.class, true); }

    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAAction(target))) return null;
	Object act = target;
	String type = "";
	if (ModelFacade.isACallAction(act)) type = "Call";
	else if (ModelFacade.isASendAction(act)) type = "Send";
	else if (ModelFacade.isACreateAction(act)) type = "Create";
	else if (ModelFacade.isAReturnAction(act)) type = "Return";
	else if (ModelFacade.isADestroyAction(act)) type = "Destroy";
	return type;
    }

    public void setValueFor(Object target, Object value) {
    }

} /* end class ColumnActionType */

class ColumnAction extends ColumnDescriptor {
    ColumnAction() { super("Action", String.class, true); }

    public Object getValueFor(Object target) {
	if (!(ModelFacade.isAStimulus(target))) return null;
	Object sti = /*(MStimulus)*/ target;
	String action = "";
	if (ModelFacade.getDispatchAction(sti) != null
	    && ModelFacade.getName(ModelFacade.getDispatchAction(sti)) != null)
	    action = ModelFacade.getName(ModelFacade.getDispatchAction(sti));
	return action;
    }

    public void setValueFor(Object target, Object value) {
	if (!(ModelFacade.isAStimulus(target))) return;
	if (!(value instanceof String)) return;
	Object sti = /*(MStimulus)*/ target;
	String _value = (String) value;
	if (_value != null) {
	    Object dispatchaction = ModelFacade.getDispatchAction(sti);
	    ModelFacade.setName(dispatchaction, _value);
	}
    }

} /* end class ColumnAction */