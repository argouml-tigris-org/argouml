// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


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
  public Namespace getNamespace();
  public void setNamespace(Namespace x) throws PropertyVetoException;
  
  public Vector getConstraint();
  public void setConstraint(Vector x) throws PropertyVetoException;
  
  public Vector getProvision();
  public void setProvision(Vector x) throws PropertyVetoException;
  
  public Vector getRequirement();
  public void setRequirement(Vector x) throws PropertyVetoException;
  
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


}
