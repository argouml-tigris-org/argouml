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

package uci.uml.ui.table;

import java.util.*;
import java.beans.*;

import uci.gef.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.generate.*;
import uci.uml.ui.ProjectBrowser;
import uci.uml.ui.Project;

public abstract class ColumnDescriptor {
  ////////////////////////////////////////////////////////////////
  // constants
  public static ColumnDescriptor Name       = new ColumnName();
  public static ColumnDescriptor Visibility = new ColumnVisibility();
  public static ColumnDescriptor FeatureVis = new ColumnFeatureVis();
  public static ColumnDescriptor Stereotype = new ColumnStereotype();

  public static ColumnDescriptor Abstract        = new ColumnAbstract();
  public static ColumnDescriptor Root            = new ColumnRoot();
  public static ColumnDescriptor Leaf            = new ColumnLeaf();
  public static ColumnDescriptor ClassVisibility = new ColumnClassVisibility();
  public static ColumnDescriptor ClassKeyword    = new ColumnClassKeyword();
  public static ColumnDescriptor Extends         = new ColumnExtends();
  public static ColumnDescriptor Implements      = new ColumnImplements();

  public static ColumnDescriptor SrcName = new ColumnSrcName();
  public static ColumnDescriptor SrcType = new ColumnSrcType();
  public static ColumnDescriptor SrcMult = new ColumnSrcMultiplicity();
  public static ColumnDescriptor SrcNav  = new ColumnSrcNavigability();

  public static ColumnDescriptor DstName = new ColumnDstName();
  public static ColumnDescriptor DstType = new ColumnDstType();
  public static ColumnDescriptor DstMult = new ColumnDstMultiplicity();
  public static ColumnDescriptor DstNav  = new ColumnDstNavigability();

  public static ColumnDescriptor Entry   = new ColumnEntry();
  public static ColumnDescriptor Exit    = new ColumnExit();
  public static ColumnDescriptor Parent  = new ColumnParent();

  public static ColumnDescriptor Source  = new ColumnSource();
  public static ColumnDescriptor Target  = new ColumnTarget();
  public static ColumnDescriptor Trigger = new ColumnTrigger();
  public static ColumnDescriptor Guard   = new ColumnGuard();
  public static ColumnDescriptor Effect  = new ColumnEffect();

  public static ColumnDescriptor Return      = new ColumnReturn();
  public static ColumnDescriptor OperKeyword = new ColumnOperKeyword();
  public static ColumnDescriptor Query       = new ColumnQuery();

  public static ColumnDescriptor Type        = new ColumnType();
  public static ColumnDescriptor AttrKeyword = new ColumnAttrKeyword();


  
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _name;
  protected Class _cls;
  protected boolean _editable;

  ////////////////////////////////////////////////////////////////
  // constructor
  public ColumnDescriptor(String name, java.lang.Class cls, boolean editable) {
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
    return super.isEditable(rowObj) && !(rowObj instanceof Pseudostate); }

  
  public Object getValueFor(Object target) {
    if (target instanceof Element) {
      Name n = ((Element) target).getName();
      String res = n.getBody();
      String ocl = "";
      if (target instanceof ElementImpl)
	ocl = ((ElementImpl)target).getOCLTypeStr();
      if (res.length() == 0) res = "(anon " + ocl +")";
      return res;
    }
    if (target instanceof Diagram)
      return ((Diagram)target).getName();
    
    return target.toString();
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Element)) return;
    if (!(value instanceof String)) return;
    Element e = (Element) target;
    String s = (String) value;
    if (s.startsWith("(anon")) return;
    try { e.setName(new Name(s)); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set name in ColumnName");
    }
  }  
} /* end class ColumnName */


class ColumnVisibility extends ColumnDescriptor {
  ColumnVisibility() { super("Visibility", VisibilityKind.class, true); }
  
  public Object getValueFor(Object target) {
    if (target instanceof ModelElement) {
      ElementOwnership eo = ((ModelElement) target).getElementOwnership();
      if (eo == null) return "N/A";
      VisibilityKind vk = eo.getVisibility();
      return vk.toString();
    }
    return "N/A";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof ModelElement)) return;
    if (!(value instanceof VisibilityKind)) {
      System.out.println("asdddd");
      return;
    }
    //    try {
      ElementOwnership oe = ((ModelElement)target).getElementOwnership();
      oe.setVisibility((VisibilityKind) value);
      //}
      //catch (PropertyVetoException pve) {
      //System.out.println("asdawasdw2easd");
      //}
  }  
} /* end class ColumnVisibility */


