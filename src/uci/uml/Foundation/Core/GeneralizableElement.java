// Source file: Foundation/Core/GeneralizableElement.java

package uci.uml.Foundation.Core;

import java.util.*;

public interface GeneralizableElement extends Namespace {
  //    public Boolean _isRoot;
  //    public Boolean _isLeaf;
  //    public Boolean _isAbstract;
  //    public Generalization _generalization[];
  //    public Generalization _specialization[];
    
  public Boolean getIsRoot();
  public void setIsRoot(Boolean x);
  
  public Boolean getIsLeaf();
  public void setIsLeaf(Boolean x);
  
  public Boolean getIsAbstract();
  public void setIsAbstract(Boolean x);
  
  public Vector getGeneralization();
  public void setGeneralization(Vector x);
  public void addGeneralization(Generalization x);
  public void removeGeneralization(Generalization x);
  
  public Vector getSpecialization();
  public void setSpecialization(Vector x);
  public void addSpecialization(Generalization x);
  public void removeSpecialization(Generalization x);

}
