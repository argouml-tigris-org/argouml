/*
 * PredNotInstanceOf.java
 */

package org.argouml.kernel;

import org.tigris.gef.util.*;

/**
 * Predicate to test if an Object is <B>NOT</B> an instance of a class.
 */
public class PredNotInstanceOf extends PredInstanceOf {

  public PredNotInstanceOf(Class cls) { 
    super(cls); 
  }

  public boolean predicate(Object obj) {
    return !super.predicate(obj);
  }

} /* end class PredNotInstanceOf */

