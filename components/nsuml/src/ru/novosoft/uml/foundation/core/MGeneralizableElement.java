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

public interface MGeneralizableElement extends MModelElement
{
  // generating attributes
  // attribute: isAbstract
  boolean isAbstract();
  void setAbstract(boolean __arg);
  // attribute: isLeaf
  boolean isLeaf();
  void setLeaf(boolean __arg);
  // attribute: isRoot
  boolean isRoot();
  void setRoot(boolean __arg);
  // generating associations
  // opposite role: specialization this role: parent
  Collection getSpecializations();
  void setSpecializations(Collection __arg);
  void addSpecialization(MGeneralization __arg);
  void removeSpecialization(MGeneralization __arg);
  void internalRefBySpecialization(MGeneralization __arg);
  void internalUnrefBySpecialization(MGeneralization __arg);
  // opposite role: generalization this role: child
  Collection getGeneralizations();
  void setGeneralizations(Collection __arg);
  void addGeneralization(MGeneralization __arg);
  void removeGeneralization(MGeneralization __arg);
  void internalRefByGeneralization(MGeneralization __arg);
  void internalUnrefByGeneralization(MGeneralization __arg);
  /** replacement of derived attribute isRoot */
  //public boolean isRoot();
  /** replacement of derived attribute isLeaf */
  //public boolean isLeaf();
  /** get parents */
  public List getChildren();
  /** get childs */
  public List getParents();
}
