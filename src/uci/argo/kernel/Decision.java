
package uci.argo.kernel;

public class Decision {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final Decision UNSPEC = new Decision("Uncategorized", 1);
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _name;
  protected int _priority;
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public Decision(String n, int p) {
    setName(n);
    setPriority(p);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public boolean equals(Object d2) {
    if (!(d2 instanceof Decision)) return false;
    return ((Decision)d2).getName().equals(getName());
  }
  
  public String getName() { return _name; }
  public void setName(String n) { _name = n; }
  public int getPriority() { return _priority; }
  public void setPriority(int p) { _priority = p; }

  public String toString() { return getName(); }
  
} /* end class Decision */
