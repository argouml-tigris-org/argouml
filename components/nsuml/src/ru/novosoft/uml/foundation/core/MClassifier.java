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

public interface MClassifier extends MNamespace, MGeneralizableElement
{
  // generating attributes
  // generating associations
  // opposite role: createAction this role: instantiation
  Collection getCreateActions();
  void setCreateActions(Collection __arg);
  void addCreateAction(MCreateAction __arg);
  void removeCreateAction(MCreateAction __arg);
  void internalRefByCreateAction(MCreateAction __arg);
  void internalUnrefByCreateAction(MCreateAction __arg);
  // opposite role: instance this role: classifier
  Collection getInstances();
  void setInstances(Collection __arg);
  void addInstance(MInstance __arg);
  void removeInstance(MInstance __arg);
  void internalRefByInstance(MInstance __arg);
  void internalUnrefByInstance(MInstance __arg);
  // opposite role: collaboration this role: representedClassifier
  Collection getCollaborations();
  void setCollaborations(Collection __arg);
  void addCollaboration(MCollaboration __arg);
  void removeCollaboration(MCollaboration __arg);
  void internalRefByCollaboration(MCollaboration __arg);
  void internalUnrefByCollaboration(MCollaboration __arg);
  // opposite role: classifierRole this role: base
  Collection getClassifierRoles();
  void setClassifierRoles(Collection __arg);
  void addClassifierRole(MClassifierRole __arg);
  void removeClassifierRole(MClassifierRole __arg);
  void internalRefByClassifierRole(MClassifierRole __arg);
  void internalUnrefByClassifierRole(MClassifierRole __arg);
  // opposite role: classifierInState this role: type
  Collection getClassifiersInState();
  void setClassifiersInState(Collection __arg);
  void addClassifierInState(MClassifierInState __arg);
  void removeClassifierInState(MClassifierInState __arg);
  void internalRefByClassifierInState(MClassifierInState __arg);
  void internalUnrefByClassifierInState(MClassifierInState __arg);
  // opposite role: objectFlowState this role: type
  Collection getObjectFlowStates();
  void setObjectFlowStates(Collection __arg);
  void addObjectFlowState(MObjectFlowState __arg);
  void removeObjectFlowState(MObjectFlowState __arg);
  void internalRefByObjectFlowState(MObjectFlowState __arg);
  void internalUnrefByObjectFlowState(MObjectFlowState __arg);
  // opposite role: powertypeRange this role: powertype
  Collection getPowertypeRanges();
  void setPowertypeRanges(Collection __arg);
  void addPowertypeRange(MGeneralization __arg);
  void removePowertypeRange(MGeneralization __arg);
  void internalRefByPowertypeRange(MGeneralization __arg);
  void internalUnrefByPowertypeRange(MGeneralization __arg);
  // opposite role: participant this role: specification
  Collection getParticipants();
  void setParticipants(Collection __arg);
  void addParticipant(MAssociationEnd __arg);
  void removeParticipant(MAssociationEnd __arg);
  void internalRefByParticipant(MAssociationEnd __arg);
  void internalUnrefByParticipant(MAssociationEnd __arg);
  // opposite role: associationEnd this role: type
  Collection getAssociationEnds();
  void setAssociationEnds(Collection __arg);
  void addAssociationEnd(MAssociationEnd __arg);
  void removeAssociationEnd(MAssociationEnd __arg);
  void internalRefByAssociationEnd(MAssociationEnd __arg);
  void internalUnrefByAssociationEnd(MAssociationEnd __arg);
  // opposite role: parameter this role: type
  Collection getParameters();
  void setParameters(Collection __arg);
  void addParameter(MParameter __arg);
  void removeParameter(MParameter __arg);
  void internalRefByParameter(MParameter __arg);
  void internalUnrefByParameter(MParameter __arg);
  // opposite role: structuralFeature this role: type
  Collection getStructuralFeatures();
  void setStructuralFeatures(Collection __arg);
  void addStructuralFeature(MStructuralFeature __arg);
  void removeStructuralFeature(MStructuralFeature __arg);
  void internalRefByStructuralFeature(MStructuralFeature __arg);
  void internalUnrefByStructuralFeature(MStructuralFeature __arg);
  // opposite role: feature this role: owner
  List getFeatures();
  void setFeatures(List __arg);
  void addFeature(MFeature __arg);
  void removeFeature(MFeature __arg);
  void addFeature(int __pos, MFeature __arg);
  void removeFeature(int __pos);
  void setFeature(int __pos, MFeature __arg);
  MFeature getFeature(int __pos);
}
