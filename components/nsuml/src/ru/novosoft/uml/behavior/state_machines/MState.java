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

package ru.novosoft.uml.behavior.state_machines;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

public interface MState extends MStateVertex
{
  // generating attributes
  // generating associations
  // opposite role: doActivity this role: state3
  MAction getDoActivity();
  void setDoActivity(MAction __arg);
  void internalRefByDoActivity(MAction __arg);
  void internalUnrefByDoActivity(MAction __arg);
  // opposite role: internalTransition this role: state
  Collection getInternalTransitions();
  void setInternalTransitions(Collection __arg);
  void addInternalTransition(MTransition __arg);
  void removeInternalTransition(MTransition __arg);
  void internalRefByInternalTransition(MTransition __arg);
  void internalUnrefByInternalTransition(MTransition __arg);
  // opposite role: deferrableEvent this role: state
  Collection getDeferrableEvents();
  void setDeferrableEvents(Collection __arg);
  void addDeferrableEvent(MEvent __arg);
  void removeDeferrableEvent(MEvent __arg);
  void internalRefByDeferrableEvent(MEvent __arg);
  void internalUnrefByDeferrableEvent(MEvent __arg);
  // opposite role: stateMachine this role: top
  MStateMachine getStateMachine();
  void setStateMachine(MStateMachine __arg);
  void internalRefByStateMachine(MStateMachine __arg);
  void internalUnrefByStateMachine(MStateMachine __arg);
  // opposite role: classifierInState this role: inState
  Collection getClassifiersInState();
  void setClassifiersInState(Collection __arg);
  void addClassifierInState(MClassifierInState __arg);
  void removeClassifierInState(MClassifierInState __arg);
  void internalRefByClassifierInState(MClassifierInState __arg);
  void internalUnrefByClassifierInState(MClassifierInState __arg);
  // opposite role: exit this role: state2
  MAction getExit();
  void setExit(MAction __arg);
  void internalRefByExit(MAction __arg);
  void internalUnrefByExit(MAction __arg);
  // opposite role: entry this role: state1
  MAction getEntry();
  void setEntry(MAction __arg);
  void internalRefByEntry(MAction __arg);
  void internalUnrefByEntry(MAction __arg);
}
