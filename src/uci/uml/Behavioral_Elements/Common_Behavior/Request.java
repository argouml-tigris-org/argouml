// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/Request.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.*;


public interface Request extends ModelElement {
  //public MessageInstance[] getMessageInstance();
  //public void setMessageInstance(MessageInstance[] x);
  public Vector getAction();
  public void setAction(Vector x);
}
