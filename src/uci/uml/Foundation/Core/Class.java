// Source file: Foundation/Core/Class.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;

public class Class extends Classifier {
  public Boolean _isActive;
    
  public Class() { }
  public Class(Name name) { super(name); }
  public Class(String nameStr) { super(new Name(nameStr)); }
  
  public Boolean getIsactive(){ return _isActive; }
  public void setIsactive(Boolean x) { _isActive = x; }

}
