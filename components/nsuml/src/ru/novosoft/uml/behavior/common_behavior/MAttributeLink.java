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

public interface MAttributeLink extends MModelElement
{
  // generating attributes
  // generating associations
  // opposite role: linkEnd this role: qualifiedValue
  MLinkEnd getLinkEnd();
  void setLinkEnd(MLinkEnd __arg);
  void internalRefByLinkEnd(MLinkEnd __arg);
  void internalUnrefByLinkEnd(MLinkEnd __arg);
  // opposite role: instance this role: slot
  MInstance getInstance();
  void setInstance(MInstance __arg);
  void internalRefByInstance(MInstance __arg);
  void internalUnrefByInstance(MInstance __arg);
  // opposite role: value this role: attributeLink
  MInstance getValue();
  void setValue(MInstance __arg);
  void internalRefByValue(MInstance __arg);
  void internalUnrefByValue(MInstance __arg);
  // opposite role: attribute this role: attributeLink
  MAttribute getAttribute();
  void setAttribute(MAttribute __arg);
  void internalRefByAttribute(MAttribute __arg);
  void internalUnrefByAttribute(MAttribute __arg);
}