class ColumnFeatureVis extends ColumnDescriptor {
  ColumnFeatureVis() { super("Visibility", VisibilityKind.class, true); }
  
  public Object getValueFor(Object target) {
    if (target instanceof Feature) {
      VisibilityKind vk = ((Feature)target).getVisibility();
      return vk.toString();
    }
    return "N/A";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Feature)) return;
    if (!(value instanceof VisibilityKind)) return;
    try {
      ((Feature)target).setVisibility((VisibilityKind) value);
    }
    catch (PropertyVetoException pve) {
      System.out.println("asdawasdw2easd");
    }
  }  
} /* end class ColumnFeatureVis */


class ColumnStereotype extends ColumnDescriptor {
  ColumnStereotype() { super("Stereotype", String.class, true); }
  
  public Object getValueFor(Object target) {
    if (target instanceof ModelElement) {
      Vector stereos = ((ModelElement) target).getStereotype();
      if (stereos.size() == 1) {
	Stereotype st = (Stereotype) stereos.elementAt(0);
	return st.getName().getBody();
      }
      return "";
    }
    return "N/A";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof ModelElement)) return;
    if (!(value instanceof String)) return;
    try {
      String stereoName = (String) value;
      Stereotype s = new Stereotype(stereoName);
      Vector stereos = new Vector();
      stereos.addElement(s);
      //System.out.println("setting stereotype");
      ((ModelElement) target).setStereotype(stereos);
    }
    catch (PropertyVetoException pve) {
      System.out.println("could not set stereotype");
    } 
  }
} /* end class ColumnStereotype */


class ColumnSrcName extends ColumnDescriptor {
  ColumnSrcName() { super("SrcName", String.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return "N/A";
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
      return ae.getName().getBody();
    }
    return "";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof IAssociation)) return;
    if (!(value instanceof String)) return;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
      try { ae.setName(new Name((String)value)); }
      catch (PropertyVetoException pve) {
	System.out.println("could not set source name");
      }
    }
  }  
} /* end class ColumnSrcName */


class ColumnSrcType extends ColumnDescriptor {
  ColumnSrcType() { super("SrcType", String.class, false); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return "N/A";
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
     GeneratorDisplay g = GeneratorDisplay.SINGLETON;
     return g.generateClassifierRef(ae.getType());
    }
    return "";
  }

  public void setValueFor(Object target, Object value) {
  }  
} /* end class ColumnSrcType */


class ColumnSrcMultiplicity extends ColumnDescriptor {
  ColumnSrcMultiplicity() { super("SrcMult", String.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return "N/A";
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
      return GeneratorDisplay.Generate(ae.getMultiplicity());
    }
    return "";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof IAssociation)) return;
    if (!(value instanceof String)) return;
    String s = (String) value;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
      Multiplicity m = ParserDisplay.SINGLETON.parseMultiplicity(s);
      try { ae.setMultiplicity(m); }
      catch (PropertyVetoException pve) {
	System.out.println("could not set src Multiplicity");
      }
    }
  }  
} /* end class ColumnSrcMultiplicity */


class ColumnSrcNavigability extends ColumnDescriptor {
  ColumnSrcNavigability() { super("SrcNav", Boolean.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return Boolean.FALSE;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
      boolean nav = ae.getIsNavigable();
      return nav ? Boolean.TRUE : Boolean.FALSE;
    }
    return Boolean.FALSE;
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof IAssociation)) return;
    if (!(value instanceof Boolean)) return;
    Boolean b = (Boolean) value;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(0);
      try { ae.setIsNavigable(b.booleanValue()); }
      catch (PropertyVetoException pve) {
	System.out.println("could not set src navigable");
      }
    }
  }  
} /* end class ColumnSrcNavigability */



class ColumnDstName extends ColumnDescriptor {
  ColumnDstName() { super("DstName", String.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return "N/A";
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
      return ae.getName().getBody();
    }
    return "";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof IAssociation)) return;
    if (!(value instanceof String)) return;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
      try { ae.setName(new Name((String)value)); }
      catch (PropertyVetoException pve) {
	System.out.println("could not set source name");
      }
    }
  }
} /* end class ColumnDstName */


class ColumnDstType extends ColumnDescriptor {
  ColumnDstType() { super("DstType", String.class, false); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return "N/A";
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
     GeneratorDisplay g = GeneratorDisplay.SINGLETON;
     return g.generateClassifierRef(ae.getType());
    }
    return "";
  }

  public void setValueFor(Object target, Object value) {
  }
} /* end class ColumnDstType */


