// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/Interaction.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

//needs-more-work: switch to Vector 
public class Interaction extends ModelElementImpl {
  public Message _message[];
  public Collaboration _context;
  public Link _link[];
  public Instance _instances[];
  
  public Interaction() { }

  public Message[] getMessage() { return _message; }
  public void setMessage(Message[] x) {
     _message = x;
  }

  public Collaboration getContext() { return _context; }
  public void setContext(Collaboration x) {
     _context = x;
  }

  public Link[] getLink() { return _link; }
  public void setLink(Link[] x) {
     _link = x;
  }

  public Instance[] getInstances() { return _instances; }
  public void setInstances(Instance[] x) {
     _instances = x;
  }
  
}
