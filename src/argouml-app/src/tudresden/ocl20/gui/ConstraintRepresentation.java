/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * OCL Compiler                                                      *
 * Copyright (C) 2001 Steffen Zschaler (sz9@inf.tu-dresden.de).       *
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
 *                                                                   *
 * See CREDITS file for further details.                             *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// ConstraintRepresentation.java -- Representation of an event.
//
// 02/15/2001  [sz9 ]   Created.
//
package tudresden.ocl20.gui;

import java.io.IOException;

import tudresden.ocl20.core.OclModelException;
import tudresden.ocl20.core.parser.sablecc.analysis.AttrEvalException;
import tudresden.ocl20.core.parser.sablecc.lexer.LexerException;
import tudresden.ocl20.core.parser.sablecc.parser.ParserException;


/** 
  * A representation of an individual constraint with name and data.
  *
  * @author  sz9
  */
public interface ConstraintRepresentation {

  /**
    * Get the constraint's name.
    */
  public String getName();
  
  /**
   * Set the constraint's name. For the exceptions the detailed message must
   * be human readable.
   *
   * @param sName the new name of the constraint
   * @param euHelper utility that can be used to check for syntactical
   * correctness etc.
   *
   * @exception IllegalStateException if the constraint is not in a state to
   *     accept name changes.
   * @exception IllegalArgumentException if the specified name is not a legal
   *     name for the constraint.
   */
  public void setName (String sName, EditingUtilities euHelper)
      throws IllegalStateException, IllegalArgumentException;
  
  /**
    * Get the constraint's body text.
    */
  public String getData();
  
  /**
   * Set the constraint's body text. For the exceptions the detailed message must
   * be human readable.
   *
   * @param sData the new body of the constraint
   * @param euHelper utility that can be used to check for syntactical
   * correctness etc.
   *
   * @exception IllegalStateException if the constraint is not in a state to
   *     accept body changes.
   * @exception OclModelException thrown by the methods of {@link OclModel OclModel} in case of an error.
   * @exception ParserException if a syntax error occurred.
   * @exception LexerException .
   * @exception AttrEvalException if a type checking error occurred.
   * @exception IOException if an I/O operation failed.
   */
  public void setData (String sData, EditingUtilities euHelper)
      throws IllegalStateException, OclModelException, LexerException, AttrEvalException, ParserException, IOException;
}