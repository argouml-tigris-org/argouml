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

package ru.novosoft.uml.behavior.collaborations;

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
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

public interface MMessage extends MModelElement
{
  // generating attributes
  // generating associations
  // opposite role: action this role: message
  MAction getAction();
  void setAction(MAction __arg);
  void internalRefByAction(MAction __arg);
  void internalUnrefByAction(MAction __arg);
  // opposite role: communicationConnection this role: message
  MAssociationRole getCommunicationConnection();
  void setCommunicationConnection(MAssociationRole __arg);
  void internalRefByCommunicationConnection(MAssociationRole __arg);
  void internalUnrefByCommunicationConnection(MAssociationRole __arg);
  // opposite role: message3 this role: predecessor
  Collection getMessages3();
  void setMessages3(Collection __arg);
  void addMessage3(MMessage __arg);
  void removeMessage3(MMessage __arg);
  void internalRefByMessage3(MMessage __arg);
  void internalUnrefByMessage3(MMessage __arg);
  // opposite role: predecessor this role: message3
  Collection getPredecessors();
  void setPredecessors(Collection __arg);
  void addPredecessor(MMessage __arg);
  void removePredecessor(MMessage __arg);
  void internalRefByPredecessor(MMessage __arg);
  void internalUnrefByPredecessor(MMessage __arg);
  // opposite role: receiver this role: message1
  MClassifierRole getReceiver();
  void setReceiver(MClassifierRole __arg);
  void internalRefByReceiver(MClassifierRole __arg);
  void internalUnrefByReceiver(MClassifierRole __arg);
  // opposite role: sender this role: message2
  MClassifierRole getSender();
  void setSender(MClassifierRole __arg);
  void internalRefBySender(MClassifierRole __arg);
  void internalUnrefBySender(MClassifierRole __arg);
  // opposite role: activator this role: message4
  MMessage getActivator();
  void setActivator(MMessage __arg);
  void internalRefByActivator(MMessage __arg);
  void internalUnrefByActivator(MMessage __arg);
  // opposite role: message4 this role: activator
  Collection getMessages4();
  void setMessages4(Collection __arg);
  void addMessage4(MMessage __arg);
  void removeMessage4(MMessage __arg);
  void internalRefByMessage4(MMessage __arg);
  void internalUnrefByMessage4(MMessage __arg);
  // opposite role: interaction this role: message
  MInteraction getInteraction();
  void setInteraction(MInteraction __arg);
  void internalRefByInteraction(MInteraction __arg);
  void internalUnrefByInteraction(MInteraction __arg);
}
