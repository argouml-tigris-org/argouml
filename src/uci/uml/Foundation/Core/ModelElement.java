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




// Source file: Foundation/Core/ModelElement.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
//nmw: import uci.uml.Behavioral_Elements.Collaborations.Collaboration;
import uci.uml.Model_Management.*;

/** Since Java does not allow multiple inheritiance, and some classes
 *  in the UML meta-model do inherit from ModelElement and another
 *  class, I have coded ModelElement as an interface.
 *
 * @see ModelElementImpl */


public interface ModelElement extends Element, java.io.Serializable {
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
  public Namespace getNamespace();
  public void setNamespace(Namespace x) throws PropertyVetoException;

  public Vector getConstraint();
  public void setConstraint(Vector x) throws PropertyVetoException;

  public Vector getProvision();
  public void setProvision(Vector x) throws PropertyVetoException;
  public void addProvision(Dependency x) throws PropertyVetoException;
  public void removeProvision(Dependency x) throws PropertyVetoException;

  public Vector getRequirement();
  public void setRequirement(Vector x) throws PropertyVetoException;
  public void addRequirement(Dependency x) throws PropertyVetoException;
  public void removeRequirement(Dependency x) throws PropertyVetoException;

  public Vector getTemplateParameter();
  public void setTemplateParameter(Vector x) throws PropertyVetoException;

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
  public void addBehavior(StateMachine x) throws PropertyVetoException;
  public void removeBehavior(StateMachine x) throws PropertyVetoException;

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

  public void addVetoableChangeListener(VetoableChangeListener listener);

  public void removeVetoableChangeListener(VetoableChangeListener listener);

  public String getOCLTypeStr();

  public Object prepareForTrash() throws PropertyVetoException;
  public void recoverFromTrash(Object momento) throws PropertyVetoException;

  static final long serialVersionUID = -4378657388222709582L;
}
