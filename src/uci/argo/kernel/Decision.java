
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

  public String getName() { return _name; }
  public void setName(String n) { _name = n; }
  public int getPriority() { return _priority; }
  public void setPriority(int p) { _priority = p; }
  
} /* end class Decision */
