// Source file: Foundation/Core/ModelElement.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
//nmw: import uci.uml.Behavioral_Elements.Collaborations.Collaboration;
import uci.uml.Model_Management.*;


public interface ModelElement extends Element {
  //    public Name _name;
  //    public Namespace _namespace;
  //    public Constraint _constraint[];
  //    public Dependency _provision[];
  //    public TaggedValue _taggedValue[];
  //    public Dependency _requirement[];
  //    public ViewElement _view[];
  //    public Binding _binding;
  //    public ModelElement _templateParameter[];
  //    public ModelElement _template;
  //    public Component _implementation[];
  //    public Stereotype _stereotype[];
  //    public StateMachine _behavior[];
  //    public Partition _partition;
  //    public Collaboration _collaboration[];
  //    public Package Uci.Uml._package[];
    
  //-     public Namespace getNamespace();
  //-     public void setNamespace(Namespace x);
  
  public ElementOwnership getElementOwnership();
  public void setElementOwnership(ElementOwnership x) throws PropertyVetoException;
  
  public Vector getConstraint();
  public void setConstraint(Vector x) throws PropertyVetoException;
  
  public Vector getProvision();
  public void setProvision(Vector x) throws PropertyVetoException;
  
  public Vector getRequirement();
  public void setRequirement(Vector x) throws PropertyVetoException;
  
  public Vector getTemplateParameter();
  public void setTemplateparameter(Vector x) throws PropertyVetoException;

  public ModelElement getTemplate();
  public void setTemplate(ModelElement x) throws PropertyVetoException;

  //-  public Vector getPackage();
  //-  public void setPackage(Vector x);

  public Vector getElementReference();
  public void setElementReference(Vector x) throws PropertyVetoException;

  public Vector getStereotype();
  public void setStereotype(Vector x) throws PropertyVetoException;

  public Vector getBehavior();
  public void setBehavior(Vector x) throws PropertyVetoException;

  //public Partition getPartition();
  //public void setPartition(Partition x);

  //public Collaboration[] getCollaboration();
  //public void setCollaboration(Collaboration[] x);

  //public Component[] getImplementation();
  //public void setImplementation(Component[] x);
  //public ViewElement[] getView();
  //public void setView(ViewElement[] x);

  //public Binding getBinding();
  //public void setBinding(Binding x);


}
