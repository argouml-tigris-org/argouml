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

public interface MUseCase extends MClassifier
{
  // generating attributes
  // generating associations
  // opposite role: extensionPoint this role: useCase
  Collection getExtensionPoints();
  void setExtensionPoints(Collection __arg);
  void addExtensionPoint(MExtensionPoint __arg);
  void removeExtensionPoint(MExtensionPoint __arg);
  void internalRefByExtensionPoint(MExtensionPoint __arg);
  void internalUnrefByExtensionPoint(MExtensionPoint __arg);
  // opposite role: include2 this role: base
  Collection getIncludes2();
  void setIncludes2(Collection __arg);
  void addInclude2(MInclude __arg);
  void removeInclude2(MInclude __arg);
  void internalRefByInclude2(MInclude __arg);
  void internalUnrefByInclude2(MInclude __arg);
  // opposite role: include this role: addition
  Collection getIncludes();
  void setIncludes(Collection __arg);
  void addInclude(MInclude __arg);
  void removeInclude(MInclude __arg);
  void internalRefByInclude(MInclude __arg);
  void internalUnrefByInclude(MInclude __arg);
  // opposite role: extend this role: extension
  Collection getExtends();
  void setExtends(Collection __arg);
  void addExtend(MExtend __arg);
  void removeExtend(MExtend __arg);
  void internalRefByExtend(MExtend __arg);
  void internalUnrefByExtend(MExtend __arg);
  // opposite role: extend2 this role: base
  Collection getExtends2();
  void setExtends2(Collection __arg);
  void addExtend2(MExtend __arg);
  void removeExtend2(MExtend __arg);
  void internalRefByExtend2(MExtend __arg);
  void internalUnrefByExtend2(MExtend __arg);
}
