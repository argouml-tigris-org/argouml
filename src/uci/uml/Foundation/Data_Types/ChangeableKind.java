// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.Foundation.Data_Types;

import java.util.*;

public class ChangeableKind implements java.io.Serializable {
  public static final ChangeableKind UNSPEC = new ChangeableKind("unspec");
  public static final ChangeableKind NONE = new ChangeableKind("none");
  public static final ChangeableKind FROZEN = new ChangeableKind("frozen");
  public static final ChangeableKind ADDONLY = new ChangeableKind("add only");

  public static final ChangeableKind[] POSSIBLE_CHANGABILITIES = {
    UNSPEC, NONE, FROZEN, ADDONLY };

  protected String _label;

  public ChangeableKind(String label) { _label = label; }

  public boolean equals(Object o) {
    if (!(o instanceof ChangeableKind)) return false;
    String oLabel = ((ChangeableKind)o)._label;
    return _label.equals(oLabel);
  }

  public int hashCode() { return _label.hashCode(); }

  public String toString() { return _label.toString(); }

  static final long serialVersionUID = 8963424672739686041L;
}
