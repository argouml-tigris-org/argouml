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

public interface MCollaboration extends MGeneralizableElement, MNamespace
{
  // generating attributes
  // generating associations
  // opposite role: representedOperation this role: collaboration
  MOperation getRepresentedOperation();
  void setRepresentedOperation(MOperation __arg);
  void internalRefByRepresentedOperation(MOperation __arg);
  void internalUnrefByRepresentedOperation(MOperation __arg);
  // opposite role: representedClassifier this role: collaboration
  MClassifier getRepresentedClassifier();
  void setRepresentedClassifier(MClassifier __arg);
  void internalRefByRepresentedClassifier(MClassifier __arg);
  void internalUnrefByRepresentedClassifier(MClassifier __arg);
  // opposite role: constrainingElement this role: collaboration1
  Collection getConstrainingElements();
  void setConstrainingElements(Collection __arg);
  void addConstrainingElement(MModelElement __arg);
  void removeConstrainingElement(MModelElement __arg);
  void internalRefByConstrainingElement(MModelElement __arg);
  void internalUnrefByConstrainingElement(MModelElement __arg);
  // opposite role: interaction this role: context
  Collection getInteractions();
  void setInteractions(Collection __arg);
  void addInteraction(MInteraction __arg);
  void removeInteraction(MInteraction __arg);
  void internalRefByInteraction(MInteraction __arg);
  void internalUnrefByInteraction(MInteraction __arg);
}