class ColumnDstMultiplicity extends ColumnDescriptor {
  ColumnDstMultiplicity() { super("DstMult", String.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return "N/A";
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
      return GeneratorDisplay.Generate(ae.getMultiplicity());
    }
    return "";
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof IAssociation)) return;
    if (!(value instanceof String)) return;
    String s = (String) value;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
      Multiplicity m = ParserDisplay.SINGLETON.parseMultiplicity(s);
      try { ae.setMultiplicity(m); }
      catch (PropertyVetoException pve) {
	System.out.println("could not set dst Multiplicity");
      }
    }
  }  
} /* end class ColumnDstMultiplicity */



class ColumnDstNavigability extends ColumnDescriptor {
  ColumnDstNavigability() { super("DstNav", Boolean.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof IAssociation)) return Boolean.FALSE;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
      boolean nav = ae.getIsNavigable();
      return nav ? Boolean.TRUE : Boolean.FALSE;
    }
    return Boolean.FALSE;
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof IAssociation)) return;
    if (!(value instanceof Boolean)) return;
    Boolean b = (Boolean) value;
    Vector conns = ((IAssociation) target).getConnection();
    if (conns.size() == 2) {
      AssociationEnd ae = (AssociationEnd) conns.elementAt(1);
      try { ae.setIsNavigable(b.booleanValue()); }
      catch (PropertyVetoException pve) {
	System.out.println("could not set dst navigable");
      }
    }
  }  
} /* end class ColumnDstNavigability */


class ColumnAbstract extends ColumnDescriptor {
  ColumnAbstract() { super("Abstract", Boolean.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof GeneralizableElement)) return Boolean.FALSE;
    GeneralizableElement ge = (GeneralizableElement) target;
    boolean abs = ge.getIsAbstract();
    return abs ? Boolean.TRUE : Boolean.FALSE;
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof GeneralizableElement)) return;
    if (!(value instanceof Boolean)) return;
    boolean b = ((Boolean) value).booleanValue();
    GeneralizableElement ge = (GeneralizableElement) target;
    try { ge.setIsAbstract(b); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set abstract");
    }
  }  
} /* end class ColumnAbstract */


class ColumnRoot extends ColumnDescriptor {
  ColumnRoot() { super("Root", Boolean.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof GeneralizableElement)) return Boolean.FALSE;
    GeneralizableElement ge = (GeneralizableElement) target;
    boolean root = ge.getIsRoot();
    return root ? Boolean.TRUE : Boolean.FALSE;
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof GeneralizableElement)) return;
    if (!(value instanceof Boolean)) return;
    boolean b = ((Boolean) value).booleanValue();
    GeneralizableElement ge = (GeneralizableElement) target;
    try { ge.setIsRoot(b); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set root");
    }
  }  
} /* end class ColumnRoot */


class ColumnLeaf extends ColumnDescriptor {
  ColumnLeaf() { super("Leaf", Boolean.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof GeneralizableElement)) return Boolean.FALSE;
    GeneralizableElement ge = (GeneralizableElement) target;
    boolean leaf = ge.getIsLeaf();
    return leaf ? Boolean.TRUE : Boolean.FALSE;
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof GeneralizableElement)) return;
    if (!(value instanceof Boolean)) return;
    boolean b = ((Boolean) value).booleanValue();
    GeneralizableElement ge = (GeneralizableElement) target;
    try { ge.setIsLeaf(b); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set leaf");
    }
  }  
} /* end class ColumnLeaf */


class ColumnClassVisibility extends ColumnDescriptor {
  ColumnClassVisibility() {
    super("Visibility", MMClassVisibility.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Classifier)) return null;
    Classifier cls = (Classifier) target;
    return MMClassVisibility.VisibilityFor(cls);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Classifier)) return;
    if (!(value instanceof MMClassVisibility)) return;
    MMClassVisibility cv = (MMClassVisibility) value;
    Classifier cls = (Classifier) target;
    cv.set(cls);
  }  
} /* end class ColumnClassVisibility */


class ColumnClassKeyword extends ColumnDescriptor {
  ColumnClassKeyword() {
    super("Keyword", MMClassKeyword.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Classifier)) return null;
    Classifier cls = (Classifier) target;
    return MMClassKeyword.KeywordFor(cls);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Classifier)) return;
    if (!(value instanceof MMClassKeyword)) return;
    MMClassKeyword ck = (MMClassKeyword) value;
    Classifier cls = (Classifier) target;
    ck.set(cls);
  }  
} /* end class ColumnClassKeyword */


