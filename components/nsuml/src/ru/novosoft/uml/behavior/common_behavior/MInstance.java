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

public interface MInstance extends MModelElement
{
  // generating attributes
  // generating associations
  // opposite role: stimulus2 this role: receiver
  Collection getStimuli2();
  void setStimuli2(Collection __arg);
  void addStimulus2(MStimulus __arg);
  void removeStimulus2(MStimulus __arg);
  void internalRefByStimulus2(MStimulus __arg);
  void internalUnrefByStimulus2(MStimulus __arg);
  // opposite role: componentInstance this role: resident
  MComponentInstance getComponentInstance();
  void setComponentInstance(MComponentInstance __arg);
  void internalRefByComponentInstance(MComponentInstance __arg);
  void internalUnrefByComponentInstance(MComponentInstance __arg);
  // opposite role: stimulus3 this role: sender
  Collection getStimuli3();
  void setStimuli3(Collection __arg);
  void addStimulus3(MStimulus __arg);
  void removeStimulus3(MStimulus __arg);
  void internalRefByStimulus3(MStimulus __arg);
  void internalUnrefByStimulus3(MStimulus __arg);
  // opposite role: stimulus1 this role: argument
  Collection getStimuli1();
  void setStimuli1(Collection __arg);
  void addStimulus1(MStimulus __arg);
  void removeStimulus1(MStimulus __arg);
  void internalRefByStimulus1(MStimulus __arg);
  void internalUnrefByStimulus1(MStimulus __arg);
  // opposite role: slot this role: instance
  Collection getSlots();
  void setSlots(Collection __arg);
  void addSlot(MAttributeLink __arg);
  void removeSlot(MAttributeLink __arg);
  void internalRefBySlot(MAttributeLink __arg);
  void internalUnrefBySlot(MAttributeLink __arg);
  // opposite role: linkEnd this role: instance
  Collection getLinkEnds();
  void setLinkEnds(Collection __arg);
  void addLinkEnd(MLinkEnd __arg);
  void removeLinkEnd(MLinkEnd __arg);
  void internalRefByLinkEnd(MLinkEnd __arg);
  void internalUnrefByLinkEnd(MLinkEnd __arg);
  // opposite role: attributeLink this role: value
  Collection getAttributeLinks();
  void setAttributeLinks(Collection __arg);
  void addAttributeLink(MAttributeLink __arg);
  void removeAttributeLink(MAttributeLink __arg);
  void internalRefByAttributeLink(MAttributeLink __arg);
  void internalUnrefByAttributeLink(MAttributeLink __arg);
  // opposite role: classifier this role: instance
  Collection getClassifiers();
  void setClassifiers(Collection __arg);
  void addClassifier(MClassifier __arg);
  void removeClassifier(MClassifier __arg);
  void internalRefByClassifier(MClassifier __arg);
  void internalUnrefByClassifier(MClassifier __arg);
}
