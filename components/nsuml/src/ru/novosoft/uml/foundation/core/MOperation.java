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

public interface MOperation extends MBehavioralFeature
{
  // generating attributes
  // attribute: specification
  String getSpecification();
  void setSpecification(String __arg);
  // attribute: isAbstract
  boolean isAbstract();
  void setAbstract(boolean __arg);
  // attribute: isLeaf
  boolean isLeaf();
  void setLeaf(boolean __arg);
  // attribute: isRoot
  boolean isRoot();
  void setRoot(boolean __arg);
  // attribute: concurrency
  MCallConcurrencyKind getConcurrency();
  void setConcurrency(MCallConcurrencyKind __arg);
  // generating associations
  // opposite role: callAction this role: operation
  Collection getCallActions();
  void setCallActions(Collection __arg);
  void addCallAction(MCallAction __arg);
  void removeCallAction(MCallAction __arg);
  void internalRefByCallAction(MCallAction __arg);
  void internalUnrefByCallAction(MCallAction __arg);
  // opposite role: collaboration this role: representedOperation
  Collection getCollaborations();
  void setCollaborations(Collection __arg);
  void addCollaboration(MCollaboration __arg);
  void removeCollaboration(MCollaboration __arg);
  void internalRefByCollaboration(MCollaboration __arg);
  void internalUnrefByCollaboration(MCollaboration __arg);
  // opposite role: occurrence this role: operation
  Collection getOccurrences();
  void setOccurrences(Collection __arg);
  void addOccurrence(MCallEvent __arg);
  void removeOccurrence(MCallEvent __arg);
  void internalRefByOccurrence(MCallEvent __arg);
  void internalUnrefByOccurrence(MCallEvent __arg);
  // opposite role: method this role: specification
  Collection getMethods();
  void setMethods(Collection __arg);
  void addMethod(MMethod __arg);
  void removeMethod(MMethod __arg);
  void internalRefByMethod(MMethod __arg);
  void internalUnrefByMethod(MMethod __arg);
}
