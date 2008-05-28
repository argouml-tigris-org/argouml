/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * OCL Editor                                                        *
 * Copyright (C) 2001 Steffen Zschaler.                              *
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
 * See CREDITS file for further details.                             *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// EditingUtilities.java -- utility interface for parsing and splitting
//                          raw constraints
//
// 05/04/2001  [sz9 ]  Created.
//
package tudresden.ocl20.gui;

import java.io.IOException;
import tudresden.ocl20.core.OclModelException;
import tudresden.ocl20.core.parser.sablecc.analysis.AttrEvalException;
import tudresden.ocl20.core.parser.sablecc.lexer.LexerException;
import tudresden.ocl20.core.parser.sablecc.parser.ParserException;

/**
 * Utility interface for parsing and splitting raw constraints.
 *
 * @author sz9
 * @author Raphael Schmid
 */
public interface EditingUtilities {
  
  /**
   * Checks the specified name and returns true if it is a valid OCL name.
   */
  public boolean isValidConstraintName (String sName);
  
  /**
   * Check the specified constraint.
   * 
   * @exception OclModelException thrown by the methods of {@link OclModel OclModel} in case of an error.
   * @exception ParserException if a syntax error occurred.
   * @exception LexerException .
   * @exception AttrEvalException if a type checking error occurred.
   * @exception IOException if an I/O operation failed.
   */
  public void parseAndCheckConstraint (final String sConstraint)
    throws OclModelException, ParserException, LexerException, AttrEvalException, IOException;

  /**
   * Return whether auto split mode is on. If this returns true, constraint
   * representations should call {@link #splitConstraint} and create one
   * constraint representation per actual constraint.
   */
  public boolean getDoAutoSplit();

//  /**
//   * Split the specified constraint into its constituting constraints. E.g.
//   * <pre>
//   * context Test
//   * inv: a > 0
//   * inv: a < 10
//   * inv: a * 100 = 900
//   * </pre>
//   * would be split into three constraints:
//   * <pre>
//   * context Test
//   * inv: a > 0
//   *
//   * context Test
//   * inv: a < 10
//   *
//   * context Test
//   * inv: a * 100 = 900
//   * </pre>
//   *
//   * @return a list of abstract syntax trees, one per sub-constraint.
//   */
//  public List splitConstraint (OclTree ocltConstraint);
}