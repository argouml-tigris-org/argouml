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

package ru.novosoft.uml.behavior.common_behavior;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

public interface MAction extends MModelElement
{
  // generating attributes
  // attribute: script
  MActionExpression getScript();
  void setScript(MActionExpression __arg);
  // attribute: isAsynchronous
  boolean isAsynchronous();
  void setAsynchronous(boolean __arg);
  // attribute: target
  MObjectSetExpression getTarget();
  void setTarget(MObjectSetExpression __arg);
  // attribute: recurrence
  MIterationExpression getRecurrence();
  void setRecurrence(MIterationExpression __arg);
  // generating associations
  // opposite role: stimulus this role: dispatchAction
  Collection getStimuli();
  void setStimuli(Collection __arg);
  void addStimulus(MStimulus __arg);
  void removeStimulus(MStimulus __arg);
  void internalRefByStimulus(MStimulus __arg);
  void internalUnrefByStimulus(MStimulus __arg);
  // opposite role: actionSequence this role: action
  MActionSequence getActionSequence();
  void setActionSequence(MActionSequence __arg);
  void internalRefByActionSequence(MActionSequence __arg);
  void internalUnrefByActionSequence(MActionSequence __arg);
  // opposite role: actualArgument this role: action
  List getActualArguments();
  void setActualArguments(List __arg);
  void addActualArgument(MArgument __arg);
  void removeActualArgument(MArgument __arg);
  void addActualArgument(int __pos, MArgument __arg);
  void removeActualArgument(int __pos);
  void setActualArgument(int __pos, MArgument __arg);
  MArgument getActualArgument(int __pos);
  // opposite role: message this role: action
  Collection getMessages();
  void setMessages(Collection __arg);
  void addMessage(MMessage __arg);
  void removeMessage(MMessage __arg);
  void internalRefByMessage(MMessage __arg);
  void internalUnrefByMessage(MMessage __arg);
  // opposite role: state3 this role: doActivity
  MState getState3();
  void setState3(MState __arg);
  void internalRefByState3(MState __arg);
  void internalUnrefByState3(MState __arg);
  // opposite role: transition this role: effect
  MTransition getTransition();
  void setTransition(MTransition __arg);
  void internalRefByTransition(MTransition __arg);
  void internalUnrefByTransition(MTransition __arg);
  // opposite role: state2 this role: exit
  MState getState2();
  void setState2(MState __arg);
  void internalRefByState2(MState __arg);
  void internalUnrefByState2(MState __arg);
  // opposite role: state1 this role: entry
  MState getState1();
  void setState1(MState __arg);
  void internalRefByState1(MState __arg);
  void internalUnrefByState1(MState __arg);
}
