/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * OCL Compiler                                                      *
 * Copyright (C) 2001 Steffen Zschaler (sz9@inf.tu-dresden.de).       *
 * All rights reserved.                                              *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other projects, please visit the web site:       *
 * http://www-st.inf.tu-dresden.de/ (Chair home page) or             *
 * http://www-st.inf.tu-dresden.de/ocl/ (project home page)          *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// ConstraintChangeListener.java -- Listener to be informed about changes in an OCLEditorModel
//
// 02/15/2001  [sz9 ]   Created.
//
package tudresden.ocl20.gui.events;

import java.util.EventListener;

/** 
  * Listener to be informed about changes in an
  * {@link tudresden.ocl20.gui.OCLEditorModel}.
  *
  * @author  sz9
  */
public interface ConstraintChangeListener extends EventListener {
  public void constraintAdded (ConstraintChangeEvent cce);
  public void constraintRemoved (ConstraintChangeEvent cce);
  public void constraintNameChanged (ConstraintChangeEvent cce);
  public void constraintDataChanged (ConstraintChangeEvent cce);
}