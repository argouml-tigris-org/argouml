/*
 * UMLException.java
 *
 * Created on 16 March 2003, 12:38
 */

package org.argouml.model.uml;

/**
 *
 * @author  Administrator
 */
public class UmlException extends Exception {
  private Throwable cause = null;

  public UmlException() {
    super();
  }

  public UmlException(String message) {
    super(message);
  }

  public UmlException(String message, Throwable cause) {
      super(message);
      this.cause = cause;
  }

  public Throwable getCause() {
    return cause;
  }

  public void printStackTrace() {
    super.printStackTrace();
    if (cause != null) {
      System.out.println("Caused by:");
      cause.printStackTrace();
    }
  }

  public void printStackTrace(java.io.PrintStream ps)
  {
    super.printStackTrace(ps);
    if (cause != null) {
      ps.println("Caused by:");
      cause.printStackTrace(ps);
    }
  }

  public void printStackTrace(java.io.PrintWriter pw)
  {
    super.printStackTrace(pw);
    if (cause != null) {
      pw.println("Caused by:");
      cause.printStackTrace(pw);
    }
  }
}

