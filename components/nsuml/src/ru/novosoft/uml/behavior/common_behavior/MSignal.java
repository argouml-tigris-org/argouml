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

public interface MSignal extends MClassifier
{
  // generating attributes
  // generating associations
  // opposite role: context this role: raisedSignal
  Collection getContexts();
  void setContexts(Collection __arg);
  void addContext(MBehavioralFeature __arg);
  void removeContext(MBehavioralFeature __arg);
  void internalRefByContext(MBehavioralFeature __arg);
  void internalUnrefByContext(MBehavioralFeature __arg);
  // opposite role: reception this role: signal
  Collection getReceptions();
  void setReceptions(Collection __arg);
  void addReception(MReception __arg);
  void removeReception(MReception __arg);
  void internalRefByReception(MReception __arg);
  void internalUnrefByReception(MReception __arg);
  // opposite role: occurrence this role: signal
  Collection getOccurrences();
  void setOccurrences(Collection __arg);
  void addOccurrence(MSignalEvent __arg);
  void removeOccurrence(MSignalEvent __arg);
  void internalRefByOccurrence(MSignalEvent __arg);
  void internalUnrefByOccurrence(MSignalEvent __arg);
  // opposite role: sendAction this role: signal
  Collection getSendActions();
  void setSendActions(Collection __arg);
  void addSendAction(MSendAction __arg);
  void removeSendAction(MSendAction __arg);
  void internalRefBySendAction(MSendAction __arg);
  void internalUnrefBySendAction(MSendAction __arg);
}
