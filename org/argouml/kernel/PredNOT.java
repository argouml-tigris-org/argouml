/*
 * PredNOT.java
 */

package org.argouml.kernel;

import org.tigris.gef.util.*;

/**
 * Predicate to provide logical <B>NOT</B>. The predicate returns 
 * true if its internal <code>Predicate</code> return false;    
 * 
 * @author Eugenio Alvarez
 *
 */
public class PredNOT implements Predicate {

  Predicate _predicate;

  public PredNOT(Predicate predicate) { 
    _predicate = predicate; 
  }

  /**
   * Returns true it initialized predicate
   * returns false;  
   *
   * @param  Object to test. 
   *
   * @return Returns true if its internal
   *         predicate returns false; 
   */
  public boolean predicate(Object obj) {
    return !_predicate.predicate(obj);
  }

} /* end class PredNOT */

