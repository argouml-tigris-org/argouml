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

// OCLEditorModel.java -- interface declaration for the ocl editor's model.
//
// 02/15/2001  [sz9 ]   Created.
//
package tudresden.ocl20.gui;

import tudresden.ocl20.gui.events.*;

/** 
  * A model for the {@link OCLEditor}. This model represents a list of 
  * {@link ConstraintRepresentation OCL constraints} that can be edited
  * using the OCLEditor.
  *
  * @author  sz9
  */
public interface OCLEditorModel {
 
  /**
    * Return the number of constraints in this model.
    */
  public int getConstraintCount();
  
  /**
    * Return the constraint with the specified index.
    *
    * @param nIdx the index of the constraint to be returned. 0 <= nIdx < {@link #getConstraintCount}
    */
  public ConstraintRepresentation getConstraintAt (int nIdx);
  
  /**
    * Add a fresh constraint to the model.
    */
  public void addConstraint();
  
  /**
    * Remove the specified constraint from the model.
    *
    * @param nIdx the index of the constraint to be removed. 0 <= nIdx < {@link #getConstraintCount}
    */
  public void removeConstraintAt (int nIdx);
  
  /**
    * Add a listener to be informed of changes in the model.
    *
    * @param ccl the new listener
    */
  public void addConstraintChangeListener (ConstraintChangeListener ccl);

  /**
    * Remove a listener to be informed of changes in the model.
    *
    * @param ccl the listener to be removed
    */
  public void removeConstraintChangeListener (ConstraintChangeListener ccl);
}