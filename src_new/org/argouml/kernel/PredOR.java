/*
 * PredOR.java
 */

package org.argouml.kernel;

import org.tigris.gef.util.*;

/**
 * Predicate to provide logical <B>OR</B> between two other predicates.  
 * 
 * @author Eugenio Alvarez
 *
 */
public class PredOR implements Predicate {
  protected Predicate _predicate1;
  protected Predicate _predicate2;

  public PredOR(Predicate predicate1, Predicate predicate2) { 
    _predicate1 = predicate1; 
    _predicate2 = predicate2;
  }

  /**
   * Returns true if at least one of its internal Predicates
   * return true; 
   *
   * @param  Object to test. 
   *
   * @return Returns true if at least one of its internal 
   *         Predicates return true.
   */
  public boolean predicate(Object obj) {
    return  _predicate1.predicate(obj) || _predicate2.predicate(obj);      
  }

} /* end class PredOR */

