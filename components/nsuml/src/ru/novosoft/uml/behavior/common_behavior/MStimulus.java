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

public interface MStimulus extends MModelElement
{
  // generating attributes
  // generating associations
  // opposite role: dispatchAction this role: stimulus
  MAction getDispatchAction();
  void setDispatchAction(MAction __arg);
  void internalRefByDispatchAction(MAction __arg);
  void internalUnrefByDispatchAction(MAction __arg);
  // opposite role: communicationLink this role: stimulus
  MLink getCommunicationLink();
  void setCommunicationLink(MLink __arg);
  void internalRefByCommunicationLink(MLink __arg);
  void internalUnrefByCommunicationLink(MLink __arg);
  // opposite role: receiver this role: stimulus2
  MInstance getReceiver();
  void setReceiver(MInstance __arg);
  void internalRefByReceiver(MInstance __arg);
  void internalUnrefByReceiver(MInstance __arg);
  // opposite role: sender this role: stimulus3
  MInstance getSender();
  void setSender(MInstance __arg);
  void internalRefBySender(MInstance __arg);
  void internalUnrefBySender(MInstance __arg);
  // opposite role: argument this role: stimulus1
  Collection getArguments();
  void setArguments(Collection __arg);
  void addArgument(MInstance __arg);
  void removeArgument(MInstance __arg);
  void internalRefByArgument(MInstance __arg);
  void internalUnrefByArgument(MInstance __arg);
}
