package org.argouml.ocl;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import java.util.*;

/**
 * Helper methods for OCL support.
 *
 * @author Steffen Zschaler
 */
public final class OCLUtil extends Object {

  /** OCLUtil shall not be instantiated! */
  private OCLUtil () {}
  
  /**
   * Get the inner-most enclosing namespace for the model element.
   */
  public static MNamespace getInnerMostEnclosingNamespace (MModelElement me) {
    while ((me != null) &&
           (! (me instanceof MNamespace))) {
      me = me.getModelElementContainer();
    }
    
    return (MNamespace) me;
  }

  /**
   * Return a context string for the given model element.
   *
   * @param me the model element for which to create a context string.
   *
   * @return the context string for the model element.
   */
  public static String getContextString (final MModelElement me) {
    MNamespace mnsContext = getInnerMostEnclosingNamespace (me);

    if (me instanceof MBehavioralFeature) {
      StringBuffer sbContext =
          new StringBuffer ("context ")
            .append (mnsContext.getName())
            .append ("::")
            .append (me.getName())
            .append (" (");

      List lParams = ((MBehavioralFeature) me).getParameters();
      String sReturnType = null;
      boolean fFirstParam = true;

      for (Iterator i = lParams.iterator(); i.hasNext();) {
        MParameter mp = (MParameter) i.next();

        switch (mp.getKind().getValue()) {
          case MParameterDirectionKind._RETURN:
            sReturnType = mp.getType().getName();
            break;

          default:
            if (fFirstParam) {
              fFirstParam = false;
            }
            else {
              sbContext.append ("; ");
            }

            sbContext.append (mp.getName())
                     .append (": ")
                     .append (mp.getType().getName());
        }
      }

      sbContext.append (")");

      if (sReturnType != null) {
        sbContext.append (": ").append (sReturnType);
      }

      return sbContext.toString();
    }
    else {
      return "context " + mnsContext.getName();
    }
  }
}