package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.tree.*;

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