class ColumnExtends extends ColumnDescriptor {
  ColumnExtends() {
    super("Extends", String.class, false);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof GeneralizableElement)) return "";
    GeneralizableElement cls = (GeneralizableElement) target;
    Vector gen = cls.getGeneralization();
    String res = "";
    if  (gen == null || gen.size() == 0) return res;
    int size = gen.size();
    GeneratorDisplay gd = GeneratorDisplay.SINGLETON;
    for (int i = 0; i < size; i++) {
      Generalization g = (Generalization) gen.elementAt(i);
      Classifier base = (Classifier) g.getSupertype();
      res += gd.generateClassifierRef(base);
      if (i < size-1) res += ", ";
    }
    return res;
  }

  public void setValueFor(Object target, Object value) {  }  
} /* end class ColumnExtends */

class ColumnImplements extends ColumnDescriptor {
  ColumnImplements() {
    super("Implements", String.class, false);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof MMClass)) return "";
    MMClass cls = (MMClass) target;
    Vector gen = cls.getSpecification();
    String res = "";
    if  (gen == null || gen.size() == 0) return res;
    int size = gen.size();
    GeneratorDisplay gd = GeneratorDisplay.SINGLETON;
    for (int i = 0; i < size; i++) {
      Realization g = (Realization) gen.elementAt(i);
      Classifier base = (Classifier) g.getSupertype();
      res += gd.generateClassifierRef(base);
      if (i < size-1) res += ", ";
    }
    return res;
  }

  public void setValueFor(Object target, Object value) {  }  
} /* end class ColumnImplements */

// needs-more-work: states and use cases!

class ColumnEntry extends ColumnDescriptor {
  ColumnEntry() {
    super("Entry Action", String.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof State)) return "";
    State st = (State) target;
    ActionSequence acts = st.getEntry();
    return GeneratorDisplay.Generate(acts);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof State)) return;
    if (!(value instanceof String)) return;
    State st = (State) target;
    String s = (String) value;
    ParserDisplay pd = ParserDisplay.SINGLETON;
    pd.parseStateEntyAction(st, s);    
  }
} /* end class ColumnEntry */


class ColumnExit extends ColumnDescriptor {
  ColumnExit() {
    super("Exit Action", String.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof State)) return "";
    State st = (State) target;
    ActionSequence acts = st.getExit();
    return GeneratorDisplay.Generate(acts);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof State)) return;
    if (!(value instanceof String)) return;
    State st = (State) target;
    String s = (String) value;
    ParserDisplay pd = ParserDisplay.SINGLETON;
    pd.parseStateExitAction(st, s);    
  }
} /* end class ColumnExit */


class ColumnParent extends ColumnDescriptor {
  ColumnParent() {
    super("Parent State", String.class, false);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof StateVertex)) return "";
    StateVertex sv = (StateVertex) target;
    CompositeState cs = sv.getParent();
    return GeneratorDisplay.Generate(cs);
  }

  public void setValueFor(Object target, Object value) { }
} /* end class ColumnParent */


////////////////////////////////////////////////////////////////



class ColumnSource extends ColumnDescriptor {
  ColumnSource() {
    super("Source", String.class, false);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Transition)) return "";
    Transition t = (Transition) target;
    StateVertex sv = t.getSource();
    return GeneratorDisplay.Generate(sv);
  }

  public void setValueFor(Object target, Object value) { }
} /* end class ColumnSource */


class ColumnTarget extends ColumnDescriptor {
  ColumnTarget() {
    super("Target", String.class, false);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Transition)) return "";
    Transition t = (Transition) target;
    StateVertex sv = t.getTarget();
    return GeneratorDisplay.Generate(sv);
  }

  public void setValueFor(Object target, Object value) { }
} /* end class ColumnTarget */



class ColumnTrigger extends ColumnDescriptor {
  ColumnTrigger() {
    super("Trigger", String.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Transition)) return "";
    Transition t = (Transition) target;
    Event trigger = t.getTrigger();
    if (trigger == null) return "";
    return GeneratorDisplay.Generate(trigger);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Transition)) return;
    if (!(value instanceof String)) return;
    Transition tr = (Transition) target;
    String s = (String) value;
    ParserDisplay pd = ParserDisplay.SINGLETON;
    try { tr.setTrigger(pd.parseEvent(s)); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set trigger");
    }
  }
} /* end class ColumnTrigger */



