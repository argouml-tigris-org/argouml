// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.ui.nav;

import java.util.*;
import javax.swing.tree.*;

/** This is a kind of TreeModel that is intended to be used as a
 *  sub-part of a TreeModelComposite, and it also has information
 *  about what type of object must be available in the tree before
 *  this class can be applied, and what objects might be in the tree
 *  afterwards.  The goal is to gray-out some of the rules in the
 *  NavigatorConfigDialog dialog box that would have no effect unless
 *  other rules are added first.
 *
 * @see TreeModelComposite
 * @see NavigatorConfigDialog */

public interface TreeModelPrereqs extends TreeModel {
  /** Some TreeModel's should be used in TreeModelComposite unless
   *  other TreeModel's have already added that will provide them with
   *  the kinds of objects that they can use a parents. This method
   *  should return a Vector of Class objects indicating which class
   *  of objects are needed before this TreeModelPrereqs can do any
   *  useful work. */
  Vector getPrereqs();

  /** Return the Vector of Classes of possible children. */
  Vector getProvidedTypes();

} /* end interface TreeModelPrereqs */
