package uci.uml.ui;

import java.util.*;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class NavInheritance extends NavPerspective {
  public NavInheritance() {
    addSubTreeModel(new GoProjectModel());
    addSubTreeModel(new GoModelToBaseElements());
    addSubTreeModel(new GoGenElementToDerived());
  }

  public String toString() { return "Inheritance"; }
  
} /* end class NavInheritance */
