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

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;
import java.beans.*;
import uci.uml.Foundation.Data_Types.*;

public class ActivityModel extends StateMachine {
  public Vector _partition;

  public ActivityModel() { }
  public ActivityModel(Name name) { super(name); }
  public ActivityModel(String nameStr) { super(new Name(nameStr)); }

  public Vector getPartition() { return _partition; }
  public void setPartition(Vector x) throws PropertyVetoException {
    if (_partition == null) _partition = new Vector();
    fireVetoableChangeNoCompare("partition", _partition, x);
    _partition = x;
  }
  public void addPartition(Partition x) throws PropertyVetoException {
    if (_partition == null) _partition = new Vector();
    fireVetoableChange("partition", _partition, x);
    _partition.addElement(x);
  }
  public void removePartition(Partition x) throws PropertyVetoException {
    if (_partition == null) return;
    fireVetoableChange("partition", _partition, x);
    _partition.removeElement(x);
  }

  static final long serialVersionUID = 6682640444743199356L;
}
