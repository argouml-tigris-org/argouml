// Source file: Foundation/Core/GeneralizableElement.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;

public interface GeneralizableElement extends Namespace {
  //    public Boolean _isRoot;
  //    public Boolean _isLeaf;
  //    public Boolean _isAbstract;
  //    public Generalization _generalization[];
  //    public Generalization _specialization[];
    
  public Boolean getIsRoot();
  public void setIsRoot(Boolean x) throws PropertyVetoException;
  
  public Boolean getIsLeaf();
  public void setIsLeaf(Boolean x) throws PropertyVetoException;
  
  public Boolean getIsAbstract();
  public void setIsAbstract(Boolean x) throws PropertyVetoException;
  
  public Vector getGeneralization();
  public void setGeneralization(Vector x) throws PropertyVetoException;
  public void addGeneralization(Generalization x) throws PropertyVetoException;
  public void removeGeneralization(Generalization x) throws PropertyVetoException;
  
  public Vector getSpecialization();
  public void setSpecialization(Vector x) throws PropertyVetoException;
  public void addSpecialization(Generalization x) throws PropertyVetoException;
  public void removeSpecialization(Generalization x) throws PropertyVetoException;

}