class ColumnGuard extends ColumnDescriptor {
  ColumnGuard() {
    super("Guard", String.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Transition)) return "";
    Transition t = (Transition) target;
    Guard guard = t.getGuard();
    if (guard == null) return "";
    return GeneratorDisplay.Generate(guard);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Transition)) return;
    if (!(value instanceof String)) return;
    Transition tr = (Transition) target;
    String s = (String) value;
    ParserDisplay pd = ParserDisplay.SINGLETON;
    try { tr.setGuard(pd.parseGuard(s)); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set guard");
    }
  }
} /* end class ColumnGuard */


class ColumnEffect extends ColumnDescriptor {
  ColumnEffect() {
    super("Effect", String.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Transition)) return "";
    Transition t = (Transition) target;
    ActionSequence effect = t.getEffect();
    if (effect == null) return "";
    return GeneratorDisplay.Generate(effect);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Transition)) return;
    if (!(value instanceof String)) return;
    Transition tr = (Transition) target;
    String s = (String) value;
    ParserDisplay pd = ParserDisplay.SINGLETON;
    try { tr.setEffect(pd.parseActions(s)); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set effect");
    }
  }
} /* end class ColumnEffect */


class ColumnReturn extends ColumnDescriptor {
  ColumnReturn() {
    super("Return", String.class, true); //Classifier.type?
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Operation)) return "";
    Operation op = (Operation) target;
    Classifier returnType = op.getReturnType();
    GeneratorDisplay gd = GeneratorDisplay.SINGLETON;
    return gd.generateClassifierRef(returnType);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Operation)) return;
    if (!(value instanceof String)) return;
    Operation op = (Operation) target;
    String s = (String) value;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Classifier rt = p.findType(s);
    ParserDisplay pd = ParserDisplay.SINGLETON;
    try { op.setReturnType(rt); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set return type");
    }
  }
} /* end class ColumnReturn */

class ColumnOperKeyword extends ColumnDescriptor {
  ColumnOperKeyword() {
    super("Keyword", uci.uml.ui.table.OperKeyword.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Operation)) return null;
    Operation oper = (Operation) target;
    return uci.uml.ui.table.OperKeyword.KeywordFor(oper);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Operation)) return;
    if (!(value instanceof OperKeyword)) return;
    OperKeyword ok = (OperKeyword) value;
    Operation oper = (Operation) target;
    ok.set(oper);
  }  
} /* end class ColumnOperKeyword */


class ColumnQuery extends ColumnDescriptor {
  ColumnQuery() { super("Query", Boolean.class, true); }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Operation)) return Boolean.FALSE;
    Operation oper = (Operation) target;
    boolean query = oper.getIsQuery();
    return query ? Boolean.TRUE : Boolean.FALSE;
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Operation)) return;
    if (!(value instanceof Boolean)) return;
    boolean b = ((Boolean) value).booleanValue();
    Operation oper = (Operation) target;
    try { oper.setIsQuery(b); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set query");
    }
  }  
} /* end class ColumnQuery */


class ColumnType extends ColumnDescriptor {
  ColumnType() {
    super("Type", String.class, true);  //Classifier.type?
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Attribute)) return null;
    Attribute op = (Attribute) target;
    Classifier type = op.getType();
    GeneratorDisplay gd = GeneratorDisplay.SINGLETON;
    return gd.generateClassifierRef(type);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Attribute)) return;
    if (!(value instanceof String)) return;
    Attribute op = (Attribute) target;
    String s = (String) value;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Classifier t = p.findType(s);
    if (t == null) {
      System.out.println("attribute type not found");
      return;
    }
    ParserDisplay pd = ParserDisplay.SINGLETON;
    try { op.setType(t); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set attribute type");
    }
  }
} /* end class ColumnType */

class ColumnAttrKeyword extends ColumnDescriptor {
  ColumnAttrKeyword() {
    super("Keyword", uci.uml.ui.table.AttrKeyword.class, true);
  }
  
  public Object getValueFor(Object target) {
    if (!(target instanceof Attribute)) return null;
    Attribute attr = (Attribute) target;
    return uci.uml.ui.table.AttrKeyword.KeywordFor(attr);
  }

  public void setValueFor(Object target, Object value) {
    if (!(target instanceof Attribute)) return;
    if (!(value instanceof AttrKeyword)) return;
    AttrKeyword ak = (AttrKeyword) value;
    Attribute attr = (Attribute) target;
    ak.set(attr);
  }  
} /* end class ColumnAttrKeyword */
