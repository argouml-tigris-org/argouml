// Source file: Foundation/Core/ModelElement.java

package uci.uml.Foundation.Core;

import java.util.*;
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
    
    public Namespace getNamespace();
    public void setNamespace(Namespace x);

    public Vector getConstraint();
    public void setConstraint(Vector x);

    public Vector getProvision();
    public void setProvision(Vector x);

    public Vector getRequirement();
    public void setRequirement(Vector x);

    public Vector getTemplateParameter();
    public void setTemplateparameter(Vector x);

    public ModelElement getTemplate();
    public void setTemplate(ModelElement x);

    public Vector getPackage();
    public void setPackage(Vector x);

    public Vector getBehavior();
    public void setBehavior(Vector x);

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
