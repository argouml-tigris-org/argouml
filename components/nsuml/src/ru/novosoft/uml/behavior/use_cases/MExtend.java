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

package ru.novosoft.uml.behavior.use_cases;

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
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

public interface MExtend extends MRelationship
{
  // generating attributes
  // attribute: condition
  MBooleanExpression getCondition();
  void setCondition(MBooleanExpression __arg);
  // generating associations
  // opposite role: extensionPoint this role: extend
  List getExtensionPoints();
  void setExtensionPoints(List __arg);
  void addExtensionPoint(MExtensionPoint __arg);
  void removeExtensionPoint(MExtensionPoint __arg);
  void addExtensionPoint(int __pos, MExtensionPoint __arg);
  void removeExtensionPoint(int __pos);
  void setExtensionPoint(int __pos, MExtensionPoint __arg);
  MExtensionPoint getExtensionPoint(int __pos);
  // opposite role: extension this role: extend
  MUseCase getExtension();
  void setExtension(MUseCase __arg);
  void internalRefByExtension(MUseCase __arg);
  void internalUnrefByExtension(MUseCase __arg);
  // opposite role: base this role: extend2
  MUseCase getBase();
  void setBase(MUseCase __arg);
  void internalRefByBase(MUseCase __arg);
  void internalUnrefByBase(MUseCase __arg);
}
