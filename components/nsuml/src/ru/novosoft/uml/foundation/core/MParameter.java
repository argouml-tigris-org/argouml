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

public interface MParameter extends MModelElement
{
  // generating attributes
  // attribute: kind
  MParameterDirectionKind getKind();
  void setKind(MParameterDirectionKind __arg);
  // attribute: defaultValue
  MExpression getDefaultValue();
  void setDefaultValue(MExpression __arg);
  // generating associations
  // opposite role: state this role: parameter
  Collection getStates();
  void setStates(Collection __arg);
  void addState(MObjectFlowState __arg);
  void removeState(MObjectFlowState __arg);
  void internalRefByState(MObjectFlowState __arg);
  void internalUnrefByState(MObjectFlowState __arg);
  // opposite role: event this role: parameter
  MEvent getEvent();
  void setEvent(MEvent __arg);
  void internalRefByEvent(MEvent __arg);
  void internalUnrefByEvent(MEvent __arg);
  // opposite role: type this role: parameter
  MClassifier getType();
  void setType(MClassifier __arg);
  void internalRefByType(MClassifier __arg);
  void internalUnrefByType(MClassifier __arg);
  // opposite role: behavioralFeature this role: parameter
  MBehavioralFeature getBehavioralFeature();
  void setBehavioralFeature(MBehavioralFeature __arg);
  void internalRefByBehavioralFeature(MBehavioralFeature __arg);
  void internalUnrefByBehavioralFeature(MBehavioralFeature __arg);
}
