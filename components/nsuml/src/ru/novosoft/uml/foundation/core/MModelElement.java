/* Novosoft UML API for Java. Version 0.4.19
 * Copyright (C) 1999, 2000, NovoSoft.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */

package ru.novosoft.uml.foundation.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

public interface MModelElement extends MElement
{
  // generating attributes
  // attribute: isSpecification
  boolean isSpecification();
  void setSpecification(boolean __arg);
  // attribute: visibility
  MVisibilityKind getVisibility();
  void setVisibility(MVisibilityKind __arg);
  // attribute: name
  String getName();
  void setName(String __arg);
  // generating associations
  // opposite role: elementImport2 this role: modelElement
  Collection getElementImports2();
  void setElementImports2(Collection __arg);
  void addElementImport2(MElementImport __arg);
  void removeElementImport2(MElementImport __arg);
  void internalRefByElementImport2(MElementImport __arg);
  void internalUnrefByElementImport2(MElementImport __arg);
  // opposite role: classifierRole1 this role: availableContents
  Collection getClassifierRoles1();
  void setClassifierRoles1(Collection __arg);
  void addClassifierRole1(MClassifierRole __arg);
  void removeClassifierRole1(MClassifierRole __arg);
  void internalRefByClassifierRole1(MClassifierRole __arg);
  void internalUnrefByClassifierRole1(MClassifierRole __arg);
  // opposite role: collaboration1 this role: constrainingElement
  Collection getCollaborations1();
  void setCollaborations1(Collection __arg);
  void addCollaboration1(MCollaboration __arg);
  void removeCollaboration1(MCollaboration __arg);
  void internalRefByCollaboration1(MCollaboration __arg);
  void internalUnrefByCollaboration1(MCollaboration __arg);
  // opposite role: partition1 this role: contents
  Collection getPartitions1();
  void setPartitions1(Collection __arg);
  void addPartition1(MPartition __arg);
  void removePartition1(MPartition __arg);
  void internalRefByPartition1(MPartition __arg);
  void internalUnrefByPartition1(MPartition __arg);
  // opposite role: behavior this role: context
  Collection getBehaviors();
  void setBehaviors(Collection __arg);
  void addBehavior(MStateMachine __arg);
  void removeBehavior(MStateMachine __arg);
  void internalRefByBehavior(MStateMachine __arg);
  void internalUnrefByBehavior(MStateMachine __arg);
  // opposite role: stereotype this role: extendedElement
  MStereotype getStereotype();
  void setStereotype(MStereotype __arg);
  void internalRefByStereotype(MStereotype __arg);
  void internalUnrefByStereotype(MStereotype __arg);
  // opposite role: templateParameter2 this role: modelElement2
  Collection getTemplateParameters2();
  void setTemplateParameters2(Collection __arg);
  void addTemplateParameter2(MTemplateParameter __arg);
  void removeTemplateParameter2(MTemplateParameter __arg);
  void internalRefByTemplateParameter2(MTemplateParameter __arg);
  void internalUnrefByTemplateParameter2(MTemplateParameter __arg);
  // opposite role: elementResidence this role: resident
  Collection getElementResidences();
  void setElementResidences(Collection __arg);
  void addElementResidence(MElementResidence __arg);
  void removeElementResidence(MElementResidence __arg);
  void internalRefByElementResidence(MElementResidence __arg);
  void internalUnrefByElementResidence(MElementResidence __arg);
  // opposite role: comment this role: annotatedElement
  Collection getComments();
  void setComments(Collection __arg);
  void addComment(MComment __arg);
  void removeComment(MComment __arg);
  void internalRefByComment(MComment __arg);
  void internalUnrefByComment(MComment __arg);
  // opposite role: binding this role: argument
  Collection getBindings();
  void setBindings(Collection __arg);
  void addBinding(MBinding __arg);
  void removeBinding(MBinding __arg);
  void internalRefByBinding(MBinding __arg);
  void internalUnrefByBinding(MBinding __arg);
  // opposite role: templateParameter3 this role: defaultElement
  Collection getTemplateParameters3();
  void setTemplateParameters3(Collection __arg);
  void addTemplateParameter3(MTemplateParameter __arg);
  void removeTemplateParameter3(MTemplateParameter __arg);
  void internalRefByTemplateParameter3(MTemplateParameter __arg);
  void internalUnrefByTemplateParameter3(MTemplateParameter __arg);
  // opposite role: sourceFlow this role: source
  Collection getSourceFlows();
  void setSourceFlows(Collection __arg);
  void addSourceFlow(MFlow __arg);
  void removeSourceFlow(MFlow __arg);
  void internalRefBySourceFlow(MFlow __arg);
  void internalUnrefBySourceFlow(MFlow __arg);
  // opposite role: targetFlow this role: target
  Collection getTargetFlows();
  void setTargetFlows(Collection __arg);
  void addTargetFlow(MFlow __arg);
  void removeTargetFlow(MFlow __arg);
  void internalRefByTargetFlow(MFlow __arg);
  void internalUnrefByTargetFlow(MFlow __arg);
  // opposite role: templateParameter this role: modelElement
  List getTemplateParameters();
  void setTemplateParameters(List __arg);
  void addTemplateParameter(MTemplateParameter __arg);
  void removeTemplateParameter(MTemplateParameter __arg);
  void addTemplateParameter(int __pos, MTemplateParameter __arg);
  void removeTemplateParameter(int __pos);
  void setTemplateParameter(int __pos, MTemplateParameter __arg);
  MTemplateParameter getTemplateParameter(int __pos);
  // opposite role: presentation this role: subject
  Collection getPresentations();
  void setPresentations(Collection __arg);
  void addPresentation(MPresentationElement __arg);
  void removePresentation(MPresentationElement __arg);
  void internalRefByPresentation(MPresentationElement __arg);
  void internalUnrefByPresentation(MPresentationElement __arg);
  // opposite role: supplierDependency this role: supplier
  Collection getSupplierDependencies();
  void setSupplierDependencies(Collection __arg);
  void addSupplierDependency(MDependency __arg);
  void removeSupplierDependency(MDependency __arg);
  void internalRefBySupplierDependency(MDependency __arg);
  void internalUnrefBySupplierDependency(MDependency __arg);
  // opposite role: constraint this role: constrainedElement
  Collection getConstraints();
  void setConstraints(Collection __arg);
  void addConstraint(MConstraint __arg);
  void removeConstraint(MConstraint __arg);
  void internalRefByConstraint(MConstraint __arg);
  void internalUnrefByConstraint(MConstraint __arg);
  // opposite role: taggedValue this role: modelElement
  Collection getTaggedValues();
  void setTaggedValues(Collection __arg);
  void addTaggedValue(MTaggedValue __arg);
  void removeTaggedValue(MTaggedValue __arg);
  void internalRefByTaggedValue(MTaggedValue __arg);
  void internalUnrefByTaggedValue(MTaggedValue __arg);
  // opposite role: clientDependency this role: client
  Collection getClientDependencies();
  void setClientDependencies(Collection __arg);
  void addClientDependency(MDependency __arg);
  void removeClientDependency(MDependency __arg);
  void internalRefByClientDependency(MDependency __arg);
  void internalUnrefByClientDependency(MDependency __arg);
  // opposite role: namespace this role: ownedElement
  MNamespace getNamespace();
  void setNamespace(MNamespace __arg);
  void internalRefByNamespace(MNamespace __arg);
  void internalUnrefByNamespace(MNamespace __arg);
  /** get tagged value or null  if it was not set */
  public String getTaggedValue(String key);
  /** get tagged value with default */
  public String getTaggedValue(String key, String defValue);
  /** set tagged value */
  public void setTaggedValue(String key, String value);
  /** get model of which own this element */
  public ru.novosoft.uml.model_management.MModel getModel();
  /** is element a template */
  public boolean isTemplate(); 
  /** is element an instantiation */
  public boolean isInstantiation(); 
  public void removeTaggedValue(String key);
}
