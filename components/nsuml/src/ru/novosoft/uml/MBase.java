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

package ru.novosoft.uml;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

public interface MBase
{
  // generating attributes
  // generating associations
  // opposite role: extension this role: baseElement
  Collection getExtensions();
  void setExtensions(Collection __arg);
  void addExtension(MExtension __arg);
  void removeExtension(MExtension __arg);
  void internalRefByExtension(MExtension __arg);
  void internalUnrefByExtension(MExtension __arg);
  MFactory getFactory();

  /** is the object reffernced */
  //public boolean isRefferenced();

  /** remove element from the model */
  public void remove();

  /** check if object was removed */
  public boolean isRemoved();

  /** add listener to this element */
  public void addMElementListener(MElementListener l);

  /** remove listener from this element */
  public void removeMElementListener(MElementListener l);

  // Reflective API

  public Object reflectiveGetValue(String feature);
  public void reflectiveSetValue(String feature, Object obj);
  public void reflectiveAddValue(String feature, Object obj);
  public void reflectiveRemoveValue(String feature, Object obj);
  public Object reflectiveGetValue(String feature, int pos);
  public void reflectiveSetValue(String feature, int pos, Object obj);
  public void reflectiveAddValue(String feature, int pos, Object obj);
  public void reflectiveRemoveValue(String feature, int pos);

  // UUID support

  public String getUUID();
  public void setUUID(String uuid);

  // Return real UML class name

  public String getUMLClassName();

  // Return owner of element
  public MModelElement getModelElementContainer();

  // Return union of composite associations
  public Collection getModelElementContents();
}
