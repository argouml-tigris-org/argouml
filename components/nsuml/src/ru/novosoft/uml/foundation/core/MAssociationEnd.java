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

public interface MAssociationEnd extends MModelElement
{
  // generating attributes
  // attribute: changeability
  MChangeableKind getChangeability();
  void setChangeability(MChangeableKind __arg);
  // attribute: multiplicity
  MMultiplicity getMultiplicity();
  void setMultiplicity(MMultiplicity __arg);
  // attribute: targetScope
  MScopeKind getTargetScope();
  void setTargetScope(MScopeKind __arg);
  // attribute: aggregation
  MAggregationKind getAggregation();
  void setAggregation(MAggregationKind __arg);
  // attribute: ordering
  MOrderingKind getOrdering();
  void setOrdering(MOrderingKind __arg);
  // attribute: isNavigable
  boolean isNavigable();
  void setNavigable(boolean __arg);
  // generating associations
  // opposite role: linkEnd this role: associationEnd
  Collection getLinkEnds();
  void setLinkEnds(Collection __arg);
  void addLinkEnd(MLinkEnd __arg);
  void removeLinkEnd(MLinkEnd __arg);
  void internalRefByLinkEnd(MLinkEnd __arg);
  void internalUnrefByLinkEnd(MLinkEnd __arg);
  // opposite role: associationEndRole this role: base
  Collection getAssociationEndRoles();
  void setAssociationEndRoles(Collection __arg);
  void addAssociationEndRole(MAssociationEndRole __arg);
  void removeAssociationEndRole(MAssociationEndRole __arg);
  void internalRefByAssociationEndRole(MAssociationEndRole __arg);
  void internalUnrefByAssociationEndRole(MAssociationEndRole __arg);
  // opposite role: specification this role: participant
  Collection getSpecifications();
  void setSpecifications(Collection __arg);
  void addSpecification(MClassifier __arg);
  void removeSpecification(MClassifier __arg);
  void internalRefBySpecification(MClassifier __arg);
  void internalUnrefBySpecification(MClassifier __arg);
  // opposite role: type this role: associationEnd
  MClassifier getType();
  void setType(MClassifier __arg);
  void internalRefByType(MClassifier __arg);
  void internalUnrefByType(MClassifier __arg);
  // opposite role: qualifier this role: associationEnd
  List getQualifiers();
  void setQualifiers(List __arg);
  void addQualifier(MAttribute __arg);
  void removeQualifier(MAttribute __arg);
  void addQualifier(int __pos, MAttribute __arg);
  void removeQualifier(int __pos);
  void setQualifier(int __pos, MAttribute __arg);
  MAttribute getQualifier(int __pos);
  // opposite role: association this role: connection
  MAssociation getAssociation();
  void setAssociation(MAssociation __arg);
  void internalRefByAssociation(MAssociation __arg);
  void internalUnrefByAssociation(MAssociation __arg);
  /** get opposite association end */
  public ru.novosoft.uml.foundation.core.MAssociationEnd getOppositeEnd();
}
