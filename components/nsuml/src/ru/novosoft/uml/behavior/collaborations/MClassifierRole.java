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

public interface MClassifierRole extends MClassifier
{
  // generating attributes
  // attribute: multiplicity
  MMultiplicity getMultiplicity();
  void setMultiplicity(MMultiplicity __arg);
  // generating associations
  // opposite role: availableContents this role: classifierRole1
  Collection getAvailableContentses();
  void setAvailableContentses(Collection __arg);
  void addAvailableContents(MModelElement __arg);
  void removeAvailableContents(MModelElement __arg);
  void internalRefByAvailableContents(MModelElement __arg);
  void internalUnrefByAvailableContents(MModelElement __arg);
  // opposite role: message1 this role: receiver
  Collection getMessages1();
  void setMessages1(Collection __arg);
  void addMessage1(MMessage __arg);
  void removeMessage1(MMessage __arg);
  void internalRefByMessage1(MMessage __arg);
  void internalUnrefByMessage1(MMessage __arg);
  // opposite role: message2 this role: sender
  Collection getMessages2();
  void setMessages2(Collection __arg);
  void addMessage2(MMessage __arg);
  void removeMessage2(MMessage __arg);
  void internalRefByMessage2(MMessage __arg);
  void internalUnrefByMessage2(MMessage __arg);
  // opposite role: availableFeature this role: classifierRole
  Collection getAvailableFeatures();
  void setAvailableFeatures(Collection __arg);
  void addAvailableFeature(MFeature __arg);
  void removeAvailableFeature(MFeature __arg);
  void internalRefByAvailableFeature(MFeature __arg);
  void internalUnrefByAvailableFeature(MFeature __arg);
  // opposite role: base this role: classifierRole
  Collection getBases();
  void setBases(Collection __arg);
  void addBase(MClassifier __arg);
  void removeBase(MClassifier __arg);
  void internalRefByBase(MClassifier __arg);
  void internalUnrefByBase(MClassifier __arg);
}
